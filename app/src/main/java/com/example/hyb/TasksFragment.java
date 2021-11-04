package com.example.hyb;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hyb.Adapter.TodoAdapter;
import com.example.hyb.Model.Task;

import java.util.ArrayList;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class TasksFragment extends Fragment {

    private String uidKey;
    private FirebaseFirestore db;
    ArrayList<Task> tasks = new ArrayList<Task>();
    Button btnAddTodo;

    public TasksFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uidKey = getArguments().getString("userId");
        db = FirebaseFirestore.getInstance();

        ListView listView = (ListView) view.findViewById(R.id.list);
        btnAddTodo = view.findViewById(R.id.btnAddTodo);

        btnAddTodo.setOnClickListener(view1 -> {
            Intent intent = new Intent(view.getContext(), AddTaskActivity.class);
            view.getContext().startActivity(intent);
        });

        db.collection("todo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            Task task = documentSnapshot.toObject(Task.class);

                            tasks.add(task);

                            TodoAdapter adapter = new TodoAdapter( getActivity(), tasks );
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

    }
}
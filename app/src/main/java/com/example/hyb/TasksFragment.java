package com.example.hyb;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hyb.Adapter.TodoAdapter;
import com.example.hyb.Model.Task;

import java.util.ArrayList;

import com.example.hyb.Model.UserInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class TasksFragment extends Fragment implements TodoAdapter.OnItemClickListener {

    public static final long THIRTY_DAYS = 2592000000L;
    private String uidKey;
    private FirebaseFirestore db;
    ArrayList<Task> tasks = new ArrayList<Task>();
    Button btnAddTodo;
    private ListView listView;
    private TodoAdapter todoAdapter;
    private ProgressBar progressBar;

    public TasksFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        uidKey = getArguments().getString("userId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnAddTodo = view.findViewById(R.id.btnAddTodo);

        btnAddTodo.setOnClickListener(view1 -> {
            Intent intent = new Intent(view.getContext(), AddTaskActivity.class);
            view.getContext().startActivity(intent);
        });

        progressBar = view.findViewById(R.id.progressBar);
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        todoAdapter = new TodoAdapter(this);
        recyclerView.setAdapter(todoAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        DocumentReference docRef = db.collection("users").document(uidKey);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserInfo user = documentSnapshot.toObject(UserInfo.class);
                assert user != null;
                db.collection("todo")
                        .orderBy("created", Query.Direction.DESCENDING)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                tasks.clear();
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    Task task = documentSnapshot.toObject(Task.class);
                                    if (System.currentTimeMillis() - THIRTY_DAYS > task.getCreated() && task.isCompleted()) continue;
                                    if(task.getResidentId().equals(user.getResidentId())) {
                                        tasks.add(task);
                                    }
                                }
                                progressBar.setVisibility(View.GONE);
                                todoAdapter.addTasks(tasks);
                            }
                        });
            }
        });
    }

    @Override
    public void onDeleteTaskClicked(Task task) {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("todo").document(task.getTaskId()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(requireContext(), "Task deleted successfully", Toast.LENGTH_SHORT).show();
                        todoAdapter.removeTask(task);
                        progressBar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onTaskCheckChanged(Task task) {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("todo").document(task.getTaskId()).update("completed", task.isCompleted())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressBar.setVisibility(View.GONE);
                        todoAdapter.onTaskChanged(task);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
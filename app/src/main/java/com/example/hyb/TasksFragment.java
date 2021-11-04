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

public class TasksFragment extends Fragment {

    private String uidKey;

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

        ListView listView = (ListView) view.findViewById(R.id.list);

        // Mock view
        ArrayList<Task> tasks = new ArrayList<Task>();
        Task t = new Task();
        t.setTitle("Title");
        t.setDescription("Task description");
        t.setCompleted(false);

        for (int i =0 ; i< 10; ++i)
            tasks.add(t);

        TodoAdapter adapter = new TodoAdapter( getActivity(), tasks );
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Button btn =  (Button) view.findViewById(R.id.btnAddTodo);

        btn.setOnClickListener(view1 -> {
            Intent intent = new Intent(view.getContext(), AddTaskActivity.class);

            view.getContext().startActivity(intent);
        });
    }
}
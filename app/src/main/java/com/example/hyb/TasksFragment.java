package com.example.hyb;

import android.os.Bundle;

import android.content.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.hyb.Adapter.TodoAdapter;
import com.example.hyb.Model.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TasksFragment extends Fragment {
    private String uidKey; //Nøkkel for å hente bruker

    public TasksFragment() {
        //Tom konstruktør
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

    }
}
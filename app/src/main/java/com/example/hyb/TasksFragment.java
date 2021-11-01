package com.example.hyb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        //ListView list = view.findViewById(R.id.list);

        ListView listView = (ListView) view.findViewById(R.id.list);

        // Mock view
        String[] events = new String[] {
                "Task 11",
                "Task 2",
                "Task 3",
                "Task 4",
                "Task 5"
        };

        List<String> events_list = new ArrayList<String>(Arrays.asList(events));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_expandable_list_item_1, events_list
        );
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        //list.
    }
}
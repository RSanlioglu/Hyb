package com.example.hyb;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardHomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = (ListView) view.findViewById(R.id.listEvents);

        //Mock liste med eventer. Byttes ut med event-objekter senere
        String[] events = new String[] {
                "Event 1",
                "Event 2",
                "Event 3",
                "Event 4",
                "Event 5"
        };

        List<String> events_list = new ArrayList<String>(Arrays.asList(events));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_expandable_list_item_1, events_list
        );

        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();



    }
}
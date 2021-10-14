package com.example.hyb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;

public class SelectJoinOrCreateRoom extends Fragment {
    private String uidKey;

    public SelectJoinOrCreateRoom() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_join_or_create_room, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uidKey = ((LoginRegisterRoomActivity)getActivity()).userUid;

        Button btnJoinRoom = view.findViewById(R.id.btnJoinRoom);
        Button btnCreateRoom = view.findViewById(R.id.btnCreateRoom);

        btnJoinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                SelectJoinOrCreateRoomDirections.ActionSelectJoinOrCreateRoomToJoinRoomFragment action = SelectJoinOrCreateRoomDirections.actionSelectJoinOrCreateRoomToJoinRoomFragment(uidKey);

                action.setUserUid(uidKey);
                navController.navigate(action);

            }
        });

        btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                SelectJoinOrCreateRoomDirections.ActionSelectJoinOrCreateRoomToRegisterRoomFragment action = SelectJoinOrCreateRoomDirections.actionSelectJoinOrCreateRoomToRegisterRoomFragment(uidKey);

                action.setUserUid(uidKey);
                navController.navigate(action);
            }
        });

    }
}
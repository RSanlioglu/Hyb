package com.example.hyb;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.hyb.Adapter.ChatUsersAdapter;
import com.example.hyb.Model.Resident;
import com.example.hyb.Model.UserInfo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ChatDisplayFragment extends Fragment {
    private String uidKey;
    private UserInfo loggedInUser;
    private Resident loggedInUserResident;
    public ArrayList<UserInfo> residentUsers = new ArrayList<>();
    private RecyclerView chatUsersRecyclerView;
    private FirebaseFirestore db;

    public ChatDisplayFragment() {
        //Tom konstruktør
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_display, container, false);

        db = FirebaseFirestore.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uidKey = getArguments().getString("userId");
        getUserInfo(view);
    }
    private void getUserInfo(View view) {
        DocumentReference docRef = db.collection("users").document(uidKey);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                loggedInUser = documentSnapshot.toObject(UserInfo.class);
                getUserResident(loggedInUser.getResidentId(), view);
            }
        });
    }

    private void getUserResident(String residentId, View view) {
        DocumentReference docRef = db.collection("residents").document(residentId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                loggedInUserResident = documentSnapshot.toObject(Resident.class);
                getResidentsUsers(loggedInUserResident.getOccupants(), view);
            }
        });
    }
    private void getResidentsUsers(ArrayList<String> occupants, View view) {
        for (String occ : occupants) {
            if (!occ.equals(uidKey)) {
                DocumentReference docRef = db.collection("users").document(occ);
                docRef.get().addOnSuccessListener(documentSnapshot -> {
                    UserInfo receivedUser = documentSnapshot.toObject(UserInfo.class);
                    residentUsers.add(receivedUser);

                    chatUsersRecyclerView = view.findViewById(R.id.usersRecyclerView);
                    chatUsersRecyclerView.setAdapter(new ChatUsersAdapter(view.getContext(), residentUsers, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = chatUsersRecyclerView.getChildAdapterPosition(v);
                            UserInfo clickedUser = residentUsers.get(position);

                            Intent intent = new Intent(view.getContext(), ChatActivity.class);
                            intent.putExtra("receiverUid", clickedUser.getUid());
                            intent.putExtra("senderUid", uidKey);
                            view.getContext().startActivity(intent);
                        }
                    }));
                    chatUsersRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                });
            }
        }
    }


}
package com.example.hyb;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hyb.Adapter.ChatUsersAdapter;
import com.example.hyb.Model.Chat;
import com.example.hyb.Model.Resident;
import com.example.hyb.Model.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

public class ShowChats extends Fragment {
    private String uidKey;
    private FirebaseFirestore db;
    private RecyclerView chatsRecyclerView;
    private UserInfo loggedInUser;
    private ChatUsersAdapter chatUsersAdapter;
    private List<UserInfo> users;


    private String TAG = "SHOWCHATS";

    public ShowChats() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        db = FirebaseFirestore.getInstance();

        return inflater.inflate(R.layout.fragment_show_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uidKey = getArguments().getString("userId");
        chatsRecyclerView = view.findViewById(R.id.chatsRecyclerView);
        chatsRecyclerView.setHasFixedSize(true);

        readChats(view);
    }

    private void readChats(View view) {
        db.collection("chat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<String> usersId = new ArrayList<>();


                for(QueryDocumentSnapshot document : task.getResult()) {
                    Chat chat = document.toObject(Chat.class);
                    if(chat.getSender().equals(uidKey)) {
                        usersId.add(chat.getReceiver());
                    }
                    if(chat.getReceiver().equals(uidKey)) {
                        usersId.add(chat.getSender());
                    }
                }
                TreeSet<String> usersIdSet = new TreeSet<>(usersId);
                getUsers(usersIdSet, view);
            }
        });
    }

    private void getUsers(TreeSet<String> set, View view) {
        List<UserInfo> users = new ArrayList<>();
        for(String id : set) {
            db.collection("users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserInfo user = documentSnapshot.toObject(UserInfo.class);
                    if(user != null) {
                            users.add(user);
                        }

                        chatsRecyclerView.setAdapter(new ChatUsersAdapter(view.getContext(), users, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int pos = chatsRecyclerView.getChildAdapterPosition(v);
                                UserInfo clickedUser = users.get(pos);

                                Intent intent = new Intent(view.getContext(), ChatActivity.class);
                                intent.putExtra("receiverUid", clickedUser.getUid());
                                intent.putExtra("senderUid", uidKey);
                                view.getContext().startActivity(intent);
                            }
                        }));
                        chatsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                }
            });
        }
    }





}
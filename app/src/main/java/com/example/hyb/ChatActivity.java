package com.example.hyb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hyb.Adapter.MessageAdapter;
import com.example.hyb.Model.Chat;
import com.example.hyb.Model.UserInfo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {
    public final String TAG = "ChatActivity";

    TextView userName;
    FirebaseFirestore db;

    Intent intent;
    String senderUid;
    String receiverUid;
    UserInfo receiverInfo;

    ImageButton btnSend;
    EditText textSend;

    MessageAdapter messageAdapter;
    RecyclerView recyclerView;

    ImageButton backButton;
    ListenerRegistration listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        db = FirebaseFirestore.getInstance();

        intent = getIntent();
        receiverUid = intent.getStringExtra("receiverUid");
        senderUid = intent.getStringExtra("senderUid");

        Toolbar toolbar = findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);

        btnSend = findViewById(R.id.btnSend);
        textSend = findViewById(R.id.txtSend);
        backButton = findViewById(R.id.backFromChat);
        userName = findViewById(R.id.chat_username);

        recyclerView = ChatActivity.this.findViewById(R.id.message_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        getUserInfo();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = textSend.getText().toString();
                if(!msg.equals("")) {
                    sendMessage(senderUid, receiverUid, msg);
                }
                textSend.setText("");
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                listener.remove();
            }
        });

        readMessages(senderUid, receiverUid);
    }

    private void getUserInfo() {
        DocumentReference docRef = db.collection("users").document(receiverUid);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    receiverInfo = documentSnapshot.toObject(UserInfo.class);
                    String fullName = receiverInfo.getFirstName() + " " + receiverInfo.getLastName();

                    userName.setText(fullName);
                }
            });
    }

    private void sendMessage(String sender, String recevier, String message) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        Chat createdChat = new Chat(sender, recevier, message, dtf.format(now), false);
        createdChat.setId(UUID.randomUUID().toString());
        db.collection("chat").document().set(createdChat);
        readMessages(senderUid, receiverUid);
    }

    private void readMessages(String senderUid, String receiverUid) {
        listener = db.collection("chat").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if(e != null) {
                    Log.w(TAG, "Listen failed ", e);
                    return;
                }
                List<Chat> chats = new ArrayList<Chat>();
                for(QueryDocumentSnapshot document:value) {
                    Chat chat = document.toObject(Chat.class);
                    if (chat.getId() == null) {
                        chat.setId(document.getId());
                    }
                    if(chat.getReceiver().equals(senderUid) && chat.getSender().equals(receiverUid) ||
                            chat.getReceiver().equals(receiverUid) && chat.getSender().equals(senderUid)) {
                        chats.add(chat);
                        if(chat.getSender().equals(receiverUid)) {
                            DocumentReference chatRef = db.collection("chat").document(document.getId());
                            chatRef.update("read", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "onSuccess: Chat read");
                                }
                            });
                        }
                        Log.d(TAG, "onComplete: " + chat.getMessage());
                    }
                }

                Collections.sort(chats);

                DocumentReference docRef = db.collection("users").document(receiverUid);
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        receiverInfo = documentSnapshot.toObject(UserInfo.class);
                        String fullName = receiverInfo.getFirstName() + " " + receiverInfo.getLastName();

                        messageAdapter = new MessageAdapter(ChatActivity.this, chats, fullName);
                        recyclerView.setAdapter(messageAdapter);
                    }
                });
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listener.remove();
    }
}
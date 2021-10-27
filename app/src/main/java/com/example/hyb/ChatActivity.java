package com.example.hyb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hyb.Adapter.MessageAdapter;
import com.example.hyb.Model.Chat;
import com.example.hyb.Model.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        db = FirebaseFirestore.getInstance();;

        intent = getIntent();
        receiverUid = intent.getStringExtra("receiverUid");
        senderUid = intent.getStringExtra("senderUid");

        userName = findViewById(R.id.username);
        btnSend = findViewById(R.id.btnSend);
        textSend = findViewById(R.id.txtSend);

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
        HashMap<String, Object> chatMap = new HashMap<>();
        chatMap.put("sender", sender);
        chatMap.put("receiver", recevier);
        chatMap.put("message", message);

        db.collection("chat").document().set(chatMap);
        readMessages(senderUid, receiverUid);
    }

    private void readMessages(String senderUid, String receiverUid) {
        //TODO: TEST OM MELDINGENE BARE HENTES MELLOM WHEREEQUALTO TAGGENE!!!
        db.collection("chat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    List<Chat> chats = new ArrayList<Chat>();
                    for(QueryDocumentSnapshot document:task.getResult()) {
                        Chat chat = document.toObject(Chat.class);
                        if(chat.getReceiver().equals(senderUid) && chat.getSender().equals(receiverUid) ||
                                chat.getReceiver().equals(receiverUid) && chat.getSender().equals(senderUid)) {
                            chats.add(chat);
                            Log.d(TAG, "onComplete: " + chat.getMessage());
                        }
                    }


                    messageAdapter = new MessageAdapter(ChatActivity.this, chats);
                    recyclerView.setAdapter(messageAdapter);
                } else {
                    Log.d(TAG, "onComplete: " + task.getException());
                }
            }
        });

    }


}
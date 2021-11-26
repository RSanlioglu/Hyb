package com.example.hyb.Repo;

import androidx.annotation.NonNull;

import com.example.hyb.Model.Chat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

public class ChatsRepository {
    public static final String CHATS_COLLECTION = "chat";
    public static final String RECEIVER_FIELD = "receiver";
    public static final String READ_FIELD = "read";
    private final CollectionReference collectionReference;

    public ChatsRepository() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore.collection(CHATS_COLLECTION);
    }

    public void getUnreadChatsForUser(String userId, ChatsListener chatsListener) {
        collectionReference.whereEqualTo(RECEIVER_FIELD, userId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Chat> chats = new ArrayList<>();
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot documentSnapshot : documents) {
                    Boolean isRead = documentSnapshot.get(READ_FIELD, Boolean.class);
                    if (isRead == null || !isRead) {
                        Chat chat = documentSnapshot.toObject(Chat.class);
                        if (chat.getId() == null) {
                            chat.setId(documentSnapshot.getId());
                        }
                        chats.add(chat);
                    }
                }
                chatsListener.onSuccess(chats);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                chatsListener.onFailure();
            }
        });
    }

    public void setUpdateChats(List<Chat> chats, RepositoryCallback repositoryCallback) {
        WriteBatch writeBatch = collectionReference.getFirestore().batch();
        for (Chat chat : chats) {
            DocumentReference documentReference = collectionReference.document(chat.getId());
            writeBatch = writeBatch.set(documentReference, chat);
        }
        writeBatch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                repositoryCallback.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                repositoryCallback.onFailure();
            }
        });
    }

    public interface ChatsListener {
        void onSuccess(List<Chat> chats);

        void onFailure();
    }
}

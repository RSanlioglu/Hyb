package com.example.hyb.Repo;

import androidx.annotation.NonNull;

import com.example.hyb.Model.UserInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsersRepository {
    public static final String USERS_COLLECTION = "users";
    public static final String UID_FIELD = "uid";
    private final CollectionReference collectionReference;

    public UsersRepository() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore.collection(USERS_COLLECTION);
    }

    public void getUserInfo(String userId, UserInfoListener userInfoListener) {
        collectionReference.document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserInfo userInfo = documentSnapshot.toObject(UserInfo.class);
                        userInfoListener.onSuccess(Collections.singletonList(userInfo));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                userInfoListener.onFailure();
            }
        });
    }

    public void getUsers(List<String> attendeesList, UserInfoListener userInfoListener) {
        collectionReference.whereIn(UID_FIELD, attendeesList).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<UserInfo> userInfos = new ArrayList<>();
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : documents) {
                            UserInfo userInfo = documentSnapshot.toObject(UserInfo.class);
                            userInfos.add(userInfo);
                        }
                        userInfoListener.onSuccess(userInfos);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                userInfoListener.onFailure();
            }
        });
    }

    public interface UserInfoListener {
        void onSuccess(List<UserInfo> userInfos);

        void onFailure();
    }
}

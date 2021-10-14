package com.example.hyb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hyb.Model.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class loginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "loginActivity";
    public static final String KEY_NAME = "UserInfo";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Check if user is signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Log.d(TAG, "Current user is signed in");
        }
    }

    @Override
    public void onClick(View view) {
        EditText txtEmailInput = findViewById(R.id.editTextEmailAddress);
        EditText txtPasswordInput = findViewById(R.id.editTextPassword);

        mAuth.signInWithEmailAndPassword(txtEmailInput.getText().toString(), txtPasswordInput.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            //Sign in success, update UI with the signed-in users info.
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            assert user != null;
                            getUserInfo(user.getUid());
                        } else {
                            //Sign in fails
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(loginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        Log.d(TAG, txtEmailInput.getText().toString());
        Log.d(TAG, txtPasswordInput.getText().toString());
    }


    private void getUserInfo(String userId) {
        DocumentReference docRef = db.collection("users").document(userId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserInfo userInfo = documentSnapshot.toObject(UserInfo.class);
                if(userInfo.getResidentId() == null) {
                    navigateToJoinCreateRoom(userId);
                } else {
                    navigateToDashboard(userId);
                }
            }
        });
    }

    private void navigateToJoinCreateRoom(String userId) {
        Intent intent = new Intent(this, LoginRegisterRoomActivity.class);
        intent.putExtra(KEY_NAME, userId);
        startActivity(intent);
    }

    private void navigateToDashboard(String userId) {
       Intent intent = new Intent(this, DashboardActivity.class);
       intent.putExtra(KEY_NAME, userId);
       startActivity(intent);
    }
}

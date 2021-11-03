package com.example.hyb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hyb.Model.UserInfo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        View login = findViewById(R.id.loginButton);
        login.setOnClickListener(this);

        View register= findViewById(R.id.registerButton);
        register.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            DocumentReference userRef = db.collection("users").document(mAuth.getUid());
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    UserInfo user = documentSnapshot.toObject(UserInfo.class);
                    if(user.getResidentId() != null) {
                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        intent.putExtra("UserInfo", currentUser.getUid());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), LoginRegisterRoomActivity.class);
                        intent.putExtra("UserInfo", currentUser.getUid());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View arg0){
        if (arg0.getId() == R.id.loginButton) {
            //define a new Intent for the login Activity
            Intent intent = new Intent(this, LoginActivity.class);

            //start the second Activity
            this.startActivity(intent);
        } else if(arg0.getId() == R.id.registerButton) {
            Intent intent = new Intent(this, RegisterActivity.class);

            this.startActivity(intent);
        }

    }

}
package com.example.hyb;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hyb.Model.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private final String TAG = "RegisterActivity";
    public static final String KEY_NAME = "UserInfo";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //TODO: RELOAD UI;
            Log.w(TAG, ": User already logged in");
        }
    }

    public void onClick(View arg0){
        EditText emailInput = findViewById(R.id.editTextEmailRegister);
        EditText passwordInput = findViewById(R.id.editTextPasswordRegister);
        EditText firstNameInput = findViewById(R.id.editTextFirstName);
        EditText lastNameInput = findViewById(R.id.editTextLastName);
        EditText phonenumberInput = findViewById(R.id.editTextPhone);

        String txtEmail = emailInput.getText().toString();
        String txtPassword = passwordInput.getText().toString();
        String txtFirstName = firstNameInput.getText().toString();
        String txtLastName = lastNameInput.getText().toString();
        Integer phoneNumber = Integer.parseInt(phonenumberInput.getText().toString());

        mAuth.createUserWithEmailAndPassword(txtEmail, txtPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            assert user != null;
                            UserInfo userInfo = new UserInfo(user.getUid(), txtFirstName, txtLastName, phoneNumber, null, null);
                            db.collection("users").document(user.getUid()).set(userInfo); //Legger til bruker med bruker info til en ny samling

                            //Ny bruker har ingen rom dem er medlemmer i så dem blir sendt videre til Join/Create
                            navigateToJoinCreateRoom(userInfo.getUid());

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Navigerer bruker til neste skjerm
     */
    private void navigateToJoinCreateRoom(String userId) {
        Intent intent = new Intent(this, LoginRegisterRoomActivity.class);
        intent.putExtra(KEY_NAME, userId);
        startActivity(intent);
    }
}

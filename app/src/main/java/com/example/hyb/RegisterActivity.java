package com.example.hyb;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hyb.Model.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "loginActivity";

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

        //Check if user is signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Log.d(TAG, "Current user is signed in");
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onClick(View view) {
        EditText txtEmailInput = findViewById(R.id.editTextEmailAddressRegister);
        EditText txtPasswordInput = findViewById(R.id.editTextPasswordRegister);
        EditText phoneInput = findViewById(R.id.editTextPhone);
        EditText txtFirstNameInput = findViewById(R.id.editTextFirstName);
        EditText txtLastNameInput = findViewById(R.id.editTextLastName);
        EditText birthDayInput = findViewById(R.id.editTextDateBirthDay);

        Date birthDay = null;

        try {
             birthDay = new SimpleDateFormat("dd//MM/yyyy").parse(birthDayInput.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //TODO: Dato er fortsatt feil
        UserInfo newRegisteredUser = new UserInfo(
                txtFirstNameInput.getText().toString(),
                txtLastNameInput.getText().toString(),
                Integer.parseInt(phoneInput.getText().toString()),
                birthDay,
                null
                );

        mAuth.createUserWithEmailAndPassword(txtEmailInput.getText().toString(), txtPasswordInput.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            db.collection("users")
                                    .document(user.getUid())
                                    .set(newRegisteredUser)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, (CharSequence) e, Toast.LENGTH_SHORT).show();
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}

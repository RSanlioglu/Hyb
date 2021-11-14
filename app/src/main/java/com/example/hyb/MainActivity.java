package com.example.hyb;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.hyb.Model.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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

        isNetworkAvailable();

    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            DocumentReference userRef = db.collection("users").document(mAuth.getUid());
            userRef.get().addOnSuccessListener(documentSnapshot -> {
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
    public void isNetworkAvailable(){

        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if(null!=activeNetwork){

            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){

                Toast.makeText(this, "Wifi Enabled", Toast.LENGTH_SHORT).show();
            }
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){

                Toast.makeText(this, "Data Network Enabled", Toast.LENGTH_SHORT).show();
            }

        }
        else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}
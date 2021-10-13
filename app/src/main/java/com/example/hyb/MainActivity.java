package com.example.hyb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View login = findViewById(R.id.loginButton);
        login.setOnClickListener(this);

        View register= findViewById(R.id.registerButton);
        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View arg0){
        if (arg0.getId() == R.id.loginButton) {
            //define a new Intent for the login Activity
            Intent intent = new Intent(this, loginActivity.class);

            //start the second Activity
            this.startActivity(intent);
        } else if(arg0.getId() == R.id.registerButton) {
            Intent intent = new Intent(this, RegisterActivity.class);

            this.startActivity(intent);
        }

    }

}
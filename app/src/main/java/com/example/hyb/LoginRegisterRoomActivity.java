package com.example.hyb;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LoginRegisterRoomActivity extends AppCompatActivity {
    public String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register_room);
        Intent receivedIntent = getIntent();
        userUid = receivedIntent.getStringExtra(RegisterActivity.KEY_NAME);
    }
}
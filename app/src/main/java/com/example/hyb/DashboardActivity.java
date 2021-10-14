package com.example.hyb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class DashboardActivity extends AppCompatActivity {
    public String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Intent receivedIntent = getIntent();
        userUid = receivedIntent.getStringExtra(RegisterActivity.KEY_NAME);
    }
}
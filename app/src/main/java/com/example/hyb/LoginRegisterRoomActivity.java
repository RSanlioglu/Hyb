package com.example.hyb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.hyb.Model.UserInfo;

public class LoginRegisterRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register_room);

        Intent receivedIntent = getIntent();

        String userinfo = receivedIntent.getStringExtra(RegisterActivity.KEY_NAME);

        TextView test = findViewById(R.id.txtUserUid);
        test.setText(userinfo);
    }
}
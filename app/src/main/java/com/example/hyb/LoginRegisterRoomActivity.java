package com.example.hyb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.hyb.Model.UserInfo;

public class LoginRegisterRoomActivity extends AppCompatActivity {
    public String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register_room);
        Intent receivedIntent = getIntent();
        String userinfo = receivedIntent.getStringExtra(RegisterActivity.KEY_NAME);

        userUid = userinfo;

        TextView test = findViewById(R.id.txtUserUid);
        test.setText(userinfo);
    }
}
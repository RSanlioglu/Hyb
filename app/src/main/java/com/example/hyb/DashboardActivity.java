package com.example.hyb;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DashboardActivity extends AppCompatActivity {
    public static final String KEY_NAME = "UserInfo";
    public String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Intent receivedIntent = getIntent();
        userUid = receivedIntent.getStringExtra(RegisterActivity.KEY_NAME);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new DashboardHomeFragment()).commit();
    }

    private NavigationBarView.OnItemSelectedListener navListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new DashboardHomeFragment();
                    break;
                case R.id.nav_event:
                    selectedFragment = new AddEventFragment();
                    break;
                case R.id.nav_calendar:
                    selectedFragment = new CalendarFragment();
                    break;
                case R.id.nav_shoppList:
                    selectedFragment = new ShoppingListFragment();
                    break;
                case R.id.nav_chat:
                    selectedFragment = new ChatDisplayFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, selectedFragment).commit();

            return true;
        }
    };




}
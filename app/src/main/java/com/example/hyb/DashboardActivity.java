package com.example.hyb;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {
    public static final String KEY_NAME = "UserInfo";
    public String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Intent receivedIntent = getIntent();
        userUid = receivedIntent.getStringExtra(RegisterActivity.KEY_NAME);

        Toolbar toolbar = findViewById(R.id.toolbar_dashboard);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(navListener);

        //The default fragment is the dashboardHomeFragment
        Fragment startFragment = new DashboardHomeFragment();
        Bundle arguments = new Bundle();
        arguments.putString("userId", userUid);
        startFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, startFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_menu_light, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                finish();
                Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.settings:
                Toast.makeText(this, "Settings is selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Listener that handles user input on navigation-bar
     */
    private NavigationBarView.OnItemSelectedListener navListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            Bundle arguments = new Bundle();
            arguments.putString("userId", userUid);

            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new DashboardHomeFragment();
                    selectedFragment.setArguments(arguments);
                    break;
                case R.id.nav_event:
                    selectedFragment = new AddEventFragment();
                    selectedFragment.setArguments(arguments);
                    break;
                case R.id.nav_calendar:
                    selectedFragment = new TasksFragment();
                    selectedFragment.setArguments(arguments);
                    break;
                case R.id.nav_shoppList:
                    selectedFragment = new ShoppingListFragment();
                    selectedFragment.setArguments(arguments);
                    break;
                case R.id.nav_chat:
                    selectedFragment = new ChatDisplayFragment();
                    selectedFragment.setArguments(arguments);
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, selectedFragment).commit();

            return true;
        }
    };






}
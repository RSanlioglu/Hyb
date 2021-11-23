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

import com.example.hyb.HybSettingsActivity.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {
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
                Intent intentLogout = new Intent(this, MainActivity.class);
                intentLogout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intentLogout);
                finish();
                Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.settings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                intentSettings.putExtra("userUid", userUid);
                intentSettings.addFlags((Intent.FLAG_ACTIVITY_NEW_TASK));
                getApplicationContext().startActivity(intentSettings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Listener that handles user input on navigation-bar
     */
    private NavigationBarView.OnItemSelectedListener navListener = new NavigationBarView.OnItemSelectedListener() {
        Fragment selectedFragment = new DashboardHomeFragment();

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Bundle arguments = new Bundle();
            arguments.putString("userId", userUid);
            switch (item.getItemId()) {
                case R.id.nav_home:
                    if(!(selectedFragment instanceof DashboardHomeFragment)) {
                        selectedFragment = new DashboardHomeFragment();
                        selectedFragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, selectedFragment).commit();
                    }
                    break;
                case R.id.nav_event:
                    if(!(selectedFragment instanceof  AddEventFragment)) {
                        selectedFragment = new AddEventFragment();
                        selectedFragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, selectedFragment).commit();
                    }
                    break;
                case R.id.nav_task:
                    if(!(selectedFragment instanceof TasksFragment)) {
                        selectedFragment = new TasksFragment();
                        selectedFragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, selectedFragment).commit();
                    }
                    break;
                case R.id.nav_shoppList:
                    if(!(selectedFragment instanceof ShoppingListFragment)) {
                        selectedFragment = new ShoppingListFragment();
                        selectedFragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, selectedFragment).commit();
                    }
                    break;
                case R.id.nav_chat:
                    if(!(selectedFragment instanceof ChatDisplayFragment)) {
                        selectedFragment = new ChatDisplayFragment();
                        selectedFragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, selectedFragment).commit();
                    }
                    break;
            }
            return true;
        }
    };






}
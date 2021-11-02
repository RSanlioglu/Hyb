package com.example.hyb.HybSettingsActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.hyb.MainActivity;
import com.example.hyb.R;
import com.example.hyb.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {
    public String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Intent receivedIntent = getIntent();
        userUid = receivedIntent.getStringExtra("userUid");

        Toolbar toolbar = findViewById(R.id.toolbar_dashboard);
        setSupportActionBar(toolbar);

        Fragment startFragment = new SettingsOperationFragment();
        Bundle arguments = new Bundle();
        arguments.putString("userUid", userUid);
        startFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView2, startFragment).commit();
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
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
package com.example.hyb;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuCompat;
import androidx.fragment.app.Fragment;

import com.example.hyb.Model.Resident;
import com.example.hyb.Model.UserInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class DashboardActivity extends AppCompatActivity {
    public String userUid;
    public FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Intent receivedIntent = getIntent();
        userUid = receivedIntent.getStringExtra(RegisterActivity.KEY_NAME);
        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar_dashboard);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(navListener);

        //The default fragment is the dashboardHomeFragment
        Fragment startFragment = new DashboardHomeFragment();

        int intentFragment = getIntent().getExtras().getInt("frgmnt");
        if (intentFragment == 1) {
            Bundle args = new Bundle();
            args.putString("userId", userUid);
            startFragment = new TasksFragment();
            startFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, startFragment).commit();
            bottomNav.setSelectedItemId(R.id.nav_task);
        }
        Bundle arguments = new Bundle();
        arguments.putString("userId", userUid);
        startFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, startFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_menu_light, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
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
            case R.id.leave:
                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                builder.setMessage("This will remove you from the resident. If you are the only member of this resident the resident will also be deleted").setTitle("Are you sure?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        leaveResident();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Nothing happens if they cancel
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
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

    private void leaveResident() {
        //Henter brukeren
        DocumentReference userRef = db.collection("users").document(userUid);
        userRef.get().addOnSuccessListener(userSnapshot -> {
            UserInfo user = userSnapshot.toObject(UserInfo.class);
            String residentId = user.getResidentId();
            //Setter resident null p?? brukeren som forlater
            userRef.update("residentId", null).addOnSuccessListener(s -> {
                //Henter resident og fjerner brukeren som occupant
                DocumentReference residentRef = db.collection("residents").document(residentId);
                residentRef.update("occupants", FieldValue.arrayRemove(userUid)).addOnSuccessListener(r -> {
                    //Henter oppdatert resident
                    DocumentReference updatedResident = db.collection("residents").document(residentId);
                    updatedResident.get().addOnSuccessListener(residentSnapshot -> {
                        Resident resident = residentSnapshot.toObject(Resident.class);
                        if(resident.getOccupants().size() <= 0) {
                            db.collection("residents").document(residentId).delete().addOnSuccessListener(unused -> navigateOut());
                        } else if(resident.getHost().equals(userUid)) {
                            //If the host leaves a random new host is selected
                            Random r1 = new Random();
                            int randomNum = r1.nextInt(resident.getOccupants().size());
                            updatedResident.update("host", resident.getOccupants().get(randomNum)).addOnSuccessListener(unused -> navigateOut());
                        } else {
                            navigateOut();
                        }
                    });

                });
            });
        });
    }

    private void navigateOut() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("UserInfo", userUid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        getBaseContext().startActivity(intent);
        finishAffinity(); //Android 4.1 or higher
        Toast.makeText(getBaseContext(), "You successfully left the resident", Toast.LENGTH_SHORT).show();
    }
}
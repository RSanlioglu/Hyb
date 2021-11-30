package com.example.hyb;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.hyb.Model.Resident;
import com.example.hyb.Model.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class SettingsOperationFragment extends Fragment {
    private static final String TAG = "SettingsOperationFragment";

    public String uidKey;
    public Button btnChangeUserInfo;
    public Button btnSignOut;
    public Button btnDeleteAccount;
    public ImageView btnBackToDasboard;

    private FirebaseFirestore db;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public SettingsOperationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        SettingsActivity activity = (SettingsActivity) getActivity();
        db = FirebaseFirestore.getInstance();
        uidKey = activity.getUserUid();

        return inflater.inflate(R.layout.fragment_settings_operation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnChangeUserInfo = view.findViewById(R.id.btnChangeUserInfo);
        btnSignOut = view.findViewById(R.id.btnSignOut);
        btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount);
        btnBackToDasboard = view.findViewById(R.id.imgBackToSettings);

        //OnClickListener for change userInfo button
        btnChangeUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(view);
                SettingsOperationFragmentDirections.ActionSettingsOperationFragmentToChangeUserInfoFragment action = SettingsOperationFragmentDirections.actionSettingsOperationFragmentToChangeUserInfoFragment(uidKey);
                action.setUserUid(uidKey);
                controller.navigate(action);

            }
        });


        //OnClickListener for sign-out button
        btnSignOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intentLogout = new Intent(getContext(), MainActivity.class);
            intentLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);  //Under android 4.1
            getContext().startActivity(intentLogout);
            getActivity().finishAffinity(); //Android 4.1 or higher
            Toast.makeText(getContext(), "Signed out", Toast.LENGTH_SHORT).show();
        });

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("This will delete you from resident and also your messages. This operation is not revertible!").setTitle("Are you sure?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccount();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Nothing happens if they cancel
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnBackToDasboard.setOnClickListener(v -> {
            getActivity().finish();
        });
    }

    private void leaveResident() {
        //Henter brukeren
        DocumentReference userRef = db.collection("users").document(uidKey);
        userRef.get().addOnSuccessListener(userSnapshot -> {
            UserInfo user = userSnapshot.toObject(UserInfo.class);
            String residentId = user.getResidentId();
            //Setter resident null på brukeren som forlater
            userRef.update("residentId", null).addOnSuccessListener(s -> {
                //Henter resident og fjerner brukeren som occupant
              DocumentReference residentRef = db.collection("residents").document(residentId);
              residentRef.update("occupants", FieldValue.arrayRemove(uidKey)).addOnSuccessListener(r -> {
                  //Henter oppdatert resident
                  DocumentReference updatedResident = db.collection("residents").document(residentId);
                  updatedResident.get().addOnSuccessListener(residentSnapshot -> {
                      Resident resident = residentSnapshot.toObject(Resident.class);
                      if(resident.getOccupants().size() <= 0) {
                          db.collection("residents").document(residentId).delete().addOnSuccessListener(unused -> navigateOut());
                      } else if(resident.getHost().equals(uidKey)) {
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
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("UserInfo", uidKey);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
        getActivity().finishAffinity(); //Android 4.1 or higher
        Toast.makeText(getContext(), "You successfully left the resident", Toast.LENGTH_SHORT).show();
    }

    private void deleteAccount() {
        //Fjerner bruker fra resident
        DocumentReference userRef = db.collection("users").document(uidKey);
        userRef.get().addOnSuccessListener(userSnapshot -> {
            UserInfo user = userSnapshot.toObject(UserInfo.class);
            String residentId = user.getResidentId();
            //Setter resident null på brukeren som forlater
            userRef.update("residentId", null).addOnSuccessListener(s -> {
                //Henter resident og fjerner brukeren som occupant
                DocumentReference residentRef = db.collection("residents").document(residentId);
                residentRef.update("occupants", FieldValue.arrayRemove(uidKey)).addOnSuccessListener(r -> {
                    //Henter oppdatert resident
                    DocumentReference updatedResident = db.collection("residents").document(residentId);
                    updatedResident.get().addOnSuccessListener(residentSnapshot -> {
                        Resident resident = residentSnapshot.toObject(Resident.class);
                        if(resident.getOccupants().size() <= 0) {
                            db.collection("residents").document(residentId).delete().addOnSuccessListener(unused -> navigateOut());
                        } else if(resident.getHost().equals(uidKey)) {
                            //If the host leaves a random new host is selected
                            Random r1 = new Random();
                            int randomNum = r1.nextInt(resident.getOccupants().size());
                            updatedResident.update("host", resident.getOccupants().get(randomNum)).addOnSuccessListener(unused -> navigateOut());
                        } else {

                            //Sletter bruker fra firestore
                            userRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "Userinfo successfully deleted");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error deleting userinfo", e);
                                }
                            });
                            firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    navigateOut();
                                }
                            });
                        }
                    });

                });
            });
        });
    }
}
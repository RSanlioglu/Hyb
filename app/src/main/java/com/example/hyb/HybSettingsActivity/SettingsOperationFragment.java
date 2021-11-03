package com.example.hyb.HybSettingsActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hyb.LoginRegisterRoomActivity;
import com.example.hyb.MainActivity;
import com.example.hyb.Model.Resident;
import com.example.hyb.Model.UserInfo;
import com.example.hyb.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsOperationFragment extends Fragment {
    public String uidKey;  //TODO: Finn en løsning for å hente dette
    public Button btnLeaveResident;
    public Button btnChangeUserInfo;
    public Button btnChangePassword;
    public Button btnSignOut;
    public Button btnDeleteAccount;
    public ImageView btnBackToDasboard;

    private FirebaseFirestore db;

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

        btnLeaveResident = view.findViewById(R.id.btnLeaveResident);
        btnChangeUserInfo = view.findViewById(R.id.btnChangeUserInfo);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnSignOut = view.findViewById(R.id.btnSignOut);
        btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount);
        btnBackToDasboard = view.findViewById(R.id.imgBackToSettings);

        //OnClickListener for leave resident button
        btnLeaveResident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
            }
        });

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

        //OnClickListener for change password button
        btnChangePassword.setOnClickListener(v -> Toast.makeText(getContext(), "Not yet implemented", Toast.LENGTH_SHORT).show());

        //OnClickListener for sign-out button
        btnSignOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intentLogout = new Intent(getContext(), MainActivity.class);
            intentLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);  //Under android 4.1
            getContext().startActivity(intentLogout);
            getActivity().finishAffinity(); //Android 4.1 or higher
            Toast.makeText(getContext(), "Signed out", Toast.LENGTH_SHORT).show();
        });

        btnDeleteAccount.setOnClickListener(v -> Toast.makeText(getContext(), "Not yet implemented", Toast.LENGTH_SHORT).show());

        btnBackToDasboard.setOnClickListener(v -> {
            getActivity().finish();
        });
    }

    private void leaveResident() {
        //Henter brukeren
        DocumentReference userRef = db.collection("users").document(uidKey);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            UserInfo user = documentSnapshot.toObject(UserInfo.class);
            String residentId = user.getResidentId();
            userRef.update("residentId", null).addOnSuccessListener(s -> {
              DocumentReference residentRef = db.collection("residents").document(residentId);
              residentRef.update("occupants", FieldValue.arrayRemove(uidKey)).addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void unused) {
                      Intent intent = new Intent(getContext(), LoginRegisterRoomActivity.class);
                      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                      getContext().startActivity(intent);
                      getActivity().finishAffinity();
                      Toast.makeText(getContext(), "You left the resident", Toast.LENGTH_SHORT).show();
                  }
              });
            });
        });
    }

}
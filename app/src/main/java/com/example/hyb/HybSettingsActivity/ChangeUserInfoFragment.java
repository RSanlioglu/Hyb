package com.example.hyb.HybSettingsActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hyb.DashboardActivity;
import com.example.hyb.Model.UserInfo;
import com.example.hyb.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangeUserInfoFragment extends Fragment {
    private String uidKey;
    public ImageView btnBackToSettings;
    public EditText txtFirstName;
    public EditText txtLastName;
    public Button btnUpdate;
    FirebaseFirestore db;

    public ChangeUserInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        db = FirebaseFirestore.getInstance();
        return inflater.inflate(R.layout.fragment_change_user_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if(arguments != null) {
            uidKey = ChangeUserInfoFragmentArgs.fromBundle(arguments).getUserUid();
        }

        updateUserInfo();

        btnBackToSettings = view.findViewById(R.id.imgBackToSettings);
        txtFirstName = view.findViewById(R.id.txtFirstName);
        txtLastName = view.findViewById(R.id.txtLastName);
        btnUpdate = view.findViewById(R.id.btnUpdate);

        btnBackToSettings.setOnClickListener(v -> {
            NavController controller = Navigation.findNavController(view);
            ChangeUserInfoFragmentDirections.ActionChangeUserInfoFragmentToSettingsOperationFragment action = ChangeUserInfoFragmentDirections.actionChangeUserInfoFragmentToSettingsOperationFragment(uidKey);
            action.setUidKey(uidKey);
            controller.navigate(action);
        });

    }

    private void updateUserInfo() {
        //Get the user to be updated
        DocumentReference docRef = db.collection("users").document(uidKey);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            UserInfo user = documentSnapshot.toObject(UserInfo.class);
            if(user != null) {
                String firstName = user.getFirstName();
                String lastName = user.getLastName();
                txtFirstName.setText(firstName);
                txtLastName.setText(lastName);
            }

            btnUpdate.setOnClickListener(v -> {
                String updatedFirstName = txtFirstName.getText().toString();
                String updatedLastName = txtLastName.getText().toString();
                DocumentReference docRef1 = db.collection("users").document(uidKey);
                docRef1.update("firstName", updatedFirstName).addOnSuccessListener(f -> docRef1.update("lastName", updatedLastName).addOnSuccessListener(l -> {
                    Intent intent = new Intent(getContext(), DashboardActivity.class);
                    intent.putExtra("UserInfo", uidKey);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getContext().startActivity(intent);
                    getActivity().finishAffinity();
                }));
            });
        });
    }
}
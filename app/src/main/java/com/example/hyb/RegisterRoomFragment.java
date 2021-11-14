package com.example.hyb;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.hyb.Model.Resident;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;


public class RegisterRoomFragment extends Fragment {
    // for debugging purpose
    private final String TAG = "RegisterResidentFragment";
    private final String ERROR_MESSAGE = "Resident with this name already exists!";
    //declare Variables
    private FirebaseFirestore db;
    private ArrayList<String> OccupantsList;
    public static final String KEY_NAME = "UserInfo";

    private String uidKey;
    public RegisterRoomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_room, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // userKey which is used to find room host
        assert getArguments() != null;
        uidKey = getArguments().getString("userUid");


        Log.d(TAG, "onViewCreated: " +  uidKey);

        //initial variables for input components
        TextView txtOutput = view.findViewById(R.id.txtOutput);
        EditText residentNameInput = view.findViewById(R.id.ResidentName);
        EditText residentAddressInput = view.findViewById(R.id.ResidentAddress);
        EditText residentCityInput = view.findViewById(R.id.ResidentCity);
        EditText residentCountryInput = view.findViewById(R.id.ResidentCountry);

        Button btnBack = view.findViewById(R.id.btnBackFromRegister);
        btnBack.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            RegisterRoomFragmentDirections.ActionRegisterRoomFragmentToSelectJoinOrCreateRoom action = RegisterRoomFragmentDirections.actionRegisterRoomFragmentToSelectJoinOrCreateRoom(uidKey);
            action.setUserUid(uidKey);
            navController.navigate(action);
        });

        // initializing button
        Button btnCreateHousing = view.findViewById(R.id.btnCreateHousing);
        // OnClickListener
        btnCreateHousing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Convert input dataTypes to String values
                String residentName = residentNameInput.getText().toString();
                String residentAddress = residentAddressInput.getText().toString();
                String residentCity = residentCityInput.getText().toString();
                String residentCountry = residentCountryInput.getText().toString();


                DocumentReference docRef = db.collection("residents").document(residentName);
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot residentNameFirebase = task.getResult();
                        //Create resident if the another resident already does not exists with the same
                        assert residentNameFirebase != null;
                        if ( !residentNameFirebase.exists()){


                            // initial arraylist of Occupants, the user that creates new housing is only one in the arraylist.
                            OccupantsList = new ArrayList<>();
                            OccupantsList.add(uidKey);
                            // Create new resident
                            Resident resident = new Resident(residentAddress,residentCity,residentCountry,uidKey,OccupantsList);

                            //add new resident using set() and check if it is successful, then navigate to dashboard
                            db.collection("residents").document(residentName).set(resident)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");

                                        DocumentReference userRef = db.collection("users").document(uidKey);
                                        userRef.update("residentId", residentName).addOnSuccessListener(success -> navigateToDashboard(uidKey));
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });
                        }
                        // Resident with this name already exists!!
                        else {
                            txtOutput.setText(ERROR_MESSAGE);
                            Log.d(TAG, ERROR_MESSAGE);
                        }

                    }
                    else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }

                });
            }

            private void navigateToDashboard(String userId) {
                Intent intent = new Intent(RegisterRoomFragment.this.getActivity(), DashboardActivity.class);
                intent.putExtra(KEY_NAME, userId);
                startActivity(intent);
            }
        });
    }
}
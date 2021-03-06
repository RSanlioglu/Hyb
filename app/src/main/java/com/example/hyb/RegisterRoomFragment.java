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
import android.widget.Toast;
import com.example.hyb.Model.Resident;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;


public class RegisterRoomFragment extends Fragment {
    // for debugging purpose
    private final String TAG = "RegisterResidentFragment";
    private final String ERROR_MESSAGE = "Resident with this name already exists!";
    private final String ERROR_MESSAGE_empty_fields = "Please fill all required fields!";
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
        EditText residentNameInput = view.findViewById(R.id.ResidentName);
        EditText residentAddressInput = view.findViewById(R.id.ResidentAddress);
        EditText residentCityInput = view.findViewById(R.id.ResidentCity);
        EditText residentCountryInput = view.findViewById(R.id.ResidentCountry);


        // button back to register/log in
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
                if (residentName.isEmpty() || residentAddress.isEmpty() || residentCity.isEmpty() || residentCountry.isEmpty()){
                    Toast.makeText(v.getContext(), ERROR_MESSAGE_empty_fields, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick: "+ residentName);
                    Log.d(TAG, "onClick: "+ residentAddress);

                }
                else if (residentName.length() > 17 || residentCountry.length() > 15 || residentCity.length() > 15 || residentAddress.length()>25){
                    Toast.makeText(v.getContext(), "To many characters!!", Toast.LENGTH_SHORT).show();
                }

                else{
                DocumentReference docRef = db.collection("residents").document(residentName);
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot residentNameFirebase = task.getResult();
                        //Create resident if the another resident already does not exists with the same
                        assert residentNameFirebase != null;

                        // Resident with this name already exists!!
                        if ( residentNameFirebase.exists()){
                            Toast.makeText(v.getContext(), ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                        }

                        else {
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
                                    .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
                        }

                    }
                    else {
                        Log.d(TAG, "get failed with ", task.getException());
                        Toast.makeText(v.getContext(), "Error!! can not receive data from database", Toast.LENGTH_SHORT).show();
                    }

                });
                    }
            }

            private void navigateToDashboard(String userId) {
                Intent intent = new Intent(RegisterRoomFragment.this.getActivity(), DashboardActivity.class);
                intent.putExtra(KEY_NAME, userId);
                startActivity(intent);
            }
        });
    }
}
package com.example.hyb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class JoinRoomFragment extends Fragment {
    private final String TAG = "Join-Room-Fragment";
    private final String ERROR_MESSAGE = "No resident with such key does exist";
    private final String NULL_MESSAGE = "Please write a resident-id";

    public static final String KEY_NAME = "UserInfo";

    private FirebaseFirestore db;
    private String uidKey;

    public JoinRoomFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_join_room, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        uidKey = getArguments().getString("userUid");

        Button btnBack = view.findViewById(R.id.btnBack);
        Button btnJoin = view.findViewById(R.id.btnJoin);

        btnBack.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);

            JoinRoomFragmentDirections.ActionJoinRoomFragmentToSelectJoinOrCreateRoom action = JoinRoomFragmentDirections.actionJoinRoomFragmentToSelectJoinOrCreateRoom(uidKey);
            action.setUserUid(uidKey);
            navController.navigate(action);
        });

        btnJoin.setOnClickListener(v -> {
            TextView txtOutput = view.findViewById(R.id.txtOutput);
            EditText residentKeyInput = view.findViewById(R.id.txtResidentID);
            String residentKey = residentKeyInput.getText().toString();

            if(residentKey.equals("")) {
                txtOutput.setText(NULL_MESSAGE);
            } else {
                DocumentReference resRef = db.collection("residents").document(residentKey);
                resRef.get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if(doc.exists()) {
                            // changes  residentKey for user
                            DocumentReference userRef = db.collection("users").document(uidKey);
                            userRef.update("residentId", residentKey);
                            resRef.update("occupants", FieldValue.arrayUnion(uidKey));
                            navigateToDashboard(uidKey);
                        } else {

                            txtOutput.setText(ERROR_MESSAGE);
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                });
            }
        });
    }
    private void navigateToDashboard(String userId) {
        Intent intent = new Intent(JoinRoomFragment.this.getActivity(), DashboardActivity.class);
        intent.putExtra(KEY_NAME, userId);
        startActivity(intent);
    }
}
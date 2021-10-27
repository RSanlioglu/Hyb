package com.example.hyb;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hyb.Model.Event;
import com.example.hyb.Model.UserInfo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardHomeFragment extends Fragment {

    private FirebaseFirestore db;  //Instans av firestore for å hente brukerinfo
    private String uidKey; //Nøkkel for å hente bruker
    private String residentKey; //Nøkkel for å hente brukers resident
    private ArrayList<String> residentEvents;

    public DashboardHomeFragment() {
        //Tom konstruktør
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uidKey = getArguments().getString("userId");
        updateUi(view);
    }

    /**
     * Function for updating user-interface after user is sendt here from register/login
     * @param view - View from onViewCreated function
     */
    private void updateUi(View view) {
        TextView userInitials = view.findViewById(R.id.userInitials);
        TextView userFullName = view.findViewById(R.id.userFullName);
        TextView userJoinedResident = view.findViewById(R.id.txtJoinedResident);

        DocumentReference userRef = db.collection("users").document(uidKey);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            //User is retrieved successful
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserInfo user = documentSnapshot.toObject(UserInfo.class);
                //use this to filter events just for current users resident
                residentKey = user.getResidentId();
                String fullName = user.getFirstName() + " " + user.getLastName();
                String initials = getInitials(fullName);
                userInitials.setText(initials);
                userFullName.setText(fullName);
                userJoinedResident.setText(user.getResidentId());
            }
        });



        // Retrieve multiple Events using QuerySnapshot and filter them based on the current users residentKey
        db.collection("events").whereEqualTo("eventResident",residentKey)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            Event event = documentSnapshot.toObject(Event.class);
                            String title = event.getEventTitle();
                            //add relevant events title to arraylist
                            residentEvents.add(title);
                        }

                    }
                });
        // TODO: 27/10/2021 show residentEvents in recycler view with mak 5 events.



        ListView listView = (ListView) view.findViewById(R.id.listEvents);

        //Mock liste med eventer. Byttes ut med event-objekter senere
        String[] events = new String[] {
                "Event 1",
                "Event 2",
                "Event 3",
                "Event 4",
                "Event 5"
        };

        List<String> events_list = new ArrayList<String>(Arrays.asList(events));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_expandable_list_item_1, events_list
        );
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    /**
     * Private function for getting initials from users full name
     * @param fullName - String of full name
     * @return Initials
     */
    private String getInitials(String fullName) {
        int idxLastWhiteSpace = fullName.lastIndexOf(' ');

        return fullName.substring(0,1) + fullName.substring(idxLastWhiteSpace + 1, idxLastWhiteSpace + 2);
    }




    private ArrayList<String> loadResidentEvent(View view){


        return residentEvents;
    }

}
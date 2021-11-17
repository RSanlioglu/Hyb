package com.example.hyb;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hyb.Model.Event;
import com.example.hyb.Model.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class AddEventFragment extends Fragment {
    private Calendar calendar;
    private final String TAG = "RegisterResidentFragment";
    private final String ERROR_MESSAGE = "Please fill all required fields!";
    private FirebaseFirestore db;
    private String eventStartTime;
    private String eventEndTime;
    private String uidKey;  //Nøkkel for å hente bruker
    private ArrayList<String> attendeesList;
    private String residentID;
    public AddEventFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_event, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //user key
        uidKey = getArguments().getString("userId");
        //initial buttons
        Button btnSelectStartTime = view.findViewById(R.id.selectStartTimeBtn);
        Button btnSelectEndTime = view.findViewById(R.id.selectEndTimeBtn);
        Button btnCreateEvent = view.findViewById(R.id.btnCreateEvent);
        //initial EditText

        TextView eventStart = view.findViewById(R.id.eventStartTime);
        TextView eventEnd = view.findViewById(R.id.eventEndTime);
        EditText eventTitleInput = view.findViewById(R.id.EventTitle);
        EditText eventLocationInput = view.findViewById(R.id.EventLocation);
        EditText eventDescriptionInput = view.findViewById(R.id.EventDescription);

        // onClickListener for select start time button
        btnSelectStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                calendar.set(Calendar.MINUTE,minute);

                                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd HH:mm");
                                eventStartTime =  simpleDateFormat.format(calendar.getTime());
                                eventStart.setText(eventStartTime);
                            }
                        };

                        new TimePickerDialog(getActivity(),timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
                    }
                };
                new DatePickerDialog(getActivity(),dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }

        });
        // onClickListener for select start time button
        btnSelectEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                calendar.set(Calendar.MINUTE,minute);

                                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd HH:mm");
                                eventEndTime = simpleDateFormat.format(calendar.getTime());
                                eventEnd.setText(eventEndTime);
                            }
                        };
                        new TimePickerDialog(getActivity(),timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
                    }
                };
                new DatePickerDialog(getActivity(),dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // onClickListener for create event button
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Convert input dataTypes to String values
                String eventTitle = eventTitleInput.getText().toString();
                String eventLocation = eventLocationInput.getText().toString();
                String eventDescription = eventDescriptionInput.getText().toString();

                // check if all fields are filed
                if (!eventTitle.isEmpty() && !eventLocation.isEmpty() && !eventDescription.isEmpty()){

                    // get users residentID
                    DocumentReference userRef = db.collection("users").document(uidKey);
                    userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                        @Override
                        //User is retrieved successful
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            UserInfo user = documentSnapshot.toObject(UserInfo.class);
                            residentID = user.getResidentId();

                            // initial arraylist of Attendees, the user that creates new event is only one in the arraylist.
                            attendeesList = new ArrayList<String>();
                            attendeesList.add(uidKey);

                            // create event object
                            Event event = new Event(eventTitle,eventLocation,eventDescription,eventStartTime,eventEndTime,residentID,attendeesList);

                            //add new event using set() and check if it is successful
                            db.collection("events").document().set(event)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                            Fragment startFragment = new DashboardHomeFragment();
                                            Bundle arguments = new Bundle();
                                            arguments.putString("userId", uidKey);
                                            startFragment.setArguments(arguments);
                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, startFragment).commit();
                                            Toast.makeText(v.getContext(), eventTitle+ " Created", Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });
                        }
                    });
                }
                // please fil all required fields
                else{
                    Log.d(TAG, ERROR_MESSAGE);

                }
                    }
                });
            }

    }

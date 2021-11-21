package com.example.hyb;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.hyb.Model.Event;
import com.example.hyb.Repo.EventsRepository;
import com.example.hyb.Repo.RepositoryCallback;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;

public class CreateEventDialog extends DialogFragment {
    public static final String UID = "uid";
    public static final String RES_ID = "resId";
    private EventsRepository eventsRepository;
    private String userId;
    private String resId;
    private EditText inputEventTitle;
    private EditText inputEventLocation;
    private EditText inputEventDescription;
    private EditText inputEventStartDate;
    private EditText inputEventEndDate;
    private ProgressBar progressBar;
    private Button btnCreateEvent;
    private CreateEventDialogListener listener;

    public static CreateEventDialog getInstance(String userId, String residentId) {
        // Create fragment and set the arguments
        Bundle bundle = new Bundle();
        bundle.putString(UID, userId);
        bundle.putString(RES_ID, residentId);
        CreateEventDialog createEventDialog = new CreateEventDialog();
        createEventDialog.setArguments(bundle);
        return createEventDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventsRepository = new EventsRepository();
        if (savedInstanceState != null) {
            // Fragment is being restored
            // So let's get args from saved stated
            userId = savedInstanceState.getString(UID);
            resId = savedInstanceState.getString(RES_ID);
        } else {
            // It is a new fragment so let's get args from set args bundle.
            userId = getArguments().getString(UID);
            resId = getArguments().getString(RES_ID);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.dialog_create_event, null, false);
        progressBar = view.findViewById(R.id.progressBar4);
        inputEventTitle = view.findViewById(R.id.txt_input_event_title);
        inputEventLocation = view.findViewById(R.id.txt_input_event_location);
        inputEventDescription = view.findViewById(R.id.txt_input_event_description);
        inputEventStartDate = view.findViewById(R.id.txt_input_start_date);
        inputEventStartDate.setOnClickListener(v -> onStartDateClicked());
        inputEventEndDate = view.findViewById(R.id.txt_input_end_date);
        inputEventEndDate.setOnClickListener(v -> onEndDateClicked());
        btnCreateEvent = view.findViewById(R.id.btnCreateEvent);
        btnCreateEvent.setOnClickListener(v -> onCreateEventClicked());
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(view);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    private void onStartDateClicked() {
        setDateAndTime(inputEventStartDate);
    }

    private void onEndDateClicked() {
        setDateAndTime(inputEventEndDate);
    }

    private void setDateAndTime(EditText dateField) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener= (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            TimePickerDialog.OnTimeSetListener timeSetListener= (view1, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd HH:mm");
                String eventEndTime = simpleDateFormat.format(calendar.getTime());
                dateField.setText(eventEndTime);
            };
            new TimePickerDialog(getActivity(),timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),false).show();
        };
        new DatePickerDialog(getActivity(), dateSetListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void onCreateEventClicked() {
        String title = inputEventTitle.getText().toString();
        String description = inputEventDescription.getText().toString();
        String location = inputEventLocation.getText().toString();
        String startDate = inputEventStartDate.getText().toString();
        String endDate = inputEventEndDate.getText().toString();
        if(title.isEmpty() || description.isEmpty() || location.isEmpty()
                || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.all_fields_are_required), Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressIndicator();
        Event event = new Event(title, location, description, startDate,
                endDate, resId, Collections.singletonList(userId));

        eventsRepository.saveEvent(event, new RepositoryCallback() {
            @Override
            public void onSuccess() {
                showMessage(getString(R.string.event_created_successfully));
                if(listener != null) listener.onEventCreated();
                dismiss();
            }

            @Override
            public void onFailure() {
                showMessage(getString(R.string.error_occurred_creating_event));
                dismiss();
            }
        });
    }

    private void showProgressIndicator() {
        inputEventTitle.setVisibility(View.INVISIBLE);
        inputEventDescription.setVisibility(View.INVISIBLE);
        inputEventLocation.setVisibility(View.INVISIBLE);
        inputEventEndDate.setVisibility(View.INVISIBLE);
        inputEventStartDate.setVisibility(View.INVISIBLE);
        btnCreateEvent.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(UID, userId);
        outState.putString(RES_ID, resId);
        super.onSaveInstanceState(outState);
    }

    public void setListener(CreateEventDialogListener listener) {
        this.listener = listener;
    }

    public interface CreateEventDialogListener {
        void onEventCreated();
    }
}

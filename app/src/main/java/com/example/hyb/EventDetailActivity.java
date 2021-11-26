package com.example.hyb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hyb.Adapter.UserInfoAdapter;
import com.example.hyb.Model.Event;
import com.example.hyb.Model.UserInfo;
import com.example.hyb.Repo.EventsRepository;
import com.example.hyb.Repo.UsersRepository;

import java.util.List;

public class EventDetailActivity extends AppCompatActivity {

    public static final String EVENT_ID = "eventId";
    private String eventId;
    private EventsRepository eventsRepository;
    private TextView title;
    private TextView location;
    private TextView description;
    private ProgressBar progressBar;
    private UsersRepository usersRepository;
    private UserInfoAdapter userInfoAdpter;
    private RecyclerView attendeesList;

    public static void launch(Context context, String eventId) {
        Intent intent = new Intent(context, EventDetailActivity.class);
        intent.putExtra(EVENT_ID, eventId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        eventsRepository = new EventsRepository();
        usersRepository = new UsersRepository();
        if (savedInstanceState != null) {
            eventId = savedInstanceState.getString(EVENT_ID);
        } else {
            eventId = getIntent().getStringExtra(EVENT_ID);
        }

        title = findViewById(R.id.txt_event_title);
        location = findViewById(R.id.txt_event_location);
        description = findViewById(R.id.txt_event_description);
        attendeesList = findViewById(R.id.listview_event_attendees);
        attendeesList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        attendeesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        userInfoAdpter = new UserInfoAdapter();
        attendeesList.setAdapter(userInfoAdpter);
        progressBar = findViewById(R.id.progressBar5);
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideViews();
        showProgressIndicator();
        eventsRepository.getEventById(eventId, new EventsRepository.EventsListener(){
            @Override
            public void onSuccess(List<Event> events) {
                Event event = events.get(0);
                showEvent(event);
                if (!event.getAttendeesList().isEmpty()) {
                    getEventAttendes(event.getAttendeesList());
                }
            }

            @Override
            public void onFailure() {
                showMessage(getString(R.string.an_error_occurred));
                hideProgressIndicator();
            }
        });
    }

    private void showEvent(Event event) {
        hideProgressIndicator();
        showViews();
        title.setText(event.getEventTitle());
        location.setText(event.getEventLocation());
        description.setText(event.getEventDescription());
    }

    private void getEventAttendes(List<String> attendeesList) {
        usersRepository.getUsers(attendeesList, new UsersRepository.UserInfoListener(){
            @Override
            public void onSuccess(List<UserInfo> userInfos) {
                userInfoAdpter.addData(userInfos);
            }

            @Override
            public void onFailure() {
                showMessage(getString(R.string.an_error_occurred));
            }
        });
    }

    private void showProgressIndicator() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressIndicator() {
        progressBar.setVisibility(View.GONE);
    }

    private void hideViews() {
        title.setVisibility(View.INVISIBLE);
        location.setVisibility(View.INVISIBLE);
        description.setVisibility(View.INVISIBLE);
        attendeesList.setVisibility(View.INVISIBLE);
    }

    private void showViews() {
        title.setVisibility(View.VISIBLE);
        location.setVisibility(View.VISIBLE);
        description.setVisibility(View.VISIBLE);
        attendeesList.setVisibility(View.VISIBLE);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(EVENT_ID, eventId);
        super.onSaveInstanceState(outState);
    }
}
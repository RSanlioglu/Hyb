package com.example.hyb;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hyb.Adapter.DetailedEventAdapter;
import com.example.hyb.Model.Event;
import com.example.hyb.Model.UserInfo;
import com.example.hyb.Repo.EventsRepository;
import com.example.hyb.Repo.RepositoryCallback;
import com.example.hyb.Repo.UsersRepository;

import java.util.List;


public class AddEventFragment extends Fragment implements DetailedEventAdapter.OnItemClickListener, CreateEventDialog.CreateEventDialogListener {
    private final String TAG = "RegisterResidentFragment";
    private final String ERROR_MESSAGE = "Please fill all required fields!";
    private String uidKey;  //Nøkkel for å hente bruker
    private DetailedEventAdapter detailedEventAdapter;
    private final UsersRepository usersRepository;
    private UserInfo userInfo;
    private final EventsRepository eventsRepository;
    private ProgressBar progressBar;
    private View rootView;

    public AddEventFragment() {
        usersRepository = new UsersRepository();
        eventsRepository = new EventsRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        uidKey = getArguments().getString("userId");
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);
        progressBar = view.findViewById(R.id.progressBar6);
        rootView = view.findViewById(R.id.root_layout);
        RecyclerView eventsList = view.findViewById(R.id.rv_event_list);
        eventsList.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        eventsList.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true));
        detailedEventAdapter = new DetailedEventAdapter(this);
        eventsList.setAdapter(detailedEventAdapter);
        Button btnCreate = view.findViewById(R.id.btn_add_event);
        btnCreate.setOnClickListener(v -> onCreateEventClicked());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData() {
        hideViews();
        showProgressIndicator();
        usersRepository.getUserInfo(uidKey, new UsersRepository.UserInfoListener() {
            @Override
            public void onSuccess(List<UserInfo> userInfos) {
                AddEventFragment.this.userInfo = userInfos.get(0);
                loadEvents(userInfo);
                showViews();
            }

            @Override
            public void onFailure() {
                showMessage(getString(R.string.an_error_occurred));
                hideProgressIndicator();
            }
        });
    }

    private void loadEvents(UserInfo userInfo) {
        eventsRepository.getEvents(userInfo.getResidentId(), new EventsRepository.EventsListener() {
            @Override
            public void onSuccess(List<Event> events) {
                detailedEventAdapter.setCurrentUserId(userInfo.getUid());
                detailedEventAdapter.addEvents(events);
                hideProgressIndicator();
            }

            @Override
            public void onFailure() {
                showMessage(getString(R.string.an_error_occurred));
                hideProgressIndicator();
            }
        });
    }

    private void onCreateEventClicked() {
        CreateEventDialog createEventDialog = CreateEventDialog.getInstance(
                userInfo.getUid(), userInfo.getResidentId());
        createEventDialog.setListener(this);
        createEventDialog.show(getParentFragmentManager(), null);
    }

    @Override
    public void onSeeDetailsClicked(Event event) {
        EventDetailActivity.launch(requireContext(), event.getId());
    }

    @Override
    public void onAttendClicked(Event event) {
        event.getAttendeesList().add(userInfo.getUid());
        showProgressIndicator();
        eventsRepository.updateEvent(event, new RepositoryCallback(){
            @Override
            public void onSuccess() {
                detailedEventAdapter.updateEvent(event);
                showMessage(getString(R.string.attendance_registration_successful));
                hideProgressIndicator();
            }

            @Override
            public void onFailure() {
                showMessage(getString(R.string.an_error_occurred));
                hideProgressIndicator();
            }
        });
    }

    @Override
    public void onDeleteClicked(Event event) {
        showProgressIndicator();
        eventsRepository.deleteEvent(event, new RepositoryCallback() {
            @Override
            public void onSuccess() {
                hideProgressIndicator();
                detailedEventAdapter.removeEvent(event);
                showMessage(getString(R.string.event_deleted_successfully));
            }

            @Override
            public void onFailure() {
                hideProgressIndicator();
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
        rootView.setVisibility(View.INVISIBLE);
    }

    private void showViews() {
        rootView.setVisibility(View.VISIBLE);
    }

    private void showMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventCreated() {
        loadData();
    }
}

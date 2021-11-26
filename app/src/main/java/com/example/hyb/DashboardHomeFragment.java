package com.example.hyb;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hyb.Adapter.EventAdapter;
import com.example.hyb.Adapter.NotificationsAdapter;
import com.example.hyb.Model.Chat;
import com.example.hyb.Model.Event;
import com.example.hyb.Model.Notification;
import com.example.hyb.Model.ShoppingItem;
import com.example.hyb.Model.Task;
import com.example.hyb.Model.UserInfo;
import com.example.hyb.Repo.ChatsRepository;
import com.example.hyb.Repo.EventsRepository;
import com.example.hyb.Repo.RepositoryCallback;
import com.example.hyb.Repo.ShoppingItemsRepository;
import com.example.hyb.Repo.TasksRepository;
import com.example.hyb.Repo.UsersRepository;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DashboardHomeFragment extends Fragment implements NotificationsAdapter.OnItemClickListener, EventAdapter.OnItemClickListener {

    private FirebaseFirestore db;  //Instans av firestore for å hente brukerinfo
    private String uidKey; //Nøkkel for å hente bruker
    private String residentKey; //Nøkkel for å hente brukers resident
    private ArrayList<String> residentEvents;
    private TextView userInitials;
    private TextView userFullName;
    private TextView userJoinedResident;
    private ChatsRepository chatsRepository;
    private EventsRepository eventsRepository;
    private ShoppingItemsRepository shoppingItemsRepository;
    private NotificationsAdapter notificationsAdapter;
    private UsersRepository usersRepository;
    private EventAdapter eventAdapter;
    private CircularProgressIndicator progressIndicator;
    private TextView txtUpcomingEventsTitle;
    private TasksRepository tasksRepository;
    private List<Chat> unreadChats;

    public DashboardHomeFragment() {
        //Tom konstruktør
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatsRepository = new ChatsRepository();
        eventsRepository = new EventsRepository();
        shoppingItemsRepository = new ShoppingItemsRepository();
        usersRepository = new UsersRepository();
        tasksRepository = new TasksRepository();
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
        txtUpcomingEventsTitle = view.findViewById(R.id.txt_upcoming_events);
        userInitials = view.findViewById(R.id.userInitials);
        userFullName = view.findViewById(R.id.userFullName);
        userJoinedResident = view.findViewById(R.id.txtJoinedResident);
        uidKey = getArguments().getString("userId");

        RecyclerView notificationsList = view.findViewById(R.id.rv_notifications);
        notificationsList.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        notificationsList.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        notificationsAdapter = new NotificationsAdapter(this);
        notificationsList.setAdapter(notificationsAdapter);

        RecyclerView eventsList = view.findViewById(R.id.listEvents);
        eventsList.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        eventsList.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true));
        eventAdapter = new EventAdapter(this);
        eventsList.setAdapter(eventAdapter);

        progressIndicator = view.findViewById(R.id.progressBar3);
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

    @Override
    public void onStart() {
        super.onStart();
        showProgressIndicator();
        notificationsAdapter.clearData();
        usersRepository.getUserInfo(uidKey, new UsersRepository.UserInfoListener() {
            @Override
            public void onSuccess(List<UserInfo> userInfos) {
                //use this to filter events just for current users resident
                UserInfo userInfo = userInfos.get(0);
                residentKey = userInfo.getResidentId();
                String fullName = userInfo.getFirstName() + " " + userInfo.getLastName();
                String initials = getInitials(fullName);
                userInitials.setText(initials);
                userFullName.setText(fullName);
                userJoinedResident.setText(userInfo.getResidentId());
                fetchEvents();
                fetchUnreadChats();
                fetchShoppingItems();
                fetchTasks();
            }

            @Override
            public void onFailure() {
                showErrorMessage();
            }
        });
    }

    private void fetchTasks() {
        tasksRepository.getTasks(residentKey, new TasksRepository.TasksListener() {
            @Override
            public void onSuccess(List<Task> tasks) {
                if (!tasks.isEmpty()) {
                    Notification notification = new Notification();
                    notification.setTitle(getString(R.string.you_have_new_tasks_to_do));
                    notification.setType(Notification.Type.TASKS);
                    notificationsAdapter.addNotification(notification);
                    hideProgressIndicator();
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void fetchShoppingItems() {
        shoppingItemsRepository.getShoppingItems(residentKey, new ShoppingItemsRepository.ShoppingItemsListener() {
            @Override
            public void onSuccess(List<ShoppingItem> shoppingItems) {
                if (!shoppingItems.isEmpty()) {
                    Notification notification = new Notification();
                    notification.setTitle(getString(R.string.you_have_new_items_to_buy));
                    notification.setType(Notification.Type.SHOPPING);
                    notificationsAdapter.addNotification(notification);
                    hideProgressIndicator();
                }
            }

            @Override
            public void onFailure() {
                showErrorMessage();
                hideProgressIndicator();
            }
        });
    }

    private void fetchUnreadChats() {
        chatsRepository.getUnreadChatsForUser(uidKey, new ChatsRepository.ChatsListener() {
            @Override
            public void onSuccess(List<Chat> chats) {
                unreadChats = chats;
                if (!chats.isEmpty()) {
                    Notification notification = new Notification();
                    notification.setTitle(getString(R.string.you_have_unread_messages));
                    notification.setType(Notification.Type.CHATS);
                    notificationsAdapter.addNotification(notification);
                    hideProgressIndicator();
                }
            }

            @Override
            public void onFailure() {
                showErrorMessage();
                hideProgressIndicator();
            }
        });
    }

    private void fetchEvents() {
        eventsRepository.getEvents(residentKey, new EventsRepository.EventsListener() {
            @Override
            public void onSuccess(List<Event> events) {
                if (!events.isEmpty()) {
                    txtUpcomingEventsTitle.setVisibility(View.VISIBLE);
                    eventAdapter.addData(events);
                    hideProgressIndicator();
                }
            }

            @Override
            public void onFailure() {
                showErrorMessage();
                hideProgressIndicator();
            }
        });
    }

    @Override
    public void onNotificationCancelClicked(Notification item, int position) {
        switch (item.getType()) {
            case CHATS:
                updateChatsAsRead();
                break;
            case TASKS:
                // Implement what to do when tasks notification is closed
                break;
            case SHOPPING:
                // Implement what to do when shopping notification is closed
                break;
        }
        notificationsAdapter.removeNotification(position);
    }

    private void updateChatsAsRead() {
        for (Chat chat : unreadChats) {
            chat.setRead(true);
        }
        chatsRepository.setUpdateChats(unreadChats, new RepositoryCallback() {
            @Override
            public void onSuccess() {
                // No-op
            }

            @Override
            public void onFailure() {
                showErrorMessage();
            }
        });
    }

    @Override
    public void onEventClicked(Event model) {
        // Not implemented
    }

    @Override
    public void onCloseClicked(int adapterPosition) {
        eventAdapter.removeItem(adapterPosition);
    }

    private void showProgressIndicator() {
        txtUpcomingEventsTitle.setVisibility(View.INVISIBLE);
        progressIndicator.show();
    }

    private void hideProgressIndicator() {
        progressIndicator.hide();
    }

    private void showErrorMessage() {
        Toast.makeText(requireContext(), getString(R.string.an_error_occurred), Toast.LENGTH_SHORT).show();
    }
}
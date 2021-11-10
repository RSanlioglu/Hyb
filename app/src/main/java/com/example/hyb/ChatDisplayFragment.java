package com.example.hyb;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hyb.Adapter.ChatUsersAdapter;
import com.example.hyb.Adapter.ViewPagerAdapter;
import com.example.hyb.Model.Resident;
import com.example.hyb.Model.UserInfo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ChatDisplayFragment extends Fragment {
    private String uidKey;
    private UserInfo loggedInUser;
    private Resident loggedInUserResident;
    public ArrayList<UserInfo> residentUsers = new ArrayList<>();
    private RecyclerView chatUsersRecyclerView;
    private FirebaseFirestore db;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private static String TAG = "CHATDISPLAYFRAGMENT";

    public ChatDisplayFragment() {
        //Tom konstruktør
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_display, container, false);

        db = FirebaseFirestore.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uidKey = getArguments().getString("userId");

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getParentFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        //TODO: Legg til flere fragmenter

        Bundle args = new Bundle();
        args.putString("userId", uidKey);

        Fragment showUsersForChatFragment = new ShowUsersForChatFragment();
        showUsersForChatFragment.setArguments(args);
        viewPagerAdapter.addFragment(showUsersForChatFragment, "USERS");

        Fragment showChats = new ShowChats();
        showChats.setArguments(args);
        viewPagerAdapter.addFragment(showChats, "CHATS");

        viewPager.setAdapter(viewPagerAdapter);

    }

}
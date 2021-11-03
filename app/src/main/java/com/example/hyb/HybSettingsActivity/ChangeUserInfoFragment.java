package com.example.hyb.HybSettingsActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hyb.R;

public class ChangeUserInfoFragment extends Fragment {
    String uidKey;
    public ImageView btnBackToSettings;

    public ChangeUserInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_user_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if(arguments != null) {
            uidKey = ChangeUserInfoFragmentArgs.fromBundle(arguments).getUserUid();
        }

        Toast.makeText(getContext(), uidKey, Toast.LENGTH_SHORT).show();

        btnBackToSettings = view.findViewById(R.id.imgBackToSettings);

        btnBackToSettings.setOnClickListener(v -> {
            NavController controller = Navigation.findNavController(view);
            ChangeUserInfoFragmentDirections.ActionChangeUserInfoFragmentToSettingsOperationFragment action = ChangeUserInfoFragmentDirections.actionChangeUserInfoFragmentToSettingsOperationFragment(uidKey);
            action.setUidKey(uidKey);
            controller.navigate(action);
        });
    }
}
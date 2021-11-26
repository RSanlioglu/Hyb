package com.example.hyb.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hyb.Model.UserInfo;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.ViewHolder> {
    private final List<UserInfo> list;

    public UserInfoAdapter() {
        this.list = new ArrayList<>();
    }

    public void addData(List<UserInfo> items) {
        list.clear();
        list.addAll(items);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView userName;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(android.R.id.text1);
        }

        public void bind(final UserInfo model) {
            userName.setText(String.format("%s %s", model.getFirstName(), model.getLastName()));
        }
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserInfo item = list.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
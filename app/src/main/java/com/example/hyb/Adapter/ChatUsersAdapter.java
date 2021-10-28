package com.example.hyb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hyb.Model.UserInfo;
import com.example.hyb.R;

import java.util.List;

public class ChatUsersAdapter extends RecyclerView.Adapter<ChatUsersAdapter.ChatUsersViewHolder> {
    private List<UserInfo> data;
    private LayoutInflater mInflater;
    private View.OnClickListener clickListener;

    public ChatUsersAdapter(Context ctx, List<UserInfo> data, View.OnClickListener clickListener) {
        this.data = data;
        this.mInflater = LayoutInflater.from(ctx);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ChatUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.users_item, parent, false);
        return new ChatUsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatUsersViewHolder holder, int position) {
        UserInfo currentUserInfo = data.get(position);
        holder.bind(currentUserInfo, clickListener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ChatUsersViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView usersInitials;

        public ChatUsersViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.txtUserName);
            usersInitials = itemView.findViewById(R.id.chatUserInitials);
        }

        public void bind(UserInfo currentUserInfo, View.OnClickListener clickListener) {
            String fullName = currentUserInfo.getFirstName() + " " + currentUserInfo.getLastName();
            String initials = getInitials(fullName);
            userName.setText(fullName);
            usersInitials.setText(initials);
            this.itemView.setOnClickListener(clickListener);
        }
    }

    private String getInitials(String fullName) {
        int idxLastWhiteSpace = fullName.lastIndexOf(' ');
        return fullName.substring(0,1) + fullName.substring(idxLastWhiteSpace + 1, idxLastWhiteSpace + 2);
    }
}

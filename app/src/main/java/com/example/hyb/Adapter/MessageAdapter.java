package com.example.hyb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hyb.ChatActivity;
import com.example.hyb.Model.Chat;
import com.example.hyb.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private List<Chat> messages;
    private Context ctx;
    private LayoutInflater mInflater;

    FirebaseUser user;

    public MessageAdapter(Context ctx, List<Chat> messages) {
        this.messages = messages;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(ctx).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat = messages.get(position);

        //holder.usersInitials.setImageResource(R.mipmap.ic_launcher);
        holder.showMessage.setText(chat.getMessage());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView showMessage;
        ImageView usersInitials;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            showMessage = itemView.findViewById(R.id.showMessage);
            usersInitials = itemView.findViewById(R.id.profile_initials);
        }

/*        public void bind(UserInfo currentUserInfo, View.OnClickListener clickListener) {
            String fullName = currentUserInfo.getFirstName() + " " + currentUserInfo.getLastName();
            userName.setText(fullName);

            this.itemView.setOnClickListener(clickListener);
        }*/
    }

    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(messages.get(position).getSender().equals(user.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}

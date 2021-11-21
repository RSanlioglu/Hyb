package com.example.hyb.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hyb.Model.Event;
import com.example.hyb.R;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DetailedEventAdapter extends RecyclerView.Adapter<DetailedEventAdapter.ViewHolder> {
    private final List<Event> list;
    private final OnItemClickListener onItemClickListener;
    private String currentUserId;

    public DetailedEventAdapter(OnItemClickListener onItemClickListener) {
        this.list = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    public void addEvents(List<Event> items) {
        list.clear();
        list.addAll(items);
        notifyDataSetChanged();
    }

    public void removeEvent(Event event) {
        int index = list.indexOf(event);
        if (index != -1) {
            list.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void updateEvent(Event event) {
        int index = list.indexOf(event);
        if (index >= 0) {
            notifyItemChanged(index);
        }
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView startDate;
        private final TextView endDate;
        private final TextView seeDetails;
        private final TextView attend;
        private final ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_event_title);
            startDate = itemView.findViewById(R.id.txt_start_date);
            endDate = itemView.findViewById(R.id.txt_end_date);
            seeDetails = itemView.findViewById(R.id.txt_see_details);
            attend = itemView.findViewById(R.id.txt_attend);
            delete = itemView.findViewById(R.id.img_delete);
        }

        public void bind(final Event event, final OnItemClickListener listener, String currentUserId) {
            title.setText(event.getEventTitle());
            startDate.setText(event.getEventStartTime());
            endDate.setText(event.getEventEndTime());
            seeDetails.setOnClickListener(v -> listener.onSeeDetailsClicked(event));
            if (event.getAttendeesList().contains(currentUserId)) {
                attend.setVisibility(View.INVISIBLE);
            } else {
                attend.setVisibility(View.VISIBLE);
                attend.setOnClickListener(v -> listener.onAttendClicked(event));
            }
            delete.setOnClickListener(v -> listener.onDeleteClicked(event));
        }

    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_large, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event item = list.get(position);
        holder.bind(item, onItemClickListener, currentUserId);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {

        void onSeeDetailsClicked(Event model);

        void onAttendClicked(Event model);

        void onDeleteClicked(Event model);
    }
}
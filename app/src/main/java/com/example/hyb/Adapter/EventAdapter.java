package com.example.hyb.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hyb.Model.Event;
import com.example.hyb.R;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private final List<Event> list;
    private final OnItemClickListener onItemClickListener;

    public EventAdapter(OnItemClickListener onItemClickListener) {
        this.list = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    public void addData(List<Event> items) {
        list.clear();
        list.addAll(items);
        notifyDataSetChanged();
    }

    public void removeItem(int adapterPosition) {
        list.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final View close;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txt_event_title);
            close = itemView.findViewById(R.id.view_close);
        }

        public void bind(final Event model, final OnItemClickListener listener) {
            textView.setText(model.getEventTitle());
            itemView.setOnClickListener(v -> listener.onEventClicked(model));
            close.setOnClickListener(v -> listener.onCloseClicked(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event item = list.get(position);
        holder.bind(item, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onEventClicked(Event model);

        void onCloseClicked(int adapterPosition);
    }
}
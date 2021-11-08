package com.example.hyb.Adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hyb.Model.Task;
import com.example.hyb.R;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private final List<Task> list;
    private final OnItemClickListener onItemClickListener;

    public TodoAdapter(OnItemClickListener onItemClickListener) {
        this.list = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    public void addTasks(List<Task> items) {
        list.clear();
        list.addAll(items);
        notifyDataSetChanged();
    }

    public void removeTask(Task task) {
        int index = list.indexOf(task);
        if (index != -1) {
            list.remove(task);
            notifyItemRemoved(index);
        }
    }

    public void onTaskChanged(Task task) {
        int index = list.indexOf(task);
        if (index != -1) {
            notifyItemChanged(index);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox checkBox;
        private final TextView txtTitle;
        private final TextView txtDescription;
        private final ImageButton delete;
        private final ConstraintLayout constraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox2);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtDescription = itemView.findViewById(R.id.txt_description);
            delete = itemView.findViewById(R.id.btn_delete);
            constraintLayout = itemView.findViewById(R.id.root_layout);
        }

        public void bind(final Task model, final OnItemClickListener listener) {
            checkBox.setChecked(model.isCompleted());
            txtTitle.setText(model.getTitle());
            txtDescription.setText(model.getDescription());
            if (model.isCompleted()) {
                txtTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                txtDescription.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                constraintLayout.setBackgroundColor(Color.LTGRAY);
            } else {
                txtTitle.setPaintFlags(txtTitle.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                txtDescription.setPaintFlags(txtDescription.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                constraintLayout.setBackgroundColor(Color.WHITE);
            }
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteTaskClicked(model);
                }
            });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    model.setCompleted(isChecked);
                    listener.onTaskCheckChanged(model);
                }
            });
        }

    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task item = list.get(position);
        holder.bind(item, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onDeleteTaskClicked(Task item);

        void onTaskCheckChanged(Task model);
    }
}
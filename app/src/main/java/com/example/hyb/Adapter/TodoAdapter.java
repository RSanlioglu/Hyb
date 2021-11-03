package com.example.hyb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.hyb.Model.Task;
import com.example.hyb.R;

import java.util.ArrayList;

public class TodoAdapter extends BaseAdapter {

    private Context ctx;
    ArrayList<Task> tasks;
    private LayoutInflater mInflater;

    public TodoAdapter(Context ctx, ArrayList<Task> tasks){
        this.ctx = ctx;
        this.tasks = tasks;
        this.mInflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        view = mInflater.inflate(R.layout.activity_list, null);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox2);

        EditText editText = (EditText) view.findViewById(R.id.editTextTextMultiLine);

        checkBox.setChecked(tasks.get(i).isCompleted());
        checkBox.setText(tasks.get(i).getTitle());

        editText.setText(tasks.get(i).getDescription());
        return view;
    }
}

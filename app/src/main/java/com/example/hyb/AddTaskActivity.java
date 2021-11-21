package com.example.hyb;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hyb.Repo.RepositoryCallback;
import com.example.hyb.Repo.TasksRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.example.hyb.Model.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.UUID;

public class AddTaskActivity extends AppCompatActivity {
    public final String TAG = "AddTaskActivity";
    Button btnAdd;
    EditText textTitle;
    EditText textDescription;
    private ProgressBar progressBar;
    private TasksRepository tasksRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        btnAdd =  findViewById(R.id.buttonAddTask);
        textTitle = findViewById(R.id.textInputTaskName);
        textDescription = findViewById(R.id.textInputTaskDescription);
        progressBar = findViewById(R.id.progressBar2);
        btnAdd.setOnClickListener(view -> onAddTaskClicked());
        tasksRepository = new TasksRepository();
    }

    private void onAddTaskClicked() {
        String title = textTitle.getText().toString();
        String description = textDescription.getText().toString();

        if (title.isEmpty() || description.isEmpty())
        {
            Toast.makeText(this,"Title and description should not be empty  ", Toast.LENGTH_SHORT).show();
            return;
        }

        Task t = new Task();
        t.setDescription(description);
        t.setTitle(title);
        t.setCompleted(false);
        t.setCreated(System.currentTimeMillis());
        progressBar.setVisibility(View.VISIBLE);
        tasksRepository.saveTask(t, new RepositoryCallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
                finish();
            }

            @Override
            public void onFailure() {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AddTaskActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

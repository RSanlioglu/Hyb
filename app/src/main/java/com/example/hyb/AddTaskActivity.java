package com.example.hyb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hyb.Model.Task;
import com.example.hyb.Model.UserInfo;
import com.example.hyb.Repo.RepositoryCallback;
import com.example.hyb.Repo.TasksRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddTaskActivity extends AppCompatActivity {
    public final String TAG = "AddTaskActivity";
    Button btnAdd;
    EditText textTitle;
    EditText textDescription;
    private ProgressBar progressBar;
    private TasksRepository tasksRepository;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

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
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    private void onAddTaskClicked() {
        String title = textTitle.getText().toString();
        String description = textDescription.getText().toString();

        if (title.isEmpty() || description.isEmpty())
        {
            Toast.makeText(this,"Title and description should not be empty  ", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference ref = db.collection("users").document(auth.getCurrentUser().getUid());
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserInfo user = documentSnapshot.toObject(UserInfo.class);

                Task t = new Task();
                t.setDescription(description);
                t.setTitle(title);
                t.setCompleted(false);
                t.setCreated(System.currentTimeMillis());
                t.setResidentId(user.getResidentId());
                progressBar.setVisibility(View.VISIBLE);
                tasksRepository.saveTask(t, new RepositoryCallback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                        finish();
                        Intent intentSettings = new Intent(getBaseContext(), DashboardActivity.class);
                        intentSettings.putExtra("UserInfo", auth.getCurrentUser().getUid());
                        intentSettings.putExtra("frgmnt", 1);
                        intentSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getBaseContext().startActivity(intentSettings);
                    }

                    @Override
                    public void onFailure() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AddTaskActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

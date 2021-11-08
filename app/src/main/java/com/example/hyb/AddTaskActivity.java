package com.example.hyb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.example.hyb.Model.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.UUID;

public class AddTaskActivity extends AppCompatActivity {
    public final String TAG = "AddTaskActivity";
    FirebaseFirestore db;

    Button btnAdd;
    EditText textTitle;
    EditText textDescription;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();

        setContentView(R.layout.activity_add_task);

        btnAdd =  findViewById(R.id.buttonAddTask);
        textTitle = findViewById(R.id.textInputTaskName);
        textDescription = findViewById(R.id.textInputTaskDescription);
        progressBar = findViewById(R.id.progressBar2);

        btnAdd.setOnClickListener(view -> {
            String title = textTitle.getText().toString();
            String description = textDescription.getText().toString();

            if (title.isEmpty() || description.isEmpty())
            {
                Toast.makeText(this,"Title and description should not be empty  ", Toast.LENGTH_SHORT).show();
                return;
            }

            Task t = new Task();

            t.setTaskId(UUID.randomUUID().toString());
            t.setDescription(description);
            t.setTitle(title);
            t.setCompleted(false);
            t.setCreated(System.currentTimeMillis());
            progressBar.setVisibility(View.VISIBLE);
            db.collection("todo").document(t.getTaskId()).set(t)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressBar.setVisibility(View.GONE);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AddTaskActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                }
            });


        });

    }
}

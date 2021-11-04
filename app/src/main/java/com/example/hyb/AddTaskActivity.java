package com.example.hyb;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();

        setContentView(R.layout.activity_add_task);

        btnAdd =  findViewById(R.id.buttonAddTask);
        textTitle = findViewById(R.id.textInputTaskName);
        textDescription = findViewById(R.id.textInputTaskDescription);

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

            db.collection("todo").document(t.getTaskId()).set(t).addOnSuccessListener(runnable -> {});



        });

    }
}

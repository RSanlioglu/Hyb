package com.example.hyb.Repo;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.hyb.AddTaskActivity;
import com.example.hyb.Model.Task;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TasksRepository {
    public static final long THIRTY_DAYS = 2592000000L;
    public static final String TODO = "todo";
    public static final String CREATED_FIELD = "created";
    public static final String COMPLETED_FIELD = "completed";
    private final CollectionReference collectionReference;

    public TasksRepository() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore.collection(TODO);
    }

    public void getTasks(TasksListener tasksListener) {
        collectionReference.orderBy(CREATED_FIELD, Query.Direction.DESCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Task> tasks = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Task task = documentSnapshot.toObject(Task.class);
                            if (System.currentTimeMillis() - THIRTY_DAYS > task.getCreated() && task.isCompleted()) continue;
                            tasks.add(task);
                        }

                        if (tasksListener != null) {
                            tasksListener.onSuccess(tasks);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                tasksListener.onFailure();
            }
        });
    }

    public void saveTask(Task task, RepositoryCallback repositoryCallback) {
        task.setTaskId(UUID.randomUUID().toString());
        collectionReference.document(task.getTaskId()).set(task)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        repositoryCallback.onSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                repositoryCallback.onFailure();
            }
        });
    }

    public void deleteTask(Task task, RepositoryCallback repositoryCallback) {
        collectionReference.document(task.getTaskId()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        repositoryCallback.onSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                repositoryCallback.onFailure();
            }
        });
    }

    public void updateTaskState(Task task, RepositoryCallback repositoryCallback) {
        collectionReference.document(task.getTaskId()).update(COMPLETED_FIELD, task.isCompleted())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        repositoryCallback.onSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                repositoryCallback.onFailure();
            }
        });
    }

    public interface TasksListener {
        void onSuccess(List<Task> tasks);

        void onFailure();
    }
}

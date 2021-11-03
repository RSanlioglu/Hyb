package com.example.hyb.Repo;

import com.example.hyb.Model.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class Repository implements IRepository{
    ArrayList<Task> taskList;
    private FirebaseFirestore db;

    public Repository(){
        db = FirebaseFirestore.getInstance();
    }

    public Repository(ArrayList<Task> taskList){

    }

    @Override
    public ArrayList<Task> getAll() {
        return taskList;
    }

    public void saveAll(){

    }

    public Task getById(String id){
        DocumentReference docRef = db.collection("todo").document(id);
        DocumentSnapshot document = docRef.get().getResult();

        Task task = null;
        if (document.exists()){
            task = document.toObject(Task.class);
        }

        return task;
    }

    @Override
    public void add(Task t) {
        taskList.add(t);
       db.collection("todo").document(t.getTaskId()).set(t);
    }
}

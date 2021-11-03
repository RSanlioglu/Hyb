package com.example.hyb.Repo;

import com.example.hyb.Model.Task;

import java.util.ArrayList;

public interface IRepository {
    ArrayList<Task> getAll();
    void add(Task t);
}

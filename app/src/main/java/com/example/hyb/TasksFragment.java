package com.example.hyb;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hyb.Adapter.TodoAdapter;
import com.example.hyb.Model.Task;
import com.example.hyb.Model.UserInfo;
import com.example.hyb.Repo.RepositoryCallback;
import com.example.hyb.Repo.TasksRepository;
import com.example.hyb.Repo.UsersRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class TasksFragment extends Fragment implements TodoAdapter.OnItemClickListener {
    private String uidKey;
    private String residentKey; //Nøkkel for å hente brukers resident
    FloatingActionButton btnAddTodo;
    private TodoAdapter todoAdapter;
    private ProgressBar progressBar;
    private TasksRepository tasksRepository;
    private UsersRepository usersRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uidKey = getArguments().getString("userId");
        tasksRepository = new TasksRepository();
        usersRepository = new UsersRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnAddTodo = view.findViewById(R.id.btnAddTodo);

        btnAddTodo.setOnClickListener(view1 -> {
            Intent intent = new Intent(view.getContext(), AddTaskActivity.class);
            view.getContext().startActivity(intent);
        });

        progressBar = view.findViewById(R.id.progressBar);
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        todoAdapter = new TodoAdapter(this);
        recyclerView.setAdapter(todoAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        usersRepository.getUserInfo(uidKey, new UsersRepository.UserInfoListener() {
            @Override
            public void onSuccess(List<UserInfo> userInfos) {
                //use this to filter events just for current users resident
                UserInfo userInfo = userInfos.get(0);
                residentKey = userInfo.getResidentId();
                tasksRepository.getTasks(residentKey ,new TasksRepository.TasksListener() {
                    @Override
                    public void onSuccess(List<Task> tasks) {
                        progressBar.setVisibility(View.GONE);
                        todoAdapter.addTasks(tasks);
                    }

                    @Override
                    public void onFailure() {
                        showErrorMessage();
                    }
                });
            }
            @Override
            public void onFailure() {
                showErrorMessage();
            }
        });
    }

    @Override
    public void onDeleteTaskClicked(Task task) {
        progressBar.setVisibility(View.VISIBLE);
        tasksRepository.deleteTask(task, new RepositoryCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(requireContext(), getString(R.string.task_deleted_successfully), Toast.LENGTH_SHORT).show();
                todoAdapter.removeTask(task);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure() {
                progressBar.setVisibility(View.GONE);
                showErrorMessage();
            }
        });
    }

    @Override
    public void onTaskCheckChanged(Task task) {
        progressBar.setVisibility(View.VISIBLE);
        tasksRepository.updateTaskState(task, new RepositoryCallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
                todoAdapter.onTaskChanged(task);
            }

            @Override
            public void onFailure() {
                showErrorMessage();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showErrorMessage() {
        Toast.makeText(requireContext(), getString(R.string.an_error_occurred), Toast.LENGTH_SHORT).show();
    }
}
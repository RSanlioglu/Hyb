package com.example.hyb.Model;

import androidx.annotation.Nullable;

public class Task {


    public Task() {
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getResidentId() {
        return residentId;
    }

    public void setResidentId(String residentId) {
        this.residentId = residentId;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof Task && ((Task) obj).taskId.equals(taskId);
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    private boolean completed;
    private String title;
    private String description;
    private String taskId;
    private String userId;
    private String residentId;
    private long created;
}

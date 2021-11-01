package com.example.hyb.Model;

public class Task {

    public Task(boolean isCompleted, String title, String description) {
        this.isCompleted = isCompleted;
        this.title = title;
        this.description = description;
    }

    public Task() {
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
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

    private boolean isCompleted;
    private String title;
    private String description;
}

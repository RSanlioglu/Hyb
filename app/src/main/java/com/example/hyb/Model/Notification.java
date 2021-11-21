package com.example.hyb.Model;

public class Notification {
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        CHATS, TASKS, SHOPPING
    }

    private String title;
    private Type type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

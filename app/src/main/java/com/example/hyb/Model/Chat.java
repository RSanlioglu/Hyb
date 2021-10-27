package com.example.hyb.Model;

import com.google.type.DateTime;

import java.time.LocalDateTime;
import java.util.Comparator;

public class Chat implements Comparable<Chat>{
    private String sender;
    private String receiver;
    private String message;
    private String sentDate;

    public Chat(String sender, String receiver, String message, String sentDate) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.sentDate = sentDate;
    }

    public Chat() {

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    @Override
    public int compareTo(Chat o) {
        if(getSentDate() == null || o.getSentDate() == null) {
            return 0;
        }
        return getSentDate().compareTo(o.getSentDate());
    }
}

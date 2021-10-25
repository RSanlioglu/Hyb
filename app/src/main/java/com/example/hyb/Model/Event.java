package com.example.hyb.Model;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class Event {
    private String eventTitle;
    private String eventLocation;
    private String eventDescription;
    private String eventStartTime;
    private String eventEndTime;
    private ArrayList<String> attendeesList = new ArrayList<>();



    public Event(String eventTitle, String eventLocation, String eventDescription, String eventStartTime, String eventEndTime) {
        this.eventTitle = eventTitle;
        this.eventLocation = eventLocation;
        this.eventDescription = eventDescription;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;

    }

    public Event() {
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public ArrayList<String> getAttendeesList() {
        return attendeesList;
    }

    public void setAttendeesList(ArrayList<String> attendeesList) {
        this.attendeesList = attendeesList;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventTitle='" + eventTitle + '\'' +
                ", eventLocation='" + eventLocation + '\'' +
                ", eventDescription='" + eventDescription + '\'' +
                ", eventStartTime='" + eventStartTime + '\'' +
                ", eventEndTime='" + eventEndTime + '\'' +
                ", attendees=" + attendeesList +
                '}';
    }
}

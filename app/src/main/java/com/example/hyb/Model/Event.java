package com.example.hyb.Model;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Event implements Comparable<Event> {
    private String eventTitle;
    private String eventLocation;
    private String eventDescription;
    private String eventStartTime;
    private String eventEndTime;
    private String eventResident;
    private long created;
    private List<String> attendeesList = new ArrayList<>();
    private String id;

    public Event(String eventTitle, String eventLocation, String eventDescription, String eventStartTime, String eventEndTime, String eventResident, List<String> attendeesList) {
        this.eventTitle = eventTitle;
        this.eventLocation = eventLocation;
        this.eventDescription = eventDescription;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventResident = eventResident;
        this.attendeesList = attendeesList;
    }

    public Event() {
    }

    public String getEventResident() {
        return eventResident;
    }

    public void setEventResident(String eventResident) {
        this.eventResident = eventResident;
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

    public List<String> getAttendeesList() {
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

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof Event && ((Event) obj).id.equals(id);
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public int compareTo(Event o) {
        return Long.compare(created, o.created);
    }
}

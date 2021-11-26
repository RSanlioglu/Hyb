package com.example.hyb.Repo;

import androidx.annotation.NonNull;

import com.example.hyb.Model.Event;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class EventsRepository {

    public static final String
            EVENTS_COLLECTION = "events";
    private static final String CREATED_FIELD = "created";
    public static final String RESIDENT_ID = "eventResident";
    private final CollectionReference collectionReference;

    public EventsRepository() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore.collection(EVENTS_COLLECTION);
    }

    public void getEvents(String residentId, EventsListener eventsListener) {
        collectionReference.whereEqualTo(RESIDENT_ID, residentId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Event> events = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Event event = documentSnapshot.toObject(Event.class);
                            if (event.getId() == null) {
                                event.setId(documentSnapshot.getId());
                            }
                            events.add(event);
                        }
                        Collections.sort(events);
                        eventsListener.onSuccess(events);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                eventsListener.onFailure();
            }
        });
    }

    public void saveEvent(Event event, RepositoryCallback repositoryCallback) {
        event.setCreated(System.currentTimeMillis());
        event.setId(UUID.randomUUID().toString());
        collectionReference.document(event.getId()).set(event)
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

    public void deleteEvent(Event event, RepositoryCallback repositoryCallback) {
        collectionReference.document(event.getId()).delete()
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

    public void updateEvent(Event event, RepositoryCallback repositoryCallback) {
        collectionReference.document(event.getId()).set(event)
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

    public void getEventById(String eventId, EventsListener eventsListener) {
        collectionReference.document(eventId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Event event = documentSnapshot.toObject(Event.class);
                        eventsListener.onSuccess(Collections.singletonList(event));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                eventsListener.onFailure();
            }
        });
    }

    public interface EventsListener {
        void onSuccess(List<Event> events);

        void onFailure();
    }
}

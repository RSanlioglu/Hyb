package com.example.hyb.Repo;

import androidx.annotation.NonNull;

import com.example.hyb.Model.ShoppingItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShoppingItemsRepository {
    public static final String RESIDENT_ID = "ResidentId";
    public static final String SHOPPING_ITEMS_COLLECTION = "shoppingItems";
    private final CollectionReference collectionReference;

    public ShoppingItemsRepository() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore.collection(SHOPPING_ITEMS_COLLECTION);
    }

    public void getShoppingItems(String residentId, ShoppingItemsListener shoppingItemsListener) {
        collectionReference.whereEqualTo(RESIDENT_ID, residentId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<ShoppingItem> shoppingItems = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            ShoppingItem shoppingItem = documentSnapshot.toObject(ShoppingItem.class);
                            if (shoppingItem.getItemId() == null) {
                                shoppingItem.setItemId(documentSnapshot.getId());
                            }
                            shoppingItems.add(shoppingItem);
                        }
                        shoppingItemsListener.onSuccess(shoppingItems);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                shoppingItemsListener.onFailure();
            }
        });
    }

    public interface ShoppingItemsListener {
        void onSuccess(List<ShoppingItem> shoppingItems);

        void onFailure();
    }
}

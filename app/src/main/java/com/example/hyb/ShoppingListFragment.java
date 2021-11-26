package com.example.hyb;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hyb.Adapter.ShoppingAdapter;
import com.example.hyb.Model.ShoppingItem;
import com.example.hyb.Model.UserInfo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ShoppingListFragment extends Fragment {
    private final String TAG = "ShoppingListFragment";
    private String residentID;
    private final String ERROR_MESSAGE = "Please Enter An Item And Amount!";
    private FirebaseFirestore db;
    private String uidKey;
    private ProgressBar progressBar;


    public ShoppingListFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create a instance of the database and get its reference
        db = FirebaseFirestore.getInstance();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uidKey = getArguments().getString("userId");

        showCurrentResidentsShoppinglist(view);

        progressBar = view.findViewById(R.id.progressBarShoppinglist);
        EditText shoppingItem = view.findViewById(R.id.shoppingItemName);
        EditText shoppingItemAmountInput = view.findViewById(R.id.shoppingItemAmountInput);
        FloatingActionButton addItemFigure = view.findViewById(R.id.FAB_addItem);


        // onClickListener for add item figure
        addItemFigure.setOnClickListener(v -> {

            //Convert input dataTypes to String values
            String shoppingItemName = shoppingItem.getText().toString();
            String shoppingIteAmount = shoppingItemAmountInput.getText().toString();
            // check if all fields are filed
            if (!shoppingItemName.isEmpty() && !shoppingIteAmount.isEmpty()){

                // get users residentID
                DocumentReference userRef = db.collection("users").document(uidKey);
                //User is retrieved successful
                userRef.get().addOnSuccessListener(documentSnapshot -> {
                    UserInfo user = documentSnapshot.toObject(UserInfo.class);
                    residentID = user.getResidentId();


                    String itemKey = db.collection("shoppingItems").document().getId();
                    // create shoppingItem object
                    ShoppingItem shoppingItem1 = new ShoppingItem(shoppingItemName,shoppingIteAmount,residentID,itemKey);

                    //add new shoppingItem using set() and check if it is successful
                    db.collection("shoppingItems").document(itemKey).set(shoppingItem1)
                            .addOnSuccessListener(aVoid -> {
                                showCurrentResidentsShoppinglist(view);
                                // item added
                                Toast.makeText(view.getContext(),shoppingItemName + " added", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
                });
            }

            // please fil all required fields
            else{
                Toast.makeText(view.getContext(),"please fil all required fields", Toast.LENGTH_SHORT).show();
                Log.d(TAG, ERROR_MESSAGE);
            }
        });

    }

    // get shoppingItems for the current resident and add the to arraylist and use it for adapter
    public void showCurrentResidentsShoppinglist(View view) {
        // get users residentID
        DocumentReference userRef = db.collection("users").document(uidKey);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            RecyclerView recyclerView;

            @Override
            //User is retrieved successful
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserInfo user = documentSnapshot.toObject(UserInfo.class);
                residentID = user.getResidentId();
                db.collection("shoppingItems")
                        .whereEqualTo("residentId", residentID)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                ArrayList<ShoppingItem> duplicateItemList = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    duplicateItemList.add(document.toObject(ShoppingItem.class));

                                }
                                // Remove duplicates every times fragment creates
                                ArrayList<ShoppingItem> itemList = removeDuplicates(duplicateItemList);
                                progressBar.setVisibility(View.GONE);
                                recyclerView = view.findViewById(R.id.recyclerview);
                                recyclerView.setAdapter(new ShoppingAdapter(getActivity(), itemList));
                                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        });
            }

        });

    }

    // Function to remove duplicates from ArrayList
    public static ArrayList<ShoppingItem> removeDuplicates(ArrayList<ShoppingItem> mixedList)
    {
        ArrayList<ShoppingItem> newList = new ArrayList<>();

        for (ShoppingItem element : mixedList) {
            //If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }

        return newList;
    }
}
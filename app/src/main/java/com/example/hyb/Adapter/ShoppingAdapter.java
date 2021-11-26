package com.example.hyb.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hyb.Model.ShoppingItem;
import com.example.hyb.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.shoppingItemViewHodler>  {
    private static final String TAG = "shoppingAdapter";
    private List<ShoppingItem> itemList;
    private LayoutInflater inflater;
    private String removedItem;
    private FirebaseFirestore db;

    //need two things: 1. the context we are in, so we can inflate layout 2. list with shoppingItems
    public ShoppingAdapter(Context context, List<ShoppingItem> itemList) {
        db = FirebaseFirestore.getInstance();
        this.inflater = LayoutInflater.from(context);
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public shoppingItemViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int position) {

        View itemView = inflater.inflate(R.layout.shopping_item_card, parent, false);
        return new shoppingItemViewHodler(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull shoppingItemViewHodler viewHolder, int position) {

        ShoppingItem currentItem = itemList.get(position);
        viewHolder.setItem(currentItem,position);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private void removeItem(int position, View view){

        db = FirebaseFirestore.getInstance();

        ShoppingItem clickedItem = itemList.get(position);
        removedItem = clickedItem.getItemId();

        db.collection("shoppingItems").document(removedItem)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // item deleted
                        Toast.makeText(view.getContext(),clickedItem.getItemName() + " deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

        itemList.remove(position);
        // notify recyclerview that data has changed and Ui should be changed too
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemList.size());

    }


    public class shoppingItemViewHodler extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textViewItemName;
        private TextView textViewItemAmount;
        private int position;

        //need this two for deleting items from recyclerview
        private ShoppingItem shoppingItem;
        private ImageButton deleteShoppingItem;


        public shoppingItemViewHodler(@NonNull View itemView) {
            //itemView is the view we want to populate with data
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.shoppingItem);
            textViewItemAmount = itemView.findViewById(R.id.shoppingItemAmountInput);
            deleteShoppingItem = itemView.findViewById(R.id.deleteShoppingItem);

            //set onclickListener for this view holder
            itemView.setOnClickListener(this);
            deleteShoppingItem.setOnClickListener(this);
        }

        // bind together view with data we get
        public void setItem(ShoppingItem currentItem, int position) {
            textViewItemName.setText(currentItem.getItemName());
            textViewItemAmount.setText(currentItem.getItemAmount());

            this.shoppingItem = currentItem;
            this.position = position;

        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.deleteShoppingItem) {
                removeItem(position, view);
            }
        }
    }

}




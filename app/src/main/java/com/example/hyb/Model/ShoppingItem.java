package com.example.hyb.Model;

public class ShoppingItem {
    private String itemName;
    private String itemAmount;
    private String residentId;
    private String itemId;


    // used to receive items from firebase since residentId is not gonna display on recyclerview

    public ShoppingItem(String itemAmount, String itemId) {
        this.itemAmount = itemAmount;
        this.itemId = itemId;
    }

    public ShoppingItem(String itemName, String itemAmount, String residentId, String itemId) {
        this.itemName = itemName;
        this.itemAmount = itemAmount;
        this.residentId = residentId;
        this.itemId = itemId;
    }

    public ShoppingItem() {
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(String itemAmount) {
        this.itemAmount = itemAmount;
    }

    public String getResidentId() {
        return residentId;
    }

    public void setResidentId(String residentId) {
        this.residentId = residentId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public String toString() {
        return "ShoppingItem{" +
                "itemName='" + itemName + '\'' +
                ", ItemAmount='" + itemAmount + '\'' +
                '}';
    }

}


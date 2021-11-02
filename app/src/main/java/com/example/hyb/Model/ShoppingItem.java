package com.example.hyb.Model;

public class ShoppingItem {
    private String itemName;
    private String ItemAmount;
    private String ResidentId;
    private String itemId;


    // used to receive items from firebase since residentId in not gonna display on recyclerview

    public ShoppingItem(String itemAmount, String itemId) {
        ItemAmount = itemAmount;
        this.itemId = itemId;
    }

    public ShoppingItem(String itemName, String itemAmount, String residentId, String itemId) {
        this.itemName = itemName;
        ItemAmount = itemAmount;
        ResidentId = residentId;
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
        return ItemAmount;
    }

    public void setItemAmount(String itemAmount) {
        ItemAmount = itemAmount;
    }

    public String getResidentId() {
        return ResidentId;
    }

    public void setResidentId(String residentId) {
        ResidentId = residentId;
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
                ", ItemAmount='" + ItemAmount + '\'' +
                '}';
    }

}


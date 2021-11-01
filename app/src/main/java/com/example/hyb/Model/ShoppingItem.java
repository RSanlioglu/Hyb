package com.example.hyb.Model;

public class ShoppingItem {
    private String itemName;
    private String ItemAmount;
    private String ResidentId;


    public ShoppingItem(String itemName, String itemAmount, String residentId) {
        this.itemName = itemName;
        ItemAmount = itemAmount;
        ResidentId = residentId;
    }
    // used to receive items from firebase since residentId in not gonna display on recyclerview
    public ShoppingItem(String itemName, String itemAmount) {
        this.itemName = itemName;
        ItemAmount = itemAmount;
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

    @Override
    public String toString() {
        return "ShoppingItem{" +
                "itemName='" + itemName + '\'' +
                ", ItemAmount='" + ItemAmount + '\'' +
                '}';
    }

}


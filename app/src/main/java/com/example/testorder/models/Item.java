//package com.example.testorder.models;
//
//import androidx.room.Entity;
//import androidx.room.PrimaryKey;
//
//@Entity(tableName = "items")
//public class Item {
//    @PrimaryKey(autoGenerate = true)
//    public int id;
//
//    public int orderId;
//    public String itemName;
//    public String unit;
//    public int quantity;
//    public double rate;
//    public Item() {}
//    public Item(String itemName) {
//        this.itemName = itemName;
//    }
//    public String getItemName() {
//        return itemName;
//    }
//    public Item(String itemName, int quantity, double rate, int orderId) {
//        this.itemName = itemName;
//        this.quantity = quantity;
//        this.rate = rate;
//        this.orderId = orderId;
//    }
//}


package com.example.testorder.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class Item {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String itemName;

    public Item() {}

    public Item(String itemName) {
        this.itemName = itemName;
    }
}

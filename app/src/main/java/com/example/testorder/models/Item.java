package com.example.testorder.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class Item {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int orderId;
    public String itemName;
    public String unit;
    public int quantity;
    public double rate;
}

package com.example.testorder.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class Order {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int clientId;
    public String clientName;
    public String date;
    public double subtotal;
    public double tax;
    public double discount;
    public double total;
    public String status; // e.g., "Pending", "Completed"
}

package com.example.testorder.models;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "order_items")
public class OrderItem {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int orderId;
    public String itemName;
    public double quantity;
    public double unitPrice;

    public OrderItem(int orderId, String itemName, double quantity, double unitPrice) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}

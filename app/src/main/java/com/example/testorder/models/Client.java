package com.example.testorder.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "clients")
public class Client {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String businessName;
    public String contactPerson;
    public String phone;
    public String email;
    public String address;
    public String pricingCategory;
}

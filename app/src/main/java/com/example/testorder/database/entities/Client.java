package com.example.testorder.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "clients")
public class Client {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String phone;

    // Constructor
    public Client(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}

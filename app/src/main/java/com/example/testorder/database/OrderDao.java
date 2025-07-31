package com.example.testorder.database;

import androidx.room.*;
import com.example.testorder.models.Order;
import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    void insert(Order order);

    @Update
    void update(Order order);

    @Query("SELECT * FROM orders ORDER BY date DESC")
    List<Order> getAllOrders();

    @Query("SELECT * FROM orders WHERE clientId = :clientId")
    List<Order> getOrdersByClient(int clientId);
}

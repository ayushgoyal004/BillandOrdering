package com.example.testorder.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.testorder.models.OrderItem;
import java.util.List;

@Dao
public interface OrderItemDao {

    @Insert
    void insert(OrderItem item);

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    List<OrderItem> getItemsForOrder(int orderId);
}

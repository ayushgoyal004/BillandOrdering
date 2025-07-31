package com.example.testorder.database;

import androidx.room.*;
import com.example.testorder.models.Item;
import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    void insert(Item item);

    @Query("SELECT * FROM items WHERE orderId = :orderId")
    List<Item> getItemsForOrder(int orderId);
}

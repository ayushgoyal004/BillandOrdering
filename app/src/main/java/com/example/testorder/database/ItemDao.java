package com.example.testorder.database;

import androidx.room.*;
import com.example.testorder.models.Item;
import com.example.testorder.models.OrderItem;
import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    void insert(Item item);

    @Query("SELECT * FROM items")
    List<Item> getAllItems();
    @Update
    void update(Item item);

    @Delete
    void delete(Item item);
}

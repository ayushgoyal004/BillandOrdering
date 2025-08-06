package com.example.testorder.database;

import androidx.room.*;
import com.example.testorder.models.Item;
import java.util.List;

@Dao
public interface ItemDao {

    @Insert
    void insert(Item item);

    @Query("SELECT * FROM items")
    List<Item> getAllItems();

    @Query("SELECT * FROM items WHERE id = :itemId")
    Item getItemById(int itemId);

    @Query("SELECT * FROM items WHERE itemName = :name LIMIT 1")
    Item getItemByName(String name);

    @Update
    void update(Item item);

    @Delete
    void delete(Item item);
}

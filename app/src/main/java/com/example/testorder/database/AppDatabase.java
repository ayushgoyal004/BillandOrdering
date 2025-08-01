package com.example.testorder.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.testorder.models.Client;
import com.example.testorder.models.Order;
import com.example.testorder.models.Item;
import com.example.testorder.database.OrderItemDao;
import com.example.testorder.models.OrderItem;

@Database(entities = {Client.class, Order.class, Item.class, OrderItem.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract ClientDao clientDao();
    public abstract OrderDao orderDao();
    public abstract ItemDao itemDao();
    public abstract OrderItemDao orderItemDao();


    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "order_billing_database"
            ).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}

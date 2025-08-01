package com.example.testorder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testorder.R;
import com.example.testorder.adapters.OrderAdapter;
import com.example.testorder.database.AppDatabase;
import com.example.testorder.models.Order;

import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    RecyclerView recyclerViewOrders;
    OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

        new Thread(() -> {
            List<Order> orders = AppDatabase.getInstance(this).orderDao().getAllOrders();
            runOnUiThread(() -> {
                if (orders.isEmpty()) {
                    Toast.makeText(this, "No orders found", Toast.LENGTH_SHORT).show();
                } else {
                    orderAdapter = new OrderAdapter(this, orders);
                    recyclerViewOrders.setAdapter(orderAdapter);
                }
            });
        }).start();
    }
}

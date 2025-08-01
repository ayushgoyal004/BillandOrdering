//package com.example.testorder.activities;
//
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.widget.EditText;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.testorder.R;
//import com.example.testorder.adapters.OrderAdapter;
//import com.example.testorder.database.AppDatabase;
//import com.example.testorder.models.Order;
//import java.util.ArrayList;
//import java.util.List;
//
//public class OrdersActivity extends AppCompatActivity {
//
//    RecyclerView recyclerView;
//    OrderAdapter adapter;
//    EditText searchInput;
//    List<Order> orderList = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_orders);
//
//        recyclerView = findViewById(R.id.recyclerOrders);
//        searchInput = findViewById(R.id.inputSearchOrder);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        loadOrders("");
//
//        searchInput.addTextChangedListener(new TextWatcher() {
//            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
//                loadOrders(s.toString());
//            }
//            @Override public void afterTextChanged(Editable s) {}
//        });
//    }
//
//    private void loadOrders(String keyword) {
//        AppDatabase db = AppDatabase.getInstance(this);
//        if (keyword.isEmpty()) {
//            orderList = db.orderDao().getAllOrders();
//        } else {
//            orderList = db.orderDao().searchOrders("%" + keyword + "%");
//        }
//
//        adapter = new OrderAdapter(orderList);
//        recyclerView.setAdapter(adapter);
//
//        if (orderList.isEmpty()) {
//            Toast.makeText(this, "No orders found", Toast.LENGTH_SHORT).show();
//        }
//    }
//}


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

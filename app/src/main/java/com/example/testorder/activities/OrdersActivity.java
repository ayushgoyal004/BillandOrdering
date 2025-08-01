package com.example.testorder.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testorder.R;
import com.example.testorder.adapters.OrderAdapter;
import com.example.testorder.database.AppDatabase;
import com.example.testorder.models.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    RecyclerView recyclerViewOrders;
    Spinner spinnerFilter, spinnerSort;
    OrderAdapter orderAdapter;

    List<Order> allOrders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

        spinnerFilter = findViewById(R.id.spinnerFilter);
        spinnerSort = findViewById(R.id.spinnerSort);

        // Setup Filter Spinner
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"All", "Pending", "PDF Generated", "Completed"});
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(filterAdapter);

        // Setup Sort Spinner
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Newest First", "Oldest First"});
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(sortAdapter);

        // Load orders
        new Thread(() -> {
            allOrders = AppDatabase.getInstance(this).orderDao().getAllOrders();
            runOnUiThread(() -> {
                applyFilterAndSort();

                // Setup listeners after data is loaded
                spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        applyFilterAndSort();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

                spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        applyFilterAndSort();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
            });
        }).start();
    }

    private void applyFilterAndSort() {
        String filter = spinnerFilter.getSelectedItem().toString();
        String sort = spinnerSort.getSelectedItem().toString();

        List<Order> filtered = new ArrayList<>();
        for (Order o : allOrders) {
            if (filter.equals("All") || o.status.equals(filter)) {
                filtered.add(o);
            }
        }

        // Sort by date
        Collections.sort(filtered, (o1, o2) -> {
            long d1 = Long.parseLong(o1.date);
            long d2 = Long.parseLong(o2.date);
            return sort.equals("Newest First") ? Long.compare(d2, d1) : Long.compare(d1, d2);
        });

        if (filtered.isEmpty()) {
            Toast.makeText(this, "No orders found", Toast.LENGTH_SHORT).show();
        }

        orderAdapter = new OrderAdapter(this, filtered);
        recyclerViewOrders.setAdapter(orderAdapter);
    }
}

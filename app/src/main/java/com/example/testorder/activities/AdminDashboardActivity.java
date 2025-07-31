package com.example.testorder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.testorder.R;

public class AdminDashboardActivity extends AppCompatActivity {

    View newOrder, viewOrders, manageClients, viewReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        newOrder = findViewById(R.id.actionNewOrder);
        viewOrders = findViewById(R.id.actionViewOrders);
        manageClients = findViewById(R.id.actionClients);
        viewReports = findViewById(R.id.actionReports);

        newOrder.setOnClickListener(v ->
                startActivity(new Intent(this, NewOrderActivity.class)));

        viewOrders.setOnClickListener(v ->
                startActivity(new Intent(this, OrdersActivity.class)));

        manageClients.setOnClickListener(v ->
                startActivity(new Intent(this, ClientsActivity.class)));

        viewReports.setOnClickListener(v ->
                startActivity(new Intent(this, ReportsActivity.class)));
        Button btnAddItem = findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(view -> {
            startActivity(new Intent(this, AddItemActivity.class));
        });

    }
}

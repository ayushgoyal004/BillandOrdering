//package com.example.testorder.activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.appcompat.app.AppCompatActivity;
//import com.example.testorder.R;
//
//public class AdminDashboardActivity extends AppCompatActivity {
//
//    View newOrder, viewOrders, manageClients, viewReports;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_admin_dashboard);
//
//        newOrder = findViewById(R.id.actionNewOrder);
//        viewOrders = findViewById(R.id.actionViewOrders);
//        manageClients = findViewById(R.id.actionClients);
//        viewReports = findViewById(R.id.actionReports);
//
//        newOrder.setOnClickListener(v ->
//                startActivity(new Intent(this, NewOrderActivity.class)));
//
//        viewOrders.setOnClickListener(v ->
//                startActivity(new Intent(this, OrdersActivity.class)));
//
//        manageClients.setOnClickListener(v ->
//                startActivity(new Intent(this, ClientsActivity.class)));
//
//        viewReports.setOnClickListener(v ->
//                startActivity(new Intent(this, ReportsActivity.class)));
//        Button btnAddItem = findViewById(R.id.btnAddItem);
//        btnAddItem.setOnClickListener(view -> {
//            startActivity(new Intent(this, AddItemActivity.class));
//        });
//
//    }
//}

package com.example.testorder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast; // Import Toast

import androidx.appcompat.app.AppCompatActivity;
import com.example.testorder.R;
import com.example.testorder.utils.DatabaseBackupHelper; // Import your backup helper

public class AdminDashboardActivity extends AppCompatActivity {

    View newOrder, viewOrders, manageClients, viewReports;
    Button btnBackup; // Declare the backup button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Your existing findViewById calls
        newOrder = findViewById(R.id.actionNewOrder);
        viewOrders = findViewById(R.id.actionViewOrders);
        manageClients = findViewById(R.id.actionClients);
        viewReports = findViewById(R.id.actionReports);

        // Find the new backup button
        btnBackup = findViewById(R.id.btnBackup);

        // Your existing click listeners
        newOrder.setOnClickListener(v ->
                startActivity(new Intent(this, NewOrderActivity.class)));

        viewOrders.setOnClickListener(v ->
                startActivity(new Intent(this, OrdersActivity.class)));

        manageClients.setOnClickListener(v ->
                startActivity(new Intent(this, ClientsActivity.class)));

        viewReports.setOnClickListener(v ->
                startActivity(new Intent(this, ReportsActivity.class)));

        // Set the click listener for the backup button
        btnBackup.setOnClickListener(v -> {
            // Call your backup method
            DatabaseBackupHelper.backupDatabase(this);

            // Show a confirmation message to the user
            Toast.makeText(this, "Backup successfully created!", Toast.LENGTH_SHORT).show();
        });
    }
}
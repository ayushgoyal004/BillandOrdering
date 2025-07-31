//package com.example.testorder.activities;
//
//import android.os.Bundle;
//import android.widget.*;
//import androidx.appcompat.app.AppCompatActivity;
//import com.example.testorder.R;
//
//public class NewOrderActivity extends AppCompatActivity {
//
//    Spinner clientSpinner;
//    LinearLayout layoutItemInputs;
//    Button btnSubmitOrder;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new_order);
//
//        clientSpinner = findViewById(R.id.clientSpinner);
//        layoutItemInputs = findViewById(R.id.layoutItemInputs);
//        btnSubmitOrder = findViewById(R.id.btnSubmitOrder);
//
//        // Dummy data
//        String[] clients = {"Client A", "Client B", "Client C"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clients);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        clientSpinner.setAdapter(adapter);
//
//        // Example: Add 2 items dynamically (you can change this logic)
//        addItemInput("Sugar");
//        addItemInput("Wheat");
//
//        btnSubmitOrder.setOnClickListener(v -> {
//            Toast.makeText(this, "Order submitted (dummy action)", Toast.LENGTH_SHORT).show();
//        });
//    }
//
//    private void addItemInput(String itemName) {
//        LinearLayout row = new LinearLayout(this);
//        row.setOrientation(LinearLayout.HORIZONTAL);
//        row.setPadding(0, 16, 0, 16);
//
//        TextView itemLabel = new TextView(this);
//        itemLabel.setText(itemName);
//        itemLabel.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//
//        EditText quantityInput = new EditText(this);
//        quantityInput.setHint("Qty");
//        quantityInput.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//
//        EditText priceInput = new EditText(this);
//        priceInput.setHint("Price/unit");
//        priceInput.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//
//        row.addView(itemLabel);
//        row.addView(quantityInput);
//        row.addView(priceInput);
//
//        layoutItemInputs.addView(row);
//    }
//}

package com.example.testorder.activities;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.testorder.R;
import com.example.testorder.database.AppDatabase;
import com.example.testorder.models.Client;
import com.example.testorder.models.Item;
import com.example.testorder.models.Order;

import java.util.ArrayList;
import java.util.List;

public class NewOrderActivity extends AppCompatActivity {

    Spinner clientSpinner;
    LinearLayout layoutItemInputs;
    Button btnSubmitOrder;

    List<Client> clientList = new ArrayList<>();
    List<Item> itemList = new ArrayList<>();
    List<EditText[]> itemInputs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        clientSpinner = findViewById(R.id.clientSpinner);
        layoutItemInputs = findViewById(R.id.layoutItemInputs);
        btnSubmitOrder = findViewById(R.id.btnSubmitOrder);

        // Load clients and items from DB in background thread
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(NewOrderActivity.this);
            clientList = db.clientDao().getAllClients();
            itemList = db.itemDao().getAllItems();

            runOnUiThread(() -> {
                // Populate Spinner
                List<String> clientNames = new ArrayList<>();
                for (Client c : clientList) clientNames.add(c.businessName);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clientNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                clientSpinner.setAdapter(adapter);

                // Add item rows dynamically
                for (Item item : itemList) addItemInput(item.itemName);
            });
        }).start();

        btnSubmitOrder.setOnClickListener(v -> {
            int clientIndex = clientSpinner.getSelectedItemPosition();
            if (clientIndex < 0 || clientIndex >= clientList.size()) {
                Toast.makeText(this, "Select a client", Toast.LENGTH_SHORT).show();
                return;
            }

            Client selectedClient = clientList.get(clientIndex);
            double subtotal = 0;

            for (int i = 0; i < itemList.size(); i++) {
                EditText[] fields = itemInputs.get(i);
                String qtyStr = fields[0].getText().toString();
                String priceStr = fields[1].getText().toString();
                if (!qtyStr.isEmpty() && !priceStr.isEmpty()) {
                    double qty = Double.parseDouble(qtyStr);
                    double price = Double.parseDouble(priceStr);
                    subtotal += qty * price;
                }
            }

            Order order = new Order();
            order.clientId = selectedClient.id;
            order.clientName = selectedClient.businessName;
            order.date = String.valueOf(System.currentTimeMillis());
            order.subtotal = subtotal;
            order.tax = 0;
            order.discount = 0;
            order.total = subtotal;
            order.status = "Pending";

            // Insert order on background thread
            new Thread(() -> {
                AppDatabase.getInstance(NewOrderActivity.this).orderDao().insert(order);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Order saved", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }

    private void addItemInput(String itemName) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(0, 16, 0, 16);

        TextView itemLabel = new TextView(this);
        itemLabel.setText(itemName);
        itemLabel.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        EditText quantityInput = new EditText(this);
        quantityInput.setHint("Qty");
        quantityInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        quantityInput.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        EditText priceInput = new EditText(this);
        priceInput.setHint("Price/unit");
        priceInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        priceInput.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        itemInputs.add(new EditText[]{quantityInput, priceInput});

        row.addView(itemLabel);
        row.addView(quantityInput);
        row.addView(priceInput);

        layoutItemInputs.addView(row);
    }
}


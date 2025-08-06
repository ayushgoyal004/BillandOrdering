//package com.example.testorder.activities;
//
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.widget.*;
//import androidx.appcompat.app.AppCompatActivity;
//import com.example.testorder.R;
//import com.example.testorder.database.AppDatabase;
//import com.example.testorder.models.Client;
//import com.example.testorder.models.Item;
//import com.example.testorder.models.Order;
//import com.example.testorder.models.OrderItem;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class NewOrderActivity extends AppCompatActivity {
//
//    AutoCompleteTextView autoClientSearch;
//    LinearLayout layoutItemInputs;
//    Button btnSubmitOrder;
//
//    List<Client> clientList = new ArrayList<>();
//    List<Client> filteredClients = new ArrayList<>();
//    List<Item> itemList = new ArrayList<>();
//    List<EditText[]> itemInputs = new ArrayList<>();
//
//    ArrayAdapter<String> clientAdapter;
//    List<String> clientNames = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new_order);
//
//        autoClientSearch = findViewById(R.id.autoClientSearch);
//        layoutItemInputs = findViewById(R.id.layoutItemInputs);
//        btnSubmitOrder = findViewById(R.id.btnSubmitOrder);
//
//        new Thread(() -> {
//            AppDatabase db = AppDatabase.getInstance(NewOrderActivity.this);
//            clientList = db.clientDao().getAllClients();
//            itemList = db.itemDao().getAllItems();
//
//            filteredClients = new ArrayList<>(clientList);
//            updateClientNamesList();
//
//            runOnUiThread(() -> {
//                clientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, clientNames);
//                autoClientSearch.setAdapter(clientAdapter);
//                autoClientSearch.setThreshold(1); // Show dropdown from 1 char
//
//                for (Item item : itemList) addItemInput(item.itemName);
//            });
//        }).start();
//
//        autoClientSearch.setOnFocusChangeListener((v, hasFocus) -> {
//            if (hasFocus) autoClientSearch.showDropDown();
//        });
//
//        autoClientSearch.addTextChangedListener(new TextWatcher() {
//            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            @Override public void afterTextChanged(Editable s) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                filterClients(s.toString().trim());
//                autoClientSearch.showDropDown(); // Show live dropdown
//            }
//        });
//
//        btnSubmitOrder.setOnClickListener(v -> {
//            String selectedName = autoClientSearch.getText().toString().trim();
//            Client selectedClient = null;
//            for (Client c : clientList) {
//                if (c.businessName.equals(selectedName)) {
//                    selectedClient = c;
//                    break;
//                }
//            }
//
//            if (selectedClient == null) {
//                Toast.makeText(this, "Select a valid client from the dropdown", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            double subtotal = 0;
//            List<OrderItem> orderItems = new ArrayList<>();
//
//            for (int i = 0; i < itemList.size(); i++) {
//                EditText[] fields = itemInputs.get(i);
//                String qtyStr = fields[0].getText().toString();
//                String priceStr = fields[1].getText().toString();
//
//                if (!qtyStr.isEmpty() && !priceStr.isEmpty()) {
//                    double qty = Double.parseDouble(qtyStr);
//                    double price = Double.parseDouble(priceStr);
//                    subtotal += qty * price;
//
//                    orderItems.add(new OrderItem(0, itemList.get(i).itemName, qty, price));
//                }
//            }
//
//            Order order = new Order();
//            order.clientId = selectedClient.id;
//            order.clientName = selectedClient.businessName;
//            order.date = String.valueOf(System.currentTimeMillis());
//            order.subtotal = subtotal;
//            order.tax = 0;
//            order.discount = 0;
//            order.total = subtotal;
//            order.status = "Pending";
//
//            new Thread(() -> {
//                AppDatabase db = AppDatabase.getInstance(NewOrderActivity.this);
//                long orderId = db.orderDao().insertAndGetId(order);
//
//                for (OrderItem orderItem : orderItems) {
//                    orderItem.orderId = (int) orderId;
//                    db.orderItemDao().insert(orderItem);
//                }
//
//                runOnUiThread(() -> {
//                    Toast.makeText(this, "Order saved", Toast.LENGTH_SHORT).show();
//                    finish();
//                });
//            }).start();
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
//        quantityInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
//        quantityInput.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//
//        EditText priceInput = new EditText(this);
//        priceInput.setHint("Price/unit");
//        priceInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
//        priceInput.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//
//        itemInputs.add(new EditText[]{quantityInput, priceInput});
//        row.addView(itemLabel);
//        row.addView(quantityInput);
//        row.addView(priceInput);
//        layoutItemInputs.addView(row);
//    }
//
//    private void filterClients(String keyword) {
//        filteredClients.clear();
//        clientNames.clear();
//
//        // Always filter from full list (not from already filtered list)
//        for (Client client : clientList) {
//            if (client.businessName.toLowerCase().contains(keyword.toLowerCase())) {
//                filteredClients.add(client);
//                clientNames.add(client.businessName);
//            }
//        }
//
//        clientAdapter.clear();  // Clear and re-add to refresh dropdown
//        clientAdapter.addAll(clientNames);
//        clientAdapter.notifyDataSetChanged();
//
//        // Show dropdown again to reflect changes immediately
//        autoClientSearch.showDropDown();
//    }
//
//
//    private void updateClientNamesList() {
//        clientNames.clear();
//        for (Client c : filteredClients) {
//            clientNames.add(c.businessName);
//        }
//    }
//}
package com.example.testorder.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.testorder.R;
import com.example.testorder.database.AppDatabase;
import com.example.testorder.models.Client;
import com.example.testorder.models.Item;
import com.example.testorder.models.Order;
import com.example.testorder.models.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class NewOrderActivity extends AppCompatActivity {

    AutoCompleteTextView autoClientSearch;
    LinearLayout layoutItemInputs;
    Button btnSubmitOrder;

    List<Client> clientList = new ArrayList<>();
    List<Client> filteredClients = new ArrayList<>();
    List<Item> itemList = new ArrayList<>();
    List<EditText[]> itemInputs = new ArrayList<>();
    List<TextView> stockViews = new ArrayList<>();

    ArrayAdapter<String> clientAdapter;
    List<String> clientNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        autoClientSearch = findViewById(R.id.autoClientSearch);
        layoutItemInputs = findViewById(R.id.layoutItemInputs);
        btnSubmitOrder = findViewById(R.id.btnSubmitOrder);

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(NewOrderActivity.this);
            clientList = db.clientDao().getAllClients();
            itemList = db.itemDao().getAllItems();

            filteredClients = new ArrayList<>(clientList);
            updateClientNamesList();

            runOnUiThread(() -> {
                clientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, clientNames);
                autoClientSearch.setAdapter(clientAdapter);
                autoClientSearch.setThreshold(1);

                for (Item item : itemList) {
                    addItemInput(item);
                }
            });
        }).start();

        autoClientSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) autoClientSearch.showDropDown();
        });

        autoClientSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterClients(s.toString().trim());
                autoClientSearch.showDropDown();
            }
        });

        btnSubmitOrder.setOnClickListener(v -> {
            String selectedName = autoClientSearch.getText().toString().trim();
            Client selectedClient = null;
            for (Client c : clientList) {
                if (c.businessName.equals(selectedName)) {
                    selectedClient = c;
                    break;
                }
            }

            if (selectedClient == null) {
                Toast.makeText(this, "Select a valid client from the dropdown", Toast.LENGTH_SHORT).show();
                return;
            }

            double subtotal = 0;
            List<OrderItem> orderItems = new ArrayList<>();

            for (int i = 0; i < itemList.size(); i++) {
                EditText[] fields = itemInputs.get(i);
                String qtyStr = fields[0].getText().toString();
                String priceStr = fields[1].getText().toString();
                Item item = itemList.get(i);

                if (!qtyStr.isEmpty() && !priceStr.isEmpty()) {
                    double qty = Double.parseDouble(qtyStr);
                    double price = Double.parseDouble(priceStr);

                    // Check for stock limit
                    if (qty > item.stock) {
                        Toast.makeText(this, "Not enough stock for " + item.itemName, Toast.LENGTH_LONG).show();
                        return;
                    }

                    subtotal += qty * price;
                    orderItems.add(new OrderItem(0, item.itemName, qty, price));
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

            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(NewOrderActivity.this);
                long orderId = db.orderDao().insertAndGetId(order);

                for (OrderItem orderItem : orderItems) {
                    orderItem.orderId = (int) orderId;
                    db.orderItemDao().insert(orderItem);

                    // Deduct stock
                    Item dbItem = db.itemDao().getItemByName(orderItem.itemName);
                    if (dbItem != null) {
                        dbItem.stock -= orderItem.quantity;
                        db.itemDao().update(dbItem);
                    }
                }

                runOnUiThread(() -> {
                    Toast.makeText(this, "Order saved and stock updated", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }

    private void addItemInput(Item item) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        row.setPadding(0, 24, 0, 24);
        row.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // Label row (name, qty, price)
        LinearLayout inputRow = new LinearLayout(this);
        inputRow.setOrientation(LinearLayout.HORIZONTAL);

        TextView itemLabel = new TextView(this);
        itemLabel.setText(item.itemName);
        itemLabel.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        EditText quantityInput = new EditText(this);
        quantityInput.setHint("Qty");
        quantityInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        quantityInput.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        EditText priceInput = new EditText(this);
        priceInput.setHint("Price/unit");
        priceInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        priceInput.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        inputRow.addView(itemLabel);
        inputRow.addView(quantityInput);
        inputRow.addView(priceInput);

        // Stock View (in red below inputs)
        TextView stockView = new TextView(this);
        stockView.setText("Stock: " + item.stock);
        stockView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        stockView.setTextSize(12);
        stockView.setPadding(8, 8, 0, 0);

        // Add all to main layout
        row.addView(inputRow);
        row.addView(stockView);

        itemInputs.add(new EditText[]{quantityInput, priceInput});
        stockViews.add(stockView);

        layoutItemInputs.addView(row);
    }

    private void filterClients(String keyword) {
        filteredClients.clear();
        clientNames.clear();

        for (Client client : clientList) {
            if (client.businessName.toLowerCase().contains(keyword.toLowerCase())) {
                filteredClients.add(client);
                clientNames.add(client.businessName);
            }
        }

        clientAdapter.clear();
        clientAdapter.addAll(clientNames);
        clientAdapter.notifyDataSetChanged();
        autoClientSearch.showDropDown();
    }

    private void updateClientNamesList() {
        clientNames.clear();
        for (Client c : filteredClients) {
            clientNames.add(c.businessName);
        }
    }
}

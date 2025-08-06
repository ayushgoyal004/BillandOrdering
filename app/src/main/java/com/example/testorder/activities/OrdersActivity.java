package com.example.testorder.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testorder.R;
import com.example.testorder.adapters.OrderAdapter;
import com.example.testorder.database.AppDatabase;
import com.example.testorder.models.Client;
import com.example.testorder.models.Order;

import java.text.SimpleDateFormat;
import java.util.*;

public class OrdersActivity extends AppCompatActivity {

    RecyclerView recyclerViewOrders;
    Spinner spinnerFilter, spinnerSort;
    OrderAdapter orderAdapter;
    TextView txtSelectedDate;
    Button btnClearDate;
    AutoCompleteTextView editSearchClient;

    String selectedDateMillis = null;
    String selectedClientName = "";

    List<Order> allOrders = new ArrayList<>();
    List<String> clientNames = new ArrayList<>();
    List<Client> allClients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

        spinnerFilter = findViewById(R.id.spinnerFilter);
        spinnerSort = findViewById(R.id.spinnerSort);
        txtSelectedDate = findViewById(R.id.txtSelectedDate);
        btnClearDate = findViewById(R.id.btnClearDate);
        editSearchClient = findViewById(R.id.editSearchClient);

        ArrayAdapter<String> clientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, clientNames);
        editSearchClient.setAdapter(clientAdapter);
        editSearchClient.setThreshold(1);

        editSearchClient.setOnItemClickListener((parent, view, position, id) -> {
            selectedClientName = parent.getItemAtPosition(position).toString();
            applyFilterAndSort();
        });

        editSearchClient.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                selectedClientName = s.toString().trim();
                applyFilterAndSort();
            }
        });

        txtSelectedDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(OrdersActivity.this, (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth, 0, 0, 0);
                long selectedMillis = calendar.getTimeInMillis();
                selectedDateMillis = String.valueOf(selectedMillis);

                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                txtSelectedDate.setText("Date: " + sdf.format(calendar.getTime()));
                btnClearDate.setVisibility(View.VISIBLE);
                applyFilterAndSort();
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnClearDate.setOnClickListener(v -> {
            selectedDateMillis = null;
            txtSelectedDate.setText("Select Date");
            btnClearDate.setVisibility(View.GONE);
            applyFilterAndSort();
        });

        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"All", "Pending", "PDF Generated", "Completed"});
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(filterAdapter);

        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Newest First", "Oldest First"});
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(sortAdapter);

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            allOrders = db.orderDao().getAllOrders();
            allClients = db.clientDao().getAllClients();

            for (Client c : allClients) {
                clientNames.add(c.businessName);
            }

            runOnUiThread(() -> {
                clientAdapter.notifyDataSetChanged();
                applyFilterAndSort();

                spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        applyFilterAndSort();
                    }

                    @Override public void onNothingSelected(AdapterView<?> parent) {}
                });

                spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        applyFilterAndSort();
                    }

                    @Override public void onNothingSelected(AdapterView<?> parent) {}
                });
            });
        }).start();
    }

    private void applyFilterAndSort() {
        String filter = spinnerFilter.getSelectedItem().toString();
        String sort = spinnerSort.getSelectedItem().toString();

        List<Order> filtered = new ArrayList<>();
        for (Order o : allOrders) {
            boolean matchesStatus = filter.equals("All") || o.status.equals(filter);
            boolean matchesClient = selectedClientName.isEmpty() ||
                    o.clientName.toLowerCase().contains(selectedClientName.toLowerCase());

            boolean matchesDate = true;
            if (!selectedClientName.isEmpty()) {
                // If client is selected, date is optional
                if (selectedDateMillis != null) {
                    long startOfDay = Long.parseLong(selectedDateMillis);
                    long endOfDay = startOfDay + 24 * 60 * 60 * 1000;
                    long orderTime = Long.parseLong(o.date);
                    matchesDate = orderTime >= startOfDay && orderTime < endOfDay;
                }
            } else {
                // If no client is selected, date filter is used as-is
                if (selectedDateMillis != null) {
                    long startOfDay = Long.parseLong(selectedDateMillis);
                    long endOfDay = startOfDay + 24 * 60 * 60 * 1000;
                    long orderTime = Long.parseLong(o.date);
                    matchesDate = orderTime >= startOfDay && orderTime < endOfDay;
                }
            }

            if (matchesStatus && matchesClient && matchesDate) {
                filtered.add(o);
            }
        }

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

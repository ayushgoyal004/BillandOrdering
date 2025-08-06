package com.example.testorder.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testorder.R;
import com.example.testorder.adapters.OrderAdapter;
import com.example.testorder.database.AppDatabase;
import com.example.testorder.models.Order;
import android.app.DatePickerDialog;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    RecyclerView recyclerViewOrders;
    Spinner spinnerFilter, spinnerSort;
    OrderAdapter orderAdapter;
    TextView txtSelectedDate;
    Button btnClearDate;
    String selectedDateMillis = null; // For filtering
    EditText editSearchClient;
    String searchKeyword = "";



    List<Order> allOrders = new ArrayList<>();

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

// Date selection logic
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

        editSearchClient = findViewById(R.id.editSearchClient);
        editSearchClient.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchKeyword = s.toString().toLowerCase().trim();
                applyFilterAndSort();
            }
        });



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


//    private void applyFilterAndSort() {
//        String filter = spinnerFilter.getSelectedItem().toString();
//        String sort = spinnerSort.getSelectedItem().toString();
//
//        List<Order> filtered = new ArrayList<>();
//        for (Order o : allOrders) {
//            boolean matchesStatus = filter.equals("All") || o.status.equals(filter);
//
//            boolean matchesDate = true;
//            if (selectedDateMillis != null) {
//                // Compare only the date part (ignoring time)
//                long startOfDay = Long.parseLong(selectedDateMillis);
//                long endOfDay = startOfDay + 24 * 60 * 60 * 1000;
//                long orderTime = Long.parseLong(o.date);
//
//                matchesDate = orderTime >= startOfDay && orderTime < endOfDay;
//            }
//
//            if (matchesStatus && matchesDate) {
//                filtered.add(o);
//            }
//        }
//
//        // Sort by date
//        Collections.sort(filtered, (o1, o2) -> {
//            long d1 = Long.parseLong(o1.date);
//            long d2 = Long.parseLong(o2.date);
//            return sort.equals("Newest First") ? Long.compare(d2, d1) : Long.compare(d1, d2);
//        });
//
//        if (filtered.isEmpty()) {
//            Toast.makeText(this, "No orders found", Toast.LENGTH_SHORT).show();
//        }
//
//        orderAdapter = new OrderAdapter(this, filtered);
//        recyclerViewOrders.setAdapter(orderAdapter);
//    }
private void applyFilterAndSort() {
    String filter = spinnerFilter.getSelectedItem().toString();
    String sort = spinnerSort.getSelectedItem().toString();

    List<Order> filtered = new ArrayList<>();

    for (Order o : allOrders) {
        boolean matchesStatus = filter.equals("All") || o.status.equals(filter);
        boolean matchesClient = true;
        boolean matchesDate = true;

        if (!searchKeyword.isEmpty()) {
            matchesClient = o.clientName.toLowerCase().contains(searchKeyword);
            matchesDate = true; // ignore date if client is typed
        } else if (selectedDateMillis != null) {
            long startOfDay = Long.parseLong(selectedDateMillis);
            long endOfDay = startOfDay + 24 * 60 * 60 * 1000;
            long orderTime = Long.parseLong(o.date);
            matchesDate = orderTime >= startOfDay && orderTime < endOfDay;
        }

        if (matchesStatus && matchesClient && matchesDate) {
            filtered.add(o);
        }
    }

    // Sort orders by date
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

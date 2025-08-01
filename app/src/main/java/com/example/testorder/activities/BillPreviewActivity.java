package com.example.testorder.activities;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.testorder.R;
import com.example.testorder.database.AppDatabase;
import com.example.testorder.models.Client;
import com.example.testorder.models.Order;
import com.example.testorder.models.OrderItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BillPreviewActivity extends AppCompatActivity {

    TextView txtClient, txtDate, txtSubtotal, txtTax, txtDiscount, txtTotal;
    LinearLayout layoutItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_preview);

        txtClient = findViewById(R.id.txtClient);
        txtDate = findViewById(R.id.txtDate);
        txtSubtotal = findViewById(R.id.txtSubtotal);
        txtTax = findViewById(R.id.txtTax);
        txtDiscount = findViewById(R.id.txtDiscount);
        txtTotal = findViewById(R.id.txtTotal);
        layoutItems = findViewById(R.id.layoutItems);

        int orderId = getIntent().getIntExtra("orderId", -1);
        if (orderId == -1) {
            finish();
            return;
        }

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            Order order = db.orderDao().getOrderById(orderId);
            Client client = db.clientDao().getClientById(order.clientId);
            List<OrderItem> orderItems = db.orderItemDao().getItemsForOrder(orderId);

            runOnUiThread(() -> {
                txtClient.setText("Client: " + client.businessName);

                // Format date
                String formattedDate = order.date;
                try {
                    long timestamp = Long.parseLong(order.date);
                    formattedDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date(timestamp));
                } catch (Exception e) {
                    // Use original date if parsing fails
                }
                txtDate.setText("Date: " + formattedDate);

                txtSubtotal.setText("Subtotal: ₹" + order.subtotal);
                txtTax.setText("Tax: ₹" + order.tax);
                txtDiscount.setText("Discount: ₹" + order.discount);
                txtTotal.setText("Total: ₹" + order.total);

                layoutItems.removeAllViews();
                for (OrderItem item : orderItems) {
                    TextView itemText = new TextView(this);
                    double total = item.quantity * item.unitPrice;
                    itemText.setText("- " + item.itemName + " (Qty: " + item.quantity + ", ₹" + item.unitPrice + ", Total: ₹" + total + ")");
                    layoutItems.addView(itemText);
                }
            });
        }).start();
    }
}

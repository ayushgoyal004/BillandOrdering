package com.example.testorder.activities;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import com.example.testorder.R;
import com.example.testorder.database.AppDatabase;
import com.example.testorder.models.Client;
import com.example.testorder.models.Order;
import com.example.testorder.models.OrderItem;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BillPreviewActivity extends AppCompatActivity {

    TextView txtClient, txtDate, txtSubtotal, txtTax, txtDiscount, txtTotal;
    LinearLayout layoutItems;
    Button btnGeneratePdf;
    Button btnToggleStatus;
    Order currentOrder;  // save reference

    String formattedDate = "";

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
        btnGeneratePdf = findViewById(R.id.btnGeneratePdf);
        layoutItems = findViewById(R.id.layoutItems);
        btnToggleStatus = findViewById(R.id.btnToggleStatus);

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
                currentOrder = order;
                updateToggleButton(order.status);
                try {
                    long timestamp = Long.parseLong(order.date);
                    formattedDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date(timestamp));
                } catch (Exception e) {
                    formattedDate = order.date;
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

                btnGeneratePdf.setOnClickListener(v -> generatePdf(order, client, orderItems));
                btnToggleStatus.setOnClickListener(v -> {
                    if (currentOrder.status.equals("Completed")) {
                        new AlertDialog.Builder(this)
                                .setTitle("Mark as Uncompleted?")
                                .setMessage("Are you sure you want to mark this order as uncompleted?")
                                .setPositiveButton("Yes", (dialog, which) -> updateOrderStatus("PDF Generated"))
                                .setNegativeButton("No", null)
                                .show();
                    } else {
                        updateOrderStatus("Completed");
                    }
                });
            });
        }).start();
    }


    private void generatePdf(Order order, Client client, List<OrderItem> orderItems) {
        new Thread(() -> {
            try {
                PdfDocument pdfDoc = new PdfDocument();
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(600, 800, 1).create();
                PdfDocument.Page page = pdfDoc.startPage(pageInfo);
                Canvas canvas = page.getCanvas();
                Paint paint = new Paint();
                paint.setTextSize(12);

                int startX = 40;
                int startY = 40;
                int cellHeight = 30;

                // Title
                paint.setFakeBoldText(true);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawText("Vendor Name (Sanjay Kumar)", startX + 100, startY, paint);
                canvas.drawText("Date: " + formattedDate, startX + 400, startY, paint);
                startY += cellHeight + 10;

                canvas.drawText("Client Name: " + client.businessName, startX + 100, startY, paint);
                startY += cellHeight;

                // Table headers
                String[] headers = {"Quantity", "Item Name", "Rate", "Price (Qty*Rate)"};
                int[] columnWidths = {60, 200, 100, 100}; // total 460
                int x = startX;

                paint.setStyle(Paint.Style.STROKE); // for borders
                for (int i = 0; i < headers.length; i++) {
                    canvas.drawRect(x, startY, x + columnWidths[i], startY + cellHeight, paint);
                    x += columnWidths[i];
                }

                // Header text
                x = startX;
                paint.setStyle(Paint.Style.FILL);
                for (int i = 0; i < headers.length; i++) {
                    canvas.drawText(headers[i], x + 5, startY + 20, paint);
                    x += columnWidths[i];
                }

                startY += cellHeight;

                // Table body
                double total = 0.0;
                int maxRows = 15;

                for (int i = 0; i < maxRows; i++) {
                    OrderItem item = (i < orderItems.size()) ? orderItems.get(i) : null;

                    x = startX;
                    paint.setStyle(Paint.Style.STROKE);
                    for (int j = 0; j < columnWidths.length; j++) {
                        canvas.drawRect(x, startY, x + columnWidths[j], startY + cellHeight, paint);
                        x += columnWidths[j];
                    }

                    if (item != null) {
                        double itemTotal = item.quantity * item.unitPrice;
                        total += itemTotal;

                        x = startX;
                        paint.setStyle(Paint.Style.FILL);
                        canvas.drawText(String.valueOf(item.quantity), x + 5, startY + 20, paint); x += columnWidths[0];
                        canvas.drawText(item.itemName, x + 5, startY + 20, paint); x += columnWidths[1];
                        canvas.drawText("₹" + item.unitPrice, x + 5, startY + 20, paint); x += columnWidths[2];
                        canvas.drawText("₹" + itemTotal, x + 5, startY + 20, paint);
                    }

                    startY += cellHeight;
                }

                // Total row
                x = startX;
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(x, startY, x + columnWidths[0] + columnWidths[1] + columnWidths[2], startY + cellHeight, paint);
                canvas.drawRect(x + columnWidths[0] + columnWidths[1] + columnWidths[2], startY, x + 460, startY + cellHeight, paint);

                paint.setStyle(Paint.Style.FILL);
                canvas.drawText("Total", startX + 150, startY + 20, paint);
                canvas.drawText("₹" + total, startX + 370, startY + 20, paint);

                pdfDoc.finishPage(page);

                // Save file
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file = new File(dir, "Bill_" + order.id + ".pdf");
                FileOutputStream out = new FileOutputStream(file);
                pdfDoc.writeTo(out);
                pdfDoc.close();
                out.close();

                runOnUiThread(() -> Toast.makeText(this, "PDF saved to Downloads", Toast.LENGTH_SHORT).show());
                updateOrderStatus("PDF Generated");

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Failed to create PDF", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }


    private void updateToggleButton(String status) {
        btnToggleStatus.setText(status.equals("Completed") ? "Mark as Uncompleted" : "Mark as Completed");
    }
    private void updateOrderStatus(String newStatus) {
        new Thread(() -> {
            currentOrder.status = newStatus;
            AppDatabase.getInstance(this).orderDao().update(currentOrder);

            runOnUiThread(() -> {
                Toast.makeText(this, "Order status updated", Toast.LENGTH_SHORT).show();
                txtTotal.setText("Total: ₹" + currentOrder.total + " (" + currentOrder.status + ")");
                updateToggleButton(newStatus);
            });
        }).start();
    }
}

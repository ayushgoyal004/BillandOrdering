//package com.example.testorder.activities;
//
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.pdf.PdfDocument;
//import android.os.Bundle;
//import android.os.Environment;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import com.example.testorder.R;
//import com.example.testorder.database.AppDatabase;
//import com.example.testorder.models.Client;
//import com.example.testorder.models.Order;
//import com.example.testorder.models.OrderItem;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//public class BillPreviewActivity extends AppCompatActivity {
//
//    TextView txtClient, txtDate, txtSubtotal, txtTax, txtDiscount, txtTotal;
//    LinearLayout layoutItems;
//    Button btnGeneratePdf;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bill_preview);
//
//        txtClient = findViewById(R.id.txtClient);
//        txtDate = findViewById(R.id.txtDate);
//        txtSubtotal = findViewById(R.id.txtSubtotal);
//        txtTax = findViewById(R.id.txtTax);
//        txtDiscount = findViewById(R.id.txtDiscount);
//        txtTotal = findViewById(R.id.txtTotal);
//        btnGeneratePdf = findViewById(R.id.btnGeneratePdf);
//        layoutItems = findViewById(R.id.layoutItems);
//
//        int orderId = getIntent().getIntExtra("orderId", -1);
//        if (orderId == -1) {
//            finish();
//            return;
//        }
//
//        new Thread(() -> {
//            AppDatabase db = AppDatabase.getInstance(this);
//            Order order = db.orderDao().getOrderById(orderId);
//            Client client = db.clientDao().getClientById(order.clientId);
//            List<OrderItem> orderItems = db.orderItemDao().getItemsForOrder(orderId);
//
//            runOnUiThread(() -> {
//                txtClient.setText("Client: " + client.businessName);
//
//                // Format date
//                String formattedDate = order.date;
//                try {
//                    long timestamp = Long.parseLong(order.date);
//                    formattedDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date(timestamp));
//                } catch (Exception e) {
//                    // Use original date if parsing fails
//                }
//                txtDate.setText("Date: " + formattedDate);
//
//                txtSubtotal.setText("Subtotal: ₹" + order.subtotal);
//                txtTax.setText("Tax: ₹" + order.tax);
//                txtDiscount.setText("Discount: ₹" + order.discount);
//                txtTotal.setText("Total: ₹" + order.total);
//                btnGeneratePdf.setOnClickListener(v -> {
//                    new Thread(() -> {
//                        try {
//                            PdfDocument pdfDoc = new PdfDocument();
//                            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
//                            PdfDocument.Page page = pdfDoc.startPage(pageInfo);
//                            Canvas canvas = page.getCanvas();
//                            Paint paint = new Paint();
//                            paint.setTextSize(12);
//
//                            int y = 25;
//                            canvas.drawText("Order Bill", 100, y, paint);
//                            y += 20;
//                            canvas.drawText("Client: " + client.businessName, 10, y, paint);
//                            y += 20;
//                            canvas.drawText("Date: " + order.date, 10, y, paint);
//                            y += 20;
//                            canvas.drawText("Status: " + order.status, 10, y, paint);
//                            y += 20;
//
//                            canvas.drawText("Items:", 10, y, paint);
//                            y += 20;
//
//                            for (OrderItem item : orderItems) {
//                                canvas.drawText("- " + item.itemName + ": " + item.quantity + " x ₹" + item.unitPrice, 10, y, paint);
//                                y += 20;
//                            }
//
//                            y += 10;
//                            canvas.drawText("Subtotal: ₹" + order.subtotal, 10, y, paint);
//                            y += 20;
//                            canvas.drawText("Tax: ₹" + order.tax, 10, y, paint);
//                            y += 20;
//                            canvas.drawText("Discount: ₹" + order.discount, 10, y, paint);
//                            y += 20;
//                            canvas.drawText("Total: ₹" + order.total, 10, y, paint);
//
//                            pdfDoc.finishPage(page);
//
//                            String fileName = "Bill_" + order.id + ".pdf";
//                            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//                            File file = new File(dir, fileName);
//                            FileOutputStream out = new FileOutputStream(file);
//                            pdfDoc.writeTo(out);
//                            pdfDoc.close();
//                            out.close();
//
//                            runOnUiThread(() -> Toast.makeText(this, "PDF saved to Downloads", Toast.LENGTH_SHORT).show());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            runOnUiThread(() -> Toast.makeText(this, "Failed to create PDF", Toast.LENGTH_SHORT).show());
//                        }
//                    }).start();
//                });
//
//
//
//                layoutItems.removeAllViews();
//                for (OrderItem item : orderItems) {
//                    TextView itemText = new TextView(this);
//                    double total = item.quantity * item.unitPrice;
//                    itemText.setText("- " + item.itemName + " (Qty: " + item.quantity + ", ₹" + item.unitPrice + ", Total: ₹" + total + ")");
//                    layoutItems.addView(itemText);
//                }
//            });
//        }).start();
//    }
//}

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
            });
        }).start();
    }

    private void generatePdf(Order order, Client client, List<OrderItem> orderItems) {
        new Thread(() -> {
            try {
                PdfDocument pdfDoc = new PdfDocument();
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
                PdfDocument.Page page = pdfDoc.startPage(pageInfo);
                Canvas canvas = page.getCanvas();
                Paint paint = new Paint();
                paint.setTextSize(12);

                int y = 25;
                canvas.drawText("Order Bill", 100, y, paint);
                y += 20;
                canvas.drawText("Client: " + client.businessName, 10, y, paint);
                y += 20;
                canvas.drawText("Date: " + formattedDate, 10, y, paint);
                y += 20;
                canvas.drawText("Status: " + order.status, 10, y, paint);
                y += 20;

                canvas.drawText("Items:", 10, y, paint);
                y += 20;

                for (OrderItem item : orderItems) {
                    canvas.drawText("- " + item.itemName + ": " + item.quantity + " x ₹" + item.unitPrice, 10, y, paint);
                    y += 20;
                }

                y += 10;
                canvas.drawText("Subtotal: ₹" + order.subtotal, 10, y, paint);
                y += 20;
                canvas.drawText("Tax: ₹" + order.tax, 10, y, paint);
                y += 20;
                canvas.drawText("Discount: ₹" + order.discount, 10, y, paint);
                y += 20;
                canvas.drawText("Total: ₹" + order.total, 10, y, paint);

                pdfDoc.finishPage(page);

                String fileName = "Bill_" + order.id + ".pdf";
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file = new File(dir, fileName);
                FileOutputStream out = new FileOutputStream(file);
                pdfDoc.writeTo(out);
                pdfDoc.close();
                out.close();

                runOnUiThread(() -> Toast.makeText(this, "PDF saved to Downloads", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Failed to create PDF", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}

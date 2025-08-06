package com.example.testorder.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testorder.R;
import com.example.testorder.database.AppDatabase;
import com.example.testorder.models.Item;

import java.util.List;

public class StockRegisterActivity extends AppCompatActivity {

    LinearLayout layoutStockItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_register);

        layoutStockItems = findViewById(R.id.layoutStockItems);

        new Thread(() -> {
            List<Item> items = AppDatabase.getInstance(this).itemDao().getAllItems();
            runOnUiThread(() -> {
                for (Item item : items) {
                    addItemRow(item);
                }
            });
        }).start();
    }

    private void addItemRow(Item item) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(0, 16, 0, 16);

        TextView name = new TextView(this);
        name.setText(item.itemName + " (Stock: " + item.stock + ")");
        name.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2));

        EditText quantityInput = new EditText(this);
        quantityInput.setHint("Add Qty");
        quantityInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        quantityInput.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        Button addButton = new Button(this);
        addButton.setText("Add");
        addButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        addButton.setOnClickListener(v -> {
            String qtyStr = quantityInput.getText().toString().trim();
            if (!qtyStr.isEmpty()) {
                double qty = Double.parseDouble(qtyStr);
                new Thread(() -> {
                    item.stock += qty;
                    AppDatabase.getInstance(this).itemDao().update(item);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Stock updated", Toast.LENGTH_SHORT).show();
                        name.setText(item.itemName + " (Stock: " + item.stock + ")");
                        quantityInput.setText("");
                    });
                }).start();
            }
        });

        row.addView(name);
        row.addView(quantityInput);
        row.addView(addButton);

        layoutStockItems.addView(row);
    }
}

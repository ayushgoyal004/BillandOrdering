package com.example.testorder.activities;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.testorder.R;
import com.example.testorder.database.AppDatabase;
import com.example.testorder.models.Item;

public class AddItemActivity extends AppCompatActivity {

    EditText editItemName;
    Button btnSaveItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        editItemName = findViewById(R.id.editItemName);
        btnSaveItem = findViewById(R.id.btnSaveItem);

        btnSaveItem.setOnClickListener(view -> {
            String itemName = editItemName.getText().toString().trim();

            if (itemName.isEmpty()) {
                Toast.makeText(this, "Item name required", Toast.LENGTH_SHORT).show();
                return;
            }

            Item newItem = new Item(itemName);

            new Thread(() -> {
                AppDatabase.getInstance(this).itemDao().insert(newItem);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show();
                    editItemName.setText("");
                });
            }).start();
        });
    }
}

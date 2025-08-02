package com.example.testorder.activities;

import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.testorder.R;
import com.example.testorder.database.AppDatabase;
import com.example.testorder.models.Item;

import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends AppCompatActivity {

    EditText editItemName;
    Button btnSaveItem;
    ListView listViewItems;

    ArrayAdapter<String> adapter;
    List<Item> itemList = new ArrayList<>();
    int selectedItemIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        editItemName = findViewById(R.id.editItemName);
        btnSaveItem = findViewById(R.id.btnSaveItem);
        listViewItems = findViewById(R.id.listViewItems);

        loadItems();

        btnSaveItem.setOnClickListener(v -> {
            String itemName = editItemName.getText().toString().trim();
            if (itemName.isEmpty()) {
                Toast.makeText(this, "Item name required", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(this);

                if (selectedItemIndex == -1) {
                    // Add new item
                    db.itemDao().insert(new Item(itemName));
                } else {
                    // Update existing item
                    Item selectedItem = itemList.get(selectedItemIndex);
                    selectedItem.itemName = itemName;
                    db.itemDao().update(selectedItem);
                }

                runOnUiThread(() -> {
                    editItemName.setText("");
                    selectedItemIndex = -1;
                    btnSaveItem.setText("Add Item");
                    loadItems();
                });
            }).start();
        });

        listViewItems.setOnItemClickListener((parent, view, position, id) -> {
            editItemName.setText(itemList.get(position).itemName);
            selectedItemIndex = position;
            btnSaveItem.setText("Update Item");
        });

        listViewItems.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Item?")
                    .setMessage("Do you want to delete this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        new Thread(() -> {
                            AppDatabase.getInstance(this).itemDao().delete(itemList.get(position));
                            runOnUiThread(this::loadItems);
                        }).start();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });
    }

    private void loadItems() {
        new Thread(() -> {
            itemList = AppDatabase.getInstance(this).itemDao().getAllItems();
            List<String> names = new ArrayList<>();
            for (Item i : itemList) names.add(i.itemName);

            runOnUiThread(() -> {
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
                listViewItems.setAdapter(adapter);
            });
        }).start();
    }
}


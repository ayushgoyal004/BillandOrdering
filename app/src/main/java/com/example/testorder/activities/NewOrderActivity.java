package com.example.testorder.activities;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.testorder.R;

public class NewOrderActivity extends AppCompatActivity {

    Spinner clientSpinner;
    LinearLayout layoutItemInputs;
    Button btnSubmitOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        clientSpinner = findViewById(R.id.clientSpinner);
        layoutItemInputs = findViewById(R.id.layoutItemInputs);
        btnSubmitOrder = findViewById(R.id.btnSubmitOrder);

        // Dummy data
        String[] clients = {"Client A", "Client B", "Client C"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clients);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clientSpinner.setAdapter(adapter);

        // Example: Add 2 items dynamically (you can change this logic)
        addItemInput("Sugar");
        addItemInput("Wheat");

        btnSubmitOrder.setOnClickListener(v -> {
            Toast.makeText(this, "Order submitted (dummy action)", Toast.LENGTH_SHORT).show();
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
        quantityInput.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        EditText priceInput = new EditText(this);
        priceInput.setHint("Price/unit");
        priceInput.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        row.addView(itemLabel);
        row.addView(quantityInput);
        row.addView(priceInput);

        layoutItemInputs.addView(row);
    }
}

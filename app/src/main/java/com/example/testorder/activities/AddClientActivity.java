package com.example.testorder.activities;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.testorder.R;
import com.example.testorder.database.AppDatabase;
import com.example.testorder.models.Client;
import java.util.concurrent.Executors;

public class AddClientActivity extends AppCompatActivity {

    EditText inputName, inputPhone;
    Button btnSaveClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        inputName = findViewById(R.id.inputName);
        inputPhone = findViewById(R.id.inputPhone);
        btnSaveClient = findViewById(R.id.btnSaveClient);

        btnSaveClient.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            String phone = inputPhone.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Client client = new Client(name, phone);
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase.getInstance(this).clientDao().insert(client);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Client saved", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });
    }
}

package com.example.testorder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.testorder.R;

public class UserDashboardActivity extends AppCompatActivity {

    Button btnNewOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        btnNewOrder = findViewById(R.id.btnNewOrder);
        btnNewOrder.setOnClickListener(v -> startActivity(new Intent(this, NewOrderActivity.class)));
    }
}

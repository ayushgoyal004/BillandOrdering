package com.example.testorder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.testorder.R;

public class LoginActivity extends AppCompatActivity {

    Button btnAdmin, btnUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnAdmin = findViewById(R.id.btnAdminLogin);
        btnUser = findViewById(R.id.btnUserLogin);

        btnAdmin.setOnClickListener(view ->
                startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class)));

//        btnUser.setOnClickListener(view ->
//                startActivity(new Intent(LoginActivity.this, UserDashboardActivity.class)));
    }
}

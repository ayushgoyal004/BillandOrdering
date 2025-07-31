package com.example.testorder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.testorder.R;

public class LoginActivity extends AppCompatActivity {

    Button btnAdmin, btnUser;
    EditText editEmail,editPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnAdmin = findViewById(R.id.btnAdminLogin);
        btnUser = findViewById(R.id.btnUserLogin);
        editEmail = findViewById(R.id.editEmail);
        editPassword=findViewById(R.id.editPassword);
        btnAdmin.setOnClickListener(view -> {
            String id = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (id.equals("admin") && password.equals("admin123")) {
                startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Invalid Admin credentials", Toast.LENGTH_SHORT).show();
            }
        });
        btnUser.setOnClickListener(view -> {
            // No credential check yet for users
            startActivity(new Intent(LoginActivity.this, UserDashboardActivity.class));
            finish();
        });
    }
}

package com.example.testorder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testorder.R;
import com.example.testorder.adapters.ClientsAdapter;
import com.example.testorder.database.AppDatabase;

public class ClientsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText inputSearch;
    Button btnAddClient;
    ClientsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);

        recyclerView = findViewById(R.id.recyclerClients);
        inputSearch = findViewById(R.id.inputSearch);
        btnAddClient = findViewById(R.id.btnAddClient);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ClientsAdapter();
        recyclerView.setAdapter(adapter);

        loadClients("");

        inputSearch.addTextChangedListener(new android.text.TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadClients(s.toString());
            }
            public void afterTextChanged(android.text.Editable s) {}
        });

        btnAddClient.setOnClickListener(v ->
                startActivity(new Intent(this, AddClientActivity.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadClients(inputSearch.getText().toString());
    }

    private void loadClients(String keyword) {
        new Thread(() -> {
            var list = AppDatabase.getInstance(this).clientDao().searchClients("%" + keyword + "%");
            runOnUiThread(() -> adapter.setData(list));
        }).start();
    }
}

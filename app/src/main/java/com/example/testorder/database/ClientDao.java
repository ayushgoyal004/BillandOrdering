package com.example.testorder.database;

import androidx.room.*;
import com.example.testorder.models.Client;
import java.util.List;

@Dao
public interface ClientDao {
    @Insert
    void insert(Client client);

    @Update
    void update(Client client);

    @Delete
    void delete(Client client);

    @Query("SELECT * FROM clients ORDER BY businessName ASC")
    List<Client> getAllClients();
}

package com.example.testorder.adapters;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testorder.R;
//import com.example.testorder.database.entities.Client;
import com.example.testorder.models.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.ClientViewHolder> {

    private List<Client> clients = new ArrayList<>();

    public void setData(List<Client> list) {
        clients = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_client, parent, false);
        return new ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        Client client = clients.get(position);
        holder.name.setText(client.getName());
        holder.phone.setText(client.getPhone());
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    static class ClientViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone;

        ClientViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.clientName);
            phone = itemView.findViewById(R.id.clientPhone);
        }
    }
}

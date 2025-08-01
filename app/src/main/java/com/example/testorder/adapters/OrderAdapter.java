//package com.example.testorder.adapters;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.testorder.R;
//import com.example.testorder.models.Order;
//import java.util.List;
//
//public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
//
//    private final List<Order> orders;
//
//    public OrderAdapter(List<Order> orders) {
//        this.orders = orders;
//    }
//
//    @NonNull
//    @Override
//    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
//        return new OrderViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
//        Order order = orders.get(position);
//        holder.clientName.setText(order.clientName);
//        holder.date.setText(order.date);
//        holder.total.setText("Total: ₹" + order.total);
//    }
//
//    @Override
//    public int getItemCount() {
//        return orders.size();
//    }
//
//    public static class OrderViewHolder extends RecyclerView.ViewHolder {
//        TextView clientName, date, total;
//
//        public OrderViewHolder(@NonNull View itemView) {
//            super(itemView);
//            clientName = itemView.findViewById(R.id.textClientName);
//            date = itemView.findViewById(R.id.textOrderDate);
//            total = itemView.findViewById(R.id.textOrderTotal);
//        }
//    }
//}
package com.example.testorder.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testorder.R;
import com.example.testorder.activities.BillPreviewActivity;
import com.example.testorder.models.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.txtClientName.setText(order.clientName);
        holder.txtAmount.setText("₹" + order.total);
        holder.txtStatus.setText(order.status);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BillPreviewActivity.class);
            intent.putExtra("orderId", order.id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtClientName, txtAmount, txtStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtClientName = itemView.findViewById(R.id.txtClientName);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtStatus = itemView.findViewById(R.id.txtStatus);
        }
    }
    private String formatDate(String millisStr) {
        try {
            long millis = Long.parseLong(millisStr);
            return new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date(millis));
        } catch (Exception e) {
            return millisStr;
        }
    }
}

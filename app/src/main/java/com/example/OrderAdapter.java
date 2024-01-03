package com.example;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uidesign.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<OrderItem> orderItemList;

    public OrderAdapter(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderItem orderItem = orderItemList.get(position);

        // 将数据绑定到视图上
        holder.senderAddressTextView.setText(orderItem.getSenderAddress());
        holder.receiverAddressTextView.setText(orderItem.getReceiverAddress());
        holder.itemTypeTextView.setText(orderItem.getItemType());
        holder.deliveryTimeTextView.setText(orderItem.getDeliveryTime());
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView senderAddressTextView;
        TextView receiverAddressTextView;
        TextView itemTypeTextView;
        TextView deliveryTimeTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderAddressTextView = itemView.findViewById(R.id.senderAddressTextView);
            receiverAddressTextView = itemView.findViewById(R.id.receiverAddressTextView);
            itemTypeTextView = itemView.findViewById(R.id.itemTypeTextView);
            deliveryTimeTextView = itemView.findViewById(R.id.deliveryTimeTextView);
        }
    }

    // 删除 addOrderItem 方法
}

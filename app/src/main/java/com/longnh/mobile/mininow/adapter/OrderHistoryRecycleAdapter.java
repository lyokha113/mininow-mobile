package com.longnh.mobile.mininow.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longnh.mobile.mininow.R;
import com.longnh.mobile.mininow.entity.Order;
import com.longnh.mobile.mininow.ultils.ConstantManager;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class OrderHistoryRecycleAdapter extends RecyclerView.Adapter<OrderHistoryRecycleAdapter.DataViewHolder> {

    private List<Order> orders;
    private Activity activity;

    public OrderHistoryRecycleAdapter(Activity activity, List<Order> orders) {
        this.orders = orders;
        this.activity = activity;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_history_item, viewGroup, false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderHistoryRecycleAdapter.DataViewHolder holder, int position) {
            Order order = orders.get(position);
            holder.name.setText(order.getStore().getName());
            holder.address.setText(order.getStore().getAddress());
            holder.orderId.setText(order.getId() + "");
            holder.orderTime.setText(order.getOrderTime().toString());
            holder.finishedTime.setText(order.getFinishedTime() != null ? order.getFinishedTime().toString() : "");
            holder.total.setText((order.getProductPrice() + order.getShipPrice()) + " VND");
            if (order.getStatus() == ConstantManager.ORDER_DONE) {
                holder.status.setText("Hoàn thành");
            } else if (order.getStatus() == ConstantManager.ORDER_FAILED) {
                holder.status.setText("Giao thất bại");
            } else if ((order.getStatus() == ConstantManager.ORDER_REJECTED)) {
                holder.status.setText("Huỷ");
            }

    }

    @Override
    public int getItemCount() {
        return orders == null ? 0 : orders.size();
    }


    public class DataViewHolder extends RecyclerView.ViewHolder {

        private TextView name, address, orderId, orderTime, finishedTime, total, status;

        public DataViewHolder(final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.store_name);
            address = itemView.findViewById(R.id.store_address);
            orderId = itemView.findViewById(R.id.order_id);
            orderTime = itemView.findViewById(R.id.order_time);
            finishedTime = itemView.findViewById(R.id.finished_time);
            total = itemView.findViewById(R.id.total);
            status = itemView.findViewById(R.id.status);
        }
    }
}


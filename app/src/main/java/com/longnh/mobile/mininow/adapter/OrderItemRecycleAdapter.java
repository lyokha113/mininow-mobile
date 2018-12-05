package com.longnh.mobile.mininow.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longnh.mobile.mininow.R;
import com.longnh.mobile.mininow.model.OrderItem;
import com.longnh.mobile.mininow.model.ProductExtra;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderItemRecycleAdapter extends RecyclerView.Adapter<OrderItemRecycleAdapter.ItemViewHolder> {

    private List<OrderItem> items;
    private Activity activity;

    public OrderItemRecycleAdapter(Activity activity, List<OrderItem> items) {
        this.items = items;
        this.activity = activity;
    }

    @Override
    public OrderItemRecycleAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderitem_row_item, parent, false);
        return new OrderItemRecycleAdapter.ItemViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemRecycleAdapter.ItemViewHolder holder, int position) {
        OrderItem orderItem = items.get(position);
        holder.name.setText(orderItem.getName() + " x " + String.valueOf(orderItem.getQuantity()));
        holder.price.setText(String.valueOf(orderItem.getTotalPrice()) + " VND");

        String extra = "";
        for (ProductExtra productExtra : orderItem.getExtras()) {
            extra += productExtra.getName() + "\n";
        }
        holder.extra.setText(extra);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView extra;
        private TextView price;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            price = itemView.findViewById(R.id.item_price);
            extra = itemView.findViewById(R.id.item_extra);
        }
    }

}

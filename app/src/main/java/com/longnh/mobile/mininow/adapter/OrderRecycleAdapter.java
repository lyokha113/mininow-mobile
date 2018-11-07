package com.longnh.mobile.mininow.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longnh.mobile.mininow.ProductActivity;
import com.longnh.mobile.mininow.R;
import com.longnh.mobile.mininow.TrackingActivity;
import com.longnh.mobile.mininow.entity.OrderItem;
import com.longnh.mobile.mininow.entity.Store;
import com.longnh.mobile.mininow.ultils.ConstantManager;
import com.longnh.mobile.mininow.ultils.JsonUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderRecycleAdapter extends RecyclerView.Adapter<OrderRecycleAdapter.ItemViewHolder> {

    private List<Store> stores;
    private Activity activity;

    public OrderRecycleAdapter(Activity activity, List<Store> stores) {
        this.stores = stores;
        this.activity = activity;
    }

    @Override
    public OrderRecycleAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.draft_row_item, parent, false);
        return new OrderRecycleAdapter.ItemViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderRecycleAdapter.ItemViewHolder holder, int position) {
        Store store = stores.get(position);
        holder.name.setText(store.getName());
        holder.address.setText(store.getAddress());

        SharedPreferences sharedPreferences = activity.getApplication().getApplicationContext().getSharedPreferences(ConstantManager.ORDER_TEMPORARY, Context.MODE_PRIVATE);
        Set<String> saved = sharedPreferences.getStringSet(store.getId(), new HashSet<>());
        int quantity = 0;
        for (String s : saved) {
            OrderItem item = JsonUtil.getObject(s, OrderItem.class);
            quantity += item.getQuantity();
        }
        holder.quantity.setText(quantity + " phần");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(activity, TrackingActivity.class);
            intent.putExtra("storeID", stores.get(position).getId());
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return stores == null ? 0 : stores.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView address;
        private TextView quantity;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.store_name);
            address = itemView.findViewById(R.id.store_address);
            quantity = itemView.findViewById(R.id.order_quantity);
        }
    }

}

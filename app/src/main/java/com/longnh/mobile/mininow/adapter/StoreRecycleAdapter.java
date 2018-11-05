package com.longnh.mobile.mininow.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.longnh.mobile.mininow.ProductActivity;
import com.longnh.mobile.mininow.R;
import com.longnh.mobile.mininow.entity.Store;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StoreRecycleAdapter extends RecyclerView.Adapter<StoreRecycleAdapter.StoreViewHolder> {

    private List<Store> stores;
    private Activity activity;

    public StoreRecycleAdapter(Activity activity, List<Store> stores) {
        this.stores = stores;
        this.activity = activity;
    }

    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_row_item, parent, false);
        return new StoreViewHolder(item);
    }

    @Override
    public void onBindViewHolder(StoreViewHolder holder, int position) {
        Store store = stores.get(position);

        holder.storeName.setText(store.getName());
        holder.storeAddress.setText(store.getAddress());
        holder.storeDescription.setText(store.getDescription());

        if (store.getImgUrl() != null && !store.getImgUrl().isEmpty()) {
            Picasso.get().load(store.getImgUrl())
                    .resize(110, 80)
                    .centerCrop()
                    .into(holder.storeImg);
        }

        if (store.getDistance() != null) {
            holder.storeDistance.setText(store.getDistance());
        }

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ProductActivity.class);
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return stores == null ? 0 : stores.size();
    }

    class StoreViewHolder extends RecyclerView.ViewHolder {

        private ImageView storeImg;
        private TextView storeName;
        private TextView storeAddress;
        private TextView storeDescription;
        private TextView storeDistance;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            storeImg = itemView.findViewById(R.id.store_img);
            storeName = itemView.findViewById(R.id.store_name);
            storeAddress = itemView.findViewById(R.id.store_address);
            storeDescription = itemView.findViewById(R.id.store_description);
            storeDistance = itemView.findViewById(R.id.store_distance);
        }
    }
}

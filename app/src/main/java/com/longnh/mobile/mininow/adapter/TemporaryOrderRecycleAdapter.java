package com.longnh.mobile.mininow.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longnh.mobile.mininow.R;
import com.longnh.mobile.mininow.entity.OrderItem;
import com.longnh.mobile.mininow.entity.Store;
import com.longnh.mobile.mininow.ultils.ConstantManager;
import com.longnh.mobile.mininow.ultils.JsonUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class TemporaryOrderRecycleAdapter extends RecyclerView.Adapter<TemporaryOrderRecycleAdapter.ItemViewHolder> {

    private List<Store> stores;
    private Activity activity;

    public TemporaryOrderRecycleAdapter(Activity activity, List<Store> stores) {
        this.stores = stores;
        this.activity = activity;
    }

    @Override
    public TemporaryOrderRecycleAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.draft_row_item, parent, false);
        return new TemporaryOrderRecycleAdapter.ItemViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull TemporaryOrderRecycleAdapter.ItemViewHolder holder, int position) {
        Store store = stores.get(position);

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
//            name = itemView.findViewById(R.id.item_name);
//            price = itemView.findViewById(R.id.item_price);
        }
    }

}

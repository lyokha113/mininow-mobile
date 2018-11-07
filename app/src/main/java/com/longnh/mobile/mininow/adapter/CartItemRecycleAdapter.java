package com.longnh.mobile.mininow.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.longnh.mobile.mininow.CartActivity;
import com.longnh.mobile.mininow.R;
import com.longnh.mobile.mininow.entity.OrderItem;
import com.longnh.mobile.mininow.ultils.ConstantManager;
import com.longnh.mobile.mininow.ultils.JsonUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartItemRecycleAdapter extends RecyclerView.Adapter<CartItemRecycleAdapter.ItemViewHolder> {

    private List<OrderItem> items;
    private Activity activity;
    private String storeID;

    public CartItemRecycleAdapter(Activity activity, List<OrderItem> items, String storeID) {
        this.items = items;
        this.activity = activity;
        this.storeID = storeID;
    }

    @Override
    public CartItemRecycleAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartitem_row_item, parent, false);
        return new CartItemRecycleAdapter.ItemViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemRecycleAdapter.ItemViewHolder holder, int position) {
        OrderItem orderItem = items.get(position);
        holder.name.setText(orderItem.getName() + " x " + String.valueOf(orderItem.getQuantity()));
        holder.price.setText(String.valueOf(orderItem.getTotalPrice()) + " VND");
        holder.itemView.setOnLongClickListener(v -> {

            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            alert.setTitle("Xác nhận");
            alert.setMessage("Bạn có muốn xoá sản phẩm này");
            alert.setPositiveButton("Xoá", (dialog, which) -> {
                SharedPreferences sharedPreferences = activity.getApplication().getApplicationContext().getSharedPreferences(ConstantManager.ORDER_TEMPORARY, Context.MODE_PRIVATE);

                Set<String> saved = sharedPreferences.getStringSet(storeID, null);
                if (saved != null) {

                    for (Iterator<String> item = saved.iterator(); item.hasNext();) {
                        String element = item.next();
                        OrderItem obj = JsonUtil.getObject(element, OrderItem.class);
                        if (obj.getTime().equals(orderItem.getTime())) {
                            saved.remove(element);
                            break;
                        }
                    }

                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.remove(storeID);
                    edit.putStringSet(storeID, saved);
                    edit.apply();

                    items.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), items.size());
                }


                dialog.dismiss();

            });
            alert.setNegativeButton("Huỷ", (dialog, which) -> dialog.dismiss());
            alert.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView price;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            price = itemView.findViewById(R.id.item_price);
        }
    }

}

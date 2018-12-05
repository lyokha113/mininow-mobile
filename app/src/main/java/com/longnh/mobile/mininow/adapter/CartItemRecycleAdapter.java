package com.longnh.mobile.mininow.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.longnh.mobile.mininow.R;
import com.longnh.mobile.mininow.model.OrderItem;
import com.longnh.mobile.mininow.model.ProductExtra;
import com.longnh.mobile.mininow.ultils.ConstantManager;
import com.longnh.mobile.mininow.ultils.JsonUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class CartItemRecycleAdapter extends RecyclerView.Adapter<CartItemRecycleAdapter.ItemViewHolder> {

    private List<OrderItem> items;
    private TextView total;
    private Activity activity;
    private String storeID;
    private SharedPreferences sharedPreferences;

    public CartItemRecycleAdapter(Activity activity, List<OrderItem> items, String storeID, TextView total) {
        this.items = items;
        this.activity = activity;
        this.storeID = storeID;
        this.total = total;
        sharedPreferences = activity.getApplication().getApplicationContext().getSharedPreferences(
                ConstantManager.ORDER_TEMPORARY, Context.MODE_PRIVATE);
    }

    @Override
    public CartItemRecycleAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartitem_row_item, parent, false);
        return new CartItemRecycleAdapter.ItemViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemRecycleAdapter.ItemViewHolder holder, int position) {

        OrderItem orderItem = items.get(position);
        holder.name.setText(orderItem.getName());
        holder.price.setText(String.valueOf(orderItem.getTotalPrice()) + " VND");
        holder.quantity.setText(String.valueOf(orderItem.getQuantity()));

        String extra = "";
        for (ProductExtra productExtra : orderItem.getExtras()) {
            extra += productExtra.getName() + "\n";
        }

        holder.extra.setText(extra);

        holder.btnAdd.setOnClickListener(v -> {

            Set<String> savedProducts = new HashSet<>(
                    sharedPreferences.getStringSet(storeID, new HashSet<>()));
            List<String> toRemove = new ArrayList<>();
            savedProducts.forEach(ele -> {
                OrderItem savedItem = JsonUtil.getObject(ele, OrderItem.class);
                if (savedItem.getProductID() == orderItem.getProductID()
                        && savedItem.getExtras().equals(orderItem.getExtras())) {
                    orderItem.setQuantity(savedItem.getQuantity() + 1);
                    holder.quantity.setText(String.valueOf(orderItem.getQuantity()));
                    toRemove.add(ele);
                }
            });

            savedProducts.removeAll(toRemove);
            savedProducts.add(JsonUtil.getJson(orderItem));

            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove(storeID);
            edit.putStringSet(storeID, savedProducts);
            edit.apply();

            setTotal();
        });

        holder.btnSub.setOnClickListener(v -> {

            Set<String> savedProducts = new HashSet<>(
                    sharedPreferences.getStringSet(storeID, new HashSet<>()));

            int quantityNum = Integer.parseInt(holder.quantity.getText().toString()) - 1;
            if (quantityNum > 0) {
                List<String> toRemove = new ArrayList<>();
                savedProducts.forEach(ele -> {
                    OrderItem savedItem = JsonUtil.getObject(ele, OrderItem.class);
                    if (savedItem.getProductID() == orderItem.getProductID()
                            && savedItem.getExtras().equals(orderItem.getExtras())) {
                        orderItem.setQuantity(savedItem.getQuantity() - 1);
                        holder.quantity.setText(String.valueOf(orderItem.getQuantity()));
                        toRemove.add(ele);
                    }
                });

                savedProducts.removeAll(toRemove);
                savedProducts.add(JsonUtil.getJson(orderItem));
                notifyItemChanged(holder.getAdapterPosition());

                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.remove(storeID);
                edit.putStringSet(storeID, savedProducts);
                edit.apply();

                setTotal();
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setTitle("Xác nhận");
                alert.setMessage("Bạn có muốn xoá sản phẩm này");
                alert.setPositiveButton("Xoá", (dialog, which) -> {

                    List<String> toRemove = new ArrayList<>();
                    savedProducts.forEach(ele -> {
                        OrderItem savedItem = JsonUtil.getObject(ele, OrderItem.class);
                        if (savedItem.getProductID() == orderItem.getProductID()
                                && savedItem.getExtras().equals(orderItem.getExtras())) {
                            orderItem.setQuantity(savedItem.getQuantity() - 1);
                            holder.quantity.setText(String.valueOf(orderItem.getQuantity()));
                            toRemove.add(ele);
                        }
                    });

                    savedProducts.removeAll(toRemove);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.remove(storeID);
                    edit.putStringSet(storeID, savedProducts);
                    edit.apply();

                    items.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), items.size());
                    setTotal();
                    dialog.dismiss();

                });
                alert.setNegativeButton("Huỷ", (dialog, which) -> dialog.dismiss());
                alert.show();
            }
        });
    }

    private void setTotal() {

        Set<String> saveProducts = sharedPreferences.getStringSet(storeID, null);

        int totalPrice = 0;
        if (saveProducts != null) {
            for (String saved : saveProducts) {
                OrderItem obj = JsonUtil.getObject(saved, OrderItem.class);
                totalPrice += obj.getTotalPrice();
            }
        }

        total.setText(String.valueOf(totalPrice) + " VND");
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView extra;
        private TextView quantity;
        private TextView price;
        private ImageButton btnAdd, btnSub;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            price = itemView.findViewById(R.id.item_price);
            extra = itemView.findViewById(R.id.item_extra);
            quantity = itemView.findViewById(R.id.product_quantity);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnSub = itemView.findViewById(R.id.btnSub);

        }
    }

}

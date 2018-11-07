package com.longnh.mobile.mininow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.longnh.mobile.mininow.adapter.CartItemRecycleAdapter;
import com.longnh.mobile.mininow.adapter.OrderItemRecycleAdapter;
import com.longnh.mobile.mininow.entity.OrderItem;
import com.longnh.mobile.mininow.ultils.ConstantManager;
import com.longnh.mobile.mininow.ultils.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CartActivity extends AppCompatActivity {

    private CartItemRecycleAdapter adapter;
    private RecyclerView orderItems;
    private List<OrderItem> items;
    private String storeID;
    private Button removeAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        storeID = getIntent().getStringExtra(ConstantManager.ORDER_CONFIRM);
        addControls();
        addEvents();

        getItems();
    }

    private void addEvents() {
        removeAll.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(CartActivity.this);
            alert.setTitle("Xác nhận");
            alert.setMessage("Bạn có muốn xoá toàn bộ giỏ hàng");
            alert.setPositiveButton("Xoá", (dialog, which) -> {
                SharedPreferences sharedPreferences = getApplication().getSharedPreferences(ConstantManager.ORDER_TEMPORARY, Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();

                edit.remove(storeID);
                edit.apply();
                adapter = new CartItemRecycleAdapter(CartActivity.this, new ArrayList<>(), storeID);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                orderItems.setLayoutManager(layoutManager);
                orderItems.setHasFixedSize(true);
                orderItems.setAdapter(adapter);

                dialog.dismiss();

            });
            alert.setNegativeButton("Huỷ", (dialog, which) -> dialog.dismiss());
            alert.show();
        });
    }

    private void addControls() {
        orderItems = findViewById(R.id.cart_item);
        removeAll = findViewById(R.id.remove_all);
    }

    private void getItems() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(ConstantManager.ORDER_TEMPORARY, Context.MODE_PRIVATE);
        Set<String> saved = sharedPreferences.getStringSet(storeID, null);
        if (saved != null) {
            items = new ArrayList<>();
            for (String item : saved) {
                OrderItem orderItem = JsonUtil.getObject(item, OrderItem.class);
                items.add(orderItem);
            }

            adapter = new CartItemRecycleAdapter(this, items, storeID);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            orderItems.setLayoutManager(layoutManager);
            orderItems.setHasFixedSize(true);
            orderItems.setAdapter(adapter);
        }
    }
}

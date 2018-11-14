package com.longnh.mobile.mininow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.longnh.mobile.mininow.adapter.CartItemRecycleAdapter;
import com.longnh.mobile.mininow.entity.OrderItem;
import com.longnh.mobile.mininow.ultils.ConstantManager;
import com.longnh.mobile.mininow.ultils.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CartActivity extends AppCompatActivity {

    private CartItemRecycleAdapter adapter;
    private RecyclerView orderItems;
    private TextView total, removeAll, confirmCart;
    private List<OrderItem> items;
    private String storeID, storeName, storeAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        storeID = getIntent().getStringExtra(ConstantManager.STORE_ID);
        storeName = getIntent().getStringExtra(ConstantManager.STORE_NAME);
        storeAddress = getIntent().getStringExtra(ConstantManager.STORE_ADDRESS);
        addControls();
        addEvents();

        getItems();
    }

    private void addEvents() {
        removeAll.setOnClickListener(v -> {
            if (total.getText().toString().equals("0 VND")) return;
            AlertDialog.Builder alert = new AlertDialog.Builder(CartActivity.this);
            alert.setTitle("Xác nhận");
            alert.setMessage("Bạn có muốn xoá toàn bộ giỏ hàng");
            alert.setPositiveButton("Xoá", (dialog, which) -> {
                SharedPreferences sharedPreferences = getApplication().getSharedPreferences(ConstantManager.ORDER_TEMPORARY, Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();

                edit.remove(storeID);
                edit.apply();
                adapter = new CartItemRecycleAdapter(CartActivity.this, new ArrayList<>(), storeID, total);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                orderItems.setLayoutManager(layoutManager);
                orderItems.setHasFixedSize(true);
                orderItems.setAdapter(adapter);

                total.setText("0 VND");

                dialog.dismiss();

            });
            alert.setNegativeButton("Huỷ", (dialog, which) -> dialog.dismiss());
            alert.show();
        });

        confirmCart.setOnClickListener(v -> {
            if (total.getText().toString().equals("0 VND")) return;
            Intent intent = new Intent(this, OrderConfirmActivity.class);
            intent.putExtra(ConstantManager.STORE_ID, storeID);
            intent.putExtra(ConstantManager.STORE_ADDRESS, storeAddress);
            intent.putExtra(ConstantManager.STORE_NAME, storeName);
            startActivity(intent);
        });
    }

    private void addControls() {
        orderItems = findViewById(R.id.cart_item);
        removeAll = findViewById(R.id.remove_all);
        total = findViewById(R.id.total_cart_price);
        confirmCart = findViewById(R.id.confirm_cart);
    }

    private void getItems() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(ConstantManager.ORDER_TEMPORARY, Context.MODE_PRIVATE);
        Set<String> saved = sharedPreferences.getStringSet(storeID, null);
        int totalPrice = 0;
        if (saved != null) {
            items = new ArrayList<>();
            for (String item : saved) {
                OrderItem orderItem = JsonUtil.getObject(item, OrderItem.class);
                totalPrice += orderItem.getTotalPrice();
                items.add(orderItem);
            }

            adapter = new CartItemRecycleAdapter(this, items, storeID, total);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            orderItems.setLayoutManager(layoutManager);
            orderItems.setHasFixedSize(true);
            orderItems.setAdapter(adapter);

        }

        total.setText(String.valueOf(totalPrice) + " VND");
    }
}

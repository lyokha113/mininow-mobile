package com.longnh.mobile.mininow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.longnh.mobile.mininow.adapter.OrderItemRecycleAdapter;
import com.longnh.mobile.mininow.entity.Customer;
import com.longnh.mobile.mininow.entity.Order;
import com.longnh.mobile.mininow.entity.OrderItem;
import com.longnh.mobile.mininow.model.APIManager;
import com.longnh.mobile.mininow.model.OrderService;
import com.longnh.mobile.mininow.ultils.ConstantManager;
import com.longnh.mobile.mininow.ultils.JsonUtil;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderConfirmActivity extends AppCompatActivity {

    private LinearLayout listProducts;
    private OrderItemRecycleAdapter adapter;
    private ImageView customerImg;
    private RecyclerView orderItems;
    private List<OrderItem> orderItemList;
    private TextView orderPrice, shipDistance, shipPrice, totalOrderPrice, paymentCash, customerName,
            customerPhone, changeOrderTime, orderTime;
    private EditText customerAddress, description;
    private Button submitOrder;
    private int orderPriceVal, shipPriceVal, totalorderPriceVal;
    private String storeID, storeAddress, storeName, desAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);

        addControls();
        addEvents();

        setOrderTime(LocalDateTime.now().plusMinutes(30));

        Intent storeIntent = getIntent();
        storeID = storeIntent.getStringExtra(ConstantManager.STORE_ID);
        storeAddress = storeIntent.getStringExtra(ConstantManager.STORE_ADDRESS);
        storeName = storeIntent.getStringExtra(ConstantManager.STORE_NAME);

        desAddress = ConstantManager.customer.getAddress();

        setCustomerInfo();
        setListProductItems();
    }

    private void addControls() {
        description = findViewById(R.id.order_description);
        submitOrder = findViewById(R.id.submitOrder);
        listProducts = findViewById(R.id.list_product);
        changeOrderTime = findViewById(R.id.change_order_time);
        orderTime = findViewById(R.id.order_time);
        customerAddress = findViewById(R.id.customer_address);
        customerName = findViewById(R.id.customer_name);
        customerPhone = findViewById(R.id.customer_phone);
        orderItems = findViewById(R.id.order_items);
        orderPrice = findViewById(R.id.order_price);
        shipDistance = findViewById(R.id.ship_distance);
        shipPrice = findViewById(R.id.ship_price);
        totalOrderPrice = findViewById(R.id.total_order_price);
        paymentCash = findViewById(R.id.payment_cash);
        customerImg = findViewById(R.id.customer_img);
    }

    private void addEvents() {

        description.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                submitOrder.setVisibility(View.GONE);
                listProducts.setVisibility(View.GONE);
            } else {
                submitOrder.setVisibility(View.VISIBLE);
                listProducts.setVisibility(View.VISIBLE);
            }
        });

        changeOrderTime.setOnClickListener(v -> {
            new SingleDateAndTimePickerDialog.Builder(this)
                    .bottomSheet()
                    .curved()
                    .title("Ngày - Giờ nhận hàng")
                    .listener(date -> {
                        Date current = new Date();
                        current.setTime((new Date().getTime() + 3600000));
                        if (date.before(current)) {
                            Toast.makeText(this, "Giờ giao mong muốn cần phải cách ít nhất 30 phút", Toast.LENGTH_SHORT).show();
                        } else {
                            setOrderTime(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
                        }
                    }).display();
        });

        customerAddress.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                submitOrder.setVisibility(View.GONE);
                listProducts.setVisibility(View.GONE);
            } else {
                submitOrder.setVisibility(View.VISIBLE);
                listProducts.setVisibility(View.VISIBLE);
                desAddress = customerAddress.getText().toString();
                setShipInfo();
            }
        });

        submitOrder.setOnClickListener(v -> {
            Order order = new Order();
            order.setCusID(ConstantManager.customerID);
            order.setCusAddress(customerAddress.getText().toString());
            order.setCusName(customerName.getText().toString());
            order.setCusPhone(customerPhone.getText().toString());
            order.setDescription(description.getText().toString());
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm, dd/MM/yyyy");
                Date parsedDate = dateFormat.parse(orderTime.getText().toString());
                order.setExpectedTime(parsedDate);
            } catch (Exception e) {
                return;
            }

            order.setTotalPrice(totalorderPriceVal);
            order.setStoreID(storeID);
            order.setStatus(ConstantManager.ORDER_WAITING);
            order.setDetail(JsonUtil.getJson(orderItemList));
            order.setStoreName(storeName);
            order.setStoreAddress(storeAddress);
            order.setShipPrice(shipPriceVal);

            String id = OrderService.createOrder(order);

            SharedPreferences sharedPreferences = getApplication().getApplicationContext().getSharedPreferences(ConstantManager.ORDER_TEMPORARY, Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove(storeID);
            edit.apply();

            Intent intent = new Intent(getApplicationContext(), TrackingActivity.class);
            intent.putExtra("orderID", id);
            startActivity(intent);
            finish();
        });
    }

    private void setOrderTime(LocalDateTime dateTime) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        orderTime.setText(dtf.format(dateTime));
    }

    private void setCustomerInfo() {
        Customer customer = ConstantManager.customer;
        customerAddress.setText(customer.getAddress());
        customerName.setText(customer.getName());
        customerPhone.setText(customer.getPhone());
        Picasso.get().load(customer.getImgUrl()).into(customerImg);
    }

    private void setListProductItems() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(ConstantManager.ORDER_TEMPORARY, Context.MODE_PRIVATE);
        Set<String> items = sharedPreferences.getStringSet(storeID, null);
        if (items != null) {
            orderItemList = new ArrayList<>();
            orderPriceVal = 0;
            for (String item : items) {
                OrderItem orderItem = JsonUtil.getObject(item, OrderItem.class);
                orderItemList.add(orderItem);
                orderPriceVal += orderItem.getTotalPrice();
            }
            adapter = new OrderItemRecycleAdapter(this, orderItemList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            orderItems.setLayoutManager(layoutManager);
            orderItems.setHasFixedSize(true);
            orderItems.setAdapter(adapter);

            orderPrice.setText(String.valueOf(orderPriceVal) + " VND");
            setShipInfo();
        }
    }

    private void setShipInfo() {
        APIManager.getCustomerAndStoreDistance(this, desAddress, storeAddress, data -> {
            String distance = String.valueOf(data);
            if (!distance.equals("Not found")) {
                shipDistance.setText(distance);

                double distanceVal = Double.parseDouble(distance.substring(0, distance.length() - 3));
                shipPriceVal = ConstantManager.SHIP_COST * ((int) Math.floor(distanceVal));
                shipPrice.setText(shipPriceVal + " VND");

                totalorderPriceVal = shipPriceVal + orderPriceVal;
                totalOrderPrice.setText(String.valueOf(totalorderPriceVal) + " VND");
                paymentCash.setText(String.valueOf(totalorderPriceVal) + " VND");

            } else {
                Toast.makeText(this, "Không thể tìm thấy địa chỉ này. Vui lòng chọn lại", Toast.LENGTH_LONG).show();
                customerAddress.requestFocus();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

}

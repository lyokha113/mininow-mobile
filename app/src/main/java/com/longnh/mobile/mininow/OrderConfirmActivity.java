package com.longnh.mobile.mininow;

import android.app.ProgressDialog;
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
import com.longnh.mobile.mininow.model.Customer;
import com.longnh.mobile.mininow.model.Order;
import com.longnh.mobile.mininow.model.OrderItem;
import com.longnh.mobile.mininow.model.Store;
import com.longnh.mobile.mininow.service.OrderService;
import com.longnh.mobile.mininow.service.StoreService;
import com.longnh.mobile.mininow.ultils.ConstantManager;
import com.longnh.mobile.mininow.ultils.JsonUtil;
import com.longnh.mobile.mininow.ultils.UserSession;
import com.squareup.picasso.Picasso;

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
    private Customer current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        UserSession session = new UserSession(getApplicationContext(), UserSession.UserSessionType.CUSTOMER);
        current = session.getCustomerDetails();

        addControls();
        addEvents();

        setOrderTime(LocalDateTime.now().plusMinutes(30));

        Intent storeIntent = getIntent();
        storeID = storeIntent.getStringExtra(ConstantManager.STORE_ID);
        storeAddress = storeIntent.getStringExtra(ConstantManager.STORE_ADDRESS);
        storeName = storeIntent.getStringExtra(ConstantManager.STORE_NAME);

        desAddress = current.getAddress();

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

            Customer customer = new Customer();
            customer.setId(current.getId());
            customer.setAddress(customerAddress.getText().toString());
            customer.setName(customerName.getText().toString());
            customer.setPhone(customerPhone.getText().toString());

            Order order = new Order();
            order.setCustomer(customer);
            order.setDescription(description.getText().toString());
            order.setOrderTime(LocalDateTime.now());
            try {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                order.setExpectedTime(LocalDateTime.parse(orderTime.getText().toString(), dtf));
            } catch (Exception e) {
                return;
            }

            Store store = new Store();
            store.setId(Long.parseLong(storeID));
            store.setName(storeName);
            store.setAddress(storeAddress);

            order.setStore(store);
            order.setStatus(ConstantManager.ORDER_WAITING);
            order.setShipPrice(shipPriceVal);
            order.setProductPrice(orderPriceVal);

            try {
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Đang xử lý ...");
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                OrderService.createOrder(getApplicationContext(), order, JsonUtil.getJson(orderItemList), data -> {
                    Order result = (Order) data;
                    SharedPreferences sharedPreferences = getApplication().getApplicationContext().getSharedPreferences(ConstantManager.ORDER_TEMPORARY, Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.remove(storeID);
                    edit.apply();

                    Intent intent = new Intent(getApplicationContext(), TrackingActivity.class);
                    intent.putExtra("orderID", result.getId());
                    startActivity(intent);
                    finish();
                    progressDialog.dismiss();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


        });
    }

    private void setOrderTime(LocalDateTime dateTime) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        orderTime.setText(dtf.format(dateTime));
    }

    private void setCustomerInfo() {
        Customer customer = current;
        customerAddress.setText(customer.getAddress());
        customerName.setText(customer.getName());
        customerPhone.setText(customer.getPhone());
        Picasso.get().load(customer.getImgURL()).into(customerImg);
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
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý ...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        StoreService.getCustomerAndStoreDistance(this, desAddress, storeAddress, data -> {
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
            progressDialog.dismiss();
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

package com.longnh.mobile.mininow;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.longnh.mobile.mininow.adapter.ProductGridAdapter;
import com.longnh.mobile.mininow.model.OrderItem;
import com.longnh.mobile.mininow.model.Product;
import com.longnh.mobile.mininow.model.ProductExtra;
import com.longnh.mobile.mininow.model.Store;
import com.longnh.mobile.mininow.service.ProductService;
import com.longnh.mobile.mininow.service.StoreService;
import com.longnh.mobile.mininow.ultils.ConstantManager;
import com.longnh.mobile.mininow.ultils.JsonUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;


public class ProductActivity extends AppCompatActivity {

    private GridView gridProducts;
    private LinearLayout storeImg, viewCart;
    private TextView storeName, storeAddress, totalOrderPrice, confirmCart;
    private SharedPreferences sharedPreferences;
    private long storeID;
    private OrderItem orderItem;
    private List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        storeID = getIntent().getExtras().getLong("storeID");

        addControls();
        addEvents();

        getProduct();
        getStoreInfo();
        setTotal();

    }

    private void addControls() {
        gridProducts = findViewById(R.id.grid_products);
        storeImg = findViewById(R.id.store_img_product);
        storeName = findViewById(R.id.store_name_product);
        storeAddress = findViewById(R.id.store_address_product);
        totalOrderPrice = findViewById(R.id.total_cart_price);
        confirmCart = findViewById(R.id.confirm_cart);
        viewCart = findViewById(R.id.view_cart);
        sharedPreferences = getApplicationContext().getSharedPreferences(ConstantManager.ORDER_TEMPORARY, Context.MODE_PRIVATE);
    }

    private void addEvents() {
        gridProducts.setOnItemClickListener((parent, view, position, id) -> {
            orderProduct(products.get(position));
        });

        confirmCart.setOnClickListener(v -> {
            if (totalOrderPrice.getText().toString().equals("0 VND")) return;
            Intent intent = new Intent(this, OrderConfirmActivity.class);
            intent.putExtra(ConstantManager.STORE_ID, String.valueOf(storeID));
            intent.putExtra(ConstantManager.STORE_ADDRESS, storeAddress.getText().toString());
            intent.putExtra(ConstantManager.STORE_NAME, storeName.getText().toString());
            startActivity(intent);
        });

        viewCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            intent.putExtra(ConstantManager.STORE_ID, String.valueOf(storeID));
            intent.putExtra(ConstantManager.STORE_ADDRESS, storeAddress.getText().toString());
            intent.putExtra(ConstantManager.STORE_NAME, storeName.getText().toString());
            startActivity(intent);
        });
    }

    private void getProduct() {
        products = new ArrayList<>();

        StoreService.getProductOfStore(getApplicationContext(), storeID, data -> {
            products = (List<Product>) data;
            gridProducts.setAdapter(new ProductGridAdapter(this, products));
        });
    }

    private void getStoreInfo() {

        StoreService.getStoreInfo(getApplicationContext(), storeID, data -> {
            Store store = (Store) data;
            storeName.setText(store.getName());
            storeAddress.setText(store.getAddress() + " - " + store.getPhone());
            Picasso.get().load(store.getBannerURL()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    storeImg.setBackground(new BitmapDrawable(bitmap));
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });
            gridProducts.setAdapter(new ProductGridAdapter(this, products));
        });
    }

    public void orderProduct(Product product) {

        orderItem = new OrderItem();
        orderItem.setPrice(product.getPrice());
        orderItem.setProductID(product.getId());
        orderItem.setName(product.getName());

        Dialog detail = new Dialog(this, R.style.MaterialDialogSheet);
        detail.setContentView(R.layout.product_detail);

        ImageView img = detail.findViewById(R.id.product_detail_img);
        Picasso.get().load(product.getImgUrl()).into(img);

        TextView name = detail.findViewById(R.id.product_detail_name);
        name.setText(product.getName());

        TextView price = detail.findViewById(R.id.product_detail_price);
        price.setText(product.getPrice() + " VND");

        TextView total = detail.findViewById(R.id.totalPrice);
        LinearLayout extraView = detail.findViewById(R.id.extras_info);

        ProductService.getProductExtra(getApplicationContext(), product.getId(), data -> {
            List<ProductExtra> extras = (List<ProductExtra>) data;
            List<ProductExtra> required = new ArrayList<>();
            List<ProductExtra> optional = new ArrayList<>();
            for (ProductExtra extra : extras) {
                if (extra.isRequired()) required.add(extra);
                else optional.add(extra);
            }
            setRequireItem(extraView, total, required);
            setOptionalItem(extraView, total, optional);
        });

        detail.findViewById(R.id.closePopup).setOnClickListener(v -> {
            detail.dismiss();
        });

        detail.findViewById(R.id.allOut).setOnClickListener(v -> {
            hideKeyboard(detail);
            detail.dismiss();
        });

        detail.findViewById(R.id.allIn).setOnClickListener(v -> hideKeyboard(detail));

        detail.findViewById(R.id.btnAdd).setOnClickListener(v -> {
            TextView quantity = detail.findViewById(R.id.product_quantity);
            int quantityNum = Integer.parseInt(quantity.getText().toString()) + 1;
            quantity.setText(String.valueOf(quantityNum));
            orderItem.setQuantity(quantityNum);
            total.setText(orderItem.getTotalPrice() + " VND");
        });

        detail.findViewById(R.id.btnSub).setOnClickListener(v -> {
            TextView quantity = detail.findViewById(R.id.product_quantity);
            int quantityNum = Integer.parseInt(quantity.getText().toString()) - 1;
            if (quantityNum >= 0) {
                quantity.setText(String.valueOf(quantityNum));
                orderItem.setQuantity(quantityNum);
                total.setText(orderItem.getTotalPrice() + " VND");
            }
        });

        detail.findViewById(R.id.addToCard).setOnClickListener(v -> {
            detail.dismiss();

            if (orderItem.getQuantity() == 0) return;

            String description = ((EditText) detail.findViewById(R.id.produdct_detail_description)).getText().toString();
            orderItem.setDescription(description);

            Set<String> savedProducts = new HashSet<>(
                    sharedPreferences.getStringSet(String.valueOf(storeID), new HashSet<>()));

            List<String> toRemove = new ArrayList<>();
            savedProducts.forEach(ele -> {
                OrderItem savedItem = JsonUtil.getObject(ele, OrderItem.class);
                if (savedItem.getProductID() == orderItem.getProductID()
                        && savedItem.getExtras().equals(orderItem.getExtras())) {
                    orderItem.setQuantity(savedItem.getQuantity() + orderItem.getQuantity());
                    toRemove.add(ele);
                }
            });

            savedProducts.removeAll(toRemove);
            savedProducts.add(JsonUtil.getJson(orderItem));

            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove(String.valueOf(storeID));
            edit.putStringSet(String.valueOf(storeID), savedProducts);
            edit.apply();
            setTotal();
        });


        detail.getWindow().setGravity(Gravity.BOTTOM);
        detail.show();
    }

    private void hideKeyboard(Dialog dialog) {
        InputMethodManager inputMethodManager = (InputMethodManager) ProductActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), 0);
    }

    private void setTotal() {

        Set<String> saveProducts = sharedPreferences.getStringSet(String.valueOf(storeID), null);

        int totalPrice = 0;
        if (saveProducts != null) {
            for (String saved : saveProducts) {
                OrderItem obj = JsonUtil.getObject(saved, OrderItem.class);
                totalPrice += obj.getTotalPrice();
            }
        }

        totalOrderPrice.setText(String.valueOf(totalPrice) + " VND");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTotal();
    }

    private void setRequireItem(LinearLayout extraView, TextView total, List<ProductExtra> extras) {
        if (extras.size() > 0) {
            LinearLayout parent = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.radio_required, extraView, false);
            RadioGroup items = parent.findViewById(R.id.require_item);
            LinearLayout values = parent.findViewById(R.id.require_value);
            extraView.addView(parent);

            boolean isFirst = true;
            for (ProductExtra extra : extras) {
                RadioButton item = (RadioButton) LayoutInflater.from(this).inflate(R.layout.radio_item, items, false);
                item.setText(extra.getName());
                items.addView(item);

                TextView value = (TextView) LayoutInflater.from(this).inflate(R.layout.textview_value, items, false);
                value.setText(extra.getValue() + " VND");
                values.addView(value);


                item.setOnClickListener(v -> {
                    orderItem.getExtras().removeIf(ProductExtra::isRequired);
                    orderItem.getExtras().add(extra);
                    total.setText(orderItem.getTotalPrice() + " VND");
                });

                if (isFirst) {
                    item.performClick();
                    isFirst = false;
                }

            }
        }
    }

    private void setOptionalItem(LinearLayout extraView, TextView total, List<ProductExtra> extras) {
        if (extras.size() > 0) {
            LinearLayout parent = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.checkbox_extra, extraView, false);
            LinearLayout items = parent.findViewById(R.id.option_item);
            LinearLayout values = parent.findViewById(R.id.option_value);
            extraView.addView(parent);

            for (ProductExtra extra : extras) {
                CheckBox item = (CheckBox) LayoutInflater.from(this).inflate(R.layout.checkbox_item, items, false);
                item.setText(extra.getName());
                items.addView(item);

                TextView value = (TextView) LayoutInflater.from(this).inflate(R.layout.textview_value, items, false);
                value.setText(extra.getValue() + " VND");
                values.addView(value);

                item.setOnClickListener(v -> {
                    if (item.isChecked()) {
                        orderItem.getExtras().add(extra);
                    } else {
                        orderItem.getExtras().remove(extra);
                    }
                    total.setText(orderItem.getTotalPrice() + " VND");
                });
            }
        }
    }
}


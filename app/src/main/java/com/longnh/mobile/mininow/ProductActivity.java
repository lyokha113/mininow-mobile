package com.longnh.mobile.mininow;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.longnh.mobile.mininow.adapter.ProductGridAdapter;
import com.longnh.mobile.mininow.entity.OrderItem;
import com.longnh.mobile.mininow.entity.Product;
import com.longnh.mobile.mininow.entity.Store;
import com.longnh.mobile.mininow.model.ProductService;
import com.longnh.mobile.mininow.model.StoreService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;


public class ProductActivity extends AppCompatActivity {

    private List<Product> products;
    private GridView gridProducts;
    private String storeID;
    private LinearLayout storeImg;
    private TextView storeName, storeAddress;
    private OrderItem orderItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        storeID = getIntent().getExtras().getString("storeID");

        addControls();
        addEvents();

        getProduct();
        getStoreInfo();

    }

    private void addControls() {
        gridProducts = findViewById(R.id.grid_products);
        storeImg = findViewById(R.id.store_img_product);
        storeName = findViewById(R.id.store_name_product);
        storeAddress = findViewById(R.id.store_address_product);
    }

    private void addEvents() {
        gridProducts.setOnItemClickListener((parent, view, position, id) -> {
            orderProduct(products.get(position));
        });
    }

    private void getProduct() {
        products = new ArrayList<>();

        ProductService.getProductOfStore(storeID, data -> {
            products = (List<Product>) data;
            gridProducts.setAdapter(new ProductGridAdapter(this, products));
        });
    }

    private void getStoreInfo() {

        StoreService.getStoreInfo(storeID, data -> {
            Store store = (Store) data;
            storeName.setText(store.getName());
            storeAddress.setText(store.getAddress() + " - " + store.getPhone());
            Picasso.get().load(store.getBannerUrl()).into(new Target() {
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
        orderItem.setStoreID(product.getStoreID());
        orderItem.setProductID(product.getId());

        Dialog detail = new Dialog(this, R.style.MaterialDialogSheet);
        detail.setContentView(R.layout.product_detail);

        ImageView img = detail.findViewById(R.id.product_detail_img);
        Picasso.get().load(product.getImgUrl()).into(img);

        TextView name = detail.findViewById(R.id.product_detail_name);
        name.setText(product.getName());

        TextView price = detail.findViewById(R.id.product_detail_price);
        price.setText(product.getPrice() + " VND");

        ArrayList<String> requires = product.getRequireExtra();
        ArrayList<String> optionals = product.getOptionalExtra();

        TextView total = detail.findViewById(R.id.totalPrice);

        LinearLayout extraView = detail.findViewById(R.id.extras_info);

        if (requires != null) {
            LinearLayout parent = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.radio_required, extraView, false);
            RadioGroup items = parent.findViewById(R.id.require_item);
            LinearLayout values = parent.findViewById(R.id.require_value);
            extraView.addView(parent);

            for (int i = 0; i < requires.size(); i += 2) {

                RadioButton item = (RadioButton) LayoutInflater.from(this).inflate(R.layout.radio_item, items, false);
                item.setText(requires.get(i));
                items.addView(item);
                item.setId(i + 1);

                TextView value = (TextView) LayoutInflater.from(this).inflate(R.layout.textview_value, items, false);
                value.setText(requires.get(i + 1) + " VND");
                values.addView(value);

                item.setOnClickListener(v -> {
                    int extra = Integer.parseInt(requires.get(item.getId()));
                    orderItem.setRequireExtra(extra);
                    total.setText(orderItem.getTotalPrice() + " VND");
                });

                if (i == 0) {
                    item.performClick();
                }
            }

        }

        if (optionals != null) {
            LinearLayout parent = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.checkbox_extra, extraView, false);
            LinearLayout items = parent.findViewById(R.id.option_item);
            LinearLayout values = parent.findViewById(R.id.option_value);
            extraView.addView(parent);

            for (int i = 0; i < optionals.size(); i += 2) {
                CheckBox item = (CheckBox) LayoutInflater.from(this).inflate(R.layout.checkbox_item, items, false);
                item.setText(optionals.get(i));
                items.addView(item);
                item.setId(i + 1);

                TextView value = (TextView) LayoutInflater.from(this).inflate(R.layout.textview_value, items, false);
                value.setText(optionals.get(i + 1) + " VND");
                values.addView(value);

                item.setOnClickListener(v -> {
                    int extra = Integer.parseInt(optionals.get(item.getId()));
                    int current = orderItem.getOptionalExtra();
                    orderItem.setOptionalExtra(item.isChecked() ? current + extra : current - extra);
                    total.setText(orderItem.getTotalPrice() + " VND");
                });
            }
        }

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

//        mBottomDialog.findViewById(R.id.addToCard).setOnClickListener(v -> {
//            mBottomDialog.dismiss();
//            final TextView textView = mBottomDialog.findViewById(R.id.viewSL);
//        });


        detail.getWindow().setGravity(Gravity.BOTTOM);
        detail.show();
    }

    private void hideKeyboard(Dialog dialog) {
        InputMethodManager inputMethodManager = (InputMethodManager) ProductActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), 0);
    }

}

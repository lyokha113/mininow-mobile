package com.longnh.mobile.mininow;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.longnh.mobile.mininow.adapter.ProductGridAdapter;
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



    public void orderProduct(View view) {
        final int[] viewSl = {0};
        final Dialog mBottomDialog = new Dialog(this, R.style.MaterialDialogSheet);
        mBottomDialog.setContentView(R.layout.activity_order_food);
        mBottomDialog.setCancelable(true);
        Toast.makeText(this, pref.getString("SLL","0"), Toast.LENGTH_SHORT).show();
        mBottomDialog.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView textView = mBottomDialog.findViewById(R.id.viewSL);
                viewSl[0] = Integer.parseInt(textView.getText().toString());
                final int finalViewSl = viewSl[0];
                textView.setText((finalViewSl +1)+"");
            }
        });
        mBottomDialog.findViewById(R.id.btnSub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView textView = mBottomDialog.findViewById(R.id.viewSL);
                viewSl[0] = Integer.parseInt(textView.getText().toString());
                final int finalViewSl = viewSl[0];
                textView.setText((finalViewSl -1)+"");
            }
        });
        mBottomDialog.findViewById(R.id.addToCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomDialog.dismiss();
                final TextView textView = mBottomDialog.findViewById(R.id.viewSL);
                editor.putString("1","id=1,sll="+textView.getText());
                editor.commit();
            }
        });
        mBottomDialog.findViewById(R.id.closePopup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomDialog.dismiss();
//                Toast.makeText(MainActivity.this, pref.getString("SLL","0"), Toast.LENGTH_SHORT).show();
            }
        });
//        mBottomDialog.findViewById(R.id.group_Size).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RadioButton radioButtonM = mBottomDialog.findViewById(R.id.size_M);
//                RadioButton radioButtonL = mBottomDialog.findViewById(R.id.size_L);
//                if(radioButtonM.isChecked()){
//                    TextView total = mBottomDialog.findViewById(R.id.totalPrice);
//                    total.setText("20000");
//                }else if(radioButtonL.isChecked()){
//                    TextView total = mBottomDialog.findViewById(R.id.totalPrice);
//                    total.setText("25000");
//                }
//            }
//        });
        RadioButton radioButtonM = mBottomDialog.findViewById(R.id.size_M);
        RadioButton radioButtonL = mBottomDialog.findViewById(R.id.size_L);
        radioButtonM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView total = mBottomDialog.findViewById(R.id.totalPrice);
                total.setText("20000");
            }
        });
        radioButtonL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView total = mBottomDialog.findViewById(R.id.totalPrice);
                total.setText("25000");
            }
        });
//                if(radioButtonM.isChecked()){
//                    TextView total = mBottomDialog.findViewById(R.id.totalPrice);
//                    total.setText("20000");
//                }else if(radioButtonL.isChecked()){
//                    TextView total = mBottomDialog.findViewById(R.id.totalPrice);
//                    total.setText("25000");
//                }
        mBottomDialog.findViewById(R.id.allOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mBottomDialog);
                mBottomDialog.dismiss();
//                Toast.makeText(MainActivity.this, pref.getString("SLL","0"), Toast.LENGTH_SHORT).show();
            }
        });

        mBottomDialog.findViewById(R.id.allIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mBottomDialog);
//                Toast.makeText(MainActivity.this, pref.getString("SLL","0"), Toast.LENGTH_SHORT).show();
            }
        });

        mBottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomDialog.show();
    }
}

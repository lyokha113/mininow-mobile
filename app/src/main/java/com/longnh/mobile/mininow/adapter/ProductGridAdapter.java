package com.longnh.mobile.mininow.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.longnh.mobile.mininow.entity.Product;

import java.util.List;

public class ProductGridAdapter extends BaseAdapter {

    private List<Product> products;
    private Activity activity;

    public ProductGridAdapter(Activity activity,  List<Product> products) {
        this.activity = activity;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products == null ? 0 : products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}

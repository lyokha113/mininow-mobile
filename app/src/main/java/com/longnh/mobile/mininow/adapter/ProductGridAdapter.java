package com.longnh.mobile.mininow.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.longnh.mobile.mininow.R;
import com.longnh.mobile.mininow.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductGridAdapter extends BaseAdapter {

    private List<Product> products;
    private Activity activity;
    private LayoutInflater layoutInflater;

    public ProductGridAdapter(Activity activity, List<Product> products) {
        this.activity = activity;
        this.products = products;
        layoutInflater = LayoutInflater.from(this.activity);
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.product_grid_item, null);
            holder = new ViewHolder();
            holder.productName = convertView.findViewById(R.id.product_name);
            holder.productPrice = convertView.findViewById(R.id.product_price);
            holder.productImg = convertView.findViewById(R.id.product_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Product product = products.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice() + " VNĐ");
        Picasso.get().load(product.getImgUrl()).resize(180,150).centerCrop().into(holder.productImg);

        return convertView;
    }

    static class ViewHolder {
        TextView productPrice;
        TextView productName;
        ImageView productImg;
    }
}

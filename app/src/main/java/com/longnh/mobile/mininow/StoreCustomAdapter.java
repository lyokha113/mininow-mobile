package com.longnh.mobile.mininow;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.longnh.mobile.mininow.entity.Store;

import java.util.List;

public class StoreCustomAdapter extends ArrayAdapter<Store> {

    Activity context;
    List<Store> stores;
    int layoutId;

    public StoreCustomAdapter(Activity context, int layoutId, List<Store> stores) {
        super(context, layoutId, stores);
        this.context = context;
        this.layoutId = layoutId;
        this.stores = stores;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(layoutId, null);

        final TextView storeName = convertView.findViewById(R.id.store_name);
        final TextView storeAddress = convertView.findViewById(R.id.store_address);
        final TextView storeDescription = convertView.findViewById(R.id.store_description);
        final TextView storeTime = convertView.findViewById(R.id.store_time);

        Store store = stores.get(position);
        storeName.setText(store.getName());
        storeAddress.setText(store.getAddress());
        storeDescription.setText(store.getDescription());
        storeTime.setText(store.getOpenTime() + " - " + store.getCloseTime());

        return convertView;

    }
}

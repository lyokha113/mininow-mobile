package com.longnh.mobile.mininow.ultils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.longnh.mobile.mininow.LoginCustomerActivity;
import com.longnh.mobile.mininow.LoginShipperActivity;
import com.longnh.mobile.mininow.model.Customer;
import com.longnh.mobile.mininow.model.Shipper;

public class UserSession {

    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_IMGURL = "imgurl";
    public static final String KEY_ID = "id";
    public static final String KEY_UID = "uid";
    private static final String CUSTOMER_PRE = "currentcustomer";
    private static final String SHIPPER_PRE = "currentshipper";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private int PRIVATE_MODE = 0;

    public UserSession(Context context, UserSessionType type) {
        this.context = context;
        if (type == UserSessionType.CUSTOMER) {
            pref = context.getSharedPreferences(CUSTOMER_PRE, PRIVATE_MODE);
            editor = pref.edit();
        } else {
            pref = context.getSharedPreferences(SHIPPER_PRE, PRIVATE_MODE);
            editor = pref.edit();
        }

    }

    public void createShipperSession(Shipper shipper) {
        editor.putString(KEY_NAME, shipper.getName());
        editor.putString(KEY_ADDRESS, shipper.getAddress());
        editor.putString(KEY_IMGURL, shipper.getImgURL());
        editor.putString(KEY_PHONE, shipper.getPhone());
        editor.putLong(KEY_ID, shipper.getId());
        editor.putString(KEY_UID, shipper.getUid());
        editor.commit();
    }

    public Shipper getShipperDetails() {
        Shipper shipper = new Shipper();
        shipper.setId(pref.getLong(KEY_ID, 0L));
        shipper.setUid(pref.getString(KEY_UID, null));
        shipper.setName(pref.getString(KEY_NAME, null));
        shipper.setPhone(pref.getString(KEY_PHONE, null));
        shipper.setAddress(pref.getString(KEY_ADDRESS, null));
        shipper.setImgURL(pref.getString(KEY_IMGURL, null));
        return shipper;
    }

    public void removeShipperSession() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginShipperActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public void createCustomerSession(Customer customer) {
        editor.putString(KEY_NAME, customer.getName());
        editor.putString(KEY_ADDRESS, customer.getAddress());
        editor.putString(KEY_IMGURL, customer.getImgURL());
        editor.putString(KEY_PHONE, customer.getPhone());
        editor.putLong(KEY_ID, customer.getId());
        editor.putString(KEY_UID, customer.getUid());
        editor.commit();
    }

    public Customer getCustomerDetails() {
        Customer customer = new Customer();
        customer.setId(pref.getLong(KEY_ID, 0L));
        customer.setUid(pref.getString(KEY_UID, null));
        customer.setName(pref.getString(KEY_NAME, null));
        customer.setPhone(pref.getString(KEY_PHONE, null));
        customer.setAddress(pref.getString(KEY_ADDRESS, null));
        customer.setImgURL(pref.getString(KEY_IMGURL, null));
        return customer;
    }

    public void removeCustomerSession() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginCustomerActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public enum UserSessionType {
        CUSTOMER, SHIPPER;
    }

}

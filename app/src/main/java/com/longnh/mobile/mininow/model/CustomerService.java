package com.longnh.mobile.mininow.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.longnh.mobile.mininow.entity.Customer;
import com.longnh.mobile.mininow.entity.Order;
import com.longnh.mobile.mininow.entity.Product;
import com.longnh.mobile.mininow.ultils.ConstantManager;
import com.longnh.mobile.mininow.ultils.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class CustomerService {

    static final String TAG = "StoreService";
    static final ObjectMapper om = new ObjectMapper();

    static {
        om.registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }


    public static void getCustomerInfo(Context context, long customerID, final VolleyCallback callback) {

        final String URL = ConstantManager.HOST + "/customer/" + customerID;
        RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();

        StringRequest objectRequest = new StringRequest(
                Request.Method.GET,
                URL,
                response -> {
                    try {
                        Customer customer = JsonUtil.getObject(response, Customer.class);
                        callback.onSuccess(customer);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Log.e(TAG, "onResponse: " + ex.getMessage());
                    }
                },
                error -> Log.e(TAG, "onErrorResponse: " + error.toString())
        );
        requestQueue.add(objectRequest);
    }


    public static void updateCustomer(Context context, Customer customer, final VolleyCallback callback) throws JSONException {

        final String URL = ConstantManager.HOST + "/customer/";
        RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();

        JSONObject request = new JSONObject();
        request.put("id", customer.getId());
        request.put("phone", customer.getPhone());
        request.put("name", customer.getName());
        request.put("address", customer.getAddress());
        request.put("imgURL", customer.getImgURL());

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                URL,
                request,
                response -> {
                    try {
                        Customer result = om.readValue(response.toString(), Customer.class);
                        callback.onSuccess(result);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Log.e(TAG, "onResponse: " + ex.getMessage());
                    }
                },
                error -> Log.e(TAG, "onErrorResponse: " + error.toString())
        );
        requestQueue.add(objectRequest);
    }
}

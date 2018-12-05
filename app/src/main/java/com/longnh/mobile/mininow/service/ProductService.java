package com.longnh.mobile.mininow.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.longnh.mobile.mininow.model.Order;
import com.longnh.mobile.mininow.model.ProductExtra;
import com.longnh.mobile.mininow.ultils.ConstantManager;

import java.util.Arrays;
import java.util.List;

public class ProductService {

    static final String TAG = "ProductService";
    static final ObjectMapper om = new ObjectMapper();
    static final String PRODUCT_URL_API = ConstantManager.HOST + "/product/";

    static {
        om.registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    public static void getProductExtra(Context context, long productId, final FirestoreCallback callback) {

        final String URL = PRODUCT_URL_API  + productId + "/extra";
        RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();

        StringRequest objectRequest = new StringRequest(
                Request.Method.GET,
                URL,
                response -> {
                    try {
                        List<ProductExtra> orders = Arrays.asList(om.readValue(response, ProductExtra[].class));
                        callback.onSuccess(orders);
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

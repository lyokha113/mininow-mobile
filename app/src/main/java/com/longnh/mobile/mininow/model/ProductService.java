package com.longnh.mobile.mininow.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.longnh.mobile.mininow.entity.Product;
import com.longnh.mobile.mininow.entity.Store;
import com.longnh.mobile.mininow.ultils.ConstantManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductService {

    static final String TAG = "ProductService";
    static final ObjectMapper om = new ObjectMapper();

    static {
        om.registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    public static void getProductOfStore(Context context, String storeID, final FirestoreCallback callback) {

        final String URL = ConstantManager.HOST + "/store/" + storeID + "/product/";
        RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();

        StringRequest objectRequest = new StringRequest(
                Request.Method.GET,
                URL,
                response -> {
                    try {
                        List<Product> products = Arrays.asList(om.readValue(response, Product[].class));
                        callback.onSuccess(products);
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

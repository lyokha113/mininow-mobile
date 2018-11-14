package com.longnh.mobile.mininow.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.longnh.mobile.mininow.entity.Store;
import com.longnh.mobile.mininow.ultils.ConstantManager;
import com.longnh.mobile.mininow.ultils.JsonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class StoreService {

    static final String TAG = "StoreService";
    static final ObjectMapper om = new ObjectMapper();

    static {
        om.registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    public static void getNewStore(Context context, final VolleyCallback callback) {

        final String URL = ConstantManager.HOST + "/store/new/";
        RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();

        StringRequest objectRequest = new StringRequest(
                Request.Method.GET,
                URL,
                response -> {
                    try {
                        List<Store> stores = Arrays.asList(om.readValue(response, Store[].class));
                        callback.onSuccess(stores);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Log.e(TAG, "onResponse: " + ex.getMessage());
                    }
                },
                error -> Log.e(TAG, "onErrorResponse: " + error.toString())
        );
        requestQueue.add(objectRequest);
    }

    public static void getAll(Context context, final VolleyCallback callback) {

        final String URL = ConstantManager.HOST + "/store/";
        RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();

        StringRequest objectRequest = new StringRequest(
                Request.Method.GET,
                URL,
                response -> {
                    try {
                        List<Store> stores = Arrays.asList(om.readValue(response, Store[].class));
                        callback.onSuccess(stores);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Log.e(TAG, "onResponse: " + ex.getMessage());
                    }
                },
                error -> Log.e(TAG, "onErrorResponse: " + error.toString())
        );
        requestQueue.add(objectRequest);
    }

    public static void getStoreInfo(Context context, String storeID, final FirestoreCallback callback) {

        final String URL = ConstantManager.HOST + "/store/" + storeID;
        RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();

        StringRequest objectRequest = new StringRequest(
                Request.Method.GET,
                URL,
                response -> {
                    try {
                        Store store = JsonUtil.getObject(response, Store.class);
                        callback.onSuccess(store);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Log.e(TAG, "onResponse: " + ex.getMessage());
                    }
                },
                error -> Log.e(TAG, "onErrorResponse: " + error.toString())
        );
        requestQueue.add(objectRequest);
    }

    public static void getStoreDistance(Context context, String origins, String destination, final VolleyCallback callback) {

        RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();

        String URL = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + origins +
                "&destinations=" + destination + "&key=AIzaSyBsY-26loYcr2kpIARp5wTmbExsf-BWC7M";

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                response -> {
                    try {
                        JSONArray rows = response.getJSONArray("rows");
                        JSONObject row = (JSONObject) rows.get(0);
                        JSONArray elements = row.getJSONArray("elements");

                        List<String> distances = new ArrayList<>();

                        for (int i = 0; i < elements.length(); i++) {
                            JSONObject element = (JSONObject) elements.get(i);
                            JSONObject distance = (JSONObject) element.get("distance");
                            distances.add(distance.getString("text"));
                        }
                        callback.onSuccess(distances);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Log.e(TAG, "onResponse: " + ex.getMessage());
                    }
                },
                error -> Log.e(TAG, "onErrorResponse: " + error.toString())
        );
        requestQueue.add(objectRequest);
    }

    public static void getCustomerAndStoreDistance(Context context, String customer, String store, final VolleyCallback callback) {

        RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();

        String URL = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + customer +
                "&destinations=" + store + "&key=AIzaSyBsY-26loYcr2kpIARp5wTmbExsf-BWC7M";

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                response -> {
                    try {
                        JSONArray rows = response.getJSONArray("rows");
                        JSONObject row = (JSONObject) rows.get(0);
                        JSONArray elements = row.getJSONArray("elements");
                        JSONObject element = (JSONObject) elements.get(0);
                        if (element.has("distance")) {
                            JSONObject distance = (JSONObject) element.get("distance");
                            callback.onSuccess(distance.getString("text"));
                        } else {
                            callback.onSuccess("Not found");
                        }

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

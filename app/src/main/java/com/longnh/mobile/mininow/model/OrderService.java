package com.longnh.mobile.mininow.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.longnh.mobile.mininow.entity.Order;
import com.longnh.mobile.mininow.ultils.ConstantManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class OrderService {

    static final String TAG = "OrderService";
    static final ObjectMapper om = new ObjectMapper();
    private final static FirebaseFirestore db = FirestoreDB.getInstance();

    static {
        om.registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());

        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static void createOrder(Context context, Order order, final VolleyCallback callback) throws JSONException, IOException {

        final String URL = ConstantManager.HOST + "/order/";
        RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();

        JSONObject storeJS = new JSONObject();
        storeJS.put("id", order.getStore().getId());
        storeJS.put("name", order.getStore().getName());
        storeJS.put("address", order.getStore().getAddress());

        JSONObject customerJS = new JSONObject();
        customerJS.put("id", order.getCustomer().getId());
        customerJS.put("name", order.getCustomer().getName());
        customerJS.put("address", order.getCustomer().getAddress());
        customerJS.put("phone", order.getCustomer().getPhone());

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("store", storeJS);
        orderRequest.put("customer", customerJS);
        orderRequest.put("productPrice", order.getProductPrice());
        orderRequest.put("shipPrice", order.getShipPrice());
        orderRequest.put("orderTime", order.getOrderTime());
        orderRequest.put("expectedTime", order.getExpectedTime());
        orderRequest.put("description", order.getDescription());
        orderRequest.put("status", order.getStatus());
        orderRequest.put("detail", order.getDetail());
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                orderRequest,
                response -> {
                    try {
                        Order result = om.readValue(response.toString(), Order.class);
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

    public static void getOrderInfo(Context context, long orderID, final FirestoreCallback callback) {

        final String URL = ConstantManager.HOST + "/order/" + orderID;
        RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();

        StringRequest objectRequest = new StringRequest(
                Request.Method.GET,
                URL,
                response -> {
                    try {
                        Order result = om.readValue(response, Order.class);
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

    public static void getOngoingOrder(Context context, long customerID, final FirestoreCallback callback) {

        final String URL = ConstantManager.HOST + "/order/ongoing/" + customerID;
        RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();

        StringRequest objectRequest = new StringRequest(
                Request.Method.GET,
                URL,
                response -> {
                    try {
                        List<Order> orders = Arrays.asList(om.readValue(response, Order[].class));
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

    public static void getFinishedOrder(Context context, long customerID, final FirestoreCallback callback) {

        final String URL = ConstantManager.HOST + "/order/finished/" + customerID;
        RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();

        StringRequest objectRequest = new StringRequest(
                Request.Method.GET,
                URL,
                response -> {
                    try {
                        List<Order> orders = Arrays.asList(om.readValue(response, Order[].class));
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

    public static void trackingOrderStatus(String orderID, final FirestoreCallback callback) {
        DocumentReference docRef = db.collection("order").document(orderID);
        docRef.addSnapshotListener((documentSnapshot, e) -> {
            if (documentSnapshot != null && documentSnapshot.exists()) {
                Long status = (Long) documentSnapshot.get("status");
                callback.onSuccess(status);
            }
        });
    }

    public static void getShipperOfOrder(String orderID, final FirestoreCallback callback) {
        DocumentReference docRef = db.collection("order").document(orderID);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                String shipper = doc.getString("shipperID");
                callback.onSuccess(shipper);
            } else {
                Log.w("FIREBASE", "Error getting documents.", task.getException());
            }
        });
    }

    public static void cancleOrder(Context context, long id, int status, final FirestoreCallback callback) {

        final String URL = ConstantManager.HOST + "/order/" + id + "/status/" + status;
        RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();

        StringRequest objectRequest = new StringRequest(
                Request.Method.GET,
                URL,
                response -> {},
                error -> Log.e(TAG, "onErrorResponse: " + error.toString())
        );
        requestQueue.add(objectRequest);
    }
}

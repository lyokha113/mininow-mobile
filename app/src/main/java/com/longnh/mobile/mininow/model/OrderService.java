package com.longnh.mobile.mininow.model;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.longnh.mobile.mininow.entity.Order;
import com.longnh.mobile.mininow.ultils.ConstantManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderService {

    private final static FirebaseFirestore db = FirestoreDB.getInstance();

    public static String createOrder(Order order) {
        DocumentReference addedDocRef = db.collection("order").document();
        order.setId(addedDocRef.getId());
        addedDocRef.set(order);
        return addedDocRef.getId();
    }

    public static void getOrderInfo(String orderID, final FirestoreCallback callback) {
        db.collection("order").document(orderID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        callback.onSuccess(doc.toObject(Order.class));
                    } else {
                        Log.w("FIREBASE", "Error getting documents.", task.getException());
                    }
                });
    }

    public static void getOngoingOrder(String customerID, final FirestoreCallback callback) {
        db.collection("order")
                .whereEqualTo("cusID", customerID)
                .whereLessThan("status", ConstantManager.ORDER_DONE)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        List<Order> orders = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Order order = document.toObject(Order.class);
                            orders.add(order);
                        }
                        callback.onSuccess(orders);
                    } else {
                        Log.w("FIREBASE", "Error getting documents.", task.getException());
                    }
                });

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

    public static void cancleOrder(String orderID) {
        Map<String, Object> update = new HashMap<>();
        update.put("status", ConstantManager.ORDER_REJECTED);

        DocumentReference docRef = db.collection("order").document(orderID);
        docRef.set(update, SetOptions.merge());
    }
}

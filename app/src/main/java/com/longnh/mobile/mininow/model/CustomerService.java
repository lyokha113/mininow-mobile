package com.longnh.mobile.mininow.model;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.longnh.mobile.mininow.entity.Customer;
import com.longnh.mobile.mininow.entity.Product;

public class CustomerService {

    private final static FirebaseFirestore db = FirestoreDB.getInstance();

    public static void getCustomerInfo(String customerID, final VolleyCallback callback) {
        db.collection("customer").whereEqualTo("id", customerID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        callback.onSuccess(document.toObject(Customer.class));
                    } else {
                        Log.w("FIREBASE", "Error getting documents.", task.getException());
                    }
                });
    }
}

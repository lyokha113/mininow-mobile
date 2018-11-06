package com.longnh.mobile.mininow.model;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.longnh.mobile.mininow.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductService {

    private final static FirebaseFirestore db = FirestoreDB.getInstance();

    public static void getProductOfStore(String storeID, final FirestoreCallback callback) {
        db.collection("product").whereEqualTo("storeID", storeID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        List<Product> products = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            products.add(product);
                        }
                        callback.onSuccess(products);
                    } else {
                        Log.w("FIREBASE", "Error getting documents.", task.getException());
                    }
                });
    }
}

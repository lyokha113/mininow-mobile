package com.longnh.mobile.mininow.model;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.longnh.mobile.mininow.entity.Store;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StoreService {

    private final static FirebaseFirestore db = FirestoreDB.getInstance();

    public static void getNewStores(final FirestoreCallback callback) {
        db.collection("store").limit(10).orderBy("registerTime", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        List<Store> stores = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Store store = document.toObject(Store.class);
                            stores.add(store);
                        }
                        callback.onSuccess(stores);
                    } else {
                        Log.w("FIREBASE", "Error getting documents.", task.getException());
                    }
                });
    }

    public static void getAll(final FirestoreCallback callback) {
        db.collection("store").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        List<Store> stores = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Store store = document.toObject(Store.class);
                            stores.add(store);
                        }
                        callback.onSuccess(stores);
                    } else {
                        Log.w("FIREBASE", "Error getting documents.", task.getException());
                    }
                });
    }

    public static void getStoreInfo(String storeID, final FirestoreCallback callback) {
        db.collection("store").whereEqualTo("id", storeID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            callback.onSuccess(document.toObject(Store.class));
                            break;
                        }
                    } else {
                        Log.w("FIREBASE", "Error getting documents.", task.getException());
                    }
                });
    }


}

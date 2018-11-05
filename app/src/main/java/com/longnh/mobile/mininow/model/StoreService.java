package com.longnh.mobile.mininow.model;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.longnh.mobile.mininow.entity.Store;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

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

    public static void getNearStores(final FirestoreCallback callback) {
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
}

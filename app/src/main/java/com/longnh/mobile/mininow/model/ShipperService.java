package com.longnh.mobile.mininow.model;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

public class ShipperService {

    private final static FirebaseFirestore db = FirestoreDB.getInstance();

    public static void updateCurrentLocation(String shipperID, GeoPoint location, final FirestoreCallback callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("currentLoc", location);
        db.collection("tracking").document(shipperID).set(location)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(true);
                    } else {
                        Log.w("FIREBASE", "Error getting documents.", task.getException());
                    }
                });
    }

    public static void getShipperLocation(String shipperID, final FirestoreCallback callback) {
        db.collection("tracking").document(shipperID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        GeoPoint location = doc.getGeoPoint("currentLoc");
                        callback.onSuccess(location);
                    } else {
                        Log.w("FIREBASE", "Error getting documents.", task.getException());
                    }
                });
    }

    public static void updateShipperLocation(String shipperID, final FirestoreCallback callback) {
        DocumentReference docRef = db.collection("tracking").document(shipperID);
        docRef.addSnapshotListener((documentSnapshot, e) -> {
            if (documentSnapshot != null && documentSnapshot.exists()) {
                GeoPoint loc = documentSnapshot.getGeoPoint("currentLoc");
                callback.onSuccess(loc);
            }
        });
    }

}

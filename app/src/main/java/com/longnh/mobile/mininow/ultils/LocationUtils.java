package com.longnh.mobile.mininow.ultils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.longnh.mobile.mininow.service.VolleyCallback;

public class LocationUtils {

    private static long MIN_UPDATE_INTERVAL = 30 * 1000;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location currentLocation = null;


    public LocationUtils(Activity activity) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(MIN_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation(VolleyCallback callback) {

        if (currentLocation == null) {
            mFusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    currentLocation = locationResult.getLastLocation();
                    callback.onSuccess(currentLocation);
                }
            }, Looper.myLooper());
        }

    }

//    private static final long REFRESH_TIME = 0;
//    private static final long REFRESH_DISTANCE = 0;
//    private static List<String> providers;
//    private static LocationManager locationManager;
//    private static Location bestLocation;
//
//    public static Location getLastKnownLocation(Activity activity, LocationListener locationListener) {
//
//        if (activity.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && activity.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(activity, "Ứng dụng chưa được cấp quyền truy cập vị trí của bạn.", Toast.LENGTH_SHORT).show();
//            return null;
//        }
//
//        locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
//        providers = locationManager.getProviders(true);
//
//        for (String provider : providers) {
//            locationManager.requestLocationUpdates(provider, REFRESH_TIME, REFRESH_DISTANCE, locationListener);
//            Location newLoccation = locationManager.getLastKnownLocation(provider);
//            android.os.SystemClock.sleep(100);
//            if (newLoccation == null) { continue; }
//            if (bestLocation == null || newLoccation.getAccuracy() < bestLocation.getAccuracy()) { bestLocation = newLoccation; }
//        }
//
//        return bestLocation;
//
//
//    }

}

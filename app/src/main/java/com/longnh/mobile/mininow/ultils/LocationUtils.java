package com.longnh.mobile.mininow.ultils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.content.Context.LOCATION_SERVICE;

public class LocationUtils {

    private static final long REFRESH_TIME = 1000 * 60 * 5;
    private static final long REFRESH_DISTANCE = 1000;
    private static List<String> providers;
    private static LocationManager locationManager;
    private static Location bestLocation;

    public static Location getLastKnownLocation(Activity activity, LocationListener locationListener) {

        if (activity.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && activity.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "Ứng dụng chưa được cấp quyền truy cập vị trí của bạn.", Toast.LENGTH_SHORT).show();
            return null;
        }

        locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        providers = locationManager.getProviders(true);

        for (String provider : providers) {
            locationManager.requestLocationUpdates(provider, REFRESH_TIME, REFRESH_DISTANCE, locationListener);
            Location newLoccation = locationManager.getLastKnownLocation(provider);
            android.os.SystemClock.sleep(100);
            if (newLoccation == null) { continue; }
            if (bestLocation == null || newLoccation.getAccuracy() < bestLocation.getAccuracy()) { bestLocation = newLoccation; }
        }

        return bestLocation;
    }

}

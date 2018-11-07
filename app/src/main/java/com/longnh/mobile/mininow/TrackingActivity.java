package com.longnh.mobile.mininow;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;
import com.longnh.mobile.mininow.model.FirestoreCallback;
import com.longnh.mobile.mininow.model.ShipperService;
import com.longnh.mobile.mininow.ultils.ConstantManager;
import com.longnh.mobile.mininow.ultils.LocationUtils;

import androidx.appcompat.app.AppCompatActivity;

public class TrackingActivity extends AppCompatActivity {

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
//            getStores(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };
    private SupportMapFragment mapFragment;
    private GoogleMap gmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        addControls();
    }

    private void addControls() {

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gmap);
        mapFragment.getMapAsync(this::onMapReady);
    }

    public void onMapReady(final GoogleMap googleMap) {

        gmap = googleMap;

        gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gmap.getUiSettings().setZoomControlsEnabled(true);

        Location location = LocationUtils.getLastKnownLocation(this, locationListener);
        setMap(new LatLng(location.getLatitude(), location.getLongitude()));
        setMarker(new LatLng(location.getLatitude(), location.getLongitude()), "My location");

        ShipperService.updateShipperLocation(ConstantManager.shipperID, data -> {
            GeoPoint loc = (GeoPoint) data;
            setMap(new LatLng(loc.getLatitude(), loc.getLongitude()));
        });
    }

    private void setMarker(LatLng location, String title) {
        MarkerOptions markerOptions = new MarkerOptions().position(location).title(title);
        gmap.addMarker(markerOptions);
    }

    private void setMap(LatLng location) {
        CameraPosition INIT = new CameraPosition.Builder().target(location).zoom(15.5F).bearing(300F)
                .tilt(50F)
                .build();
        gmap.moveCamera(CameraUpdateFactory.newCameraPosition(INIT));
    }



}

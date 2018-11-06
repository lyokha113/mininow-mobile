package com.longnh.mobile.mininow;


import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.firestore.GeoPoint;
import com.longnh.mobile.mininow.adapter.StoreRecycleAdapter;
import com.longnh.mobile.mininow.entity.Store;
import com.longnh.mobile.mininow.model.APIManager;
import com.longnh.mobile.mininow.model.StoreService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class NearStoreFragment extends Fragment {

    private static final long REFRESH_TIME = 1000 * 60;
    private static final long REFRESH_DISTANCE = 1000;
    private static final double LIMIT_DISTANCE = 5;
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
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

    List<Store> result;
    private ProgressBar spinner;
    private StoreRecycleAdapter adapter;
    private RecyclerView listStores;
    private LocationManager locationManager;


    public NearStoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_near_store, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        listStores = getActivity().findViewById(R.id.near_stores);
        spinner = getActivity().findViewById(R.id.progress_bar_near_stores);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (result == null) {
            locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            if (getActivity().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && getActivity().checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Chưa được cấp quyền truy cập vị trí của bạn.", Toast.LENGTH_SHORT).show();
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, REFRESH_TIME, REFRESH_DISTANCE, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            getStores(location);
        } else {
            setStoreList();
        }
    }

    private void setStoreList() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        listStores.setLayoutManager(mLayoutManager);
        listStores.setHasFixedSize(true);
        listStores.setAdapter(adapter);
        spinner.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    private void getStores(final Location location) {
        spinner.setVisibility(View.VISIBLE);
        StoreService.getNearStores(data -> {
            final List<Store> stores = (List<Store>) data;
            String origin = location.getLatitude() + "," + location.getLongitude();
            String destination = "";
            for (Store store : stores) {
                GeoPoint location1 = store.getLocation();
                destination += location1.getLatitude() + "," + location1.getLongitude() + "|";
            }

            destination = destination.substring(0, destination.length() - 1);

            APIManager.getStoreDistance(getActivity(), origin, destination, data1 -> {
                List<String> distances = (List<String>) data1;
                for (int i = 0; i < distances.size(); i++) {
                    String distance = distances.get(i);
                    double range = Double.parseDouble(distance.substring(0, distance.length() - 3));
                    result = new ArrayList<>();
                    if (range <= LIMIT_DISTANCE) {
                        stores.get(i).setDistance(distance);
                        result.add(stores.get(i));
                    }
                }

                result.sort(Comparator.comparing(Store::getDistance));

                adapter = new StoreRecycleAdapter(getActivity(), stores);
                setStoreList();
            });
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}

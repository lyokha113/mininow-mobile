package com.longnh.mobile.mininow;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.longnh.mobile.mininow.adapter.StoreRecycleAdapter;
import com.longnh.mobile.mininow.entity.Store;
import com.longnh.mobile.mininow.model.StoreService;
import com.longnh.mobile.mininow.ultils.LocationUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NearStoreFragment extends Fragment {

    private static final double LIMIT_DISTANCE = 10;
    List<Store> result;
    private ProgressBar spinner;
    private StoreRecycleAdapter adapter;
    private RecyclerView listStores;
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
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
            getStores(LocationUtils.getLastKnownLocation(getActivity(), locationListener));
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
    }

    private void getStores(final Location location) {

        if (location == null) {
            spinner.setVisibility(View.GONE);
            return;
        }

        spinner.setVisibility(View.VISIBLE);
        StoreService.getAll(getContext(), data -> {
            final List<Store> stores = (List<Store>) data;
            String origin = location.getLatitude() + "," + location.getLongitude();
            String destination = "";
            for (Store store : stores) {
                destination += store.getLatitude() + "," + store.getLongitude() + "|";
            }

            destination = destination.substring(0, destination.length() - 1);

            StoreService.getStoreDistance(getActivity(), origin, destination, data1 -> {
                List<String> distances = (List<String>) data1;
                result = new ArrayList<>();
                for (int i = 0; i < distances.size(); i++) {
                    String distance = distances.get(i);
                    double range = Double.parseDouble(distance.substring(0, distance.length() - 3));
                    if (range <= LIMIT_DISTANCE) {
                        stores.get(i).setDistance(distance);
                        result.add(stores.get(i));
                    }
                }

                result.sort(Comparator.comparing(Store::getDistance));

                adapter = new StoreRecycleAdapter(getActivity(), result);
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

package com.longnh.mobile.mininow;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.longnh.mobile.mininow.adapter.OrderRecycleAdapter;
import com.longnh.mobile.mininow.adapter.TemporaryOrderRecycleAdapter;
import com.longnh.mobile.mininow.entity.Store;
import com.longnh.mobile.mininow.model.StoreService;
import com.longnh.mobile.mininow.ultils.ConstantManager;
import com.longnh.mobile.mininow.ultils.LocationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OngoingFragment extends Fragment {

    private LinearLayout ongoingFragment;
    private SharedPreferences sharedPreferences;
    private List<Store> stores;
    private RecyclerView listStore;
    private OrderRecycleAdapter adapter;
    private ProgressBar spinner;

    public OngoingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ongoing, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        ongoingFragment = getActivity().findViewById(R.id.ongoing_fragment);
        listStore = getActivity().findViewById(R.id.list_ongoing_order);
        spinner = getActivity().findViewById(R.id.progress_spinner_ongoing);
        getAllTemporaryOrder();
    }

    private void getAllTemporaryOrder() {
        spinner.setVisibility(View.VISIBLE);

        sharedPreferences = getContext().getSharedPreferences(ConstantManager.ORDER_TEMPORARY, Context.MODE_PRIVATE);

        Set<String> keys = sharedPreferences.getAll().keySet();
        stores = new ArrayList<>();
        StoreService.getAll(data -> {

            List<Store> rs = (List<Store>) data;
            for (String key : keys) {
                for (int i = 0; i < rs.size(); i++) {
                    Store tmp = rs.get(i);
                    if (tmp.getId().equals(key)) {
                        stores.add(tmp);
                        break;
                    }
                }
            }

            spinner.setVisibility(View.GONE);
            ongoingFragment.setBackground(null);

            adapter = new OrderRecycleAdapter(getActivity(), stores);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
            listStore.setLayoutManager(layoutManager);
            listStore.setHasFixedSize(true);
            listStore.setAdapter(adapter);
        });

    }


}

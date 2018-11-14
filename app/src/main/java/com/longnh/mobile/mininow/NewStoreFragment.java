package com.longnh.mobile.mininow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;
import com.longnh.mobile.mininow.adapter.StoreRecycleAdapter;
import com.longnh.mobile.mininow.entity.Store;
import com.longnh.mobile.mininow.model.StoreService;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewStoreFragment extends Fragment {

    private StoreRecycleAdapter adapter;
    private RecyclerView listStores;
    private ProgressBar spinner;
    private List<Store> stores;

    public NewStoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_store, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        listStores = getActivity().findViewById(R.id.new_stores);
        spinner = getActivity().findViewById(R.id.progress_bar_new_store);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (stores == null) {
            getStores();
        } else {
            setStoreList();
        }

    }

    private void setStoreList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        listStores.setLayoutManager(layoutManager);
        listStores.setHasFixedSize(true);
        listStores.setAdapter(adapter);
        spinner.setVisibility(View.GONE);
    }

    private void getStores() {
        spinner.setVisibility(View.VISIBLE);
        StoreService.getNewStore(getContext(), data -> {
            stores = (List<Store>) data;
            adapter = new StoreRecycleAdapter(getActivity(), stores);
            setStoreList();
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

package com.longnh.mobile.mininow;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.longnh.mobile.mininow.adapter.StoreRecycleAdapter;
import com.longnh.mobile.mininow.model.Store;
import com.longnh.mobile.mininow.service.StoreService;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainStoreFragment extends Fragment {

    private StoreRecycleAdapter adapter;
    private RecyclerView listStores;
    private ProgressBar spinner;
    private List<Store> stores;
    private SearchView searchStore;

    public MainStoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_store, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        listStores = getActivity().findViewById(R.id.main_stores);
        spinner = getActivity().findViewById(R.id.progress_bar_main_store);
        searchStore = getActivity().findViewById(R.id.search_store);

        spinner.setVisibility(View.GONE);

        searchStore.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    findStore(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }


    private void setStoreList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        listStores.setLayoutManager(layoutManager);
        listStores.setHasFixedSize(true);
        listStores.setAdapter(adapter);
        spinner.setVisibility(View.GONE);
    }

    private void findStore(String name) {
        spinner.setVisibility(View.VISIBLE);
        StoreService.findStore(getContext(), name, data -> {
            stores = (List<Store>) data;
        });
        adapter = new StoreRecycleAdapter(getActivity(), stores);
        setStoreList();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private static boolean containsIgnoreCase(String str, String searchStr)     {
        if(str == null || searchStr == null) return false;

        final int length = searchStr.length();
        if (length == 0)
            return true;

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length))
                return true;
        }
        return false;
    }
}

package com.longnh.mobile.mininow;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.longnh.mobile.mininow.adapter.TemporaryOrderRecycleAdapter;
import com.longnh.mobile.mininow.entity.Store;
import com.longnh.mobile.mininow.model.StoreService;
import com.longnh.mobile.mininow.ultils.ConstantManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class DraftFragment extends Fragment {

    private FrameLayout draftFragment;
    private SharedPreferences sharedPreferences;
    private List<Store> stores;
    private RecyclerView listStore;
    private TemporaryOrderRecycleAdapter adapter;
    private ProgressBar spinner;

    public DraftFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_draft, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        draftFragment = getActivity().findViewById(R.id.draft_fragment);
        listStore = getActivity().findViewById(R.id.list_tmp_order);
        spinner = getActivity().findViewById(R.id.progress_spinner);
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
            draftFragment.setBackground(null);

            adapter = new TemporaryOrderRecycleAdapter(getActivity(), stores);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
            listStore.setLayoutManager(layoutManager);
            listStore.setHasFixedSize(true);
            listStore.setAdapter(adapter);
        });

    }
}

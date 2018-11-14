package com.longnh.mobile.mininow;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.longnh.mobile.mininow.adapter.TemporaryOrderRecycleAdapter;
import com.longnh.mobile.mininow.entity.Store;
import com.longnh.mobile.mininow.model.StoreService;
import com.longnh.mobile.mininow.ultils.ConstantManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class DraftFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private List<Store> stores;
    private RecyclerView listStore;
    private TemporaryOrderRecycleAdapter adapter;
    private ProgressBar spinner;
    private LinearLayout removeAll;

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
        sharedPreferences = getContext().getSharedPreferences(ConstantManager.ORDER_TEMPORARY, Context.MODE_PRIVATE);
        listStore = getActivity().findViewById(R.id.list_tmp_order);
        spinner = getActivity().findViewById(R.id.progress_spinner);
        removeAll = getActivity().findViewById(R.id.remove_all);

        removeAll.setOnClickListener(v -> {
            if (stores.size() == 0) return;
            AlertDialog.Builder alert = new AlertDialog.Builder(DraftFragment.this.getActivity());
            alert.setTitle("Xác nhận");
            alert.setMessage("Bạn có muốn xoá toàn bộ đơn nháp ?");
            alert.setPositiveButton("Xoá", (dialog, which) -> {
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.clear();
                edit.apply();
                stores.clear();
                adapter.notifyDataSetChanged();
                dialog.dismiss();

            });
            alert.setNegativeButton("Huỷ", (dialog, which) -> dialog.dismiss());
            alert.show();
        });

        getAllTemporaryOrder();
    }

    private void getAllTemporaryOrder() {
        spinner.setVisibility(View.VISIBLE);



        Set<String> keys = sharedPreferences.getAll().keySet();
        stores = new ArrayList<>();
        StoreService.getAll(getContext(), data -> {

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
            adapter = new TemporaryOrderRecycleAdapter(getActivity(), stores);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
            listStore.setLayoutManager(layoutManager);
            listStore.setHasFixedSize(true);
            listStore.setAdapter(adapter);
        });

    }
}

package com.longnh.mobile.mininow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longnh.mobile.mininow.adapter.OrderHistoryRecycleAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderHistoryFragment extends Fragment {


    private View view;
    private List<Object> orderHistory = new ArrayList<>();
    private RecyclerView rvItems;
    private OrderHistoryRecycleAdapter adapter;

    public OrderHistoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_history, container, false);

        for (int i = 0; i < 20; i++) {
            orderHistory.add(null);
        }
        updateList();
        return view;
    }

    private void updateList() {
        adapter = new OrderHistoryRecycleAdapter(getActivity(), orderHistory);
        rvItems = view.findViewById(R.id.rvOrderHistory);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rvItems.setLayoutManager(layoutManager);
        rvItems.setHasFixedSize(true);
        rvItems.setAdapter(adapter);
    }
}

package com.longnh.mobile.mininow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longnh.mobile.mininow.adapter.OrderHistoryRecycleAdapter;
import com.longnh.mobile.mininow.adapter.OrderRecycleAdapter;
import com.longnh.mobile.mininow.entity.Order;
import com.longnh.mobile.mininow.model.OrderService;
import com.longnh.mobile.mininow.ultils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderHistoryFragment extends Fragment {


    private View view;
    private List<Order> orders;
    private RecyclerView rvItems;
    private OrderHistoryRecycleAdapter adapter;

    public OrderHistoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_history, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        rvItems = getActivity().findViewById(R.id.rvOrderHistory);
        getFinishedOrder();
    }

    private void getFinishedOrder() {

        orders = new ArrayList<>();
        OrderService.getFinishedOrder(getContext(), ConstantManager.customerID, data -> {

            orders = (List<Order>)data;
            adapter = new OrderHistoryRecycleAdapter(getActivity(), orders);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
            rvItems.setLayoutManager(layoutManager);
            rvItems.setHasFixedSize(true);
            rvItems.setAdapter(adapter);
        });
    }
}

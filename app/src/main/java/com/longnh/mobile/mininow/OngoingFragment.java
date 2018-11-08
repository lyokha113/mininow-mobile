package com.longnh.mobile.mininow;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.longnh.mobile.mininow.adapter.OrderRecycleAdapter;
import com.longnh.mobile.mininow.entity.Order;
import com.longnh.mobile.mininow.model.OrderService;
import com.longnh.mobile.mininow.ultils.ConstantManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OngoingFragment extends Fragment {

    private LinearLayout ongoingFragment;
    private SharedPreferences sharedPreferences;
    private List<Order> orders;
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

        orders = new ArrayList<>();
        OrderService.getOngoingOrder(ConstantManager.customerID, data -> {

            orders = (List<Order>)data;
            spinner.setVisibility(View.GONE);
            ongoingFragment.setBackground(null);

            adapter = new OrderRecycleAdapter(getActivity(), orders);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
            listStore.setLayoutManager(layoutManager);
            listStore.setHasFixedSize(true);
            listStore.setAdapter(adapter);
        });

    }


}

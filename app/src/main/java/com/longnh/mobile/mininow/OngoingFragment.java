package com.longnh.mobile.mininow;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longnh.mobile.mininow.adapter.OrderRecycleAdapter;
import com.longnh.mobile.mininow.model.Customer;
import com.longnh.mobile.mininow.model.Order;
import com.longnh.mobile.mininow.service.OrderService;
import com.longnh.mobile.mininow.ultils.UserSession;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OngoingFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private List<Order> orders;
    private RecyclerView listStore;
    private OrderRecycleAdapter adapter;
    private Customer current;

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
        listStore = getActivity().findViewById(R.id.list_ongoing_order);
        UserSession session = new UserSession(getContext(), UserSession.UserSessionType.CUSTOMER);
        current = session.getCustomerDetails();
        getAllTemporaryOrder();
    }

    private void getAllTemporaryOrder() {

        orders = new ArrayList<>();
        OrderService.getOngoingOrder(getContext(), current.getId(), data -> {

            orders = (List<Order>)data;
            adapter = new OrderRecycleAdapter(getActivity(), orders);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
            listStore.setLayoutManager(layoutManager);
            listStore.setHasFixedSize(true);
            listStore.setAdapter(adapter);
        });

    }


}

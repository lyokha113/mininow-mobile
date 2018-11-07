package com.longnh.mobile.mininow;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.tabs.TabLayout;


import com.longnh.mobile.mininow.adapter.ViewPagerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int selectedTab;

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
        initTablayout();

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(selectedTab).select();
    }

    private void initView() {
        tabLayout = getActivity().findViewById(R.id.tabLayoutOrderScreen);
        viewPager = getActivity().findViewById(R.id.viewPagerOrderScreen);
    }

    private void initTablayout() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new OngoingFragment(), "Đang đến");
        adapter.addFrag(new OrderHistoryFragment(), "Lịch sử");
        adapter.addFrag(new DraftFragment(), "Đơn nháp");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        selectedTab = tabLayout.getSelectedTabPosition();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}

package com.longnh.mobile.mininow;


import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import ss.com.bannerslider.ImageLoadingService;
import ss.com.bannerslider.Slider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends Fragment {

    Slider slider;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public StoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Slider.init(new ImageLoadingService() {
            @Override
            public void loadImage(String url, ImageView imageView) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(url));
            }

            @Override
            public void loadImage(int resource, ImageView imageView) {
                imageView.setImageResource(resource);
            }

            @Override
            public void loadImage(String url, int placeHolder, int errorDrawable, ImageView imageView) {
            }
        });

        slider = getActivity().findViewById(R.id.store_banner);
        slider.setAdapter(new StoreSliderAdapter());
        slider.setSelectedSlide(0);


        viewPager = getActivity().findViewById(R.id.accountviewpager);
        setupViewPager(viewPager);

        tabLayout = getActivity().findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new MainStoreFragment(), "Nổi Bật");
        adapter.addFrag(new NewStoreFragment(), "Mới");
        adapter.addFrag(new NearStoreFragment(), "Gần Tôi");
        viewPager.setAdapter(adapter);
    }
}

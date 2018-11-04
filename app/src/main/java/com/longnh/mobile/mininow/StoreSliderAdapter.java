package com.longnh.mobile.mininow;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class StoreSliderAdapter extends SliderAdapter {

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        switch (position) {
            case 0:
                viewHolder.bindImageSlide(R.drawable.store_banner1);
                break;
            case 1:
                viewHolder.bindImageSlide(R.drawable.store_banner2);
                break;
            case 2:
                viewHolder.bindImageSlide(R.drawable.store_banner3);
                break;
        }
    }
}
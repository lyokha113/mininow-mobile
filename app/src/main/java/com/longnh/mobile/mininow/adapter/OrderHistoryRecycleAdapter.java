package com.longnh.mobile.mininow.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longnh.mobile.mininow.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class OrderHistoryRecycleAdapter extends RecyclerView.Adapter<OrderHistoryRecycleAdapter.DataViewHolder> {

    private List<Object> listPost;
    private Activity activity;

    public OrderHistoryRecycleAdapter(Activity activity, List<Object> listPosts) {
        this.listPost = listPosts;
        this.activity = activity;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_history_item, viewGroup, false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderHistoryRecycleAdapter.DataViewHolder holder, int position) {

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPost == null ? 0 : listPost.size();
    }


    public class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemClickListener itemClickListener;

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public DataViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }

    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }

}


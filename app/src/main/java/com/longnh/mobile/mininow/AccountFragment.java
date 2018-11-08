package com.longnh.mobile.mininow;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.longnh.mobile.mininow.ultils.ConstantManager;
import com.squareup.picasso.Picasso;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private TextView changeInfo, changePass, cusName;
    private ImageView cusImg;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        changeInfo = getActivity().findViewById(R.id.change_info);
        changePass = getActivity().findViewById(R.id.change_password);
        cusName = getActivity().findViewById(R.id.custom_name_info);
        cusImg = getActivity().findViewById(R.id.custom_img_info);

        changeInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DTTActivity.class);
            startActivity(intent);
        });

        changePass.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DMKActivity.class);
            startActivity(intent);
        });

        cusName.setText(ConstantManager.customer.getName());
        Picasso.get().load(ConstantManager.customer.getImgUrl()).into(cusImg);
    }
}

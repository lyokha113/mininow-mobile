package com.longnh.mobile.mininow;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.longnh.mobile.mininow.model.Customer;
import com.longnh.mobile.mininow.ultils.UserSession;
import com.squareup.picasso.Picasso;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private TextView changeInfo, changePass, cusName, logout;
    private ImageView cusImg;
    private  UserSession session;
    private Customer current;

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
//        changePass = getActivity().findViewById(R.id.change_password);
        cusName = getActivity().findViewById(R.id.custom_name_info);
        cusImg = getActivity().findViewById(R.id.custom_img_info);
        logout = getActivity().findViewById(R.id.logout);

        session = new UserSession(getContext(), UserSession.UserSessionType.CUSTOMER);
        current = session.getCustomerDetails();

        changeInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangeInformationActivity.class);
            startActivity(intent);
        });

//        changePass.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), DMKActivity.class);
//            startActivity(intent);
//        });

        logout.setOnClickListener(v -> {
            session.removeCustomerSession();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
        });

        cusName.setText(current.getName());
        Picasso.get().load(current.getImgURL()).into(cusImg);
    }
}

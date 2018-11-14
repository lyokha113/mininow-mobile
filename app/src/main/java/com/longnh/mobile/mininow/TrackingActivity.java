package com.longnh.mobile.mininow;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;
import com.longnh.mobile.mininow.entity.Order;
import com.longnh.mobile.mininow.model.OrderService;
import com.longnh.mobile.mininow.model.ShipperService;
import com.longnh.mobile.mininow.ultils.ConstantManager;
import com.longnh.mobile.mininow.ultils.LocationUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TrackingActivity extends AppCompatActivity {

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };
    private SupportMapFragment mapFragment;
    private GoogleMap gmap;
    private long orderID;
    private Order order;
    private TextView price, pricePay;
    private ImageView step1, step2, step3, step4;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        addControls();
        addEvents();

        Intent intent = getIntent();
        orderID = intent.getLongExtra("orderID", 0);
        getOrderInfo();
    }

    private void addEvents() {
        cancel.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(TrackingActivity.this);
            alert.setTitle("Xác nhận");
            alert.setMessage("Bạn có muốn huỷ đơn hàng này ?");
            alert.setPositiveButton("Huỷ", (dialog, which) -> {
                OrderService.cancleOrder(getApplicationContext(), orderID, ConstantManager.ORDER_REJECTED, null);
                dialog.dismiss();
                finish();
            });
            alert.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
            alert.show();

        });
    }

    private void addControls() {

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gmap);
        price = findViewById(R.id.total_price);
        pricePay = findViewById(R.id.total_price_pay);
        step1 = findViewById(R.id.step_1);
        step2 = findViewById(R.id.step_2);
        step3 = findViewById(R.id.step_3);
        step4 = findViewById(R.id.step_4);
        cancel = findViewById(R.id.cancel_button);

        mapFragment.getMapAsync(this::onMapReady);
    }

    public void onMapReady(final GoogleMap googleMap) {

        gmap = googleMap;

        gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gmap.getUiSettings().setZoomControlsEnabled(true);

        Location location = LocationUtils.getLastKnownLocation(this, locationListener);
        setMap(new LatLng(location.getLatitude(), location.getLongitude()));
        setMarker(new LatLng(location.getLatitude(), location.getLongitude()), "Vị trí của bạn", true);

//        trackingOrder();

    }

    private void setMarker(LatLng location, String title, boolean isMyLocation) {
        MarkerOptions markerOptions = new MarkerOptions().position(location)
                .title(title);
        markerOptions.icon(
                isMyLocation ? BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                        : BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        gmap.addMarker(markerOptions);
    }

    private void setMap(LatLng location) {
        CameraPosition INIT = new CameraPosition.Builder().target(location).zoom(15.5F).bearing(300F)
                .tilt(50F)
                .build();
        gmap.moveCamera(CameraUpdateFactory.newCameraPosition(INIT));
    }

    private void getOrderInfo() {
        OrderService.getOrderInfo(getApplicationContext(), orderID, data -> {
            order = (Order) data;
            price.setText((order.getShipPrice() + order.getProductPrice()) + " VND - Tiền mặt");
            pricePay.setText((order.getShipPrice() + order.getProductPrice()) + " VND");
        });
    }

//    private void trackingOrder() {
//        OrderService.trackingOrderStatus(orderID, data -> {
//            Long status = (Long) data;
//            if (status == ConstantManager.ORDER_APPROVE) {
//                step1.setImageResource(R.drawable.ic_checked);
//            }
//
//            if (status == ConstantManager.ORDER_ACCEPTED) {
//                step1.setImageResource(R.drawable.ic_checked);
//                step2.setImageResource(R.drawable.ic_checked);
//                cancel.setVisibility(View.GONE);
//                OrderService.getShipperOfOrder(orderID, shipper -> {
//                    String shipperID = (String) shipper;
//                    trackingShipper(shipperID);
//                });
//            }
//
//            if (status == ConstantManager.ORDER_PICKED) {
//                step1.setImageResource(R.drawable.ic_checked);
//                step2.setImageResource(R.drawable.ic_checked);
//                step3.setImageResource(R.drawable.ic_checked);
//                cancel.setVisibility(View.GONE);
//            }
//
//            if (status == ConstantManager.ORDER_REJECTED) {
//                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);
//                alert.setTitle("Thông báo");
//                alert.setMessage("Đơn hàng đã bị huỷ");
//                alert.setPositiveButton("Đóng", null);
//                alert.show();
//            }
//
//            if (status == ConstantManager.ORDER_DONE) {
//                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);
//                alert.setTitle("Thông báo");
//                alert.setMessage("Đơn hàng giao thành công");
//                alert.setPositiveButton("Đóng", null);
//                alert.show();
//            }
//
//            if (status == ConstantManager.ORDER_FAILED) {
//                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);
//                alert.setTitle("Thông báo");
//                alert.setMessage("Đơn hàng giao thất bại");
//                alert.setPositiveButton("Đóng", null);
//                alert.show();
//            }
//        });
//    }

//    private void trackingShipper(String shipperID) {
//        ShipperService.trackingShipperLocation(shipperID, data -> {
//            GeoPoint loc = (GeoPoint) data;
//            setMap(new LatLng(loc.getLatitude(), loc.getLongitude()));
//            setMarker(new LatLng(loc.getLatitude(), loc.getLongitude()), "Vị trí shipper", false);
//        });
//    }

}

package com.longnh.mobile.mininow;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.longnh.mobile.mininow.model.Order;
import com.longnh.mobile.mininow.model.Store;
import com.longnh.mobile.mininow.service.MapService;
import com.longnh.mobile.mininow.ultils.CalculateDistanceTime;
import com.longnh.mobile.mininow.ultils.ConstantManager;
import com.longnh.mobile.mininow.ultils.DirectionsJSONParser;
import com.longnh.mobile.mininow.ultils.LocationUtils;
import com.longnh.mobile.mininow.ultils.UserSession;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class ShipperActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

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
    CalculateDistanceTime distanceTask;
    private SupportMapFragment mapFragment;
    private GoogleMap gmap;
    private Button thanhcong, thatbai, pickedOrder;
    private LinearLayout orderInfo;
    private Button orderDetail, takeOrder;
    private Order selectedOrder;
    private LatLng shipperLocation;
    private ProgressDialog progressDialog;
    private boolean isFinding, isPicking, isDelivering;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipper);

        Toolbar toolbar = findViewById(R.id.toolbar);
        thanhcong = findViewById(R.id.giaohangthanhcong);
        thatbai = findViewById(R.id.giaohangthatbai);
        pickedOrder = findViewById(R.id.pickedOrder);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        orderInfo = findViewById(R.id.order_info);
        orderInfo.setVisibility(View.GONE);

        orderDetail = findViewById(R.id.detail);
        orderDetail.setOnClickListener(v -> {
            final Dialog mTopDialog = new Dialog(ShipperActivity.this, R.style.MaterialDialogSheetDown);
            mTopDialog.setContentView(R.layout.activity_list_popup);
            mTopDialog.setCancelable(true);
            mTopDialog.getWindow().setGravity(Gravity.TOP);
            mTopDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mTopDialog.show();
        });

        takeOrder = findViewById(R.id.takeOrder);
        takeOrder.setOnClickListener(v -> {
            Toast.makeText(ShipperActivity.this, "Take", Toast.LENGTH_LONG).show();
            LatLng shipper = new LatLng(shipperLocation.latitude, shipperLocation.longitude);
            LatLng shop = new LatLng(selectedOrder.getStore().getLatitude(), selectedOrder.getStore().getLongitude());
            getDirection(shipper, shop);
            pickedOrder.setVisibility(View.VISIBLE);
            takeOrder.setVisibility(View.GONE);
        });

        distanceTask = new CalculateDistanceTime(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gmap);
        mapFragment.getMapAsync(this::onMapReady);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }

    public void onMapReady(final GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gmap.getUiSettings().setZoomControlsEnabled(true);
//        Location loc = LocationUtils.getLastKnownLocation(ShipperActivity.this, locationListener);
        shipperLocation = new LatLng(10F, 10F);
        setMap(shipperLocation, 13.0F);
        setShipperMarker(shipperLocation);
        CircleOptions circleOptions = new CircleOptions()
                .center(shipperLocation)
                .strokeColor(Color.rgb(140, 181, 237))
                .fillColor(Color.argb(100, 240, 245, 252))
                .radius(5000);
        gmap.addCircle(circleOptions);

        gmap.setOnMarkerClickListener(marker -> {
            setMap(marker.getPosition(), 15.0F);
            showOrder((Order) marker.getTag());
            return false;
        });

        gmap.setOnMapClickListener(latLng -> hideOrder());

        Store store = new Store();
        store.setLatitude(10.8083265);
        store.setLongitude(106.6364821);

        Order order = new Order();
        order.setStore(store);

        setStoreMarker(new LatLng(10.8083265, 106.6364821), order);



//        setCurrentLocation(location);
    }

    private void setStoreMarker(LatLng location, Order order) {
        MarkerOptions markerOptions = new MarkerOptions().position(location).title("Đơn hàng mới");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.store));
        gmap.addMarker(markerOptions).setTag(order);
    }

    private void setShipperMarker(LatLng location) {
        MarkerOptions markerOptions = new MarkerOptions().position(location).title("Vị trí của tôi");
        gmap.addMarker(markerOptions);
    }

    private void setMap(LatLng location, float zoom) {
        CameraPosition INIT = new CameraPosition.Builder().target(location).zoom(zoom).bearing(300F)
                .tilt(50F)
                .build();
        gmap.moveCamera(CameraUpdateFactory.newCameraPosition(INIT));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.profile) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.dangxuat) {
            UserSession session = new UserSession(getApplicationContext(), UserSession.UserSessionType.SHIPPER);
            session.removeCustomerSession();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setCurrentLocation(Location location) {
//        GeoPoint loc = new GeoPoint(location.getLatitude(), location.getLongitude());
//        ShipperService.updateCurrentLocation(ConstantManager.shipperID, loc, new FirestoreCallback() {
//            @Override
//            public void onSuccess(Object data) {
//                Toast.makeText(getApplicationContext(), "Ca5 nhat6", Toast.LENGTH_LONG).show();
//            }
//        });
    }

    private void showOrder(Order order) {
        orderInfo.setVisibility(View.VISIBLE);
        selectedOrder = order;
    }

    private void hideOrder() {
        orderInfo.setVisibility(View.GONE);
        selectedOrder = null;
    }


    public void getDirection(LatLng origin, LatLng dest) {

        showProgressDialog();

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + ConstantManager.API_GOOGLE_KEY;

        MapService.getDircection(ShipperActivity.this, url, data -> {
            ParserTask parser = new ParserTask();
            parser.execute((String) data);
        });
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = new ArrayList<>();
            PolylineOptions lineOptions = new PolylineOptions();

            for (int i = 0; i < result.size(); i++) {
                List<HashMap<String, String>> path = result.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.CYAN);
                gmap.addPolyline(lineOptions);
                hideProgressDialog();
            }
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Đang xử lý ...");
            progressDialog.setIndeterminate(true);
        }

        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}

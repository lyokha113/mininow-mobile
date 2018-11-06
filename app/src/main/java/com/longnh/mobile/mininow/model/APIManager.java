package com.longnh.mobile.mininow.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class APIManager {

    private static final String TAG = APIManager.class.toString();

    public static void getStoreDistance(Context context, String origins, String destination, final VolleyCallback callback) {

        RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();

        String URL = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + origins +
                "&destinations=" + destination +"&key=AIzaSyBsY-26loYcr2kpIARp5wTmbExsf-BWC7M";

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray rows = response.getJSONArray("rows");
                            JSONObject row = (JSONObject) rows.get(0);
                            JSONArray elements = row.getJSONArray("elements");

                            List<String> distances = new ArrayList<>();

                            for (int i = 0; i < elements.length(); i++) {
                                JSONObject element = (JSONObject) elements.get(i);
                                JSONObject distance = (JSONObject) element.get("distance");
                                distances.add(distance.getString("text"));
                            }
                            callback.onSuccess(distances);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.e(TAG, "onResponse: " + ex.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: " + error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);
    }
}
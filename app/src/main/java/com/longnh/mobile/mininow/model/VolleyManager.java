package com.longnh.mobile.mininow.model;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyManager {

    private RequestQueue requestQueue;
    private static VolleyManager INSTANCE;

    private VolleyManager(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public static synchronized VolleyManager getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new VolleyManager(context);
        return INSTANCE;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}

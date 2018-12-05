package com.longnh.mobile.mininow.ultils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CalculateDistanceTime {

    private taskCompleteListener mTaskListener;
    private Context mContext;

    public CalculateDistanceTime(Context context) {
        mContext = context;
    }

    void setLoadListener(taskCompleteListener taskListener) {
        mTaskListener = taskListener;
    }

    void getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        HttpURLConnection urlConnection;
        URL url = new URL(strUrl);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.connect();
        try (InputStream iStream = urlConnection.getInputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Excp. while downloading", e.toString());
        } finally {
            urlConnection.disconnect();
        }
        return data;
    }


    interface taskCompleteListener {
        void taskCompleted(String[] time_distance);
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<HashMap<String, String>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DistanceTimeParser parser = new DistanceTimeParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String duration_distance = "";
            if (result.size() < 1) {
                Log.e("Error : ", "No Points found");
                return;
            }

            String[] date_dist = new String[2];
            for (int i = 0; i < result.size(); i++) {
                HashMap<String, String> tmpData = result.get(i);
                Set<String> key = tmpData.keySet();
                Iterator it = key.iterator();
                while (it.hasNext()) {
                    String hmKey = (String) it.next();
                    duration_distance = tmpData.get(hmKey);
                    System.out.println("Key: " + hmKey + " & Data: " + duration_distance);
                    it.remove();
                }
                date_dist[i] = duration_distance;
            }
            mTaskListener.taskCompleted(date_dist);
        }
    }
}


package com.example.martin.mapbox;


import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Server {

    public static final String REST_URL = "http://147.175.152.178:8090";
    public static final String SUCCESS_TAG = "success";
    public static final String FAIL_TAG = "fail";

    private class AsyncLocation extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String urlAction = params[0];
                String countString = params[1];
                int count = Integer.parseInt(countString);
                URL url = new URL(REST_URL+urlAction);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(60000);
                con.setConnectTimeout(60000);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder();
                for(int i = 0; i < count; i++)
                    builder.appendQueryParameter(params[2+i*2], params[3+i*2]);
                String query = builder.build().getEncodedQuery();

                OutputStream outputStream = new BufferedOutputStream(con.getOutputStream());
                outputStream.write(query.getBytes());
                outputStream.flush();
                outputStream.close();
                con.connect();
                int code = con.getResponseCode();
                int j = 0;
                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    con.disconnect();
                    return (result.toString());

                } else {
                    con.disconnect();
                    return (FAIL_TAG);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return FAIL_TAG;
        }
    }

    public ArrayList<Location> getLocations(String amenity, Location userLocation) {
        ArrayList<Location> locations = new ArrayList<>();

        try {
            String locationString = new AsyncLocation().execute(
                    "/location", "3",
                    "amenity", amenity,
                    "locationX", String.valueOf(userLocation.getX()),
                    "locationY", String.valueOf(userLocation.getY())).get();

            JSONArray jsonArray = new JSONArray(locationString);
            JSONObject element;
            Location location;
            for (int index = 1; index < jsonArray.length(); index++) {
                location = new Location();
                element = jsonArray.getJSONObject(index);
                location.setX(element.getDouble("x"));
                location.setY(element.getDouble("y"));
                location.setName(element.getString("name"));
                location.setAmenity(element.getString("amenity"));
                location.setIndex(element.getInt("index"));
                locations.add(location);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return locations;
    }

    public ArrayList<Way> getCycrcle(final Location userLocation){
        String[] params = new String[]{ "/circle", "2",
                "locationX", String.valueOf(userLocation.getX()),
                "locationY", String.valueOf(userLocation.getY())};
        return getWays(params);
    }

    public ArrayList<Way> getWay(final Location userLocation, int length){
        String[] params = new String[]{ "/way", "3",
                "locationX", String.valueOf(userLocation.getX()),
                "locationY", String.valueOf(userLocation.getY()),
                "length", String.valueOf(length) };
        return getWays(params);
    }

    private ArrayList<Way> getWays(String... params){
        ArrayList<Way> ways = new ArrayList<>();

        try {
            String wayString = new AsyncLocation().execute(params).get();
            JSONArray jsonArrayOuter = new JSONArray(wayString);

            JSONObject element;
            Way way;
            Point point;
            for (int i=0; i<jsonArrayOuter.length(); i++){
                way = new Way();
                JSONObject wayJason = jsonArrayOuter.getJSONObject(i);
                JSONArray jsonArrayInner = wayJason.getJSONArray("points");
                int z = 0;
                for (int j=0; j<jsonArrayInner.length(); j++){
                    point = new Point();
                    element = jsonArrayInner.getJSONObject(j);
                    point.setX(element.getDouble("x"));
                    point.setY(element.getDouble("y"));
                    way.addPoint(point);
                }
                ways.add(way);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ways;
    }
}

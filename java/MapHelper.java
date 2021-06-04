package com.example.doan2;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapHelper {
    private Context context;
    private GoogleMap map;
    private final double initialFee=12000;
    private final double additionalFee=3400;
    private LatLng HoChiMinhCity;
    private double travelDuration;
    private TextView tvKilometer, tvPrice;
    private double currentRouteLength;
    private boolean isMarkerPlaced;
    private double currentLongitude, currentLatitude;
    private Marker currentMarker, destinationMarker, startMarker;
    private Polyline currentRoute;
    private OnMapReadyCallback callback;

    public double getTravelDuration()
    {
        return travelDuration;
    }

    public void setPricesDisplayingControl(TextView tvPrice) {
        this.tvPrice = tvPrice;
    }
    public void setKilometersDisplayingControl(TextView tvKilometer) {
        this.tvKilometer = tvKilometer;
    }
    public boolean hasPlacedMarker(){
        return isMarkerPlaced;
    }
    public double getCurrentRouteLength()
    {
        return currentRouteLength;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public MapHelper()
    {
        isMarkerPlaced=false;
        tvPrice=null;
        tvKilometer=null;
        currentRouteLength=0;
        currentRoute=null;
        HoChiMinhCity=new LatLng(10.762622,106.660172);
        callback = new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map=googleMap;
                currentMarker= googleMap.addMarker(new MarkerOptions().position(HoChiMinhCity).title("TP Ho Chi Minh"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HoChiMinhCity,18));
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        isMarkerPlaced=true;
                        currentLongitude =latLng.longitude;
                        currentLatitude =latLng.latitude;
                        currentMarker.remove();
                        currentMarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude,currentLongitude)).title("Diem duoc chon"));
                    }
                });
            }
        };
    }

    public OnMapReadyCallback getCallback() {
        return callback;
    }

    public double getTravelPrice()
    {
        double t=0;
        if(currentRouteLength!=0)
        {
            t=initialFee;
            double temptLeng=currentRouteLength-2000;
            t+= temptLeng > 0? temptLeng/1000*additionalFee : 0;
        }
        return t;
    }


    public void reset()
    {
        isMarkerPlaced=false;
    }

    public void updateDisplayedKm()
    {
        tvKilometer.setText(String.valueOf(currentRouteLength/1000));
    }

    public void updateDisplayedPrice()
    {
        tvPrice.setText(String.valueOf(getTravelPrice())+" VND");
    }
    public void setStartMarker()
    {
        currentMarker.remove();
        if(startMarker!=null) startMarker.remove();
        startMarker=map.addMarker(new MarkerOptions().position(new LatLng(currentLatitude,currentLongitude)).title("Diem bat dau"));
        if(destinationMarker != null) drawRoute();
        reset();
    }
    public void setStartMarker(double latitude, double longitude)
    {
        currentMarker.remove();
        if(startMarker!=null) startMarker.remove();
        startMarker=map.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("Diem bat dau"));
        if(destinationMarker != null) drawRoute();
        reset();
    }
    public void setDestinationMarker()
    {
        currentMarker.remove();
        if(destinationMarker!=null) destinationMarker.remove();
        destinationMarker=map.addMarker(new MarkerOptions().position(new LatLng(currentLatitude,currentLongitude)).title("Diem den"));
        if(startMarker != null) drawRoute();
        reset();
    }
    public void setDestinationMarker(double latitude, double longitude)
    {
        currentMarker.remove();
        if(destinationMarker!=null) destinationMarker.remove();
        destinationMarker=map.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("Diem den"));
        if(startMarker != null) drawRoute();
        reset();
    }

    public void drawRoute()
    {
        String url=getDirectionUrl();
        MapHelper.DownloadTask downloadTask=new MapHelper.DownloadTask();
        downloadTask.execute(url);
    }

    private String getDirectionUrl()
    {
        //route's original starting point
        String originalStartPoint= "origin="+String.valueOf(startMarker.getPosition().latitude)+
                ","+String.valueOf(startMarker.getPosition().longitude);
        //route's destination point
        String destinationPoint= "destination="+String.valueOf(destinationMarker.getPosition().latitude)+
                ","+String.valueOf(destinationMarker.getPosition().longitude);
        String key="key="+context.getString(R.string.api_key);

        String parameters= originalStartPoint+"&"+destinationPoint+"&"+key;
        String output="json";
        return "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
    }

    private String getRouteData(String strUrl) throws IOException
    {
        String data="";
        InputStream inputStream=null;
        HttpURLConnection urlConnection=null;
        try{
            URL url=new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            BufferedReader br= new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb= new StringBuffer();
            String line="";
            while((line= br.readLine())!=null)
            {
                sb.append(line);
            }
            data=sb.toString();
            br.close();
        }catch(Exception e)
        {
            Log.d("Exception occured while downloading - ", e.toString());
        }finally {
            inputStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    //a class to download data in background
    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service

            String data = "";

            try{
                // Fetching the data from web service
                data = getRouteData(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            MapHelper.ParserTask parserTask = new MapHelper.ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>>
    {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
                currentRouteLength=parser.getTotalLength();
                travelDuration=parser.getTravelDuration();
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline-route in google map while removing existing old route
            try {
                Polyline temptRoute = map.addPolyline(lineOptions);
                if(currentRoute!=null) currentRoute.remove();
                currentRoute=temptRoute;
                if(tvKilometer != null) updateDisplayedKm();
                if(tvPrice != null) updateDisplayedPrice();
            }catch (Exception e)
            {
                Toast.makeText(context, "Failed to draw route. Error: "+e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public String getLocationAddress(double latitude, double longitude)
    {
        String address="";
        return address;
    }
}

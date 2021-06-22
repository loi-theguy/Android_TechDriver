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
    private LatLng initialLocation;
    private double travelDuration;
    private TextView tvKilometer, tvPrice;
    private double currentRouteLength;
    private boolean isMarkerPlaced;
    private Place currentPlace;
    private Marker currentMarker, destinationMarker, startMarker;
    private Polyline currentRoute;
    private OnMapReadyCallback callback;
    private int zoomLevel;
    private Place[] checkpoints=new Place[2];

    public double getTravelDuration()
    {
        return travelDuration;
    }

    public void setInitialLocation(LatLng initialLocation) {
        this.initialLocation = initialLocation;
    }

    public Place[] getCheckpoints() {
        return checkpoints;
    }

    public Place getCurrentPlace(){ return currentPlace; }

    public void setCurrentPlace(Place currentPlace) {
        this.currentPlace = currentPlace;
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
        zoomLevel=18;
        tvKilometer=null;
        currentPlace=new Place();
        currentRouteLength=0;
        currentRoute=null;
        initialLocation =new LatLng(10.762622,106.660172);
        callback = new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map=googleMap;
                BackgroundJob backgroundJob=new BackgroundJob() {
                    @Override
                    public String getData(String url) {
                        try {
                            return downloadData(url);
                        } catch (IOException e) {
                            Log.e("GETTING_PLACE_FAIL",e.getMessage());
                        }
                        return null;
                    }

                    @Override
                    public void doOnPostExecute(String result) {
                        DirectionsJSONParser parser=new DirectionsJSONParser();
                        String address=parser.getPlaceFromLatLng(result);
                        currentPlace.setAddress(address);
                        currentMarker.remove();
                        currentMarker = googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(currentPlace.getLatitude(),currentPlace.getLongitude()))
                                .title(address));
                        currentMarker.showInfoWindow();
                    }
                };
                currentMarker= googleMap.addMarker(new MarkerOptions().position(initialLocation).title("TP Ho Chi Minh"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation,18));
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        isMarkerPlaced=true;
                        currentPlace.setLongitude(latLng.longitude);
                        currentPlace.setLatitude(latLng.latitude);
                        //update the current marker on the map
                        BackgroundTask backgroundTask=new BackgroundTask();
                        backgroundTask.job=backgroundJob;
                        backgroundTask.execute(getPlaceUrl(currentPlace.getLatitude(),currentPlace.getLongitude()));
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
        checkpoints[0]=new Place(currentPlace);
        currentMarker.remove();
        if(startMarker!=null) startMarker.remove();
        startMarker=map.addMarker(new MarkerOptions()
                .position(new LatLng(currentPlace.getLatitude(),currentPlace.getLongitude()))
                .title(currentPlace.getAddress()));
        if(destinationMarker != null) drawRoute();
        else moveCameraToCurrentMarker();
        reset();
    }
//    public void setStartMarker(double latitude, double longitude)
//    {
//        currentMarker.remove();
//        if(startMarker!=null) startMarker.remove();
//        startMarker=map.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("Diem bat dau"));
//        if(destinationMarker != null) drawRoute();
//        reset();
//    }
    public void setDestinationMarker()
    {
        checkpoints[1]=new Place(currentPlace);
        currentMarker.remove();
        if(destinationMarker!=null) destinationMarker.remove();
        destinationMarker=map.addMarker(new MarkerOptions()
                .position(new LatLng(currentPlace.getLatitude(),currentPlace.getLongitude()))
                .title(currentPlace.getAddress()));
        if(startMarker != null) drawRoute();
        else moveCameraToCurrentMarker();
        reset();
    }
//    public void setDestinationMarker(double latitude, double longitude)
//    {
//        currentMarker.remove();
//        if(destinationMarker!=null) destinationMarker.remove();
//        destinationMarker=map.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("Diem den"));
//        if(startMarker != null) drawRoute();
//        reset();
//    }

    public void drawRoute()
    {
        String url=getDirectionUrl();
        BackgroundTask backgroundTask =new BackgroundTask();
        backgroundTask.job=new BackgroundJob() {
            @Override
            public String getData(String url) {
                try {
                    return downloadData(url);
                } catch (IOException e) {
                    Log.e("ROUTING_FAIL",e.getMessage());
                }
                return null;
            }

            @Override
            public void doOnPostExecute(String result) {
                MapHelper.ParserTask parserTask = new MapHelper.ParserTask();
                // Invokes the thread for parsing the JSON data
                parserTask.execute(result);
            }
        };
        backgroundTask.execute(url);
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

    private String downloadData(String strUrl) throws IOException
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
    private interface BackgroundJob
    {
        String getData(String url);
        void doOnPostExecute(String result);
    }
    public interface OnPostGettingAddressJob
    {
        void updateCurrentLocation(Place place);
    }
    //a class to download data in background
    private class BackgroundTask extends AsyncTask<String, Void, String> {
        public BackgroundJob job;
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service

            String data = "";

            try{
                // Fetching the data from web service
//                data = getRouteData(url[0]);
                data=job.getData(url[0]);
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
//
//            MapHelper.ParserTask parserTask = new MapHelper.ParserTask();
//
//            // Invokes the thread for parsing the JSON data
//            parserTask.execute(result);
            job.doOnPostExecute(result);
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
                setZoomLevel();
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
            moveCameraToCurrentMarker();
        }
    }

    private void setZoomLevel()
    {
        double distance=currentRouteLength/1000;
        if(distance>1600)
        {
            zoomLevel=3;
            return;
        }
        if(distance>800)
        {
            zoomLevel=4;
            return;
        }
        if(distance>400) {
            zoomLevel=6;
            return;
        }
        if(distance>100) {
            zoomLevel=7;
            return;
        }
        if(distance>50) {
            zoomLevel=9;
            return;
        }
        if(distance>10) {
            zoomLevel=11;
            return;
        }
        if(distance>5) {
            zoomLevel=12;
            return;
        }
        if(distance>2) {
            zoomLevel=14;
            return;
        }
        if(distance>1) {
            zoomLevel=15;
            return;
        }
        if(distance>0.3) {
            zoomLevel=16;
            return;
        }
        zoomLevel=18;
    }
    public void moveCameraToCurrentMarker()
    {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentPlace.getLatitude(),currentPlace.getLongitude()),zoomLevel));
    }
    private String getPlaceUrl(String searchString)
    {
        return "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="+
                searchString+"&inputtype=textquery&fields=formatted_address,name,geometry&key="
                +context.getString(R.string.api_key);
    }
    private String getPlaceUrl(double latitude, double longitude)
    {
        return "https://maps.googleapis.com/maps/api/geocode/json?latlng="+String.valueOf(latitude)
                +","+String.valueOf(longitude)+"&key="+context.getString(R.string.api_key);
    }
    public void getLocationFromAddress(final OnPostGettingAddressJob onPostGettingAddressJob, String searchString)
    {
        String url=getPlaceUrl(searchString);
        BackgroundJob backgroundJob = new BackgroundJob() {
            @Override
            public String getData(String url) {
                try {
                    return downloadData(url);
                } catch (IOException e) {
                    Log.e("GETTING_PLACE_FAIL",e.getMessage());
                }
                return null;
            }

            @Override
            public void doOnPostExecute(String result) {
                DirectionsJSONParser parser=new DirectionsJSONParser();
                currentPlace= parser.getPlace(result);
                onPostGettingAddressJob.updateCurrentLocation(currentPlace);
            }
        };
        BackgroundTask task=new BackgroundTask();
        task.job=backgroundJob;
        task.execute(url);
    }
}

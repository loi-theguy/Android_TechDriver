package com.example.doan2;

public class Place {
    private double latitude, longitude;
    private String address;

    public Place() {
    }

    public Place(double latitude, double longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public Place(Place place)
    {
        this(place.getLatitude(),place.getLongitude(),place.getAddress());
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

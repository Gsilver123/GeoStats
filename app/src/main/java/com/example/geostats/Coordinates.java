package com.example.geostats;

class Coordinates {

    private double mLatitude = 0;
    private double mLongitude = 0;
    private String mLocation = "";

    Coordinates() { }

    void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    double getLatitude() {
        return mLatitude;
    }

    void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    double getLongitude() {
        return mLongitude;
    }

    void setLocation(String location) {
        mLocation = location;
    }

    String getLocation() {
        return mLocation;
    }
}

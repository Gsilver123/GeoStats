package com.example.geostats;

import com.squareup.moshi.Json;

class Coordinates {
    Double mLatitude = 0.0;
    Double mLongitude = 0.0;


    Coordinates() { }

    void setLatitude(Double latitude) {
        mLatitude = latitude;
    }

    double getLatitude() {
        return mLatitude;
    }

    void setLongitude(Double longitude) {
        mLongitude = longitude;
    }

    double getLongitude() {
        return mLongitude;
    }
}

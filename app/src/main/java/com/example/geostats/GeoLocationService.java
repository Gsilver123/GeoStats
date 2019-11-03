package com.example.geostats;

import java.util.LinkedHashMap;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeoLocationService {
    @GET("json")
    Single<Coordinates> getCoordinates(@Query("address") String locationString, @Query("key") String key);
}

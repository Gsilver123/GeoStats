package com.example.geostats;

import java.util.HashMap;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeoInformationService {
    @GET("enrich")
    Single<HashMap<String, String>> getGeoInfo(
            @Query("f") String type,
            @Query("token") String key,
            @Query("inSR") String insr,
            @Query("outSR") String outsr,
            @Query("returnGeometry") String returnGeo,
            @Query("studyAreas") String latLng,
            @Query("studyAreasOptions") String buffer,
            @Query("dataCollections") String data);
}

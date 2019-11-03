package com.example.geostats;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.ToJson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

class APIClient {

    private static final String TAG = APIClient.class.getSimpleName();
    private static final String GEO_LOCATION_API_URL = "https://maps.googleapis.com/maps/api/geocode/";
    private static final String GEO_INFORMATION_API_URL = "https://geoenrich.arcgis.com/arcgis/rest/services/World/geoenrichmentserver/GeoEnrichment/";

    private GeoLocationService mGeoLocationService;
    private GeoInformationService mGeoInformationService;

    public APIClient() {
        createGeoLocationRetrofit();
        createGeoInformationRetrofit();
    }

    private void createGeoLocationRetrofit() {
        Moshi moshi = new Moshi.Builder().add(getGeoLocationJsonAdapter()).build();
        mGeoLocationService =
                getRetrofitInstance(GEO_LOCATION_API_URL, moshi).create(GeoLocationService.class);
    }

    private void createGeoInformationRetrofit() {
        Moshi moshi = new Moshi.Builder().add(getGeoInfoJsonAdapter()).build();
        mGeoInformationService =
                getRetrofitInstance(GEO_INFORMATION_API_URL, moshi).create(GeoInformationService.class);
    }

    private JsonAdapter<HashMap<String, String>> getGeoInfoJsonAdapter() {
        return new JsonAdapter<HashMap<String, String>>() {
            @Override
            @FromJson
            public HashMap<String, String> fromJson(JsonReader reader) throws IOException {
                HashMap<String, String> geoInfoHashMap = new HashMap<>();

                reader.beginObject();
                reader.skipValue();
                reader.beginArray();
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();

                    switch(name) {
                        case "features":
                            reader.beginArray();
                            reader.beginObject();
                            if (reader.nextName().equals("attributes")) {
                                reader.beginObject();
                                while(reader.hasNext()) {
                                    String key = "";
                                    try {
                                        key = reader.nextName();
                                    }
                                    catch (Exception ex) {
                                        reader.skipValue();
                                        continue;
                                    }
                                    if (GeoInfoKeys.getInstance().getGeoInfoKeys().containsKey(key)) {
                                        geoInfoHashMap.put(GeoInfoKeys.getInstance().getGeoInfoKeys().get(key), String.valueOf(reader.nextDouble()));
                                    }
                                }
                            }
                            break;
                        case "value":
                            reader.beginObject();
                            break;
                        case "FeatureSet":
                            reader.beginArray();
                            reader.beginObject();
                            break;
                        case "fieldAliases":
                            reader.beginObject();
                            while(reader.hasNext()) { reader.skipValue(); }
                            reader.endObject();
                            break;
                        case "spatialReference":
                            reader.beginObject();
                            while (reader.hasNext()) { reader.skipValue(); }
                            reader.endObject();
                            break;
                        case "fields":
                            reader.beginArray();
                            reader.beginObject();
                            while (reader.hasNext()) {
                                while (reader.hasNext()) {
                                    reader.skipValue();
                                }
                                reader.endObject();
                                try {
                                    reader.beginObject();
                                }
                                catch (Exception ex) {
                                    reader.endArray();
                                    break;
                                }
                            }
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }


                return geoInfoHashMap;
            }

            @Override
            @ToJson
            public void toJson(JsonWriter writer, HashMap<String, String> value) throws IOException { }
        };
    }

    private JsonAdapter<Coordinates> getGeoLocationJsonAdapter() {
        return new JsonAdapter<Coordinates>() {
            @Override
            @FromJson
            public Coordinates fromJson(JsonReader reader) throws IOException {
                Coordinates coordinates = new Coordinates();
                reader.beginObject();

                if (reader.nextName().equals("results")) {
//                    System.out.println(reader.nextName());
                    reader.beginArray();
                    reader.beginObject();
                    while (reader.hasNext()) {
                        String name = reader.nextName();
                        switch(name) {
                            case "geometry": {
                                reader.beginObject();
                                while(reader.hasNext()) {
                                    name = reader.nextName();
                                    switch(name) {
                                        case "location": {
                                            reader.beginObject();
                                            while(reader.hasNext()) {
                                                name = reader.nextName();
                                                Double value = reader.nextDouble();
                                                setCoord(name, value, coordinates);
                                            }
                                            break;
                                        }
                                        default: {
                                            reader.skipValue();
                                            break;
                                        }

                                    }

                                }
                                break;
                            }
                            default: {
                                reader.skipValue();
                                break;
                            }
                        }
                    }
                }

                return coordinates;
            }

            @Override
            @ToJson
            public void toJson(JsonWriter writer, Coordinates value) throws IOException { }
        };
    }

    private void setCoord(String type, Double value, Coordinates coord) {
        if (type.equals("lat")) {
            coord.setLatitude(value);

        } else {
            coord.setLongitude(value);
        }

    }

    private Retrofit getRetrofitInstance(String url, Moshi moshi) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();
    }

    public Single<Coordinates> getCurrentLocation(String location, String key) {
        return mGeoLocationService.getCoordinates(location, key);
    }

    public Single<HashMap<String, String>> getGeoInformation(String key) {
        if (GeoViewModel.getInstance().getLocationCoordinates().getValue() == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        double latitude = GeoViewModel.getInstance().getLocationCoordinates().getValue().getLatitude();
        final double longitude = GeoViewModel.getInstance().getLocationCoordinates().getValue().getLongitude();

        Map<String, Double> map = new HashMap<>();
        map.put("y", latitude);
        map.put("x", longitude);
        Map<String, Map<String, Double>> z = new HashMap<>();
        z.put("geometry", map);
        List<Map<String, Map<String, Double>>> please = new ArrayList<>();
        please.add(z);
        String first = "";
        try {
            first = mapper.writeValueAsString(please);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<Integer> radiusList = new ArrayList<>();
        radiusList.add(50);
        String radiusString = "";
        try {
            radiusString = mapper.writeValueAsString(radiusList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Map<String, String> studyAreaOptions = new HashMap<>();
        studyAreaOptions.put("areaType", "RingBuffer");
        studyAreaOptions.put("bufferUnits", "esriMiles");
        studyAreaOptions.put("bufferRadii", radiusString);
        String second = "";
        try {
            second = mapper.writeValueAsString(studyAreaOptions);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        String b = "";
        String c = "";
        String d = "";
        try {
            b = mapper.writeValueAsString("[{geometry:{x:"
                    + GeoViewModel.getInstance().getLocationCoordinates().getValue().getLatitude() + ",y:"
                    + GeoViewModel.getInstance().getLocationCoordinates().getValue().getLongitude() + "}}]");
        c = mapper.writeValueAsString(second);
        d = mapper.writeValueAsString("[KeyGlobalFacts],KeyUSFacts]");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(GeoViewModel.getInstance().getLocationCoordinates().getValue().mLatitude);
        System.out.println(first);



        return mGeoInformationService.getGeoInfo(
                "json", key, "4326", "4326", "true", first,
                second, "[KeyGlobalFacts],KeyUSFacts]");

    }
}

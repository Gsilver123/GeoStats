package com.example.geostats;

import android.util.Log;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.ToJson;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.logging.Level;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

class APIClient {

    private static final String TAG = APIClient.class.getSimpleName();
    private static final String GEO_LOCATION_API_URL = "https://maps.googleapis.com/maps/api/geocode/";

    private static APIClient sAPIClient;
    private GeoLocationService mGeoLocationService;

    private GeoViewModel mViewModel;

    static APIClient getInstance() {
        if (sAPIClient == null) {
            sAPIClient = new APIClient();
        }
        return sAPIClient;
    }

    public APIClient() {
        createGeoLocationRetrofit();
    }

    private void createGeoLocationRetrofit() {
        Moshi moshi = new Moshi.Builder().add(getGeoLocationJsonAdapter()).build();
        mGeoLocationService =
                getRetrofitInstance(GEO_LOCATION_API_URL, moshi).create(GeoLocationService.class);
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
}

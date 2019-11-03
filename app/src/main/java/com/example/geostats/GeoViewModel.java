package com.example.geostats;

import android.util.Log;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.LinkedHashMap;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


import static android.content.ContentValues.TAG;

public class GeoViewModel extends ViewModel implements LifecycleObserver {

    private MutableLiveData<String> mLocationLiveData;
    private MutableLiveData<Coordinates> mLocationCoordinates;
    private CompositeDisposable disposable = new CompositeDisposable();
    private APIClient client;

    MutableLiveData<String> getLocation() {
        if (mLocationLiveData == null) {
            mLocationLiveData = new MutableLiveData<>();
        }

        return mLocationLiveData;
    }

    MutableLiveData<Coordinates> getLocationCoordinates() {
        if (mLocationCoordinates == null) {
            mLocationCoordinates = new MutableLiveData<>();
        }

        return mLocationCoordinates;
    }

    public GeoViewModel() {
        client = new APIClient();
    }

    public void getCoordinates(String key) {
        disposable.add(
                client.getCurrentLocation(mLocationLiveData.getValue(), key)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Coordinates>() {
                            @Override
                            public void onSuccess(Coordinates coordinates) {
                                mLocationCoordinates.setValue(coordinates);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "Error throws: " + e.getLocalizedMessage());
                            }

                        })
        );
    }

    @Override
    public void onCleared() {
        disposable.clear();
        super.onCleared();
    }




}

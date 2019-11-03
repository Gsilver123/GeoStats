package com.example.geostats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText mSearchBar;
    private Button mSendButton;
    private Button mGeoInfoButton;

    private GeoViewModel mGeoViewModel;

    private Marker mMarker;
    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try{
            Thread.sleep(3000);
        }
        catch (InterruptedException ex){
            Log.d("thread", ex.getLocalizedMessage());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGeoViewModel = GeoViewModel.getInstance();

        setLocationObserver();

        mSearchBar = findViewById(R.id.search_bar_edit_text);
        mSendButton = findViewById(R.id.search_btn);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mSearchBar.getText().toString().isEmpty()) {
                    mGeoViewModel.getLocation().setValue(mSearchBar.getText().toString());
                    mGeoViewModel.getCoordinates(getString(R.string.api_key));
                }
                else {
                    Toast.makeText(
                            view.getContext(),
                            "Please input a location",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        mGeoInfoButton = findViewById(R.id.see_geo_info_btn);
        mGeoInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFragment(new GeoInfoFragment());
                mGeoViewModel.getGeoInfo(getString(R.string.esri_api_key));
                mGeoInfoButton.setVisibility(View.INVISIBLE);
            }
        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            transaction.replace(R.id.map, mapFragment);
        }

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setLocationObserver() {
        final Observer<Coordinates> observer = new Observer<Coordinates>() {
            @Override
            public void onChanged(Coordinates coordinates) {
                LatLng latLng = new LatLng(coordinates.getLatitude(), coordinates.getLongitude());
                mMarker.setPosition(latLng);
                mMarker.setVisible(true);

                if (mGoogleMap != null) {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng)
                            .build();

                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    mGeoInfoButton.setVisibility(View.VISIBLE);
                }
            }
        };

        mGeoViewModel.getLocationCoordinates().observe(this, observer);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mMarker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .visible(false));
        mMarker.setSnippet("Helllo");
    }

    private void startFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

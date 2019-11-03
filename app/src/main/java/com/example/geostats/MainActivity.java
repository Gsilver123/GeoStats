package com.example.geostats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchBar;
    private Button mSendButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBar = findViewById(R.id.search_bar_edit_text);
        mSendButton = findViewById(R.id.search_btn);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mSearchBar.getText().toString().isEmpty()) {
                    Coordinates coordinates = new Coordinates();
                    coordinates.setLocation(mSearchBar.getText().toString());

                    // REMOVE
                    //if (findViewById(R.id.geoInfoFragment) != null) {
                        Fragment infoFragment = new GeoInfoFragment();
                        infoFragment.setArguments(getIntent().getExtras());
                        getSupportFragmentManager().beginTransaction().add(R.id.geoInfoContainer, infoFragment).commit();
                    //}
                    //
                }
                else {
                    Toast.makeText(
                            view.getContext(),
                            "Please input a location",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}

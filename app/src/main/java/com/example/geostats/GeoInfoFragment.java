package com.example.geostats;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Set;

public class GeoInfoFragment extends Fragment {

    private GeoViewModel mGeoViewModel;
    private LinearLayout mScrollViewLinearLayout;

    private Button mBackButton;

    public GeoInfoFragment() {
        mGeoViewModel = GeoViewModel.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.geo_info_fragment, container, false);

        mScrollViewLinearLayout = view.findViewById(R.id.scroll_view_linear_layout);
        mBackButton = view.findViewById(R.id.back_btn);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStackImmediate();
                mGeoViewModel.getGeoInfoLiveData().getValue().clear();
            }
        });
        setGeoInfoObserver();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setGeoInfoObserver() {
        final Observer<HashMap<String, String>> observer = new Observer<HashMap<String, String>>() {
            @Override
            public void onChanged(HashMap<String, String> stringStringHashMap) {
                Object[] objects = stringStringHashMap.keySet().toArray();
                for (Object object : objects) {
                    addChildToLinearLayout(object.toString(), stringStringHashMap.get(object.toString()));
                }
            }
        };

        mGeoViewModel.getGeoInfoLiveData().observe(this, observer);
    }

    private void addChildToLinearLayout(String key, String value) {
//        ViewGroup.LayoutParams layoutParams =
//                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        TextView textViewKey = new TextView(getContext());
//        textViewKey.setText(key);
//        textViewKey.setLayoutParams(layoutParams);
//        textViewKey.setTextSize(22);
//
//        TextView textViewValue = new TextView(getContext());
//        textViewValue.setText(value);
//        textViewValue.setLayoutParams(layoutParams);
//        textViewValue.setGravity(Gravity.END);
//        textViewValue.setTextSize(22);
//
//        LinearLayout linearLayout = new LinearLayout(getContext());
//        LinearLayout.LayoutParams linearLayoutParams =
//                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        linearLayout.setLayoutParams(linearLayoutParams);
//        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//
//        linearLayout.addView(textViewKey);
//        linearLayout.addView(textViewValue);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dynamic_child, mScrollViewLinearLayout, false);
        ((TextView) view.findViewById(R.id.key_text_view)).setText(key);
        ((TextView) view.findViewById(R.id.value_text_view)).setText(value);
        mScrollViewLinearLayout.addView(view);
    }
}

package com.ricky.bluejackpharmacy.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ricky.bluejackpharmacy.R;

public class InfoActivity extends AppCompatActivity {

    private GoogleMap map;
    private final Double LAT = -6.20201;
    private final Double LONG = 106.78113;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gMaps);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap) {
                    map = googleMap;

                    LatLng blueJack = new LatLng(LAT, LONG);
                    map.addMarker(new MarkerOptions()
                            .position(blueJack)
                            .title("Bluejack Pharmacy"));
                    map.moveCamera(CameraUpdateFactory.newLatLng(blueJack));
                }
            });
        }
    }
}
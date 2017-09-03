package com.deputyshift.UI;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.deputyshift.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private LatLng mStartShiftLocation;
    private LatLng mEndShiftLocation ;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mStartShiftLocation = new LatLng(getIntent().getDoubleExtra("startLat", 0.0),
                getIntent().getDoubleExtra("startLong", 0.0));

        mEndShiftLocation = new LatLng(getIntent().getDoubleExtra("endLat", 0.0),
                getIntent().getDoubleExtra("endLong", 0.0));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map));

            mapFragment.getMapAsync(this);
           /* if (mMap != null) {
                addLines();
            }*/
        }
    }

    private void addLines() {
        mMap.addPolyline((new PolylineOptions())
                .add(mStartShiftLocation, mEndShiftLocation).width(5).color(Color.BLUE)
                .geodesic(true));
        mMap.addMarker(new MarkerOptions().position(mStartShiftLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_location)));
        mMap.addMarker(new MarkerOptions().position(mEndShiftLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_location)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mEndShiftLocation,
                15));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        addLines();
    }
}

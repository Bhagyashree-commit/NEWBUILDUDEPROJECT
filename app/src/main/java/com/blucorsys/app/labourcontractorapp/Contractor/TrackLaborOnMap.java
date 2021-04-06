package com.blucorsys.app.labourcontractorapp.Contractor;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.blucorsys.app.labourcontractorapp.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import static com.blucorsys.app.labourcontractorapp.Contractor.TrackingActivity.arrayList;

public class TrackLaborOnMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLngBounds.Builder builder;
    CameraUpdate cu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_labor_on_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mSetUpMap();

        // Add a marker in Sydney and move the camera
       // LatLng sydney = new LatLng(-34, 151);
      //  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
      //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void mSetUpMap() {
        /**clear the map before redraw to them*/
        mMap.clear();
        /**Create dummy Markers List*/
        List<Marker> markersList = new ArrayList<Marker>();

        Log.d("Rashmiiiiii",""+arrayList);

        for(int i = 0 ; i < arrayList.size() ; i++) {
            Marker location = mMap.addMarker(new MarkerOptions().position(new LatLng(
                    Double.parseDouble(arrayList.get(i).get("lat")), Double.parseDouble(arrayList.get(i).get("lng")))).title(arrayList.get(i).get("name")));
            markersList.add(location);

        }


        builder = new LatLngBounds.Builder();
        for (Marker m : markersList) {
            builder.include(m.getPosition());
        }
        /**initialize the padding for map boundary*/
        int padding = 5;
        /**create the bounds from latlngBuilder to set into map camera*/
        LatLngBounds bounds = builder.build();
        /**create the camera with bounds and padding to set into map*/
        cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        /**call the map call back to know map is loaded or not*/
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                /**set animated zoom camera into map*/
                mMap.animateCamera(cu);
                //mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            }
        });
    }

}
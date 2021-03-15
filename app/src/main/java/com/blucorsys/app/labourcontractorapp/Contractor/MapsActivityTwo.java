package com.blucorsys.app.labourcontractorapp.Contractor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.blucorsys.app.ServerCall.Preferences;
import com.blucorsys.app.labourcontractorapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MapsActivityTwo extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int REQUEST_LOCATION = 1;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private GoogleMap mMap;
    MarkerOptions markerOptions;
    String address;
    LatLng latLng;
    double l1;
    double l2;
    Location l3;
    double latitude,longitude;
    Preferences sessionManagerContractor;
    FusedLocationProviderClient fusedLocationProviderClient;
    public static String newloc;
    LocationManager locmanager;
    int markercount=0;

    Marker m1;
    Marker m2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_two);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        sessionManagerContractor = new Preferences(this);
        GPSTracker mGPS = new GPSTracker(this);

        l1= mGPS.getLatitude();
        l2 =mGPS.getLongitude();
        // l3=mGPS.getLocation();
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mLastLocation = location;
                    Toast.makeText(getApplicationContext(), mLastLocation.getLatitude() + "" + mLastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapsActivityTwo.this);
                }
            }
        });




    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mMap.clear();
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
//Showing Current Location Marker on Map
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        Log.e("mrakerrrr", "" + latLng);
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();
        if (null != mLastLocation && null != providerList && providerList.size() > 0) {
            longitude = mLastLocation.getLongitude();
            latitude = mLastLocation.getLatitude();
            Log.e("mrakerrrr1232", "" + longitude);
            Geocoder geocoder = new Geocoder(getApplicationContext(),
                    Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latitude,
                        longitude, 1);
                if (null != listAddresses && listAddresses.size() > 0) {
                    String state = listAddresses.get(0).getAdminArea();
                    String country = listAddresses.get(0).getCountryName();
                    String subLocality = listAddresses.get(0).getSubLocality();
                    markerOptions.title("" + latLng + "," + subLocality + "," + state
                            + "," + country);
                    Log.e("mrakerrrrfdyyyyyyyy", "" + state);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//
//        // mCurrLocationMarker.remove();
//        mMap.clear();
//
//        MarkerOptions mp = new MarkerOptions();
//
//        mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
//
//        mp.title("my position");
//
//        mMap.addMarker(mp);
//
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                new LatLng(location.getLatitude(), location.getLongitude()), 10));

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                        mLocationRequest, (com.google.android.gms.location.LocationListener) this);
            }
            catch (Exception e){}
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
//        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        if(sessionManagerContractor.get("latitude").isEmpty()){
            Log.e("testtttt1","first");
            // latLng = new LatLng(location.getLatitude(), location.getLongitude());
            // LatLng lt = new LatLng(18.5903995,73.7482045);
//            latLng test=new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
//            mMap.addMarker(new MarkerOptions().position(test).draggable(true).title(address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(test, 10));


//            LatLng latLng = new LatLng(l3, mLastLocation.getLongitude());
            GPSTracker mgps=new GPSTracker(this);
//             //Location newloc= mgps.getLocation();
//             newloc = String.valueOf(mgps.getLatitude());
//
            LatLng lt = new LatLng(mgps.getLatitude(),mgps.getLongitude());
//            Log.e("newwwww loccc",""+lt);

            Log.e("MARKER ",""+markercount);

            // m1.setVisible(true);
            m1=createMarker(mgps.getLatitude(),mgps.getLongitude(),address);
            // mMap.addMarker(m1);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lt, 10));

        }
        else {
            Log.e("testtttt2","Second");
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(MapsActivityTwo.this, Locale.getDefault());
            try {
                String sPlace;
                addresses = geocoder.getFromLocation(Double.parseDouble(sessionManagerContractor.get("latitude")), Double.parseDouble(sessionManagerContractor.get("longitude")), 1);
                String address1 = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getAddressLine(1);
                String country = addresses.get(0).getAddressLine(2);

                String[] splitAddress = address1.split(",");
                sPlace = splitAddress[0] + "\n";
                if(city != null && !city.isEmpty()) {
                    String[] splitCity = city.split(",");
                    sPlace += splitCity[0];
                }
                Log.e("new addreesss",""+address1);

                LatLng India = new LatLng(Double.parseDouble(sessionManagerContractor.get("latitude")), Double.parseDouble(sessionManagerContractor.get("longitude")));
                m1=createMarker(Double.parseDouble(sessionManagerContractor.get("latitude")), Double.parseDouble(sessionManagerContractor.get("longitude")),address);

//                    mMap.addMarker(new MarkerOptions().position(India).draggable(true).title(address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(India, 10));
                // m1.remove();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);


        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
            }
        });



        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                try {
                  mCurrLocationMarker.remove();
                    mCurrLocationMarker.setPosition(latLng);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Log.e("start", "");
                m1.remove();
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Location location = null;
                mLastLocation = location;
                LatLng latLng = marker.getPosition();

                Geocoder geocoder = new Geocoder(getApplicationContext(),
                        Locale.getDefault());
                try {
                    List<Address> listAddresses = Collections.singletonList(geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0));
                    if (null != listAddresses && listAddresses.size() > 0) {

                        String state = listAddresses.get(0).getAdminArea();
                        String country = listAddresses.get(0).getCountryName();
                        String subLocality = listAddresses.get(0).getSubLocality();
                        String postalCode = listAddresses.get(0).getPostalCode();
                        String knownName = listAddresses.get(0).getFeatureName();
                        String newadd = listAddresses.get(0).getAddressLine(0);
                        address = postalCode;
                        markerOptions.title("" + latLng + "," + subLocality + "," + state
                                + "," + country);
                        markercount=1;
                        Log.e("ADDDDDD", "" + address);
                        Log.e("pos", "" + postalCode);
                        Log.e("add", "" + knownName);

//                        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true).title(address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                        sessionManagerContractor.set("add", "" + newadd);
                        sessionManagerContractor.set("latitude", "" + latLng.latitude);
                        sessionManagerContractor.set("longitude", "" + latLng.longitude);
                        sessionManagerContractor.set("pos", "" + postalCode);
                        sessionManagerContractor.commit();

                        Log.e("session", sessionManagerContractor.get("add"));
                        Log.e("session", sessionManagerContractor.get("pos"));


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                Log.e("Latitude", "kjsdfhkdf" + latLng);

                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                // mMap.addMarker(markerOptions);
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MapsActivityTwo.this, PostJob.class);
        startActivity(intent);
        //uper.onBackPressed();
    }


    protected Marker createMarker(double latitude, double longitude, String title) {

        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title).draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker()));
    }
    @Override
    public void onStart(){
        super.onStart();
        if(this.mGoogleApiClient != null){
            this.mGoogleApiClient.connect();
        }
        super.onStart();
    }
    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }
}
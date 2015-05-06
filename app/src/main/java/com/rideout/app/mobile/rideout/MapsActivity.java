package com.rideout.app.mobile.rideout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.rideout.app.mobile.rideout.createARide.GooglePlacesAutocompleteActivity;

import java.io.IOException;
import java.util.Locale;

public class MapsActivity extends FragmentActivity  implements View.OnClickListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Bundle extras = getIntent().getExtras();
        int locType = 0;
        if (extras != null) {
            locType = extras.getInt("LOCATION_TYPE");
            Log.v("locType", locType+"");
        }

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status

        setUpMapIfNeeded();

        Button pickLocation = (Button) findViewById(R.id.pickLocation);

        if(locType == 3){
            pickLocation.setText("Pick Up Location");
        } else if(locType == 4){
            pickLocation.setText("Drop Off Location");
        }
        Drawable search = new IconDrawable(this, Iconify.IconValue.fa_search).colorRes(R.color.gray).actionBarSize();
        pickLocation.setCompoundDrawables(search, null, null, null);
        pickLocation.setCompoundDrawablePadding(5);
        pickLocation.setOnClickListener(this);

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pickLocation:
                Intent searchActivity = new Intent(MapsActivity.this, GooglePlacesAutocompleteActivity.class);
                startActivityForResult(searchActivity, 0);
                break;
            case R.id.submit:
                Intent returnToMap = new Intent();
                returnToMap.putExtra("ADDRESS", m.getPosition());
                setResult(RESULT_OK, returnToMap);
                finish();

                break;
        }

    }


    // Listen for results.
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // See which child activity is calling us back.
        Log.d("here", requestCode + "->RequestCode, " + resultCode + "->resultCode");
        if (requestCode == 0) {
            if(resultCode == RESULT_OK){
                String result=data.getStringExtra("ADDRESS");
                placeMarker(result);
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
    private Marker m;
    //TODO figure out how to differentiate between pickup and dropoff
    private void placeMarker(String result) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        Address address = null;
        try {
             address = geocoder.getFromLocationName(result, 1).get(0);
            Log.v("HELLO", address.getLatitude() + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(address!=null){
            LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
            m = mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(), 15.0f));
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
         GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                Marker mMarker = mMap.addMarker(new MarkerOptions().position(loc));
                if(mMap != null){
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                }
            }
        };
    }


}

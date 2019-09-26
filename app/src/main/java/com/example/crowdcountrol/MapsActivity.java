package com.example.crowdcountrol;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;

    GoogleApiClient client;
    Location lastLoc;
    LocationRequest reqLoc;

    private Button btn_logout;
    private Boolean isLoggingOut = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Logout
        btn_logout = (Button) findViewById(R.id.logout_btn);
        btn_logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                isLoggingOut = true;

                disconnectPass();

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        });
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

        /* Add a marker in Sydney and move the camera
        LatLng manila = new LatLng(14.599313, 120.981833);
        mMap.addMarker(new MarkerOptions().position(manila).title("Target marked: Manila"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(manila));

         */

        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    protected synchronized void buildGoogleApiClient(){
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        client.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLoc = location;

        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(28));

        mMap.addMarker(new MarkerOptions().position(latlng).title("You are here!")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_star)));
    }

    /* @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    } */

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        reqLoc = new LocationRequest();
        reqLoc.setInterval(500);
        reqLoc.setFastestInterval(500);
        reqLoc.setPriority(reqLoc.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(client, reqLoc, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void disconnectPass(){
        LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("passengerWaiting");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected void onStop(){
        super.onStop();

        if(!isLoggingOut){
            disconnectPass();
        }
    }
}

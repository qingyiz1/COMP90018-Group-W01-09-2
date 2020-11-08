package com.example.comp90018.Activity.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.comp90018.Activity.Shake.UserViewActivity;
import com.example.comp90018.R;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.view.View;
import android.widget.Button;
import android.location.Location;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {
    private GoogleMap mMap;
    protected static final String TAG = "MAP";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Button follow;
    private Button near;
    private LatLng myloc;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("location");
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private List<Marker> allMarkers = new ArrayList<Marker>();
    private final double EARTH_RADIUS = 6378137.0;
    private String name;
    // String:username LatLng: 经纬度
    Map<String,LatLng> locdata;
    // String:username
    List<String> followlist;
    private Location lastKnownLocation;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        follow = (Button)findViewById(R.id.follow);
        near = (Button)findViewById(R.id.near);
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFollow();
            }
        });
        near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNear();
            }
        });
    }

    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        setName();
        mMap.setOnMarkerClickListener(this);
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        if(lastKnownLocation!=null){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myloc));
        }
    }
    public void setName(){
        DocumentReference docRef = db.collection("users").document(mAuth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        if(document.getData().get("nickName") != null){
                            name = document.getData().get("nickName").toString();
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    void clearMarker(){
        for(Marker marker : allMarkers){
            marker.remove();
        }
        allMarkers.clear();
    }

    // 需后端改写 更新当前位置
    void update(DatabaseReference ref, String username, LatLng location){
        ref.child("name").child(username).child("location").setValue(location);
    }


    @Override
    public boolean onMarkerClick(final Marker marker){
        Intent intent = new Intent(MapActivity.this, UserViewActivity.class);
        startActivity(intent);
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    public void showFollow(){
        clearMarker();
        for (Map.Entry<String,LatLng> a: locdata.entrySet()){
            if(followlist.contains(a.getKey())){
                LatLng currloc = a.getValue();
                String username = a.getKey();
                Marker marker = this.mMap.addMarker(new MarkerOptions().position(currloc).title(username));
                allMarkers.add(marker);
            }
        }
    }
    public void showNear(){
        clearMarker();
        for (Map.Entry<String,LatLng> a: locdata.entrySet()){
            LatLng currloc = a.getValue();
            String username = a.getKey();
            if(distance(currloc,this.myloc)<5){
                Marker marker = this.mMap.addMarker(new MarkerOptions().position(currloc).title(username));
                allMarkers.add(marker);
            }
        }
    }

    public double distance(LatLng a, LatLng b){
        double lata = a.latitude * Math.PI / 180;
        double latb = b.latitude * Math.PI / 180;
        double lona = a.longitude * Math.PI / 180;
        double lonb = b.longitude * Math.PI / 180;
        double lat = lata - latb;
        double lon = lona - lonb;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(lat/2), 2)
                + Math.cos(lata) * Math.cos(latb)
                * Math.pow(Math.sin(lon/2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(lastKnownLocation == null){
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }else {
            LatLng currloc = new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
            this.myloc = currloc;
            update(myRef,name,currloc);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null){
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }else{
            lastKnownLocation = location;
            LatLng currloc = new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
            this.myloc = currloc;
            update(myRef,name,currloc);
        }
    }
}
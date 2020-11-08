package com.example.comp90018.Activity.Map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.comp90018.Activity.Shake.UserViewActivity;
import com.example.comp90018.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.comp90018.DataModel.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, OnMarkerClickListener{
    UserModel user = new UserModel();
    private GoogleMap mMap;
    private Button follow;
    private Button near;
    private LatLng myloc;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("location");
    protected static final String TAG = "Map";
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private List<Marker> allMarkers = new ArrayList<Marker>();
    private final double EARTH_RADIUS = 6378137.0;
    private String name;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String uid;
    // String:username LatLng: 经纬度
    Map<String,LatLng> locdata;
    // String:username
    List<String> followlist;
    private Location lastKnownLocation;
    private CameraPosition cameraPosition;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // 记录下自己的位置

        if (savedInstanceState != null) {
            // 把位置和用户视角记录下来
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        // 两个button 待写
        follow = (Button)findViewById(R.id.follow);
        near = (Button)findViewById(R.id.near);
        // 待写
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFollow();
            }
        });
        // 待写
        near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNear();
            }
        });
//        mapFragment.getMapAsync(this);
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

    // 标记用户当前位置和方向
    void clearMarker(){
        for(Marker marker : allMarkers){
            marker.remove();
        }
        allMarkers.clear();
    }

    void setUid(){
        this.uid = mAuth.getUid();
    }
    // 需后端改写 更新当前位置
    void update(DatabaseReference ref, String username, LatLng location){
        ref.child("name").child(username).child("location").setValue(location);
    }

    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        setName();
        setUid();
        LatLng currloc = new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
        this.myloc = currloc;
        // 把name,currloc存入数据库
        update(myRef,name,currloc);
        mMap.setOnMarkerClickListener(this);
        //maplist.put(R.id,currloc)
        //mMap.addMarker(new MarkerOptions().position(currloc));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(currloc));
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
                Marker marker = this.mMap.addMarker(new MarkerOptions().position(currloc));
                allMarkers.add(marker);
            }
        }
        // if(myfollow.contains(id)){
        //LatLng currloc = maplist.get(id);
        //Marker marker = this.mMap.addMarker(new MarkerOptions().position(currloc));
        //allMarkers.add(marker);
        //}
        //}
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
        //for(Integer id: maplist){
        //    LatLng currloc = maplist.get(id);
        //    if(distance(currloc,this.myloc) < 5){
        //        Marker marker = this.mMap.addMarker(new MarkerOptions().position(currloc).title(username)));
        //        allMarkers.add(marker);
        //    }
        //}
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
}
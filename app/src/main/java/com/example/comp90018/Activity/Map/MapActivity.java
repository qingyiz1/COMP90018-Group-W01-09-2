package com.example.comp90018.Activity.Map;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    private Button follow;
    private Button near;
    private LatLng myloc;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private List<Marker> allMarkers = new ArrayList<Marker>();
    private final double EARTH_RADIUS = 6378137.0;
    // 后端位置数据（可用map或者array存，如果用array，要保证arr[i]是id为i的client的地图信息）
    // hashmap<Integer,Laglng> maplist?
    // follow的人的list myfollow List<Integer>
    // 假设为map
    // 当前位置
    private Location lastKnownLocation;
    // 当前视角
    private CameraPosition cameraPosition;
    @Override
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
        mapFragment.getMapAsync(this);
    }

    // 标记用户当前位置和方向
    void clearMarker(){
        for(Marker marker : allMarkers){
            marker.remove();
        }
        allMarkers.clear();
    }
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        LatLng currloc = new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
        this.myloc = currloc;
        //maplist.put(R.id,currloc)
        //mMap.addMarker(new MarkerOptions().position(currloc));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(currloc));
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
        //for (Integer id: maplist){
        // if(myfollow.contains(id)){
        //LatLng currloc = maplist.get(id);
        //Marker marker = this.mMap.addMarker(new MarkerOptions().position(currloc));
        //allMarkers.add(marker);
        //}
        //}
    }
    public void showNear(){
        clearMarker();
        //for(Integer id: maplist){
        //    LatLng currloc = maplist.get(id);
        //    if(distance(currloc,this.myloc) < 5){
        //        Marker marker = this.mMap.addMarker(new MarkerOptions().position(currloc));
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
package com.example.comp90018.Activity.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comp90018.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    protected static final String TAG = "MAP";

    private Button shop;
    private Button square;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private List<Marker> allMarkers = new ArrayList<Marker>();
    private final double EARTH_RADIUS = 6378137.0;
    Map<String,LatLng> shopdata = new HashMap<>();
    Map<String,LatLng> squaredata = new HashMap<>();
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        shop = (Button)findViewById(R.id.shop);
        square = (Button)findViewById(R.id.square);
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showshop();
            }
        });
        square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showsquare();
            }
        });
    }

    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
//        setName();
        LatLng myloc = new LatLng(-37.798968, 144.964473);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myloc));
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
        getshop();
        getsquare();
    }
    public void getshop(){
        shopdata.put("Upmarket Pets",new LatLng(-37.807348, 144.958702));
        shopdata.put("Supaw Pet Bakery",new LatLng(-37.810860, 144.951312));
        shopdata.put("Pompous Paws",new LatLng(-37.806045, 144.961526));
        shopdata.put("Upmarket Pets",new LatLng(-37.807673, 144.958350));
        shopdata.put("Classy Tails",new LatLng(-37.817369, 144.962642));
        shopdata.put("My Pet Warehouse South Melbourne",new LatLng(-37.828344, 144.961886));
        shopdata.put("The Pet Grocer",new LatLng(-37.830487, 144.960311));
        shopdata.put("South Melbourne Market Pet Shop",new LatLng(-37.831912, 144.956566));
        shopdata.put("PETstock South Melbourne",new LatLng(-37.832116, 144.955021));
        shopdata.put("Best Friends Pets Richmond",new LatLng(-37.810222, 145.013924));
        shopdata.put("Laundrymutt",new LatLng(-37.816327, 145.005264));
        shopdata.put("Petbarn Richmond",new LatLng(-37.828938, 145.004406));
        shopdata.put("New Life Aquarium & Pet Abbotsford",new LatLng(-37.805478, 144.997368));
        shopdata.put("Doghouse Australia",new LatLng(-37.794627, 145.007667));
        shopdata.put("PETstock Clifton Hill",new LatLng(-37.779432, 144.985351));
        shopdata.put("Petbarn Brunswick",new LatLng(-37.772070, 144.967477));
        shopdata.put("Pet Art",new LatLng(-37.781025, 144.980352));
    }
    public void getsquare(){
        squaredata.put("University Square",new LatLng(-37.801242, 144.960349));
        squaredata.put("Curtain Square",new LatLng(-37.786698, 144.978816));
        squaredata.put("Lincoln Square",new LatLng(-37.802306, 144.962884));
        squaredata.put("Argyle Square",new LatLng(-37.802889, 144.965800));
        squaredata.put("Darling Square",new LatLng(-37.812835, 144.989402));
        squaredata.put("City Square",new LatLng(-37.815854, 144.966952));
        squaredata.put("Fed Square",new LatLng(-37.817972, 144.969081));

        squaredata.put("Macarthur Square",new LatLng(-37.798327, 144.971541));
        squaredata.put("Golden Square Bicentennial Park",new LatLng(-37.828529, 145.010047));
        squaredata.put("Garden Square",new LatLng(-37.820403, 144.978786));
    }
    void clearMarker(){
        for(Marker marker : allMarkers){
            marker.remove();
        }
        allMarkers.clear();
    }


    public void showshop(){
        clearMarker();
        for (Map.Entry<String,LatLng> a: shopdata.entrySet()){
            LatLng currloc = a.getValue();
            String shopname = a.getKey();
            Marker marker = this.mMap.addMarker(new MarkerOptions().position(currloc).title(shopname));
            allMarkers.add(marker);
        }
    }
    public void showsquare(){
        clearMarker();
        for (Map.Entry<String,LatLng> a: squaredata.entrySet()){
            LatLng currloc = a.getValue();
            String squarename = a.getKey();
            Marker marker = this.mMap.addMarker(new MarkerOptions().position(currloc).title(squarename));
            allMarkers.add(marker);
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





}
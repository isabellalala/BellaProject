package com.example.stefa.mycustommaps;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.content.Context;
import android.location.Criteria;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private String provider;
    private Location location;
    private DatabaseReference firebaseDB;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE2 = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE3 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.firebaseDB = FirebaseDatabase.getInstance().getReference();

        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);
        } catch (SecurityException e) {
            Toast.makeText(this, "Exception onCreate", Toast.LENGTH_SHORT).show();
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Seed newS = new Seed("Bella's hus", 55.10, 12.22);
        //firebaseDB.child(newS.title).setValue(newS);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.startmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            // Handle item selection
            switch (item.getItemId()) {
                case R.id.addSeed:
                    location = locationManager.getLastKnownLocation(provider);
                    Intent intent = new Intent(this, AddSeedActivity.class);
                    if (location != null) {
                        intent.putExtra("latitude", String.valueOf(location.getLatitude()));
                        intent.putExtra("longitude", String.valueOf(location.getLongitude()));
                    }
                    startActivity(intent);
                    return true;
            }
        } catch (SecurityException e) {
            Toast.makeText(this, "Exception onMapReady" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
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
        boolean fullAccess = requestPermissions();

        try {
            this.firebaseDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.e("Count " ,""+dataSnapshot.getChildrenCount());

                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        Seed seed = child.getValue(Seed.class);
                        Log.e("Child key:", ""+seed.title);

                        MarkerOptions marker = new MarkerOptions().position(new LatLng(seed.latitude, seed.longitude)).title(seed.title);

                        marker.icon((BitmapDescriptorFactory.fromResource(R.mipmap.seedicon)));

                        mMap.addMarker(marker);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //System.out.println("The read failed: "+databaseError.getCode() +" "+databaseError.getMessage());
                }
            });

            mMap.setMyLocationEnabled(true);
            location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
            }

            final MapsActivity that = this;

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    Intent intent = new Intent(that, OpenSeed.class);
                    intent.putExtra("seed", marker.getTitle());
                    startActivity(intent);
                    return true;

//                    if (inRange(marker.getPosition().latitude, marker.getPosition().longitude)) {
//                        Intent intent = new Intent(that, OpenSeed.class);
//                        intent.putExtra("seed", marker.getTitle());
//                        startActivity(intent);
//                        return true;
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Out Of Range", Toast.LENGTH_SHORT).show();
//                    }
//                    return false;
                }
            });
        } catch (SecurityException e) {
            Toast.makeText(this, "Exception onMapReady"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371.0; // miles (or 6371.0 kilometers) - Fungerer ikke på Mars
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist*1000;
    }

    public boolean inRange(double lat, double lng)
    {
        try {
            location = locationManager.getLastKnownLocation(provider);

            if (location == null) {
                Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show();
                return false;
            }
            double range = distFrom(location.getLatitude(), location.getLongitude(), lat, lng);

            if (range <= 100) {
                return true;
            }
            return false;
        } catch (SecurityException e) {
            Toast.makeText(this, "Exception InRange", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean requestPermissions()
    {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return false;
        }

        if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE2);
            return false;
        }

        if (checkSelfPermission(android.Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {android.Manifest.permission.VIBRATE}, LOCATION_PERMISSION_REQUEST_CODE3);
            return false;
        }

        return true;
    }
}

package com.example.get_current_map_here;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.mapview.MapError;
import com.here.sdk.mapview.MapScene;
import com.here.sdk.mapview.MapScheme;
import com.here.sdk.mapview.MapView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {
    static MainActivity instance;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    private MapView mapView;
    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        loadMap();
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Log.e("Log", "onPermissionGranted");
                        updateLocation();
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                       Log.e("Log", "onPermissionDenied");
                        Toast.makeText(getApplicationContext(),"onPermissionDenied !!!" , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

    }
    private void loadMap()
    {
        Log.e("Log", "VÀO LOA MAP");
        GeoCoordinates geoCoordinates = new GeoCoordinates(10.46171 , 105.64354);
//        GeoCoordinates geoCoordinates = new GeoCoordinates(latitude , longtitude);
        mapView.getMapScene().loadScene(MapScheme.NORMAL_DAY, new MapScene.LoadSceneCallback() {
            @Override
            public void onLoadScene(@Nullable MapError mapError) {
                if (mapError == null) {
                    double distanceInMeters = 1000;
                    mapView.getCamera().lookAt(
                            //        new GeoCoordinates(10.46384,  105.6441), distanceInMeters);
                            geoCoordinates,distanceInMeters);
                } else {
                    Log.d("Log", "Loading map failed: mapError: " + mapError.name());
                }
            }
        });
    }



    private void updateLocation() {
        buildLocationRequest();
        Log.e("Log" , "VÀO UPDATE LOCATION");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
    }
    private PendingIntent getPendingIntent()
    {
        Intent intent = new Intent(this , MyLocationService.class);
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE);

        return PendingIntent.getBroadcast(this , 0 ,intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setSmallestDisplacement(0f);
    }
    public void updateTextView(String value)
    {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("Log", "Vào UPDATE TEXT VIEW");
               // txtShow.setText(value);
              //  loadMap();
                Toast.makeText(getApplicationContext(), "" + value , Toast.LENGTH_SHORT).show();
            }
        });
    }
}
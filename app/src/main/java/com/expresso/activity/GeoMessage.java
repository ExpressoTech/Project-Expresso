package com.expresso.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.TextView;

import android.util.Log;
import android.widget.Toast;

/**
 * Created by ArunLodhi on 31-05-2015.
 */
public class GeoMessage extends Activity implements LocationListener{
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView tv_geom_info;
    String lat;
    String provider;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geomessage);
        tv_geom_info = (TextView) findViewById(R.id.textview1);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }
    @Override
    public void onLocationChanged(Location location) {
        tv_geom_info.setText("Latitude:" + location.getLatitude() + "\nLongitude:" + location.getLongitude() + "\nAltitude:" + location.getAltitude() + "\nAccuracy:" + location.getAccuracy());
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
        if(!locationManager.isProviderEnabled(provider)) {
            tv_geom_info.setText("Please Enable GPS");
            showGPSDisabledAlertToUser();
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
        if(locationManager.isProviderEnabled(provider)){
            Toast.makeText(this, "GPS on", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Please Enable GPS")
                .setCancelable(false)
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}

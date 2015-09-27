package com.expresso.activity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.expresso.activity.R;
import com.expresso.adapter.AppController;
import com.expresso.util.Links;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;import java.lang.Double;import java.lang.Exception;import java.lang.Override;import java.lang.String;

public class GeoMsgMap extends AppCompatActivity {

    private GoogleMap googleMap;
    private String url;
    private String jsonResponse;
    private Double lat_old;
    private Double lng_old;
    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gmsg_map);

        b = getIntent().getExtras();
        initialization();
    }

    protected void initialization() {
        initializeMap();
        addMarkers();
    }

    private void addMarkers() {
        googleMap.setMyLocationEnabled(true);
        Location myLocation = googleMap.getMyLocation();

        if (b!=null) {
            lat_old = b.getDouble("lat");
            lng_old = b.getDouble("lng");
            LatLng latLng = new LatLng(lat_old, lng_old);
            //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.circle);
            //MarkerOptions marker = new MarkerOptions().position(latLng).icon(icon).anchor(0.5f, 0.5f);
            //CircleOptions circleOptions = new CircleOptions().center(latLng).radius(500).fillColor(getResources().getColor(R.color.dark_blue)).strokeWidth(0);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
            //googleMap.addMarker(marker);
            //googleMap.addCircle(circleOptions);
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                updateMap(location);
            }
        });
    }

    private void updateMap(Location newLocation) {
        url = new Uri.Builder()
                .scheme(Links.schemeType)
                .authority(Links.hostURL)
                .appendPath(Links.fetchURL)
                .appendQueryParameter(Links.fetchLAT, String.valueOf(newLocation.getLatitude()))
                .appendQueryParameter(Links.fetchLNG, String.valueOf(newLocation.getLongitude()))
                .build().toString();

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        googleMap.clear();
                        try {
                            jsonResponse = "";
                            for (int i=0; i<response.length(); i++) {
                                JSONObject message = (JSONObject) response.get(i);
                                Double lat = message.getDouble("lat");
                                Double lng = message.getDouble("lng");
                                String msg = message.getString("msg_text");
                                int type = message.getInt("type");
                                if (type == 0) {
                                    googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(msg));
                                            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_insert_comment_white_48dp)));
                                }
                                /*
                                else if (type == 1) {
                                    googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng))
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_image_white_48dp)));
                                }
                                else if (type == 2) {
                                    googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng))
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_videocam_white_48dp)));
                                }
                                */
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Map", "Error: " + error.getMessage());
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        AppController.getInstance().addToRequestQueue(request);
    }

    private void initializeMap() {
        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.map)).getMap();
                // check if map is created successfully or not
                if (googleMap == null) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gmsg_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeMap();
    }
}

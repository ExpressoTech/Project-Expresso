package com.expresso.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.expresso.adapter.LocationAdapter;
import com.expresso.database.DatabaseHelper;
import com.expresso.model.LocationModel;
import com.expresso.service.LocationService;
import com.expresso.utils.Constant;
import com.expresso.utils.GPSTracker;
import com.expresso.utils.JSONParser;
import com.expresso.utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FeedPageLocation extends Activity implements View.OnClickListener {
    private AutoCompleteTextView et_feedLocation;
    private Button btn_Next;
    private static final String API_KEY = "AIzaSyDp5u0Z3h9abid8puEy-sqdrFLZ3fONHN8";
    private LocationService appLocationService;
    private boolean isGPSEnablesManually=false;
    private ArrayList<LocationModel> result;
    private LocationAdapter adapter;
    private ListView lv_locations;
    ProgressDialog pd;
    private int selectedPosition=-1;
    private DatabaseHelper db;
    private static final int ENABLE_GPS = 100;
    private Location mainLocation;
    // GPSTracker class
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_page_location);
        getWidgetReferences();
        bindWidgetEvents();
        initialization();

        lv_locations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocationModel item=adapter.getItem(position);
                et_feedLocation.setText(item.getLocationName());
                selectedPosition=position;
            }
});
        }
    private void getWidgetReferences() {
        et_feedLocation= (AutoCompleteTextView) findViewById(R.id.et_feedLocation);
        lv_locations = (ListView) findViewById(R.id.lv_locations);
        btn_Next= (Button) findViewById(R.id.btn_Next);
    }

    private void bindWidgetEvents() {
        btn_Next.setOnClickListener(this);
    }

    private void initialization() {
        db=new DatabaseHelper(getApplicationContext());
        pd=new ProgressDialog(this);
        fetchLocation();
    }

    private void fetchLocation() {
        // create class object
        gps = new GPSTracker(FeedPageLocation.this);
        // check if GPS enabled
        if(gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            mainLocation=gps.getLocation();
            fetchNearbyLocation(latitude, longitude);
        }else{
            gps.showSettingsAlert();
        }
    }


    private void fetchNearbyLocation(double latitude, double longitude) {
        new fetchNearbyLocationAsynTask().execute(latitude+"",longitude+"");
    }

    @Override
    public void onClick(View v) {
        if(v==btn_Next)
        {
            if(!et_feedLocation.getText().toString().isEmpty())
            {
                    if(selectedPosition!=-1)
                    {
                        Constant.saveLocation(getApplicationContext(),et_feedLocation.getText().toString());
                        LocationModel item=adapter.getItem(selectedPosition);
                        long feedID=db.createFeed(item);
                        Constant.setFeedID(getApplicationContext(),feedID);
                        Intent i=new Intent(this,FeedCreationPage.class);
                        startActivity(i);
                    }
            }
        }
    }

    private class fetchNearbyLocationAsynTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jsonParser=new JSONParser();
            // Building Parameters
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            String url= Constant.LOCATIONAPI+"location="+params[0]+","+params[1]+Constant.LOCATIONURL2;
            JSONObject json = jsonParser.getJSONFromUrl(url, parameters);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            Utils.closeProgress();
            try {
            if(!json.getString("status").equalsIgnoreCase("ZERO_RESULTS"))
            {
                JSONArray resultArray=json.getJSONArray("results");
                for(int i=0;i<resultArray.length();i++)
                {
                    JSONObject data=resultArray.getJSONObject(i);
                    String geometry=data.getJSONObject("geometry").getJSONObject("location").getString("lat")+","+data.getJSONObject("geometry").getJSONObject("location").getString("lng");
                    Location fetchedLocation=new Location(data.getString("vicinity"));
                    fetchedLocation.setLatitude(Float.parseFloat(data.getJSONObject("geometry").getJSONObject("location").getString("lat")));
                    fetchedLocation.setLongitude(Float.parseFloat(data.getJSONObject("geometry").getJSONObject("location").getString("lng")));
                    Float distance= fetchedLocation.distanceTo(mainLocation);
                    LocationModel item=new LocationModel(data.getString("name"),data.getString("vicinity"),geometry,distance);
                    result.add(item);

                }
                Collections.sort(result, new CustomComparator());
                adapter=new LocationAdapter(FeedPageLocation.this,result);
                lv_locations.setAdapter(adapter);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public class CustomComparator implements Comparator<LocationModel> {
            @Override
            public int compare(LocationModel o1, LocationModel o2) {
                return o1.getDistance().compareTo(o2.getDistance());
            }
        }

        @Override
        protected void onPreExecute() {
            result=new ArrayList<LocationModel>();
            Utils.showProgress(FeedPageLocation.this,getResources().getString(R.string.pleasewait));
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}

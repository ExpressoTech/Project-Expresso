package com.expresso.fragment.UserProfile;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.expresso.activity.R;
import com.expresso.activity.UserProfileActivities.UserProfileActivity;
import com.expresso.utils.Constant;
import com.expresso.utils.JSONParser;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by rohit on 15/6/15.
 */
public class MapFragment extends Fragment {

    private ProgressDialog pd;
    private Marker marker;
    private String f_id= UserProfileActivity.f_user_id;
    private String u_id= UserProfileActivity.USER_ID;
    private JSONArray resultArray = null;
    private JSONArray FeedAttachment=null;
    private MapView mMapView;
    private GoogleMap googleMap;
    private LatLng currentLocation;
    private ArrayList<LatLng> locations_markers = new ArrayList();
    private ArrayList<String> place_name = new ArrayList<String>();
    private ArrayList<String> image_array = new ArrayList<String>();
//    Object[]markers= MainActivity.place_name.toArray();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        // inflat and return the layout
        View v = inflater.inflate(R.layout.map_layout, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.map1);
        new MapTask().execute();
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try
        {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        return v;
    }







    private class MapTask extends AsyncTask<String, Void, JSONObject>
    {

        @Override
        protected JSONObject doInBackground(String... params)
        {
            JSONParser jsonParser=new JSONParser();
            // Building Parameters
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();

            if(f_id.equalsIgnoreCase(u_id))
            {parameters.add(new BasicNameValuePair(Constant.USERID, u_id));}
            else{parameters.add(new BasicNameValuePair(Constant.USERID, f_id));}
            JSONObject json = jsonParser.getJSONFromUrl(Constant.USERPROFILEFEEDDATA,parameters);


            return json;
        }

        @Override
        protected void onPostExecute(JSONObject result)
        {
            try {

                if(result.getString("status").equalsIgnoreCase("Success"))
                {

                    resultArray=result.getJSONArray("resultArray");

                    for (int i = 0; i < resultArray.length(); i++)
                    {
                        JSONObject locationdata = resultArray.getJSONObject(i);

                        JSONObject feeddata=locationdata.getJSONObject("FeedData");

                        place_name.add(feeddata.getString("place"));
                        String currentLocation=feeddata.getString("feedGeometry");
                        String[] separated = currentLocation.split(",");
                        locations_markers.add(new LatLng(Double.parseDouble(separated[0]), Double.parseDouble(separated[1])));

                        FeedAttachment=locationdata.getJSONArray("FeedAttachment");
                        for(int j = 0;j< 1;j++)
                        {
                            JSONObject object = FeedAttachment.getJSONObject(j);
                            JSONObject feedattachment=object.getJSONObject("FeedAttachment");
                            String imageurl    =feedattachment.getString("url");
                            image_array.add(imageurl);
                        }




                    }



                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), result.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            setupMap();
        }
  public void  setupMap(){
      int i=0;

        for (LatLng location : locations_markers)
        {

            marker=googleMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(place_name.get(i).toString())
                            // .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                            // Specifies the anchor to be at a particular point in the marker image.
                    .anchor(0.5f, 1).icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

            currentLocation=new LatLng(location.latitude,location.longitude);
            marker.showInfoWindow();
            i++;
        }

      CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 16);
       googleMap.animateCamera(cameraUpdate);//uncomment to zoom at proper location



     mMapView.getMap().setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()
     {
         private final View window = getActivity().getLayoutInflater().inflate(
                  R.layout.custom_info_contents, null);

         @Override
          public View getInfoWindow(Marker marker) {


             String title = marker.getTitle();

              if (title != null) {
                // Spannable string allows us to edit the formatting of the
                 // text.
                 SpannableString titleText = new SpannableString(title);
                  titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
                        titleText.length(), 0);

             } else {

             }

             TextView txtType = ((TextView) window
               .findViewById(R.id.r_placename));



             ImageView iv_image=(ImageView)window.findViewById(R.id.first);
             for(int i=0;i<place_name.size();i++)
             {
                 if(place_name.get(i).toString().equalsIgnoreCase(marker.getTitle()))
                 {
                     txtType.setText(place_name.get(i).toString());
//                     Glide.with(getActivity())
//                             .load(image_array.get(i).toString())
//                             .centerCrop()
//                             .placeholder(R.drawable.user)
//                             .crossFade()
//                             .into(iv_image);
                     Picasso.with(getActivity())
                             .load(image_array.get(i).toString())
                             .into(iv_image);

                 }
             }

//               if (eventInfo.getType() != null)
//               txtType.setText(eventInfo.getType());

              return window;
          }

          @Override
          public View getInfoContents(Marker marker) {
//              // this method is not called if getInfoWindow(Marker) does not
            // return null
            return null;
         }
      });
  }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


//    public boolean onMarkerClick(Marker arg0) {
//        if (arg0.getSnippet() == null) {
//            googleMap.moveCamera(CameraUpdateFactory.zoomIn());
//            return true;
//        }
//        //arg0.showInfoWindow();
//
//        final Dialog d = new Dialog(getActivity());
//        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //d.setTitle("Select");
//        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//        d.setContentView(R.layout.marker_window);
//        photo = (ImageView) d.findViewById(R.id.imageview);
//        TextView tvName = (TextView) d.findViewById(R.id.infocontent_tv_name);
//        tvName.setText("Mark");
//
//        TextView tvType = (TextView) d.findViewById(R.id.infocontent_tv_type);
//        tvType.setText("(" + "sfsafnnsf" + ")");
//
//        TextView tvDesc = (TextView) d.findViewById(R.id.infocontent_tv_desc);
//        tvDesc.setText("fsafsafsafsaf");
//
//        TextView tvAddr = (TextView) d.findViewById(R.id.infocontent_tv_addr);
//        tvAddr.setText(Html.fromHtml("DataADDRESS"));
//
//        d.show();
//        return true;
//    }

}

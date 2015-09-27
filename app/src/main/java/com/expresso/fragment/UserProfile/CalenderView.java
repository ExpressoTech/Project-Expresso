package com.expresso.fragment.UserProfile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bumptech.glide.Glide;
import com.expresso.activity.R;
import com.expresso.activity.UserProfileActivities.UserProfileActivity;
import com.expresso.adapter.UserProfileAdapters.VisitedPlaceAdapter;
import com.expresso.model.UserProfileModel.VisitedPlaces;
import com.expresso.utils.Constant;
import com.expresso.utils.JSONParser;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CalenderView extends Fragment {
    private View view;

    private ProgressDialog pd;
    private String f_id = UserProfileActivity.f_user_id;//Friend_id
    private String u_id = UserProfileActivity.USER_ID;//User_id
    private JSONArray resultArray = null;
    private ListView listview_visitedplace;
    private VisitedPlaceAdapter adpater;
    private ArrayList<VisitedPlaces> VisitedList;

    private ArrayList<String> place_name = new ArrayList<String>();
    private ArrayList<String> date_of_vist = new ArrayList<String>();
    private ArrayList<String> month_of_visit = new ArrayList<String>();
    private ArrayList<String> year_of_vist = new ArrayList<String>();


    public CalenderView() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_userprofile_calender, container, false);


        bindWidgetEvents();
        getWidgetReferences();
        initialization();
//
        return view;

    }

    private void getWidgetReferences() {
    }

    private void bindWidgetEvents() {
        listview_visitedplace = (ListView) view.findViewById(R.id.listview_visitedplace);

    }

    private void initialization() {
        //  res=getResources();//Explain me why is line
        new FetchCalender().execute();

    }

    private class FetchCalender extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jsonParser = new JSONParser();
            // Building Parameters
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            if (f_id != null && !f_id.isEmpty()) {
                parameters.add(new BasicNameValuePair(Constant.USERID, f_id));
            } else {
                parameters.add(new BasicNameValuePair(Constant.USERID, u_id));
            }
            JSONObject json = jsonParser.getJSONFromUrl(Constant.LOCATION_DATE, parameters);


            return json;
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            Log.d("+++JSON::USERPROFILE++", String.valueOf(result));

            try {

                if (result.getString("status").equalsIgnoreCase("Success")) {
                    setUpCalender(result);

                } else {
                    Toast.makeText(getActivity().getApplicationContext(), result.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    private void setUpCalender(JSONObject result) {
        try {
            resultArray = result.getJSONArray("resultArray");
            for (int i = 0; i < resultArray.length(); i++) {


                JSONObject c = resultArray.getJSONObject(i);
                String currentdate = c.getString("feedDate");
                String[] spilter = currentdate.split("\\s+");
                String[] fulldate = spilter[0].split("-");
                year_of_vist.add(fulldate[0].toString());
                month_of_visit.add(fulldate[1].toString());
                date_of_vist.add(fulldate[2].toString());
                String currentplace = c.getString("feedPlace");
                place_name.add("Visited  " + currentplace);
                Log.d("JSONCOUNT", String.valueOf(place_name.size()));
            }
            Collections.reverse(year_of_vist);
            Collections.reverse(month_of_visit);
            Collections.reverse(date_of_vist);
            Collections.reverse(place_name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setUpCalenderMonth();
    }

    private void setUpCalenderMonth() {
        VisitedList = new ArrayList<VisitedPlaces>();

        Log.d("COUNT", String.valueOf(place_name.size()));


        int i = 0;
        for (String place : place_name) {
            VisitedPlaces item = new VisitedPlaces();
            Log.d("PALCE", place);
            item.setVisited_place_name(place);
            item.setDate_of_visit(date_of_vist.get(i).toString());

            switch (Integer.parseInt((String) month_of_visit.get(i).toString())) {
                case 1:
                    item.setMonth_of_visit("JAN");
                    break;
                case 2:
                    item.setMonth_of_visit("FEB");
                    break;
                case 3:
                    item.setMonth_of_visit("MAR");
                    break;
                case 4:
                    item.setMonth_of_visit("APRIL");
                    break;
                case 5:
                    item.setMonth_of_visit("MAY");
                    break;
                case 6:
                    item.setMonth_of_visit("JUNE");
                    break;
                case 7:
                    item.setMonth_of_visit("JULY");
                    break;
                case 8:
                    item.setMonth_of_visit("AUGUST");
                    break;
                case 9:
                    item.setMonth_of_visit("SEPT");
                    break;
                case 10:
                    item.setMonth_of_visit("OCT");
                    break;
                case 11:
                    item.setMonth_of_visit("NOV");
                    break;
                case 12:
                    item.setMonth_of_visit("DEC");
                    break;

            }


            item.setYear_of_visit(year_of_vist.get(i).toString());
            VisitedList.add(item);
            i++;
        }
        adpater = new VisitedPlaceAdapter(getActivity().getApplicationContext(), VisitedList);
        listview_visitedplace.setAdapter(adpater);
    }

}

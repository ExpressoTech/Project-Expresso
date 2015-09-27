package com.expresso.activity.UserProfileActivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.expresso.Managers.LoginManager;
import com.expresso.activity.R;

import com.expresso.adapter.UserProfileAdapters.FollowerAdapter;
import com.expresso.model.UserProfileModel.Following;
import com.expresso.utils.Constant;
import com.expresso.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohit on 6/7/15.
 */
public class Followers extends Activity {
    private ListView listview_feeds;
    private ProgressDialog pd;
    private JSONArray resultArray = null;
    private final String u_id = UserProfileActivity.USER_ID;//User_id
    private String f_id = UserProfileActivity.f_user_id;//Friend_id
    private ArrayList<Following> list;
    private FollowerAdapter followeradapter;
    private String STATUS_FLAG;
    private String friendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follower_listview);
        getWidgetReferences();
        initialization();
    }

    private void getWidgetReferences() {
        listview_feeds = (ListView) findViewById(R.id.listview_visitedplace);
    }

    private void bindWidgetEvents() {
    }

    private void initialization() {
        pd = new ProgressDialog(Followers.this);
        Intent intent = getIntent();// to find Following clicked or Follower
        STATUS_FLAG = intent.getStringExtra("FLAG_INDICATOR");
        friendId = intent.getExtras().getString(Constant.USERID);
        Log.d("STAtUS_FLAG", STATUS_FLAG);
        if (STATUS_FLAG.equalsIgnoreCase("0")) {
            new FetchFollowers().execute();
        } else {
            new FetchFollowing().execute();
        }
    }

    private class FetchFollowers extends AsyncTask<String, Void, JSONObject> {

        private final String FLAG = "0";

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jsonParser = new JSONParser();
            // Building Parameters
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair(Constant.USERID, new LoginManager(Followers.this).getUserId()));
            parameters.add(new BasicNameValuePair(Constant.FRIENDSID, friendId));
            parameters.add(new BasicNameValuePair(Constant.STATUS_FLAG, FLAG));
            JSONObject json = jsonParser.getJSONFromUrl(Constant.FETCHFOLLOWINGUSERS, parameters);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (pd.isShowing() && pd != null) {
                pd.dismiss();
            }
            try {
                if (result.getString("status").equalsIgnoreCase("Success")) {
                    setUpFetchListofFollowers(result);
                } else {
                    Toast.makeText(Followers.this, result.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            pd.setMessage("Loading");
            pd.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    private void setUpFetchListofFollowers(JSONObject result) {
        try {
            list = new ArrayList<Following>();

            resultArray = result.getJSONArray("FollowUsers");
            for (int i = 0; i < resultArray.length(); i++) {
                Following item = new Following();
                JSONObject c = resultArray.getJSONObject(i);
                item.setUser_name(c.getString("followerUserName"));
                item.setUser_avatar(c.getString("followerUserAvatar"));
                item.setUser_ID(Integer.parseInt(c.getString("followerUserId")));
                item.setFollowing_status(c.getString("FollowingStatus"));
                list.add(item);
            }
            followeradapter = new FollowerAdapter(Followers.this, list);
            listview_feeds.setAdapter(followeradapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class FetchFollowing extends AsyncTask<String, Void, JSONObject> {

        private final String FLAG = "1";

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jsonParser = new JSONParser();
            // Building Parameters
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair(Constant.USERID, new LoginManager(Followers.this).getUserId()));
            parameters.add(new BasicNameValuePair(Constant.FRIENDSID, friendId));
            parameters.add(new BasicNameValuePair(Constant.STATUS_FLAG, FLAG));
            JSONObject json = jsonParser.getJSONFromUrl(Constant.FETCHFOLLOWINGUSERS, parameters);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (pd.isShowing() && pd != null) {
                pd.dismiss();
            }
            try {
                if (result.getString("status").equalsIgnoreCase("Success")) {
                    setUpFetchListofFollowing(result);
                } else {
                    Toast.makeText(Followers.this, result.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            pd.setMessage("Loading");
            pd.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void setUpFetchListofFollowing(JSONObject result) {
        try {
            list = new ArrayList<Following>();


            resultArray = result.getJSONArray("FollowUsers");
            for (int i = 0; i < resultArray.length(); i++) {

                Following item = new Following();
                JSONObject c = resultArray.getJSONObject(i);
                item.setUser_name(c.getString("followingUserName"));
                item.setUser_avatar(c.getString("followingUserAvatar"));
                item.setUser_ID(Integer.parseInt(c.getString("followingUserId")));
                item.setFollowing_status(c.getString("checkFollowingStatus"));
                if (u_id.equalsIgnoreCase(f_id)) {
                    if (c.getString("checkFollowingStatus").equalsIgnoreCase("true")) {
                        list.add(item);
                    }

                } else {
                    list.add(item);
                }

            }
            followeradapter = new FollowerAdapter(Followers.this, list);
            listview_feeds.setAdapter(followeradapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, R.anim.splashfadeout);
    }
}

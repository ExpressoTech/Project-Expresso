package com.expresso.Managers;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.expresso.Expresso;
import com.expresso.database.DatabaseHelper;
import com.expresso.model.Users;
import com.expresso.utils.Constant;
import com.expresso.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rishi M on 7/25/2015.
 */
public class UserManager {
    private static final String FOLLOWING = "1";
    DatabaseHelper db;
    private Context mContext;
    private LoginManager mManager;
    public UserManager(Activity context)
    {
        mContext=context;
        db=new DatabaseHelper(context);
        mManager=new LoginManager(context);
    }

    public void getFollowingUsers(final Activity context) {
        String tag="following_users";

        String url = Constant.FETCHFOLLOWINGUSERS;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("response", response.toString());
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray followingusers = null;
                    followingusers = json.getJSONArray("FollowUsers");
                    for(int i=0;i<followingusers.length();i++)
                    {
                        JSONObject item = followingusers.getJSONObject(i);
                        String friendId= item.getString("followingUserId");
                        String username= item.getString("followingUserName");
                        String useravatar= item.getString("followingUserAvatar");
                        String followingStatus= item.getBoolean("checkFollowingStatus")+"";
                        db.followingUser(friendId, followingStatus, username, useravatar);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());
            }
        }) {
            /**
             * Passing some request params
             */

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(Constant.USERID,mManager.getUserId());
                params.put(Constant.FRIENDSID,mManager.getUserId());
                params.put(Constant.STATUS_FLAG,"1");

                return params;
            }


        };

        // Adding request to request queue
        Expresso.getInstance().addToRequestQueue(strReq, tag);
    }


    public void getFollowerUsers(final Activity context) {
        String tag="followers_users";

        String url = Constant.FETCHFOLLOWINGUSERS;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("response", response.toString());
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray followingusers = null;
                    followingusers = json.getJSONArray("FollowUsers");
                    for(int i=0;i<followingusers.length();i++)
                    {
                        JSONObject item = followingusers.getJSONObject(i);
                        String friendId= item.getString("followerUserId");
                        String username= item.getString("followerUserName");
                        String useravatar= item.getString("followerUserAvatar");
                        String followingStatus= item.getBoolean("FollowingStatus")+"";
                        db.followerUser(friendId, followingStatus, username, useravatar);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());
            }
        }) {
            /**
             * Passing some request params
             */

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(Constant.USERID,mManager.getUserId());
                params.put(Constant.FRIENDSID,mManager.getUserId());
                params.put(Constant.STATUS_FLAG,"0");

                return params;
            }


        };

        // Adding request to request queue
        Expresso.getInstance().addToRequestQueue(strReq, tag);
    }

    public void clearAllUsers()
    {
        db.clearAllUsers();
    }

    public ArrayList<Users> getAllUsers() {
        return db.getAllUsers();
    }
}

package com.expresso.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.expresso.activity.R;
import com.expresso.database.DatabaseHelper;
import com.expresso.fragment.HomeFragment;
import com.expresso.model.Feed;
import com.expresso.utils.Constant;
import com.expresso.utils.JSONParser;
import com.expresso.utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rishi M on 7/11/2015.
 */
public class FollowAsynTask extends AsyncTask<String, Void, JSONObject> {

    public static final String USERID = "userId";
    public static final String FRIENDID = "friendId";
    public static final String STATUS = "status";
    public static final String MODE = "mode";


    public static final String FOLLOW = "1";
    public static final String UNFOLLOW = "0";
    public static final String MODEEMAIL = "email";
    private Feed item;
    private String mode,status,userId,friendId;
    private Activity activity;
    private DatabaseHelper db;
    private int position;

    public FollowAsynTask(String userId, String friendId, Activity activity, Feed item, String mode, String status, int position) {
        this.item=item;
        this.mode=mode;
        this.activity=activity;
        this.status=status;
        this.userId=userId;
        this.friendId=friendId;
        this.position=position;
        db=new DatabaseHelper(activity);
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONParser jsonParser=new JSONParser();
        // Building Parameters
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair(USERID,userId));
        parameters.add(new BasicNameValuePair(FRIENDID,friendId));
        parameters.add(new BasicNameValuePair(MODE,mode));
        parameters.add(new BasicNameValuePair(STATUS,status));
        JSONObject json = jsonParser.getJSONFromUrl(Constant.FOLLOWUSER, parameters);
        return json;
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        Utils.closeProgress();
        try {
            if(json.getString("status").equalsIgnoreCase("Success")) {
                if (status.equalsIgnoreCase(FOLLOW)) {
                    db.followingUser(friendId,status,item.getUser_name(), item.getUser_avatar());
                    HomeFragment.updateListView(position,true);
                } else {
                    db.unfollowUser(userId, friendId);
                    HomeFragment.updateListView(position,false);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        Utils.showProgress(activity,activity.getResources().getString(R.string.pleasewait));
    }

    @Override
    protected void onProgressUpdate(Void... values) {

    }
}

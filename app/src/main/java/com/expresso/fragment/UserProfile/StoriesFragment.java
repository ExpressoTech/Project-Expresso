package com.expresso.fragment.UserProfile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.expresso.activity.FeedPageLocation;
import com.expresso.activity.R;
import com.expresso.activity.StoryBoard;
import com.expresso.activity.UserProfileActivities.UserProfileActivity;
import com.expresso.adapter.FeedAdapter;
import com.expresso.database.DatabaseHelper;
import com.expresso.model.Feed;
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
 * Created by rohit on 13/7/15.
 */
public class StoriesFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ListView listview_feeds;
    private FeedAdapter adpater;
    private ArrayList<Feed> feedList;
    private Resources res;
    private ProgressDialog pd;
    private DatabaseHelper db;
    private String f_id = UserProfileActivity.f_user_id;
    private String u_id = UserProfileActivity.USER_ID;

    public StoriesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_stories, container, false);

        getWidgetReferences();
        bindWidgetEvents();
        initialization();


        listview_feeds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Feed item = adpater.getItem(position);
                Intent i = new Intent(getActivity(), StoryBoard.class);
                i.putExtra(Constant.FEEDID, item.getFeedID());
                startActivity(i);
            }
        });
        return view;
    }


    private void getWidgetReferences() {

        listview_feeds = (ListView) view.findViewById(R.id.listview_feeds);
    }

    private void bindWidgetEvents() {
    }

    private void initialization() {
        pd = new ProgressDialog(getActivity());
        res = getResources();
        new FetchFeeds().execute();
        db = new DatabaseHelper(getActivity().getApplicationContext());
//        setUpDummyFeeds();
    }


    @Override
    public void onClick(View v) {

    }


    private class FetchFeeds extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jsonParser = new JSONParser();
            // Building Parameters
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();

            parameters.add(new BasicNameValuePair(Constant.USERID, UserProfileActivity.f_user_id));
            JSONObject json = jsonParser.getJSONFromUrl(Constant.USERPROFILEDETAILFEEDDATA, parameters);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {

            try {
                if (json.getString("status").equalsIgnoreCase("Success")) {
                    setUpFetchFeeds(json);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), json.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

 /*   private void setUpFetchFeeds(JSONObject json) {
        try {
            Log.d("JSON:USERPROFILE_Story", String.valueOf(json));
            feedList=new ArrayList<Feed>();
            JSONArray resultArray=json.getJSONArray("resultArray");
            for(int i=0;i<resultArray.length();i++)
            {
                JSONObject jsonResult=resultArray.getJSONObject(i);
                JSONObject data= jsonResult.getJSONObject("FeedData");
                Feed item=new Feed();
                item.setUser_name(jsonResult.getString("feedUserName"));
                item.setUser_avatar(jsonResult.getString("feedUserAvatar"));
                item.setFeed_image(data.getString("images"));
                item.setFeed_title(data.getString("title"));
//                item.setFeed_privacy(data.getString("privacy"));
//                item.setCreatedById(Integer.parseInt(data.getString("userId")));
                item.setFeed_age(data.getString("createdAt"));
                item.setFeed_location(data.getString("place"));
                item.setFeedID(Integer.parseInt(data.getString("feedId")));
//                item.setFollowingUser(userdata.getBoolean("isFollowing"));
//                item.setTotalComment(Integer.parseInt(userdata.getString("totalComments")));
                feedList.add(item);
            }
            db.saveAllFeeds(json);
            adpater=new FeedAdapter(getActivity(),feedList);
            listview_feeds.setAdapter(adpater);


        }

        catch (JSONException e) {
            e.printStackTrace();
        }
    }*/


    private void setUpFetchFeeds(JSONObject json) {
        try {
            feedList = new ArrayList<Feed>();
            JSONArray resultArray = json.getJSONArray("resultArray");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject jsonResult = resultArray.getJSONObject(i);
                JSONObject data = jsonResult.getJSONObject("FeedData");
                JSONObject userdata = jsonResult.getJSONObject("feedUserData");
                Feed item = new Feed();
                item.setUser_name(userdata.getString("FeedCreatedByName"));
                item.setUser_avatar(userdata.getString("FeedCreatedByAvatar"));
                item.setFeed_image(data.getString("images"));
                item.setFeed_title(data.getString("title"));
                item.setFeed_privacy(data.getString("privacy"));
                item.setCreatedById(Integer.parseInt(data.getString("userId")));
                item.setFeed_age(data.getString("createdAt"));
                item.setFeed_location(data.getString("place"));
                item.setFeedID(Integer.parseInt(data.getString("feedId")));
                item.setFollowingUser(userdata.getBoolean("isFollowing"));
                item.setTotalComment(Integer.parseInt(userdata.getString("totalComments")));
                feedList.add(item);
            }
            //    db.saveAllFeeds(json);
            adpater = new FeedAdapter(getActivity(), feedList);
            listview_feeds.setAdapter(adpater);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

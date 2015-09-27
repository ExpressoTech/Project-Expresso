package com.expresso.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
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

import com.bumptech.glide.util.Util;
import com.expresso.Managers.LoginManager;
import com.expresso.Managers.UserManager;
import com.expresso.activity.BirdView;
import com.expresso.activity.FeedPageLocation;
import com.expresso.activity.MainActivity;
import com.expresso.activity.R;
import com.expresso.activity.StoryBoard;
import com.expresso.activity.ViewFeedDrafts;
import com.expresso.adapter.FeedAdapter;
import com.expresso.database.DatabaseHelper;
import com.expresso.model.Feed;
import com.expresso.model.FeedComment;
import com.expresso.utils.Constant;
import com.expresso.utils.JSONParser;
import com.expresso.utils.Utils;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private View view;
    private static ListView listview_feeds;
    private static FeedAdapter adpater;
    private static ArrayList<Feed> feedList;
    private ArrayList<FeedComment> feedCommentList;
    private Resources res;
    private ImageView iv_newFeed;
    private ProgressDialog pd;
    private DatabaseHelper db;
    private LoginManager mManager;
    private FloatingActionButton fab_story,fab_drafts;
    private UserManager userManager;
    public HomeFragment() {
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        getWidgetReferences();
        bindWidgetEvents();
        initialization();

        /*listview_feeds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Feed item=adpater.getItem(position);
                Intent i=new Intent(getActivity(),StoryBoard.class);
                i.putExtra(Constant.FEEDID,item.getFeedID());
                startActivity(i);
            }
        });*/
		return view;
	}


    private void getWidgetReferences() {
        listview_feeds= (ListView) view.findViewById(R.id.listview_feeds);
        iv_newFeed= (ImageView) view.findViewById(R.id.iv_newFeed);
        fab_story= (FloatingActionButton) view.findViewById(R.id.fab_story);
        fab_drafts= (FloatingActionButton) view.findViewById(R.id.fab_drafts);
    }

    private void bindWidgetEvents() {
        iv_newFeed.setOnClickListener(this);
        fab_story.setOnClickListener(this);
        fab_drafts.setOnClickListener(this);
    }

    private void initialization() {
        userManager=new UserManager(getActivity());
        mManager=new LoginManager(getActivity());
        pd=new ProgressDialog(getActivity());
        res=getResources();
        new FetchFeeds().execute();
        db=new DatabaseHelper(getActivity().getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        if(v==fab_story)
        {
            Intent i=new Intent(getActivity(),FeedPageLocation.class);
            startActivity(i);
        }
        if(v==fab_drafts)
        {
            Intent i=new Intent(getActivity(),ViewFeedDrafts.class);
            startActivity(i);
        }
    }

    private class FetchFeeds extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jsonParser=new JSONParser();
            // Building Parameters
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair(Constant.USERID,mManager.getUserId()));
            JSONObject json = jsonParser.getJSONFromUrl(Constant.FETCHFEEDSURL, parameters);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
           Utils.closeProgress();
            try {
                if(json.getString("status").equalsIgnoreCase("Success"))
                {
                    setUpFetchFeeds(json);
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), json.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            Utils.showProgress(getActivity(),getResources().getString(R.string.pleasewait));
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    private void setUpFetchFeeds(JSONObject json) {
        try {
            feedList=new ArrayList<Feed>();
            JSONArray resultArray=json.getJSONArray("resultArray");
            for(int i=0;i<resultArray.length();i++)
            {
                JSONObject jsonResult=resultArray.getJSONObject(i);
                JSONObject data= jsonResult.getJSONObject("FeedData");
                JSONObject userdata= jsonResult.getJSONObject("feedUserData");
                Feed item=new Feed();
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
            db.saveAllFeeds(json);
            adpater=new FeedAdapter(getActivity(),feedList);
            listview_feeds.setAdapter(adpater);
            // save all following/follower users
            userManager.clearAllUsers();
            userManager.getFollowingUsers(getActivity());
            userManager.getFollowerUsers(getActivity());


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void updateListView(int pos,boolean status)
    {
        Feed item = feedList.get(pos);
        int createdById = item.getCreatedById();
        for(int i=0;i<feedList.size();i++)
        {
            Feed feedItem = feedList.get(i);
            if(createdById==feedItem.getCreatedById())
                feedItem.setFollowingUser(status);
        }
        adpater.notifyDataSetChanged();
    }
}

package com.expresso.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.expresso.activity.BirdView;
import com.expresso.activity.FeedPageLocation;
import com.expresso.activity.MainActivity;
import com.expresso.activity.R;
import com.expresso.activity.StoryBoard;
import com.expresso.adapter.FeedAdapter;
import com.expresso.database.DatabaseHelper;
import com.expresso.model.Feed;
import com.expresso.utils.Constant;
import com.expresso.utils.JSONParser;
import com.expresso.utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private View view;
    private ListView listview_feeds;
    private FeedAdapter adpater;
    private ArrayList<Feed> feedList;
    private Resources res;
    private ImageView iv_newFeed;
    private ProgressDialog pd;
    private DatabaseHelper db;
    public HomeFragment() {
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        getWidgetReferences();
        bindWidgetEvents();
        initialization();

        listview_feeds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Feed item=adpater.getItem(position);
                Intent i=new Intent(getActivity(),StoryBoard.class);
                i.putExtra(Constant.FEEDID,item.getFeedID());
                startActivity(i);
            }
        });
		return view;
	}


    private void getWidgetReferences() {

        listview_feeds= (ListView) view.findViewById(R.id.listview_feeds);
        iv_newFeed= (ImageView) view.findViewById(R.id.iv_newFeed);
    }

    private void bindWidgetEvents() {
        iv_newFeed.setOnClickListener(this);
    }

    private void initialization() {
        pd=new ProgressDialog(getActivity());
        res=getResources();
        new FetchFeeds().execute();
        db=new DatabaseHelper(getActivity().getApplicationContext());
//        setUpDummyFeeds();
    }

    private void setUpDummyFeeds() {
        feedList=new ArrayList<Feed>();
        String userAvatar[]={res.getString(R.string.userprofile1),res.getString(R.string.userprofile2),res.getString(R.string.userprofile1),res.getString(R.string.userprofile2),res.getString(R.string.userprofile1),res.getString(R.string.userprofile2),res.getString(R.string.userprofile1),res.getString(R.string.userprofile2),res.getString(R.string.userprofile1),res.getString(R.string.userprofile2)};
        String userName[]={res.getString(R.string.username1),res.getString(R.string.username2),res.getString(R.string.username1),res.getString(R.string.username2),res.getString(R.string.username1),res.getString(R.string.username2),res.getString(R.string.username1),res.getString(R.string.username2),res.getString(R.string.username1),res.getString(R.string.username1)};
        String userFeeds[]=res.getStringArray(R.array.feedlist);
        String userFeedTitle[]=res.getStringArray(R.array.feedtitlelist);

        for(int i=0;i<userFeeds.length;i++)
        {
            Feed item=new Feed();
            item.setUser_name(userName[i]);
            item.setUser_avatar(userAvatar[i]);
            item.setFeed_image(userFeeds[i]);
            item.setFeed_title(userFeedTitle[i]);
            item.setFeed_age("3 min ago");
           /* item.setFeed_comments("5 comments");*/
            item.setFeed_location("IIT Powai");
            feedList.add(item);
        }

        adpater=new FeedAdapter(getActivity(),feedList);
        listview_feeds.setAdapter(adpater);
    }

    @Override
    public void onClick(View v) {
        if(v==iv_newFeed)
        {
            Intent i=new Intent(getActivity(),FeedPageLocation.class);
            startActivity(i);
        }
    }




    private class FetchFeeds extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jsonParser=new JSONParser();
            // Building Parameters
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            JSONObject json = jsonParser.getJSONFromUrl(Constant.FETCHFEEDSURL, parameters);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            if(pd.isShowing() && pd!=null)
                pd.dismiss();
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
            pd.setMessage("Loading");
            pd.show();
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
                item.setFeed_age(data.getString("createdAt"));
                item.setFeed_location(data.getString("place"));
                item.setFeedID(Integer.parseInt(data.getString("feedId")));
                feedList.add(item);
            }
            db.saveAllFeeds(json);
            adpater=new FeedAdapter(getActivity(),feedList);
            listview_feeds.setAdapter(adpater);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

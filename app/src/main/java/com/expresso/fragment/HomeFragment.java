package com.expresso.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.expresso.activity.R;
import com.expresso.adapter.FeedAdapter;
import com.expresso.model.Feed;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private View view;
    private ListView listview_feeds;
    private FeedAdapter adpater;
    private ArrayList<Feed> feedList;
    private Resources res;
    public HomeFragment() {
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        getWidgetReferences();
        bindWidgetEvents();
        initialization();
		return view;
	}


    private void getWidgetReferences() {
    }

    private void bindWidgetEvents() {
        listview_feeds= (ListView) view.findViewById(R.id.listview_feeds);
    }

    private void initialization() {
        res=getResources();
        setUpDummyFeeds();
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
            item.setFeed_comments("5 comments");
            item.setFeed_location("IIT Powai");
            feedList.add(item);
        }

        adpater=new FeedAdapter(getActivity(),feedList);
        listview_feeds.setAdapter(adpater);
    }
}

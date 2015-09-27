package com.expresso.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.expresso.Managers.FeedManager;
import com.expresso.Managers.LoginManager;
import com.expresso.Managers.UserManager;
import com.expresso.activity.R;
import com.expresso.model.Feed;
import com.expresso.model.Users;
import com.expresso.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Rishi M on 6/13/2015.
 */
public class FullScreenImageAdapter extends PagerAdapter implements View.OnClickListener {
    private Activity _activity;
    private ArrayList<String> _imagePaths;
    private LayoutInflater inflater;
    private int feedID;
    private FeedManager feedManager;
    private Feed item;
    private Animation animation;
    private TextView tv_heading;
    private LoginManager mManager;
    private ImageView imgDisplay;
    private TextView tv_startTitle;
    private Button btnClose;
    // constructor
    public FullScreenImageAdapter(Activity activity, ArrayList<String> imagePaths, int feedID) {
        this._activity = activity;
        this._imagePaths = imagePaths;
        this.feedID = feedID;
        feedManager=new FeedManager(activity);
        mManager=new LoginManager(activity);
    }

    @Override
    public int getCount() {
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        item = feedManager.getFeedItembyID(feedID);
        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);
        animation = AnimationUtils.loadAnimation(_activity,R.anim.fade_in);
        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);
        btnClose = (Button) viewLayout.findViewById(R.id.btnClose);
        tv_startTitle= (TextView) viewLayout.findViewById(R.id.tv_startTitle);
        tv_heading= (TextView) viewLayout.findViewById(R.id.tv_heading);
        imgDisplay.setOnClickListener(this);
        Picasso.with(_activity).load(_imagePaths.get(position)).placeholder(R.drawable.please_wait).into(imgDisplay);

        // close button click event
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _activity.finish();
            }
        });

        //tag heading visibility
        if(mManager.isMyStory(item.getCreatedById()))
            tv_heading.setVisibility(View.VISIBLE);
        else
            tv_heading.setVisibility(View.GONE);

        if(position==0) {
            tv_startTitle.setVisibility(View.VISIBLE);
            Spanned title  = Html.fromHtml("<b><big><font size=\"40\" color=\"#ff0000\">" + item.getFeed_title().toUpperCase() + "</font></big></b>" + "<br />" +
                    "<small> at </small>" +
                    "<small>" + item.getFeed_location() + "</small>");
            tv_startTitle.setText(title);
            tv_startTitle.startAnimation(animation);
        }
        else if(_imagePaths.size()-1==position)
        {
            tv_startTitle.setVisibility(View.VISIBLE);
            Spanned title  = Html.fromHtml("Story by <big> " + item.getUser_name()+ "</big>");
            tv_startTitle.setText(title);
            tv_startTitle.startAnimation(animation);
        }
        else
            tv_startTitle.setVisibility(View.GONE);
        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }

    @Override
    public void onClick(View v) {
        if(v==imgDisplay) {
            if (mManager.isMyStory(item.getCreatedById())) {
                ArrayList<Users> usersList = new UserManager(_activity).getAllUsers();
                Log.e("usersList", usersList.toString());
                for (int i = 0; i < usersList.size(); i++) {
                    Utils.showToast(_activity, usersList.get(i).getUsername());
                }
            }
        }
    }
}

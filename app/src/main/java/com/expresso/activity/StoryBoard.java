package com.expresso.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.expresso.database.DatabaseHelper;
import com.expresso.model.Feed;
import com.expresso.model.FeedAttachment;
import com.expresso.utils.Constant;
import com.expresso.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rishi M on 6/12/2015.
 */
public class StoryBoard extends ActionBarActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private SliderLayout mDemoSlider;
    private int feedId;
    private Feed feedItem;
    private DatabaseHelper db;
    private ImageView iv_userImage;
    private TextView tv_feedage,tv_username,tv_location;
    private ArrayList<FeedAttachment> attachment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storyboard);
        Intent data=getIntent();
        feedId=data.getIntExtra(Constant.FEEDID,0);
        getWidgetReferences();
        bindWidgetEvents();
        initialization();
        setupStory();
    }

    private void setupStory() {
        HashMap<String,String> url_maps = new HashMap<String, String>();
        feedItem=db.getUserFeed(feedId);
        if(feedItem!=null)
        {
            Picasso.with(getApplicationContext()).load(feedItem.getUser_avatar()).placeholder(R.drawable.please_wait).into(iv_userImage);
            tv_username.setText(feedItem.getUser_name());
            tv_feedage.setText(Utils.dateDiff(Utils.getDate(feedItem.getFeed_age())));
            tv_location.setText(feedItem.getFeed_location());
            attachment = db.getUserFeedAttachemnt(feedId);
            for(int i=0;i<attachment.size();i++)
            {
                FeedAttachment item=attachment.get(i);
                if(url_maps.containsKey(item.getFeed_tag()))
                    url_maps.put("No Tag",item.getFeed_url());
                else
                    url_maps.put(item.getFeed_tag(),item.getFeed_url());
            }
            for(String name : url_maps.keySet()){
                TextSliderView textSliderView = new TextSliderView(this);
                // initialize a SliderLayout
                textSliderView
                        .description(name)
                        .image(url_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(StoryBoard.this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(2000);
        mDemoSlider.addOnPageChangeListener(StoryBoard.this);
        }
    }

    private void getWidgetReferences() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_header);
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        iv_userImage= (ImageView) findViewById(R.id.iv_userImage);
        tv_feedage= (TextView) findViewById(R.id.tv_feedage);
        tv_username= (TextView) findViewById(R.id.tv_username);
        tv_location= (TextView) findViewById(R.id.tv_location);
    }

    private void bindWidgetEvents() {
    }

    private void initialization() {
        db=new DatabaseHelper(getApplicationContext());
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("StoryBord");
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        attachment=new ArrayList<FeedAttachment>();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
        Log.e("Slider Demo", "Page Changed: " + i);
    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}

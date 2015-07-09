package com.expresso.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.expresso.Managers.LoginManager;
import com.expresso.adapter.CommentAdapter;
import com.expresso.database.DatabaseHelper;
import com.expresso.model.Feed;
import com.expresso.model.FeedAttachment;
import com.expresso.model.FeedComment;
import com.expresso.utils.Constant;
import com.expresso.utils.JSONParser;
import com.expresso.utils.Utils;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rishi M on 6/12/2015.
 */
public class StoryBoard extends ActionBarActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener,View.OnClickListener {
    private SliderLayout mDemoSlider;
    private int feedId;
    private Feed feedItem;
    private DatabaseHelper db;
    private ImageView iv_userImage;
    private TextView tv_feedage,tv_username,tv_location;
    private ArrayList<FeedAttachment> attachment;
    private Toolbar toolbar;
    private ArrayList<FeedComment> feedCommentList;
    private CommentAdapter commentAdapter;
    private ListView commentsListview;
    private RelativeLayout footer;
    private LoginManager mManager;
    private String comment="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storyboard);
        Intent data=getIntent();
        feedId=data.getIntExtra(Constant.FEEDID,0);
        // fetch feed comments API
        new FetchFeedComment().execute();
        getWidgetReferences();
        bindWidgetEvents();
        initialization();
        setupStory();
        setupStoryComment();

        commentsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FeedComment item =commentAdapter.getItem(position);
                Intent replyIntent =new Intent(StoryBoard.this,ReplyActivity.class);
                replyIntent.putExtra("CommentId",item.getCommentId());
                replyIntent.putExtra("Comment",item.getComment());
                replyIntent.putExtra("UserImage",item.getAvatar());
                startActivity(replyIntent);
            }
        });
    }

    private void setupStoryComment() {
        /*feedCommentList=getIntent().getParcelableArrayListExtra(Constant.FEEDCOMMENT);
        commentAdapter=new CommentAdapter(this,feedCommentList);
        commentsListview.setAdapter(commentAdapter);*/
    }

    private void setupStory() {
        HashMap<String,String> url_maps = new HashMap<String, String>();
        feedItem=db.getUserFeed(feedId);
        if(feedItem!=null)
        {
            if(!feedItem.getUser_avatar().isEmpty())
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
        commentsListview= (ListView) findViewById(R.id.commentsListview);
        footer= (RelativeLayout) findViewById(R.id.footer);
    }

    private void bindWidgetEvents() {
        footer.setOnClickListener(this);
    }

    private void initialization() {
        mManager=new LoginManager(getApplicationContext());
        db=new DatabaseHelper(getApplicationContext());
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("StoryBord");
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        attachment=new ArrayList<FeedAttachment>();
        feedCommentList=new ArrayList<FeedComment>();
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

    @Override
    public void onClick(View v) {
        if(v==footer)
        {
            showDialogtoAddComment();
        }
    }


    private class FetchFeedComment extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jsonParser=new JSONParser();
            // Building Parameters
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair(Constant.FEEDID,feedId+""));
            JSONObject json = jsonParser.getJSONFromUrl(Constant.FETCHFEED_COMMENT_URL, parameters);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            Utils.closeProgress();
            try {
                if(json.getString("status").equalsIgnoreCase("Success"))
                {
                    setUpFetchFeedComments(json);
                }
                else {
                    Toast.makeText(StoryBoard.this, json.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            Utils.showProgress(StoryBoard.this,getResources().getString(R.string.pleasewait));
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }


    private void setUpFetchFeedComments(JSONObject json) {
        try {
            feedCommentList=new ArrayList<FeedComment>();
            JSONArray resultArray=json.getJSONArray("feedComment");
            for(int i=0;i<resultArray.length();i++)
            {
                JSONObject jsonResult=resultArray.getJSONObject(i);
                try {
                            JSONObject userComment = jsonResult.getJSONObject("CommentUserData");
                            FeedComment item = new FeedComment();
                            item.setComment(jsonResult.getString("Comment"));
                            item.setCommentId(Integer.parseInt(userComment.getString("commentId")));
                            item.setName(userComment.getString("CommentCreatedByName"));
                            item.setAvatar(userComment.getString("CommentCreatedByAvatar"));
                            feedCommentList.add(item);
                }catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
           if(feedCommentList.size()>0) {
               commentAdapter = new CommentAdapter(this, feedCommentList);
               commentsListview.setAdapter(commentAdapter);
           }

        } catch (JSONException e) {
        }
    }


    private void showDialogtoAddComment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StoryBoard.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.add_commentview, null);
        builder.setView(dialoglayout);
        builder.setTitle("Ask you query");
        final EditText et_query = (EditText) dialoglayout.findViewById(R.id.et_query);
        builder.setPositiveButton(R.string.post, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(!et_query.getText().toString().isEmpty())
                {
                    new PostComment().execute(et_query.getText().toString());
                }
                else
                    Utils.showToast(StoryBoard.this,"Please enter text");
            }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        Dialog d= builder.create();
        d.show();

    }



    private class PostComment extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            comment=params[0];
            JSONParser jsonParser=new JSONParser();
            // Building Parameters
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("feedId",feedId+""));
            parameters.add(new BasicNameValuePair(Constant.USERID,mManager.getUserId()));
            parameters.add(new BasicNameValuePair(Constant.COMMENT,params[0]));
            parameters.add(new BasicNameValuePair(Constant.FLAG,"1"));
            JSONObject json = jsonParser.getJSONFromUrl(Constant.POST_COMMENT_URL, parameters);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            Utils.closeProgress();
            try {
                if(json.getString("status").equalsIgnoreCase("Success"))
                {
                    FeedComment item = new FeedComment();
                    item.setComment(comment);
                    item.setCommentId(Integer.parseInt(json.getString("LastCommentId")));
                    item.setName(mManager.getUserName());
                    item.setAvatar(mManager.getUserAvatar());
                    if(feedCommentList.size()==0)
                    {
                        feedCommentList.add(item);
                        commentAdapter = new CommentAdapter(StoryBoard.this, feedCommentList);
                        commentsListview.setAdapter(commentAdapter);
                    }
                    else {
                        feedCommentList.add(item);
                        commentAdapter.notifyDataSetChanged();
                    }
                 }
                else {
                    Toast.makeText(StoryBoard.this, json.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            Utils.showProgress(StoryBoard.this,getResources().getString(R.string.pleasewait));
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }
}

package com.expresso.activity.UserProfileActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.expresso.Managers.LoginManager;
import com.expresso.activity.R;
import com.expresso.adapter.UserProfileAdapters.UserProfileTabsPagerAdapter;
import com.expresso.database.DatabaseHelper;
import com.expresso.utils.Constant;
import com.expresso.utils.JSONParser;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends FragmentActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private UserProfileTabsPagerAdapter mAdapter;
    public static ArrayList<String> list_of_following = new ArrayList<String>();
    private ImageView iv_user_pic;
    private TextView tv_username, tv_stories_count, tv_following_count, tv_follower_count, tv_following, tv_follower;
    private ImageView iv_back;
    private String display_pic;
    private String Total_following, Total_follower;
    private ProgressDialog pd;
    private String frnd_id;
    private JSONArray resultArray = null;
    public String FOLLOWING_FOLLOWER_FLAG;
    public static  String USER_ID;
    public static String f_user_id ;
    private DatabaseHelper db;
    private LoginManager mManager;

    // Tab tisdsadtles


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        mManager =new LoginManager(this);
        getWidgetReferences();
        bindWidgetEvents();
        // Building Parameters
        Intent intent = getIntent();
        frnd_id = intent.getExtras().getInt(Constant.USERID)+"";
        if (frnd_id != null && !frnd_id.isEmpty()) {
            if (mManager.getUserId().equalsIgnoreCase(frnd_id))
                f_user_id = mManager.getUserId();
            else
                f_user_id = frnd_id;
        }
        initialization();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mAdapter = new UserProfileTabsPagerAdapter(getSupportFragmentManager());
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        viewPager.setAdapter(mAdapter);
        tabs.setShouldExpand(true);
        tabs.setViewPager(viewPager);




        viewPager.setOffscreenPageLimit(3);

    }

    private void getWidgetReferences() {

        iv_user_pic = (ImageView) findViewById(R.id.profile_pic1);
        tv_username = (TextView) findViewById(R.id.Txt01);
        tv_stories_count = (TextView) findViewById(R.id.StoryCount);
        tv_follower_count = (TextView) findViewById(R.id.FollowerCount);
        tv_following_count = (TextView) findViewById(R.id.FollowingCount);
        tv_following = (TextView) findViewById(R.id.Following);
        tv_follower = (TextView) findViewById(R.id.Follower);
        iv_back = (ImageView) findViewById(R.id.ib);

    }

    private void bindWidgetEvents() {
        tv_following.setOnClickListener(this);
        tv_follower.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_user_pic.setOnClickListener(this);

    }

    private void initialization() {
        pd = new ProgressDialog(UserProfileActivity.this);
        new UserProfile().execute();
    }

    @Override
    public void onClick(View v) {
        if (v == tv_following) {
            FOLLOWING_FOLLOWER_FLAG = "1";
            Intent intent = new Intent(UserProfileActivity.this, Followers.class);
            intent.putExtra("FLAG_INDICATOR", FOLLOWING_FOLLOWER_FLAG);
            intent.putExtra(Constant.USERID,f_user_id);
            startActivity(intent);
        }
        if (v == tv_follower) {
            FOLLOWING_FOLLOWER_FLAG = "0";
            Intent intent = new Intent(UserProfileActivity.this, Followers.class);
            intent.putExtra("FLAG_INDICATOR", FOLLOWING_FOLLOWER_FLAG);
            intent.putExtra(Constant.USERID,f_user_id);
            startActivity(intent);
        }
        if (v == iv_user_pic) {
            Intent intent = new Intent(UserProfileActivity.this, ImageFullScreen.class);
            intent.putExtra(Constant.USERAVATAR, display_pic);
            startActivity(intent);
        }
        if (v == iv_back) {
            finish();
        }


    }


    private class UserProfile extends AsyncTask<String, Void, JSONObject> {


        @Override
        protected JSONObject doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser();
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();

            parameters.add(new BasicNameValuePair(Constant.USERID, f_user_id));
            JSONObject json = jsonParser.getJSONFromUrl(Constant.USERPROFILEDETAILS, parameters);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            Log.d("Main_USERPROFILE++", String.valueOf(result));
            /*if (pd.isShowing() && pd != null) {
                pd.dismiss();
            }*/
            try {
                if (result.getString("status").equalsIgnoreCase("Success")) {
                    setUpProfile(result);

                } else {
                    Toast.makeText(UserProfileActivity.this, result.getString("message"), Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
        /*    pd.setMessage("Loading");
            pd.show();*/
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    private void setUpProfile(JSONObject result) {
        try {
            resultArray = result.getJSONArray("resultArray");

            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject c = resultArray.getJSONObject(i);
                String contact = c.getString("userName");
                tv_username.setText(contact);


                display_pic = c.getString("userAvatar");
                if (display_pic != null && !display_pic.isEmpty()) {
                    Picasso.with(UserProfileActivity.this)
                            .load(display_pic)
                            .into(iv_user_pic);
//                            Glide.with(UserFragment.this)
//                                    .load(display_pic)
//                                    .centerCrop()
//                                    .placeholder(R.drawable.default_user)
//                                    .crossFade()
//                                    .into(iv_user_pic);
                }

                Total_follower = c.getString("totalFollowerCount");
                tv_follower_count.setText(Total_follower);


                Total_following = c.getString("totalFollowingCount");
                tv_following_count.setText(Total_following);


                String stories = c.getString("totalStories");
                tv_stories_count.setText(stories);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new UserProfile().execute();
    }

    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, R.anim.splashfadeout);
    }
}
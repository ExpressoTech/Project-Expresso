package com.expresso.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.expresso.database.DatabaseHelper;
import com.expresso.model.Feed;
import com.expresso.model.FeedAttachment;
import com.expresso.utils.Constant;
import com.expresso.utils.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.ArrayList;

import com.expresso.Managers.LoginManager;

/**
 * Created by Anirdesh_And0001 on 31-05-2015.
 */
public class EndStory extends ActionBarActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private EditText et_feedTitle;
    private Button btn_renderStory;
    private DatabaseHelper db;
    ProgressDialog pd;
    HttpEntity resEntity;
    private LoginManager mManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_story_page);
        getWidgetReferences();
        bindWidgetEvents();
        initialization();
        setupToolBar();
    }


    private void setupToolBar() {
        // set up toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.endStory);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getWidgetReferences() {
        toolbar= (Toolbar) findViewById(R.id.toolbar_header);
        et_feedTitle= (EditText) findViewById(R.id.et_feedTitle);
        btn_renderStory= (Button) findViewById(R.id.btn_renderStory);
    }

    private void bindWidgetEvents() {
        btn_renderStory.setOnClickListener(this);
    }

    private void initialization() {
        db=new DatabaseHelper(getApplicationContext());
        mManager=new LoginManager(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        if(v==btn_renderStory)
        {
            if(!et_feedTitle.getText().toString().isEmpty())
            {
                db.updateFeedData(et_feedTitle.getText().toString(),Constant.getCoverPicUrl(getApplicationContext()),Constant.getFeedID(getApplicationContext()));
                new CreateFeedStory().execute();
            }
        }
    }

    private class CreateFeedStory extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
             String response_str="";
            ArrayList<FeedAttachment> result=new ArrayList<FeedAttachment>();
            Feed item=db.getCurrentFeed(Constant.getFeedID(getApplicationContext()));
            if(item!=null)
            {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(Constant.CREATEFEED_URL);
                MultipartEntity reqEntity = new MultipartEntity();

                result=db.getUserCreationFeedAttachemnt(Constant.getFeedID(getApplicationContext()));

                try {

                    reqEntity.addPart("feedTitle", new StringBody(item.getFeed_title()));
                    reqEntity.addPart("feedLocationName", new StringBody(item.getFeed_location()));
                    reqEntity.addPart("feedLocationGeometry", new StringBody(item.getFeed_geometry()));
                    reqEntity.addPart("feedCreatedBy",new StringBody(mManager.getUserId().toString()));
                    reqEntity.addPart("feedAttachmentCount", new StringBody(result.size() + ""));
                    File coverfile = new File(item.getFeed_image());
                    FileBody coverbin = new FileBody(coverfile);
                    reqEntity.addPart("feedcoverPic", coverbin);

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                for(int i=0;i< result.size();i++)
                {
                    try {
                    FeedAttachment attachedItem=result.get(i);
                    File file = new File(attachedItem.getFeed_url());
                    FileBody bin = new FileBody(file);
                    reqEntity.addPart("feedMM"+i, bin);
                    reqEntity.addPart("feedType"+i, new StringBody(attachedItem.getFeed_type()+" "));
                    reqEntity.addPart("feedTag"+i,  new StringBody(attachedItem.getFeed_tag()));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                post.setEntity(reqEntity);
                HttpResponse response = client.execute(post);
                resEntity = response.getEntity();
                    response_str = EntityUtils.toString(resEntity);
                    if (resEntity != null) {
                        Log.i("RESPONSE", response_str);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response_str;
        }

        @Override
        protected void onPostExecute(String result) {
            if(pd.isShowing() && pd!=null)
                pd.dismiss();
            try {
                Utils.showToast(EndStory.this,result);
                        Intent i=new Intent(EndStory.this,MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            pd=new ProgressDialog(EndStory.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }
}

package com.expresso.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.expresso.Managers.LoginManager;
import com.expresso.adapter.CommentAdapter;
import com.expresso.adapter.ReplyAdapter;
import com.expresso.model.CommentReply;
import com.expresso.model.FeedComment;
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

/**
 * Created by Rishi M on 7/8/2015.
 */
public class ReplyActivity extends ActionBarActivity implements View.OnClickListener{

    private ImageView iv_userImage;
    private TextView tv_usercomment;
    private ListView replyListview;
    private ReplyAdapter replyAdapter;
    private String comment,userImage;
    private int commentId;
    private EditText et_reply;
    private Button btn_post;
    LoginManager mManager;
    private ArrayList<CommentReply> commentReplyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reply_activity);

        Intent intent =getIntent();
        comment =intent.getStringExtra("Comment");
        userImage=intent.getStringExtra("UserImage");
        commentId=intent.getIntExtra("CommentId", 0);
        new FetchCommentReply().execute();

        getWidgetReferences();
        bindWidgetEvents();
        initialization();
        
    }

    private void initialization() {
        mManager=new LoginManager(ReplyActivity.this);
        commentReplyList=new ArrayList<CommentReply>();
        tv_usercomment.setText(comment);
        if (!userImage.isEmpty() &&userImage!=null) {
            Glide.with(this)
                    .load(userImage)
                    .centerCrop()
                    .placeholder(R.drawable.user)
                    .crossFade()
                    .into(iv_userImage);
        }
    }

    private void bindWidgetEvents() {
        btn_post.setOnClickListener(this);
    }

    private void getWidgetReferences() {
        tv_usercomment= (TextView) findViewById(R.id.tv_usercomment);
        iv_userImage= (ImageView) findViewById(R.id.iv_userImage);
        replyListview= (ListView) findViewById(R.id.replyListview);
        et_reply= (EditText) findViewById(R.id.et_reply);
        btn_post= (Button) findViewById(R.id.btn_post);
    }

    @Override
    public void onClick(View v) {
            if(v==btn_post)
            {
                if(!et_reply.getText().toString().isEmpty())
                    new PostReply(et_reply.getText().toString()).execute(et_reply.getText().toString(),commentId+"");
                else
                    Utils.showToast(ReplyActivity.this,getResources().getString(R.string.add_reply));
            }
    }


    private class FetchCommentReply extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jsonParser=new JSONParser();
            // Building Parameters
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("commentId",commentId+""));
            JSONObject json = jsonParser.getJSONFromUrl(Constant.FETCHFEED_COMMENT_REPLY_URL, parameters);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            Utils.closeProgress();
            try {
                if(json.getString("status").equalsIgnoreCase("Success"))
                {
                   setUpFetchedCommentReplies(json);
                }
                else {
                    Toast.makeText(ReplyActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            Utils.showProgress(ReplyActivity.this,getResources().getString(R.string.pleasewait));
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    private void setUpFetchedCommentReplies(JSONObject json) {
        try {
            Log.e("json",json.toString());
            commentReplyList=new ArrayList<CommentReply>();
            JSONArray resultArray=json.getJSONArray("CommentReply");
            for(int i=0;i<resultArray.length();i++)
            {
                JSONObject jsonResult=resultArray.getJSONObject(i);
                try {
                    JSONObject userReply = jsonResult.getJSONObject("replyUserData");
                    CommentReply item = new CommentReply();
                    item.setReply(jsonResult.getString("reply"));
            //        item.setReplyId(Integer.parseInt(userReply.getString("replyId")));
                    item.setName(userReply.getString("replyCreatedByName"));
                    item.setAvatar(userReply.getString("replyCreatedByAvatar"));
                    commentReplyList.add(item);
                }catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            if(commentReplyList.size()>0) {
                replyAdapter = new ReplyAdapter(ReplyActivity.this, commentReplyList);
                replyListview.setAdapter(replyAdapter);
            }

        } catch (JSONException e) {
        }
    }


    private class PostReply extends AsyncTask<String, Void, JSONObject> {
        String reply;

        public PostReply(String s) {
            reply=s;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jsonParser=new JSONParser();
            // Building Parameters
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair(Constant.COMMENTID,params[1]));
            parameters.add(new BasicNameValuePair(Constant.USERID,mManager.getUserId()));
            parameters.add(new BasicNameValuePair(Constant.REPLY,params[0]));
            parameters.add(new BasicNameValuePair(Constant.FLAG,"1"));
            JSONObject json = jsonParser.getJSONFromUrl(Constant.POST_COMMENT_REPLY_URL, parameters);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            Utils.closeProgress();
            et_reply.setText("");
            try {
                if(json.getString("status").equalsIgnoreCase("Success"))
                {
                    CommentReply item = new CommentReply();
                    item.setReply(reply);
                    item.setReplyId(Integer.parseInt(json.getString("Last ReplyId")));
                    item.setName(mManager.getUserName());
                    item.setAvatar(mManager.getUserAvatar());
                    commentReplyList.add(item);

                    if(commentReplyList.size()==0) {
                        replyAdapter = new ReplyAdapter(ReplyActivity.this, commentReplyList);
                        replyListview.setAdapter(replyAdapter);
                    }
                    else
                        replyAdapter.notifyDataSetChanged();

                }
                else {
                    Toast.makeText(ReplyActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            Utils.showProgress(ReplyActivity.this, getResources().getString(R.string.pleasewait));
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }
}

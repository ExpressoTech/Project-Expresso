package com.expresso.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.expresso.Managers.LoginManager;
import com.expresso.activity.R;
import com.expresso.activity.ReplyActivity;
import com.expresso.database.DatabaseHelper;
import com.expresso.model.FeedComment;
import com.expresso.utils.Constant;
import com.expresso.utils.JSONParser;
import com.expresso.utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akshay M on 4/8/2015.
 */
public class CommentAdapter extends ArrayAdapter<FeedComment> {

    private final Activity activity;
    private DatabaseHelper db;
    private LoginManager mManager;
    private static class ViewHolder {
        TextView tv_username,tv_feedcomment;
        ImageView iv_userImage,iv_reply;
    }
    Context context;
    public CommentAdapter(Activity activity, ArrayList<FeedComment> feedComment) {
        super(activity, R.layout.feed_comment_layout_row, feedComment);
        this.activity=activity;
        db=new DatabaseHelper(activity);
        mManager=new LoginManager(activity);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final FeedComment item = getItem(position);
        ViewHolder holder;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.feed_comment_layout_row, parent, false);
            holder.tv_username= (TextView) convertView.findViewById(R.id.tv_username);
            holder.tv_feedcomment= (TextView) convertView.findViewById(R.id.tv_usercomment);
            holder.iv_userImage= (ImageView) convertView.findViewById(R.id.iv_userImage);
            holder.iv_reply= (ImageView) convertView.findViewById(R.id.iv_reply);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(!item.getComment().equalsIgnoreCase("False")) {
            if (!item.getAvatar().isEmpty() && item.getAvatar()!=null) {
                Glide.with(activity)
                        .load(item.getAvatar())
                        .centerCrop()
                        .placeholder(R.drawable.user)
                        .crossFade()
                        .into(holder.iv_userImage);
            }
            if (!item.getName().isEmpty() && item.getName()!=null)
                holder.tv_username.setText(item.getName());
            if (!item.getComment().isEmpty() && item.getComment()!=null)
                holder.tv_feedcomment.setText(item.getComment());
        }

        holder.iv_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReplyToComment(item);
            }
        });
        return convertView;
    }

    private void addReplyToComment(final FeedComment item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.add_commentview, null);
        builder.setView(dialoglayout);
        builder.setTitle(item.getComment());
        final EditText et_query = (EditText) dialoglayout.findViewById(R.id.et_query);
        et_query.setHint("Add your reply");
        builder.setPositiveButton(R.string.post, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(!et_query.getText().toString().isEmpty())
                {
                    new PostReply(item).execute(et_query.getText().toString(),item.getCommentId()+"");
                }
                else
                    Utils.showToast(activity, "Please enter text");
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        Dialog d= builder.create();
        d.show();
    }


    private class PostReply extends AsyncTask<String, Void, JSONObject> {
        FeedComment item;
        public PostReply(FeedComment item) {
            this.item=item;
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
            try {
                if(json.getString("status").equalsIgnoreCase("Success"))
                {
                    Utils.showToast(activity,activity.getResources().getString(R.string.add_reply_success));
                    Intent replyIntent =new Intent(activity,ReplyActivity.class);
                    replyIntent.putExtra("CommentId",item.getCommentId());
                    replyIntent.putExtra("Comment",item.getComment());
                    replyIntent.putExtra("UserImage",item.getAvatar());
                    activity.startActivity(replyIntent);
                }
                else {
                    Toast.makeText(activity, json.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            Utils.showProgress(activity,activity.getResources().getString(R.string.pleasewait));
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }
}

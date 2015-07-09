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
import com.expresso.model.CommentReply;
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
public class ReplyAdapter extends ArrayAdapter<CommentReply> {

    private final Activity activity;
    private DatabaseHelper db;
    private LoginManager mManager;
    private static class ViewHolder {
        TextView tv_username,tv_feedcomment;
        ImageView iv_userImage,iv_reply;
    }
    Context context;
    public ReplyAdapter(Activity activity, ArrayList<CommentReply> commentReplyList) {
        super(activity, R.layout.feed_comment_layout_row, commentReplyList);
        this.activity=activity;
        db=new DatabaseHelper(activity);
        mManager=new LoginManager(activity);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final CommentReply item = getItem(position);
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
        holder.iv_reply.setVisibility(View.GONE);
        if(!item.getReply().equalsIgnoreCase("False")) {
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
            if (!item.getReply().isEmpty() && item.getReply()!=null)
                holder.tv_feedcomment.setText(item.getReply());
        }

        return convertView;
    }
}

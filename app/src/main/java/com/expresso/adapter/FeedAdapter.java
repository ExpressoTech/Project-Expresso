package com.expresso.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.expresso.activity.BirdView;
import com.expresso.activity.FullScreenViewActivity;
import com.expresso.activity.R;
import com.expresso.database.DatabaseHelper;
import com.expresso.model.Feed;
import com.expresso.utils.Constant;
import com.expresso.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Akshay M on 4/8/2015.
 */
public class FeedAdapter extends ArrayAdapter<Feed> {

    private final Activity activity;
    private DatabaseHelper db;
    private static class ViewHolder {
        TextView tv_username,tv_feedage,tv_feedtitle,tv_feedlocation,tv_feedcomment;
        ImageView iv_userImage,iv_feedimage,iv_birdeye;
    }
    Context context;
    public FeedAdapter(Activity activity, ArrayList<Feed> arr_deals) {
        super(activity, R.layout.feed_layout_row, arr_deals);
        this.activity=activity;
        db=new DatabaseHelper(activity);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Feed item = getItem(position);
        ViewHolder holder;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.feed_layout_row, parent, false);
            holder.tv_username= (TextView) convertView.findViewById(R.id.tv_username);
            holder.tv_feedage= (TextView) convertView.findViewById(R.id.tv_feedage);
            holder.tv_feedtitle= (TextView) convertView.findViewById(R.id.tv_feedtitle);
            holder.tv_feedlocation= (TextView) convertView.findViewById(R.id.tv_feedlocation);
            holder.tv_feedcomment= (TextView) convertView.findViewById(R.id.tv_feedcomment);
            holder.iv_userImage= (ImageView) convertView.findViewById(R.id.iv_userImage);
            holder.iv_feedimage= (ImageView) convertView.findViewById(R.id.iv_feedimage);
            holder.iv_birdeye= (ImageView) convertView.findViewById(R.id.iv_birdeye);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_username.setText(item.getUser_name());
        holder.tv_feedage.setText(Utils.dateDiff(Utils.getDate(item.getFeed_age())));
        holder.tv_feedtitle.setText(item.getFeed_title());
        holder.tv_feedlocation.setText(item.getFeed_location());
/*        holder.tv_feedcomment.setText(item.getFeed_comments());*/
        Log.e("Image",item.getFeed_image());
        Glide.with(activity)
                .load(item.getUser_avatar())
                .centerCrop()
                .placeholder(R.drawable.user)
                .crossFade()
                .into(holder.iv_userImage);

        Glide.with(activity)
                .load(item.getFeed_image())
                .centerCrop()
                .placeholder(R.drawable.please_wait)
                .crossFade()
                .into(holder.iv_feedimage);

        holder.iv_birdeye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    Intent i=new Intent(activity,BirdView.class);
                i.putExtra(Constant.FEEDID,item.getFeedID());
                activity.startActivity(i);*/
                Intent i = new Intent(activity, FullScreenViewActivity.class);
                i.putExtra("position", position);
                i.putExtra(Constant.IMAGEPATH, Utils.getImagePaths(db.getUserFeedAttachemnt(item.getFeedID())));
                activity.startActivity(i);
            }
        });
        return convertView;
    }
}

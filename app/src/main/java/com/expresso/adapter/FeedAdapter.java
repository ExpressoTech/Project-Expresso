package com.expresso.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.expresso.Managers.LoginManager;
import com.expresso.activity.FullScreenViewActivity;
import com.expresso.activity.R;
import com.expresso.activity.StoryBoard;
import com.expresso.activity.UserProfileActivities.UserProfileActivity;
import com.expresso.asynctask.FollowAsynTask;
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
    private LoginManager mManager;
    private FollowAsynTask asynTask;
    private static class ViewHolder {
        TextView tv_username,tv_feedage,tv_feedtitle,tv_feedlocation,tv_feedcomment;
        ImageView iv_userImage,iv_feedimage,iv_birdeye,iv_status,iv_privacy;
        ProgressBar pb_loader;
    }
    Context context;
    public FeedAdapter(Activity activity, ArrayList<Feed> arr_deals) {
        super(activity, R.layout.feed_layout_row, arr_deals);
        this.activity=activity;
        db=new DatabaseHelper(activity);
        mManager=new LoginManager(activity);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            // Get the data item for this position
            final Feed item = getItem(position);
            final ViewHolder holder;
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.feed_layout_row, parent, false);
                holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
                holder.tv_feedage = (TextView) convertView.findViewById(R.id.tv_feedage);
                holder.tv_feedtitle = (TextView) convertView.findViewById(R.id.tv_feedtitle);
                holder.tv_feedlocation = (TextView) convertView.findViewById(R.id.tv_feedlocation);
                holder.tv_feedcomment = (TextView) convertView.findViewById(R.id.tv_feedcomment);
                holder.iv_userImage = (ImageView) convertView.findViewById(R.id.iv_userImage);
                holder.iv_feedimage = (ImageView) convertView.findViewById(R.id.iv_feedimage);
                holder.iv_birdeye = (ImageView) convertView.findViewById(R.id.iv_birdeye);
                holder.iv_status = (ImageView) convertView.findViewById(R.id.iv_status);
                holder.pb_loader = (ProgressBar) convertView.findViewById(R.id.pb_loader);
                holder.iv_privacy = (ImageView) convertView.findViewById(R.id.iv_privacy);

                holder.iv_privacy.setVisibility(View.VISIBLE);
                holder.iv_status.setVisibility(View.VISIBLE);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_username.setText(item.getUser_name());
            holder.tv_feedage.setText(Utils.dateDiff(Utils.getDate(item.getFeed_age())));
            holder.tv_feedtitle.setText(item.getFeed_title());
            holder.tv_feedlocation.setText(item.getFeed_location());
/*        holder.tv_feedcomment.setText(item.getFeed_comments());*/
            Log.e("Image", item.getFeed_image());
            Glide.with(activity)
                    .load(item.getUser_avatar())
                    .placeholder(R.drawable.default_user)
                    .centerCrop()
                    .crossFade()
                    .into(holder.iv_userImage);

            Glide.with(activity)
                    .load(item.getFeed_image())
                    .centerCrop()
                    .crossFade()
                    .into(new GlideDrawableImageViewTarget(holder.iv_feedimage) {
                        @Override
                        public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                            super.onResourceReady(drawable, anim);
                            holder.pb_loader.setVisibility(View.GONE);
                        }
                    });
            if (item.getFeed_privacy() != null) {
                if (item.getFeed_privacy().equalsIgnoreCase(Constant.PUBLIC))
                    holder.iv_privacy.setImageResource(R.drawable.public_icon);
                else if (item.getFeed_privacy().equalsIgnoreCase(Constant.PRIVATE))
                    holder.iv_privacy.setImageResource(R.drawable.private_icon);
                else
                    holder.iv_privacy.setImageResource(R.drawable.public_icon);
            }

            if (item.isFollowingUser())
                holder.iv_status.setImageResource(R.drawable.user_a);
            else
                holder.iv_status.setImageResource(R.drawable.user);


            holder.tv_feedcomment.setText(item.getTotalComment() + " comments");

            holder.iv_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!item.isFollowingUser()) {
                        asynTask = new FollowAsynTask(mManager.getUserId() + "", item.getCreatedById() + "", activity, item, FollowAsynTask.MODEEMAIL, FollowAsynTask.FOLLOW, position);
                        asynTask.execute();
                    } else {
                        new MaterialDialog.Builder(activity)
                                .title(R.string.app_name)
                                .content(activity.getResources().getString(R.string.unfollow_message) + " " + item.getUser_name() + "?")
                                .positiveText(R.string.UNFOLLOW)
                                .negativeText(R.string.CANCEL)
                                .theme(Theme.LIGHT)
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        asynTask = new FollowAsynTask(mManager.getUserId() + "", item.getCreatedById() + "", activity, item, FollowAsynTask.MODEEMAIL, FollowAsynTask.UNFOLLOW, position);
                                        asynTask.execute();
                                    }

                                    @Override
                                    public void onNegative(MaterialDialog dialog) {
                                        dialog.dismiss();
                                    }
                                }).show();


                    }
                }
            });

            holder.iv_birdeye.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, FullScreenViewActivity.class);
                    i.putExtra("position", position);
                    i.putExtra("feedId",item.getFeedID());
                    i.putExtra(Constant.IMAGEPATH, Utils.getImagePaths(db.getUserFeedAttachemnt(item.getFeedID())));
                    activity.startActivity(i);
                }
            });

            holder.tv_username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, UserProfileActivity.class);
                    i.putExtra(Constant.USERID, item.getCreatedById());
                    activity.startActivity(i);
                }
            });

            holder.iv_feedimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, StoryBoard.class);
                    i.putExtra(Constant.FEEDID, item.getFeedID());
                    activity.startActivity(i);
                }
            });


            return convertView;
        }catch (Exception e)
        {
            e.printStackTrace();
            return convertView;
        }
    }
}

package com.expresso.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.expresso.activity.R;
import com.expresso.database.DatabaseHelper;
import com.expresso.model.FeedAttachment;

import java.util.ArrayList;

public class FeedLayerAdapter extends ArrayAdapter<FeedAttachment> {

    private DatabaseHelper db;
    private static class ViewHolder {
        ImageView iv_image;
    }
    Activity activity;
    public FeedLayerAdapter(Activity activity, ArrayList<FeedAttachment> result) {
        super(activity, R.layout.horizontal_list_row, result);
        this.activity=activity;
        db=new DatabaseHelper(activity.getApplicationContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        FeedAttachment item = getItem(position);
        ViewHolder holder;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.horizontal_list_row, parent, false);
            holder.iv_image= (ImageView) convertView.findViewById(R.id.iv_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(!item.getFeed_url().isEmpty()) {
            Glide.with(activity)
                    .load(item.getFeed_url())
                    .centerCrop()
                    .crossFade()
                    .into(holder.iv_image);
        }
         //   holder.iv_image.setImageDrawable(Drawable.createFromPath(item.getFeed_url()));

        return convertView;
    }
}

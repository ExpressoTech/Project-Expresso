package com.expresso.adapter;

/**
 * Created by Rishi M on 6/13/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.expresso.activity.FullScreenViewActivity;
import com.expresso.activity.R;
import com.expresso.model.Feed;
import com.expresso.model.FeedAttachment;
import com.expresso.utils.Constant;
import com.expresso.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BirdViewImageAdapter extends ArrayAdapter<FeedAttachment> {

    private final Activity activity;
    private ArrayList<String> imagePaths;
    private ArrayList<FeedAttachment> result;
    private static class ViewHolder {
        ImageView iv_feedimage;
    }
    Context context;
    public BirdViewImageAdapter(Activity activity, ArrayList<FeedAttachment> result, ArrayList<String> imagePaths) {
        super(activity, R.layout.birdview_layout_row, result);
        this.activity=activity;
        this.result=result;
        this.imagePaths=imagePaths;
        Log.e("Images",imagePaths.toString());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        FeedAttachment item = getItem(position);
        ViewHolder holder;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.birdview_layout_row, parent, false);
            holder.iv_feedimage= (ImageView) convertView.findViewById(R.id.iv_feedimage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String url= item.getFeed_url();
        if(!url.isEmpty() && url!=null)
        {
            Picasso.with(activity).load(url).placeholder(R.drawable.please_wait).into(holder.iv_feedimage);
        }

        holder.iv_feedimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, FullScreenViewActivity.class);
                i.putExtra("position", position);
                i.putExtra(Constant.IMAGEPATH,imagePaths);
                activity.startActivity(i);
            }
        });


        return convertView;
    }
}

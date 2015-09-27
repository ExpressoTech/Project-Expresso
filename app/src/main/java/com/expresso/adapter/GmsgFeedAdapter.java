package com.expresso.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.expresso.activity.R;
import com.expresso.model.GmsgFeed;

import java.util.List;

/**
 * Created by ArunLodhi on 18-07-2015.
 */
public class GmsgFeedAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<GmsgFeed> feedItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public GmsgFeedAdapter(Activity activity, List<GmsgFeed> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = inflater.inflate(R.layout.gmsg_list_item, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView avatar = (NetworkImageView) view.findViewById(R.id.avatar);
        NetworkImageView preview = (NetworkImageView) view.findViewById(R.id.preview);
        TextView msg = (TextView) view.findViewById(R.id.msg);
        TextView username = (TextView) view.findViewById(R.id.username);
        TextView location = (TextView) view.findViewById(R.id.location);
        ImageView iconMedia = (ImageView) view.findViewById(R.id.icon_media);

        // Getting data for the row
        GmsgFeed m = feedItems.get(position);

        // Username
        username.setText(m.getUsername());

        // User image
        avatar.setImageUrl("http://markbaindesign.com/longneck/wp-content/uploads/2013/03/People-Avatar-Set-Rectangular-10.jpg",
                imageLoader);

        preview.setVisibility(View.GONE);
        msg.setVisibility(View.GONE);

        switch (Integer.parseInt(m.getType())) {
            case 0:
                iconMedia.setImageResource(R.drawable.ic_insert_comment_white_48dp);
                msg.setVisibility(View.VISIBLE);
                msg.setText(m.getMsg_text());
                //preview.setVisibility(View.INVISIBLE);
                break;
            case 1:
                iconMedia.setImageResource(R.drawable.ic_collections_white_48dp);
                preview.setVisibility(View.VISIBLE);
                preview.setImageUrl(m.getMedia(), imageLoader);
                msg.setVisibility(View.VISIBLE);
                if (!m.getMsg_text().equals("")) {
                    msg.setText(m.getMsg_text());
                } else msg.setText("Picture GeoMessage");
                break;
            case 2:
                iconMedia.setImageResource(R.drawable.ic_videocam_white_48dp);
                Bitmap videoThumbnail = ThumbnailUtils.createVideoThumbnail(m.getMedia(), MediaStore.Video.Thumbnails.MINI_KIND);
                preview.setVisibility(View.VISIBLE);
                preview.setImageBitmap(videoThumbnail);
                msg.setVisibility(View.VISIBLE);
                if (!m.getMsg_text().equals("")) {
                    msg.setText(m.getMsg_text());
                } else msg.setText("Video GeoMessage");
                break;
        }

        /*
        String genreStr = "";
        for (String str : m.getGenre()) {
            genreStr += str + ", ";
        }
        genreStr = genreStr.length() > 0 ? genreStr.substring(0,
                genreStr.length() - 2) : genreStr;
        genre.setText(genreStr);
        */

        return view;
    }
}

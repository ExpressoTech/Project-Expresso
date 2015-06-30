package com.expresso.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.expresso.activity.FeedPageLocation;
import com.expresso.activity.R;
import com.expresso.model.LocationModel;

import java.util.ArrayList;

/**
 * Created by Anirdesh_And0001 on 27-05-2015.
 */
public class LocationAdapter extends ArrayAdapter<LocationModel>{
    Activity activity;
    ArrayList<LocationModel> result;
    public LocationAdapter(Activity activity, ArrayList<LocationModel> result) {
        super(activity, R.layout.location_layout_row, result);
        this.activity=activity;
        this.result=result;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final LocationModel item = getItem(position);
        ViewHolder holder;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.location_layout_row, parent, false);
            holder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_address= (TextView) convertView.findViewById(R.id.tv_address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.tv_name.setText(item.getLocationName());
        holder.tv_address.setText(item.getLocationAddress());

        return convertView;
    }

    private static class ViewHolder {
        TextView tv_name,tv_address;
    }
}

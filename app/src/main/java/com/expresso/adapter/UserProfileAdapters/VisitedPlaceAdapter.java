package com.expresso.adapter.UserProfileAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.expresso.activity.UserProfileActivities.ListViewPeoples;
import com.expresso.activity.R;
import com.expresso.model.UserProfileModel.VisitedPlaces;

import java.util.ArrayList;


/**
 * Created by rohit on 25/6/15.
 */
public class VisitedPlaceAdapter extends ArrayAdapter<VisitedPlaces> {

    private final Context context;




    private static class ViewHolder {
        TextView tv_placename,tv_date,tv_month,tv_year,tv_with_friends;

    }

    public VisitedPlaceAdapter(Context context, ArrayList<VisitedPlaces> arr_deals) {
        super(context, R.layout.calender_view, arr_deals);
        this.context=context;


    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        VisitedPlaces item = getItem(position);
        ViewHolder holder;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =inflater.inflate(R.layout.calender_view, parent, false);
            holder.tv_placename= (TextView) convertView.findViewById(R.id.placenametextView);
            holder.tv_date= (TextView) convertView.findViewById(R.id.date);
            holder.tv_month= (TextView) convertView.findViewById(R.id.month);
            holder.tv_year= (TextView) convertView.findViewById(R.id.year);
            holder.tv_with_friends = (TextView) convertView.findViewById(R.id.with_friends);



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_placename.setText(item.getVisited_place_name());
        holder.tv_date.setText(item.getDate_of_visit());
        holder.tv_month.setText(item.getMonth_of_visit());
        holder.tv_year.setText(item.getYear_of_visit());

       holder.tv_with_friends.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(context,ListViewPeoples.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
        return convertView;
    }



}

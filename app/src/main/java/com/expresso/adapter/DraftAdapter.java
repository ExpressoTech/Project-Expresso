package com.expresso.adapter;

/**
 * Created by Rishi M on 7/13/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.expresso.Managers.LoginManager;
import com.expresso.activity.R;
import com.expresso.database.DatabaseHelper;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Akshay on 13/3/15.
 */
public class DraftAdapter extends SimpleCursorAdapter {

    private Context mContext;
    private Cursor cursor;
    private LoginManager mManager;
    private SimpleDateFormat sdf,month_date;
    private Calendar calendar;
    public DraftAdapter(Context context, int layout,
                        Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        cursor = c;
        mContext = context;
        mManager=new LoginManager(mContext);
        sdf = new SimpleDateFormat("dd:MM:yyyy");
        month_date = new SimpleDateFormat("MMMM");
        calendar = Calendar.getInstance();
    }

    @Override
    public Cursor swapCursor(Cursor c) {
        cursor = c;
        return super.swapCursor(c);
    }


    public void onItemClick(int position) {
        cursor.moveToPosition(position);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final View view = super.newView(context, cursor, parent);

        ViewHolder viewHolder = new ViewHolder();

        viewHolder.tv_date = (TextView) view.findViewById(R.id.tv_date);
        viewHolder.tv_location = (TextView) view.findViewById(R.id.tv_location);

        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        final int position = cursor.getPosition();
        int day;
        final int feedId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_ID));
        final String locationname = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_LOCATIONNAME));
        final String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_CREATED_AT));
        try {
            calendar.setTime(sdf.parse(date));
            String month_name = month_date.format(calendar.getTime());
            day= calendar.get(Calendar.DAY_OF_MONTH);
            viewHolder.tv_date.setText(day+" "+month_name);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        viewHolder.tv_location.setText(locationname);

    }

    private class ViewHolder {
        TextView tv_location, tv_date;
    }




}
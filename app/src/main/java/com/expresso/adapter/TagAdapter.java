package com.expresso.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.expresso.activity.R;

public class TagAdapter extends ArrayAdapter<String> {

    private Activity activity;
    private String[] tagName;
    private Integer[] tagIcon;

    public TagAdapter(Activity activity, String[] tagName, Integer[] tagIcon) {
        super(activity, R.layout.tag_row, tagName);
        // TODO Auto-generated constructor stub

        this.activity=activity;
        this.tagName=tagName;
        this.tagIcon=tagIcon;
    }

    public View getView(int position,View view,ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(R.layout.tag_row, parent, false);
            holder.iv_tagIcon= (ImageView) view.findViewById(R.id.iv_tagIcon);
            holder.tv_tagName= (TextView) view.findViewById(R.id.tv_tagName);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.iv_tagIcon.setImageResource(tagIcon[position]);
        holder.tv_tagName.setText(tagName[position]);
        return view;
    }


    private static class ViewHolder {
        ImageView iv_tagIcon;
        TextView tv_tagName;
    }
}
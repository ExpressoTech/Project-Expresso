package com.expresso.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.widget.GridView;

import com.expresso.adapter.BirdViewImageAdapter;
import com.expresso.database.DatabaseHelper;
import com.expresso.model.FeedAttachment;
import com.expresso.utils.Constant;
import com.expresso.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Rishi M on 6/13/2015.
 */
public class BirdView extends Activity {
    private int feedId;
    private GridView gridView;
    private int columnWidth;
    private DatabaseHelper db;
    private ArrayList<String> imagePaths;
    private ArrayList<FeedAttachment> result = new ArrayList<FeedAttachment>();
    private BirdViewImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.birdview);
        Intent data=getIntent();
        feedId=data.getIntExtra(Constant.FEEDID,0);
        getWidgetReferences();
        bindWidgetEvents();
        initialization();
    //    InitilizeGridLayout();
        setupStory();
    }

    private void getWidgetReferences() {
        gridView = (GridView) findViewById(R.id.grid_view);
    }
    private void bindWidgetEvents() {
    }
    private void initialization() {
        db=new DatabaseHelper(getApplicationContext());
        imagePaths=new ArrayList<String>();
    }
    private void setupStory() {
        result = db.getUserFeedAttachemnt(feedId);

        // Gridview adapter
        imagePaths=Utils.getImagePaths(result);
        Log.e("Images", imagePaths.toString());
        adapter = new BirdViewImageAdapter(BirdView.this, result,imagePaths);

        // setting grid view adapter
        gridView.setAdapter(adapter);
    }

    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                Constant.GRID_PADDING, r.getDisplayMetrics());

        columnWidth = (int) ((Utils.getScreenWidth(getApplicationContext()) - ((Constant.NUM_OF_COLUMNS + 1) * padding)) / Constant.NUM_OF_COLUMNS);

        gridView.setNumColumns(Constant.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }
}

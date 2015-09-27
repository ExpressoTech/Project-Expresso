package com.expresso.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.expresso.adapter.FullScreenImageAdapter;
import com.expresso.utils.Constant;
import com.expresso.utils.ZoomOutPageTransformer;

import java.util.ArrayList;

public class FullScreenViewActivity extends Activity{

    private FullScreenImageAdapter adapter;
	private ViewPager viewPager;
    ArrayList<String> imagePaths=new ArrayList<String>();
    private int feedID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_view);
		viewPager = (ViewPager) findViewById(R.id.pager);

		Intent i = getIntent();
		int position = i.getIntExtra("position", 0);
        feedID = i.getIntExtra("feedId", 0);
        imagePaths=i.getStringArrayListExtra(Constant.IMAGEPATH);
        Log.e("Images",imagePaths.toString());
        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,imagePaths,feedID);
		viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true,new ZoomOutPageTransformer());
	}
}

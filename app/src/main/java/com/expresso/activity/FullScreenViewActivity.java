package com.expresso.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.expresso.adapter.FullScreenImageAdapter;
import com.expresso.utils.Constant;

import java.util.ArrayList;

public class FullScreenViewActivity extends Activity{

    private FullScreenImageAdapter adapter;
	private ViewPager viewPager;
    ArrayList<String> imagePaths=new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_view);
		viewPager = (ViewPager) findViewById(R.id.pager);

		Intent i = getIntent();
		int position = i.getIntExtra("position", 0);
        imagePaths=i.getStringArrayListExtra(Constant.IMAGEPATH);
        Log.e("Images",imagePaths.toString());
        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,imagePaths);
		viewPager.setAdapter(adapter);

		// displaying selected image first
		viewPager.setCurrentItem(position);
	}
}

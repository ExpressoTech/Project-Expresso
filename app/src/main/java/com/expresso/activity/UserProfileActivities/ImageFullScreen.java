package com.expresso.activity.UserProfileActivities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.expresso.activity.R;
import com.expresso.utils.Constant;
import com.squareup.picasso.Picasso;

/**
 * Created by rohit on 13/7/15.
 */
public class ImageFullScreen extends Activity {
    ImageView imagel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_picture);
        imagel = (ImageView) findViewById(R.id.fullsize);
        Intent intent = getIntent();

        String image = intent.getStringExtra(Constant.USERAVATAR);
        if (image != null && !image.isEmpty()) {
            Picasso.with(ImageFullScreen.this)
                    .load(image)
                    .into(imagel);

        }
    }
    protected void onPause()
    {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, R.anim.splashfadeout);
    }
}

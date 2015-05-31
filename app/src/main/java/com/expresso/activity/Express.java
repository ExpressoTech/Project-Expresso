package com.expresso.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by ArunLodhi on 20-05-2015.
 */
public class Express extends Activity implements View.OnClickListener {
    ImageButton btn_gallery, btn_done;
    ImageView iv, iv_active, thumb_active;
    ImageView[] thumbs = new ImageView[5];
    String ivNum, thumbNum, activePicturePath;
    String[] thumbList = new String[5];
    String[] imgList = new String[5];
    WindowManager wm;
    Display disp;
    Point size = new Point();
    int i = 1;
    int width,height,resId_img,resId_thumb,deltaX,deltaY;
    int[] visibleList = {1,1,1,1,1};
    boolean btnDoneEnabled=false;
    private static final int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.express_screen);
        getWidgetReferences();
        bindWidgetEvents();
        initialization();
        //imgList[i] = onRetainNonConfigurationInstance();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putStringArray("Images", imgList);
        savedInstanceState.putStringArray("Thumbnails", thumbList);
        savedInstanceState.putInt("index", i);
        savedInstanceState.putString("ActivePicPath", activePicturePath);
        savedInstanceState.putBoolean("DoneState", btnDoneEnabled);
        savedInstanceState.putIntArray("VisibleList", visibleList);
        //savedInstanceState.putString("MyString", "Welcome back to Android");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        imgList = savedInstanceState.getStringArray("Images");
        thumbList = savedInstanceState.getStringArray("Thumbnails");
        i = savedInstanceState.getInt("index");
        activePicturePath = savedInstanceState.getString("ActivePicPath");
        btnDoneEnabled = savedInstanceState.getBoolean("DoneState");
        visibleList = savedInstanceState.getIntArray("VisibleList");
        iv_active = getImageView(i);
        thumb_active = getThumbView(i);

        initialization();

        for (int m=1; m<6; m++) {
            if (imgList[m-1] != null) {
                getImageView(m).setImageBitmap(decodeSampledBitmapFromFile(imgList[m-1],(0.5*width),(0.5*height)));
            }
            if (thumbList[m-1] != null) {
                getThumbView(m).setImageBitmap(decodeSampledBitmapFromFile(thumbList[m-1],100,100));
            }
            if (visibleList[m-1] == 1) {
                show(m);
            }
            else if (visibleList[m-1] == 0) {
                hide(m);
            }
        }

    }

    private void getWidgetReferences() {
        btn_gallery = (ImageButton) findViewById(R.id.btn_gallery);
        btn_done = (ImageButton) findViewById(R.id.btn_done);
        wm = getWindowManager();
        disp = wm.getDefaultDisplay();
        for (int j=1; j<6; j++) {
            thumbs[j-1]=getThumbView(j);
        }
    }

    private void bindWidgetEvents() {
        btn_gallery.setOnClickListener(this);
        btn_done.setOnClickListener(this);
        for (int j=1; j<6; j++) {
            thumbs[j-1].setOnClickListener(this);
        }
    }

    private void initialization() {
        btn_done.setEnabled(btnDoneEnabled);
        getWindowManager().getDefaultDisplay().getSize(size);
        width = size.x;
        height = size.y;
    }

    @Override
    public void onClick(View v) {

        //get active imageview --> int i
        iv_active = getImageView(i);
        thumb_active = getThumbView(i);

        //then perform action

        if (v==btn_gallery) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, RESULT_LOAD_IMAGE);
        }
        else if (v==btn_done) {
            //thumb_active.setImageBitmap(BitmapFactory.decodeFile(activePicturePath));
            thumb_active.setImageBitmap(decodeSampledBitmapFromFile(activePicturePath,100,100));
            iv_active.setVisibility(View.GONE);
            //moveToTop(iv_active, i);
            thumbList[i-1] = activePicturePath;
            btnDoneEnabled = false;
            btn_done.setEnabled(false);
            visibleList[i-1] = 0;
            hide(i);
            if (i<5) {
                i += 1;
            }
        }
        else {
            for(int k=1; k<6; k++) {
                visibleList[k-1] = 0;
                hide(k);
                if (v==getThumbView(k)) {
                    i=k;
                    visibleList[k-1] = 1;
                    show(i);
                    btnDoneEnabled = false;
                    btn_done.setEnabled(false);
                }
            }
        }
    }

    public void moveToTop(final View abc, int position) {
        //calculate animation parameters based on position
        deltaX= position;


        AnimationSet as = new AnimationSet(true);

        TranslateAnimation move = new TranslateAnimation(0,deltaX,0,deltaY);
        move.setDuration(500);


        //ScaleAnimation scale = new ScaleAnimation();
        //scale.setDuration(500);

        final int newLeft = abc.getLeft() + deltaX;
        final int newTop = abc.getTop() + deltaY;
        final int newRight = abc.getRight();
        final int newBottom = abc.getBottom();
        //final int finalChange = change; abc.getMeasuredHeight()
        as.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                abc.layout(newLeft, newTop, newRight, newBottom);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        as.addAnimation(move);
        //as.addAnimation(scale);

        abc.startAnimation(as);
    }

    public ImageView getImageView (int pos) {
        ivNum = "express_iv"+ Integer.toString(pos);
        resId_img = getResources().getIdentifier(ivNum,"id",getPackageName());
        iv = (ImageView) findViewById(resId_img);
        return iv;
    }
    public ImageView getThumbView (int pos) {
        thumbNum = "express_thumb"+ Integer.toString(pos);
        resId_thumb = getResources().getIdentifier(thumbNum,"id",getPackageName());
        thumb_active = (ImageView) findViewById(resId_thumb);
        return thumb_active;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            activePicturePath = picturePath;
            //iv_active.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            iv_active.setImageBitmap(decodeSampledBitmapFromFile(picturePath,(0.5*width),(0.5*height)));
            visibleList[i-1] = 1;
            show(i);
            imgList[i-1]=activePicturePath;
            btnDoneEnabled = true;
            btn_done.setEnabled(true);
        }
    }

    public void hide(int position) {
        iv = getImageView(position);
        iv.setVisibility(View.GONE);
    }
    public void show(int position) {
        iv = getImageView(position);
        iv.setVisibility(View.VISIBLE);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
    public static Bitmap decodeSampledBitmapFromFile(String path, double rWidth, double rHeight) {

        int reqWidth = (int) rWidth;
        int reqHeight = (int) rHeight;
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

}

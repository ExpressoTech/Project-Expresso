package com.expresso.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.expresso.adapter.FeedLayerAdapter;
import com.expresso.adapter.TagAdapter;
import com.expresso.database.DatabaseHelper;
import com.expresso.model.FeedAttachment;
import com.expresso.utils.Constant;
import com.expresso.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.HListView;

public class FeedCreationPage extends ActionBarActivity implements View.OnClickListener{
    private ImageView iv_camera,iv_preview,iv_cancel,iv_done,iv_gallery;
    private static final int CAMERA_REQUEST = 1888;
    HListView listView;
    DatabaseHelper db;
    private FeedLayerAdapter adapter;
    ArrayList<FeedAttachment> result;
    private String url;
    private Uri fileUri;
    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int SELECT_PHOTO = 100;
    private int selectedPos=-1,selectedItem;
    private LinearLayout bottom_layout,bottom_layout2;
    private HListView lv_tags;
    private TagAdapter tagAdapter;
    private String selectedtag="";
    private Toolbar toolbar;
    private boolean isFromList=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_page_creation);
        getWidgetReferences();
        bindWidgetEvents();
        initialization();
        setupToolBar();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                try {
                    FeedAttachment item = result.get(position);
                    Glide.with(FeedCreationPage.this)
                            .load(item.getFeed_url())
                            .centerCrop()
                            .crossFade()
                            .into(iv_preview);

                    hideOptions();
                    iv_cancel.setVisibility(View.VISIBLE);
                    selectedPos = item.getFed_pos();
                    selectedItem = position;
                    isFromList= true;
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        lv_tags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                selectedtag=tagAdapter.getItem(pos);
            }
        });
    }

    private void setupToolBar() {
        // set up toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Constant.getFeedLocation(getApplicationContext()));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getWidgetReferences() {
        iv_camera= (ImageView) findViewById(R.id.iv_camera);
        iv_preview= (ImageView) findViewById(R.id.iv_preview);
        iv_cancel= (ImageView) findViewById(R.id.iv_cancel);
        iv_done= (ImageView) findViewById(R.id.iv_done);
        listView= (HListView) findViewById(R.id.hListView1);
        bottom_layout= (LinearLayout) findViewById(R.id.bottom_layout);
        bottom_layout2= (LinearLayout) findViewById(R.id.bottom_layout2);
        lv_tags= (HListView) findViewById(R.id.lv_tags);
        toolbar = (Toolbar) findViewById(R.id.toolbar_header);
        iv_gallery= (ImageView) findViewById(R.id.iv_gallery);
    }

    private void bindWidgetEvents() {
        iv_camera.setOnClickListener(this);
        iv_cancel.setOnClickListener(this);
        iv_done.setOnClickListener(this);
        iv_gallery.setOnClickListener(this);
    }

    private void initialization() {
        db=new DatabaseHelper(getApplicationContext());
        setUpFeedLayers();
        setUpTags();
    }

    private void setUpTags() {
        String[] tagNames=getResources().getStringArray(R.array.tagArray);
        Integer[] tagIcon={
                R.drawable.camer,
                R.drawable.camer,
                R.drawable.camer,
                R.drawable.camer
        };

        tagAdapter=new TagAdapter(this,tagNames,tagIcon);
        lv_tags.setAdapter(tagAdapter);
        lv_tags.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    //    lv_tags.setSelector(R.drawable.tag_list_selector);
    }

    private void setUpFeedLayers() {
        result=new ArrayList<FeedAttachment>();
        result=db.getUserCreationFeedAttachemnt(Constant.getFeedID(getApplicationContext()));
        adapter=new FeedLayerAdapter(this,result);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v==iv_camera)
        {
            bottom_layout2.setVisibility(View.GONE);
            bottom_layout.setVisibility(View.VISIBLE);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, CAMERA_REQUEST);
        }
        if(v==iv_cancel)
        {
            bottom_layout2.setVisibility(View.GONE);
            bottom_layout.setVisibility(View.VISIBLE);
            iv_preview.setImageBitmap(null);
            hideOptions();
            if(result.size()>0 && result!=null) {
            if(isFromList) {
                db.removeAttachment(selectedItem, Constant.getFeedID(getApplicationContext()));
                result.remove(selectedItem);
                adapter.notifyDataSetChanged();
                isFromList=false;
            }
            }
        }
        if(v==iv_done)
        {
            Constant.setCoverPicUrl(getApplicationContext(),url);
            bottom_layout2.setVisibility(View.GONE);
            bottom_layout.setVisibility(View.VISIBLE);
            iv_preview.setImageBitmap(null);
            hideOptions();
            FeedAttachment item=new FeedAttachment();
            item.setFeed_tag(selectedtag);
            item.setFeed_type(Constant.TYPE_IMAGE);
            item.setFeed_url(url);
            item.setFed_pos(getEmptyPosition());
            result.add(item);
            adapter.notifyDataSetChanged();
            db.addFeedAttachment(item, Constant.getFeedID(getApplicationContext()));
            selectedtag="";
        }

        if(v==iv_gallery)
        {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }
    }

    private int getEmptyPosition() {
        int position=-1;
        for(int i=0;i<result.size();i++)
        {
            FeedAttachment item=result.get(i);
            if(item.getFeed_url().isEmpty())
            {
                position=i;
                break;
            }
        }
        return  position;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            url=fileUri.getPath();
            renderPreviewImage(url);
        } else if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            url = getPath(selectedImageUri);
            renderPreviewImage(url);
        }
    }

    //UPDATED!
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String imagePath = cursor.getString(column_index);
        return imagePath;
    }


    private void renderPreviewImage(String fileurl) {
        // Display Bottom Section
        bottom_layout2.setVisibility(View.VISIBLE);
        bottom_layout.setVisibility(View.GONE);
        showOptions();
        Utils.showToast(this,"Before :"+Utils.fileSize(url));
        url= Utils.compressImage(url,this);
        Utils.showToast(this,"After :"+Utils.fileSize(url));
        Glide.with(this)
                .load(url)
                .centerCrop()
                .crossFade()
                .into(iv_preview);
    }

    private void hideOptions()
    {
        iv_done.setVisibility(View.GONE);
        iv_cancel.setVisibility(View.GONE);
    }
    private void showOptions()
    {
        iv_done.setVisibility(View.VISIBLE);
        iv_cancel.setVisibility(View.VISIBLE);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.endStory) {
            Intent intent=new Intent(this,EndStory.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

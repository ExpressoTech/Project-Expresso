package com.expresso.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expresso.model.Feed;
import com.expresso.model.FeedAttachment;
import com.expresso.model.LocationModel;
import com.expresso.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    static SQLiteDatabase db;
    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION =1;

    // Database Name
    private static final String DATABASE_NAME = "ExpressoDB";

    // Table Names
    private static final String TABLE_FEEDATA = "feed";
    private static final String TABLE_FEEDATTACHMENT = "feedAttachment";
    private static final String TABLE_USERS_FEEDATA = "usersFeed";
    private static final String TABLE_USERS_FEEDATTACHMENT = "usersFeedAttachment";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_URL = "url";
    private static final String KEY_TAGS = "tags";
    private static final String KEY_TYPE = "type";
    private static final String KEY_POS = "pos";
    private static final String KEY_CREATED_BY="created_by";
    private static final String KEY_CREATED_AT="created_at";

    //TABLE_FEEDATTACHMENT column names
    private static final String KEY_TITLE="title";
    private static final String KEY_LOCATIONNAME="location_name";
    private static final String KEY_LOCATIONGEMETRY="location_geometry";
    private static final String KEY_FLAG="flag";
    private static final String KEY_COVERPICURL="coverpicurl";
    private static final String KEY_FEEDID="feedID";
    private static final String KEY_CREATED_BY_NAME="name";
    private static final String KEY_CREATED_BY_AVATAR="avatar";


    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_FEEDATTACHMENT = "CREATE TABLE "
            + TABLE_FEEDATTACHMENT + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_FEEDID + " INTEGER,"  + KEY_URL
            + " TEXT," + KEY_TAGS + " TEXT," + KEY_TYPE + " INTEGER," + KEY_POS + " INTEGER )";

    private static final String CREATE_TABLE_FEEDATA = "CREATE TABLE "
            + TABLE_FEEDATA + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_TITLE
            + " TEXT,"+ KEY_COVERPICURL + " TEXT," + KEY_LOCATIONNAME + " TEXT," + KEY_LOCATIONGEMETRY + " TEXT," + KEY_CREATED_BY + " INTEGER,"+KEY_CREATED_AT + " TEXT,"+ KEY_FLAG + " INTEGER)";



    private static final String CREATE_TABLE_USERS_FEEDATTACHMENT = "CREATE TABLE "
            + TABLE_USERS_FEEDATTACHMENT + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_FEEDID + " INTEGER,"  + KEY_URL
            + " TEXT," + KEY_TAGS + " TEXT," + KEY_TYPE + " INTEGER," + KEY_POS + " INTEGER )";

    private static final String CREATE_TABLE_USERS_FEEDATA = "CREATE TABLE "
            + TABLE_USERS_FEEDATA + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_FEEDID + " INTEGER," + KEY_TITLE
            + " TEXT,"+ KEY_COVERPICURL + " TEXT," + KEY_LOCATIONNAME + " TEXT," + KEY_LOCATIONGEMETRY + " TEXT," + KEY_CREATED_BY + " INTEGER,"+KEY_CREATED_BY_NAME + " TEXT,"+KEY_CREATED_BY_AVATAR + " TEXT,"+KEY_CREATED_AT + " TEXT,"+ KEY_FLAG + " INTEGER)";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_FEEDATTACHMENT);
        db.execSQL(CREATE_TABLE_FEEDATA);
        db.execSQL(CREATE_TABLE_USERS_FEEDATTACHMENT);
        db.execSQL(CREATE_TABLE_USERS_FEEDATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDATTACHMENT);
        // create new tables
        onCreate(db);
    }



    public ArrayList<FeedAttachment> getUserCreationFeedAttachemnt(int feedID) {
        ArrayList<FeedAttachment> result = new ArrayList<FeedAttachment>();
        String selectQuery = "SELECT  * FROM " + TABLE_FEEDATTACHMENT +" Where "+ KEY_FEEDID +"="+feedID;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                    FeedAttachment item = new FeedAttachment();
                    item.setFed_pos((c.getInt(c.getColumnIndex(KEY_POS))));
                    item.setFeed_url(c.getString(c.getColumnIndex(KEY_URL)));
                    item.setFeed_tag(c.getString((c.getColumnIndex(KEY_TAGS))));
                    item.setFeed_type(c.getInt((c.getColumnIndex(KEY_TYPE))));
                    // adding to todo list
                    result.add(item);
            } while (c.moveToNext());
        }

        return result;
    }


    public long createFeed(LocationModel item) {
        ContentValues values = new ContentValues();
        values.put(KEY_LOCATIONNAME, item.getLocationName());
        values.put(KEY_LOCATIONGEMETRY,item.getLocationGeometry());
        values.put(KEY_FLAG, 1);
        long id=db.insert(TABLE_FEEDATA, null, values);
        if(id!=-1)
            return id;
        else
            return -1;
    }

    public void addFeedAttachment(FeedAttachment item, int feedID) {
        ContentValues values = new ContentValues();
        values.put(KEY_URL, item.getFeed_url());
        values.put(KEY_TAGS,item.getFeed_tag());
        values.put(KEY_TYPE, item.getFeed_type());
        values.put(KEY_POS, item.getFed_pos());
        values.put(KEY_FEEDID, feedID);
        db.insert(TABLE_FEEDATTACHMENT, null, values);
    }

    public void removeAttachment(int selectedPos, int feedID) {
        String selectQuery = "DELETE FROM " + TABLE_FEEDATTACHMENT +" WHERE "+ KEY_FEEDID+"="+feedID+" AND "+ KEY_POS+"="+selectedPos;
        db.execSQL(selectQuery);
    }

    public void updateFeedData(String title, String coverPicUrl, int feedID) {
        String selectQuery = "UPDATE " + TABLE_FEEDATA + " SET "+KEY_TITLE+"='"+title+"', "+KEY_COVERPICURL+"='"+coverPicUrl+"', "+KEY_CREATED_BY+"=38, "+ KEY_CREATED_AT+"='"+ Utils.currentDate()+"' WHERE "+ KEY_ID+"="+feedID;
        Log.e("Query",selectQuery);
        db.execSQL(selectQuery);
    }

    public Feed getCurrentFeed(int feedID) {
        String selectQuery = "SELECT  * FROM " + TABLE_FEEDATA +" Where "+ KEY_ID +"="+feedID;
        Cursor c = db.rawQuery(selectQuery, null);
        Feed item = new Feed();
        if(c.getCount()>0) {
            if (c.moveToFirst()) {
                item.setFeed_title((c.getString(c.getColumnIndex(KEY_TITLE))));
                item.setFeed_location(c.getString(c.getColumnIndex(KEY_LOCATIONNAME)));
                item.setFeed_geometry(c.getString((c.getColumnIndex(KEY_LOCATIONGEMETRY))));
                item.setFeed_image(c.getString((c.getColumnIndex(KEY_COVERPICURL))));
            }
        }
        else
            return null;

        return item;
    }

    public void saveAllFeeds(JSONObject json) {
        clearAllFeedData();
        try {
            JSONArray resultArray=json.getJSONArray("resultArray");
            for(int i=0;i<resultArray.length();i++) {
                // Storing feed Data
                JSONObject jsonResult = resultArray.getJSONObject(i);
                JSONObject data = jsonResult.getJSONObject("FeedData");
                JSONObject userdata= jsonResult.getJSONObject("feedUserData");
                ContentValues values = new ContentValues();
                values.put(KEY_FEEDID, Integer.parseInt(data.getString("feedId")));
                values.put(KEY_TITLE, data.getString("title"));
                values.put(KEY_COVERPICURL, data.getString("images"));
                values.put(KEY_CREATED_BY, Integer.parseInt(data.getString("userId")));
                values.put(KEY_CREATED_BY_NAME, userdata.getString("FeedCreatedByName"));
                values.put(KEY_CREATED_BY_AVATAR,userdata.getString("FeedCreatedByAvatar"));
                values.put(KEY_CREATED_AT, data.getString("createdAt"));
                values.put(KEY_LOCATIONNAME, data.getString("place"));
                values.put(KEY_LOCATIONGEMETRY, data.getString("feedGeometry"));
                values.put(KEY_FLAG, 1);
                db.insert(TABLE_USERS_FEEDATA, null, values);
                // Storing feed Attachment
                JSONArray jsonFeedArray= null;
                    jsonFeedArray = jsonResult.getJSONArray("FeedAttachment");
                    for(int j=0;j<jsonFeedArray.length();j++)
                    {
                            JSONObject item=jsonFeedArray.getJSONObject(j);
                            ContentValues feedvalues = new ContentValues();
                            feedvalues.put(KEY_FEEDID, Integer.parseInt(item.getString("feedId")));
                            feedvalues.put(KEY_TYPE, Integer.parseInt(item.getString("type")));
                            feedvalues.put(KEY_TAGS, item.getString("tag"));
                            feedvalues.put(KEY_URL, item.getString("url"));
                            db.insert(TABLE_USERS_FEEDATTACHMENT, null, feedvalues);
                    }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void clearAllFeedData() {
        String selectQuery = "DELETE FROM " + TABLE_USERS_FEEDATA;
        Log.e("Query",selectQuery);
        db.execSQL(selectQuery);

        String selectQuery2 = "DELETE FROM " + TABLE_USERS_FEEDATTACHMENT;
        Log.e("Query2",selectQuery2);
        db.execSQL(selectQuery2);
    }

    public Feed getUserFeed(int feedID) {
        String selectQuery = "SELECT  * FROM " + TABLE_USERS_FEEDATA +" Where "+ KEY_FEEDID +"="+feedID;
        Cursor c = db.rawQuery(selectQuery, null);
        Feed item = new Feed();
        if(c.getCount()>0) {
            if (c.moveToFirst()) {
                item.setFeed_title((c.getString(c.getColumnIndex(KEY_TITLE))));
                item.setFeed_location(c.getString(c.getColumnIndex(KEY_LOCATIONNAME)));
                item.setFeed_geometry(c.getString((c.getColumnIndex(KEY_LOCATIONGEMETRY))));
                item.setFeed_image(c.getString((c.getColumnIndex(KEY_COVERPICURL))));
                item.setFeed_age((c.getString(c.getColumnIndex(KEY_CREATED_AT))));
                item.setFeedID(c.getInt(c.getColumnIndex(KEY_FEEDID)));
                item.setUser_name(c.getString((c.getColumnIndex(KEY_CREATED_BY_NAME))));
                item.setUser_avatar(c.getString((c.getColumnIndex(KEY_CREATED_BY_AVATAR))));
            }
        }
        else
            return null;

        return item;
    }

    public ArrayList<FeedAttachment> getUserFeedAttachemnt(int feedID) {
        ArrayList<FeedAttachment> result = new ArrayList<FeedAttachment>();
        String selectQuery = "SELECT  * FROM " + TABLE_USERS_FEEDATTACHMENT +" Where "+ KEY_FEEDID +"="+feedID;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                FeedAttachment item = new FeedAttachment();
                item.setFeed_url(c.getString(c.getColumnIndex(KEY_URL)));
                item.setFeed_tag(c.getString((c.getColumnIndex(KEY_TAGS))));
                item.setFeed_type(c.getInt((c.getColumnIndex(KEY_TYPE))));
                // adding to todo list
                result.add(item);
            } while (c.moveToNext());
        }

        return result;
    }
}

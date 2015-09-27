package com.expresso.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expresso.Managers.LoginManager;
import com.expresso.model.Feed;
import com.expresso.model.FeedAttachment;
import com.expresso.model.LocationModel;
import com.expresso.model.Users;
import com.expresso.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    static SQLiteDatabase db;
    // Logcat tag
    public static final String LOG = "DatabaseHelper";

    // Database Version
    public static final int DATABASE_VERSION =1;

    // Database Name
    public static final String DATABASE_NAME = "ExpressoDB";

    // Table Names
    public static final String TABLE_FEEDATA = "feed";
    public static final String TABLE_FEEDATTACHMENT = "feedAttachment";
    public static final String TABLE_USERS_FEEDATA = "usersFeed";
    public static final String TABLE_USERS_FEEDATTACHMENT = "usersFeedAttachment";
    public static final String TABLE_FOLLOWERS = "followers";
    public static final String TABLE_FOLLOWINGS = "followings";
    public static final String KEY_STATUS="status";
    // Common column names
    public static final String KEY_ID = "id";
    public static final String KEY_URL = "url";
    public static final String KEY_TAGS = "tags";
    public static final String KEY_TYPE = "type";
    public static final String KEY_POS = "pos";
    public static final String KEY_CREATED_BY="created_by";
    public static final String KEY_CREATED_AT="created_at";

    //TABLE_FEEDATTACHMENT column names
    public static final String KEY_TITLE="title";
    public static final String KEY_LOCATIONNAME="location_name";
    public static final String KEY_LOCATIONGEMETRY="location_geometry";
    public static final String KEY_FLAG="flag";
    public static final String KEY_COVERPICURL="coverpicurl";
    public static final String KEY_FEEDID="feedID";
    public static final String KEY_CREATED_BY_NAME="name";
    public static final String KEY_CREATED_BY_AVATAR="avatar";
    public static final String KEY_PRIVACY="privacy";


    //TABLE_FOLLOWING/FOLLOWERS column names
    public static final String KEY_FRIENDID="friendId";
    public static final String KEY_FOLLOWING_STATUS="f_status";
    public static final String KEY_NAME="name";
    public static final String KEY_AVATAR = "avatar";


    // Table Create Statements
    // Todo table create statement
    public static final String CREATE_TABLE_FEEDATTACHMENT = "CREATE TABLE "
            + TABLE_FEEDATTACHMENT + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_FEEDID + " INTEGER,"  + KEY_URL
            + " TEXT," + KEY_TAGS + " TEXT," + KEY_TYPE + " INTEGER," + KEY_POS + " INTEGER )";

    public static final String CREATE_TABLE_FEEDATA = "CREATE TABLE "
            + TABLE_FEEDATA + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_TITLE
            + " TEXT,"+ KEY_COVERPICURL + " TEXT," + KEY_LOCATIONNAME + " TEXT," + KEY_LOCATIONGEMETRY + " TEXT," + KEY_CREATED_BY + " INTEGER,"+KEY_CREATED_AT + " TEXT,"+ KEY_FLAG + " INTEGER,"+KEY_PRIVACY+" TEXT,"+ KEY_STATUS + " INTEGER)";

    public static final String CREATE_TABLE_USERS_FEEDATTACHMENT = "CREATE TABLE "
            + TABLE_USERS_FEEDATTACHMENT + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_FEEDID + " INTEGER,"  + KEY_URL
            + " TEXT," + KEY_TAGS + " TEXT," + KEY_TYPE + " INTEGER," + KEY_POS + " INTEGER )";

    public static final String CREATE_TABLE_USERS_FEEDATA = "CREATE TABLE "
            + TABLE_USERS_FEEDATA + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_FEEDID + " INTEGER," + KEY_TITLE
            + " TEXT,"+ KEY_COVERPICURL + " TEXT," + KEY_LOCATIONNAME + " TEXT," + KEY_LOCATIONGEMETRY + " TEXT," + KEY_CREATED_BY + " INTEGER,"+KEY_CREATED_BY_NAME + " TEXT,"+KEY_CREATED_BY_AVATAR + " TEXT,"+KEY_CREATED_AT + " TEXT,"+ KEY_FLAG + " INTEGER)";

    public static final String CREATE_TABLE_FOLLOWINGUSER = "CREATE TABLE "
            + TABLE_FOLLOWINGS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"+ KEY_FRIENDID + " INTEGER," + KEY_FOLLOWING_STATUS + " TEXT,"+ KEY_NAME + " TEXT,"+ KEY_AVATAR + " TEXT)";

    public static final String CREATE_TABLE_FOLLOWERUSER = "CREATE TABLE "
            + TABLE_FOLLOWERS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_FRIENDID + " INTEGER," + KEY_FOLLOWING_STATUS + " TEXT,"+ KEY_NAME + " TEXT,"+ KEY_AVATAR + " TEXT)";

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
        db.execSQL(CREATE_TABLE_FOLLOWINGUSER);
        db.execSQL(CREATE_TABLE_FOLLOWERUSER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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


    public long createFeed(LocationModel item, LoginManager mManager) {
        ContentValues values = new ContentValues();
        values.put(KEY_LOCATIONNAME, item.getLocationName());
        values.put(KEY_LOCATIONGEMETRY,item.getLocationGeometry());
        values.put(KEY_FLAG, 1);
        values.put(KEY_STATUS, 0);
        values.put(KEY_CREATED_AT,Utils.currentDate());
        values.put(KEY_CREATED_BY, Integer.parseInt(mManager.getUserId()));
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
        String selectQuery = "UPDATE " + TABLE_FEEDATA + " SET "+KEY_TITLE+"='"+title+"', "+KEY_COVERPICURL+"='"+coverPicUrl+"', "+ KEY_CREATED_AT+"='"+ Utils.currentDate()+"' WHERE "+ KEY_ID+"="+feedID;
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
                            JSONObject attacheditem=item.getJSONObject("FeedAttachment");
                            ContentValues feedvalues = new ContentValues();
                            feedvalues.put(KEY_FEEDID, Integer.parseInt(attacheditem.getString("feedId")));
                            feedvalues.put(KEY_TYPE, Integer.parseInt(attacheditem.getString("type")));
                            feedvalues.put(KEY_TAGS, attacheditem.getString("tag"));
                            feedvalues.put(KEY_URL, attacheditem.getString("url"));
                            db.insert(TABLE_USERS_FEEDATTACHMENT, null, feedvalues);
                    }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void clearAllFeedData() {
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
                item.setCreatedById(c.getInt(c.getColumnIndex(KEY_CREATED_BY)));
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

    public void followingUser(String friendId,String status, String user_name, String user_avatar) {
        ContentValues values = new ContentValues();
        values.put(KEY_FRIENDID,Integer.parseInt(friendId));
        values.put(KEY_FOLLOWING_STATUS, status);
        values.put(KEY_NAME, user_name);
        values.put(KEY_AVATAR, user_avatar);
        db.insert(TABLE_FOLLOWINGS, null, values);
    }

    public void unfollowUser(String userId, String friendId) {
        String selectQuery = "UPDATE " + TABLE_FOLLOWINGS + " SET "+KEY_STATUS+"=0 WHERE "+KEY_FRIENDID+"="+Integer.parseInt(friendId);
        Log.e("Query",selectQuery);
        db.execSQL(selectQuery);
    }

    public void updateFeedDraftStatus(int feedID,int status) {
        String selectQuery = "UPDATE " + TABLE_FEEDATA + " SET "+KEY_STATUS+"="+status+" WHERE "+ KEY_ID+"="+feedID;
        Log.e("Query",selectQuery);
        db.execSQL(selectQuery);
    }

    public Cursor getFeedCursor() {
     return db.rawQuery("Select rowid _id,* from " + TABLE_FEEDATA + " WHERE " + KEY_STATUS + " = 0 ORDER BY " + KEY_CREATED_AT +" DESC", null);
    }

    public void followerUser(String friendId, String followingStatus, String username, String useravatar) {
        ContentValues values = new ContentValues();
        values.put(KEY_FRIENDID,Integer.parseInt(friendId));
        values.put(KEY_FOLLOWING_STATUS, followingStatus);
        values.put(KEY_NAME, username);
        values.put(KEY_AVATAR, useravatar);
        db.insert(TABLE_FOLLOWERS, null, values);
    }

    public void clearAllUsers() {
        String selectQuery = "DELETE FROM " + TABLE_FOLLOWINGS;
        Log.e("Query",selectQuery);
        db.execSQL(selectQuery);

        String selectQuery2 = "DELETE FROM " + TABLE_FOLLOWERS;
        Log.e("Query2",selectQuery2);
        db.execSQL(selectQuery2);
    }

    public Cursor getUserCursor() {
        return db.rawQuery("Select rowid _id,* from " + TABLE_FOLLOWINGS, null);
    }

    public ArrayList<Users> getAllUsers() {
        ArrayList<Users> result = new ArrayList<Users>();
        String selectQuery = "SELECT  * FROM " + TABLE_FOLLOWINGS;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Users item = new Users();
                item.setUserId(c.getInt(c.getColumnIndex(KEY_FRIENDID)));
                item.setUsername(c.getString(c.getColumnIndex(KEY_NAME)));
                item.setUserAvatar(c.getString(c.getColumnIndex(KEY_AVATAR)));
                result.add(item);
            } while (c.moveToNext());
        }

        return result;
    }
}

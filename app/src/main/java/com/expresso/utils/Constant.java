package com.expresso.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Akshay on 02/05/2015.
 */
public class Constant {


        public static final String SERVERURL = "http://expresso.netne.net/expresso/";
    public static final String SIGNINURL = SERVERURL+"signin.php";
    public static final String SIGNUPURL = SERVERURL+"signup.php";
    public static final String LOCATIONAPI="https://maps.googleapis.com/maps/api/place/search/json?";
    public static final String LOCATIONURL2 ="&radius=2000&types=restaurant&sensor=false&key=AIzaSyAB-dwfPHYylEYEUn0Bg74lB1ogi-jCKBs" ;
    public static final String CREATEFEED_URL = SERVERURL+"createFeeds.php";
    public static final String FETCHFEEDSURL = SERVERURL+"fetchFeed.php";
    public static final String IMAGEPATH = "imagepath";
    public static final String AVATAR = "Avatar" ;


    public static String USEREMAIL="userEmail";
    public static String USERPASSWORD="password";
    public static final String USERNAME = "userName";
    public static String FEEDID="feedID";
    public static String FEEDLOCATION="feedLocation";
    public static String FEEDCOVERPIC="feedCoverpic";

    public static void setFeedID(Context context, long feedID) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(FEEDID,(int)feedID).commit();
    }

    public static int getFeedID(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(FEEDID,0);
    }

    public static void saveLocation(Context context, String s) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(FEEDLOCATION, s).commit();
    }
    public static String getFeedLocation(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(FEEDLOCATION, "");
    }

    public static void setCoverPicUrl(Context context, String url) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(FEEDCOVERPIC, url).commit();
    }

    public static String getCoverPicUrl(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(FEEDCOVERPIC, "");
    }


    // Number of columns of Grid View
    public static final int NUM_OF_COLUMNS = 3;

    // Gridview image padding
    public static final int GRID_PADDING = 8; // in dp

    // SD card image directory
    public static final String PHOTO_ALBUM = "express";

    // supported file formats
    public static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg","png");
}

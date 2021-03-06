package com.expresso.Managers;

import android.content.Context;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rishi M on 6/20/2015.
 */
public class LoginManager {

    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String USERID ="userid" ;
    private static final String ISLOGGEDIN = "isloggedin";
    private static final String USERAVATAR = "userAvatar";
    public static final String DEVICETOKEN = "deviceToken";

    private Context context;

    public LoginManager(Context context)
    {
        this.context=context;
    }

    public void saveUserInfo(JSONObject result) {
        try {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString(NAME,result.getString(NAME)).commit();
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString(EMAIL,result.getString(EMAIL)).commit();
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString(USERID,result.getString(USERID)).commit();
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString(USERAVATAR,result.getString(USERAVATAR)).commit();
            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(ISLOGGEDIN,true).commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isLoggedIn()
    {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(ISLOGGEDIN,false);
    }

    public String getUserName()
    {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(NAME,"");
    }

    public String getUserEmail()
    {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(EMAIL,"");
    }

    public String getUserId()
    {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(USERID,"");
    }

    public String getUserAvatar()
    {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(USERAVATAR,"");
    }


    public String getDeviceToken() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(DEVICETOKEN,"");
    }

    public boolean isMyStory(int createdById) {
        if(Integer.parseInt(getUserId())==createdById)
            return true;
        else
            return false;
    }
}

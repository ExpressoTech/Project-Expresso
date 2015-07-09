package com.expresso.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;
import org.json.JSONObject;


import com.expresso.Managers.LoginManager;
import com.expresso.utils.Constant;
import com.expresso.utils.JSONParser;
import com.expresso.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akshay M on 4/5/2015.
 */
public class LandingScreen extends Activity implements View.OnClickListener{
    Button btn_signin,btn_signup;
    LinearLayout facebookLinearButton,googlePlusLinearButton;
    private SocialAuthAdapter adapter;
    private Profile profileMap;
    private String signupmode;
    private LoginManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_screen);
        getWidgetReferences();
        bindWidgetEvents();
        initialization();
        if(mManager.getDeviceToken().isEmpty())
            Utils.registerInBackground(LandingScreen.this);

        checkForLoggedInUsers();
    }

    private void checkForLoggedInUsers() {
        if(new LoginManager(getApplicationContext()).isLoggedIn())
        {
            finish();
            Intent i=new Intent(LandingScreen.this,MainActivity.class);
            startActivity(i);
        }
        else
        {

        }
    }

    private void getWidgetReferences() {
        btn_signin= (Button) findViewById(R.id.btn_signin);
        btn_signup= (Button) findViewById(R.id.btn_signup);
        facebookLinearButton = (LinearLayout) findViewById(R.id.facebookLinearButton);
        googlePlusLinearButton= (LinearLayout) findViewById(R.id.googlePlusLinearButton);
    }

    private void bindWidgetEvents() {
        btn_signin.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        facebookLinearButton.setOnClickListener(this);
        googlePlusLinearButton.setOnClickListener(this);
    }

    private void initialization() {
        adapter = new SocialAuthAdapter(new ResponseListener());
        mManager=new LoginManager(this);
    }

    @Override
    public void onClick(View v) {
        if(v==btn_signin)
        {
            Intent i=new Intent(LandingScreen.this,SignIn.class);
            startActivity(i);
        }
        else if(v==btn_signup)
        {
            Intent i=new Intent(LandingScreen.this,SignUp.class);
            startActivity(i);
        }
        else if(v==facebookLinearButton)
        {
            adapter.authorize(LandingScreen.this, SocialAuthAdapter.Provider.FACEBOOK);
        }
        else if(v==googlePlusLinearButton)
        {
            adapter.authorize(LandingScreen.this, SocialAuthAdapter.Provider.GOOGLE);
        }

    }


    private final class ResponseListener implements DialogListener
    {
        public void onComplete(Bundle values) {
            adapter.getUserProfileAsync(new ProfileDataListener());
        }

        @Override
        public void onError(SocialAuthError socialAuthError) {
            Log.e("Error",socialAuthError.getMessage().toString());
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onBack() {

        }
    }

    // To receive the profile response after authentication
    private final class ProfileDataListener implements SocialAuthListener<Profile> {
        @Override
        public void onExecute(String s, Profile profile) {

            Log.e("SocialAuthAdapter.Provider.FACEBOOK", SocialAuthAdapter.Provider.FACEBOOK.toString());
            profileMap = profile;
            Log.d("Custom-UI", "Validate ID         = " + profileMap.getValidatedId());
            Log.d("Custom-UI", "First Name          = " + profileMap.getFirstName());
            Log.d("Custom-UI", "Last Name           = " + profileMap.getLastName());
            Log.d("Custom-UI", "Email               = " + profileMap.getEmail());
            Log.d("Custom-UI", "Gender                   = " + profileMap.getGender());
            Log.d("Custom-UI", "Country                  = " + profileMap.getCountry());
            Log.d("Custom-UI", "Language                 = " + profileMap.getLanguage());
            Log.d("Custom-UI", "Location                 = " + profileMap.getLocation());
            Log.d("Custom-UI", "Profile Image URL  = " + profileMap.getProfileImageURL());
            new SignUpTask().execute();
        }

        @Override
        public void onError(SocialAuthError socialAuthError) {
        }
    }

    private class SignUpTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jsonParser=new JSONParser();
            JSONObject json;
            // Building Parameters
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair(Constant.USEREMAIL, profileMap.getEmail()));
            parameters.add(new BasicNameValuePair(Constant.USERNAME,profileMap.getFirstName()+" "+profileMap.getLastName()));
            parameters.add(new BasicNameValuePair(Constant.AVATAR,profileMap.getProfileImageURL()));
            parameters.add(new BasicNameValuePair(Constant.PLATFORM,Constant.ANDROID_PLATFORM));
            parameters.add(new BasicNameValuePair(Constant.MODE,SocialAuthAdapter.Provider.FACEBOOK.toString()));
            parameters.add(new BasicNameValuePair(Constant.SOCIALMEDIAID,profileMap.getValidatedId()));
            parameters.add(new BasicNameValuePair(LoginManager.DEVICETOKEN,mManager.getDeviceToken()));
            json = jsonParser.getJSONFromUrl(Constant.SIGNUPURL, parameters);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            Utils.closeProgress();
            try {
                if(result.getString("status").equalsIgnoreCase("Success"))
                {
                    mManager.saveUserInfo(result);
                    finish();
                    Intent i=new Intent(LandingScreen.this,MainActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
           Utils.showProgress(LandingScreen.this,getResources().getString(R.string.pleasewait));
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

}

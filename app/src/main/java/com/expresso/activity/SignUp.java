package com.expresso.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.expresso.Managers.LoginManager;
import com.expresso.utils.Constant;
import com.expresso.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akshay M on 4/5/2015.
 */
public class SignUp extends Activity implements View.OnClickListener{
    private static final int SELECT_PHOTO = 11;
    Button btn_signup;
    EditText et_signup_email,et_signup_passwd,et_signup_first_name,et_signup_last_name;
    ProgressDialog pd;
    ImageView iv_userImage;
    private String fileurl="";
    private LoginManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);
        getWidgetReferences();
        bindWidgetEvents();
        initialization();
    }

    private void getWidgetReferences() {
        btn_signup= (Button) findViewById(R.id.btn_signup);
        et_signup_email= (EditText) findViewById(R.id.et_signup_email);
        et_signup_passwd= (EditText) findViewById(R.id.et_signup_passwd);
        et_signup_first_name= (EditText) findViewById(R.id.et_signup_first_name);
        iv_userImage= (ImageView) findViewById(R.id.iv_userImage);
    }

    private void bindWidgetEvents() {
        btn_signup.setOnClickListener(this);
        iv_userImage.setOnClickListener(this);
    }

    private void initialization() {
        pd=new ProgressDialog(this);
        mManager=new LoginManager(this);
    }

    @Override
    public void onClick(View v) {
        if(v==btn_signup) {
            new SignUpTask().execute();
        }
        if(v==iv_userImage)
        {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }
    }


    private class SignUpTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jsonParser=new JSONParser();
            JSONObject json;
            // Building Parameters
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair(Constant.USEREMAIL, et_signup_email.getText().toString()));
            parameters.add(new BasicNameValuePair(Constant.USERPASSWORD, et_signup_passwd.getText().toString()));
            parameters.add(new BasicNameValuePair(Constant.USERNAME, et_signup_first_name.getText().toString()));
            parameters.add(new BasicNameValuePair(LoginManager.DEVICETOKEN,mManager.getDeviceToken()));
            if(!fileurl.equalsIgnoreCase(""))
                json = jsonParser.getJSONWithImageUpload(Constant.SIGNUPURL, parameters,fileurl);
            else
                json = jsonParser.getJSONFromUrl(Constant.SIGNUPURL, parameters);

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if(pd.isShowing() && pd!=null)
                pd.dismiss();
            try {
                if(result.getString("status").equalsIgnoreCase("Success"))
                {
                    finish();
                    Intent i=new Intent(SignUp.this,MainActivity.class);
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
            pd.setMessage("Loading");
            pd.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            fileurl = getPath(selectedImageUri);
            iv_userImage.setImageDrawable(Drawable.createFromPath(fileurl));
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String imagePath = cursor.getString(column_index);
        return imagePath;
    }

}
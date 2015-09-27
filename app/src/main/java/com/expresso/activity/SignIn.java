package com.expresso.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.expresso.utils.Constant;
import com.expresso.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.expresso.Managers.LoginManager;

/**
 * Created by Akshay M on 4/5/2015.
 */
public class SignIn extends Activity implements View.OnClickListener{
    Button btn_signin;
    EditText et_login_email,et_login_passwd;
    ProgressDialog pd;
    private LoginManager mManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        getWidgetReferences();
        bindWidgetEvents();
        initialization();
    }

    private void getWidgetReferences() {
        btn_signin= (Button) findViewById(R.id.btn_signin);
        et_login_email= (EditText) findViewById(R.id.login_email);
        et_login_passwd= (EditText) findViewById(R.id.login_passwd);
    }

    private void bindWidgetEvents() {
        btn_signin.setOnClickListener(this);
    }

    private void initialization() {
        mManager=new LoginManager(this);
        pd=new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        if(!et_login_email.getText().toString().isEmpty() && !et_login_passwd.getText().toString().isEmpty())
            new SignInTask().execute();
    }


    private class SignInTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jsonParser=new JSONParser();
            // Building Parameters
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair(Constant.USEREMAIL, et_login_email.getText().toString()));
            parameters.add(new BasicNameValuePair(Constant.USERPASSWORD, et_login_passwd.getText().toString()));
            JSONObject json = jsonParser.getJSONFromUrl(Constant.SIGNINURL, parameters);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if(pd.isShowing() && pd!=null)
                    pd.dismiss();
            try {
                if(result.getString("status").equalsIgnoreCase("Success"))
                {
                    mManager.saveUserInfo(result);
                    finish();
                    Intent i=new Intent(SignIn.this,MainActivity.class);
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
}
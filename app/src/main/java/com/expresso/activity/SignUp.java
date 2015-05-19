package com.expresso.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

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
    Button btn_signup;
    EditText et_signup_email,et_signup_passwd,et_signup_first_name,et_signup_last_name;
    ProgressDialog pd;
    CheckBox chkbox_showPass;
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
        chkbox_showPass= (CheckBox) findViewById(R.id.checkbox_showPass);
    }

    private void bindWidgetEvents() {
        btn_signup.setOnClickListener(this);
        et_signup_passwd.setTypeface(Typeface.DEFAULT);
        chkbox_showPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    int pos = et_signup_passwd.getSelectionStart();
                    et_signup_passwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_signup_passwd.setSelection(pos);
                    et_signup_passwd.setTypeface(Typeface.DEFAULT);
                    //et_signup_passwd.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    int pos = et_signup_passwd.getSelectionStart();
                    et_signup_passwd.setInputType(129);
                    et_signup_passwd.setSelection(pos);
                    et_signup_passwd.setTypeface(Typeface.DEFAULT);
                    //et_signup_passwd.setTransformationMethod(null);
                }
            }
        });
    }

    private void initialization() {
        pd=new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        new SignUpTask().execute();
    }

    private class SignUpTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jsonParser=new JSONParser();
            // Building Parameters
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair(Constant.USEREMAIL, et_signup_email.getText().toString()));
            parameters.add(new BasicNameValuePair(Constant.USERPASSWORD, et_signup_passwd.getText().toString()));
            parameters.add(new BasicNameValuePair(Constant.USERNAME, et_signup_first_name.getText().toString()));
            JSONObject json = jsonParser.getJSONFromUrl(Constant.SIGNUPURL, parameters);
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
}
package com.expresso.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Akshay M on 4/5/2015.
 */
public class LandingScreen extends Activity implements View.OnClickListener{
    Button btn_signin,btn_signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_screen);
        getWidgetReferences();
        bindWidgetEvents();
        initialization();
    }

    private void getWidgetReferences() {
        btn_signin= (Button) findViewById(R.id.btn_signin);
        btn_signup= (Button) findViewById(R.id.btn_signup);
    }

    private void bindWidgetEvents() {
        btn_signin.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
    }

    private void initialization() {
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

    }
}

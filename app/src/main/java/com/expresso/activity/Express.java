package com.expresso.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by ArunLodhi on 20-05-2015.
 */
public class Express extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_screen);
        getWidgetReferences();
        bindWidgetEvents();
        initialization();
    }

    private void getWidgetReferences() {
    }

    private void bindWidgetEvents() {
    }

    private void initialization() {
    }

    @Override
    public void onClick(View v) {

    }
}

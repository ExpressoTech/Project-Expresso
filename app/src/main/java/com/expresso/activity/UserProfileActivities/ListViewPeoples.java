package com.expresso.activity.UserProfileActivities;

import android.app.Activity;
import android.os.Bundle;


import com.expresso.activity.R;

/**
 * Created by rohit on 1/7/15.
 */
public class ListViewPeoples extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_display);
    }
//    private void getWidgetReferences() {
//    }
//
//    private void bindWidgetEvents() {
//        listview_people= (ListView)findViewById(R.id.listview_visitedplace);
//
//    }
//    private void initialization() {
//        res=getResources();//Explain me why is line
//        VisitedList=new ArrayList<VisitedPlaces>();
//        adpater=new VisitedPlaceAdapter(this,VisitedList);
//        listview_people.setAdapter(adpater);
//    }
protected void onPause()
{
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in,  R.anim.splashfadeout);
}
}

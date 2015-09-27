package com.expresso.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.expresso.Managers.FeedManager;
import com.expresso.Managers.LoginManager;
import com.expresso.adapter.DraftAdapter;
import com.expresso.database.DatabaseHelper;
import com.expresso.utils.Utils;

/**
 * Created by Rishi M on 7/12/2015.
 */
public class ViewFeedDrafts extends ActionBarActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private DraftAdapter draftAdapter;
    private ListView lv_drafts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_drafts);
        getWidgetReferences();
        bindWidgetEvents();
        initialization();
        setupToolBar();
        setListAdapter();

    }


    private void setupToolBar() {
        // set up toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.viewdrafts);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getWidgetReferences() {
        toolbar= (Toolbar) findViewById(R.id.toolbar_header);
        lv_drafts= (ListView) findViewById(R.id.lv_drafts);
    }

    private void bindWidgetEvents() {
    }

    private void initialization() {
    }

    @Override
    public void onClick(View v) {

    }

    private void setListAdapter() {
        Cursor cursor = new FeedManager(this).getCursor();
        if (draftAdapter == null) {
            if(cursor!=null && cursor.getCount()>0) {
                draftAdapter = new DraftAdapter(ViewFeedDrafts.this,
                        R.layout.list_item_draft, cursor, new String[]{},
                        new int[]{}, 0);
                lv_drafts.setAdapter(draftAdapter);
            }
            else
                Utils.showToast(this,getResources().getString(R.string.no_draft_message));
        }
    }


}

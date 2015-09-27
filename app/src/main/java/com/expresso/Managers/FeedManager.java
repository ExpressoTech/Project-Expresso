package com.expresso.Managers;

import android.content.Context;
import android.database.Cursor;

import com.expresso.database.DatabaseHelper;
import com.expresso.model.Feed;
import com.expresso.model.LocationModel;
import com.expresso.utils.Constant;

/**
 * Created by Rishi M on 7/12/2015.
 */
public class FeedManager {
    DatabaseHelper db;
    private Context mContext;
    public FeedManager(Context context)
    {
        mContext=context;
        db=new DatabaseHelper(context);
    }

    public long createFeed(LocationModel item, LoginManager mManager) {
        return db.createFeed(item,mManager);
    }

    public void updateFeedData(String title, String coverPicUrl, int feedID) {
        db.updateFeedData(title,coverPicUrl,feedID);
    }

    public void updateFeedDraftStatus(int feedID, int status) {
        db.updateFeedDraftStatus(feedID,status);
    }

    public Cursor getCursor() {
        Cursor cursor = null;
        cursor = db.getFeedCursor();
        return cursor;
    }

    public Feed getFeedItembyID(int feedID) {
        return db.getUserFeed(feedID);
    }
}

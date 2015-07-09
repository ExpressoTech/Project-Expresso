package com.expresso.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;

/**
 * Created by Rishi M on 7/7/2015.
 */
public class FeedComment implements Parcelable {

    int commentId,userId,feedId;
    String comment;
    String datetime;

    String name;
    String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public int getCommentId() {
        return commentId;
    }

    public int getUserId() {
        return userId;
    }

    public int getFeedId() {
        return feedId;
    }

    public String getComment() {
        return comment;
    }

    public String getDatetime() {
        return datetime;
    }


    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFeedId(int feedId) {
        this.feedId = feedId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.name,
                this.comment,
                this.avatar});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public FeedComment createFromParcel(Parcel in) {
            return new FeedComment(in);
        }

        public FeedComment [] newArray(int size) {
            return new FeedComment [size];
        }
    };

    public FeedComment (Parcel in){
        String[] data = new String[3];
        in.readStringArray(data);
        this.name = data[0];
        this.comment = data[1];
        this.avatar = data[2];
    }
    public FeedComment()
    {

    }
}

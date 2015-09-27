package com.expresso.model;

/**
 * Created by Akshay on 4/8/2015.
 */
public class Feed {
    String feed_age;
    String user_name;
    String user_avatar;
    String feed_title;
    String feed_image;
    String feed_views;
    String feed_location;
    String feed_geometry;
    int totalComment;

    String feed_privacy;
    int feedID;
    boolean isFollowingUser;
    int createdById;

    public int getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(int totalComment) {
        this.totalComment = totalComment;
    }



    public String getFeed_privacy() {
        return feed_privacy;
    }

    public void setFeed_privacy(String feed_privacy) {
        this.feed_privacy = feed_privacy;
    }




    public int getCreatedById() {
        return createdById;
    }

    public void setCreatedById(int createdById) {
        this.createdById = createdById;
    }

    public boolean isFollowingUser() {
        return isFollowingUser;
    }

    public void setFollowingUser(boolean isFollowingUser) {
        this.isFollowingUser = isFollowingUser;
    }


    public void setFeed_age(String feed_age) {
        this.feed_age = feed_age;
    }

    public String getFeed_age() {
        return feed_age;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setFeed_title(String feed_title) {
        this.feed_title = feed_title;
    }

    public String getFeed_title() {
        return feed_title;
    }

    public void setFeed_views(String feed_views) {
        this.feed_views = feed_views;
    }

    public String getFeed_views() {
        return feed_views;
    }

    public void setFeed_image(String feed_image) {
        this.feed_image = feed_image;
    }

    public String getFeed_image() {
        return feed_image;
    }

    public void setFeed_location(String feed_location) {
        this.feed_location = feed_location;
    }

    public String getFeed_location() {
        return feed_location;
    }

    public void setFeed_geometry(String feed_geometry) {
        this.feed_geometry = feed_geometry;
    }

    public String getFeed_geometry() {
        return feed_geometry;
    }

    public void setFeedID(int feedID) {
        this.feedID = feedID;
    }

    public int getFeedID() {
        return feedID;
    }
}

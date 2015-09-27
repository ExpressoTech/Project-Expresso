package com.expresso.model.UserProfileModel;

/**
 * Created by rohit on 6/7/15.
 */
public class Following {
    String user_name, user_avatar, following_status;
    int user_id;

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

    public void setUser_ID(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setFollowing_status(String following_status) {
        this.following_status = following_status;
    }

    public String getFollowing_status() {
        return following_status;
    }
}

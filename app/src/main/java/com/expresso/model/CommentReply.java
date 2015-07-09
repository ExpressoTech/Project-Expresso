package com.expresso.model;

/**
 * Created by Rishi M on 7/9/2015.
 */
public class CommentReply {
    int replyId,userId;
    String reply,name,avatar,datetime;

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDatetime() {
        return datetime;
    }

    public int getReplyId() {
        return replyId;
    }

    public int getUserId() {
        return userId;
    }

    public String getReply() {
        return reply;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

}

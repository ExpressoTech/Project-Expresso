package com.expresso.model;

public class FeedAttachment {
    String feed_url,feed_tag;
    int feed_type,fed_pos;

    public void setFeed_url(String feed_url)
    {
        this.feed_url=feed_url;
    }
    public String getFeed_url()
    {
        return feed_url;
    }

    public void setFeed_tag(String feed_tag)
    {
        this.feed_tag=feed_tag;
    }
    public String getFeed_tag()
    {
        return feed_tag;
    }

    public void setFeed_type(int feed_type)
    {
        this.feed_type=feed_type;
    }
    public int getFeed_type()
    {
        return feed_type;
    }

    public void setFed_pos(int fed_pos)
    {
        this.fed_pos=fed_pos;
    }
    public int getFed_pos()
    {
        return fed_pos;
    }
}

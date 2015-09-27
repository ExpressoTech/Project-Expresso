package com.expresso.model;

/**
 * Created by ArunLodhi on 18-07-2015.
 */
public class GmsgFeed {
    private String type, msg_text, media, username;
    private Double lat, lng, distance;
    private int radius;

    public GmsgFeed() {
    }

    public GmsgFeed(String type, String msg_text, String media, String username, Double lat, Double lng, Double distance, int radius) {
        this.type = type;
        this.msg_text = msg_text;
        this.lat = lat;
        this.lng = lng;
        this.distance = distance;
        this.radius = radius;
        this.username = username;
    }

    public String getType() {return type;}
    public String getMsg_text() {return msg_text;}
    public String getMedia() {return media;}
    public String getUsername() {return username;}
    public Double getLat(){return lat;}
    public Double getLng() {return lng;}
    public Double getDistance() {return distance;}
    public int getRadius() {return radius;}

    public void setType(String type) {this.type = type;}
    public void setMsg_text(String msg_text) {this.msg_text = msg_text;}
    public void setMedia(String media) {this.media = media;}
    public void setUsername(String username) {this.username = username;}
    public void setLat(Double lat) {this.lat = lat;}
    public void setLng(Double lng) {this.lng = lng;}
    public void setDistance(Double distance) {this.distance = distance;}
    public void setRadius(int radius) {this.radius = radius;}
}

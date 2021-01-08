package com.lsc.content.distribution.twitter.model;

public class TwitterImageResponse {


    private long media_id;
    private String media_id_string;
    private int size;
    private int expires_after_secs;
    private TwitterImage image;

    public TwitterImageResponse() {
    }

    public String getMedia_id_string() {
        return media_id_string;
    }

    public void setMedia_id_string(String media_id_string) {
        this.media_id_string = media_id_string;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getExpires_after_secs() {
        return expires_after_secs;
    }

    public void setExpires_after_secs(int expires_after_secs) {
        this.expires_after_secs = expires_after_secs;
    }

    public TwitterImage getImage() {
        return image;
    }

    public void setImage(TwitterImage image) {
        this.image = image;
    }

    public long getMedia_id() {
        return media_id;
    }

    public void setMedia_id(long media_id) {
        this.media_id = media_id;
    }
}

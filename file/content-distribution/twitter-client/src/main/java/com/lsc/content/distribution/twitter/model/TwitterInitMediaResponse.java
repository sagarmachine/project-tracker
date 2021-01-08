package com.lsc.content.distribution.twitter.model;

public class TwitterInitMediaResponse {

    private long media_id;
    private String media_id_string;
    private int expires_after_secs;
    private String media_key;

    public TwitterInitMediaResponse() {
    }


    public String getMedia_key() {
        return media_key;
    }

    public void setMedia_key(String media_key) {
        this.media_key = media_key;
    }

    public long getMedia_id() {
        return media_id;
    }

    public void setMedia_id(long media_id) {
        this.media_id = media_id;
    }

    public String getMedia_id_string() {
        return media_id_string;
    }

    public void setMedia_id_string(String media_id_string) {
        this.media_id_string = media_id_string;
    }

    public int getExpires_after_secs() {
        return expires_after_secs;
    }

    public void setExpires_after_secs(int expires_after_secs) {
        this.expires_after_secs = expires_after_secs;
    }
}

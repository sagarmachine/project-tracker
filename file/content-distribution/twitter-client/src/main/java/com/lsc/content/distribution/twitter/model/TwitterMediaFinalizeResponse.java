package com.lsc.content.distribution.twitter.model;
import java.util.HashMap;

public class TwitterMediaFinalizeResponse {


    private long media_id;
    private String media_id_string;
    private long size;
    private long expires_after_secs;
    private String media_key;
    private Object image;


    public String getMedia_key() {
        return media_key;
    }

    public void setMedia_key(String media_key) {
        this.media_key = media_key;
    }

    HashMap<String, String> video;


    HashMap<String, String> processing_info;


    public TwitterMediaFinalizeResponse(long media_id, String media_id_string, long size, long expires_after_secs, String media_key, Object image, HashMap<String, String> video, HashMap<String, String> processing_info) {
        this.media_id = media_id;
        this.media_id_string = media_id_string;
        this.size = size;
        this.expires_after_secs = expires_after_secs;
        this.media_key = media_key;
        this.image = image;
        this.video = video;
        this.processing_info = processing_info;
    }

    public TwitterMediaFinalizeResponse() {
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public HashMap<String, String> getProcessing_info() {
        return processing_info;
    }

    public void setProcessing_info(HashMap<String, String> processing_info) {
        this.processing_info = processing_info;
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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getExpires_after_secs() {
        return expires_after_secs;
    }

    public void setExpires_after_secs(long expires_after_secs) {
        this.expires_after_secs = expires_after_secs;
    }

    public HashMap<String, String> getVideo() {
        return video;
    }

    public void setVideo(HashMap<String, String> video) {
        this.video = video;
    }

    //    {
//        "media_id": 1323257720493932544,
//            "media_id_string": "1323257720493932544",
//            "size": 1071430,
//            "expires_after_secs": 86400,
//            "video": {
//        "video_type": "video/mp4"
//    }
//    }

}

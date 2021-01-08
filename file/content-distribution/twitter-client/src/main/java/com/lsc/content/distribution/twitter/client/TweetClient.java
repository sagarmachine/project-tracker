package com.lsc.content.distribution.twitter.client;

import com.lsc.content.distribution.twitter.ContentTwitterException;
import com.lsc.content.distribution.twitter.model.TwitterImageResponse;
import com.lsc.content.distribution.twitter.model.TwitterInitMediaResponse;
import com.lsc.content.distribution.twitter.model.TwitterMediaFinalizeResponse;
import org.springframework.http.*;
import org.springframework.social.OperationNotPermittedException;
import org.springframework.social.UncategorizedApiException;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

public class TweetClient {

    static final String TWITTER_UPLOAD_STATUS = "https://api.twitter.com/1.1/statuses/update.json";
    static final String TWITTER_UPLOAD_MEDIA = "https://upload.twitter.com/1.1/media/upload.json";
    static final long MAX_CHUNK_SIZE = 5120000L;
    static final long MAX_IMAGE_SIZE = 5120000L;
    static final HashMap<String, String> MIME_TYPES = new HashMap<String, String>() {
        {
            put(".mov", "video/quicktime");
            put(".avi", "video/x-msvideo");
            put(".wmv", "video/x-ms-wmv");
            put(".m4v", "video/mp4");
            put(".mp4", "video/mp4");
            put(".gif", "image/gif");

        }
    };

    TwitterTemplate twitterTemplate;
    String consumerId;
    String consumerSecret;
    String accessToken;
    String accessSecret;

    public TweetClient(String consumerId, String consumerKeySecret, String accessToken, String accessSecret) {
        this.consumerId = consumerId;
        this.consumerSecret = consumerKeySecret;
        this.accessToken = accessToken;
        this.accessSecret = accessSecret;
        twitterTemplate = new TwitterTemplate(consumerId, consumerSecret, accessToken, accessSecret);
    }


    // tweet status
    public String tweet(String status) {
        status = status.trim();
        RestOperations ro = twitterTemplate.getRestTemplate();
        TwitterInitMediaResponse twitterMediaResponse = null;
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("status", status);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, httpHeaders);
        try {
            return ro.exchange(TWITTER_UPLOAD_STATUS, HttpMethod.POST, request, HashMap.class).getBody().get("id").toString();
        } catch (ResourceAccessException exception) {
            throw new ContentTwitterException("Authentication Failed - check your credentials");
        } catch (OperationNotPermittedException exception) {
            throw new ContentTwitterException(exception.getMessage());
        } catch (Exception exception) {
            throw new ContentTwitterException("something went wrong", exception);
        }
    }

    // tweet upto 5 images (maximum 5mb each)
    public Object tweet(MultipartFile[] files) throws IOException {
        int count = 0;
        String mediaIds = "";
        validateSize(files);
        while (count < files.length && count <= 3) {
            if (count == 0)
                mediaIds = uploadImage(files[count], twitterTemplate);
            else
                mediaIds += "," + uploadImage(files[count], twitterTemplate);
            count++;
        }
        RestOperations ro = twitterTemplate.getRestTemplate();
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        try {
            return ro.exchange(TWITTER_UPLOAD_STATUS + "?media_ids=" + mediaIds, HttpMethod.POST, null, HashMap.class).getBody().get("id").toString();
        } catch (ResourceAccessException exception) {
            throw new ContentTwitterException("Authentication Failed - check your credentials");
        } catch (Exception exception) {
            throw new ContentTwitterException("something went wrong", exception);
        }
    }


    // tweet upto 4 images (maximum 5mb each) and status
    public Object tweet(MultipartFile[] files, String status) throws IOException {
        status = status.trim();
        int count = 0;
        String mediaIds = "";
        validateSize(files);
        while (count < files.length) {
            if (count == 0)
                mediaIds = uploadImage(files[count], twitterTemplate);
            else
                mediaIds += "," + uploadImage(files[count], twitterTemplate);
            count++;
        }
        RestOperations ro = twitterTemplate.getRestTemplate();
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("status", status);
        map.add("mediaIds", mediaIds);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, httpHeaders);
        try {
            return ro.exchange(TWITTER_UPLOAD_STATUS + "?media_ids=" + mediaIds, HttpMethod.POST, request, HashMap.class).getBody().get("id").toString();
        } catch (ResourceAccessException exception) {
            throw new ContentTwitterException("Authentication Failed - check your credentials");
        } catch (OperationNotPermittedException exception) {
            throw new ContentTwitterException(exception.getMessage());
        } catch (Exception exception) {
            throw new ContentTwitterException("something went wrong", exception);
        }
    }


    // tweet a video (maximum 512MB) and status
    public Object tweet(MultipartFile file, String status) throws IOException, InterruptedException {
        String mediaId = initMedia(file);
        appendMedia(file, mediaId);
        finalizeMedia(mediaId);
        RestOperations ro = twitterTemplate.getRestTemplate();
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("status", status);
        map.add("mediaIds", mediaId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, httpHeaders);
        try {
            return ro.exchange(TWITTER_UPLOAD_STATUS + "?media_ids=" + mediaId, HttpMethod.POST, request, HashMap.class).getBody().get("id").toString();
        } catch (ResourceAccessException exception) {
            throw new ContentTwitterException("Authentication Failed - check your credentials");
        } catch (OperationNotPermittedException exception) {
            throw new ContentTwitterException(exception.getMessage());
        } catch (Exception exception) {
            throw new ContentTwitterException("something went wrong", exception);
        }
    }

    // tweet a video (maximum 512MB)
    public String tweet(MultipartFile file) throws IOException, InterruptedException {
        String mediaId = initMedia(file);
        appendMedia(file, mediaId);
        finalizeMedia(mediaId);
        RestOperations ro = twitterTemplate.getRestTemplate();
        try {
            return ro.exchange(TWITTER_UPLOAD_STATUS + "?media_ids=" + mediaId, HttpMethod.POST, null, HashMap.class).getBody().get("id").toString();
        } catch (HttpClientErrorException.BadRequest exception) {
            throw new ContentTwitterException("Invalid Video - https://developer.twitter.com/en/docs/twitter-api/v1/media/upload-media/uploading-media/media-best-practices");
        } catch (ResourceAccessException exception) {
            throw new ContentTwitterException("Invalid Video - https://developer.twitter.com/en/docs/twitter-api/v1/media/upload-media/uploading-media/media-best-practices");
        } catch (Exception exception) {
            throw new ContentTwitterException("some thing went wrong", exception);
        }
    }


    private String initMedia(MultipartFile file) {
        long total_bytes = file.getSize();
        if(!MIME_TYPES.containsValue(file.getContentType()))
            throw  new ContentTwitterException("unrecognized type");
        if (total_bytes > 512000000)
            throw new ContentTwitterException("media size exceeding 512MB");
        TwitterInitMediaResponse twitterMediaResponse = null;
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("total_bytes", total_bytes + "");
        map.add("command", "INIT");
        map.add("media_type", file.getContentType());
        if(!file.getContentType().equals("image/gif"))
             map.add("media_category", "TweetVideo");
        else
        if (total_bytes > MAX_IMAGE_SIZE)
            throw new ContentTwitterException("gif size exceeding 5MB");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, httpHeaders);
        try {
            twitterMediaResponse = twitterTemplate.restOperations().exchange(TWITTER_UPLOAD_MEDIA
                    , HttpMethod.POST, request, TwitterInitMediaResponse.class).getBody();
        } catch (ResourceAccessException exception) {
            throw new ContentTwitterException("Authentication Failed - check your credentials", exception);
        } catch (Exception exception) {
            throw new ContentTwitterException("some thing went wrong", exception);
        }
        String mediaId = twitterMediaResponse.getMedia_id_string();
        return mediaId;
    }


    private void appendMedia(MultipartFile multipartFile, String mediaId) throws IOException {
        chuckAppend(multipartFile, mediaId);
    }


    private void chuckAppend(MultipartFile file, String mediaId) throws IOException {
        final int CHUNK_SIZE = (int) MAX_CHUNK_SIZE;
        byte[] bytes = file.getBytes();
        int offset = 0;
        for (int segmentIndex = 0; segmentIndex < 1000; segmentIndex++) {
         //   System.out.println(segmentIndex+1);
            if (offset == bytes.length) break;
            byte[] chunk = null;
            if (offset + CHUNK_SIZE < bytes.length)
                chunk = Arrays.copyOfRange(bytes, offset, offset + CHUNK_SIZE);
            else
                chunk = Arrays.copyOfRange(bytes, offset, bytes.length);
            offset += chunk.length;
            String encoded = Base64.getEncoder().encodeToString(chunk);
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("media_data", encoded); // chunked
            map.add("command", "APPEND");
            map.add("segment_index", segmentIndex + "");
            map.add("media_id", mediaId + "");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, httpHeaders);
            try {
                twitterTemplate.restOperations().exchange(TWITTER_UPLOAD_MEDIA
                        , HttpMethod.POST
                        , request
                        , Object.class);
            } catch (Exception exception) {
                throw new ContentTwitterException("something went wrong", exception);
            }
        }
    }


    private void finalizeMedia(String mediaId) throws InterruptedException {
        TwitterMediaFinalizeResponse twitterMediaFinalizeResponse = null;
        boolean finalized = false;
        while (!finalized) {
            try {
                twitterMediaFinalizeResponse = twitterTemplate.restOperations().exchange(TWITTER_UPLOAD_MEDIA + "?command=FINALIZE&media_id=" + mediaId, HttpMethod.POST, null, TwitterMediaFinalizeResponse.class).getBody();
            } catch (UncategorizedApiException exception) {
                throw new ContentTwitterException("Invalid Video - https://developer.twitter.com/en/docs/twitter-api/v1/media/upload-media/uploading-media/media-best-practices", exception);
            } catch (Exception exception) {
                throw new ContentTwitterException("some thing went wrong", exception);
            }
            if (twitterMediaFinalizeResponse.getProcessing_info() == null || !twitterMediaFinalizeResponse.getProcessing_info().containsKey("check_after_secs")) {
                finalized = true;
            } else {
             //   System.out.println("delay "+twitterMediaFinalizeResponse.getProcessing_info().get("check_after_secs"));
                Thread.sleep(Integer.parseInt(twitterMediaFinalizeResponse.getProcessing_info().get("check_after_secs")) * 1000);
            }
        }
    }

    private String uploadImage(MultipartFile file, TwitterTemplate twitterTemplate) {
        validateFileSizeAndType(file);
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("media", file.getResource());
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map);
        TwitterImageResponse twitterImageResponse = null;
        try {
            twitterImageResponse = twitterTemplate.restOperations().exchange(TWITTER_UPLOAD_MEDIA, HttpMethod.POST, request, TwitterImageResponse.class).getBody();
        } catch (UncategorizedApiException exception) {
        } catch (ResourceAccessException exception) {
            throw new ContentTwitterException("Authentication Failed - check your credentials");
        } catch (Exception exception) {
            throw new ContentTwitterException("something went wrong", exception);
        }
        return twitterImageResponse.getMedia_id_string();
    }


    private void validateSize(MultipartFile[] files) {
        if (files.length > 4)
            throw new ContentTwitterException("Number of files exceeding 4");
        if (files.length == 0)
            throw new ContentTwitterException("At least one image required");
    }

    private void validateFileSizeAndType(MultipartFile file) {
        if (file.getSize() > MAX_IMAGE_SIZE)
            throw new ContentTwitterException("One or more file size exceeding 5mb");
        String type = file.getContentType();
        switch (type) {
            case "image/jpeg":
            case "image/jpg":
            case "image/png":
            case "image/webp":
                break;
            default:
                throw new ContentTwitterException("One or more file type is invalid  (types allowed- jpeg, jpg, png, webp)");
        }
    }
}
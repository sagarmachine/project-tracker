package com.lsc.content.distribution.contentdistribution;

import com.lsc.content.distribution.twitter.ContentTwitterException;
import com.lsc.content.distribution.twitter.client.TwitterConnectClient;
import com.lsc.content.distribution.twitter.model.TwitterInitMediaResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.social.UncategorizedApiException;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

public class TwitterMediaUpload {

    TwitterTemplate  twitterTemplate =null;

    static final String TWITTER_UPLOAD_STATUS = "https://api.twitter.com/1.1/statuses/update.json";
    static final String TWITTER_UPLOAD_MEDIA = "https://upload.twitter.com/1.1/media/upload.json";
    static final long MAX_CHUNK_SIZE = 5120000L;
    static final long MAX_IMAGE_SIZE = 5120000L;

    public TwitterMediaUpload(TwitterTemplate twitterTemplate) {
        this.twitterTemplate = twitterTemplate;
    }



    public  String uploadMdia(MultipartFile multipartFile) throws IOException {
       String mediaId= initMedia(multipartFile);
        appendMedia(multipartFile,mediaId);
        return mediaId;
    }

    private String initMedia(MultipartFile file) {

        long total_bytes = file.getSize();
        if (total_bytes > 512000000)
            throw new ContentTwitterException("media size exceeding 512MB");
        TwitterInitMediaResponse twitterMediaResponse = null;
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("total_bytes", total_bytes + "");
        map.add("command", "INIT");
        map.add("media_type", file.getContentType());
        // map.add("media_category", "TweetGif");
        map.add("media_category", "TweetVideo");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, httpHeaders);
        try {
            twitterMediaResponse = twitterTemplate.restOperations().exchange(TWITTER_UPLOAD_MEDIA
                    , HttpMethod.POST, request, TwitterInitMediaResponse.class).getBody();
        } catch (ResourceAccessException exception) {
            throw new ContentTwitterException("Authentication Failed - check your credentials", exception);
        } catch (Exception exception) {
            throw new ContentTwitterException("EXCEPTION ,exception " + exception.getClass(), exception);
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
            } catch (UncategorizedApiException exception) {
                throw new ContentTwitterException("Media Not Satisfying Twitter's standards ", exception);
            } catch (ResourceAccessException exception) {
                throw new ContentTwitterException("Authentication Failed - check your credentials");
            } catch (Exception exception) {
                throw new ContentTwitterException("EXCEPTION", exception);
            }
        }

    }





}

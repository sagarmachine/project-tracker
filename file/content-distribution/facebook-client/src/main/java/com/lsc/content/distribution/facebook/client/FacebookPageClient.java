package com.lsc.content.distribution.facebook.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.*;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

//import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FacebookPageClient {

         String  appNamespace;
         String    appId;


    @Autowired
    FacebookConnectionFactory facebookConnectionFactory;


    public String postMessage(String accessToken, String pageId, String message) {
        FacebookTemplate facebookTemplate= new FacebookTemplate(accessToken);

        PostData pagePostData= new PostData(pageId);
        pagePostData.message(message);

        return  facebookTemplate.feedOperations().post(pagePostData);
    }


    public String postLink(String accessToken, String pageId, String message, String linkUrl) {


        FacebookTemplate facebookTemplate = new FacebookTemplate(accessToken);

        PostData pagePostData = new PostData(pageId);
        pagePostData.message(message);

        pagePostData.link(linkUrl,null,null,null,null);

       // facebookTemplate.;

        return facebookTemplate.feedOperations().post(pagePostData);
    }
//
//
//
//
//
    public String postPhoto(String accessToken, String albumId, Resource photo, String caption) {

        FacebookTemplate facebookTemplate = new FacebookTemplate(accessToken);


       // PostData pagePostData = new PostData(albumId);

       // String postId=postUnpublishedPhoto(accessToken, albumId, photo, caption);

        //pagePostData.toRequestParameters()

        MultiValueMap<String, Object> multiValueMap=new LinkedMultiValueMap<>();
        multiValueMap.add("caption",caption);
        multiValueMap.add("photo",photo);

       return  facebookTemplate.publish(albumId,"photos",multiValueMap);
        //return  postId;

    }
//
// feed
//
//
//    public String postPhoto(String userPhotosToken, String targetId, Resource photo, String caption,
//
//                            boolean isPublished) {
//
//    }
//
//
    public String postPhoto(String userPhotosToken, String targetId, String photoUrl, String caption,

                            boolean isPublished) {
        FacebookTemplate facebookTemplate = new FacebookTemplate(userPhotosToken);
facebookTemplate.setApiVersion("8.0");

        //PostData pagePostData = new PostData(targetId);


        MultiValueMap<String, Object> multiValueMap=new LinkedMultiValueMap<>();
        multiValueMap.add("caption",caption);
       multiValueMap.add("url",photoUrl);
//        multiValueMap.add("access_token",userPhotosToken);




         return  facebookTemplate.publish(targetId,"photos",multiValueMap);
//return  facebookTemplate.getRestTemplate().postForObject("https://graph.facebook.com/v8.0/"+targetId+"/photos",multiValueMap,String.class);
       // RestTemplate restTemplate= new RestTemplate();

//        return restTemplate.postForObject("https://graph.facebook.com/v8.0/"+targetId+"/photos",new HashMap<String, String>(){{  put("url",photoUrl);
//                                                                                                                           put("access_token",userPhotosToken);}},String.class);

          }
//
//
//

    public String publishMultiPhotoPost(String userPhotosToken, String targetId, String message,

                                        List<String> attachedMediaIds) {
return null;
    }


    public String publishMultiPhotoPost(String userPhotosToken, String targetId,

                                        Map<Resource, String> photoCaptionMap, String message) {

        String photoIds="";

        Set<Resource> set = photoCaptionMap.keySet();

        for (Resource resource :set) {
            MultiValueMap params= new LinkedMultiValueMap();
            params.add("photo",resource);
          //  params.add("message",message);
            params.add("caption",photoCaptionMap.get(resource));


            photoIds += postData(userPhotosToken,targetId,"photos",params);
        }


        return  photoIds;

    }





    private String postUnpublishedPhoto(String userPhotosToken, String targetId, Resource photo,

                                        String caption) {

        MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();

        data.add("photo", photo);

        data.add("caption", caption);

        data.add("published", "false");

        String postId = "";

        if(targetId == null || targetId.equals(""))

            postId=postData(userPhotosToken, "me", "photos", data);

        else

            postId=postData(userPhotosToken, targetId, "photos", data);

        return postId;

    }

    public String postData(String accessToken, String targetId,

                           String connectionType, MultiValueMap<String, Object> data) {

        FacebookTemplate facebookTemplate= new FacebookTemplate(accessToken);

       return  facebookTemplate.publish(targetId,connectionType,data);


    }
}

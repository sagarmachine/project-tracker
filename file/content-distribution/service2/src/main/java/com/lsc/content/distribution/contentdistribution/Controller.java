package com.lsc.content.distribution.contentdistribution;


import com.lsc.content.distribution.facebook.client.FacebookConnectClient;
import com.lsc.content.distribution.facebook.client.FacebookPageClient;
//import com.lsc.content.distribution.facebook.client.FacebookUserClient;
import com.lsc.content.distribution.facebook.model.FacebookUser;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;

@RestController
public class Controller {


    FacebookConnectClient facebookConnectClient= new FacebookConnectClient("http://localhost:8081/callback","pages_show_list,pages_manage_posts");
    FacebookPageClient facebookPageClient=new FacebookPageClient();
//    FacebookUserClient facebookUserClient= new FacebookUserClient();

    @GetMapping("/oauth")
    public ModelAndView oauth(){

        return facebookConnectClient.connectFacebook();

    }

    @GetMapping("/callback")
    public String  callback(@RequestParam("code")String code) throws IOException {

        return code;
    }

    @GetMapping("/detail/{code}")
    public FacebookUser oauth(@PathVariable("code")String code) throws IOException {

        return facebookConnectClient.connectCallback(code);
    }

//    @GetMapping("/test")
//    public Object oauth(@RequestParam("accessToken")String accessToken,@RequestParam("pageId")String pageId,@RequestParam("message")String message) throws IOException {
//
//        return facebookPageClient.test(accessToken,pageId,message);
//    }

//    @GetMapping("/user")
//    public Object getUserDetails(@RequestParam("accessToken")String accessToken,@RequestParam("userId")String userId) throws IOException {
//
//        return facebookUserClient.get(accessToken, userId);
//    }


    @PostMapping("/message")
    public  Object postMessage(@RequestParam("accessToken")String accessToken,@RequestParam("pageId")String pageId,@RequestParam("message")String message) {

            return  facebookPageClient.postMessage(accessToken,pageId,message);
    }


    @PostMapping("/link")
    public  Object postLink(@RequestParam("accessToken")String accessToken,@RequestParam("pageId")String pageId,@RequestParam("message")String message
                           ,@RequestParam("linkUrl")String linkUrl) {

        return  facebookPageClient.postLink(accessToken,pageId,message,linkUrl);
    }

//    @Resource
//    MultipartFile resource;

    @PostMapping("/image/url")
    public Object postImage( @RequestParam("url")String url, @RequestParam("accessToken")String accessToken , @RequestParam("caption")String caption, @RequestParam("pageId")String pageId){

        //resource= file;

      // return  ae.getAnnotation(Resource.class).type();
return facebookPageClient.postPhoto(accessToken,pageId,url,caption,false);
      //  return facebookPageClient.postPhoto(accessToken,pageId,ae.get,caption);

    }

    @PostMapping("/image")
    public Object postImage(@RequestBody MultipartFile file,@RequestParam("accessToken")String accessToken , @RequestParam("caption")String caption, @RequestParam("pageId")String pageId){

        //resource= file;

        // return  ae.getAnnotation(Resource.class).type();
        return facebookPageClient.postPhoto(accessToken,pageId,file.getResource(),caption);
    }

    @PostMapping("/image/multiple")
    public Object postMultipleImage(@RequestBody MultipartFile[] files,@RequestParam("accessToken")String accessToken , @RequestParam("message")String message, @RequestParam("pageId")String pageId){

        HashMap<Resource,String> hashMap= new HashMap<>();

        for(MultipartFile file:files)
             hashMap.put(file.getResource(),"caption "+file.getName());



        return facebookPageClient.publishMultiPhotoPost(accessToken,pageId,hashMap,message);
    }

}

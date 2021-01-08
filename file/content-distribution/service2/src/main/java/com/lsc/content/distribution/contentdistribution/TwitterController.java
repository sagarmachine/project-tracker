package com.lsc.content.distribution.contentdistribution;


import com.lsc.content.distribution.twitter.ContentTwitterException;
import com.lsc.content.distribution.twitter.client.TwitterConnectClient;
import com.lsc.content.distribution.twitter.client.TweetClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;
import java.util.Date;


@RestController
@RequestMapping("/twitter")
public class TwitterController {

    TwitterConnectClient twitterConnectClient= new TwitterConnectClient("http://localhost:8081/twitter/callback");
    TweetClient tweetClient =new TweetClient("4UH308yVuQAr81NKjFRp7uTol",
            "Bd6CpENKRABN9JUnbmPX50s0vO6s3s1ipj8ylgFs5K9BE5mQ9E","1320353507967336451-FM7cv8qk6gNsCKhUzIBurFt8BcgeJD","9HMo7rmq0Y20r1pDoP9gTO1trr5oZba9RD5fOET8qO4B8");

    @GetMapping("/oauth")
    public ModelAndView oauth(){

        return twitterConnectClient.connectTwitter();

    }


    @GetMapping("/callback")
    public Object  callback(@RequestParam("oauth_token")String oauth_token,@RequestParam("oauth_verifier")String oauth_verifier) throws IOException {

        return twitterConnectClient.connectCallback(oauth_verifier);
    }

    @GetMapping("/tweet/message")
    public Object tweet(@RequestParam("message")String message) throws IOException {


        return tweetClient.tweet(message);

    }

    @PostMapping("/tweet/image")
    public Object oauth(@RequestBody MultipartFile[] files) throws IOException {

        return tweetClient.tweet(files);
    }

    @PostMapping("/tweet/message/image")
    public Object oauth(@RequestBody MultipartFile[] files,@RequestParam("status")String status) throws IOException {

        return tweetClient.tweet(files);
    }


    @PostMapping("/tweet/media")
    public Object oauth(@RequestBody MultipartFile file) throws IOException, InterruptedException {

        System.out.println( " \t " + Runtime.getRuntime().freeMemory() +
                             " \t \t " + Runtime.getRuntime().totalMemory() +
                             " \t \t " + Runtime.getRuntime().maxMemory());


//       TwitterMediaUpload twitterMediaUpload= new TwitterMediaUpload(tweetClient.twitterTemplate);
//       ;
       try{
        return tweetClient.tweet(file);
    }catch (ContentTwitterException ex){
           System.out.println(ex.getMessage());
       return "exception";
       }


    }

    @PostMapping("/tweet/message/media")
    public Object oauth(@RequestBody MultipartFile file,@RequestParam("status")String status) throws IOException, InterruptedException {

        System.out.println(44444);
        return tweetClient.tweet(file,status);
    }
}

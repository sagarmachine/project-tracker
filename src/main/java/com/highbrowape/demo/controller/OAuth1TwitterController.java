package com.highbrowape.demo.controller;


import com.highbrowape.demo.dto.input.UserRegister;
import com.highbrowape.demo.repository.UserRepository;
import com.highbrowape.demo.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.social.connect.Connection;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/oauth1/authorization/twitter")
@Slf4j
public class OAuth1TwitterController {


    String redirectUrl="http://localhost:8085/oauth1/authorization/twitter/callback";

    @Autowired
    UserRepository userRepository;

    @Autowired
    IUserService userService;

    @Value("${frontend.auth.twitter.redirect}")
    String twitterRedirect;

    @Value("${frontend.url}")
    String frontendUrl;

    TwitterConnectionFactory twitterConnectionFactory= new TwitterConnectionFactory("4UH308yVuQAr81NKjFRp7uTol", "Bd6CpENKRABN9JUnbmPX50s0vO6s3s1ipj8ylgFs5K9BE5mQ9E");
    OAuth1Operations oAuth1Operations = twitterConnectionFactory.getOAuthOperations();

    @RequestMapping("")
    ModelAndView connectTwitter(){

        OAuthToken oauthToken= oAuth1Operations.fetchRequestToken(redirectUrl,null);
        String authorizeUrl= oAuth1Operations.buildAuthorizeUrl(oauthToken.getValue() , OAuth1Parameters.NONE);
        RedirectView redirectView = new RedirectView(authorizeUrl, true, true,
                true);
        return new ModelAndView(redirectView);
    }

    @RequestMapping("/callback")
    void callBack(@RequestParam("oauth_verifier")String oauthVerifier, @RequestParam("oauth_token")String oauthToken, HttpServletResponse httpServletResponse) throws IOException {

        OAuthToken accessToken = oAuth1Operations.exchangeForAccessToken(new AuthorizedRequestToken(new OAuthToken(oauthToken, "Bd6CpENKRABN9JUnbmPX50s0vO6s3s1ipj8ylgFs5K9BE5mQ9E"), oauthVerifier), null);


        Connection<Twitter> connection = twitterConnectionFactory.createConnection(accessToken);
        Twitter twitter = connection.getApi();
        TwitterProfile twitterProfile = twitter.userOperations().getUserProfile();

        String email = "twitter@" + twitterProfile.getId();
        HttpHeaders headers = null;
        if (userRepository.existsByEmail(email)) {
            log.error("Login");

            headers = userService.createResponseForAuthenticatedUser(email).getHeaders();
        } else {
            log.error("Register");
            UserRegister userRegister = UserRegister.builder().email(email).firstName(twitterProfile.getScreenName()).lastName(twitterProfile.getName()).imageUrl(twitterProfile.getProfileImageUrl()).build();
            headers = userService.register(userRegister).getHeaders();

        }

        httpServletResponse.sendRedirect(frontendUrl + twitterRedirect
                + "/" + headers.get("Authorization")
                + "/" + headers.get("Email")
                + "/" + headers.get("Name")
//                + "/" + headers.get("ImageUrl")
        );

    }


}

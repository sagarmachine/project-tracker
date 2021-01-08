package com.lsc.content.distribution.twitter.client;

import com.lsc.content.distribution.twitter.model.TwitterOAuthResponse;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


public class TwitterConnectClient {

    private String redirectUrl;
    private OAuthToken requestToken;
    TwitterConnectionFactory connectionFactory;

    public TwitterConnectClient(String redirectUrl) {
        this.redirectUrl = redirectUrl;
        this.connectionFactory = new TwitterConnectionFactory("4UH308yVuQAr81NKjFRp7uTol", "Bd6CpENKRABN9JUnbmPX50s0vO6s3s1ipj8ylgFs5K9BE5mQ9E");
    }


    // connects with Twitter's AOuth window
    public ModelAndView connectTwitter() {

        OAuth1Operations oAuth1Operations = connectionFactory.getOAuthOperations();
        OAuthToken requestToken = oAuth1Operations.fetchRequestToken(redirectUrl, null);
        String callBack = oAuth1Operations.buildAuthorizeUrl(requestToken.getValue(), OAuth1Parameters.NONE);
        this.requestToken = requestToken;
        RedirectView redirectView = new RedirectView(callBack, true, true,
                true);
        return new ModelAndView(redirectView);

    }


    // callback function after successful authentication from Twitter's window
    public TwitterOAuthResponse connectCallback(String oauthVerifier) {

        OAuth1Operations oAuth1Operations = connectionFactory.getOAuthOperations();
        OAuthToken accessToken = oAuth1Operations.exchangeForAccessToken(
                new AuthorizedRequestToken(requestToken, oauthVerifier), null);
        TwitterOAuthResponse twitterOAuthResponse = new TwitterOAuthResponse(accessToken.getValue(), accessToken.getSecret());
        return twitterOAuthResponse;
    }


}

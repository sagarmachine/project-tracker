package com.lsc.content.distribution.facebook.client;



import java.io.IOException;
import java.util.List;

import com.lsc.content.distribution.facebook.model.FacebookPage;
import com.lsc.content.distribution.facebook.model.FacebookUser;
import com.lsc.content.distribution.facebook.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Account;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.Page;
import org.springframework.social.facebook.api.PageOperations;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.lsc.content.distribution.facebook.ContentFacebookException;
//import com.lsc.content.distribution.facebook.model.FacebookPage;
//import com.lsc.content.distribution.facebook.model.FacebookUser;


public class FacebookConnectClient {


    public  String test(){
        return "test";
    }

    FacebookConnectionFactory connectionFactory;

//    @Autowired
//    FacebookConnectionFactory facebookConnectionFactory;

    //@Value("#{appProperties['facebook.redirect.url']}")
    private String redirectUrl;

    //@Value("#{appProperties['facebook.login.scope']}")
    private String loginScope;

    public FacebookConnectClient(String redirectUrl, String loginScope) {
        this.redirectUrl = redirectUrl;
        this.loginScope = loginScope;
        this.connectionFactory=new FacebookConnectionFactory("430546587907477","04f5ae05e3de1e8965d578e85f942ed0");

    }

    public ModelAndView connectFacebook() {

//        OAuth2Operations oauthOperations = facebookConnectionFactory
//                .getOAuthOperations();

//updated
        OAuth2Operations oauthOperations =connectionFactory.getOAuthOperations();


        OAuth2Parameters params = new OAuth2Parameters();
        params.setRedirectUri(redirectUrl);
        params.setScope(loginScope);
        params.set("namespace","lcs");
        System.out.println(params.getScope());
        String authorizeUrl = oauthOperations.buildAuthorizeUrl(params);
        System.out.println(authorizeUrl);
        RedirectView redirectView = new RedirectView(authorizeUrl, true, true,
                true);
        return new ModelAndView(redirectView);
    }

    public ModelAndView connectFacebook(String state) {
//        OAuth2Operations oauthOperations = facebookConnectionFactory
//                .getOAuthOperations();

        //updated
     //   FacebookConnectionFactory connectionFactory= new FacebookConnectionFactory("430546587907477","04f5ae05e3de1e8965d578e85f942ed0");
        OAuth2Operations oauthOperations =connectionFactory.getOAuthOperations();

        OAuth2Parameters params = new OAuth2Parameters();
        params.setRedirectUri(redirectUrl);
        params.setScope(loginScope);
        params.setState(state);
        String authorizeUrl = oauthOperations.buildAuthorizeUrl(params);
        RedirectView redirectView = new RedirectView(authorizeUrl, true, true,
                true);
        return new ModelAndView(redirectView);
    }

    public FacebookUser connectCallback(String callBackCode)
            throws IOException, ContentFacebookException {
        if (callBackCode != null) {
//            OAuth2Operations oauthOperations = facebookConnectionFactory
//                    .getOAuthOperations();

            //updated
          //  FacebookConnectionFactory connectionFactory= new FacebookConnectionFactory("430546587907477","04f5ae05e3de1e8965d578e85f942ed0");
            OAuth2Operations oauthOperations =connectionFactory.getOAuthOperations();

//update
            MultiValueMap<String, String> multiValueMap= new LinkedMultiValueMap<>();
            multiValueMap.add("client_id","430546587907477");
            multiValueMap.add("client_secret","04f5ae05e3de1e8965d578e85f942ed0");

            AccessGrant accessGrant = oauthOperations.exchangeForAccess(
                    callBackCode, redirectUrl, multiValueMap);
            String userAccessToken = accessGrant.getAccessToken();

//            Connection<Facebook> connection = facebookConnectionFactory
//                    .createConnection(accessGrant);

            Connection<Facebook> connection=connectionFactory.createConnection(accessGrant);
//UserProfile userProfile = connection.fetchUserProfile();


            Facebook facebook = connection.getApi();
            //Change
            String [] fields = { "id", "email",  "first_name", "last_name" };
// complete list of fields..
//{ "id", "about", "age_range", "birthday", "context", "cover", "currency",
// "devices", "education", "email", "favorite_athletes", "favorite_teams",
//"first_name", "gender", "hometown", "inspirational_people", "installed",
//"install_type", "is_verified", "languages", "last_name", "link", "locale",
//"location", "meeting_for", "middle_name", "name", "name_format", "political",
//"quotes", "payment_pricepoints", "relationship_status", "religion",
//"security_settings", "significant_other", "sports", "test_group", "timezone",
//"third_party_id", "updated_time", "verified", "video_upload_limits",
//"viewer_can_send_gift", "website", "work"}

            User userProfile = facebook.fetchObject("me", User.class,fields);


            FacebookUser user = new FacebookUser();
            user.setEmail(userProfile.getEmail());
            user.setFirstName(userProfile.getFirst_name());
            user.setLastName(userProfile.getLast_name());
            user.setName(userProfile.getName());
//user.setUsername(userProfile.getUsername());
            user.setId(userProfile.getId());
            user.setAccessToken(userAccessToken);

//Facebook facebook = connection.getApi();
            PageOperations po = facebook.pageOperations();

            List<Account> accounts = po.getAccounts();
            for (int i = 0; i < accounts.size(); i++) {

                FacebookPage page = new FacebookPage();
                Account account = accounts.get(i);
                page.setAccessToken(account.getAccessToken());
                page.setUserAccessToken(userAccessToken);
                page.setCategory(account.getCategory());
                page.setName(account.getName());
                page.setPageId(account.getId());
              //  page.setPermissions(account.getPermissions());
                Page fbPage = po.getPage(account.getId());
                if(fbPage != null) {
                    page.setLikes(fbPage.getLikes());
                }
// if (account.getName().equals("Swati Arts")) {
// final PagePostData post = new PagePostData(account.getId());
// post.message("<body>hey <b>Swati<b> !</body>");
// String id = "1697760510484145_1731778717082324";
// LikeOperations lo = facebook.likeOperations();
// PagedList pl = lo.getLikes(id);
// int num = pl.size();
// result += "\n9=== id: " + id + " : num : " + num;
// CommentOperations co = facebook.commentOperations();
// PagedList pl2 = co.getComments(id);
// result += "\n10=== comments : " + pl2.size();
// }
                user.addPage(page);
            }
            return user;
        } else {
// handle what to do when user denies permission

// int code = Integer.valueOf(request.getParameter("error_code"));
// String error = request.getParameter("error");
// String description = request.getParameter("error_description");
// String reason = request.getParameter("error_reason");
            throw new ContentFacebookException();
        }
    }

}

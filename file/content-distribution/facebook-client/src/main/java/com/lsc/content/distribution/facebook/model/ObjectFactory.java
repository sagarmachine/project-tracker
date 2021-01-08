package com.lsc.content.distribution.facebook.model;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {
	public FacebookUser createFacebookUser() {
        return new FacebookUser();
    }

	public FacebookPage createFacebookPage() {
		return new FacebookPage();
	}

	public FacebookPost createFacebookPost() {
		return new FacebookPost();
	}
	
}

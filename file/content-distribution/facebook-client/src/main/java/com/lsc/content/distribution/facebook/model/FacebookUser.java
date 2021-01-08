package com.lsc.content.distribution.facebook.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.lsc.content.distribution.facebook.XMLConstants;


@XmlType(namespace=XMLConstants.PPC_DISTRIBUTION_SERVICE_FACEBOOK_NS_URI)
@XmlRootElement(name = "facebookUser",namespace=XMLConstants.PPC_DISTRIBUTION_SERVICE_FACEBOOK_NS_URI)
@XmlAccessorType(XmlAccessType.FIELD)
public class FacebookUser {
	@XmlElement(required = true)
	private String id;
	@XmlElement()
	private String accessToken;
	@XmlElement()
	private String firstName;
	@XmlElement()
	private String lastName;
	@XmlElement()
	private String username;
	@XmlElement()
	private String email;
	@XmlElement()
	private String name;
	@XmlElementWrapper(name = "pages")
    @XmlElement(name = "page")
	private List<FacebookPage> pages;
	
	public List<FacebookPage> getPages() {
		return pages;
	}
	public void setPages(List<FacebookPage> pages) {
		this.pages = pages;
	}
	public void addPage(FacebookPage page) {
		if(pages == null)
			pages = new ArrayList<FacebookPage>();
		pages.add(page);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "FacebookUser [id=" + id + ", accessToken=" + accessToken
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", username=" + username + ", email=" + email + ", pages="
				+ pages + "]";
	}
	
	
	
}

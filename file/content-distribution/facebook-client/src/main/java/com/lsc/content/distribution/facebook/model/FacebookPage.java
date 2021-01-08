package com.lsc.content.distribution.facebook.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.lsc.content.distribution.facebook.XMLConstants;;

@XmlType(namespace=XMLConstants.PPC_DISTRIBUTION_SERVICE_FACEBOOK_NS_URI)
@XmlRootElement(name = "facebookPage",namespace=XMLConstants.PPC_DISTRIBUTION_SERVICE_FACEBOOK_NS_URI)
@XmlAccessorType(XmlAccessType.FIELD)
public class FacebookPage {

	@XmlElement
	private String pageId;
	@XmlElement
	private String name;
	@XmlElement
	private String category;
	@XmlElement
	private String accessToken;
	@XmlElement
	private String userAccessToken;
	@XmlElement
	private int likes;
	@XmlElement
	private String displayName;
	@XmlElementWrapper(name = "permissions")
    @XmlElement(name = "permission")
	private List<String> permissions;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public List<String> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	public String getPageId() {
		return pageId;
	}
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public String getDisplayName() {
		if(displayName != null)
			return displayName;
		return name + " (" + likes + " likes)";
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
//	@Override
//	public String toString() {
//		return "FacebookPage [pageId=" + pageId + ", accessToken="
//				+ accessToken + "]";
//	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accessToken == null) ? 0 : accessToken.hashCode());
		result = prime * result + ((pageId == null) ? 0 : pageId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FacebookPage other = (FacebookPage) obj;
		if (accessToken == null) {
			if (other.accessToken != null)
				return false;
		} else if (!accessToken.equals(other.accessToken))
			return false;
		if (pageId == null) {
			if (other.pageId != null)
				return false;
		} else if (!pageId.equals(other.pageId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "FacebookPage [pageId=" + pageId + ", name=" + name
				+ ", category=" + category + ", accessToken=" + accessToken
				+ ", likes=" + likes + ", displayName=" + displayName
				+ ", permissions=" + permissions + "]";
	}
	public String getUserAccessToken() {
		return userAccessToken;
	}
	public void setUserAccessToken(String userAccessToken) {
		this.userAccessToken = userAccessToken;
	}
	
	
	
}

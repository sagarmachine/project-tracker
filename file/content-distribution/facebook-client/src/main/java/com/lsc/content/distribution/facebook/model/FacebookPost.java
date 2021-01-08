package com.lsc.content.distribution.facebook.model;


//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlRootElement(name = "facebookPost")
public class FacebookPost {

//	@XmlElement()
	private String postId;
//	@XmlElement()
	private String postData;
	
	
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public String getPostData() {
		return postData;
	}
	public void setPostData(String postData) {
		this.postData = postData;
	}
	
}

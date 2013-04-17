package com.example.willowtweet;


import java.text.NumberFormat;
import java.util.Locale;

import android.graphics.Bitmap;

public class twitUser {
	private String userName;
	private String userFollowing;
	private String userFollowers;
	private int userTweets;
	private Bitmap userPic;
    
    public twitUser(){
    	
    }
    
    public twitUser(final String userName, final int userFollowing, final int userFollowers, final int userTweets, final Bitmap userPic){
    	this.userName = userName;
    	this.userFollowing = Integer.toString(userFollowing);
    	this.userFollowers = Integer.toString(userFollowers);
    	this.userTweets = userTweets;
    	this.userPic = userPic;
    }

	

	public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserFollowers() {
        return userFollowers;
    }
    public void setUserFollowers(int userFollowers) {
        this.userFollowers = Integer.toString(userFollowers);
    }
    
    public String getUserFollowing() {
        return userFollowing;
    }
    public void setUserFollowing(int userFollowing) {
        this.userFollowing = Integer.toString(userFollowing);
    }
    
    public String getUserTweets() {
    	String userTweetsStr = NumberFormat.getNumberInstance(Locale.US).format(userTweets);
        return userTweetsStr;
    }
    public void setUserTweets(int userTweets) {
        this.userTweets = userTweets;
    }
   
    public Bitmap getUserPic() {
        return userPic;
    }
    public void setUserPic(Bitmap userPic) {
        this.userPic = userPic;
    }
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
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
		twitUser other = (twitUser) obj;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}

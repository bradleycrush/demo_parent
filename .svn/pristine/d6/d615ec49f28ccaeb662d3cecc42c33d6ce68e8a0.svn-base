package org.event_manager.native_events;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mongodb.DBObject;

public class TwitterEvent implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static SimpleDateFormat TWITTER_DATE_FORMAT = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
	
	public TwitterEvent(){
		
	}
	
	
	public TwitterEvent(DBObject obj){
		this.tweetId =(long) obj.get("tweetId");
		this.tweetDate = (Date) obj.get("tweetDate");
		this.hashtag = (String) obj.get("hashTag");
		this.languageCode = (String) obj.get("languageCode");
		this.message = ((String) obj.get("message"));
		this.retweetUser = (String) obj.get("retweetUser");
		this.source = ((String) obj.get("source"));
		this.userId = ((long) obj.get("userId"));
		this.username = ((String) obj.get("username"));
	}
	
	private String hashtag;
	private long tweetId;
	private Date tweetDate;
	private String username;
	private long userId;
	private String source;
	private String languageCode;
	private String message;
	private String retweetUser = "";

	
	public String getHashtag() {
		return hashtag;
	}

	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}

	public Date getTweetDate() {
		return tweetDate;
	}

	public void setTweetDate(Date tweetDate) {
		this.tweetDate = tweetDate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRetweetUser() {
		return retweetUser;
	}

	public void setRetweetUser(String retweetUser) {
		this.retweetUser = retweetUser;
	}

	public long getTweetId() {
		return tweetId;
	}

	public void setTweetId(long tweetId) {
		this.tweetId = tweetId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String toJson(){	
		  JsonObject obj = new JsonObject();
		  obj.add("hashtag", new JsonPrimitive(hashtag));
		  obj.add("tweetId", new JsonPrimitive(tweetId));
		  obj.add("tweetDate", new JsonPrimitive(TWITTER_DATE_FORMAT.format(tweetDate)));
		  obj.add("username", new JsonPrimitive(username));
		  obj.add("userId", new JsonPrimitive(userId));
		  obj.add("source", new JsonPrimitive(source));
		  obj.add("languageCode", new JsonPrimitive(languageCode));
		  obj.add("message", new JsonPrimitive(message));
		  obj.add("retweetUser", new JsonPrimitive(retweetUser));
		
		return obj.toString();
	}

	
	
	/*
	 * { "hashtag":"hashtag_value", "date":"date_of_tweet_as_long",
	 * "user":"user_name", "message":"tweet_message", "retweet_user",
	 * "user_name" }
	 * 
	 * Event: { "topic":"", "date":"", "source":"", "author","", "message":{
	 * "category":"", "emotions":"", "length":"", }
	 */

}

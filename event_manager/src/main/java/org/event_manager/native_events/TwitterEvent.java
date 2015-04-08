package org.event_manager.native_events;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mongodb.DBObject;

public class TwitterEvent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final SimpleDateFormat dateFormat;

	public static String TWITTER_DATE_FORMAT = "EEE MMM dd HH:mm:ss z yyyy";

	public TwitterEvent() {
		dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

	}

	public TwitterEvent(DBObject obj) {
		dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

		this.tweetId = (long) obj.get("tweetId");
		this.tweetDate = (Date) obj.get("tweetDate");
		this.hashtag = (String) obj.get("hashTag");
		this.languageCode = (String) obj.get("languageCode");
		this.message = ((String) obj.get("message"));
		this.retweetUser = (String) obj.get("retweetUser");
		this.source = ((String) obj.get("source"));
		this.userId = ((long) obj.get("userId"));
		this.username = ((String) obj.get("username"));
	}

	private String hashtag = "";
	private long tweetId = -1;
	private Date tweetDate = null;
	private String username = "";
	private long userId = -1;
	private String source = "";
	private String languageCode = "";
	private String message = "";
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

	public String toJson() {
		JsonObject obj = new JsonObject();

		String date = "";

		if (tweetDate!=null) {

			date = dateFormat.format(tweetDate);
		}

		obj.add("hashtag", new JsonPrimitive(hashtag));
		obj.add("tweetId", new JsonPrimitive(tweetId));
		obj.add("username", new JsonPrimitive(username));
		obj.add("tweetDate", new JsonPrimitive(date));
		obj.add("userId", new JsonPrimitive(userId));
		obj.add("source", new JsonPrimitive(source));
		obj.add("languageCode", new JsonPrimitive(languageCode));
		obj.add("message", new JsonPrimitive(message));
		obj.add("retweetUser", new JsonPrimitive(retweetUser));

		return obj.toString();
	}

	public static TwitterEvent createFromJson(String json) {
		Gson gson = new GsonBuilder().setDateFormat(TWITTER_DATE_FORMAT).create(); 
		TwitterEvent event = gson.fromJson(json, TwitterEvent.class); 
		
		return event;
	}

}

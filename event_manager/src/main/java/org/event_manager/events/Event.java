package org.event_manager.events;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.SerializedName;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Event implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8035640373033517539L;
	public static String EVENT_DATE_FORMAT_STRING = "yyyy-dd-MM'T'HH:mm:ss:SSS'Z'";
	public static SimpleDateFormat EVENT_DATE_FORMAT = new SimpleDateFormat(EVENT_DATE_FORMAT_STRING);
	
	public enum Emotion{
		   @SerializedName("Like")Like, @SerializedName("DontLike")DontLike, @SerializedName("NoOpinion")NoOpinion
	}
	
	public enum Source{
		@SerializedName("Twitter")Twitter
	}
	
	public Event(){
		
	}
	
	public Event(DBObject obj){
		this.author = (String) obj.get("author");
		this.date = (Date) obj.get("date");
		this.topic = (String) obj.get("topic");
		this.message = ((String) obj.get("message"));
		this.emotion =  Enum.valueOf(Emotion.class,(String)obj.get("emotion"));
		this.source =  Enum.valueOf(Source.class,(String)obj.get("source"));
	}
	
	
	private String topic;
	private Date date;
	private Source source;
	private String author;
	private String message;
	private Emotion emotion;
	
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Source getSource() {
		return source;
	}
	public void setSource(Source source) {
		this.source = source;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Emotion getEmotion() {
		return emotion;
	}
	public void setEmotion(Emotion emotion) {
		this.emotion = emotion;
	}
	
	public BasicDBObject toBasicDBObject(){
		BasicDBObject doc = new BasicDBObject("author", this.getAuthor())
		.append("date", this.getDate())
		.append("emotion", this.getEmotion().toString())
		.append("message", this.getMessage())
		.append("source", this.getSource().toString())
		.append("topic", this.getTopic());
		return doc;
	}
	
	public String toJson(){	
		  JsonObject obj = new JsonObject();
		  obj.add("topic", new JsonPrimitive(topic));
		  obj.add("date", new JsonPrimitive(EVENT_DATE_FORMAT.format(date)));
		  obj.add("author", new JsonPrimitive(author));
		  obj.add("source", new JsonPrimitive(source.toString()));
		  obj.add("message", new JsonPrimitive(message));
		  obj.add("emotion", new JsonPrimitive(emotion.toString()));
		
		return obj.toString();
	}
	
	public static Event JSONtoEvent(String json) {
		Gson gson = new GsonBuilder().setDateFormat(EVENT_DATE_FORMAT_STRING).create(); 
		Event event = gson.fromJson(json, Event.class); 
		
		return event;
	}
	
	public String getKey(){
		return (author + "::" + date.toString() + "::"+ source);
	}

}

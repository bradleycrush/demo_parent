package org.event_manager.events;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.event_manager.events.Event.Emotion;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Component implements Comparable<Component>, Serializable {
	
	public static String COMPONENT_DATE_FORMAT = "MM/dd/yyyy HH:mm:ss:SSS";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String topic;
	
	private Emotion associatedEmotion;
	
	private Date componentUpdateDate;
	
	private final SimpleDateFormat dateFormat;

	public Component(){
		componentUpdateDate = new Date();
		dateFormat = new SimpleDateFormat(COMPONENT_DATE_FORMAT);
	}
	
	public Component(String topic, Emotion emotion, Date date){
		this.topic = topic;
		this.associatedEmotion = emotion;
		this.componentUpdateDate = date;
		dateFormat = new SimpleDateFormat(COMPONENT_DATE_FORMAT);
		
	}
	
	public Component(DBObject obj){
		this.topic = (String) obj.get("topic");
		this.associatedEmotion = Enum.valueOf(Event.Emotion.class,(String) obj.get("associatedEmotions"));
		this.componentUpdateDate = (Date) obj.get("date");
		dateFormat = new SimpleDateFormat(COMPONENT_DATE_FORMAT);
	}

	
	public Date getComponentUpdateDate() {
		return componentUpdateDate;
	}

	public void setComponentUpdateDate(Date componentUpdateDate) {
		this.componentUpdateDate = componentUpdateDate;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String category) {
		this.topic = category;
	}

	public Emotion getAssociatedEmotion() {
		return this.associatedEmotion;
	}

	public void setAssociatedEmotions(Emotion associatedEmotion) {
		this.associatedEmotion = associatedEmotion;
	}

	public BasicDBObject toBasicDBObject(){
		BasicDBObject doc = new BasicDBObject("topic", this.getTopic())
		.append("associatedEmotion", this.getAssociatedEmotion().toString())
		.append("componentUpdateDate", this.componentUpdateDate)
		.append("date", this.componentUpdateDate);
		return doc;
	}
	
	public String toJson() {
		JsonObject obj = new JsonObject();

		String date = "";

		if (componentUpdateDate!=null) {

			date = dateFormat.format(componentUpdateDate);
		}

		obj.add("topic", new JsonPrimitive(topic));
		obj.add("associatedEmotion", new JsonPrimitive(associatedEmotion.name()));
		obj.add("componentUpdateDate", new JsonPrimitive(date));
		
		return obj.toString();
	}

	public static Component JSONtoComponent(String json) {
		Gson gson = new GsonBuilder().setDateFormat(COMPONENT_DATE_FORMAT).create(); 
		Component component = gson.fromJson(json, Component.class); 
		
		return component;
	}
	

	@Override
	public int compareTo(Component o) {
		return this.topic.compareTo(o.getTopic());
	}
	
	public boolean equals(Component component){
		return (this.topic.equals(component.getTopic())&& (this.getAssociatedEmotion().equals(this.associatedEmotion)));
	}
	
	public String getKey(){
		return (this.getTopic() + "::" + this.getComponentUpdateDate().toString());
	}
	
}

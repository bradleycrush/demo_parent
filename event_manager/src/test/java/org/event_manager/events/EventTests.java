package org.event_manager.events;

import static org.junit.Assert.*;

import java.util.Date;

import org.event_manager.events.Event.Emotion;
import org.event_manager.events.Event.Source;
import org.junit.Test;

public class EventTests {

	@Test
	public void test(){
		Event event = new Event();
		event.setAuthor("author");
		event.setDate(new Date());
		event.setEmotion(Emotion.Like);
		event.setMessage("message");
		event.setSource(Source.Twitter);
		event.setTopic("topic");
		
		String json = event.toJson();
		Event event2 = Event.JSONtoEvent(json);
		
		assertEquals(event.getAuthor(), event2.getAuthor());
		assertEquals(event.getDate(), event2.getDate());
		assertEquals(event.getEmotion(), event2.getEmotion());
		assertEquals(event.getMessage(), event2.getMessage());
		assertEquals(event.getSource(), event2.getSource());
		assertEquals(event.getTopic(), event2.getTopic());
	}
}

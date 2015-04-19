package org.event_manager.events;

import static org.junit.Assert.*;

import java.util.Date;

import org.event_manager.events.Event.Emotion;
import org.junit.Test;

public class ComponentTests {

	@Test
	public void test(){
	Component comp = new Component("topic", Emotion.Like, new Date());
	
	String json = comp.toJson();
	
	assertNotSame("", json);
	
	Component comp2 = Component.JSONtoComponent(json);
	
	assertEquals(comp.getComponentUpdateDate(), comp2.getComponentUpdateDate());
	assertEquals(comp.getTopic(), comp2.getTopic());
	assertEquals(comp.getAssociatedEmotion(), comp2.getAssociatedEmotion());
	
	}
	
}

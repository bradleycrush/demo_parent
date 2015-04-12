package org.event_manager.native_events;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class TwitterEventsTest {

	@Test
	public void test(){
	TwitterEvent twitEvent = new TwitterEvent();
	
	twitEvent.setHashtag("hashtag");
	twitEvent.setLanguageCode("lanCode");
	twitEvent.setMessage("message");
	twitEvent.setRetweetUser("user");
	twitEvent.setSource("Source");
	twitEvent.setTweetDate(new Date());
	twitEvent.setTweetId(1000);
	twitEvent.setUserId(12312312);
	twitEvent.setUsername("username");
	
	
	String json = twitEvent.toJson();
	
	TwitterEvent twitEvent2 = TwitterEvent.createFromJson(json);
	
	
	assertEquals(twitEvent2.toJson(),json);
	
	}
}

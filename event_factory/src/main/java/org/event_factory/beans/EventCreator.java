package org.event_factory.beans;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.TableExistsException;
import org.apache.accumulo.core.client.TableNotFoundException;
import org.event_manager.api.AccumuloUtils;
import org.event_manager.events.Event;
import org.event_manager.events.Event.Emotion;
import org.event_manager.events.Event.Source;
import org.event_manager.native_events.TwitterEvent;
import org.message_manager.api.MessageClientAPI;

/**
 * Creates events and stores them in persistence
 * If the event updates context, creates a new Message for the component factory
 * @author ubuntu
 *
 */
public class EventCreator {

	MessageClientAPI messageClient = new MessageClientAPI();
	
	/**
	 * Example emotion generator based on message
	 * @param message
	 * @return
	 */
	private Emotion getEmotionFromMessage(String message){
		Emotion emotion = Emotion.NoOpinion;
		
		if ( message.toLowerCase().contains("like") || message.toLowerCase().contains("love")
				|| message.toLowerCase().contains("perfection") || message.toLowerCase().contains("good")
				|| message.toLowerCase().contains("party")|| message.toLowerCase().contains("best")
				|| message.toLowerCase().contains("yum")|| message.toLowerCase().contains("delicious")){
			emotion = Emotion.Like;
		} else if ( message.toLowerCase().contains("hate") || message.toLowerCase().contains("dontlike")
				|| message.toLowerCase().contains("bad") || message.toLowerCase().contains("yuck")
				|| message.toLowerCase().contains("stupid")|| message.toLowerCase().contains("worst")
				|| message.toLowerCase().contains("gross")){
			emotion = Emotion.DontLike;
		} 
		
		return emotion;
	}
	
	/**
	 * Returns true if the emotion change is meaningful
	 * @param contextEmotion
	 * @param newEmotion
	 * @return
	 */
	private boolean emotionChanged(Emotion contextEmotion, Emotion newEmotion){
		boolean changed = false;
		
		if ( !contextEmotion.equals(Emotion.NoOpinion)){
			if ( !newEmotion.equals(Emotion.NoOpinion)){
				return contextEmotion.equals(newEmotion);
			}
		} else{
			if ( !newEmotion.equals(Emotion.NoOpinion)){
				changed = true;
			}
		}
		return changed;
	}
	
	/**
	 * Called on changes in the twitter stream
	 * @param message
	 * @throws TableExistsException 
	 * @throws AccumuloSecurityException 
	 * @throws AccumuloException 
	 * @throws TableNotFoundException 
	 */
	public void handle(String message) throws AccumuloException, AccumuloSecurityException, TableExistsException, TableNotFoundException {
		String[] params = message.split("\n");


		SimpleDateFormat tweetDateFormat = new SimpleDateFormat(TwitterEvent.TWITTER_DATE_FORMAT);

		
		TwitterEvent event = new TwitterEvent();
		
		event.setHashtag(params[0]);
		try {
			event.setTweetDate(tweetDateFormat
					.parse(params[1]));
		} catch (ParseException e) {
			System.out.println("Could not parse date:" + params[1]);
		}
		event.setUsername(params[2]);
		event.setUserId(Long.parseLong(params[3]));
		event.setSource(params[4]);
		event.setTweetId(Long.parseLong(params[5]));
		event.setLanguageCode(params[6]);
		event.setMessage(params[7]);

		if (event.getMessage().startsWith("RT")) {
			event.setRetweetUser(event.getMessage().substring(
					event.getMessage().indexOf("@") + 1,
					event.getMessage().indexOf(":")));
		}

		// Add message to persistence
		AccumuloUtils.addTwitterEvent(event);

		// Convert to Event
		Event newEvent = new Event();
		newEvent.setAuthor(event.getUsername());
		newEvent.setDate(event.getTweetDate());
		newEvent.setMessage(event.getMessage());
		newEvent.setEmotion(getEmotionFromMessage(event.getMessage()));
		newEvent.setSource(Source.Twitter);
		newEvent.setTopic(event.getHashtag());

		AccumuloUtils.addEvent(newEvent);

		// Check if this new event affects the event context
		Event context = AccumuloUtils.getEventContext(event.getUsername());

		// IF this is an update or new event
		if (context == null || emotionChanged(context.getEmotion(), newEvent.getEmotion())
				|| newEvent.getDate().compareTo(context.getDate()) > 0) {
			AccumuloUtils.updateEventContext(newEvent);

			System.out.println("New Event- " + newEvent.toJson());

			// Send event to the queue
			try {
				messageClient.publish(newEvent);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}
}

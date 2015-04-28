package org.event_manager.api;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.BatchWriter;
import org.apache.accumulo.core.client.BatchWriterConfig;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Scanner;
import org.apache.accumulo.core.client.TableExistsException;
import org.apache.accumulo.core.client.TableNotFoundException;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Mutation;
import org.apache.accumulo.core.data.Range;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.security.Authorizations;
import org.apache.accumulo.core.security.ColumnVisibility;
import org.apache.hadoop.io.Text;
import org.event_manager.events.Component;
import org.event_manager.events.Event;
import org.event_manager.events.Event.Source;
import org.event_manager.native_events.TwitterEvent;



public class AccumuloUtils {
	//public static final String DATABASE_EVENT_DEMO = "EventDemo";
	public static final String DATABASE_COLLECTION_EVENT_CONTEXT = "EventContext";
	public static final String DATABASE_COLLECTION_COMPONENT_CONTEXT = "ComponentContext";
	public static final String DATABASE_COLLECTION_EVENT = "Events";
	public static final String DATABASE_COLLECTION_COMPONENT = "Components";
	public static final String DATABASE_COLLECTION_TWITTER_EVENT = "TwitterEvents";

	/**
	 * Delete a collection
	 * @param collection
	 */
	public static void deleteCollection(String collection) {
		
//		MongoClient mongoClient = MongoClientInstance.getInstance();
//		DB db = mongoClient.getDB(DATABASE_EVENT_DEMO);
//
//		db.getCollection(collection).drop();
	}

	/**
	 * Add an event
	 * @param event
	 * @param collection
	 * @throws TableExistsException 
	 * @throws AccumuloSecurityException 
	 * @throws AccumuloException 
	 */
	private static void addEvent(Event event, String collection) throws AccumuloException, AccumuloSecurityException, TableExistsException {
		
		Connector aClient = AccumuloClientInstance.getInstance();
		BatchWriter wr = null;
		
		// Attempt to write -exception based programming
		while(true){
		   try {
			   wr = aClient.createBatchWriter(DATABASE_COLLECTION_EVENT, new BatchWriterConfig());
		    break;
		   } catch (TableNotFoundException e) {
		  	  // Create Table and retry
			  aClient.tableOperations().create(DATABASE_COLLECTION_EVENT);
		   }
		}
		
      Mutation m = new Mutation(event.getAuthor());		
      Text family = new Text(event.getDate().toString());
      Text qual = new Text(event.getSource().name());
      Value value = new Value(event.toJson().getBytes());
      ColumnVisibility visibility = new ColumnVisibility("public");
      
      m.put(family, qual,visibility, value );
      
      wr.addMutation(m);
      wr.close();
      
	}
	

	/**
	 * Add a component
	 * @param component
	 * @param collection
	 * @throws TableExistsException 
	 * @throws AccumuloSecurityException 
	 * @throws AccumuloException 
	 */
	private static void addComponent(Component component, String collection) throws AccumuloException, AccumuloSecurityException, TableExistsException {

		Connector aClient = AccumuloClientInstance.getInstance();
		BatchWriter wr = null;
		
		// Attempt to write -exception based programming
		while(true){
		   try {
			   wr = aClient.createBatchWriter(DATABASE_COLLECTION_COMPONENT, new BatchWriterConfig());
		    break;
		   } catch (TableNotFoundException e) {
		  	  // Create Table and retry
			  aClient.tableOperations().create(DATABASE_COLLECTION_COMPONENT);
		   }
		}
		
      Mutation m = new Mutation(component.getTopic());		
      Text family = new Text(component.getComponentUpdateDate().toString());
      Text qual = new Text();
      Value value = new Value(component.toJson().getBytes());
      ColumnVisibility visibility = new ColumnVisibility("public");
      
      m.put(family, qual,visibility, value );
      
      wr.addMutation(m);
      wr.close();

				
	}

	/**
	 * Add a component
	 * @param component
	 * @throws TableExistsException 
	 * @throws AccumuloSecurityException 
	 * @throws AccumuloException 
	 */
	public static void addComponent(Component component) throws AccumuloException, AccumuloSecurityException, TableExistsException{
		addComponent(component, DATABASE_COLLECTION_COMPONENT);
	}
	
	/**
	 * Add an Event
	 * @param event
	 * @throws TableExistsException 
	 * @throws AccumuloSecurityException 
	 * @throws AccumuloException 
	 */
	public static void addEvent(Event event) throws AccumuloException, AccumuloSecurityException, TableExistsException {
		addEvent(event, DATABASE_COLLECTION_EVENT);
	}
	
	/**
	 * Update or add the event into the event context
	 * @param event
	 * @throws TableExistsException 
	 * @throws AccumuloSecurityException 
	 * @throws AccumuloException 
	 */
	public static void updateEventContext(Event event) throws AccumuloException, AccumuloSecurityException, TableExistsException{
		Connector aClient = AccumuloClientInstance.getInstance();
		BatchWriter wr = null;
		
		// Attempt to write -exception based programming
		while(true){
		   try {
			   wr = aClient.createBatchWriter(DATABASE_COLLECTION_EVENT_CONTEXT, new BatchWriterConfig());
		    break;
		   } catch (TableNotFoundException e) {
		  	  // Create Table and retry
			  aClient.tableOperations().create(DATABASE_COLLECTION_EVENT_CONTEXT);
		   }
		}
		
      Mutation m = new Mutation(event.getAuthor());		
      Value value = new Value(event.getKey().getBytes());
      ColumnVisibility visibility = new ColumnVisibility("public");
      
      Text t = new Text();
      m.put(t, t,visibility, value );
      
      wr.addMutation(m);
      wr.close();

	}
	
	/**
	 * Update or add the component into the component context
	 * @param component
	 * @throws TableExistsException 
	 * @throws AccumuloSecurityException 
	 * @throws AccumuloException 
	 */
	public static void updateComponentContext(Component component) throws AccumuloException, AccumuloSecurityException, TableExistsException{

	Connector aClient = AccumuloClientInstance.getInstance();
	BatchWriter wr = null;
	
	// Attempt to write -exception based programming
	while(true){
	   try {
		   wr = aClient.createBatchWriter(DATABASE_COLLECTION_COMPONENT_CONTEXT, new BatchWriterConfig());
	    break;
	   } catch (TableNotFoundException e) {
	  	  // Create Table and retry
		  aClient.tableOperations().create(DATABASE_COLLECTION_COMPONENT_CONTEXT);
	   }
	}
	
  Mutation m = new Mutation(component.getTopic());		
  Value value = new Value(component.getKey().getBytes());
  ColumnVisibility visibility = new ColumnVisibility("public");
  
  Text t = new Text();
  m.put(t, t,visibility, value );
  
  wr.addMutation(m);
  wr.close();

		
	}

	/**
	 * Get the current context for this topic. Returns null if no context
	 * @param topic
	 * @return
	 * @throws AccumuloSecurityException 
	 * @throws AccumuloException 
	 * @throws TableNotFoundException 
	 */
	public static Component getComponentContext(String topic) throws AccumuloException, AccumuloSecurityException  {
		Component contextComponent = null;
		Event context = null;

		Connector aClient = AccumuloClientInstance.getInstance();
		
		
		 Authorizations au = new Authorizations("public");
		 Scanner scanner = null;
		 Range range = null;
		 
		while(true){
			try {
				scanner = aClient.createScanner(DATABASE_COLLECTION_COMPONENT_CONTEXT, au);
				range = Range.exact(topic);
				break;
		    } catch (TableNotFoundException e) {
				 // Create Table and retry
				try {
					aClient.tableOperations().create(DATABASE_COLLECTION_COMPONENT_CONTEXT);
				} catch (TableExistsException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		 
		 Text familyIn = null, qualIn = null, idIn = null;
		 Value valueIn = null;
		 
		 for(Entry<Key, Value> entry: scanner){
			 idIn = entry.getKey().getRow();
			 familyIn = entry.getKey().getColumnFamily();
			 qualIn = entry.getKey().getColumnQualifier();
			 valueIn = entry.getValue(); 
		 }  
		 
		 if( !Objects.equals(valueIn, null)){

		   // Now use the value to extract the actual event
		   StringBuffer sb = new StringBuffer(valueIn.toString());
		   String str = valueIn.toString();
		   String[] values = str.split("::");
		 
		   range = Range.exact(values[0]);
		   scanner.setRange(range);
		   scanner.fetchColumnFamily(new Text(values[1]));
		 		 
		   Component comp = null;
		 
		   for(Entry<Key, Value> entry: scanner){
			 valueIn = entry.getValue(); 
			 // CONVERT TO actual component
			 contextComponent = Component.JSONtoComponent(valueIn.toString());
		   }
		 }

		 
		 scanner.close();	
		
		return contextComponent;
	}

	/**
	 * Gets the event context for this author. Returns null if no context
	 * @param author
	 * @return
	 * @throws TableNotFoundException 
	 * @throws AccumuloSecurityException 
	 * @throws AccumuloException 
	 */
	public static Event getEventContext(String author) throws  AccumuloException, AccumuloSecurityException {
		Event context = null;

		Connector aClient = AccumuloClientInstance.getInstance();
		
		 Authorizations au = new Authorizations("public");
		 Scanner scanner = null;
		 Range range = null;

		 
		while(true){
			try {
				scanner = aClient.createScanner(DATABASE_COLLECTION_EVENT_CONTEXT, au);
			    range = Range.exact(author);
					break;
			    } catch (TableNotFoundException e) {
					 // Create Table and retry
					try {
						aClient.tableOperations().create(DATABASE_COLLECTION_EVENT_CONTEXT);
					} catch (TableExistsException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		 
		 
		 Text familyIn = null, qualIn = null, idIn = null;
		 Value valueIn = null;
		 
		 for(Entry<Key, Value> entry: scanner){
			 idIn = entry.getKey().getRow();
			 familyIn = entry.getKey().getColumnFamily();
			 qualIn = entry.getKey().getColumnQualifier();
			 valueIn = entry.getValue(); 
		 }
		 
		 if( !Objects.equals(valueIn, null)){
		    // Now use the value to extract the actual event
		    StringBuffer sb = new StringBuffer(valueIn.toString());
		    String str = valueIn.toString();
		    String[] values = str.split("::");
		 
		    range = Range.exact(values[0]);
		    scanner.setRange(range);
		    scanner.fetchColumn(new Text(values[1]), new Text(values[2]));
		 		 
		    Event newEvent = null;
		 
		    for(Entry<Key, Value> entry: scanner){
			 valueIn = entry.getValue(); 
			 Event.JSONtoEvent(valueIn.toString()); 
		   }
		 }

		 
		 scanner.close();	
	 
		return context;
	}

	/**
	 * Get all events by author and source
	 * @param author
	 * @param source
	 * @return
	 */
	public static List<Event> getEventByAuthorAndSource(String author,
			Source source) {
		List<Event> events = new ArrayList<Event>();

//		MongoClient mongoClient = MongoClientInstance.getInstance();
//		DB db = mongoClient.getDB(DATABASE_EVENT_DEMO);
//
//		DBCollection coll = db.getCollection(DATABASE_COLLECTION_EVENT);
//
//		BasicDBObject query = new BasicDBObject();
//		query.put("author", author);
//		query.put("source", source.toString());
//		DBCursor cursor2 = coll.find(query);
//		// set index on a field
//
//		try {
//			while (cursor2.hasNext()) {
//				DBObject obj = cursor2.next();
//				events.add(new Event(obj));
//			}
//		} finally {
//			cursor2.close();
//		}
		return events;
	}

	/**
	 * Gets all twitter events by username
	 * @param username
	 * @return
	 */
	public static List<TwitterEvent> getTwitterEventByUsername(String username) {
//		MongoClient mongoClient = MongoClientInstance.getInstance();
//		DB db = mongoClient.getDB(DATABASE_EVENT_DEMO);
		List<TwitterEvent> events = new ArrayList<TwitterEvent>();

//		DBCollection coll = db.getCollection(DATABASE_COLLECTION_TWITTER_EVENT);
//
//		BasicDBObject query = new BasicDBObject();
//		query.put("username", username);
//		DBCursor cursor2 = coll.find(query);
//		// set index on a field
//
//		try {
//			while (cursor2.hasNext()) {
//				DBObject obj = cursor2.next();
//				events.add(new TwitterEvent(obj));
//			}
//		} finally {
//			cursor2.close();
//		}
		return events;
	}

	/**
	 * Gets all twitter event by date
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<TwitterEvent> getTwitterEventByDate(Date startDate,
			Date endDate) {
//		MongoClient mongoClient = MongoClientInstance.getInstance();
//		DB db = mongoClient.getDB(DATABASE_EVENT_DEMO);
		List<TwitterEvent> events = new ArrayList<TwitterEvent>();

		// BasicDBObject query = new BasicDBObject("tweetDate", );
//		DBCollection coll = db.getCollection(DATABASE_COLLECTION_TWITTER_EVENT);
//		BasicDBObject query = new BasicDBObject();
//		query.put("tweetDate", BasicDBObjectBuilder.start("$gte", startDate)
//				.add("$lte", endDate).get());
//		DBCursor cursor2 = coll.find(query);
//		// set index on a field
//
//		try {
//			while (cursor2.hasNext()) {
//				DBObject obj = cursor2.next();
//				TwitterEvent event = new TwitterEvent(obj);
//				events.add(event);
//			}
//		} finally {
//			cursor2.close();
//		}
		return events;
	}

	/**
	 * Add a twitter event to the collection
	 * @param event
	 * @throws TableExistsException 
	 * @throws AccumuloSecurityException 
	 * @throws AccumuloException 
	 */
	public static void addTwitterEvent(TwitterEvent event) throws AccumuloException, AccumuloSecurityException, TableExistsException {
		//ByteArrayOutputStream os = new ByteArrayOutputStream();
		//os.w
		byte[] rowId = event.getUsername().getBytes();
		byte[] familyB = event.getTweetDate().toString().getBytes();
		
		Connector aClient = AccumuloClientInstance.getInstance();
		BatchWriter wr = null;
		
		// Attempt to write -exception based programming
		while(true){
		   try {
			   wr = aClient.createBatchWriter(DATABASE_COLLECTION_TWITTER_EVENT, new BatchWriterConfig());
		    break;
		   } catch (TableNotFoundException e) {
		  	  // Create Table and retry
			  aClient.tableOperations().create(DATABASE_COLLECTION_TWITTER_EVENT);
		   }
		}
		
      Mutation m = new Mutation(rowId);		
      Text family = new Text(event.getTweetDate().toString());
      Text qual = new Text(event.getSource());
      Value value = new Value(event.toJson().getBytes());
      ColumnVisibility visibility = new ColumnVisibility("public");
      
      m.put(family, qual,visibility, value );
      
      wr.addMutation(m);
      wr.close();

	}
}

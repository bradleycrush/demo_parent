package org.event_manager.api;

import java.net.UnknownHostException;

import com.mongodb.MongoClient;

/**
 * Singleton accessor so we only have one MongoClient
 * @author ubuntu
 *
 */
public class MongoClientInstance {
	private static MongoClient mongoClient= null;
	
	private MongoClientInstance(){
	}
	
	public static synchronized MongoClient getInstance(){
	
		if ( mongoClient == null){
			try {
				mongoClient = new MongoClient();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mongoClient;
	}
}

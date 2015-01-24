package org.component_factory.engine;

import java.io.IOException;

import org.component_factory.EventDisambiguator;
import org.event_manager.events.Event;
import org.message_manager.api.MessageClientAPI;
import org.message_manager.api.MessageClientAPI.Queue;
import org.message_manager.common.ObjectSerializer;

import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * Start up a component factory to listen for events from rabbit mq
 * @author ubuntu
 *
 */
public class ComponentFactoryEngine {

	public static void main(String args[]) {
		System.out.println("Started Engine");
		
		EventDisambiguator disambiguator = new EventDisambiguator();
		
		MessageClientAPI client = new MessageClientAPI();
		
		/**
		 * Set up the consumer to listen for changes on the Events queue
		 */
		QueueingConsumer consumer = null;
		try {
			consumer = client.subscribe(Queue.Events);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		 while (true) {
			 QueueingConsumer.Delivery delivery;
			try {
				delivery = consumer.nextDelivery();
				Event event =  (Event) ObjectSerializer.deserialize(delivery.getBody());
				disambiguator.disambiguate(event);
				System.out.println(" [x] Received '" + event.toJson() + "'");
			} catch (ShutdownSignalException | ConsumerCancelledException
					| InterruptedException | ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			 }
		
		
	}

}

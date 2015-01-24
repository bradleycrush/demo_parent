package org.message_manager.pubsub;

import java.io.IOException;
import java.io.Serializable;

import org.message_manager.api.MessageClientAPI.Queue;
import org.message_manager.common.ObjectSerializer;
import org.springframework.amqp.core.AmqpTemplate;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * Controls publishing and subscribing through RabbitMQ
 * @author ubuntu
 *
 */
public class PubSubChannelClient {

	private final ApplicationContext context;
	private final AmqpTemplate template;
	
	private final Channel channel;
	private final Connection connection;
	
	
	public PubSubChannelClient() throws IOException {
		context = new GenericXmlApplicationContext(
				"classpath:/rabbit-context.xml");
		template = context.getBean(AmqpTemplate.class);
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		factory.setUsername("events");
		factory.setPassword("events");
	    connection = factory.newConnection();
	    channel = connection.createChannel();	    
	}

	public QueueingConsumer subscribe(Queue queue) throws IOException {
		 QueueingConsumer consumer = new QueueingConsumer(channel);
		 channel.basicConsume(queue.toString(), true, consumer);
		
		return consumer;
	}

	public boolean Send(Serializable message) throws IOException {
	    channel.basicPublish("", Queue.Events.toString(), null, ObjectSerializer.serialize(message));
		return true;
	}

	public Serializable getMessage(String queue) {
		Serializable message = (Serializable) template.receiveAndConvert(queue);
		return message;

	}
	
	public void close() throws IOException{
	    channel.close();
	    connection.close();
	}
	
	
}

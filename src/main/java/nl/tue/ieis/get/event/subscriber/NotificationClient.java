package main.java.nl.tue.ieis.get.event.subscriber;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import main.java.nl.tue.ieis.get.event.type.EventType;

import org.apache.activemq.ActiveMQConnectionFactory;


public class NotificationClient {
	
	private boolean continues = true;
	
	private String brokerHost;
	private String brokerPort;
	private String topic;
	private EventType eventType;
	
	private MessageConsumer consumer;
	private Session session;
	private Connection connection;
	
	
	public NotificationClient (String brokerHost, String brokerPort, String topic, EventType eventType) {
		this.brokerHost = brokerHost;
		this.brokerPort = brokerPort;
		this.topic = topic;
		this.eventType = eventType;
		
		initializeBrokerConnection();
	}
	
	public void setContinues(boolean continues) {
		this.continues = continues;
	}
	
	private void initializeBrokerConnection() {
		try {
			 
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(String.format("tcp://%s:%s", brokerHost, brokerPort));

            this.connection = connectionFactory.createConnection();
            connection.start();

            this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination destination1 = session.createQueue(topic);
            this.consumer = session.createConsumer(destination1);
            
            ProcessReceivedEvent pe = new ProcessReceivedEvent();
            
            for(EventThread et : EventSubscriberFactory.threads) {
    			if(et.getQueryId().contentEquals(topic)) {
    				et.setConnection(connection);
    				et.setConsumer(consumer);
    				et.setSession(session);
    			}
    		}
            
            
            while (continues) {
	            Message message1 = consumer.receive(0);
	
	            if (message1 instanceof TextMessage) {
	                TextMessage textMessage = (TextMessage) message1;
	                String text = textMessage.getText();
	                System.out.println("[" + eventType + "] == " + text);
	                pe.recordEvent(new Event(eventType, text));
	            } else {
	                //System.out.println("Received event is not text msg: " + message1);
	            }
            }
            
        } catch (Exception e) {
            //System.out.println("Caught: " + e.getMessage());
        }
	}
}

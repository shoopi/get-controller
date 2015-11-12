package main.java.nl.tue.ieis.get.event.subscriber;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;


public class EventThread {
	private String queryId;
	private String email;
	private Thread thread;

	private MessageConsumer consumer;
	private Session session;
	private Connection connection;
	
	private boolean continues = true;
	
	public EventThread(String queryId, String email) {
		super();
		this.queryId = queryId;
		this.email = email;
	}
	
	
	
	public void setConsumer(MessageConsumer consumer) {
		this.consumer = consumer;
	}



	public void setSession(Session session) {
		this.session = session;
	}



	public void setConnection(Connection connection) {
		this.connection = connection;
	}



	public void closeConnection() {
		try {
			consumer.close();
			session.close();
			connection.close();
			//System.out.println("Connection has been closed.");
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public boolean isContinues() {
		return continues;
	}
	public void setContinues(boolean continues) {
		this.continues = continues;
	}
	public String getQueryId() {
		return queryId;
	}
	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Thread getThread() {
		return thread;
	}
	public void setThread(Thread thread) {
		this.thread = thread;
	}
}
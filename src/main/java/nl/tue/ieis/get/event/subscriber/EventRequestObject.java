package main.java.nl.tue.ieis.get.event.subscriber;

import main.java.nl.tue.ieis.get.event.type.EventType;

public class EventRequestObject {
	
	 private String queryString;
	 private EventType eventType;

	 public EventRequestObject(String queryString, EventType eventType) {
		this.queryString = queryString;
		this.eventType = eventType;
	 }

	public String getQueryString() {
		return queryString;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	 
	 
}

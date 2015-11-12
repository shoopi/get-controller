package main.java.nl.tue.ieis.get.event.subscriber;

import java.util.HashMap;
import java.util.LinkedHashMap;

import main.java.nl.tue.ieis.get.activiti.taskDocumentation.TaskDocumentation.QueryAnnotation.Query;
import main.java.nl.tue.ieis.get.event.type.EventType;


public class Event {
	
	public static HashMap<String, Query> subscriptionMap = new LinkedHashMap<String, Query>();
	
	EventType eventType;
	String json;
	
	public EventType getEventType() {
		return eventType;
	}
	public String getJson() {
		return json;
	}
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public Event(EventType eventType, String json) {
		super();
		this.eventType = eventType;
		this.json = json;
	}
}

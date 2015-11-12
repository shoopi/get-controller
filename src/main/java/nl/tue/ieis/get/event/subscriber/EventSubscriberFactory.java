package main.java.nl.tue.ieis.get.event.subscriber;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.axis2.AxisFault;

import main.java.de.hpi.EventProcessingPlatformWebserviceStub;
import main.java.de.hpi.EventProcessingPlatformWebserviceStub.*;
import main.java.nl.tue.ieis.get.activiti.taskDocumentation.TaskDocumentation.QueryAnnotation.Query;


public class EventSubscriberFactory {
	
	public static List<EventThread> threads = new ArrayList<EventThread>();
	
	public static String subscribeEvent(final EventRequestObject request, final String email) {
		String returnValue = "";
		try {
			EventProcessingPlatformWebserviceStub ws = new EventProcessingPlatformWebserviceStub();
			System.out.println("Start Calling Event Engine for " + request.getEventType().toString() + " ...");
			final String queryTitle = UUID.randomUUID().toString();
			RegisterQueryForQueue registerQuery = new RegisterQueryForQueue();
			registerQuery.setTitle(queryTitle);
			registerQuery.setQueryString(request.getQueryString());
			registerQuery.setEMail(email);

			RegisterQueryForQueueResponse queryResponse = ws.registerQueryForQueue(registerQuery);
			final String queryResult = queryResponse.get_return();
			returnValue = queryResult;
			
			System.err.println("Query result for " + request.getEventType().toString() + " is: " + queryResult);
			final EventThread eventThread = new EventThread(queryResult, email);
			threads.add(eventThread);
			Thread thread = new Thread(new Runnable() {
				public void run() {
					 if(eventThread.isContinues())
						 new NotificationClient("bpt.hpi.uni-potsdam.de", "61616", queryResult, request.getEventType());
				 }
			});
			thread.setDaemon(false);
			eventThread.setThread(thread);
			thread.start();			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return returnValue;
		
	}
	
	public static void unsubscribe(String queryId) {
		try {
			EventProcessingPlatformWebserviceStub ws = new EventProcessingPlatformWebserviceStub();
			UnregisterQueryFromQueue unregisterQuery = new UnregisterQueryFromQueue();
			unregisterQuery.setUuid(queryId);
			UnregisterQueryFromQueueResponse queryResponse = ws.unregisterQueryFromQueue(unregisterQuery);
			System.err.println("Unregister " + queryId + " is: " + queryResponse.get_return());
			Event.subscriptionMap.remove(queryId);
			EventThread removed = null;
			for(EventThread et : threads) {
				if(et.getQueryId().contentEquals(queryId)) {
					 removed = et;
					 destroyThread(et);
					 break;
				}
			}
			if(threads.contains(removed))
				threads.remove(removed);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static boolean unsubscribeAll(String email) {
		try {
			EventProcessingPlatformWebserviceStub ws = new EventProcessingPlatformWebserviceStub();
			UnregisterQueriesFromQueue removeAllReceived = new UnregisterQueriesFromQueue();
			removeAllReceived.setEmail(email);
			UnregisterQueriesFromQueueResponse qResp = ws.unregisterQueriesFromQueue(removeAllReceived);
			System.err.println(qResp.get_return() + " queries of " + email + " have been removed.");
			
			List<EventThread> removed = new ArrayList<EventThread>();
			for(EventThread et : threads) {
				if(et.getEmail().contentEquals(email)) {
					 removed.add(et);
					 destroyThread(et);
				}
			}
			threads.removeAll(removed);			
			System.err.println("All threads of " + email + "(# " + removed.size() + ") have been stopped.");
			
			removeFromEventMaps(email);
			return true;
		} catch (AxisFault e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		} catch (RemoteException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	private static void removeFromEventMaps(String email) {
		List<String> activeSubscriptionForEmail = new ArrayList<String>();
		for(Map.Entry<String, Query> entry : Event.subscriptionMap.entrySet()) {
			if(entry.getValue().getEmail() != null && entry.getValue().getEmail().contentEquals(email))
				activeSubscriptionForEmail.add(entry.getKey());
		}
		for(String id : activeSubscriptionForEmail) {
			Event.subscriptionMap.remove(id);
		}
	}

	private static void destroyThread(EventThread et) {
		et.closeConnection();
		et.getThread().interrupt();
		et.setContinues(false);
	}

}

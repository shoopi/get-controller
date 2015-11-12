package main.java.nl.tue.ieis.get.event.publisher;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import main.java.de.hpi.EventProcessingPlatformWebserviceStub;
import main.java.de.hpi.EventProcessingPlatformWebserviceUnparsableExceptionException;
import main.java.de.hpi.EventProcessingPlatformWebserviceXMLParsingExceptionException;
import main.java.de.hpi.EventProcessingPlatformWebserviceStub.ImportEvents;

public class DelayedEvents {
	
	public static List<String> unpublishedEvents = new ArrayList<String>();
	
	public void sendUnpublished() {
		try {
			EventProcessingPlatformWebserviceStub ws = new EventProcessingPlatformWebserviceStub();
			if(unpublishedEvents.size() > 0) {
				List<String> temp = new ArrayList<String>();
				temp = unpublishedEvents;
				for(String eventString : temp) {
					ImportEvents e = new ImportEvents();
					e.setXml(eventString);
					try {
						ws.importEvents(e);
						unpublishedEvents.remove(eventString);
					} catch (EventProcessingPlatformWebserviceXMLParsingExceptionException e1) {
						System.out.println(e1.getMessage());
						e1.printStackTrace();
					} catch (EventProcessingPlatformWebserviceUnparsableExceptionException e1) {
						System.out.println(e1.getMessage());
						e1.printStackTrace();
					}
					/*
					boolean testSend = ws.importEvents(eventString);
					if(testSend) {
						unpublishedEvents.remove(eventString);
					}
					*/
				}
			}
		} catch (RemoteException e) {
			System.out.println("unable to send unpublished events now ... " + e.getMessage());
			//e.printStackTrace();
		}
	}
}

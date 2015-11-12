package main.java.nl.tue.ieis.get.event.publisher;

import java.io.StringWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import main.java.de.hpi.EventProcessingPlatformWebserviceCallbackHandler;
import main.java.de.hpi.EventProcessingPlatformWebserviceStub;
import main.java.de.hpi.EventProcessingPlatformWebserviceStub.ImportEvents;
import main.java.de.hpi.EventProcessingPlatformWebserviceStub.StoreRoute;
import main.java.nl.tue.ieis.get.event.type.activitiLog.ActivityLog;
import main.java.nl.tue.ieis.get.event.type.vehicleSelected.VehicleSelected;

public class PublishEvent {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	public static void publishActivityLogEvent(String processSpecId, String processInstanceId, String taskId, String taskState, String userId) {
		
		ActivityLog actLog = new ActivityLog();
		
		actLog.setProcessSpecId(processSpecId);
		actLog.setProcessInstanceId(processInstanceId);
		actLog.setTaskId(taskId);
		actLog.setTaskState(taskState);
		actLog.setUserId(userId);
		actLog.setTimestamp(sdf.format(new Date()));
		
		JAXBContext jaxbContext;
		StringWriter stringWriter;
		try {
			jaxbContext = JAXBContext.newInstance(ActivityLog.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "activityLogType.xsd");
			stringWriter = new StringWriter();
			jaxbMarshaller.marshal(actLog, stringWriter);

			try {
				EventProcessingPlatformWebserviceStub ws = new EventProcessingPlatformWebserviceStub();
				ImportEvents event = new ImportEvents();
				event.setXml(stringWriter.toString());
				ws.importEvents(event);
				System.out.println("Case " + processInstanceId + "		Task: [" + taskId + "]		State: [" + taskState + "]		User: [" + userId + "]");	
			} catch (Exception e) {
				System.out.println(e.getMessage());
				DelayedEvents.unpublishedEvents.add(stringWriter.toString());
				System.err.println("Event: [" + stringWriter.toString() + "] has been queued for future ...");
			}
		} catch (JAXBException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	public static void publishSelectedRoutet(String processInstanceId, String routeJson) {
		try {
			StoreRoute route = new StoreRoute();
			route.setJson(routeJson);
			
		    // non-blocking callback method for storing the route points
			EventProcessingPlatformWebserviceStub ws = new EventProcessingPlatformWebserviceStub();
			ws.startstoreRoute(route, new EventProcessingPlatformWebserviceCallbackHandler() {
		            // no callback necessary: engine sends no response anyway 
		    });
		    System.out.println("Route for case: " + processInstanceId +  " has been sent to the event engine.");
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	public static void publishVehicleSelectedEvent(String operatorId, String routeName, String processInstanceId) {
		
		VehicleSelected vehicleSelected = new VehicleSelected();
		vehicleSelected.setOperatorId(new BigInteger(operatorId));
		vehicleSelected.setRoute(routeName);
		vehicleSelected.setCaseId(new BigInteger(processInstanceId));
		vehicleSelected.setTimestamp(sdf.format(new Date()));
		
		JAXBContext jaxbContext;
		StringWriter stringWriter;
		try {
			jaxbContext = JAXBContext.newInstance(VehicleSelected.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "VehicleSelected.xsd");
			stringWriter = new StringWriter();
			jaxbMarshaller.marshal(vehicleSelected, stringWriter);

			//boolean submitLog;
			try {
				EventProcessingPlatformWebserviceStub ws = new EventProcessingPlatformWebserviceStub();
				ImportEvents event = new ImportEvents();
				event.setXml(stringWriter.toString());
				ws.importEvents(event);
				System.out.println("Case " + processInstanceId + "		Route: [" + routeName + "]		Operator: [" + operatorId + "]");
			} catch (Exception e) {
				System.out.println(e.getMessage());
				DelayedEvents.unpublishedEvents.add(stringWriter.toString());
				System.err.println("Event: [" + stringWriter.toString() + "] has been queued for future ...");
				//e.printStackTrace();
			}
		} catch (JAXBException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	public static void publishVehicleSelectedEventWithEATandDeadline(String operatorId, String routeName, String processInstanceId, Date deadline, Date eta) {
		
		VehicleSelected vehicleSelected = new VehicleSelected();
		vehicleSelected.setOperatorId(new BigInteger(operatorId));
		vehicleSelected.setRoute(routeName);
		vehicleSelected.setCaseId(new BigInteger(processInstanceId));
		vehicleSelected.setTimestamp(sdf.format(new Date()));
		vehicleSelected.setVehicleId(new BigInteger(operatorId));
		vehicleSelected.setDeadline(sdf.format(deadline));
		vehicleSelected.setETA(sdf.format(eta));
		
		JAXBContext jaxbContext;
		StringWriter stringWriter;
		try {
			jaxbContext = JAXBContext.newInstance(VehicleSelected.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "VehicleSelected.xsd");
			stringWriter = new StringWriter();
			jaxbMarshaller.marshal(vehicleSelected, stringWriter);
	
			//boolean submitLog;
			try {
				EventProcessingPlatformWebserviceStub ws = new EventProcessingPlatformWebserviceStub();
				ImportEvents event = new ImportEvents();
				event.setXml(stringWriter.toString());
				ws.importEvents(event);
				System.out.println("Case " + processInstanceId + "		Route: [" + routeName + "]		Operator: [" + operatorId + "]		ETA: [ " + eta + " ] 	DEADLINE: [ " + deadline + " ]");
			} catch (Exception e) {
				System.out.println(e.getMessage());
				DelayedEvents.unpublishedEvents.add(stringWriter.toString());
				System.err.println("Event: [" + stringWriter.toString() + "] has been queued for future ...");
				//e.printStackTrace();
			}
		} catch (JAXBException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
}

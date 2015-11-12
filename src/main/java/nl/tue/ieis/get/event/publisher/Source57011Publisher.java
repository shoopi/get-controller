package main.java.nl.tue.ieis.get.event.publisher;

import org.apache.axis2.AxisFault;

//import main.java.nl.tue.ieis.get.event.publisher.source.CreateTraceEventFromJdRData;
import main.java.nl.tue.ieis.get.event.publisher.source.CreateVehicleEventFromJdRData;

public class Source57011Publisher {
	public static void main(String[] args) {
		System.out.println("Load 57011 Publisher...");
		try {
			CreateVehicleEventFromJdRData.createTraceEvent("57011.xml");
		} catch (AxisFault e) {
			e.printStackTrace();
		}
	}
}

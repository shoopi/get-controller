package main.java.nl.tue.ieis.get.event.publisher;

import org.apache.axis2.AxisFault;

//import main.java.nl.tue.ieis.get.event.publisher.source.CreateTraceEventFromJdRData;
import main.java.nl.tue.ieis.get.event.publisher.source.CreateVehicleEventFromJdRData;

public class Source7256Publisher {
	public static void main(String[] args) {
		System.out.println("Load 7256 Publisher...");
		//CreateTraceEventFromJdRData.createTraceEvent("7256.xml");
		try {
			CreateVehicleEventFromJdRData.createTraceEvent("7256.xml");
		} catch (AxisFault e) {
			e.printStackTrace();
		}
	}
}

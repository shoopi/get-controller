package main.java.nl.tue.ieis.get.event.publisher;

import org.apache.axis2.AxisFault;

//import main.java.nl.tue.ieis.get.event.publisher.source.CreateTraceEventFromJdRData;
import main.java.nl.tue.ieis.get.event.publisher.source.CreateVehicleEventFromJdRData;

public class Source1849Publisher {
	public static void main(String[] args) {
		System.out.println("Load 1849 Publisher...");
		//CreateTraceEventFromJdRData.createTraceEvent("5246.xml");
		try {
			CreateVehicleEventFromJdRData.createTraceEvent("1849.xml");
		} catch (AxisFault e) {
			e.printStackTrace();
		}
	}
}

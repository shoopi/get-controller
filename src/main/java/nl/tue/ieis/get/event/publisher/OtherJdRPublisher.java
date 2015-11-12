package main.java.nl.tue.ieis.get.event.publisher;

import org.apache.axis2.AxisFault;

//import main.java.nl.tue.ieis.get.event.publisher.source.CreateTraceEventFromJdRData;
import main.java.nl.tue.ieis.get.event.publisher.source.CreateVehicleEventFromJdRData;

public class OtherJdRPublisher {

	public static void main(String[] args) {
		System.out.println("Load other Jan de Rijk Publisher...");
		//CreateTraceEventFromJdRData.createTraceEvent("OtherJdR.xml");
		try {
			CreateVehicleEventFromJdRData.createTraceEvent("OtherJdR.xml");
		} catch (AxisFault e) {
			e.printStackTrace();
		}
	}

}

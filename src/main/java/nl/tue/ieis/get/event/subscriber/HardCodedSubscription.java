package main.java.nl.tue.ieis.get.event.subscriber;

import main.java.nl.tue.ieis.get.event.type.EventType;

public class HardCodedSubscription {
	
	static EventSubscriberFactory eventSub = new EventSubscriberFactory();
	
	public static String subscribeUnexpectedEvent(String email) {
		try {
			String query = "Select source as source, title as title, coordinate.latitude as latitude, coordinate.longitude as longitude, delay as delay FROM unexpectedtrafficGETService";
			return EventSubscriberFactory.subscribeEvent(new EventRequestObject(query, EventType.UnexpectedEvent), email);
		} catch(Exception e) { 
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public static String subscribeVehicleIncident(String email) {
		try {
			String query = "Select timestamp, operatorId, description, longitude, latitude, type FROM VehicleIncident";
			return EventSubscriberFactory.subscribeEvent(new EventRequestObject(query, EventType.VehicleIncident), email);
		} catch(Exception e) { 
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public static String subscribeForVehicleLocationEventType(String email) {
		try {
			String query = "Select timestamp as timestamp, mobOperID as mobOperID, latitude as latitude, longitude as longitude, altitude as altitude FROM VehicleLocationEventType";
			return EventSubscriberFactory.subscribeEvent(new EventRequestObject(query, EventType.VehicleLocationEventType), email);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public static String subscribeForVehiclePositionUpdate(String email) {
		try {
			String query = "Select operatorId, timestamp, distance, duration, startTime, speed, currentLatitude, currentLongitude,  previousLatitude, previousLongitude from PositionUpdate";
			return EventSubscriberFactory.subscribeEvent(new EventRequestObject(query, EventType.PositionUpdate), email);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public static String subscribeForVehiclePosition (String email) {
		try {
			String query = "Select operatorId, timestamp, latitude, longitude,  mode from VehiclePosition";
			return EventSubscriberFactory.subscribeEvent(new EventRequestObject(query, EventType.VehiclePosition), email);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public static String subscribeForFlightTrace(String email) {
		try {
			String query = "SELECT flightId, departure.origin, departure.departurelatitude, departure.departurelongitude, arrival.destination, arrival.arrivallatitude, arrival.arrivallongitude, timestamp, aircraftId, flightNumber, latitude, longitude, speed, bearing, altitude FROM  Flighttrace";
			return EventSubscriberFactory.subscribeEvent(new EventRequestObject(query, EventType.Flighttrace), email);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public static String subscribeForRoadTrafficEventType(String email) {
		try {
			String query = "Select timestamp, areaCenter.longitude, areaCenter.latitude, idAtProvider, description, length, magnitude, provider, delay, type, identifier, roadsAffected from RoadTraffic";
			return EventSubscriberFactory.subscribeEvent(new EventRequestObject(query, EventType.RoadTraffic), email);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	/*
	public static String subscribeForLockClosedEventType(String email) {
		try {
			String query = "Select timestamp, delay, startTime, endTime, latitude, longitude, source from LockClosed";
			return EventSubscriberFactory.subscribeEvent(new EventRequestObject(query, EventType.LockClosed), email);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	*/
	public static String subscribeForDeadlineExpired(String email) {
		try {
			String query = "Select timestamp, operatorId, longitude, latitude, distanceRemaining, nodeDeadline from TransportDeadlineExpired";
			return EventSubscriberFactory.subscribeEvent(new EventRequestObject(query, EventType.TransportDeadlineExpired), email);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	/*
	public static String subsribeForFlightTrace(String email) {
		try {
			String query = " SELECT flightId, departure.origin, departure.departurelatitude, departure.departurelongitude, arrival.destination, arrival.arrivallatitude, arrival.arrivallongitude, timestamp, aircraftId, flightNumber, latitude, longitude, speed, bearing, altitude FROM  Flighttrace WHERE flightNumber='str1234'";
			return EventSubscriberFactory.subscribeEvent(new EventRequestObject(query, EventType.Flighttrace), email);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	*/
	
	public static String subsribeForCongestionOnRoute(String email) {
		try {
			String query = "SELECT predictedLength, predictedDelay, longitude, latitude, route FROM CongestionOnRoute";
			return EventSubscriberFactory.subscribeEvent(new EventRequestObject(query, EventType.CongestionOnRoute), email);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public static String subsribeForFlightDiversion(String email) {
		try {
			String query = " SELECT flightId, origin, departurelatitude,departurelongitude, destination,  arrivallatitude,arrivallongitude, certainty, aircraftId, flightCode, latitude, longitude, timestamp FROM FlightDiversionDetected";
			return EventSubscriberFactory.subscribeEvent(new EventRequestObject(query, EventType.FlightDiversionDetected), email);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
}

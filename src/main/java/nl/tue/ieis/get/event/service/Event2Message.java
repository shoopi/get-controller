package main.java.nl.tue.ieis.get.event.service;

import main.java.nl.tue.ieis.get.event.type.predictedDeadlineViolatio.PredictedDeadlineViolationEvent;
import main.java.nl.tue.ieis.get.event.type.startedFromTransportNode.StartedFromTransportNode;
import main.java.nl.tue.ieis.get.event.type.transportDeadlineExpired.*;
import main.java.nl.tue.ieis.get.event.type.transportDeadlineTransgression.TransportDeadlineTransgression;
import main.java.nl.tue.ieis.get.event.type.transportationAtRisk.TransportationAtRisk;
import main.java.nl.tue.ieis.get.event.type.transportationFinished.TransportationFinished;
import main.java.nl.tue.ieis.get.event.type.vehicleIncident.VehicleIncident;
import main.java.nl.tue.ieis.get.event.type.arrivedAtTransportNode.*;
import main.java.nl.tue.ieis.get.event.type.bargeCloseToTerminal.BargeCloseToTerminal;
import main.java.nl.tue.ieis.get.event.type.congestionOnRoute.*;
import main.java.nl.tue.ieis.get.event.type.flightDiversionDetected.FlightDiversion;

public class Event2Message {

	public static String event2message(Object event) {
		String message = "No Message Found!";
		
		if(event instanceof TransportDeadlineExpired) {
			message = "Vehicle " +  ((TransportDeadlineExpired) event).getOperatorId() + " missed deadline (";
			message = message.concat(((TransportDeadlineExpired) event).getNodeDeadline() + ") of transport node. ");
		} 
		
		else if (event instanceof ArrivedAtTransportNode) {
			message = "Vehicle " + ((ArrivedAtTransportNode) event).getOperatorId() + " reached transport node. " + 
					((ArrivedAtTransportNode) event).getNodeName();
		} 
		
		else if (event instanceof StartedFromTransportNode) {
			message = "Vehicle " + ((StartedFromTransportNode) event).getOperatorId() + " left transport node. " + 
					((StartedFromTransportNode) event).getNodeName();
		}
		
		else if (event instanceof TransportationFinished) {
			message = "The transportation involving vehicle " + ((TransportationFinished) event).getOperatorId() + " finished. ";
		}
		
		else if (event instanceof TransportDeadlineTransgression) {
			message = "Vehicle " + ((TransportDeadlineTransgression) event).getOperatorId() + " reached transport node with delay of " + 
					(double)(((TransportDeadlineTransgression) event).getDelay()/60000) + " minutes.";
		}
		
		else if (event instanceof CongestionOnRouteEvent) {
			message = "Congestion on route " + ((CongestionOnRouteEvent) event).getRoute() + " detected. Length: " + 
					((CongestionOnRouteEvent) event).getPredictedLength() + " , delay: " + (double)(((CongestionOnRouteEvent) event).getPredictedDelay()/60000) + " minutes."; 
		}
		
		else if (event instanceof main.java.nl.tue.ieis.get.event.type.flightDiversion.FlightTrace) {
			main.java.nl.tue.ieis.get.event.type.flightDiversion.FlightTrace flt = (main.java.nl.tue.ieis.get.event.type.flightDiversion.FlightTrace)event;
			message = "[FLIGHT DIVERSION] Flight " + flt.getFlightNumber() + " from " + flt.getDeparture().getOrigin() + " toward " + flt.getArrival().getDestination() +
					" has been diverted.";
		}
		
		else if (event instanceof main.java.nl.tue.ieis.get.event.type.flightDiversionDetectedShort.FlightTrace) {
			main.java.nl.tue.ieis.get.event.type.flightDiversionDetectedShort.FlightTrace flt = (main.java.nl.tue.ieis.get.event.type.flightDiversionDetectedShort.FlightTrace)event;
			message = "Flight " + flt.getAircraftId() + //" from " + flt.getDeparture().getOrigin() + " toward " + flt.getArrival().getDestination() +
					" has been diverted.";
		}
		
		else if (event instanceof main.java.nl.tue.ieis.get.event.type.congestionAhead.CongestionAheadEvent) {
			main.java.nl.tue.ieis.get.event.type.congestionAhead.CongestionAheadEvent msg = (main.java.nl.tue.ieis.get.event.type.congestionAhead.CongestionAheadEvent)event;
			message = "Congestion ahead has been detected. Predicted delay is: " + (double)(msg.getPredictedDelay()/60000) +
					" minutes and predicted length is " + msg.getPredictedLength() + "."; 
		}
		
		else if (event instanceof PredictedDeadlineViolationEvent) {
			PredictedDeadlineViolationEvent deadline = (PredictedDeadlineViolationEvent)event;
			//TODO: MESSAGE FOR DEADLINE VIOLATION
			message = "[DEADLINE VIOLATION] Predicted delay is " + deadline.getPredictedDelay()/60000 + " minutes.";// Remaining Distance is " + deadline.getDistanceRemaining() + " and ETA is " + deadline.getEta();
		}
		
		else if (event instanceof TransportationAtRisk) {
			TransportationAtRisk msg = (TransportationAtRisk)event;
			//TODO: MESSAGE FOR DEADLINE VIOLATION
			message = "[TRANSPORTATION AT RISK] Source is " + msg.getSource() + ", Node Deadline is " + msg.getNodeDeadline() + " and ETA is " + msg.getEta();
		
		}
		
		else if (event instanceof VehicleIncident) {
			VehicleIncident msg = (VehicleIncident)event;
				//TODO: MESSAGE FOR DEADLINE VIOLATION
				message = "[Vehicle Incident] Source is " + msg.getType() + ", Description is " + msg.getDescription();
		}
	
		else if (event instanceof FlightDiversion) {
			FlightDiversion msg = (FlightDiversion)event;
				message = "[Flight Diversion Detected] Flight " + msg.getFlightId() +" diverts with a certainty of " + msg.getCertainty() +
						" from the destination airport " + msg.getDestination() + ".";
		}
		
		else if (event instanceof BargeCloseToTerminal) {
			BargeCloseToTerminal msg = (BargeCloseToTerminal)event;
				message = "Next Terminals are: " + msg.getNextTerminal() + ".";
		}
	return message;
		
	}
}


/*

Event							Message																			Comment




TransportProgressTransgression																					Event not yet implemented
VehicleInCongestion																								Event not yet implemented


*/
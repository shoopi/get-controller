package main.java.nl.tue.ieis.get.event.subscriber;

import java.util.List;

import main.java.nl.tue.ieis.get.activiti.TaskObject;
import main.java.nl.tue.ieis.get.control.MapController;
import main.java.nl.tue.ieis.get.control.TaskServiceImpl;
import main.java.nl.tue.ieis.get.map.StatusCode;
import main.java.nl.tue.ieis.get.data.TransportOrderDataManagement;
import main.java.nl.tue.ieis.get.event.type.EventType;
import main.java.nl.tue.ieis.get.event.type.arrivedAtTransportNode.ArrivedAtTransportNode;
import main.java.nl.tue.ieis.get.event.type.bargeCloseToTerminal.BargeCloseToTerminal;
import main.java.nl.tue.ieis.get.event.type.bargePosition.BargePosition;
import main.java.nl.tue.ieis.get.event.type.congestionAhead.CongestionAheadEvent;
import main.java.nl.tue.ieis.get.event.type.congestionOnRoute.CongestionOnRouteEvent;
import main.java.nl.tue.ieis.get.event.type.flightTrace.FlightTrace;
import main.java.nl.tue.ieis.get.event.type.positionUpdate.PositionUpdate;
import main.java.nl.tue.ieis.get.event.type.predictedDeadlineViolatio.PredictedDeadlineViolationEvent;
import main.java.nl.tue.ieis.get.event.type.roadTraffic.RoadTrafficEvent;
import main.java.nl.tue.ieis.get.event.type.startedFromTransportNode.StartedFromTransportNode;
import main.java.nl.tue.ieis.get.event.type.trainPosition.TrainPosition;
import main.java.nl.tue.ieis.get.event.type.transportProgress.TransportProgress;
import main.java.nl.tue.ieis.get.event.type.unexpected.UnexpectedTraffic;
import main.java.nl.tue.ieis.get.event.type.vehicleIncident.VehicleIncident;
import main.java.nl.tue.ieis.get.event.type.vehiclePosition.VehiclePosition;
import main.java.nl.tue.ieis.get.event.type.warning.WarningEvent;
import main.java.nl.tue.ieis.get.event.type.transportDeadlineExpired.TransportDeadlineExpired;
import main.java.nl.tue.ieis.get.event.service.*;
import main.java.nl.tue.ieis.get.map.Icon;

import com.google.gson.Gson;


public class ProcessReceivedEvent {

    private TransportOrderDataManagement tpdm = new TransportOrderDataManagement();
    private Gson gson = new Gson();
        
    public void recordEvent(Event evt) {
    	try {
	    	String json = evt.getJson();
		    	if(evt.getEventType() == EventType.VehicleLocationEventType) {
		    		//vehicelLocationEventHandler(json);
	    	    	
	    	    } else if(evt.getEventType() == EventType.PositionUpdate) {
		    		positionUpdateEventHandler(json);
	    	    
	    	    } else if (evt.getEventType() == EventType.VehiclePosition) {
	    	    	vehiclePositionEventHandler(json);
	    	    	
	    	    } else if (evt.getEventType() == EventType.BargePosition) {
	    	    	bargePositionEventHandler(json);
	    	    	
	    	    } else if (evt.getEventType() == EventType.TrainPosition) {
	    	    	trainPositionEventHandler(json);
		    		
	    	    } else if (evt.getEventType() == EventType.UnexpectedEvent) {
	    	    	unexpectedEventHandler(json);

	    	    } else if (evt.getEventType() == EventType.ArrivedAtTransportNode) {
	    	    	arrivedAtTransportNodeHandler(json);
	    	    	
	    	    } else if (evt.getEventType() == EventType.TransportProgress) {
	    	    	transportProgressHandler(json);
	    	    	
	    	    } else if (evt.getEventType() == EventType.StartedFromTransportNode) {
	    	    	startedFromTransportNodeHandler(json);
	    	    	
	    	    } else if(evt.getEventType() == EventType.RoadTraffic) {
	    	    	roadTrafficHandler(json);

	    	    } else if(evt.getEventType() == EventType.TransportDeadlineExpired) {
	    	    	transportDeadlineExpiredHandler(json);
	    	    	
	    	    }  else if(evt.getEventType() == EventType.Flighttrace) {
	    	    	flightTraceEventHandler(json);
	    	    	
	    	    //} else if(evt.getEventType() == EventType.FlightDiversion) {
	    	    //	flightDiversionEventHandler(json);
	    	    	
	    	    } else if(evt.getEventType() == EventType.Warning) {
	    	    	flightWarningEventHandler(json);
	    	    	
	    	    } else if(evt.getEventType() == EventType.CongestionOnRoute) {
	    	    	congestionOnRouteEventHandler(json);
	    	    	
	    	    } else if(evt.getEventType() == EventType.CongestionAhead) {
	    	    	congestionAheadEventHandler(json);
	    	    	
	    	    } else if(evt.getEventType() == EventType.TransportationAtRisk) {
	    	    	transportationAtRiskEventHandler(json);	
	    	    	
	    	    } else if(evt.getEventType() == EventType.VehicleIncident) {
	    	    	vehicleIncidentEventHandler(json);	
	    	    	
	    	    } else if(evt.getEventType() == EventType.FlightDiversionDetectedShort) {
	    	    	flightDiversionDetectedShortHandler(json);
	    	    	
	    	    } else if(evt.getEventType() == EventType.FlightDiversionDetected) {
	    	    	flightDiversionDetectedHandler(json);
	    	    	
	    	    } else if(evt.getEventType() == EventType.PredictedDeadlineViolation) {
	    	    	predictedDeadlineViolationHandler(json);
	    	    	
	    	    } else if(evt.getEventType() == EventType.BargeCloseToTerminal) {
	    	    	findCloseTerminalEventHandler(json);
	    	    	
	    	    }
		    	
		    	//FlightDiversionDetectedShort
	    	    else { 
	    	    	MapController.showNotification("Event Type: " + evt.getEventType().toString() + " <br/> " + evt.getJson());
	    	    	System.err.println("Event Type: " + evt.getEventType().toString() + " <br/> " + evt.getJson());
	    	    }
    	}catch(Exception e) {
    		System.out.println(e.getMessage());
    	}
    }
    
    private void vehicleIncidentEventHandler(String json) {
    	VehicleIncident event = gson.fromJson(json, VehicleIncident.class);
		//String caseId = String.valueOf(event.getCaseId());
		List<String> caseIds = tpdm.loadCaseIdByTruck(event.getOperatorId() + "");
		if(caseIds != null && caseIds.size() > 0) {
			for(String c : caseIds) {
				if(isTransportationAvailable(c)) {
					String message = Event2Message.event2message(event);
					String icon = Icon.EVENT_VEHICLE_INCIDENT_ICON.getUrl();
					tpdm.addMessageEvent(c, event.getOperatorId()+"" , message, EventType.VehicleIncident.toString(), event.getLatitude()+"" , event.getLongitude()+"", icon);
				}
			}
		}
	}

	private void predictedDeadlineViolationHandler(String json) {
		PredictedDeadlineViolationEvent predictedDeadlineViolation = gson.fromJson(json, PredictedDeadlineViolationEvent.class);
		
		String message = Event2Message.event2message(predictedDeadlineViolation);
		
		List<String> caseIds = tpdm.loadCaseIdByTruck(predictedDeadlineViolation.getOperatorId() + "");
		caseIds.addAll(tpdm.loadCaseIdByTrain(predictedDeadlineViolation.getOperatorId() + ""));
		caseIds.addAll(tpdm.loadCaseIdByBarge(predictedDeadlineViolation.getOperatorId() + ""));
		
		if(caseIds != null && caseIds.size() > 0) {
			for(String c : caseIds) {
				if(isTransportationAvailable(c)) {
					String icon = Icon.EVENT_PREDICTED_DEADLINE_ICON.getUrl();
					tpdm.addMessageEvent(c, predictedDeadlineViolation.getOperatorId() + "", message, EventType.PredictedDeadlineViolation.toString(), predictedDeadlineViolation.getLatitude()+"", predictedDeadlineViolation.getLongitude()+"", icon);;
					tpdm.updateTransportStatus(c, StatusCode.TRANSPORTATION_DEADLINE_VIOLATION.getValue());
				}
			}
		}
	}

	private void flightDiversionDetectedHandler(String json) {
		main.java.nl.tue.ieis.get.event.type.flightDiversionDetected.FlightDiversion diversion = gson.fromJson(json, main.java.nl.tue.ieis.get.event.type.flightDiversionDetected.FlightDiversion.class);
		String flightId = diversion.getFlightId() + "";
		String message = Event2Message.event2message(diversion);
		String icon = Icon.EVENT_FLIGHT_DIVERSION_ICON.getUrl();
		tpdm.updateAssetStatus(flightId, StatusCode.FLIGHT_DIVERSION.getValue());

		List<String> caseIds = tpdm.loadCaseIdByFlight(flightId);
		if(caseIds != null && caseIds.size() > 0) {
			for(String caseId : caseIds) {
				tpdm.addMessageEvent(caseId, flightId , message, EventType.FlightDiversionDetectedShort.toString(), diversion.getLatitude()+"" , diversion.getLongitude()+"", icon);
				tpdm.updateTransportStatus(caseId, StatusCode.TRANSPORTATION_FLIGHT_DIVERSION.getValue());
			}
		}		
	}
    
	private void flightDiversionDetectedShortHandler(String json) {
		main.java.nl.tue.ieis.get.event.type.flightDiversionDetectedShort.FlightTrace diversion = gson.fromJson(json, main.java.nl.tue.ieis.get.event.type.flightDiversionDetectedShort.FlightTrace.class);
		String caseId = diversion.getCaseId();
		String message = Event2Message.event2message(diversion);
		String source = tpdm.loadFlightIdByCaseId(caseId);
		String icon = Icon.EVENT_FLIGHT_DIVERSION_ICON.getUrl();
		String id = diversion.getAircraftId().toString();
		if(caseId != null) {
			tpdm.addMessageEvent(caseId, source , message, EventType.FlightDiversionDetectedShort.toString(), diversion.getLatitude()+"" , diversion.getLongitude()+"", icon);
			tpdm.updateTransportStatus(caseId, StatusCode.TRANSPORTATION_FLIGHT_DIVERSION.getValue());
		}
		tpdm.updateAssetStatus(id, StatusCode.FLIGHT_DIVERSION.getValue());
		
	}
	
	private void transportationAtRiskEventHandler(String json) {
		main.java.nl.tue.ieis.get.event.type.transportationAtRisk.TransportationAtRisk event = gson.fromJson(json, main.java.nl.tue.ieis.get.event.type.transportationAtRisk.TransportationAtRisk.class);
		String caseId = event.getCaseId() + "";
		String message = Event2Message.event2message(event);
		String icon = Icon.EVENT_FLIGHT_DIVERSION_ICON.getUrl();
		tpdm.addMessageEvent(caseId, event.getVehicleId()+"" , message, EventType.TransportationAtRisk.toString(), event.getLatitude()+"" , event.getLongitude()+"", icon);
		if(event.getSource().contentEquals("LockClosed")) {
			tpdm.updateTransportStatus(caseId, StatusCode.TRANSPORTATION_LOCK_CLOSED.getValue());

		} else if (event.getSource().contentEquals("CongestionAhead")) {
			tpdm.updateTransportStatus(caseId, StatusCode.TRANSPORTATION_CONGESTION.getValue());
		}
		
	}
	
	private void congestionOnRouteEventHandler(String json) {
		CongestionOnRouteEvent congestion = gson.fromJson(json, CongestionOnRouteEvent.class);
		String message = Event2Message.event2message(congestion);
		String caseId = congestion.getCaseId();
		String icon = Icon.EVENT_TRAFFIC_ICON.getUrl();
		tpdm.addMessageEvent(caseId, "", message, EventType.CongestionOnRoute.toString(), congestion.getLatitude()+"", congestion.getLongitude()+"", icon);
		tpdm.updateTransportStatus(caseId, StatusCode.TRANSPORTATION_CONGESTION.getValue());
	}
	
	private void congestionAheadEventHandler(String json) {
		CongestionAheadEvent congestion = gson.fromJson(json, CongestionAheadEvent.class);
		String message = Event2Message.event2message(congestion);
		List<String> caseIds = tpdm.loadCaseIdByTruck(congestion.getOperatorId() + "");
		
		if(caseIds != null && caseIds.size() > 0) {
			for(String c : caseIds) {
				if(isTransportationAvailable(c)) {
					String icon = Icon.EVENT_TRAFFIC_ICON.getUrl();
					tpdm.addMessageEvent(c, "", message, EventType.CongestionAhead.toString(), congestion.getLatitude()+"", congestion.getLongitude()+"", icon);
					tpdm.updateTransportStatus(c, StatusCode.TRANSPORTATION_CONGESTION.getValue());
				}
			}
		}		
	}
	
	private void flightWarningEventHandler(String json) {
		WarningEvent warning = gson.fromJson(json, WarningEvent.class);
		String caseId = warning.getCaseId();
		String message = warning.getDescription();
		String icon = Icon.EVENT_FLIGHT_WARNING_ICON.getUrl();
		tpdm.addMessageEvent(caseId, "1234" , message, EventType.Warning.toString(), warning.getLatitude()+"" , warning.getLongitude()+"", icon);
		tpdm.updateTransportStatus(caseId, StatusCode.TRANSPORTATION_FLIGHT_WARNING.getValue());
	}
/*
	private void flightDiversionEventHandler(String json) {
		main.java.nl.tue.ieis.get.event.type.flightDiversion.FlightTrace diversion = gson.fromJson(json, main.java.nl.tue.ieis.get.event.type.flightDiversion.FlightTrace.class);
		String caseId = diversion.getCaseId();
		String message = Event2Message.event2message(diversion);
		String source = tpdm.loadAssetIdByCaseId(caseId);
		String icon = Icon.EVENT_FLIGHT_DIVERSION_ICON.getUrl();
		tpdm.addMessageEvent(caseId, source , message, EventType.FlightDiversion.toString(), diversion.getArrival().getArrivallatitude()+"" , diversion.getArrival().getArrivallongitude()+"", icon);
		tpdm.updateTransportStatus(caseId, StatusCode.TRANSPORTATION_FLIGHT_DIVERSION.getValue());
	}
*/
	private void flightTraceEventHandler(String json) {
		FlightTrace flightTrace = gson.fromJson(json, FlightTrace.class);
		String id = flightTrace.getFlightId().toString();
		if(id == null) id = "";
		int status = 0;
		if(tpdm.hasPreviousVehicleRecord(id)) {
			status = tpdm.loadAssetStatusById(id);
			tpdm.removePreviousVehicleRecord(id);
		} else {
			status = StatusCode.FLIGHT_BUSY.getValue();
		}
			tpdm.addVehicleTraceRecord(id, String.valueOf(flightTrace.getLatitude()), String.valueOf(flightTrace.getLongitude()), String.valueOf(flightTrace.getAltitude()), flightTrace.getTimestamp(), status, "");
	}

	private void transportDeadlineExpiredHandler(String json) {
		TransportDeadlineExpired deadline = gson.fromJson(json, TransportDeadlineExpired.class);
		String message = Event2Message.event2message(deadline);
		List<String> caseIds = tpdm.loadCaseIdByTruck(deadline.getOperatorId() + "");
		caseIds.addAll(tpdm.loadCaseIdByTrain(deadline.getOperatorId() + ""));
		caseIds.addAll(tpdm.loadCaseIdByBarge(deadline.getOperatorId() + ""));
		if(caseIds != null && caseIds.size() > 0) {
			for(String c : caseIds) {
				if(isTransportationAvailable(c)) {
					String icon = Icon.EVENT_DEADLINE_ICON.getUrl();
					tpdm.addMessageEvent(c, deadline.getOperatorId() + "", message, EventType.TransportDeadlineExpired.toString(), deadline.getLatitude()+"", deadline.getLongitude()+"", icon);
				
				}
			}
		}
	}

	private void roadTrafficHandler(String json) {
		//status = 1 ... Current Traffic to show
		RoadTrafficEvent traffic = gson.fromJson(json, RoadTrafficEvent.class);
		tpdm.addTrafficEvent(traffic.getIdAtProvider(), traffic.getProvider(), 
				String.valueOf(traffic.getAreaCenter().getLongitude()), String.valueOf(traffic.getAreaCenter().getLatitude()), 
				traffic.getTimestamp(), traffic.getType(), traffic.getDelay(), traffic.getLength(), traffic.getCause(), 
				traffic.getDescription(), traffic.getRoadsAffected(), traffic.getIdentifier(), traffic.getMagnitude(), 1);
	}

	private void startedFromTransportNodeHandler(String json) {
		StartedFromTransportNode startedFromTransportNode = gson.fromJson(json, StartedFromTransportNode.class);
		List<String> caseIds = tpdm.loadCaseIdByTruck(startedFromTransportNode.getOperatorId() + "");
		caseIds.addAll(tpdm.loadCaseIdByTrain(startedFromTransportNode.getOperatorId() + ""));
		caseIds.addAll(tpdm.loadCaseIdByBarge(startedFromTransportNode.getOperatorId() + ""));		
		if(caseIds != null && caseIds.size() > 0) {
			for(String c : caseIds) {
				if(isTransportationAvailable(c)) {
					String message = Event2Message.event2message(startedFromTransportNode);
					String icon = Icon.EVENT_START_AT_POINT_ICON.getUrl();
					tpdm.addMessageEvent(c, startedFromTransportNode.getOperatorId() + "" , message, EventType.StartedFromTransportNode.toString(), startedFromTransportNode.getLatitude()+"", startedFromTransportNode.getLongitude()+"", icon);
				}
			}		
		}
	}
	
	private void findCloseTerminalEventHandler(String json) {
		BargeCloseToTerminal event = gson.fromJson(json, BargeCloseToTerminal.class);
		List<String> caseIds = tpdm.loadCaseIdByBarge(event.getBargeId() + "");
		if(caseIds != null && caseIds.size() > 0) {
			for(String c : caseIds) {
				if(isTransportationAvailable(c)) {
					if(tpdm.hasPreviousMessageTypeForCase(c, EventType.BargeCloseToTerminal.toString())) {
						tpdm.removePreviousMessageTypeForCase(c, EventType.BargeCloseToTerminal.toString());
					}
					String message = Event2Message.event2message(event);
					String icon = Icon.EVENT_BARGE_CLOSE_TERMIANL.getUrl();
					tpdm.addMessageEvent(c, event.getBargeId() + "" , message, EventType.BargeCloseToTerminal.toString(), "0.000","0.000", icon);
				}
			}		
		}
	}

	private void transportProgressHandler(String json) {
		TransportProgress transportProgress = gson.fromJson(json, TransportProgress.class);
		double total = (double)(transportProgress.getDistanceCovered() + transportProgress.getDistanceRemaining());
		double perDouble = (double)( transportProgress.getDistanceCovered() / total);
		int per = (int) (perDouble * 100);
		List<String> caseIds = tpdm.loadCaseIdByTruck(transportProgress.getOperatorId() + "");
		caseIds.addAll(tpdm.loadCaseIdByTrain(transportProgress.getOperatorId() + ""));
		caseIds.addAll(tpdm.loadCaseIdByBarge(transportProgress.getOperatorId() + ""));			
		if(caseIds != null && caseIds.size() > 0) {
			for(String c : caseIds) {
				if(isTransportationAvailable(c)) {
					MapController.progressPercentage.put(c, per);
				}
			}
		}
	}

	private void arrivedAtTransportNodeHandler(String json) {
		ArrivedAtTransportNode arrivedAtTransportNode = gson.fromJson(json, ArrivedAtTransportNode.class);
		List<String> caseIds = tpdm.loadCaseIdByTruck(arrivedAtTransportNode.getOperatorId() + "");
		caseIds.addAll(tpdm.loadCaseIdByTrain(arrivedAtTransportNode.getOperatorId() + ""));
		caseIds.addAll(tpdm.loadCaseIdByBarge(arrivedAtTransportNode.getOperatorId() + ""));	
		if(caseIds != null && caseIds.size() > 0) {
			for(String c : caseIds) {
				if(isTransportationAvailable(c)) {
					String message = Event2Message.event2message(arrivedAtTransportNode);
					String icon = Icon.EVENT_ARRIVED_AT_POINT_ICON.getUrl();
					tpdm.addMessageEvent(c, arrivedAtTransportNode.getOperatorId() + "" , message, EventType.ArrivedAtTransportNode.toString(), arrivedAtTransportNode.getLatitude()+"", arrivedAtTransportNode.getLongitude()+"", icon);
					
					executeTask(c, "Drive (to warehouse)", "Automatic");
					executeTask(c, "Drive to warehouse", "Automatic");
					executeTask(c, "Drive" , "Automatic");
					executeTask(c, "Transport by truck", "Automatic");
					executeTask(c, "Transport by train", "Automatic");
					executeTask(c, "Drive to Harbor", "Automatic");
				}
			}
			
		}
				
	}

	private void unexpectedEventHandler(String json) {
		System.err.println("event is unexpected event \n" + json);
		UnexpectedTraffic unexpectedEvent = gson.fromJson(json, UnexpectedTraffic.class);
		String source = "";
		if(unexpectedEvent.getSource() != null)
			source = unexpectedEvent.getSource().toString();
		List<String> caseIds = tpdm.loadCaseIdByTruck(source);
		if(caseIds != null && caseIds.size() > 0) {
			tpdm.updateTransportStatus(caseIds.get(0), StatusCode.TRANSPORTATION_UNEXPECTED_EVENT.getValue());
		}
		if(tpdm.hasPreviousUnexpectedRecord(source)) {
			tpdm.removePreviousUnexpected(source);
		}
		tpdm.addUnexpectedEvent(source, unexpectedEvent.getCoordinate().getLatitude(), unexpectedEvent.getCoordinate().getLongitude(), unexpectedEvent.getDelay().intValue(), unexpectedEvent.getTitle());
	}
	
	private void vehiclePositionEventHandler(String json) {
		VehiclePosition trace = gson.fromJson(json, VehiclePosition.class);
		String mobileOperatorId = "";
		if(trace.getOperatorId() != null)
			mobileOperatorId = trace.getOperatorId().toString();
		
		List<String> caseIds = tpdm.loadCaseIdByTruck(trace.getOperatorId() + "");
		caseIds.addAll(tpdm.loadCaseIdByTrain(trace.getOperatorId() + ""));
		caseIds.addAll(tpdm.loadCaseIdByBarge(trace.getOperatorId() + ""));
		
		int status = StatusCode.TRUCK_FREE.getValue();
		if(trace.getMode() != null) {
			if (trace.getMode().equalsIgnoreCase("rail"))
				status = StatusCode.TRAIN_FREE.getValue();
			else if (trace.getMode().equalsIgnoreCase("Barge"))
				status = StatusCode.BARGE_FREE.getValue();
				
		}
		for(String caseId : caseIds) {
			if(isTransportationAvailable(caseId)) {
				status = StatusCode.TRUCK_BUSY.getValue();
				if(trace.getMode() != null) {
					if(trace.getMode().equalsIgnoreCase("rail"))
						status = StatusCode.TRAIN_BUSY.getValue();
					else if (trace.getMode().equalsIgnoreCase("Barge"))
						status = StatusCode.BARGE_BUSY.getValue();
				}
			}
		}
		if(tpdm.hasPreviousVehicleRecord(mobileOperatorId)) 
			tpdm.removePreviousVehicleRecord(mobileOperatorId);
		tpdm.addVehicleTraceRecord(mobileOperatorId, String.valueOf(trace.getLatitude()), String.valueOf(trace.getLongitude()), "0.0", trace.getTimestamp().toString(), status, trace.getEta());
	}
	
	
	
	
	
	private void positionUpdateEventHandler(String json) {
		PositionUpdate trace = gson.fromJson(json, PositionUpdate.class);
		String mobileOperatorId = "";
		if(trace.getOperatorId() != null)
			mobileOperatorId = trace.getOperatorId().toString();
		
		List<String> caseIds = tpdm.loadCaseIdByTruck(trace.getOperatorId() + "");
		caseIds.addAll(tpdm.loadCaseIdByTrain(trace.getOperatorId() + ""));
		caseIds.addAll(tpdm.loadCaseIdByBarge(trace.getOperatorId() + ""));
		
		int status = StatusCode.TRUCK_FREE.getValue();
		if(trace.getMode() != null) {
			/* For Empty Train for future improvement */
			if (trace.getMode().equalsIgnoreCase("rail"))
				status = StatusCode.TRAIN_FREE.getValue();
			else if (trace.getMode().equalsIgnoreCase("Barge"))
				status = StatusCode.BARGE_FREE.getValue();
				
		}
		for(String caseId : caseIds) {
			if(isTransportationAvailable(caseId)) {
				status = StatusCode.TRUCK_BUSY.getValue();
				if(trace.getMode() != null) {
					if (trace.getMode().equalsIgnoreCase("rail"))
						status = StatusCode.TRAIN_BUSY.getValue();
					else if (trace.getMode().equalsIgnoreCase("Barge"))
						status = StatusCode.BARGE_BUSY.getValue();
				}
			}
		}
		
		if(tpdm.hasPreviousVehicleRecord(mobileOperatorId)) 
			tpdm.removePreviousVehicleRecord(mobileOperatorId);
		tpdm.addVehicleTraceRecord(mobileOperatorId, String.valueOf(trace.getCurrentLatitude()), String.valueOf(trace.getCurrentLongitude()), String.valueOf(trace.getDistance()), trace.getTimestamp().toString(), status, trace.getEta());
	}
	
	private void bargePositionEventHandler(String json) {
		BargePosition trace = gson.fromJson(json, BargePosition.class);
		String mobileOperatorId = "";
		if(trace.getOperatorId() != null)
			mobileOperatorId = trace.getOperatorId().toString();
		List<String> caseIds = tpdm.loadCaseIdByBarge(trace.getOperatorId() + "");
		//TODO: if the case ID is correct I can change BARGE_BUSY here to BARGE_FREE
		int status = StatusCode.BARGE_FREE.getValue();
		//int status = StatusCode.BARGE_BUSY.getValue();
		for(String caseId : caseIds) {
			if(isTransportationAvailable(caseId)) {
				status = StatusCode.BARGE_BUSY.getValue();
			}
		}
		if(tpdm.hasPreviousVehicleRecord(mobileOperatorId)) 
			tpdm.removePreviousVehicleRecord(mobileOperatorId);
		tpdm.addVehicleTraceRecord(mobileOperatorId, String.valueOf(trace.getLatitude()), String.valueOf(trace.getLongitude()), "0.00", trace.getTimestamp().toString(), status, null);
	}
	
	private void trainPositionEventHandler(String json) {
		TrainPosition trace = gson.fromJson(json, TrainPosition.class);
		String mobileOperatorId = "";
		if(trace.getOperatorId() != null)
			mobileOperatorId = trace.getOperatorId().toString();
		List<String> caseIds = tpdm.loadCaseIdByTrain(trace.getOperatorId() + "");
		//TODO: if the case ID is correct I can change BARGE_BUSY here to BARGE_FREE
		//int status = StatusCode.BARGE_FREE.getValue();
		int status = StatusCode.TRAIN_BUSY.getValue();
		for(String caseId : caseIds) {
			if(isTransportationAvailable(caseId)) {
				status = StatusCode.TRAIN_BUSY.getValue();
			}
		}
		if(tpdm.hasPreviousVehicleRecord(mobileOperatorId)) 
			tpdm.removePreviousVehicleRecord(mobileOperatorId);
		tpdm.addVehicleTraceRecord(mobileOperatorId, String.valueOf(trace.getLatitude()), String.valueOf(trace.getLongitude()), "0.00", trace.getTimestamp().toString(), status, null);
	}
	
    private void executeTask(String instanceId, String taskDesignName, String userId) {
		TaskServiceImpl taskService = new TaskServiceImpl(instanceId, userId);
		List<TaskObject> runningTask = taskService.getExecutingTasks();
		for(TaskObject t : runningTask) {
			if((t.getTaskName().toLowerCase().trim()).contentEquals((taskDesignName).toLowerCase().trim())) {
				try {
					taskService.completeTask(null, "", t.getTaskId());
					break;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				
			}
		}
	}
    
    private boolean isTransportationAvailable(String caseId) {
		return 	tpdm.loadTransportStatusByCaseId(caseId) != StatusCode.TRANSPORTATION_SUSPENDED.getValue() 				||
				tpdm.loadTransportStatusByCaseId(caseId) != StatusCode.TRANSPORTATION_SUSPENDED_REMOVED.getValue() 		||
				tpdm.loadTransportStatusByCaseId(caseId) != StatusCode.TRANSPORTATION_FINISHED.getValue();
	}

}

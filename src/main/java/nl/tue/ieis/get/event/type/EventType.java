package main.java.nl.tue.ieis.get.event.type;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "eventType")
@XmlEnum(String.class)
public enum EventType {
	@XmlEnumValue("ActivityLog")
	ActivityLog,
	
	@XmlEnumValue("TraceNew")
	TraceNew,
	
	@XmlEnumValue("UnexpectedEvent")
	UnexpectedEvent,
	
	@XmlEnumValue("VehicleLocationEventType")
	VehicleLocationEventType,
	
	@XmlEnumValue("LoadContainerType")
	LoadContainerType,
	
	@XmlEnumValue("UnloadContainerType")
	UnloadContainerType,
		
	@XmlEnumValue("ExecuteActivity")
	ExecuteActivity,
	
	@XmlEnumValue("ArrivedAtTransportNode")
	ArrivedAtTransportNode,
	
	@XmlEnumValue("CongestionAhead")
	CongestionAhead,
	
	@XmlEnumValue("CongestionOnRoute")
	CongestionOnRoute,
	
	@XmlEnumValue("PositionUpdate")
	PositionUpdate,
	
	@XmlEnumValue("PredictedDeadlineViolation")
	PredictedDeadlineViolation,
	
	@XmlEnumValue("ProbableCongestionDelay")
	ProbableCongestionDelay,
	
	@XmlEnumValue("PTVRouteCoordinate")
	PTVRouteCoordinate,
	
	@XmlEnumValue("RoadTraffic")
	RoadTraffic,
	
	@XmlEnumValue("RoadTrafficUpdate")
	RoadTrafficUpdate,
	
	@XmlEnumValue("StartedFromTransportNode")
	StartedFromTransportNode,
	
	@XmlEnumValue("TransportationFinished")
	TransportationFinished,
	
	@XmlEnumValue("TransportationStarted")
	TransportationStarted,
	
	@XmlEnumValue("TransportDeadlineExpired")
	TransportDeadlineExpired,
	
	@XmlEnumValue("TransportDeadlineTransgression")
	TransportDeadlineTransgression,
	
	@XmlEnumValue("TransportProgress")
	TransportProgress,
	
	@XmlEnumValue("TransportProgressTransgression")
	TransportProgressTransgression,
	
	@XmlEnumValue("VehicleEnteredCongestion")
	VehicleEnteredCongestion,
	
	@XmlEnumValue("VehicleInCongestion")
	VehicleInCongestion,
	
	@XmlEnumValue("VehiclePosition")
	VehiclePosition,
	
	@XmlEnumValue("VehicleSelected")
	VehicleSelected,
	
	@XmlEnumValue("Warning")
	Warning,
	
	@XmlEnumValue("FlightDiversion")
	FlightDiversion,
	
	@XmlEnumValue("Flighttrace")
	Flighttrace,
	
	@XmlEnumValue("FlightDiversionDetectedShort")
	FlightDiversionDetectedShort,
	
	@XmlEnumValue("FlightDiversionDetected")
	FlightDiversionDetected,
	
	@XmlEnumValue("TruckPosition")
	TruckPosition,
	
	@XmlEnumValue("TrainPosition")
	TrainPosition,
	
	@XmlEnumValue("BargePosition")
	BargePosition,
	
	@XmlEnumValue("LockClosed")
	LockClosed,
	
	@XmlEnumValue("TransportationAtRisk")
	TransportationAtRisk,
	
	@XmlEnumValue("BargeCloseToTerminal")
	BargeCloseToTerminal,
	
	@XmlEnumValue("VehicleIncident")
	VehicleIncident;
	
	public String value() {
        return name();
    }

    public static EventType fromValue(String v) {
        return valueOf(v.trim());
    }
}

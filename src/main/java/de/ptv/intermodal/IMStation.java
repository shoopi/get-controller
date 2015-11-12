package main.java.de.ptv.intermodal;

public class IMStation {
	
	private IMPoint Point;
	private int AccTime;
	private int AccDistance;
	private int AccCost;
	private IMEmission AccEmission;
	private int AccTransfers;
	private String Deadline;
	private String LocationExtId;
	private String LocationName;
	private String LocationCountry;
	private String LocationCity;
	private String LocationStreet;
	private String LocationPostalCode;
	private String LocationHouseNumber;
	private int FirstPolygonPointIndex;
	private int LastPolygonPointIndex;
	private String LineExtId;	
	private String OperatorExtId;
	private String OperatorName;
	//private String Arrival;
	//private String Departure;
	
	public String getOperatorName() {
		return OperatorName;
	}
	public void setOperatorName(String operatorName) {
		OperatorName = operatorName;
	}
	String TransportMode;
	boolean Accompanied;
	IMStationTimes StationTimes;
	IMStationTimeEvent[] TimeEvents;
	
	
	public IMPoint getPoint() {
		return Point;
	}
	public int getAccTime() {
		return AccTime;
	}
	public int getAccDistance() {
		return AccDistance;
	}
	public int getAccCost() {
		return AccCost;
	}
	public IMEmission getAccEmission() {
		return AccEmission;
	}
	public int getAccTransfers() {
		return AccTransfers;
	}
	public String getLocationExtId() {
		return LocationExtId;
	}
	public String getLocationName() {
		return LocationName;
	}
	public String getLocationCountry() {
		return LocationCountry;
	}
	public String getLocationCity() {
		return LocationCity;
	}
	public String getLocationStreet() {
		return LocationStreet;
	}
	public String getLocationPostalCode() {
		return LocationPostalCode;
	}
	public String getLocationHouseNumber() {
		return LocationHouseNumber;
	}
	public int getFirstPolygonPointIndex() {
		return FirstPolygonPointIndex;
	}
	public int getLastPolygonPointIndex() {
		return LastPolygonPointIndex;
	}
	public String getLineExtId() {
		return LineExtId;
	}
	public String getOperatorExtId() {
		return OperatorExtId;
	}
	public String getTransportMode() {
		return TransportMode;
	}
	public boolean isAccompanied() {
		return Accompanied;
	}
	public IMStationTimes getStationTimes() {
		return StationTimes;
	}
	
	public void setPoint(IMPoint point) {
		Point = point;
	}
	public void setAccTime(int accTime) {
		AccTime = accTime;
	}
	public void setAccDistance(int accDistance) {
		AccDistance = accDistance;
	}
	public void setAccCost(int accCost) {
		AccCost = accCost;
	}
	public void setAccEmission(IMEmission accEmission) {
		AccEmission = accEmission;
	}
	public void setAccTransfers(int accTransfers) {
		AccTransfers = accTransfers;
	}
	public void setLocationExtId(String locationExtId) {
		LocationExtId = locationExtId;
	}
	public void setLocationName(String locationName) {
		LocationName = locationName;
	}
	public void setLocationCountry(String locationCountry) {
		LocationCountry = locationCountry;
	}
	public void setLocationCity(String locationCity) {
		LocationCity = locationCity;
	}
	public void setLocationStreet(String locationStreet) {
		LocationStreet = locationStreet;
	}
	public void setLocationPostalCode(String locationPostalCode) {
		LocationPostalCode = locationPostalCode;
	}
	public void setLocationHouseNumber(String locationHouseNumber) {
		LocationHouseNumber = locationHouseNumber;
	}
	public void setFirstPolygonPointIndex(int firstPolygonPointIndex) {
		FirstPolygonPointIndex = firstPolygonPointIndex;
	}
	public void setLastPolygonPointIndex(int lastPolygonPointIndex) {
		LastPolygonPointIndex = lastPolygonPointIndex;
	}
	public void setLineExtId(String lineExtId) {
		LineExtId = lineExtId;
	}
	public void setOperatorExtId(String operatorExtId) {
		OperatorExtId = operatorExtId;
	}
	public void setTransportMode(String transportMode) {
		TransportMode = transportMode;
	}
	public void setAccompanied(boolean accompanied) {
		Accompanied = accompanied;
	}
	public void setStationTimes(IMStationTimes stationTimes) {
		StationTimes = stationTimes;
	}
	public IMStationTimeEvent[] getTimeEvents() {
		return TimeEvents;
	}
	public void setTimeEvents(IMStationTimeEvent[] timeEvents) {
		TimeEvents = timeEvents;
	}
	/*
	public String getArrival() {
		return Arrival;
	}
	public void setArrival(String Arrival) {
		this.Arrival = Arrival;
	}
	public String getDeparture() {
		return Departure;
	}
	public void setDeparture(String Departure) {
		this.Departure = Departure;
	}
	*/
	public String getDeadline() {
		return Deadline;
	}
	public void setDeadline(String deadline) {
		Deadline = deadline;
	}
}

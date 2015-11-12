package main.java.de.ptv.intermodal;

public class IMStopOff {

	//Geocoded coordinates for this stop.
	IMPoint Point;
	 
	//The LocationExt is a unique identifier for this stop. 
	String LocationExtId;
		 	
	String LocationName;
	
	//LocationCountry contains the Iso2 code of the country.
	String LocationCountry;
	 
	String LocationCity;
	
	String LocationStreet;
	
	String LocationPostalCode;
	
	String LocationHouseNumber;
	
	//OPTIONAL: if used, this is the code of an existing Terminal in the router.
	String TerminalExtId;
	
	//standard = false 
	boolean IsTerminal = false;

	public IMPoint getPoint() {
		return Point;
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

	public String getTerminalExtId() {
		return TerminalExtId;
	}

	public boolean isIsTerminal() {
		return IsTerminal;
	}

	public void setPoint(IMPoint point) {
		Point = point;
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

	public void setTerminalExtId(String terminalExtId) {
		TerminalExtId = terminalExtId;
	}

	public void setIsTerminal(boolean isTerminal) {
		IsTerminal = isTerminal;
	}
	
	
		 	
}

package main.java.de.ptv.intermodal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IMStationTimes {

	int Arrival;
	int Departure;
	int HandlingTime;
	int WaitingTime;
	int Costs;
	IMEmission Emission;
	Date ArrivalTime;
	Date DepartureTime;
	double CarbonDioxide;
	public int getArrival() {
		return Arrival;
	}
	public int getDeparture() {
		return Departure;
	}
	public int getHandlingTime() {
		return HandlingTime;
	}
	public int getWaitingTime() {
		return WaitingTime;
	}
	public int getCosts() {
		return Costs;
	}
	public IMEmission getEmission() {
		return Emission;
	}
	public Date getArrivalTime() {
		return ArrivalTime;
	}
	public Date getDepartureTime() {
		return DepartureTime;
	}
	public double getCarbonDioxide() {
		return CarbonDioxide;
	}
	public void setArrival(int arrival) {
		Arrival = arrival;
	}
	public void setDeparture(int departure) {
		Departure = departure;
	}
	public void setHandlingTime(int handlingTime) {
		HandlingTime = handlingTime;
	}
	public void setWaitingTime(int waitingTime) {
		WaitingTime = waitingTime;
	}
	public void setCosts(int costs) {
		Costs = costs;
	}
	public void setEmission(IMEmission emission) {
		Emission = emission;
	}
	public void setArrivalTime(String arrivalTimeStr) {
		//DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-mm-dd'T'HH:mm:ss.SSSSSSSZ");
		//ArrivalTime = formatter.parseDateTime(arrivalTimeStr);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			ArrivalTime = df.parse(arrivalTimeStr);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	public void setDepartureTime(String departureTimeStr) {
		//DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-mm-dd'T'HH:mm:ss.SSSSSSSZ");
		//DepartureTime = formatter.parseDateTime(departureTimeStr);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			DepartureTime = df.parse(departureTimeStr);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	public void setCarbonDioxide(double carbonDioxide) {
		CarbonDioxide = carbonDioxide;
	}
	
	
}

package main.java.de.ptv.intermodal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IMRoute {
	
	int TotalCost;
	String Name;
	int TotalTravelTime;
	int TotalDistance;
	int Transfers;
	Date Start;
	IMPoint[] Polygon;
	IMEmission TotalEmission;
	double[][] 	PolygonLine;
	IMStation[] Stations;
	
	
	public int getTotalCost() {
		return TotalCost;
	}
	public String getName() {
		return Name;
	}
	public int getTotalTravelTime() {
		return TotalTravelTime;
	}
	public int getTotalDistance() {
		return TotalDistance;
	}
	public int getTransfers() {
		return Transfers;
	}
	public Date getStart() {
		return Start;
	}


	public IMEmission getTotalEmission() {
		return TotalEmission;
	}
	

	public void setTotalCost(int totalCost) {
		TotalCost = totalCost;
	}
	public void setName(String name) {
		Name = name;
	}
	public void setTotalTravelTime(int totalTravelTime) {
		TotalTravelTime = totalTravelTime;
	}
	public void setTotalDistance(int totalDistance) {
		TotalDistance = totalDistance;
	}
	public void setTransfers(int transfers) {
		Transfers = transfers;
	}
	public void setStart(String startStr) {
		//DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-mm-dd'T'HH:mm:ss.SSSSSSSZ");
		//Start = formatter.parseDateTime(startStr);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			Start = df.parse(startStr);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public IMPoint[] getPolygon() {
		return Polygon;
	}
	public double[][] getPolygonLine() {
		return PolygonLine;
	}
	public void setPolygon(IMPoint[] polygon) {
		Polygon = polygon;
	}
	public void setPolygonLine(double[][] polygonLine) {
		PolygonLine = polygonLine;
	}
	public void setTotalEmission(IMEmission totalEmission) {
		TotalEmission = totalEmission;
	}
	
	public IMStation[] getStations() {
		return Stations;
	}
	public void setStations(IMStation[] stations) {
		Stations = stations;
	}
}

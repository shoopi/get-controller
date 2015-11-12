package main.java.de.ptv.intermodal;

public class IMStationTimeEvent {

	String 	Type;
	int StartTime;
	int Duration;
	int Cost;
	IMEmission Emission;
	double CarbonDioxide;
	public String getType() {
		return Type;
	}
	public int getStartTime() {
		return StartTime;
	}
	public int getDuration() {
		return Duration;
	}
	public int getCost() {
		return Cost;
	}
	public IMEmission getEmission() {
		return Emission;
	}
	public double getCarbonDioxide() {
		return CarbonDioxide;
	}
	public void setType(String type) {
		Type = type;
	}
	public void setStartTime(int startTime) {
		StartTime = startTime;
	}
	public void setDuration(int duration) {
		Duration = duration;
	}
	public void setCost(int cost) {
		Cost = cost;
	}
	public void setEmission(IMEmission emission) {
		Emission = emission;
	}
	public void setCarbonDioxide(double carbonDioxide) {
		CarbonDioxide = carbonDioxide;
	}
}

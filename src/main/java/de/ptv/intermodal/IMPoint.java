package main.java.de.ptv.intermodal;

public class IMPoint {

	double Latitude;
	double Longitude;
	
	public IMPoint(double lng, double lat) {
		this.Longitude = lng;
		this.Latitude = lat;
	}
	
	public IMPoint() {}
	
	public double getLatitude() {
		return Latitude;
	}
	public double getLongitude() {
		return Longitude;
	}
	public void setLatitude(double latitude) {
		Latitude = latitude;
	}
	public void setLongitude(double longitude) {
		Longitude = longitude;
	}
	
	
}

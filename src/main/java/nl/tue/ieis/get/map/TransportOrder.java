package main.java.nl.tue.ieis.get.map;

import org.joda.time.DateTime;

public class TransportOrder {
	
	private String id;
	private String originAddress;
	private String destAddress;
	private DateTime availableTimeAtOrigin;
	private DateTime closingTimeAtDest;

	public TransportOrder(String id, String originAddress, String destAddress,
			DateTime availableTimeAtOrigin, DateTime closingTimeAtDest) {
		super();
		this.id = id;
		this.originAddress = originAddress;
		this.destAddress = destAddress;
		this.availableTimeAtOrigin = availableTimeAtOrigin;
		this.closingTimeAtDest = closingTimeAtDest;
	}
	
	public String getId() {
		return id;
	}
	public String getOriginAddress() {
		return originAddress;
	}
	public String getDestAddress() {
		return destAddress;
	}
	public DateTime getAvailableTimeAtOrigin() {
		return availableTimeAtOrigin;
	}
	public DateTime getClosingTimeAtDest() {
		return closingTimeAtDest;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setOriginAddress(String originAddress) {
		this.originAddress = originAddress;
	}
	public void setDestAddress(String destAddress) {
		this.destAddress = destAddress;
	}
	public void setAvailableTimeAtOrigin(DateTime availableTimeAtOrigin) {
		this.availableTimeAtOrigin = availableTimeAtOrigin;
	}
	public void setClosingTimeAtDest(DateTime closingTimeAtDest) {
		this.closingTimeAtDest = closingTimeAtDest;
	}

}

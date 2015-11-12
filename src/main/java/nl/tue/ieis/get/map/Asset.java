package main.java.nl.tue.ieis.get.map;

import main.java.de.ptv.intermodal.IMPoint;


public class Asset {

	private IMPoint point;
	private int status;
	private String mobOpId;
	private String eta;
	
	public IMPoint getPoint() {
		return point;
	}
	public void setPoint(IMPoint point) {
		this.point = point;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public String getMobOpId() {
		return mobOpId;
	}
	public void setMobOpId(String mobOpId) {
		this.mobOpId = mobOpId;
	}
	
	public String getEta() {
		return eta;
	}
	public void setEta(String eta) {
		this.eta = eta;
	}
	/*
	public Asset(IMPoint point, int status, String mobOpId) {
		super();
		this.point = point;
		this.status = status;
		this.mobOpId = mobOpId;
	}
	*/
	public Asset(IMPoint point, int status, String mobOpId, String eta) {
		super();
		this.point = point;
		this.status = status;
		this.mobOpId = mobOpId;
		this.eta = eta;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mobOpId == null) ? 0 : mobOpId.hashCode());
		result = prime * result + status;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Asset other = (Asset) obj;
		if (mobOpId == null) {
			if (other.mobOpId != null)
				return false;
		} else if (!mobOpId.equals(other.mobOpId))
			return false;
		if (status != other.status)
			return false;
		return true;
	}
}

package main.java.nl.tue.ieis.get.event.service;

public class MessageObject {

	private String caseId;
	private String sourceId;
	private String message;
	private String type;
	private String iconURL;
	private String latitude;
	private String longitude;
	
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getIconURL() {
		return iconURL;
	}
	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}
	public MessageObject(String caseId, String sourceId, String message,
			String type, String iconURL, String latitude, String longitude) {
		super();
		this.caseId = caseId;
		this.sourceId = sourceId;
		this.message = message;
		this.type = type;
		this.iconURL = iconURL;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public boolean equals(Object o) {
		return ((o instanceof MessageObject) && 
				((((MessageObject) o).getCaseId()).equals(this.caseId)) && 
				((((MessageObject) o).getIconURL()).equals(this.iconURL)) &&
				((((MessageObject) o).getLatitude()).equals(this.latitude)) &&
				((((MessageObject) o).getLongitude()).equals(this.longitude)) &&
				((((MessageObject) o).getMessage()).equals(this.message)) &&
				((((MessageObject) o).getSourceId()).equals(this.sourceId)) &&
				((((MessageObject) o).getType()).equals(this.type)));
	}
	
	public int hashCode() {
        return caseId.hashCode() ^ iconURL.hashCode() ^ latitude.hashCode() ^ longitude.hashCode() ^ message.hashCode() ^ sourceId.hashCode() ^ type.hashCode();
    }
}

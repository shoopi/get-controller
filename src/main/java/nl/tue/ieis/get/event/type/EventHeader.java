package main.java.nl.tue.ieis.get.event.type;

public class EventHeader {
	protected String timestamp;
	protected String ProcessInstances;
	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * @return the processInstances
	 */
	public String getProcessInstances() {
		return ProcessInstances;
	}
	/**
	 * @param processInstances the processInstances to set
	 */
	public void setProcessInstances(String processInstances) {
		ProcessInstances = processInstances;
	}
	
	
}

package main.java.nl.tue.ieis.get.activiti.service;

import org.activiti.rest.service.api.RestActionRequest;

public class DynamicAdaptationRequest extends RestActionRequest {

	  private String oldCase;
	  private String newProcessId;
	  private String route;
	  private String sourceAddress;
	  private String destAddress;
	  private String sourceDate;
	  private String destDate;
	  
	public String getOldCase() {
		return oldCase;
	}
	public void setOldCase(String oldCase) {
		this.oldCase = oldCase;
	}
	public String getNewProcessId() {
		return newProcessId;
	}
	public void setNewProcessId(String newProcessId) {
		this.newProcessId = newProcessId;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public String getSourceAddress() {
		return sourceAddress;
	}
	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
	}
	public String getDestAddress() {
		return destAddress;
	}
	public void setDestAddress(String destAddress) {
		this.destAddress = destAddress;
	}
	public String getSourceDate() {
		return sourceDate;
	}
	public void setSourceDate(String sourceDate) {
		this.sourceDate = sourceDate;
	}
	public String getDestDate() {
		return destDate;
	}
	public void setDestDate(String destDate) {
		this.destDate = destDate;
	} 
}
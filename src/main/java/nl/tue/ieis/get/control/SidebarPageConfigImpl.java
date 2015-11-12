package main.java.nl.tue.ieis.get.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import main.java.nl.tue.ieis.get.activiti.ProcessInstanceFunctions;
import main.java.nl.tue.ieis.get.map.*;
import main.java.nl.tue.ieis.get.data.TransportOrderDataManagement;
import main.java.nl.tue.ieis.get.services.*;
import main.java.nl.tue.ieis.get.event.service.*;

public class SidebarPageConfigImpl implements SidebarPageConfig{

	private static HashMap<String,SidebarPage> pageMap = new LinkedHashMap<String,SidebarPage>();

	//for lcoal DB connection
	TransportOrderDataManagement tpdm = new TransportOrderDataManagement();
	ProcessInstanceFunctions instanceFunc = new ProcessInstanceFunctions();
		
	// CASE ID: 0 --> ALL CASES  
	// CASE ID: -1 --> NO CASE or ERROR
	
	public SidebarPageConfigImpl() {		
		try {
			ArrayList<String> caseIDs = instanceFunc.getAllRunningProcessInstanceIDs();
			pageMap.clear();
			if(caseIDs.size() == 0) {
				pageMap.put("noActiveCaseKey", new SidebarPage("noActiveCaseName","No Running Case",Icon.SIDEBAR_ERROR.getUrl(),"", "-1"));
				//MapController.runningCases = new ArrayList<String>();
			} else {
				//to make consistent with the number of cases in show all
				pageMap.put("0Key", new SidebarPage("0Name", "Show All",Icon.SIDEBAR_SHOW_ALL.getUrl(),"", "0"));
					
				for(String caseID : caseIDs) {
					String defaultIcon = Icon.SIDEBAR_TRUCK.getUrl();
					if(tpdm.loadTransportStatusByCaseId(caseID) == StatusCode.TRANSPORTATION_RUNNING.getValue()) {
						String assetId = tpdm.loadTruckIdByCaseId(caseID);
						if(assetId != null && assetId.length() > 1) {
							Asset asset = tpdm.loadAssetById(assetId);
							if(asset != null) {
								if(asset.getStatus() == StatusCode.TRAIN_BUSY.getValue())
									defaultIcon = Icon.SIDEBAR_TRAIN.getUrl();
								else if (asset.getStatus() == StatusCode.TRUCK_BUSY_REPLANNED.getValue())
									defaultIcon = Icon.SIDEBAR_TRUCK_REPLANNED.getUrl();
							}
						}
						String flightId = tpdm.loadFlightIdByCaseId(caseID);
						if(flightId != null && flightId.length() > 1) {
							Asset asset = tpdm.loadAssetById(flightId);
							if(asset != null) {
								if(asset.getStatus() == StatusCode.FLIGHT_BUSY.getValue())
									defaultIcon = Icon.SIDEBAR_FLIGHT.getUrl();
								else if (asset.getStatus() == StatusCode.FLIGHT_DIVERSION.getValue())
									defaultIcon = Icon.SIDEBAR_FLIGHT_REPLAN.getUrl();
							}
						}
					}
					
					else if(tpdm.loadTransportStatusByCaseId(caseID) == StatusCode.TRANSPORTATION_UNEXPECTED_EVENT.getValue()) {
						defaultIcon = Icon.SIDEBAR_UNEXPECTED_EVENT.getUrl();
					} 
					
					else if (tpdm.loadTransportStatusByCaseId(caseID) == StatusCode.TRANSPORTATION_FLIGHT_DIVERSION.getValue()) {
						defaultIcon = Icon.SIDEBAR_FLIGHT_DIVERSION.getUrl();
					} 
					
					else if (tpdm.loadTransportStatusByCaseId(caseID) == StatusCode.TRANSPORTATION_FLIGHT_WARNING.getValue()) {
						defaultIcon = Icon.SIDEBAR_FLIGHT_WARNING.getUrl();
					} 
					
					else if (tpdm.loadTransportStatusByCaseId(caseID) == StatusCode.TRANSPORTATION_LOCK_CLOSED.getValue()) {
						defaultIcon = Icon.SIDEBAR_LOCK_CLOSED.getUrl();
					} 
					
					else if (tpdm.loadTransportStatusByCaseId(caseID) == StatusCode.TRANSPORTATION_CONGESTION.getValue()) {
						defaultIcon = Icon.SIDEBAR_TRUCK_CONGESTION.getUrl();
					} 
					
					else if (tpdm.loadTransportStatusByCaseId(caseID) == StatusCode.TRANSPORTATION_DEADLINE_VIOLATION.getValue()) {
						defaultIcon = Icon.SIDEBAR_TRUCK_DEADLINE.getUrl();
					} 
					
					
					pageMap.put(caseID + "Key", new SidebarPage(caseID +"Name", "Case " + caseID, defaultIcon,"", caseID));
				}	
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			pageMap.put("errorKey", new SidebarPage("errorName","No Connection to Workflow Engine found.",Icon.SIDEBAR_ERROR.getUrl(),"", "-1"));
		}
	}
	
	public List<SidebarPage> getPages(){
		return new ArrayList<SidebarPage>(pageMap.values());
	}
	
	public SidebarPage getPage(String name){
		return pageMap.get(name);
	}

	public List<MessageObject> updateMessageWindow(String caseId) {
		List<MessageObject> msgs = tpdm.loadEventMessageByCaseId(caseId);
		Collections.reverse(msgs);
		return msgs;
	}
}

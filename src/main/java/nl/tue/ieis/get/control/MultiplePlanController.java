package main.java.nl.tue.ieis.get.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import main.java.nl.tue.ieis.get.activiti.InstanceMigration;
import main.java.nl.tue.ieis.get.activiti.config.ActivitiEngineConfig;
import main.java.nl.tue.ieis.get.data.TransportOrderDataManagement;
import main.java.nl.tue.ieis.get.event.publisher.PublishEvent;
import main.java.nl.tue.ieis.get.map.ProcessPTVPlan;
import main.java.nl.tue.ieis.get.map.StatusCode;
import main.java.nl.tue.ieis.get.map.TransportRequest;
import main.java.nl.tue.ieis.get.services.CaseService;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.Button;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Window;

public class MultiplePlanController extends SelectorComposer<Component> {
	
	private static final long serialVersionUID = 2348163917018549342L;
	private TransportOrderDataManagement tpdm = new TransportOrderDataManagement();
	private List<TransportRequest> requests = new ArrayList<TransportRequest>();
	
	@Wire	private Listbox requestListbox;
	@Wire	private Window 	multiplePlanWin;
	@Wire 	private Button plannAllBtn;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		requestListbox.setMultiple(true);
		requests = tpdm.loadAllTransportRequestsWithoutCaseId();
		ListModelList<TransportRequest> model = new ListModelList<TransportRequest>(requests);
		model.setMultiple(true);
		requestListbox.setModel(model);
	}
	
	
	@Listen("onClick = #plannAllBtn")
    public void planSelected() {
        Set<Listitem> selectedItems = requestListbox.getSelectedItems();
        List<TransportRequest> okRequest = new ArrayList<TransportRequest>();
        boolean isDemoScenario3Offline = true;
        boolean isDemoScenario3Online = true;
        if(selectedItems.size() == 4) {
	        for(Listitem i : selectedItems) {
	        	TransportRequest t = (TransportRequest)i.getValue();
	        	if(!(t.getSource().getAddress().getName().trim().contentEquals("Kechnec Industrial Park")
	        			||t.getSource().getAddress().getName().trim().contentEquals("Budapest Mahart Container Center"))) {
	        		isDemoScenario3Offline = false;
	        		break;
	        	}
	        	if(!(t.getDest().getAddress().getName().trim().contentEquals("RoLa-Terminal Hafen")
	        			||t.getDest().getAddress().getName().trim().contentEquals("München Riem Ubf")
	        			||t.getDest().getAddress().getName().trim().contentEquals("Linz CCT Stadthafen"))) {
	        		isDemoScenario3Offline = false;
	        		break;
	        	}
	        	if(isDemoScenario3Offline) {
	        		okRequest.add(t);
		        } else {
		        	String message = "The multiple orders planning service is not available online. We will calculate your request offline as soon as possible.";
					Messagebox.Button[] buttons = { Messagebox.Button.OK}; 
					String icon = Messagebox.INFORMATION;
					EventListener<ClickEvent> eventListener = new EventListener<ClickEvent>() {
						public void onEvent(ClickEvent event) {
							if (Messagebox.ON_OK.equals(event.getName())) {
							}
						}
					};
					Messagebox.show(message, "Multiple Orders", buttons, icon, eventListener);
		        }
	        }
        } else {
        	isDemoScenario3Offline = false;
        	if(selectedItems.size() == 2) {
        		for(Listitem i : selectedItems) {
    	        	TransportRequest t = (TransportRequest)i.getValue();
    	        	if(!(t.getSource().getAddress().getName().trim().contentEquals("Container Terminal Enns (CTE)") ||
    	        			t.getSource().getAddress().getName().trim().contentEquals("Kechnec Industrial Park"))) {
    	        		isDemoScenario3Online = false;
    	        		break;
    	        	}
    	        	if(!t.getDest().getAddress().getName().trim().contentEquals("RoLa-Terminal Hafen")) {
    	        		isDemoScenario3Online = false;
    	        		break;
    	        	}
    	        	if(isDemoScenario3Online) {
    		        	okRequest.add(t);
    		        } else {
    		        	String message = "The multiple orders planning service is not available online. We will calculate your request offline as soon as possible.";
    					Messagebox.Button[] buttons = { Messagebox.Button.OK}; 
    					String icon = Messagebox.INFORMATION;
    					EventListener<ClickEvent> eventListener = new EventListener<ClickEvent>() {
    						public void onEvent(ClickEvent event) {
    							if (Messagebox.ON_OK.equals(event.getName())) {
    							}
    						}
    					};
    					Messagebox.show(message, "Multiple Orders", buttons, icon, eventListener);
    		        }
    	        }
        	} else {
        		isDemoScenario3Online = false;
        	}
        }
        if(isDemoScenario3Offline) {
        	runInlandWaterWayDemoOffline(okRequest);
        } else if (isDemoScenario3Online) {
        	runInlandWaterWayDemoOnline(okRequest);
        } else {
        	Clients.showNotification("Make sure you have selected correct entries.");
        }
    }
    

	
	private void runInlandWaterWayDemoOffline(List<TransportRequest> trasnportList) {
		String processKey1 = ActivitiEngineConfig.inlandWaterwayScenarioProcessModel1;
		String processKey2 = ActivitiEngineConfig.inlandWaterwayScenarioProcessModel2;
		String processKey3 = ActivitiEngineConfig.inlandWaterwayScenarioProcessModel3;
		String processKey4 = ActivitiEngineConfig.inlandWaterwayScenarioProcessModel4;

		TransportOrderDataManagement dbMan = new TransportOrderDataManagement();
		CaseService caseService = new CaseServiceImpl();
		String instanceId1 = caseService.createNewCase(processKey1, new HashMap<String, Object> ());
		String instanceId2 = caseService.createNewCase(processKey2, new HashMap<String, Object> ());
		String instanceId3 = caseService.createNewCase(processKey3, new HashMap<String, Object> ());
		String instanceId4 = caseService.createNewCase(processKey4, new HashMap<String, Object> ());
		
		String request1 = "", request2 = "", request3 = "", request4 = "";
		String date_source1 = "", date_dest1 = "", date_source2 = "", date_dest2 = "", date_source3 = "", date_dest3 = "", date_source4 = "", date_dest4 = "";
		for(TransportRequest t : trasnportList) {
			if(t.getSource().getAddress().getName().trim().contentEquals("Kechnec Industrial Park")) {
				//TODO: separate based on the time
				if(request1.length() > 2) {
					request2 = t.getId();
					date_source2 = t.getSource().getDate().toString();
					date_dest2 = t.getDest().getDate().toString();
				} else {
					request1 = t.getId();
					date_source1 = t.getSource().getDate().toString();
					date_dest1 = t.getDest().getDate().toString();
				}
			} else {
				if(t.getDest().getAddress().getName().trim().contentEquals("Linz CCT Stadthafen")) {
					request3 = t.getId();
					date_source3 = t.getSource().getDate().toString();
					date_dest3 = t.getDest().getDate().toString();
				} else {
					request4 = t.getId();
					date_source4 = t.getSource().getDate().toString();
					date_dest4 = t.getDest().getDate().toString();
				}
			}
		}
		
		dbMan.updateCaseIdForRequest(request1, instanceId1 + ",");
		dbMan.updateCaseIdForRequest(request2, instanceId2 + ",");
		dbMan.updateCaseIdForRequest(request3, instanceId3 + ",");
		dbMan.updateCaseIdForRequest(request4, instanceId4 + ",");
		
		String route1 = ProcessPTVPlan.getJsonString(ProcessPTVPlan.localJsonRouteResponseForInlandWaterwayOffline(1));
		String route2 = ProcessPTVPlan.getJsonString(ProcessPTVPlan.localJsonRouteResponseForInlandWaterwayOffline(2));
		String route3 = ProcessPTVPlan.getJsonString(ProcessPTVPlan.localJsonRouteResponseForInlandWaterwayOffline(3));
		String route4 = ProcessPTVPlan.getJsonString(ProcessPTVPlan.localJsonRouteResponseForInlandWaterwayOffline(4));
		
		
		dbMan.addTransportOrder(instanceId1, route1, date_source1, date_dest1, StatusCode.TRANSPORTATION_RUNNING.getValue());
		dbMan.addTransportOrder(instanceId2, route2, date_source2, date_dest2, StatusCode.TRANSPORTATION_RUNNING.getValue());
		dbMan.addTransportOrder(instanceId3, route3, date_source3, date_dest3, StatusCode.TRANSPORTATION_RUNNING.getValue());
		dbMan.addTransportOrder(instanceId4, route4, date_source4, date_dest4, StatusCode.TRANSPORTATION_RUNNING.getValue());
		
		ActivitiEngineConfig.scenario3_cases.add(instanceId1);
		ActivitiEngineConfig.scenario3_cases.add(instanceId2);
		ActivitiEngineConfig.scenario3_cases.add(instanceId3);
		ActivitiEngineConfig.scenario3_cases.add(instanceId4);	
		
		try {
			PublishEvent.publishSelectedRoutet(instanceId1, route1);
			PublishEvent.publishSelectedRoutet(instanceId2, route2);
			PublishEvent.publishSelectedRoutet(instanceId3, route3);
			PublishEvent.publishSelectedRoutet(instanceId4, route4);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void runInlandWaterWayDemoOnline(List<TransportRequest> trasnportList) {
		String processKey1 = ActivitiEngineConfig.inlandWaterwayScenarioProcessModel_online1;
		String processKey2 = ActivitiEngineConfig.inlandWaterwayScenarioProcessModel_online2;

		TransportRequest tr1 = new TransportRequest();
		TransportRequest tr2 = new TransportRequest();

		/*
		for(TransportRequest t : trasnportList) {
			if(t.getDest().getAddress().getName().trim().contentEquals("Linz CCT Stadthafen")) {
				tr1 = t;
			} else {
				tr2 = t;
			}
		}
		*/
		
		tr1 = trasnportList.get(0);
		tr2 = trasnportList.get(1);
		
		String route1 = ProcessPTVPlan.getJsonString(ProcessPTVPlan.localJsonRouteResponseForInlandWaterwayOnline());
		
		InstanceMigration migration1 = new InstanceMigration(false, ActivitiEngineConfig.scenario3_cases.get(0));
		migration1.doMigration(processKey1, route1, tr1, false);
		
		InstanceMigration migration2 = new InstanceMigration(false, ActivitiEngineConfig.scenario3_cases.get(1));
		migration2.doMigration(processKey2, route1, tr2, false);
	}
}
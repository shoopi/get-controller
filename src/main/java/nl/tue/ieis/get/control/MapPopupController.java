package main.java.nl.tue.ieis.get.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.de.ptv.intermodal.*;
import main.java.de.ptv.xserver.XIntermodalWSServiceStub;
import main.java.de.ptv.xserver.XIntermodalWSServiceStub.CalculateRoute;
import main.java.de.ptv.xserver.XIntermodalWSServiceStub.CalculateRouteResponse;
import main.java.de.ptv.xserver.XIntermodalWSServiceStub.CalculateRouteResponseE;
import main.java.de.ptv.xserver.XIntermodalWSServiceStub.RoutingAlternative;
import main.java.de.ptv.xserver.XIntermodalWSServiceStub.RoutingResponse;
import main.java.de.ptv.xserver.XIntermodalWSServiceStub.CalculateRouteE;
import main.java.de.ptv.xserver.XIntermodalWSServiceStub.RoutingRequest;
import main.java.nl.tue.ieis.get.activiti.ProcessDefinitionFunctions;
import main.java.nl.tue.ieis.get.activiti.TaskObject;
import main.java.nl.tue.ieis.get.activiti.config.ActivitiEngineConfig;
import main.java.nl.tue.ieis.get.map.StatusCode;
import main.java.nl.tue.ieis.get.data.TransportOrderDataManagement;
import main.java.nl.tue.ieis.get.event.publisher.PublishEvent;
import main.java.nl.tue.ieis.get.event.subscriber.*;
import main.java.nl.tue.ieis.get.json.rpc.PTVClientReceiver;
import main.java.nl.tue.ieis.get.map.ProcessPTVPlan;
import main.java.nl.tue.ieis.get.map.Asset;
import main.java.nl.tue.ieis.get.services.CaseService;
import main.java.nl.tue.ieis.get.simulation.FindDistance;

import org.activiti.engine.identity.User;
import org.joda.time.DateTime;
import org.zkoss.gmaps.Gmaps;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.East;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.South;
import org.zkoss.zul.Timer;
import org.zkoss.zul.West;

public class MapPopupController extends SelectorComposer<Component> {
	
	private static final long serialVersionUID = 4107910278717185858L;
	
	private CaseService caseService = new CaseServiceImpl();
	private MapServiceImpl mapService = new MapServiceImpl();

	private static boolean isPTVLoginRequired = false;
    static PTVClientReceiver rjson = new PTVClientReceiver(isPTVLoginRequired);
    
    private List<IMRoute> imRoutes = new ArrayList<IMRoute>();
    private ProcessDefinitionFunctions pdf = new ProcessDefinitionFunctions();
    private TransportOrderDataManagement dbMan = new TransportOrderDataManagement();

    
    private String selectedItemLabel = "";
    private String selectedJsonRoute = "{}";
    private boolean assetCheck = false;
    private boolean hasTruckId = false;
    
    @Wire	private East eastLayout;
    @Wire	private West westLayout;
    @Wire	private South southLayout;
	@Wire	private Grid alternativeList, truckList;
	@Wire	private Gmaps gmaps;
	@Wire	private Listbox resultListBox;
	@Wire	private Menuitem addMenuItem;
	@Wire	private Timer mapTimer;
	@Wire	private Auxheader truckListHeader;
	@Wire	private Textbox selectedTruckTextbox;//, idTextBox;
	@Wire	private Button selectAssetButton, selectProcessModelBtn;
	@Wire	private Combobox processComboBox;
	@Wire	private	Spinner instanceCounter;

	private String queueTitleVehiclePosition = "";
	private String queueTitleFlightTrace = "";
	
	private boolean showFlights = false;

	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		try{
			mapTimer.stop();
			//selectedTruckTextbox.setDisabled(true);
			gmaps.setZoom(6);
			
			//TODO:Just for Demo
			String demo = (String)(Sessions.getCurrent()).getAttribute("demo");
			String jsonReq = (String)(Sessions.getCurrent()).getAttribute("jsonRouteRequest");
			RoutingRequest rq = (RoutingRequest)(Sessions.getCurrent()).getAttribute("objectRouteRequest");
			ProcessPTVPlan ptv = new ProcessPTVPlan();
			if(jsonReq != null) {				
				//TODO for Demo
				if(demo.contentEquals("export1"))  				imRoutes = ptv.localJsonRouteResponseForExportCase(false);
				else if (demo.contentEquals("export2"))  		imRoutes = ptv.localJsonRouteResponseForExportCase(true);
				else if (demo.contentEquals("freightShift1")) 	imRoutes = ptv.localJsonRouteResponseForFreightShift1();
				else if (demo.contentEquals("freightShift2")) 	imRoutes = ptv.localJsonRouteResponseForFreightShift2();
				else if (demo.contentEquals("inlandWaterway")) 	imRoutes = ptv.localJsonRouteResponseForInlandWaterway(false);
				else if (demo.contentEquals("inlandWaterway2")) imRoutes = ptv.localJsonRouteResponseForInlandWaterway(true);
				else imRoutes = ptv.receiveJsonRouteResponse(jsonReq);
				
			} else if(rq != null){
				CalculateRouteE cs = new CalculateRouteE();
				CalculateRoute c = new CalculateRoute();
				c.setRoutingRequest_1(rq);
				cs.setCalculateRoute(c);
				CalculateRouteResponseE rre = new CalculateRouteResponseE();
				XIntermodalWSServiceStub service = new XIntermodalWSServiceStub();
				service._getServiceClient().getOptions().setProperty(org.apache.axis2.Constants.Configuration.DISABLE_SOAP_ACTION, true);

				rre = service.calculateRoute(cs);
				CalculateRouteResponse rrs = rre.getCalculateRouteResponse();
				RoutingResponse rr = rrs.getResult();
				List<RoutingAlternative> routingAlternativeList = new ArrayList<RoutingAlternative>(); 
				if(rr.getWrappedRoutes().getRoutingAlternative() != null) {
					routingAlternativeList = new ArrayList<RoutingAlternative>(Arrays.asList(rr.getWrappedRoutes().getRoutingAlternative()));
					routingAlternativeList = Arrays.asList(rr.getWrappedRoutes().getRoutingAlternative());
					imRoutes.addAll(ptv.converAlternativeRoutesToIMRoutes(routingAlternativeList));
					
				}
			}
			resultListBox.setModel(new ListModelList<IMRoute>(imRoutes));
			Rows rows = alternativeList.getRows();
			Row firstRow = constructMenuItem("All Routes", null);
			rows.appendChild(firstRow);
			for(IMRoute route : imRoutes) {
				Row row = constructMenuItem(route.getName(), route);
				rows.appendChild(row);
			}
			
			EventListener<MouseEvent> actionListener = new SerializableEventListener<MouseEvent>() {
				private static final long serialVersionUID = -6010198351381726898L;
				public void onEvent(MouseEvent event) throws Exception {
					checkRouteConfirmation();
	            }
	        };
	        addMenuItem.addEventListener(Events.ON_CLICK, actionListener);
	        addMenuItem.addEventListener(Events.ON_RIGHT_CLICK, actionListener);
	        
	        mapService.showAllRoutes(gmaps, imRoutes);
	        
	        boolean updateSuspendedCase = false;
			if((Sessions.getCurrent()).getAttribute("updateSuspendedCase") != null)
				updateSuspendedCase = (boolean) (Sessions.getCurrent()).getAttribute("updateSuspendedCase"); 
	        if(updateSuspendedCase)
	        	addMenuItem.setLabel("Update Route");
	        else
	        	addMenuItem.setLabel("Select Route");
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private Row constructMenuItem(final String label, final IMRoute route) {
		final Row row = new Row();
		try{
			Label lab = new Label(label);
			row.appendChild(lab);
			row.setValue(label);
			row.setSclass("sidebar-fn");
	
			EventListener<Event> actionListener = new SerializableEventListener<Event>() {
				private static final long serialVersionUID = 4385974355059855972L;
				
				public void onEvent(Event event) throws Exception {
					if(label.equalsIgnoreCase("All Routes")) { 
						resultListBox.setModel(new ListModelList<IMRoute>(imRoutes));						
						if(gmaps.getChildren() != null)	Components.removeAllChildren(gmaps);
						mapService.showAllRoutes(gmaps, imRoutes);
					} else { 
						if(route.getName().equalsIgnoreCase(label)) {
							if(gmaps.getChildren() != null)	Components.removeAllChildren(gmaps);
							mapService.showSelectedRoute(gmaps, route, imRoutes.indexOf(route));
						}
					}
					selectedItemLabel = label;
					changeGridSelectedColor();
				}
			};
			row.addEventListener(Events.ON_CLICK, actionListener);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return row;
	}
	
	private void changeGridSelectedColor(){
		for(Component comp : alternativeList.getRows().getChildren()) {
			if(comp instanceof Row) {
				Row row = (Row) comp;
				if(selectedItemLabel.contentEquals(row.getValue().toString())) {
					row.setStyle("background-color: #FFFF99; font-weight: bold;");
				} else {
					if(alternativeList.getRows().getChildren().indexOf(comp) % 2 == 0)
						row.setStyle("background-color: #FFFFFF; font-weight: normal;");
					else
						row.setStyle("background-color: #F2F2F0; font-weight: normal;");
				}
			}
		}
	}
	
	@Listen("onSelect = #resultListBox")
	public void selectOnMap(){
		try {
			if(resultListBox.getSelectedItem() != null) {
				IMRoute selected = resultListBox.getSelectedItem().getValue();
				if(gmaps.getChildren() != null)	Components.removeAllChildren(gmaps);
				mapService.showSelectedRoute(gmaps, selected, imRoutes.indexOf(selected));
				selectedItemLabel = selected.getName();
				changeGridSelectedColor();
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Listen("onChange = #instanceCounter")
	public void chnageSpinner() {
		if(instanceCounter.getValue() > 1) {
			if(isFreightShiftScenario()) {
				selectedTruckTextbox.setValue(ActivitiEngineConfig.flightNumber);
				selectedTruckTextbox.setDisabled(false);
			} else {
				selectedTruckTextbox.setValue("");
				selectedTruckTextbox.setDisabled(true);
				selectAssetButton.setLabel("Finish");
			}
		} else {
			selectedTruckTextbox.setDisabled(false);
			selectAssetButton.setLabel("Select Asset");
		}
	}
	
	@Listen("onChange = #processComboBox")
	public void changeProcessModel() {
		if(isFreightShiftScenario()) {
			selectedTruckTextbox.setValue(ActivitiEngineConfig.flightNumber);
			selectAssetButton.setLabel("Select Flight");
			selectedTruckTextbox.setDisabled(false);
		} else {
			selectedTruckTextbox.setValue("");
			selectAssetButton.setLabel("Select Asset");
		}
	}
	
	private void checkRouteConfirmation() {
		try {
			String message = "You have selected " + selectedItemLabel + ". \n Do you want to continue?";
			Messagebox.Button[] buttons = { Messagebox.Button.YES,Messagebox.Button.NO}; 
			String icon = Messagebox.QUESTION;
			EventListener<ClickEvent> eventListener = new EventListener<ClickEvent>() {
				public void onEvent(ClickEvent event) {
					if (Messagebox.ON_YES.equals(event.getName())) {
			             for(IMRoute route : imRoutes) {
			            	 if(route.getName().contains(selectedItemLabel)) {
			            		selectedJsonRoute = ProcessPTVPlan.getJsonString(route);
			            		dbMan.addNoCaseRoute(selectedJsonRoute);
			            		boolean updateSuspendedCase = false;
			        			if((Sessions.getCurrent()).getAttribute("updateSuspendedCase") != null)
			        				updateSuspendedCase = (boolean) (Sessions.getCurrent()).getAttribute("updateSuspendedCase");
			            		if(!updateSuspendedCase) {
				        	   		changeStyleForTruckSelection();
				        	   		updateProcessModelSelection();
									mapTimer.start();
			            		} else {
			            			//UpdateCaseController.selectedRoute = selectedJsonRoute;
			            			(Sessions.getCurrent()).setAttribute("selectedUpdateJsonRoute", selectedJsonRoute);
			            			Executions.sendRedirect(ActivitiEngineConfig.projectURL);
			            		}
			            	}
		            	 }
					} else if(Messagebox.ON_NO.equals(event.getName())) { 
						return; 
					}
				}
		   };
		   Messagebox.show(message, "Route Selected", buttons, icon, eventListener);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private void changeStyleForTruckSelection() {
		alternativeList.detach();
		westLayout.setOpen(false);
		resultListBox.detach();
		southLayout.setStyle("height: 55px;");
		gmaps.setStyle("height: 100%;");
		eastLayout.setOpen(true);
		eastLayout.setStyle("width: 260px;");
	}

	private void updateProcessModelSelection() {
		//String id = dbMan.estimateNewCaseId();
		
		ListModel<String> processes = new ListModelList<String>(pdf.getAllProcessModels());
		processComboBox.setModel(processes);
		
		//idTextBox.setValue("Case " + id);
		//idTextBox.setDisabled(true);
	}
	

	
	@Listen("onTimer = #mapTimer")
	public void refresh() { 
		try{
			new DateTime();
			truckListHeader.setLabel("Last update: " + DateTime.now().toLocalTime());
			updateTruckList();
			if(showFlights)
    	   		mapService.showAllFlights(gmaps);
			
			//TODO: check whether it is required or not?
			updateProcessModelSelection();
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	
	private void updateTruckList() {
		List<Asset> freeTrucks = mapService.showFreeAssets(gmaps);
		truckList.getRows().getChildren().clear();
		Rows rows = truckList.getRows();
		for(Asset asset : freeTrucks) {
			Row row = new Row();
			row.setSclass("sidebar-fn");
			final Label sourceLabel = new Label(asset.getMobOpId());
			
			Label distanceLabel = new Label("Not Available");
			IMPoint src = (IMPoint)(Sessions.getCurrent()).getAttribute("sourceGeoPoint");
			if(src != null) {
				double distance = FindDistance.distance(asset.getPoint().getLatitude(), asset.getPoint().getLongitude(), src.getLatitude(), src.getLongitude(), 'K');
				distanceLabel = new Label(distance + "");
			}
			row.appendChild(sourceLabel);
			row.appendChild(distanceLabel);
			rows.appendChild(row);
			EventListener<Event> actionListener1 = new SerializableEventListener<Event>() {
				private static final long serialVersionUID = 9217713350816914070L;
				public void onEvent(Event event) throws Exception {
					selectedTruckTextbox.setValue(sourceLabel.getValue());
				}
			};
			row.addEventListener(Events.ON_CLICK, actionListener1);
			row.addEventListener(Events.ON_RIGHT_CLICK, actionListener1);
		}
	}
	
	@Listen("onClick = #selectProcessModelBtn")
	public void enableProcessInstantiation() {
		if(processComboBox.getSelectedItem() != null && instanceCounter.getValue() > 0) { 
			String message = "You have selected " + processComboBox.getSelectedItem().getValue() + ". \n Do you want to continue?";
			Messagebox.Button[] buttons = { Messagebox.Button.YES,Messagebox.Button.NO}; 
			String icon = Messagebox.QUESTION;
			EventListener<ClickEvent> eventListener = new EventListener<ClickEvent>() {
				public void onEvent(ClickEvent event) {
					if (Messagebox.ON_YES.equals(event.getName())) {
						processComboBox.setDisabled(true);
						selectAssetButton.setDisabled(false);
						instanceCounter.setDisabled(true);
						selectProcessModelBtn.setDisabled(true);
	        	   		queueTitleVehiclePosition = HardCodedSubscription.subscribeForVehiclePosition(ActivitiEngineConfig.hardCodedEmailAddress);
						if(isFreightShiftScenario()) {
		        	   		//queueTitleFlightTrace = HardCodedSubscription.subscribeForFlightTrace(ActivitiEngineConfig.hardCodedEmailAddress);
		        	   		showFlights = true;
						}
					}
		      }
		   };
		   Messagebox.show(message, "Process Model Selection", buttons, icon, eventListener);
		}
	}
	
	@Listen("onClick = #selectAssetButton")
	public void createInstance() {
		String time1 = (String)(Sessions.getCurrent()).getAttribute("newTime1");
		String time2 = (String)(Sessions.getCurrent()).getAttribute("newTime2");
		String processKey = processComboBox.getSelectedItem().getValue();
		//String caseID = idTextBox.getValue().substring(5);
		String caseIds_CommaDelimited = "";
		if(fianlAssetCheck()) {
			if(selectedJsonRoute.length() > 1) {
				Map<String, Object> varibales = new HashMap<String, Object>();
				if(hasTruckId && instanceCounter.getValue() == 1) 
					varibales.put("truckId", selectedTruckTextbox.getValue());
				try {
					List<String> instanceIDs = new ArrayList<String>();
					for(int i = 0; i < instanceCounter.getValue(); i++) {
						String instanceId = caseService.createNewCase(processKey, varibales);
						if(instanceId != null)
							instanceIDs.add(instanceId);
					}
					for(int i = 0; i < instanceIDs.size(); i++) {
						//if(caseID != instanceId) Messagebox.show("Case ID is change to " + instanceId, "WARNING", 0, Messagebox.INFORMATION);
						int transportationStatus = StatusCode.TRANSPORTATION_RUNNING.getValue();
						dbMan.addTransportOrder(instanceIDs.get(i), selectedJsonRoute, time1, time2, transportationStatus);
						caseIds_CommaDelimited = caseIds_CommaDelimited + instanceIDs.get(i) + ",";
						dbMan.removeNoCaseRoute(selectedJsonRoute);
						try{
							PublishEvent.publishSelectedRoutet(instanceIDs.get(i), selectedJsonRoute);
							PublishEvent.publishVehicleSelectedEvent(selectedTruckTextbox.getValue(), selectedItemLabel, instanceIDs.get(i));
						} catch(Exception ex) { System.err.println(ex.getMessage());}
						
						String userId = "Automatic";
						if((Sessions.getCurrent()).getAttribute("user") != null) {
							userId = ((User)(Sessions.getCurrent()).getAttribute("user")).getId();
						}
						
						if(hasTruckId & !processKey.toLowerCase().contains("freight")) {
							dbMan.setTruckForTransportationOrder(instanceIDs.get(i), selectedTruckTextbox.getValue());
							dbMan.updateAssetStatus(selectedTruckTextbox.getValue(), StatusCode.TRUCK_BUSY.getValue());
							
							if(queueTitleVehiclePosition.length() > 1 ) 
								EventSubscriberFactory.unsubscribe(queueTitleVehiclePosition);

							/* ========================Execute first 3 tasks in the prototype ====================*/
							executeTask(processKey, instanceIDs.get(i), userId, "Reserve Truck");
							executeTask(processKey, instanceIDs.get(i), userId, "Confirm truck");
							executeTask(processKey, instanceIDs.get(i), userId, "Pick empty container");
							//=====================================================================================//
	
						} 
						
						if(isFreightShiftScenario()) {
							dbMan.setFlightForTransportationOrder(instanceIDs.get(i), ActivitiEngineConfig.flightNumber);
							executeTask(processKey, instanceIDs.get(i), userId, "Start Monitoring");
							
							if(queueTitleFlightTrace.length() > 1 ) 
								EventSubscriberFactory.unsubscribe(queueTitleFlightTrace);
						}
					}
					
					String requestId = (String) (Sessions.getCurrent()).getAttribute("requestId");
					if(requestId != null && requestId.length() > 1) {
						dbMan.updateCaseIdForRequest(requestId, caseIds_CommaDelimited);
					}
					Executions.sendRedirect(ActivitiEngineConfig.projectURL);
				} catch(Exception e) {
					Messagebox.show(e.getMessage(), "Error", 0, Messagebox.ERROR);
				}
			} else Messagebox.show("Selected route cannot be parsed.", "Error", 0, Messagebox.ERROR);
		}
	}

	private void executeTask(String processKey, String instanceId, String userId, String taskDesignName) {
		TaskServiceImpl taskService = new TaskServiceImpl(instanceId, userId);
		List<TaskObject> runningTask = taskService.getExecutingTasks();
		for(TaskObject t : runningTask) {
			if((t.getTaskName().toLowerCase().trim()).contentEquals((taskDesignName).toLowerCase().trim())) {
				try{
					taskService.completeTask(new HashMap<String, String>(), processKey, t.getTaskId());
					break;
				} catch(Exception e) {
					e.getMessage();
				}
			}
		}
	}
	
	private boolean fianlAssetCheck() {
		if(selectedTruckTextbox.getValue() != null && selectedTruckTextbox.getValue().length() > 1 ) { 
			String message = "You have selected " + selectedTruckTextbox.getValue() + ". \n Do you want to continue?";
			Messagebox.Button[] buttons = { Messagebox.Button.YES,Messagebox.Button.NO}; 
			String icon = Messagebox.QUESTION;
			EventListener<ClickEvent> eventListener = new EventListener<ClickEvent>() {
				public void onEvent(ClickEvent event) {
					if (Messagebox.ON_YES.equals(event.getName())) {
						hasTruckId = true;
						assetCheck = true;
						createInstance();
						return;
		          } else if(Messagebox.ON_NO.equals(event.getName())) { 
						return; 
		          }
		      }
		   };
		   Messagebox.show(message, "Asset Selection", buttons, icon, eventListener);
		} else {
			String message = "You have not selected any asset yet. \n Do you want to continue?";
			Messagebox.Button[] buttons = { Messagebox.Button.YES,Messagebox.Button.NO}; 
			String icon = Messagebox.EXCLAMATION;
			EventListener<ClickEvent> eventListener2 = new EventListener<ClickEvent>() {
				public void onEvent(ClickEvent event) {
					if (Messagebox.ON_YES.equals(event.getName())) {
						 assetCheck = true;
						 createInstance();
						 return;
					} else if(Messagebox.ON_NO.equals(event.getName())) { 
						return; 
					}
				}
			 };
			 Messagebox.show(message, "WARNING - Asset Selection NOT Compeleted", buttons, icon, eventListener2);
		}
		return assetCheck;
	}
	
	private boolean isFreightShiftScenario() {
		String processKey = processComboBox.getSelectedItem().getValue();
		if(processKey.toLowerCase().contains("freight") && processKey.toLowerCase().contains("shift"))
			return true;
		else 
			return false;
	}
}

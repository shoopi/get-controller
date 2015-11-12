package main.java.nl.tue.ieis.get.control;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import main.java.de.ptv.intermodal.IMPoint;
import main.java.de.ptv.intermodal.IMRouteRequest;
import main.java.de.ptv.intermodal.IMRoutingOptions;
import main.java.de.ptv.intermodal.IMStopOff;
import main.java.de.ptv.xserver.XIntermodalWSServiceStub.ArrayOfLocation;
import main.java.de.ptv.xserver.XIntermodalWSServiceStub.Location;
import main.java.de.ptv.xserver.XIntermodalWSServiceStub.RoutingOption;
import main.java.de.ptv.xserver.XIntermodalWSServiceStub.RoutingRequest;
import main.java.de.ptv.xserver.XIntermodalWSServiceStub.PlainPoint;
import main.java.nl.tue.ieis.get.activiti.config.ActivitiEngineConfig;
import main.java.nl.tue.ieis.get.data.TransportOrderDataManagement;
import main.java.nl.tue.ieis.get.map.Address;
import main.java.nl.tue.ieis.get.map.AddressTime;
import main.java.nl.tue.ieis.get.map.TransportRequest;

import java.util.UUID;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;
import org.zkoss.zul.Messagebox.ClickEvent;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.gson.Gson;

public class TransportCaseController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 8579250038174724144L;
	
	@Wire	private Button 		findAddressButton, addMultipleButton;
	@Wire	private Window 		addWindows;
	@Wire	private Label 		errorLabel;
	@Wire	private Popup 		mapPopUp;
	@Wire	private Combobox 	scenarioCombobox;
	@Wire	private Comboitem 	exportComboItem1, exportComboItem2, freightShiftComboItem1, freightShiftComboItem2, ptvServerComboItem, 
		inlandWaterwayComboItem1, inlandWaterwayComboItem2, inlandWaterwayComboItem3, inlandWaterwayComboItem4, inlandWaterwayComboItem_replanning2;
	@Wire	private Checkbox 	ptvNewServerCheckbox;
	@Wire	private	Tabbox		tabbox;
	@Wire	private Tabs		tabs;
	@Wire	private Tab			tpOrder1, addTab;
	@Wire 	private Tabpanels	tabpanels;
	@Wire	private Tabpanel	tabpanel;
	@Wire	private Timer		timer;
	
	private TransportOrderDataManagement tpdm = new TransportOrderDataManagement();
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	@Override
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		try {
			Grid grid = constructAddressGrid();
			tabpanel.appendChild(grid);
			setDefaultValue(grid);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Listen("onTimer = #timer")
	public void doOnTimer() {
		try{
			Grid grid = getCurrentTabGrid();
			List<String> cases = (List<String>) (Sessions.getCurrent()).getAttribute("selectedRequests");
			boolean updateCase = false;
			if((Sessions.getCurrent()).getAttribute("updateSuspendedCase") != null)
				updateCase = (boolean) (Sessions.getCurrent()).getAttribute("updateSuspendedCase");
			if(updateCase && cases != null && cases.size() > 0) {
				for(int j = 0; j < cases.size(); j++ ) {
					List<TransportRequest> requests = tpdm.loadTransportRequestswithCaseId(cases.get(j));
					for(int i = 0; i < requests.size(); i++) {
						TransportRequest req = requests.get(i);
						Address sourceAddress = req.getSource().getAddress();
						Address destAddress = req.getDest().getAddress();
						if(i == 0 && j == 0) {
							setSource(grid, sourceAddress, req.getSource().getDate());
							setDestination(grid, destAddress, req.getDest().getDate());
						} else {
							Tab tab = new Tab("Transport Order " + (tabs.getChildren().size()));
							tab.setClosable(true);
							tabs.getChildren().add(tabs.getChildren().size()-1, tab);
							Tabpanel tabpanel = new Tabpanel();
							
							Grid grid2 = constructAddressGrid();
							tabpanel.appendChild(grid2);
							tabpanels.appendChild(tabpanel);
							setSource(grid2, sourceAddress, req.getSource().getDate());
							setDestination(grid2, destAddress, req.getDest().getDate());
						}
					}
				}
				timer.stop();
			} else {
				setDefaultValue(grid);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Listen("onClick = #addWindows")
	public void stopTimer() {
		timer.stop();
	}
	
	@Listen("onClick = #addTab")
	public void addTab() {
		Tab tab = new Tab("Transport Order " + (tabs.getChildren().size()));
		tab.setClosable(true);
		tabs.getChildren().add(tabs.getChildren().size()-1, tab);
		Tabpanel tabpanel = new Tabpanel();
		
		Grid grid = constructAddressGrid();
		tabpanel.appendChild(grid);
		tabpanels.appendChild(tabpanel);
		
		tab.setSelected(true);
	}
	private Grid constructAddressGrid() {
		Grid grid = new Grid();
		grid.appendChild(new Columns());
		grid.appendChild(new Rows());
		
		Column c1 = new Column();
		c1.setWidth("150px");
		grid.getColumns().appendChild(c1);
		grid.getColumns().appendChild(new Column());
		grid.getColumns().appendChild(new Column());
		
		Row r1 = new Row();
		Label lr1 = new Label("Origin Address");
		lr1.setWidth("170px");
		lr1.setStyle("color:#888888; font-family:arial; font-size:13px; font-weight:bold;");
		r1.appendChild(lr1);
		
		Vlayout vlayout11 = new Vlayout();
		vlayout11.appendChild(new Label("Location Name"));
		Textbox sourceNametb = new Textbox();
		sourceNametb.setTabindex(2);
		sourceNametb.setWidth("230px");
		sourceNametb.setMold("rounded");
		sourceNametb.setConstraint("no empty");
		vlayout11.appendChild(sourceNametb);
		
		vlayout11.appendChild(new Label("Street"));
		Textbox sourceStreettb = new Textbox();
		sourceStreettb.setTabindex(2);
		sourceStreettb.setWidth("230px");
		sourceStreettb.setMold("rounded");
		sourceNametb.setConstraint("no empty");
		vlayout11.appendChild(sourceStreettb);
		
		vlayout11.appendChild(new Label("Number"));
		Textbox sourceNumbertb = new Textbox();
		sourceNumbertb.setTabindex(2);
		sourceNumbertb.setWidth("230px");
		sourceNumbertb.setMold("rounded");
		sourceNametb.setConstraint("no empty");
		vlayout11.appendChild(sourceNumbertb);
		
		vlayout11.appendChild(new Label("Post Code"));
		Textbox sourceZipcodetb = new Textbox();
		sourceZipcodetb.setTabindex(2);
		sourceZipcodetb.setWidth("230px");
		sourceZipcodetb.setMold("rounded");
		sourceZipcodetb.setConstraint("no empty");
		vlayout11.appendChild(sourceZipcodetb);
		
		vlayout11.appendChild(new Label("City"));
		Textbox sourceCitytb = new Textbox();
		sourceCitytb.setTabindex(2);
		sourceCitytb.setWidth("230px");
		sourceCitytb.setMold("rounded");
		sourceCitytb.setConstraint("no empty");
		vlayout11.appendChild(sourceCitytb);
		
		vlayout11.appendChild(new Label("Country"));
		Textbox sourceCountrytb = new Textbox();
		sourceCountrytb.setTabindex(2);
		sourceCountrytb.setWidth("230px");
		sourceCountrytb.setMold("rounded");
		sourceCountrytb.setConstraint("no empty");
		vlayout11.appendChild(sourceCountrytb);
		r1.appendChild(vlayout11);
		
		Vlayout vlayout12 = new Vlayout();
		Label lv21 = new Label("Transportation Start Time");
		lv21.setWidth("170px");
		vlayout12.appendChild(lv21);
		
		Label lv22 = new Label("Available at Origin");
		lv22.setWidth("170px");
		vlayout12.appendChild(lv22);
		
		Datebox availTimeOrigindb = new Datebox(new Date());
		availTimeOrigindb.setFormat("long+medium");
		availTimeOrigindb.setMold("rounded");
		availTimeOrigindb.setConstraint("no empty");
		availTimeOrigindb.setWidth("230px");
		availTimeOrigindb.setTabindex(4);
		vlayout12.appendChild(availTimeOrigindb);
		r1.appendChild(vlayout12);
		
		Row r2 = new Row();
		Label lr2 = new Label("Destination Address");
		lr2.setWidth("170px");
		lr2.setStyle("color:#888888; font-family:arial; font-size:13px; font-weight:bold;");
		r2.appendChild(lr2);
		
		Vlayout vlayout21 = new Vlayout();
		vlayout21.appendChild(new Label("Location Name"));
		Textbox destNametb = new Textbox();
		destNametb.setTabindex(2);
		destNametb.setWidth("230px");
		destNametb.setMold("rounded");
		destNametb.setConstraint("no empty");
		vlayout21.appendChild(destNametb);
		
		vlayout21.appendChild(new Label("Street"));
		Textbox destStreettb = new Textbox();
		destStreettb.setTabindex(2);
		destStreettb.setWidth("230px");
		destStreettb.setMold("rounded");
		destStreettb.setConstraint("no empty");
		vlayout21.appendChild(destStreettb);
		
		vlayout21.appendChild(new Label("Number"));
		Textbox destNumbertb = new Textbox();
		destNumbertb.setTabindex(2);
		destNumbertb.setWidth("230px");
		destNumbertb.setMold("rounded");
		sourceNametb.setConstraint("no empty");
		vlayout21.appendChild(destNumbertb);
		
		vlayout21.appendChild(new Label("Post Code"));
		Textbox destZipcodetb = new Textbox();
		destZipcodetb.setTabindex(2);
		destZipcodetb.setWidth("230px");
		destZipcodetb.setMold("rounded");
		destZipcodetb.setConstraint("no empty");
		vlayout21.appendChild(destZipcodetb);
		
		vlayout21.appendChild(new Label("City"));
		Textbox destCitytb = new Textbox();
		destCitytb.setTabindex(2);
		destCitytb.setWidth("230px");
		destCitytb.setMold("rounded");
		destCitytb.setConstraint("no empty");
		vlayout21.appendChild(destCitytb);
		
		vlayout21.appendChild(new Label("Country"));
		Textbox destCountrytb = new Textbox();
		destCountrytb.setTabindex(2);
		destCountrytb.setWidth("230px");
		destCountrytb.setMold("rounded");
		destCountrytb.setConstraint("no empty");
		vlayout21.appendChild(destCountrytb);
		r2.appendChild(vlayout21);
		
		Vlayout vlayout22 = new Vlayout();
		Label lv221 = new Label("Transportation End Time");
		lv221.setWidth("170px");
		vlayout22.appendChild(lv221);
		
		Label lv222 = new Label("Closing Time at Destination");
		lv222.setWidth("170px");
		vlayout22.appendChild(lv222);
		
		Datebox closingTimeDestdb = new Datebox(new Date());
		closingTimeDestdb.setFormat("long+medium");
		closingTimeDestdb.setMold("rounded");
		closingTimeDestdb.setConstraint("no empty");
		closingTimeDestdb.setWidth("230px");
		closingTimeDestdb.setTabindex(4);
		vlayout22.appendChild(closingTimeDestdb);
		r2.appendChild(vlayout22);
		
		grid.getRows().appendChild(r1);
		grid.getRows().appendChild(r2);
		return grid;
		
	}
	
	@Listen("onClick = #addMultipleButton")
	public void addTransportRequestToStach() {
		try {
			if(ptvNewServerCheckbox.isChecked()) {
				String message = "The PTV xServer does not support multiple orders."
						+ "Please uncheck the PTV xServer and send your request to WP5 directly.";
				Messagebox.Button[] buttons = { Messagebox.Button.OK}; 
				String icon = Messagebox.QUESTION;
				EventListener<ClickEvent> eventListener = new EventListener<ClickEvent>() {
					public void onEvent(ClickEvent event) {
						return;
					}
			   };
			   Messagebox.show(message, "Multiple Orders", buttons, icon, eventListener);
			} else {
				boolean added = false;
				for(Component c : tabbox.getTabpanels().getChildren()){
					if(c instanceof Tabpanel) {
						if(c.getChildren().get(0) instanceof Grid) {
							Grid grid =  (Grid) c.getChildren().get(0);
							AddressTime src = getOriginAddressTime(grid);
							AddressTime dest = getDestinationAddressTime(grid);
							added = tpdm.addTransportRequestWithoutCaseID(new TransportRequest(UUID.randomUUID().toString(), null, src, dest));
						}
					}
				}
				if(added) {
					String message = "We have successfully received your orders. we guarantee to provide you the most optimal solution as soon as possible.";
					Messagebox.Button[] buttons = { Messagebox.Button.OK}; 
					String icon = Messagebox.INFORMATION;
					EventListener<ClickEvent> eventListener = new EventListener<ClickEvent>() {
						public void onEvent(ClickEvent event) {
							if (Messagebox.ON_OK.equals(event.getName())) {
								Executions.sendRedirect(ActivitiEngineConfig.projectURL);
							}
						}
					};
					Messagebox.show(message, "Multiple Orders", buttons, icon, eventListener);
				} else {
					Clients.showNotification("Please try again later...");
				}
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	@Listen("onClick = #findAddressButton")
	public void addActivitiProcessInstance(){
		try {
			if(ptvNewServerCheckbox.isChecked() && tabs.getChildren().size() > 2) {
				String message = "The PTV xServer does not support multiple orders. To plan the request from the active tab, click YES. "
						+ "To plane all orders, please click NO and uncheck the PTV xServer checkbox and press the 'Add Order(s)' button. This sends your request to the WP5 directly.";
				Messagebox.Button[] buttons = { Messagebox.Button.YES,Messagebox.Button.NO}; 
				String icon = Messagebox.QUESTION;
				EventListener<ClickEvent> eventListener = new EventListener<ClickEvent>() {
					public void onEvent(ClickEvent event) {
						if (Messagebox.ON_YES.equals(event.getName())) {
							searchForOneOrder(getCurrentTabGrid()); 
						} else if(Messagebox.ON_NO.equals(event.getName())) { 
							return; 
						}
					}
			   };
			   Messagebox.show(message, "Multiple Orders", buttons, icon, eventListener);
			} else if (!ptvNewServerCheckbox.isChecked() && tabs.getChildren().size() > 2) {
				String message = "If you would like to plane all orders, "
						+ "please use the 'Add Order(s)' button. This sends your request to the WP5 directly.";
				Messagebox.Button[] buttons = { Messagebox.Button.OK}; 
				String icon = Messagebox.ERROR;
				EventListener<ClickEvent> eventListener = new EventListener<ClickEvent>() {
					public void onEvent(ClickEvent event) {
							return; 
						
					}
			   };
			   Messagebox.show(message, "Multiple Orders", buttons, icon, eventListener);
			} else {
				searchForOneOrder(getCurrentTabGrid()); 
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void searchForOneOrder(Grid grid) {
		AddressTime originAddressTime = getOriginAddressTime(grid);
		AddressTime destAddressTime = getDestinationAddressTime(grid);
		
		boolean result = findLocations(originAddressTime, destAddressTime);
		if(result){
			(Sessions.getCurrent()).setAttribute("newTime1", originAddressTime.getDate().toString());
			(Sessions.getCurrent()).setAttribute("newTime2", destAddressTime.getDate().toString());
			if(scenarioCombobox.getSelectedItem().equals(exportComboItem1))	
				(Sessions.getCurrent()).setAttribute("demo", "export1");
			else if(scenarioCombobox.getSelectedItem().equals(exportComboItem2))	
				(Sessions.getCurrent()).setAttribute("demo", "export2");
			else if(scenarioCombobox.getSelectedItem().equals(freightShiftComboItem1))	
				(Sessions.getCurrent()).setAttribute("demo", "freightShift1");
			else if(scenarioCombobox.getSelectedItem().equals(freightShiftComboItem2))	
				(Sessions.getCurrent()).setAttribute("demo", "freightShift2");
			else if(scenarioCombobox.getSelectedItem().equals(inlandWaterwayComboItem1) || scenarioCombobox.getSelectedItem().equals(inlandWaterwayComboItem2)
					|| scenarioCombobox.getSelectedItem().equals(inlandWaterwayComboItem3) || scenarioCombobox.getSelectedItem().equals(inlandWaterwayComboItem4))	
				(Sessions.getCurrent()).setAttribute("demo", "inlandWaterway");
			else if ( scenarioCombobox.getSelectedItem().equals(inlandWaterwayComboItem_replanning2))
				(Sessions.getCurrent()).setAttribute("demo", "inlandWaterway2");
			else (Sessions.getCurrent()).setAttribute("demo", "other");
			String requestId = UUID.randomUUID().toString();
			boolean added = tpdm.addTransportRequestWithoutCaseID(new TransportRequest(requestId, null, originAddressTime, destAddressTime));
			if(added) {
				Executions.getCurrent().sendRedirect("/gui/mapPlanPopup.zul");
				(Sessions.getCurrent()).setAttribute("requestId", requestId);
			}
		}
	
	}
	
	private boolean findLocations(AddressTime origin, AddressTime dest) {
		try {
			final Geocoder geocoder = new Geocoder();
			
			String originAddress = origin.getAddress().toString();
			String destAddress = dest.getAddress().toString();
			
			GeocoderRequest originRequest = new GeocoderRequestBuilder().setAddress(originAddress).setLanguage("en").getGeocoderRequest();
			GeocodeResponse originResponse = geocoder.geocode(originRequest);
			if(originResponse.getResults().size() > 0) {
				GeocoderRequest destRequest = new GeocoderRequestBuilder().setAddress(destAddress).setLanguage("en").getGeocoderRequest();
				GeocodeResponse destResponse = geocoder.geocode(destRequest);
				if(destResponse.getResults().size() > 0 ) {
					
					IMPoint sourcePoint = new IMPoint();
					sourcePoint.setLatitude(Double.parseDouble(originResponse.getResults().get(0).getGeometry().getLocation().getLat().toString()));
					sourcePoint.setLongitude(Double.parseDouble(originResponse.getResults().get(0).getGeometry().getLocation().getLng().toString()));
					
					IMPoint destPoint = new IMPoint();
					destPoint.setLatitude(Double.parseDouble(destResponse.getResults().get(0).getGeometry().getLocation().getLat().toString()));
					destPoint.setLongitude(Double.parseDouble(destResponse.getResults().get(0).getGeometry().getLocation().getLng().toString()));
					if(scenarioCombobox.getSelectedItem().equals(exportComboItem1) || scenarioCombobox.getSelectedItem().equals(exportComboItem2) ) {
						destPoint.setLatitude(51.93495416742096);
						destPoint.setLongitude(4.134529854447256);
					}
					if(!ptvNewServerCheckbox.isChecked()) {
						(Sessions.getCurrent()).setAttribute("jsonRouteRequest", createOldJsonIMRequest(sourcePoint, destPoint, origin, dest));
						(Sessions.getCurrent()).setAttribute("objectRouteRequest", null);
					} else {
						(Sessions.getCurrent()).setAttribute("objectRouteRequest", createNewPTVRoutingRequest(sourcePoint, destPoint, origin, dest));
						(Sessions.getCurrent()).setAttribute("jsonRouteRequest", null);
					}
					(Sessions.getCurrent()).setAttribute("sourceGeoPoint", sourcePoint);
					
					Clients.showBusy(addWindows, "finding address...");
					return true;
				} else {
					errorLabel.setValue("Destination Address has not been found!");
					return false;
				}
			} else {
				errorLabel.setValue("Origin Address has not been found!");
				return false;
			}
		} catch(Exception e) {errorLabel.setValue("One of the Addresses has not been found!"); return false;}
	}
	
	private String createOldJsonIMRequest(IMPoint sourcePoint, IMPoint destPoint, AddressTime origin, AddressTime dest) {
		String result = "";
		try {
		IMRouteRequest imRequest = new IMRouteRequest();
		
		IMStopOff originStopOff = new IMStopOff();
		originStopOff.setIsTerminal(false);
		originStopOff.setLocationName(origin.getAddress().getName());
		originStopOff.setLocationStreet(origin.getAddress().getStreet());
		originStopOff.setLocationHouseNumber(origin.getAddress().getNumber());
		originStopOff.setLocationPostalCode(origin.getAddress().getZipcode());
		originStopOff.setLocationCity(origin.getAddress().getCity());
		originStopOff.setLocationCountry(origin.getAddress().getCountry());
		originStopOff.setLocationExtId("");
		originStopOff.setTerminalExtId("");
		originStopOff.setPoint(sourcePoint);
		
		IMStopOff destStopOff = new IMStopOff();
		destStopOff.setIsTerminal(false);
		destStopOff.setLocationName(dest.getAddress().getName());
		destStopOff.setLocationStreet(dest.getAddress().getStreet());
		destStopOff.setLocationHouseNumber(dest.getAddress().getNumber());
		destStopOff.setLocationPostalCode(dest.getAddress().getZipcode());
		destStopOff.setLocationCity(dest.getAddress().getCity());
		destStopOff.setLocationCountry(dest.getAddress().getCountry());
		destStopOff.setLocationExtId("");
		destStopOff.setTerminalExtId("");
		destStopOff.setPoint(destPoint);
		

		IMStopOff[] imStopOffs = new IMStopOff[2];
		imStopOffs[0] = originStopOff;
		imStopOffs[1] = destStopOff;
		imRequest.setStopOffs(imStopOffs);
		
		IMRoutingOptions routingOption = new IMRoutingOptions();
		routingOption.setAccompanied(false);
		routingOption.setContainerTypeId(0);
		//routingOption.setExcludedOperators(new IMOperator[]);
		//routingOption.setExcludedTransportModes(excludedTransportModes);
		routingOption.setFreightTariffFile("");
		routingOption.setMaxCosts(400000);
		routingOption.setNoOfAlternatives(0);
		routingOption.setRegardDrivingHours(false);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.ZZZZ");
		//routingOption.setStartTime("2013-10-15T08:00:00.0000000+02:00");
		routingOption.setStartTime(sdf.format(origin.getDate()));
		routingOption.setTimeCostWeight(100);
		//routingOption.setTransport(new Transport());
		imRequest.setOptions(routingOption);
		
		Gson gson = new Gson();
		result = gson.toJson(imRequest);
		} catch(Exception e) {}
		return result;
	}
	
	private RoutingRequest createNewPTVRoutingRequest(IMPoint sourcePoint, IMPoint destPoint, AddressTime origin, AddressTime dest) {
		RoutingRequest rq = new RoutingRequest();
		try {
			
			RoutingOption option = new RoutingOption();

			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(origin.getDate());
			//XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
			
			option.setStartTime(cal);
			option.setAccompanied(false);
			option.setMaxCosts(4000000);
			option.setNumberOfAlternatives(20);
			option.setTimeCostWeight(5000);
			option.setWithWayList(true);
			rq.setRoutingOptions(option);
			
			Location[] loc = new Location[2];
			
			Location l1 = new Location();
			l1.setCode("");
			l1.setName(origin.getAddress().getName());
			PlainPoint p1 = new PlainPoint();
			p1.setX(sourcePoint.getLongitude());
			p1.setY(sourcePoint.getLatitude());
			p1.setZ(0.0);
			l1.setStreet(origin.getAddress().getStreet());
			l1.setHouseNumber(origin.getAddress().getNumber());
			l1.setPostalCode(origin.getAddress().getZipcode());
			l1.setCity(origin.getAddress().getCity());
			l1.setCountry(origin.getAddress().getCountry());
			l1.setPoint(p1);
			loc[0] = l1;

			Location l2 = new Location();
			l2.setCode("");
			l2.setName(dest.getAddress().getName());
			PlainPoint p2 = new PlainPoint();
			p2.setX(destPoint.getLongitude());
			p2.setY(destPoint.getLatitude());
			p2.setZ(0.0);
			l2.setPoint(p2);
			l2.setStreet(dest.getAddress().getStreet());
			l2.setHouseNumber(dest.getAddress().getNumber());
			l2.setPostalCode(dest.getAddress().getZipcode());
			l2.setCity(dest.getAddress().getCity());
			l2.setCountry(dest.getAddress().getCountry());
			loc[1] = l2;
			
			ArrayOfLocation locations = new ArrayOfLocation();
			locations.setLocation(loc);
			rq.setWrappedStopOffs(locations);
			//rq.setWrappedStopOffs(loc);
		} catch(Exception e) {}
		return rq;
	}
	
	@Listen("onSelect = #scenarioCombobox")
	public void showDemoConfig() {
		Grid grid = getCurrentTabGrid();
		if(scenarioCombobox.getSelectedItem().equals(exportComboItem1)) {
			Address src = new Address ("Wijnen Logistics B.V.", "Vinkenpeelweg", "10", "5971 NJ", "Grubbenvorst", "Netherlands");
			Address dst = new Address("ECT Delta Terminal", "Europaweg", "875", "3199 LD", "Rotterdam", "Netherlands");
			Date departure = new Date();
			try {
				departure = sdf.parse("2015-10-01T14:00:00");
			} catch (ParseException e) {System.out.println(e.getMessage());}
			Date arrival = new Date();
			try {
				arrival = sdf.parse("2015-10-01T18:15:00");
			} catch (ParseException e) {System.out.println(e.getMessage());}

			setSource(grid, src, departure);
			setDestination(grid, dst, arrival);
			
		} else if(scenarioCombobox.getSelectedItem().equals(exportComboItem2)) {
			Address src = new Address ("Wijnen Logistics B.V.", "Vinkenpeelweg", "10", "5971 NJ", "Grubbenvorst", "Netherlands");
			Address dst = new Address("ECT Delta Terminal", "Europaweg", "875", "3199 LD", "Rotterdam", "Netherlands");
			Date departure = new Date();
			try {
				departure = sdf.parse("2015-10-01T15:00:00");
			} catch (ParseException e) {System.out.println(e.getMessage());}
			Date arrival = new Date();
			try {
				arrival = sdf.parse("2015-10-01T18:15:00");
			} catch (ParseException e) {System.out.println(e.getMessage());}

			setSource(grid, src, departure);
			setDestination(grid, dst, arrival);
			
		} else if(scenarioCombobox.getSelectedItem().equals(freightShiftComboItem1)) {
			Address src = new Address("Frankfurt Airport", "Airportring", "1", "60547", "Frankfurt", "Germany");
			Address dst = new Address("Schiphol Amsterdam Airport", "Evert van de Beekstraat", "202", "1118 CP", "Schiphol", "Netherlands");
			Date departure = new Date();
			try {
				departure = sdf.parse("2015-10-01T10:30:00");
			} catch (ParseException e) {System.out.println(e.getMessage());}
			Date arrival = new Date();
			try {
				arrival = sdf.parse("2015-10-01T14:30:00");
			} catch (ParseException e) {System.out.println(e.getMessage());}
			setSource(grid, src, departure);
			setDestination(grid, dst, arrival);
			
		} else if(scenarioCombobox.getSelectedItem().equals(freightShiftComboItem2)) {
			Address src = new Address("Brussels Airport", "A201", "1", "1930", "Zaventem", "Belgium");
			Address dst = new Address("Schiphol Amsterdam Airport", "Evert van de Beekstraat", "202", "1118 CP", "Schiphol", "Netherlands");
			Date departure = new Date();
			try {
				departure = sdf.parse("2015-10-01T11:00:00");
			} catch (ParseException e) {System.out.println(e.getMessage());}
			Date arrival = new Date();
			try {
				arrival = sdf.parse("2015-10-01T14:30:00");
			} catch (ParseException e) {System.out.println(e.getMessage());}
			setSource(grid, src, departure);
			setDestination(grid, dst, arrival);
			
		} else if (scenarioCombobox.getSelectedItem().equals(inlandWaterwayComboItem1)) {
			Address src = new Address("Kechnec Industrial Park", "Kechnec", "265", "044 58", "Kechnec", "Slovakia");
			Address dst = new Address("RoLa-Terminal Hafen", "Wiener Str.", "18a", "93055", "Regensburg", "Germany");
			Date departure = new Date();
			try {
				departure = sdf.parse("2015-01-02T13:00:00");
			} catch (ParseException e) {System.out.println(e.getMessage());}
			Date arrival = new Date();
			try {
				arrival = sdf.parse("2015-01-09T09:00:00");
			} catch (ParseException e) {System.out.println(e.getMessage());}
			setSource(grid, src, departure);
			setDestination(grid, dst, arrival);
		} else if (scenarioCombobox.getSelectedItem().equals(inlandWaterwayComboItem2)) {
			Address src = new Address("Kechnec Industrial Park", "Kechnec", "265", "044 58", "Kechnec", "Slovakia");
			Address dst = new Address("RoLa-Terminal Hafen", "Wiener Str.", "18a", "93055", "Regensburg", "Germany");
			Date departure = new Date();
			try {
				departure = sdf.parse("2015-01-02T16:00:00");
			} catch (ParseException e) {System.out.println(e.getMessage());}
			Date arrival = new Date();
			try {
				arrival = sdf.parse("2015-01-09T09:00:00");
			} catch (ParseException e) {System.out.println(e.getMessage());}
			setSource(grid, src, departure);
			setDestination(grid, dst, arrival);
		} else if (scenarioCombobox.getSelectedItem().equals(inlandWaterwayComboItem3)) {
			Address src = new Address("Budapest Mahart Container Center", "Weiss Manfréd út", "5", "1211", "Budapest", "Hungary");
			Address dst = new Address("Linz CCT Stadthafen", "Saxingerstraße", "37", "4020", "Linz", "Austria");
			Date departure = new Date();
			try {
				departure = sdf.parse("2015-01-05T16:00:00");
			} catch (ParseException e) {System.out.println(e.getMessage());}
			Date arrival = new Date();
			try {
				arrival = sdf.parse("2015-01-08T06:00:00");
			} catch (ParseException e) {System.out.println(e.getMessage());}
			setSource(grid, src, departure);
			setDestination(grid, dst, arrival);
		} else if (scenarioCombobox.getSelectedItem().equals(inlandWaterwayComboItem4)) {
			Address src = new Address("Budapest Mahart Container Center", "Weiss Manfréd út", "5", "1211", "Budapest", "Hungary");
			Address dst = new Address("München Riem Ubf", "St2082", "1", "81829", "München", "Germany");
			Date departure = new Date();
			try {
				departure = sdf.parse("2015-01-02T15:22:00");
			} catch (ParseException e) {System.out.println(e.getMessage());}
			Date arrival = new Date();
			try {
				arrival = sdf.parse("2015-01-05T06:00:00");
			} catch (ParseException e) {System.out.println(e.getMessage());}
			setSource(grid, src, departure);
			setDestination(grid, dst, arrival);
		} else if (scenarioCombobox.getSelectedItem().equals(inlandWaterwayComboItem_replanning2)) {
			Address src = new Address("Container Terminal Enns (CTE)", "Donaustraße", "3", "4470", "Enns", "Austria");
			Address dst = new Address("RoLa-Terminal Hafen", "Wiener Str.", "18a", "93055", "Regensburg", "Germany");
			setSource(grid,src , new Date());
			setDestination(grid,dst , new Date());
		} else {
			setDefaultValue(grid);
		}
	}
	
	
	private void setDefaultValue(Grid grid) {
		Address sourceAddress = new Address("Jan de Rijk Logistics", "Leemstraat", "15", "4705 RT", "Roosendaal", "Netherlands");
		Address destAddress = new Address("Buss Port Logistics GmbH & Co. KG", "Am Seehafen, ", "4", "21683", "Stade-Butzfleth", "Germany");
		setSource(grid, sourceAddress, new Date());
		setDestination(grid, destAddress, new Date());
	}
	
	private void setSource(Grid grid, Address address, Date originTime) {
		((Textbox)grid.getRows().getFirstChild().getChildren().get(1).getChildren().get(1)).setValue(address.getName());
		((Textbox)grid.getRows().getFirstChild().getChildren().get(1).getChildren().get(3)).setValue(address.getStreet());
		((Textbox)grid.getRows().getFirstChild().getChildren().get(1).getChildren().get(5)).setValue(address.getNumber());
		((Textbox)grid.getRows().getFirstChild().getChildren().get(1).getChildren().get(7)).setValue(address.getZipcode());
		((Textbox)grid.getRows().getFirstChild().getChildren().get(1).getChildren().get(9)).setValue(address.getCity());
		((Textbox)grid.getRows().getFirstChild().getChildren().get(1).getChildren().get(11)).setValue(address.getCountry());
		((Datebox)grid.getRows().getFirstChild().getChildren().get(2).getChildren().get(2)).setValue(originTime);
	}
	
	private void setDestination(Grid grid, Address address, Date destTime) {
		((Textbox)grid.getRows().getLastChild().getChildren().get(1).getChildren().get(1)).setValue(address.getName());
		((Textbox)grid.getRows().getLastChild().getChildren().get(1).getChildren().get(3)).setValue(address.getStreet());
		((Textbox)grid.getRows().getLastChild().getChildren().get(1).getChildren().get(5)).setValue(address.getNumber());
		((Textbox)grid.getRows().getLastChild().getChildren().get(1).getChildren().get(7)).setValue(address.getZipcode());
		((Textbox)grid.getRows().getLastChild().getChildren().get(1).getChildren().get(9)).setValue(address.getCity());
		((Textbox)grid.getRows().getLastChild().getChildren().get(1).getChildren().get(11)).setValue(address.getCountry());
		((Datebox)grid.getRows().getLastChild().getChildren().get(2).getChildren().get(2)).setValue(destTime);
	}
	
	private AddressTime getOriginAddressTime(Grid grid) {
	 	String name = ((Textbox)grid.getRows().getFirstChild().getChildren().get(1).getChildren().get(1)).getValue();
		String street = ((Textbox)grid.getRows().getFirstChild().getChildren().get(1).getChildren().get(3)).getValue();
		String number = ((Textbox)grid.getRows().getFirstChild().getChildren().get(1).getChildren().get(5)).getValue();
		String zipcode = ((Textbox)grid.getRows().getFirstChild().getChildren().get(1).getChildren().get(7)).getValue();
		String city = ((Textbox)grid.getRows().getFirstChild().getChildren().get(1).getChildren().get(9)).getValue();
		String country = ((Textbox)grid.getRows().getFirstChild().getChildren().get(1).getChildren().get(11)).getValue();
		Address address = new Address(name, street, number, zipcode, city, country);
		Date time = ((Datebox)grid.getRows().getFirstChild().getChildren().get(2).getChildren().get(2)).getValue();
		return new AddressTime(address, time);
	}
	
	private AddressTime getDestinationAddressTime(Grid grid) {
		String name = ((Textbox)grid.getRows().getLastChild().getChildren().get(1).getChildren().get(1)).getValue();
		String street = ((Textbox)grid.getRows().getLastChild().getChildren().get(1).getChildren().get(3)).getValue();
		String number = ((Textbox)grid.getRows().getLastChild().getChildren().get(1).getChildren().get(5)).getValue();
		String zipcode = ((Textbox)grid.getRows().getLastChild().getChildren().get(1).getChildren().get(7)).getValue();
		String city = ((Textbox)grid.getRows().getLastChild().getChildren().get(1).getChildren().get(9)).getValue();
		String country = ((Textbox)grid.getRows().getLastChild().getChildren().get(1).getChildren().get(11)).getValue();
		Address address = new Address(name, street, number, zipcode, city, country);
		Date time = ((Datebox)grid.getRows().getLastChild().getChildren().get(2).getChildren().get(2)).getValue();
		return new AddressTime(address, time);
	}
	
	private Grid getCurrentTabGrid() {
		return (Grid)tabbox.getSelectedPanel().getChildren().get(0);
	}
}

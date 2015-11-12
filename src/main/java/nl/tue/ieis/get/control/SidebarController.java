package main.java.nl.tue.ieis.get.control;

import java.util.ArrayList;
import java.util.List;

import main.java.nl.tue.ieis.get.event.service.MessageObject;
import main.java.nl.tue.ieis.get.event.type.EventType;
import main.java.nl.tue.ieis.get.services.*;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Timer;

public class SidebarController extends SelectorComposer<Component>{

	private static final long serialVersionUID = 1L;
	
	@Wire	private Grid caseListSidebar, msgGrid;
	@Wire	private Timer sidebarTimer;
	
	private int messageboxCounter = 0;
	private int caseCounter = 0;
	private String prevCaseId = "";
	private List<MessageObject> messageObjects = new ArrayList<MessageObject>();
	
	SidebarPageConfig pageConfig = new SidebarPageConfigImpl();
	//TransportOrderDataManagement tpdm = new TransportOrderDataManagement();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		
		//initialize view after view construction.
		try {
			Rows rows = caseListSidebar.getRows();
			
			for(SidebarPage page : pageConfig.getPages()){
				Row row = constructSidebarRow(page.getLabel(),page.getIconUri(),page.getUri(), page.getCaseID());
				rows.appendChild(row);
				String caseID = (String) (Sessions.getCurrent()).getAttribute("caseID");
				if(caseID == null || Integer.parseInt(caseID) < 1)
					caseID = "0";
				//to determine refresh message box
				prevCaseId = caseID;
				if(page.getCaseID().contentEquals(caseID)) {
					row.setStyle("background-color: #FFFF99; font-weight: bold;");
				}
			}
			constructMsgBox();
			caseCounter = pageConfig.getPages().size();
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	private Row constructSidebarRow(String label, String imageSrc, final String locationUri, final String caseID) {
		
		//construct component and hierarchy
		final Row row = new Row();
		try {
			Image image = new Image(imageSrc);
			Label lab = new Label(label);
			
			row.appendChild(image);
			row.appendChild(lab);
			
			//set style attribute
			row.setSclass("sidebar-fn");
			
			//create and register event listener
			EventListener<Event> actionListener = new SerializableEventListener<Event>() {
				private static final long serialVersionUID = 1L;
	
				public void onEvent(Event event) throws Exception {
					//redirect current url to new location
					Executions.getCurrent().sendRedirect(locationUri);
					(Sessions.getCurrent()).setAttribute("caseID" , caseID);
					prevCaseId = caseID;
				}
			};
			row.addEventListener(Events.ON_CLICK, actionListener);
		} catch(Exception e) { 
			System.out.println(e.getMessage());
		}
		return row;
	}
	
	@Listen("onTimer = #sidebarTimer")
	public void update() {
		try {
			pageConfig = new SidebarPageConfigImpl();
			if(caseCounter != pageConfig.getPages().size())
				Executions.sendRedirect("");
			
			caseListSidebar.getRows().getChildren().clear();
			Rows rows = caseListSidebar.getRows();
			
			for(SidebarPage page : pageConfig.getPages()) {
				Row row = constructSidebarRow(page.getLabel(),page.getIconUri(),page.getUri(), page.getCaseID());
				rows.appendChild(row);
				String caseID = (String) (Sessions.getCurrent()).getAttribute("caseID");
				if(caseID == null || Integer.parseInt(caseID) < 1)
					caseID = "0";
				if(page.getCaseID().contentEquals(caseID)) {
					row.setStyle("background-color: #FFFF99; font-weight: bold;");
				}
			}
			constructMsgBox();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void constructMsgBox(){
		try {
			String caseID = (String) (Sessions.getCurrent()).getAttribute("caseID");
			if(caseID == null || caseID == "-1" || caseID == "0") {
				msgGrid.setVisible(false);
			} else {
				List<MessageObject> messages = pageConfig.updateMessageWindow(caseID);
				if(messages.size() != messageboxCounter && caseID.contentEquals(prevCaseId)) {
					msgGrid.setVisible(true);
					msgGrid.getRows().getChildren().clear();
					Rows rows = msgGrid.getRows();
					for(final MessageObject msg : messages) {
						final Row row = new Row();
						Image image = new Image(msg.getIconURL());
						Label lab = new Label(msg.getMessage());
						row.appendChild(image);
						row.appendChild(lab);
						rows.appendChild(row);
						EventListener<Event> actionListener = new SerializableEventListener<Event>() {
							private static final long serialVersionUID = 1L;
							public void onEvent(Event event) throws Exception {
								if(!messageObjects.contains(msg)) {	
									messageObjects.add(msg); 
									Clients.showNotification(msg.getType() + " has been added to the map.");
								} else {	
									messageObjects.remove(msg); 
									Clients.showNotification(msg.getType() + " will be removed from the map. <br> Please refresh to see the result.");
								}
								(Sessions.getCurrent()).setAttribute("messageObjects" , messageObjects);
								
								//Executions.sendRedirect("");
							}
						};
						row.addEventListener(Events.ON_CLICK, actionListener);
						row.setSclass("sidebar-fn");
						if(msg.getType().contentEquals(EventType.PredictedDeadlineViolation.toString())) {
							row.setStyle("font-weight: bold; color: red"); 
						}
					}
					messageboxCounter = messages.size();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
}


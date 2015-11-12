package main.java.nl.tue.ieis.get.control;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.nl.tue.ieis.get.activiti.InstanceMigration;
import main.java.nl.tue.ieis.get.activiti.ProcessDefinitionFunctions;
import main.java.nl.tue.ieis.get.activiti.config.ActivitiEngineConfig;
import main.java.nl.tue.ieis.get.data.TransportOrderDataManagement;
import main.java.nl.tue.ieis.get.map.TransportRequest;
import main.java.nl.tue.ieis.get.services.CaseService;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class UpdateCaseController extends SelectorComposer<Component> {

	private static final long serialVersionUID = -7352479718101374229L;
	private CaseService caseService = new CaseServiceImpl();
	static ArrayList<String> selectedCases = new ArrayList<String>();
	
	@Wire	private Window 	terminateWin, updateWin;
	@Wire	private Textbox reasonTextbox;
	@Wire	private Grid 	terminateCaseGrid;
	@Wire	private Button	updateRouteBtn, updateProcessBtn, removeTransportBtn, refreshRepositoryBtn, deployProcessFromRepositoryBtn;
	@Wire	private	Label 	routeLbl;
	@Wire	private	Combobox addCompTaskCombobox, processComboBox;
	@Wire	private	Comboitem add2StartComboItem, add2RunningComboItem;
	
	private Window addWindow;
		
	@Override
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		constructUpdatedCaseGrid();
		determineButtonsStatus();
	}

	@SuppressWarnings("deprecation")
	private void constructUpdatedCaseGrid() {
		try {
			Map<String, String> terminatedCase = new HashMap<String,String>();
			terminatedCase = caseService.readyForUpdateCases();
			Rows rows = terminateCaseGrid.getRows();

			if(terminatedCase.size() > 0) {
				for(Map.Entry<String, String> entry : terminatedCase.entrySet()) {
					Row row = constructRow(entry.getKey(), entry.getValue());
					rows.appendChild(row);
				}
			} else {
				Label lbl = new Label("No suspended case has been found.");
				Row row = new Row();
				row.setSpans("3");
				row.appendChild(lbl);
				rows.appendChild(row);
			}
			
			
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			//e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Listen("onClick = #terminateBtn")
	public void terminateProcessInstance() {
		try{
			if(reasonTextbox.getValue().length() > 0 && reasonTextbox.getValue() != null) {
				terminateWin.detach();
				final String caseID = (String)(Sessions.getCurrent()).getAttribute("caseID");				

				@SuppressWarnings("rawtypes")
				EventListener eventListener = new EventListener() {
			        public void onEvent(final Event evt) throws Exception{
			            switch (((Integer)evt.getData()).intValue()) {
			            case Messagebox.YES: 
			            	String s1 = StringUtils.join(caseService.terminateAllRelatedCases(caseID, reasonTextbox.getValue()), ", ");
							Clients.showNotification(s1);
			            	break; // Yes button is pressed
			            case Messagebox.NO: 
			            	String s2 = caseService.terminateCase(caseID, reasonTextbox.getValue());
							Clients.showNotification(s2);
			            	break; // No button is pressed
			            }
			        }
			    };
				Messagebox.show("Do you want to suspend 'all' related cases?", "Suspend case", Messagebox.YES| Messagebox.NO, Messagebox.QUESTION, eventListener);
			}
		} catch (Exception e) { 
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Listen("onUpload = #updateProcessBtn")
	public void uploadSpecification(UploadEvent event) {
		final org.zkoss.util.media.Media media = event.getMedia();
		try {
			final String name = media.getName();
			System.out.println("fileupload: " + media + " == Extension: " + media.getFormat());
			if(!name.substring(name.length()-5, name.length()).contentEquals(".bpmn")) {
				Messagebox.show("Please upload a file with .bpmn extension." , "Error", Messagebox.OK, Messagebox.ERROR);
			} else {
				String message = "You have selected " + name + ". \n Do you want to continue?";
				Messagebox.Button[] btns = { Messagebox.Button.YES,Messagebox.Button.NO}; 
				String icon = Messagebox.QUESTION;
				EventListener<ClickEvent> clickEventListener = new EventListener<ClickEvent>() {
					public void onEvent(ClickEvent event) {
						if (Messagebox.ON_YES.equals(event.getName())) {
							try {
								copyFileToDirectory(media);
								String fileName = name.substring(0, name.length()-5);
								runMigration(fileName, true);
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
			      }
			   };
			   Messagebox.show(message, "Process Model Selection", btns, icon, clickEventListener);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			Messagebox.show(e.getMessage() , "Error", Messagebox.OK, Messagebox.ERROR);
		}
	}

	private void runMigration(String fileNameOrID, boolean newDeployment) {		
		boolean add2Running = true;
		if(addCompTaskCombobox.getSelectedItem().equals(add2StartComboItem))
			add2Running = false;
		TransportRequest tr = new TransportRequest();
		String requestId = (String) (Sessions.getCurrent()).getAttribute("requestId");
		if(requestId != null && requestId.length() > 1) {
			TransportOrderDataManagement dbMan = new TransportOrderDataManagement();
			tr = dbMan.loadTransportRequestWithID(requestId);
		}
		
		String updatedRoute = (String)(Sessions.getCurrent()).getAttribute("selectedUpdateJsonRoute");
		if(updatedRoute != null && updatedRoute.length() > 3) {
			for(String caseId : selectedCases) {
				InstanceMigration migration = new InstanceMigration(add2Running, caseId);
				migration.doMigration(fileNameOrID, updatedRoute, tr, newDeployment);
				migration = null;
			}
		}
		
		(Sessions.getCurrent()).setAttribute("updateSuspendedCase", false);
		(Sessions.getCurrent()).setAttribute("selectedUpdateJsonRoute", null);
		
		EventListener<ClickEvent> eventListener = new EventListener<ClickEvent>() {
			public void onEvent(ClickEvent eve) {
				Executions.sendRedirect(ActivitiEngineConfig.projectURL);
		    }
		};
		Messagebox.Button[] buttons = { Messagebox.Button.OK};
		Messagebox.show(fileNameOrID + " has been added successfully. \n ", 
				"Successfully Deployed", buttons , Messagebox.INFORMATION, eventListener);
	}

	private void copyFileToDirectory(org.zkoss.util.media.Media media)
			throws FileNotFoundException, IOException {
		java.io.File file = new java.io.File(this.getClass().getClassLoader().getResource("").getPath() + "main/resources/replanning/" + media);							
		byte[] bpmn = media.getByteData();
		
		java.io.FileOutputStream fileOuputStream1 = new java.io.FileOutputStream(file); 
		fileOuputStream1.write(bpmn);
		fileOuputStream1.close();
		
		bpmn = null;
	}
	
	private Row constructRow(final String caseId, String reason) {
		final Row row = new Row();
		try {
			Checkbox checkbox = new Checkbox();
			checkbox.setId(caseId);
			row.appendChild(checkbox);
			if(selectedCases.contains(caseId))
				checkbox.setChecked(true);
			
			Label lab = new Label("Case " + caseId);
			row.appendChild(lab);
			
			Label lab2 = new Label(reason);
			row.appendChild(lab2);
		} catch(Exception e) { 
			System.out.println(e.getMessage());
		}
		return row;
	}
	
	@Listen("onCheck = checkbox")
	public void getSelectedCases() {
		selectedCases.clear();
		try {
			for(Component r : terminateCaseGrid.getRows().getChildren()) {
				for(Component c : r.getChildren()) {
					if( c instanceof Checkbox) {
						if(((Checkbox)c).isChecked()) {
							selectedCases.add(c.getId());
						}
					}
				}
			}
			
			determineButtonsStatus();
			
		} catch (Exception e) {
			System.out.println("System cannot load selected cases.");
		}
	}
	
	private void determineButtonsStatus() {
		try {
			if(selectedCases.size() > 0) {
				updateRouteBtn.setDisabled(false);
				removeTransportBtn.setDisabled(false);
				determineUpdateProcessButtonStatus();
			} else {
				updateRouteBtn.setDisabled(true);
				removeTransportBtn.setDisabled(true);
				updateProcessBtn.setDisabled(true);
				addCompTaskCombobox.setDisabled(true);
			}
		} catch(java.lang.NullPointerException e) {
		}
	}
	
	
	@Listen("onClick = #updateRouteBtn")
	public void showAddTransportWin() {
		(Sessions.getCurrent()).setAttribute("updateSuspendedCase", true);
		if(addWindow == null) {
			List<Component> comps = updateWin.getParent().getChildren();
			for(Component c : comps) {
				if(c instanceof Window && c.getId().contentEquals("addWindows")) {
					addWindow = (Window)c;
					(Sessions.getCurrent()).setAttribute("selectedRequests", selectedCases);
					break;
				}
			}
		}
		if(addWindow != null) {
		updateWin.appendChild(addWindow);
			if (!addWindow.isVisible())
				addWindow.setVisible(true);
			addWindow.doHighlighted();	
		}
	}
	
	@Listen("onClick = #removeTransportBtn")
	public void removeTransportationPlan() {
		for(String caseId : selectedCases) {
			InstanceMigration.removeTransportPlan(caseId);
		}
		(Sessions.getCurrent()).setAttribute("selectedUpdateJsonRoute", null);
		Executions.sendRedirect(ActivitiEngineConfig.projectURL);
	}
	
	private void determineUpdateProcessButtonStatus(){
		String updatedRoute = (String)(Sessions.getCurrent()).getAttribute("selectedUpdateJsonRoute");
		if(updatedRoute != null && updatedRoute.length() > 3) {
			updateProcessBtn.setDisabled(false);
			addCompTaskCombobox.setDisabled(false);
			refreshRepositoryBtn.setDisabled(false);
			routeLbl.setValue(updatedRoute.substring(1, updatedRoute.indexOf(",\"Start\":\"")));
		} else {
			updateProcessBtn.setDisabled(true);
			addCompTaskCombobox.setDisabled(true);
			addCompTaskCombobox.setDisabled(true);
			refreshRepositoryBtn.setDisabled(true);
			routeLbl.setValue("No route has been selected.");

		}
	}
	
	@Listen("onClick = #refreshRepositoryBtn")
	public void updateProcessModelSelection() {
		ProcessDefinitionFunctions pdf = new ProcessDefinitionFunctions();
		ListModel<String> processes = new ListModelList<String>(pdf.getAllProcessModels());
		processComboBox.setModel(processes);
	}
	@Listen("onChange = #processComboBox")
	public void processComboBoxChangeAction() {
		if(processComboBox.getSelectedItem() != null) { 
			deployProcessFromRepositoryBtn.setDisabled(false);
		} else {
			deployProcessFromRepositoryBtn.setDisabled(true);
		}
	}
	@Listen("onClick = #deployProcessFromRepositoryBtn")
	public void deployFromRespository() {
		if(processComboBox.getSelectedItem() != null) { 
			String message = "You have selected " + processComboBox.getSelectedItem().getValue() + ". \n Do you want to continue?";
			Messagebox.Button[] buttons = { Messagebox.Button.YES,Messagebox.Button.NO}; 
			String icon = Messagebox.QUESTION;
			EventListener<ClickEvent> eventListener = new EventListener<ClickEvent>() {
				public void onEvent(ClickEvent event) {
					if (Messagebox.ON_YES.equals(event.getName())) {
						String processKey = processComboBox.getSelectedItem().getValue();
						runMigration(processKey, false);
					}
		      }
		   };
		   Messagebox.show(message, "Process Model Selection", buttons, icon, eventListener);
		}
	}
}

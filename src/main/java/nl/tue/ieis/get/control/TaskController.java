package main.java.nl.tue.ieis.get.control;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.nl.tue.ieis.get.activiti.TaskObject;
import main.java.nl.tue.ieis.get.activiti.TaskStatus;
import org.activiti.engine.form.FormProperty;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

public class TaskController extends SelectorComposer<Component> {


	private static final long serialVersionUID = 1L;

	@Wire	private Listbox activityListBox;
	@Wire	private Label 	eventStatus, descriptionLabel,drivingStatus;
	@Wire	private Image	previewImage;
	@Wire	private Hbox 	activityDetails;
	@Wire	private Button 	execButton, terminateBtn, showTerminateWinBtn;
	@Wire	private Timer 	activityDetailsTimer;
	@Wire	private Grid 	varGrid;
	@Wire	private Div 	activityDive;
	@Wire	private Window 	terminateWin;
	
	private TaskServiceImpl activityService = new TaskServiceImpl();
	
	
	//this activity is used to display the details-box after refresh...
	private TaskObject lastSelectedActivity;
	private boolean isDataRequired = false;
	private Map<FormProperty, InputElement> generatedDataField = new HashMap<FormProperty, InputElement>();
	private Map<FormProperty,String> dateTimePattern = new HashMap<FormProperty,String>();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		try{
			List<TaskObject> result = activityService.updateInfo();
			activityListBox.setModel(new ListModelList<TaskObject>(result));
			activityDetails.setVisible(false);
			setEventStatus(activityService.getCaseID());
			showTerminateButton(activityService.getCaseID());
		
		} catch(Exception e) {
			e.getStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	@Listen("onSelect = #activityListBox")
	public void onSelectActivity() {
		activityDetails.setVisible(true);
		varGrid.setVisible(false);
		isDataRequired = false;
		showDetail();
	}
	
	private void showDetail() {
		try {
			if(activityListBox.getSelectedItem() != null) {
				TaskObject selected = activityListBox.getSelectedItem().getValue();
				lastSelectedActivity = selected;
				updateActivityDetailsView(selected);
			} else {
				activityDetails.setVisible(false);
				execButton.setDisabled(true);
				previewImage.setVisible(false);
				varGrid.setVisible(false);
				isDataRequired = false;
				clearVarGrid();
				generatedDataField.clear();
			}
		} catch (Exception e) {
			e.getStackTrace();
			System.out.println(e.getMessage());
		}
 	}
	
	private void updateActivityDetailsView(TaskObject selected) {
		try {
			//activityLabel.setValue("Selected Task is: " + selected.getTaskName());
			//statusLabel.setValue("Status is: " + selected.getTaskStatus().toString());
			descriptionLabel.setValue("");
			
			if(selected.getTaskStatus() == TaskStatus.EXECUTING) {
				generateForm(selected);
				execButton.setDisabled(false);
				previewImage.setVisible(true);
			} else { 
				execButton.setDisabled(true);
				previewImage.setVisible(false);
				varGrid.setVisible(false);
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	private void generateForm(TaskObject selected) {
		try {
			Map<FormProperty, InputElement> dataField = new HashMap<FormProperty, InputElement>();
			String caseID = (String)(Sessions.getCurrent()).getAttribute("caseID");
			List<FormProperty> varForm = activityService.createForm(selected, caseID);
			if(varForm != null) {
				if(varForm.size() > 0 ) {
					isDataRequired = true;
					varGrid.setVisible(true);
					if(varForm.size() != varGrid.getRows().getVisibleItemCount()) {
						varGrid.getRows().getChildren().clear();
						createFormVisualization(dataField, varForm, caseID, selected.getTaskId());
					} else {
						int temp = 0;
						for(Component c : varGrid.getRows().getChildren()) {
							if(c instanceof Row) {
								if(c.getChildren().get(0) instanceof Label) {
									for(FormProperty form : varForm) {
										String label = ((Label)c.getChildren().get(0)).getValue();
										if(label.charAt(label.length()-1) == '*') 
											label = label.substring(0, label.length()-1);
										if(form.getName().contentEquals(label)) {
											temp++;
											break;
										}
									}
								}
							}
						}
						if(varForm.size() != temp) {
							varGrid.getRows().getChildren().clear();
							createFormVisualization(dataField, varForm, caseID, selected.getTaskId());
						}
					}
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	private void createFormVisualization( Map<FormProperty, InputElement> dataField, List<FormProperty> varForm, String caseId, String taskId) {
		for(FormProperty var : varForm) {
			Row row = new Row();
			Label varName = new Label(var.getName());
			if(var.isRequired()) {
				varName.setValue(varName.getValue() + "*");
				varName.setStyle("font-weight:bold");
			}
			row.appendChild(varName);
			if (var.getType() instanceof org.activiti.engine.impl.form.DateFormType) {
				Datebox db = new Datebox();
				db.setVflex("1");
				db.setHflex("1");
				db.setFormat("long+medium");
				db.setId(var.getId());
				db.setValue(new Date());
				if(var.isRequired()) db.setConstraint("no empty");
				dataField.put(var, db);
				dateTimePattern.put(var, var.getType().getInformation("datePattern").toString());
				row.appendChild(db);
				
			} else if (var.getType() instanceof org.activiti.engine.impl.form.LongFormType) {
				Spinner sp = new Spinner();
				sp.setVflex("1");
				sp.setHflex("1");
				sp.setId(var.getId());
				if(var.isRequired()) sp.setConstraint("no empty");
				dataField.put(var, sp);
				row.appendChild(sp);
			} else if (var.getType() instanceof org.activiti.engine.impl.form.BooleanFormType) {
				Combobox cb = new Combobox();
				cb.setVflex("1");
				cb.setHflex("1");
				//cb.setId(var.getId());
				if (!var.isRequired()) {
					Comboitem nullItem = new Comboitem("None");
					nullItem.setAttribute("realData", "_null_");
					cb.appendChild(nullItem);
				}
				Comboitem yes = new Comboitem("YES"); yes.setAttribute("realData", "true");	cb.appendChild(yes);
				Comboitem no = new Comboitem("NO"); no.setAttribute("realData", "false");	cb.appendChild(no);
				cb.setReadonly(true);
				cb.setStyle("background: #FFFFFF");
				dataField.put(var, cb);
				row.appendChild(cb);
			} else if (var.getType() instanceof org.activiti.engine.impl.form.EnumFormType) {
				try {
					@SuppressWarnings("unchecked")
					Map<String, String> values = (Map<String, String>) var.getType().getInformation("values");
					if (values != null) {
						Combobox cb = new Combobox();
						cb.setVflex("1");
						cb.setHflex("1");
						if (!var.isRequired()) {
							Comboitem nullItem = new Comboitem("None");
							nullItem.setAttribute("realData", "_null_");
							cb.appendChild(nullItem);
						}
						for (Map.Entry<String, String> entry : values.entrySet()) {
							Comboitem item = new Comboitem(entry.getValue());
							item.setAttribute("realData", entry.getKey());
							cb.appendChild(item);
						}
						cb.setReadonly(true);
						cb.setStyle("background: #FFFFFF");
						dataField.put(var, cb);
						row.appendChild(cb);
					} else {row.appendChild(new Label("cannot find enum data"));}
				} catch (Exception e) { row.appendChild(new Label("cannot load enum data")); }
			} else { // e.g. (var.getType() instanceof org.activiti.engine.impl.form.StringFormType)
				Textbox tb = new Textbox();
				tb.setVflex("1");
				tb.setHflex("1");
				tb.setId(var.getId());
				if(var.isRequired()) tb.setConstraint("no empty");
				dataField.put(var, tb);
				row.appendChild(tb);
			}
			varGrid.getRows().appendChild(row);
		}
		if(dataField != null && dataField.size() > 0) {
			try {
				activityService.setDefaultVariablesForScenario3(caseId, taskId, dataField);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		generatedDataField = dataField;
	}

	private Map<String,String> getData (TaskObject selected) {
		Map<String, String> output = new HashMap<String, String>();
		for (Map.Entry<FormProperty, InputElement> entry : generatedDataField.entrySet()) {
			FormProperty var = entry.getKey();
			InputElement i = entry.getValue();
			if( i instanceof Combobox) { 
				String keyValue = (String) ((Combobox) i).getSelectedItem().getAttribute("realData");
				if(!keyValue.contains("_null_")) 
					output.put(var.getId(), keyValue);
			} else if ( i instanceof Datebox) {
				Date dateValue = ((Datebox) i ).getValue();
				SimpleDateFormat sdf = new SimpleDateFormat(dateTimePattern.get(var));
				output.put(var.getId(), sdf.format(dateValue));
			}
			else{ output.put(var.getId(), i.getText()); }
		}
		return output;
	}
	
	@Listen("onClick = #execButton")
	public void executeTask() {
		try {
			if(execButton.getLabel().contentEquals("Execute")) {
				Map<String,String> data = new HashMap<String, String>();
				if(isDataRequired)
					data = getData(lastSelectedActivity);
				activityService.completeTask(data, lastSelectedActivity.getProcessDefinitionId(), lastSelectedActivity.getTaskId());
				generatedDataField.clear();
				isDataRequired = false;
				Executions.sendRedirect("");
				activityListBox.setModel(new ListModelList<TaskObject>(activityService.updateInfo(lastSelectedActivity.getProcessInstanceId())));
			}
		} catch (Exception e) {
			e.getStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	@Listen("onTimer = #activityDetailsTimer")
	public void updateActivityList() {
		try {
			String caseID = (String)(Sessions.getCurrent()).getAttribute("caseID");
			if(!activityListBox.getModel().equals(activityService.updateInfo(caseID))) {
				activityListBox.setModel(new ListModelList<TaskObject>(activityService.updateInfo(caseID)));
			}
			
			setEventStatus(caseID);
			showTerminateButton(caseID);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void setEventStatus(String caseID){
		try {
			showDetail();
			if(caseID != null && caseID != "-1" && caseID != "0") {
				previewImage.setSrc(activityService.determineEventStatus(caseID)[0]);
				eventStatus.setValue(activityService.determineEventStatus(caseID)[1]);
				execButton.setLabel(activityService.determineEventStatus(caseID)[2]);
				if(activityService.determineEventStatus(caseID)[2].contains("[disabled]"))	
					execButton.setDisabled(true);
			}
		} catch (Exception e) {
			e.getStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	private void showTerminateButton(String caseID) {
		if(caseID != null && caseID != "-1" && caseID != "0") {
			showTerminateWinBtn.setVisible(true);
		} else
			showTerminateWinBtn.setVisible(false);
	}
	
	@Listen("onClick = #showTerminateWinBtn")
	public void showTerminateWinodw() {
		try{
			String caseID = (String)(Sessions.getCurrent()).getAttribute("caseID");
			terminateWin.setTitle("Suspend Case " + caseID);
			activityDive.appendChild(terminateWin);
		} catch (Exception e) { System.out.println(e.getMessage());}
		if (!terminateWin.isVisible()) 
			terminateWin.setVisible(true);
		terminateWin.doHighlighted();
	}
	
	private void clearVarGrid() {
		if(varGrid.getRows().getChildren() != null) {
			varGrid.getRows().getChildren().clear();
		}
	}
}

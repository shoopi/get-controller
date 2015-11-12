package main.java.nl.tue.ieis.get.control;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.identity.User;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.impl.InputElement;

import com.google.gson.Gson;

import main.java.de.ptv.intermodal.IMRoute;
import main.java.nl.portbase.Log;
import main.java.nl.tue.ieis.get.activiti.*;
import main.java.nl.tue.ieis.get.activiti.taskDocumentation.TaskDocumentation.QueryAnnotation.Query;
import main.java.nl.tue.ieis.get.map.*;
import main.java.nl.tue.ieis.get.data.TransportOrderDataManagement;
import main.java.nl.tue.ieis.get.event.publisher.PublishEvent;
import main.java.nl.tue.ieis.get.event.service.MessageObject;
import main.java.nl.tue.ieis.get.event.subscriber.*;
import main.java.nl.tue.ieis.get.event.type.EventType;
import main.java.nl.tue.ieis.get.activiti.config.*;

public class TaskServiceImpl {
	
	private List<TaskObject> taskList = new ArrayList<TaskObject>();
	private TaskFunctions taskFunc = new TaskFunctions();
	private TransportOrderDataManagement tpdm = new TransportOrderDataManagement();
	private String caseID;
	private String userId;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	public String getCaseID() {
		return caseID;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public TaskServiceImpl() {
		this.caseID = (String)(Sessions.getCurrent()).getAttribute("caseID");
		this.userId = "Automatic";
		if((Sessions.getCurrent()).getAttribute("user") != null) { 
			User u = (User)(Sessions.getCurrent()).getAttribute("user");
			userId = u.getId();
		}
		taskList = updateInfo();
	}
	
	public TaskServiceImpl(String caseID, String userId) {
		this.caseID = caseID;
		this.userId = userId;
		taskList = updateInfo();
	}

	public List<TaskObject> getTaskList() {
		return taskList;
	}
		
	
	public boolean completeTask(Map<String, String> varibales, final String specId, final String taskId) throws Exception {
		boolean completed = taskFunc.completeTask(caseID, taskId, varibales);
		if(completed) {
			try{
				if(varibales != null && varibales.size() > 0) {
					setAssetsForCase(varibales);
				}
			} catch(Exception e) {
				
			}
			setEventEnginePreConfig(specId, taskId);
			setEventEnginePostConfig();
			logTaskCompletion(taskId);
		}
		return completed;
	}

	private void setAssetsForCase(Map<String, String> varibales) {
		String assetId = "";
		Date deadline = new Date();
		Date eta = new Date();
		boolean hasAsset = false;
		boolean hasDeadline = false;
		boolean hasETA = false;
		for(Map.Entry<String, String> entry : varibales.entrySet()) {
			if(entry.getKey().contentEquals("truckId")) {
				tpdm.setTruckForTransportationOrder(caseID, entry.getValue().toString());
				assetId = entry.getValue().toString();
				hasAsset = true;
			} else if(entry.getKey().contentEquals("trainId")) {
				tpdm.setTrainForTransportationOrder(caseID, entry.getValue().toString());
				assetId = entry.getValue().toString();
				hasAsset = true;
			} else if(entry.getKey().contentEquals("bargeId")) {
				tpdm.setBargeForTransportationOrder(caseID, entry.getValue().toString());
				assetId = entry.getValue().toString();
				hasAsset = true;
			} else if(entry.getKey().contentEquals("deadline")) {
				try {
					deadline = sdf.parse(entry.getValue());
					hasDeadline= true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if(entry.getKey().contentEquals("eta")) {
				try {
					eta = sdf.parse(entry.getValue());
					hasETA = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if(hasAsset && hasDeadline & hasETA) {
			String routeName = getRouteName(caseID);
			PublishEvent.publishVehicleSelectedEventWithEATandDeadline(assetId, routeName, caseID, deadline, eta);
			
		} else if (hasAsset) {
			String routeName = getRouteName(caseID);
			PublishEvent.publishVehicleSelectedEvent(assetId, routeName, caseID);
		}
	}

	private String getRouteName(String caseId) {
		String routeName = "";
		String route = "";
		if(tpdm.loadRouteByCaseId(caseId) != null)
			route = tpdm.loadRouteByCaseId(caseId);
		Gson gson = new Gson();
		IMRoute imRoute = gson.fromJson(route, IMRoute.class);
		if(imRoute.getName() != null)	
			routeName = imRoute.getName();
		return routeName;
	}

	public void setEventEnginePreConfig(final String specId, final String taskId) {
		try {
			PublishEvent.publishActivityLogEvent(specId, caseID, taskId, TaskStatus.COMPLETED.toString() , userId);
			@SuppressWarnings("unchecked")
			HashMap<String, Query> cloneMap = (HashMap<String, Query>) Event.subscriptionMap.clone();
			//unsubscribe when needed
			for (Entry<String, Query> entry : cloneMap.entrySet()) {
				List<String> endTasks = new ArrayList<String>();
				endTasks = entry.getValue().getScope().getEndTask();
				for(String e : endTasks) {
					if(e.contentEquals(taskId)) {
						String email = ActivitiEngineConfig.caseIDEmailAddress + caseID + ActivitiEngineConfig.emailExtension;
						if(entry.getValue().getEmail() != null && entry.getValue().getEmail().contentEquals(email)) {
							EventSubscriberFactory.unsubscribe(entry.getKey());			
							break;
						}
					}
				}
				
			}
		} catch (Exception e2) { 
			System.out.println(e2.getMessage());
		}
	}
	
	public void setEventEnginePostConfig() {
		
		List<TaskObject> executingTasks = taskFunc.getExecutingTasks(caseID, userId);
		if(executingTasks != null && executingTasks.size() > 0) {
			for(TaskObject t : executingTasks) {
				if(t.getProcessInstanceId() == caseID) {
					PublishEvent.publishActivityLogEvent(t.getProcessDefinitionId(), t.getProcessInstanceId(), t.getTaskId(), TaskStatus.EXECUTING.toString() , userId);
					if(t.getTaskDocumentation() != null && t.getTaskDocumentation().getQueryAnnotation() != null && t.getTaskDocumentation().getQueryAnnotation().getQuery().size() > 0) {
						for(Query query : t.getTaskDocumentation().getQueryAnnotation().getQuery()) {
							String queryText = query.getQueryText().trim();
							if(queryText.contains("$truckId")) {
								String truckId = tpdm.loadTruckIdByCaseId(caseID);
								if(truckId != null && truckId.length() > 0) {
									queryText = queryText.replace("$truckId", truckId);
								}
							}
							if(queryText.contains("$trainId")) {
								String trainId = tpdm.loadTrainIdByCaseId(caseID);
								if(trainId != null && trainId.length() > 0) {
									queryText = queryText.replace("$trainId", trainId);
								}
							}
							if(queryText.contains("$bargeId")) {
								String bargeId = tpdm.loadBargeIdByCaseId(caseID);
								if(bargeId != null && bargeId.length() > 0) {
									queryText = queryText.replace("$bargeId", bargeId);
								}
							}
							if(queryText.contains("$caseId")) {
								//TODO: Implicit conversion from datatype 'Integer' to 'String' is not allowed --> 'caseID' instead of caseID integer.
								queryText = queryText.replace("$caseId", Integer.parseInt(caseID) + "");
							}
							try {
								EventType eventType2 = EventType.valueOf(query.getEventType().trim());
								String email = ActivitiEngineConfig.caseIDEmailAddress + caseID + ActivitiEngineConfig.emailExtension;
								query.setEmail(email);
								
								if(!Event.subscriptionMap.containsValue(query)) {
									String queueTitle = EventSubscriberFactory.subscribeEvent(new EventRequestObject(queryText, eventType2), email);
									Event.subscriptionMap.put(queueTitle, query);
								}
								
							} catch (Exception e1) {
								System.out.println(e1.getMessage());
								e1.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
	
	public List<TaskObject> updateInfo() {
		if(caseID != null && caseID != "-1" && caseID != "0") {
			return taskFunc.showAllTasksForProcessInstance(caseID, userId);
		}
		else return taskList;
	}
	
	public List<TaskObject> updateInfo(String caseID) {
		if(caseID != null && caseID != "-1" && caseID != "0") {
			return taskFunc.showAllTasksForProcessInstance(caseID, userId);
		}
		else 
			return taskList;
	}
	
	public List<TaskObject> getExecutingTasks() {
		return taskFunc.getExecutingTasks(caseID, userId);
	}
	
	public String[] determineEventStatus(String caseID) {
		String[] output = new String[3];
		try{
			if(caseID != null && caseID != "-1" && caseID != "0") {
				if(tpdm.loadTransportStatusByCaseId(caseID) == StatusCode.TRANSPORTATION_RUNNING.getValue() || 
						tpdm.loadTransportStatusByCaseId(caseID) == StatusCode.TRANSPORTATION_SUSPENDED_REPLANNED.getValue()) {
					output[0] = Icon.PROCESS_MODEL_OK.getUrl();
					output[1] = tpdm.loadDescriptonByCaseId(caseID);
					output[2] = "Execute";
				}  
					
				else if(tpdm.loadTransportStatusByCaseId(caseID) == StatusCode.TRANSPORTATION_CONGESTION.getValue()) {	
					List<MessageObject> warnings = tpdm.loadEventMessageByCaseIdAndType(caseID, EventType.CongestionOnRoute.toString());
					warnings.addAll(tpdm.loadEventMessageByCaseIdAndType(caseID, EventType.CongestionAhead.toString()));
					String message = "";
					for(MessageObject msgObj : warnings) message = message + msgObj.getMessage();
					
					output[0] = Icon.PROCESS_MODEL_WARNING.getUrl();
					output[1] = message;
					output[2] = "Execute";
				}
				
				else if(tpdm.loadTransportStatusByCaseId(caseID) == StatusCode.TRANSPORTATION_UNEXPECTED_EVENT.getValue()) {
					output[0] = Icon.PROCESS_MODEL_ERROR.getUrl();
					String source = tpdm.loadTruckIdByCaseId(caseID);
					output[1] = tpdm.loadUnexpectedTitleByAssetId(source);
					output[2] = "Replan";
				}
				
				else if(tpdm.loadTransportStatusByCaseId(caseID) == StatusCode.TRANSPORTATION_DEADLINE_VIOLATION.getValue() ||
						tpdm.loadTransportStatusByCaseId(caseID) == StatusCode.TRANSPORTATION_LOCK_CLOSED.getValue()) {
					List<MessageObject> warnings = tpdm.loadEventMessageByCaseIdAndType(caseID, EventType.PredictedDeadlineViolation.toString());
					warnings.addAll(tpdm.loadEventMessageByCaseIdAndType(caseID, EventType.TransportationAtRisk.toString()));
					String message = "";
					for(MessageObject msgObj : warnings) message = message + msgObj.getMessage();
					
					output[0] = Icon.PROCESS_MODEL_ERROR.getUrl();
					output[1] = message;
					output[2] = "Execute [disabled]";
				}
				
				else if(tpdm.loadTransportStatusByCaseId(caseID) == StatusCode.TRANSPORTATION_FLIGHT_WARNING.getValue()) {
					List<MessageObject> warnings = tpdm.loadEventMessageByCaseIdAndType(caseID, EventType.Warning.toString());
					String message = "";
					for(MessageObject msgObj : warnings) message = message + msgObj.getMessage();
					output[0] = warnings.get(0).getIconURL();
					output[1] = message;
					output[2] = "Execute";
				}
				
				else if(tpdm.loadTransportStatusByCaseId(caseID) == StatusCode.TRANSPORTATION_FLIGHT_DIVERSION.getValue()) {
					List<MessageObject> diversion = tpdm.loadEventMessageByCaseIdAndType(caseID, EventType.FlightDiversionDetectedShort.toString());
					String message = "";
					for(MessageObject msgObj : diversion) message = message + msgObj.getMessage();
					output[0] = diversion.get(0).getIconURL();
					output[1] = message;
					output[2] = "Execute [disabled]";
				}
			}
		} catch (Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return output;
	}
	
	public List<FormProperty> createForm(TaskObject selected, String caseID) {
		//System.out.println("task ID is: " + selected.getTaskId());
		if(taskFunc.showForm(caseID, selected.getTaskId()) != null )
			return taskFunc.showForm(caseID, selected.getTaskId());
		else
			return new ArrayList<FormProperty>();
	}
	
	public BufferedImage gerProcessImage(String caseID) {
		if(caseID != null && caseID != "-1" && caseID != "0") {
			InputStream img = taskFunc.getProcessModelImage(caseID);
			try {
				BufferedImage img2 = ImageIO.read(img);
				return img2;
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
		return null;
	}
	
	public void setDefaultVariablesForScenario3(String caseId, String taskId, Map<FormProperty, InputElement> dataField) throws ParseException {
		if(ActivitiEngineConfig.scenario3_cases != null && ActivitiEngineConfig.scenario3_cases.size() == 4) {
			if(caseId.contentEquals(ActivitiEngineConfig.scenario3_cases.get(0))) {
				if(taskId.contentEquals("reserveVessel")) {
					for (Map.Entry<FormProperty, InputElement> entry : dataField.entrySet()) {
						FormProperty var = entry.getKey();
						InputElement i = entry.getValue();
						if(var.getId().contentEquals("bargeId")) {
							((Spinner) i).setValue(321);
						} else if (var.getId().contentEquals("eta")) {
							String inputStr = "2015-01-07T18:00:00";
							Date inputDate = sdf.parse(inputStr);
							((Datebox) i ).setValue(inputDate);
						} else if (var.getId().contentEquals("deadline")) {
							String inputStr = "2015-01-09T09:00:00";
							Date inputDate = sdf.parse(inputStr);
							((Datebox) i ).setValue(inputDate);
						}
					}
				} else if (taskId.contentEquals("reserveTruck")) {
					for (Map.Entry<FormProperty, InputElement> entry : dataField.entrySet()) {
						FormProperty var = entry.getKey();
						InputElement i = entry.getValue();
						if(var.getId().contentEquals("truckId")) {
							((Spinner) i).setValue(1);
						} else if (var.getId().contentEquals("eta")) {
							String inputStr = "2015-01-02T13:00:00";
							Date inputDate = sdf.parse(inputStr);
							((Datebox) i ).setValue(inputDate);
						} else if (var.getId().contentEquals("deadline")) {
							String inputStr = "2015-01-02T18:00:00";
							Date inputDate = sdf.parse(inputStr);
							((Datebox) i ).setValue(inputDate);
						}
					}
				}
			} else if (caseId.contentEquals(ActivitiEngineConfig.scenario3_cases.get(1))) {
				if(taskId.contentEquals("reserveVessel")) {
					for (Map.Entry<FormProperty, InputElement> entry : dataField.entrySet()) {
						FormProperty var = entry.getKey();
						InputElement i = entry.getValue();
						if(var.getId().contentEquals("bargeId")) {
							((Spinner) i).setValue(321);
						} else if (var.getId().contentEquals("eta")) {
							String inputStr = "2015-01-07T18:00:00";
							Date inputDate = sdf.parse(inputStr);
							((Datebox) i ).setValue(inputDate);
						} else if (var.getId().contentEquals("deadline")) {
							String inputStr = "2015-01-09T09:00:00";
							Date inputDate = sdf.parse(inputStr);
							((Datebox) i ).setValue(inputDate);
						}
					}
				} else if (taskId.contentEquals("reserveTruck")) {
					for (Map.Entry<FormProperty, InputElement> entry : dataField.entrySet()) {
						FormProperty var = entry.getKey();
						InputElement i = entry.getValue();
						if(var.getId().contentEquals("truckId")) {
							((Spinner) i).setValue(2);
						} else if (var.getId().contentEquals("eta")) {
							String inputStr = "2015-01-02T16:00:00";
							Date inputDate = sdf.parse(inputStr);
							((Datebox) i ).setValue(inputDate);
						} else if (var.getId().contentEquals("deadline")) {
							String inputStr = "2015-01-02T18:00:00";
							Date inputDate = sdf.parse(inputStr);
							((Datebox) i ).setValue(inputDate);
						}
					}
				}
				
			} else if (caseId.contentEquals(ActivitiEngineConfig.scenario3_cases.get(2))) {
				if(taskId.contentEquals("usertask1")) {
					for (Map.Entry<FormProperty, InputElement> entry : dataField.entrySet()) {
						FormProperty var = entry.getKey();
						InputElement i = entry.getValue();
						if(var.getId().contentEquals("bargeId")) {
							((Spinner) i).setValue(321);
						} else if (var.getId().contentEquals("eta")) {
							String inputStr = "2015-01-05T16:00:00";
							Date inputDate = sdf.parse(inputStr);
							((Datebox) i ).setValue(inputDate);
						} else if (var.getId().contentEquals("deadline")) {
							String inputStr = "2015-01-08T06:00:00";
							Date inputDate = sdf.parse(inputStr);
							((Datebox) i ).setValue(inputDate);
						}
					}
				}
			} else if (caseId.contentEquals(ActivitiEngineConfig.scenario3_cases.get(3))) {
				if(taskId.contentEquals("usertask1")) {
					for (Map.Entry<FormProperty, InputElement> entry : dataField.entrySet()) {
						FormProperty var = entry.getKey();
						InputElement i = entry.getValue();
						if(var.getId().contentEquals("trainId")) {
							((Spinner) i).setValue(234);
						} else if (var.getId().contentEquals("eta")) {
							String inputStr = "2015-01-03T10:00:00";
							Date inputDate = sdf.parse(inputStr);
							((Datebox) i ).setValue(inputDate);
						} else if (var.getId().contentEquals("deadline")) {
							String inputStr = "2015-01-05T06:00:00";
							Date inputDate = sdf.parse(inputStr);
							((Datebox) i ).setValue(inputDate);
						}
					}
				} else if (taskId.contentEquals("usertask2")) {
					for (Map.Entry<FormProperty, InputElement> entry : dataField.entrySet()) {
						FormProperty var = entry.getKey();
						InputElement i = entry.getValue();
						if(var.getId().contentEquals("truckId")) {
							((Spinner) i).setValue(4);
						} else if (var.getId().contentEquals("eta")) {
							String inputStr = "2015-01-02T15:22:00";
							Date inputDate = sdf.parse(inputStr);
							((Datebox) i ).setValue(inputDate);
						} else if (var.getId().contentEquals("deadline")) {
							String inputStr = "2015-01-02T20:00:00";
							Date inputDate = sdf.parse(inputStr);
							((Datebox) i ).setValue(inputDate);
						}
					}
				}
			}
		}
	}
	
	private void logTaskCompletion(String taskId) {
		Log log = new Log();
		//log.setToken();
		log.setCorrelationId(ActivitiEngineConfig.generalLogCorrelationId);
		log.setOrganisationId(ActivitiEngineConfig.generalGETControllerCompanyId);
		log.setLevel("INFO");
		log.setType("Task Execution");
		log.setApplication("GETController");
		log.setActivity(taskId);
		log.setMessage("The task " + taskId + " from the case " + caseID + " has been executed by: " + userId);
		log.submitLog();
	}
}

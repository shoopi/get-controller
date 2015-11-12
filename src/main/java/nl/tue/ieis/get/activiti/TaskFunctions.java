package main.java.nl.tue.ieis.get.activiti;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import main.java.nl.tue.ieis.get.activiti.config.ActivitiEngineConfig;
import main.java.nl.tue.ieis.get.activiti.taskDocumentation.TaskDocumentation;
import main.java.nl.tue.ieis.get.data.TransportOrderDataManagement;
import main.java.nl.tue.ieis.get.map.StatusCode;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricFormProperty;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Sessions;


public class TaskFunctions {
	
	private ProcessEngine processEngine = ActivitiEngineConfig.processEngine;
	private TaskService taskService = processEngine.getTaskService();
	private HistoryService historyService = processEngine.getHistoryService();
	private RuntimeService runtimeService = processEngine.getRuntimeService();
	private RepositoryService repositoryService = processEngine.getRepositoryService();
	private FormService formService = processEngine.getFormService();
	
	public static Map<String,List<TaskObject>> compensatoryTasks4Instances = new HashMap<String,List<TaskObject>>(); 
	
	public List<TaskObject> showAllTasksForProcessInstance(String instanceId, String userId) {
		List<TaskObject> tasks = new ArrayList<TaskObject>();
		try{
			ProcessInstance selectedProcessInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
			String definitionID = selectedProcessInstance.getProcessDefinitionId();
			
			tasks.clear();
			ProcessDefinitionFunctions defFunc = new ProcessDefinitionFunctions();
			BpmnModel bpmn = defFunc.getBpmnModel(definitionID);
			for(Process process : bpmn.getProcesses()) {
				for(FlowElement fe : process.getFlowElements()) {
					if(fe instanceof UserTask) {
						if(!(((UserTask) fe).getIncomingFlows().size() == 0 && ((UserTask) fe).getOutgoingFlows().size() == 0)) {
							String documentation = fe.getDocumentation();
							TaskDocumentation taskDocumentation = new TaskDocumentation();
							if(documentation != null && documentation.length() > 10) {
								try{
									StringReader reader = new StringReader(documentation);
									JAXBContext jaxbContext = JAXBContext.newInstance(TaskDocumentation.class);
									Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
									taskDocumentation = (TaskDocumentation) jaxbUnmarshaller.unmarshal(reader);
									} catch(JAXBException e ) {
											System.out.println("System cannot parse the Task Documentation for task " + fe.getId() + ":" + fe.getName() + " - (" + e.getMessage() + ")");
											e.getStackTrace();
									}
								}
							
								//Role Processing
								List<String> userGroupList = ((UserTask) fe).getCandidateGroups();
								if(userGroupList != null && userGroupList.size() > 0 && userId != null) {
									String userGroup = StringUtils.join(userGroupList, ", ");
									UserFunctions uf = new UserFunctions();
									String currentUserGroup = uf.findUserGroups(userId);
									if(currentUserGroup != null && userGroup.contains(currentUserGroup)) {
										tasks.add(new TaskObject(fe.getId(), fe.getName(), TaskType.UserTask , TaskStatus.FUTURE, definitionID, instanceId, taskDocumentation));
									}
								} else if (userGroupList == null || userGroupList.size() == 0) {
									tasks.add(new TaskObject(fe.getId(), fe.getName(), TaskType.UserTask, TaskStatus.FUTURE, definitionID, instanceId, taskDocumentation));
								}
							}
						}
					}
				}
			
			
			List<HistoricTaskInstance> historyTasks = historyService.createHistoricTaskInstanceQuery().processInstanceId(instanceId).list();
			if(historyTasks != null) {
				for(HistoricTaskInstance h : historyTasks) {
					for(TaskObject t : tasks) {
						if(h.getTaskDefinitionKey().contentEquals(t.getTaskId())) {
							t.setTaskStatus(TaskStatus.COMPLETED);
							break;
						}
					}
				}
			}
			
			List<Task> enabledTask = taskService.createTaskQuery().processInstanceId(instanceId).list();
			for(Task e: enabledTask) {
				for(TaskObject t : tasks) {
					if(e.getTaskDefinitionKey().contentEquals(t.getTaskId())) {
						t.setTaskStatus(TaskStatus.EXECUTING);
					}
				}
			}
		} catch (Exception e) {
			TransportOrderDataManagement tpdm = new TransportOrderDataManagement();
			String caseID = (String)(Sessions.getCurrent()).getAttribute("caseID");
			tpdm.updateTransportStatus(caseID, StatusCode.TRANSPORTATION_FINISHED.getValue());
			String truckId = tpdm.loadTruckIdByCaseId(caseID);
			if(truckId != null && truckId.length() > 0) {
				try {
					//if(tpdm.loadAssetStatusById(assetId) == StatusCode.TRUCK_BUSY.getValue())
						tpdm.updateAssetStatus(truckId, StatusCode.TRUCK_FREE.getValue());

				} catch(Exception e2) {}
			}
			tpdm.setTruckForTransportationOrder(instanceId, "");
			System.out.println("Transportation case " + caseID + " has been finished.");
			(Sessions.getCurrent()).setAttribute("caseID" , "0");
		}
		return tasks;
	}
	
	public List<TaskObject> getExecutingTasks(String instanceId, String userId) {
		List<TaskObject> tasks = new ArrayList<TaskObject>();
		tasks = showAllTasksForProcessInstance(instanceId, userId);
		List<TaskObject> enabledTask = new ArrayList<TaskObject>();
		try {
			if(tasks != null) {
				for(TaskObject task : tasks) {
					if(task.getTaskStatus() == TaskStatus.EXECUTING) {
						enabledTask.add(task);
					}
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			System.err.println(e.getMessage() + " in loading all executing tasks for the case " + instanceId);
		}
		return enabledTask;
	}

	public List<FormProperty> showForm(String processInstanceId, String taskId) {
		String currentId = taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey(taskId).singleResult().getId();
		TaskFormData formData = (TaskFormData) formService.getTaskFormData(currentId);
		return formData.getFormProperties();
	}
	
	public boolean completeTask(String instanceId, String taskId, Map<String, String> variable) {
		boolean ok = false;
		try {
			Task task = taskService.createTaskQuery().processInstanceId(instanceId).taskDefinitionKey(taskId).singleResult();
			if(variable == null || variable.size() < 1) {
				taskService.complete(task.getId());
				ok = true;
			} else {
				formService.submitTaskFormData(task.getId(), variable);
				//variable.put("eta","1989/09/17"); 
				//taskService.complete(task.getId(), variable);
				ok = true;
			}
		
		} catch (Exception e) {
			System.out.println("Task " + taskId + " from " + instanceId + " cannot be executed: " + e.getMessage());
			ok = false;
			e.printStackTrace();
			//e.printStackTrace();
			//e.getMessage();
			//e.printStackTrace();
		}
		return ok;
	}
	
	public InputStream getProcessModelImage(String instanceId) {
		try{
			ProcessInstance selectedProcessInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
			String definitionID = selectedProcessInstance.getProcessDefinitionId();
			ProcessDiagramGenerator pdg = new DefaultProcessDiagramGenerator();
			BpmnModel model = repositoryService.getBpmnModel(definitionID);
			
			InputStream imageStream = pdg.generateDiagram(model, "png", runtimeService.getActiveActivityIds(instanceId), 0.75);
			return imageStream;
		} catch(Exception e) {
			return null;
		}
	}
	
	public Map<String,String> retreiveTaskFormVariable(String taskId, String processInstanceId) {
		Map<String,String> variables = new HashMap<String, String>();
		HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).taskDefinitionKey(taskId).singleResult();
		if(task != null) {
			String taskRuntimeId = task.getId();
			
			List<HistoricDetail> detialList = historyService.createHistoricDetailQuery().processInstanceId(processInstanceId).taskId(taskRuntimeId).list();
			if(detialList != null && detialList.size() > 0) {
				for(HistoricDetail hd : detialList) {
					HistoricFormProperty form = (HistoricFormProperty) hd;
					variables.put(form.getPropertyId(), form.getPropertyValue());
				}
			}
			
			/*
			List<HistoricDetail> details = historyService.createHistoricDetailQuery().processInstanceId(processInstanceId).list();
			for (HistoricDetail historicDetail : details) {
				if (historicDetail instanceof HistoricFormPropertyEntity) {
					HistoricFormPropertyEntity formEntity = (HistoricFormPropertyEntity) historicDetail;
					System.out.println(String.format("form->, key: %s, value: %s", formEntity.getPropertyId(), formEntity.getPropertyValue()));
				} else if (historicDetail instanceof HistoricDetailVariableInstanceUpdateEntity) {
					HistoricDetailVariableInstanceUpdateEntity varEntity = (HistoricDetailVariableInstanceUpdateEntity) historicDetail;
					//System.out.println(String.format("variable->, key: %s, value: %s", varEntity.getName(), varEntity.getValue()));
				}
			}
			*/
		}
		
		return variables;
	}
	
}

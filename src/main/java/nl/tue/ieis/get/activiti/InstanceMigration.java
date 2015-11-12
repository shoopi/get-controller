package main.java.nl.tue.ieis.get.activiti;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import main.java.nl.tue.ieis.get.activiti.config.ActivitiEngineConfig;
import main.java.nl.tue.ieis.get.activiti.taskDocumentation.TaskDocumentation;
import main.java.nl.tue.ieis.get.control.TaskServiceImpl;
import main.java.nl.tue.ieis.get.data.TransportOrderDataManagement;
import main.java.nl.tue.ieis.get.event.publisher.PublishEvent;
import main.java.nl.tue.ieis.get.map.StatusCode;
import main.java.nl.tue.ieis.get.map.TransportRequest;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.Gateway;
import org.activiti.bpmn.model.ParallelGateway;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;


import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricData;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.HistoricFormPropertyEntity;
import org.activiti.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.google.gson.Gson;


public class InstanceMigration {
	private boolean addToRunningTasks;
	private ArrayList<TaskObject> allOldTaskObjects;
	private String instanceId;
	private static TransportOrderDataManagement dbMan = new TransportOrderDataManagement();

	
	public InstanceMigration(boolean addToRunningTasks, String instanceId) {
		this.addToRunningTasks = addToRunningTasks;
		this.instanceId = instanceId;
		try {
			String jsonTaskList = dbMan.loadTaskObjectJsonForSuspendedCase(instanceId);
			if(jsonTaskList != null && jsonTaskList.length() > 2) {
				Gson gson = new Gson();
				TaskObject[] taskListArray = gson.fromJson(jsonTaskList, TaskObject[].class);
				allOldTaskObjects = new ArrayList<TaskObject>(Arrays.asList(taskListArray));
			} else 
				return;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static ProcessEngine processEngine = ActivitiEngineConfig.processEngine;
	private static TaskService taskService = processEngine.getTaskService();
	private static HistoryService historyService = processEngine.getHistoryService();
	private static RuntimeService runtimeService = processEngine.getRuntimeService();
	private static ProcessDefinitionFunctions defFunc = new ProcessDefinitionFunctions();
	private	TaskFunctions taskFunc = new TaskFunctions();

	private Map<TaskObject, TaskObject> userTask2compensatoryTask = new HashMap<TaskObject, TaskObject>();
	private Map<TaskObject, TaskObject> userTask2confirmationTask = new HashMap<TaskObject, TaskObject>();
	
	private Map<String, Map<String,String>> task2Varibales = new HashMap<String, Map<String,String>>();
	private List<String> mustBeExecuted = new ArrayList<String>();
	private List<String> executingTasks = new ArrayList<String>();
	
	//public static Map<String, List<TaskObject>> deletedProcessInstance = new HashMap<String, List<TaskObject>>();
		
	public static List<TaskObject> loadAllTasks(String instanceId) {
		List<TaskObject> finalSortedTasks = new ArrayList<TaskObject>();
		List<TaskObject> allTasks = new ArrayList<TaskObject>();
		try{
			ProcessInstance selectedProcessInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
			String definitionID = selectedProcessInstance.getProcessDefinitionId();
			
			BpmnModel bpmn = defFunc.getBpmnModel(definitionID);
			for(Process process : bpmn.getProcesses()) {
				for(FlowElement fe : process.getFlowElements()) {
					if(fe instanceof UserTask) {
						if(((UserTask) fe).getIncomingFlows().size() == 0 && ((UserTask) fe).getOutgoingFlows().size() == 0) {
							allTasks.add(new TaskObject(fe.getId(), fe.getName(), TaskType.CompensatoryTask, TaskStatus.FUTURE, definitionID, instanceId, null));
						} else {
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
							allTasks.add(new TaskObject(fe.getId(), fe.getName(), TaskType.UserTask, TaskStatus.FUTURE, definitionID, instanceId, taskDocumentation));
							}
						}
					}
				}
			List<TaskObject> pastTasks = findPastTasks(instanceId, allTasks);
			List<TaskObject> currentTasks = findExecutingTasks(instanceId, allTasks);
			finalSortedTasks.addAll(pastTasks);
			finalSortedTasks.addAll(currentTasks);
			for(TaskObject t : allTasks) {
				if(t.getTaskStatus() == TaskStatus.FUTURE) {
					finalSortedTasks.add(t);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalSortedTasks;
	}

	private static List<TaskObject> findExecutingTasks(String instanceId, List<TaskObject> allTasks) {
		List<TaskObject> executing = new ArrayList<TaskObject>();
		List<Task> enabledTask = taskService.createTaskQuery().processInstanceId(instanceId).list();
		for(Task e: enabledTask) {
			for(TaskObject t : allTasks) {
				if(e.getTaskDefinitionKey().contentEquals(t.getTaskId())) {
					t.setTaskStatus(TaskStatus.EXECUTING);
					executing.add(t);
				}
			}
		}
		return executing;
	}

	private static List<TaskObject> findPastTasks(String instanceId, List<TaskObject> allTasks) {
		List<TaskObject> pastTasks = new ArrayList<TaskObject>();
		List<HistoricTaskInstance> historyTasks = historyService.createHistoricTaskInstanceQuery().processInstanceId(instanceId).finished().orderByHistoricTaskInstanceEndTime().desc().list();
		if(historyTasks != null) {
			for(HistoricTaskInstance h : historyTasks) {
				for(TaskObject t : allTasks) {
					if(h.getTaskDefinitionKey().contentEquals(t.getTaskId())) {
						t.setTaskStatus(TaskStatus.COMPLETED);
						pastTasks.add(t);
						break;
					}
				}
			}
		}
		return pastTasks;
	}
	
	private void fillTaskMapping() {
		for(TaskObject task : allOldTaskObjects) {
			Map<String, String> variables = taskFunc.retreiveTaskFormVariable(task.getTaskId(), instanceId);
			if(variables != null && variables.size() > 0) 
				task2Varibales.put(task.getTaskId(), variables);
			if(task.getTaskType() != TaskType.CompensatoryTask) {
				if(task.getTaskDocumentation().getReplanningAnnotation() != null) {
					if(task.getTaskDocumentation().getReplanningAnnotation().getConfirmationTask() != null) {
						String confirmationTaskId = task.getTaskDocumentation().getReplanningAnnotation().getConfirmationTask();
						for(TaskObject t : allOldTaskObjects) {
							if(t.getTaskId().contentEquals(confirmationTaskId)) {
								userTask2confirmationTask.put(task, t);
							}
						}
					} if(task.getTaskDocumentation().getReplanningAnnotation().getCompensatoryTask() != null) {
						String comepnsatoryTaskId = task.getTaskDocumentation().getReplanningAnnotation().getCompensatoryTask();
						for(TaskObject t : allOldTaskObjects) {
							if(t.getTaskId().contentEquals(comepnsatoryTaskId)) {
								userTask2compensatoryTask.put(task, t);
							}
						}
					}
				}
			}
		}
	} 
	
	private List<TaskObject> loadActiveCompensatoryTasks() {
		fillTaskMapping();
		List<TaskObject> activeCompensatoryTasks = new ArrayList<TaskObject>();
		for(TaskObject task : allOldTaskObjects) {
			if(task.getTaskStatus() == TaskStatus.COMPLETED) {
				if(task.getTaskDocumentation() != null && task.getTaskDocumentation().getReplanningAnnotation() != null) {
					if(task.getTaskDocumentation().getReplanningAnnotation().isIsRollbackable() != null && 
							task.getTaskDocumentation().getReplanningAnnotation().isIsRollbackable() &&
							!task.getTaskDocumentation().getReplanningAnnotation().isIsConfirmationTask()) {
						if(userTask2compensatoryTask.containsKey(task)) {
							TaskObject compTask = userTask2compensatoryTask.get(task);
							if(userTask2confirmationTask.containsKey(task)) {
								TaskObject confTask = userTask2confirmationTask.get(task);
								//TODO: CHANGE TO ADAPT TO THE PROCESS MODEL FROM MIRELA
								if(confTask.getTaskStatus() == TaskStatus.FUTURE) {
									activeCompensatoryTasks.add(compTask);
								}
							} else {
								activeCompensatoryTasks.add(compTask);
							}
						}
					}
				}
			}
		}
		return activeCompensatoryTasks;
	}
	
	private BpmnModel createNewProcessModel(BpmnModel newProcessDefinition) {
		List<TaskObject> activeCompensatoryTasks = loadActiveCompensatoryTasks();
		List<UserTask> compensatoryUserTasks = new ArrayList<UserTask>();
		//List<TaskObject> allTasks = deletedProcessInstance.get(oldInstanceId);
			
		for(TaskObject t : activeCompensatoryTasks) {
			UserTask ut = new UserTask();
			//In order to prevent any conflicts between the IDs of old and new process
			ut.setId(UUID.randomUUID().toString());
			ut.setName(t.getTaskName());
			
			if(t.getTaskDocumentation() != null) {
				try {
					JAXBContext jaxbContext = JAXBContext.newInstance(TaskDocumentation.class);
					Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
					jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					Writer writer = new StringWriter();
					jaxbMarshaller.marshal(t.getTaskDocumentation(), writer);
					ut.setDocumentation(writer.toString());
				} catch (JAXBException e) {
					e.printStackTrace();
				}
			}
			
			//TODO: dynamically from the process model
			List<String> users = new ArrayList<String>();
			users.add("admin");
			ut.setCandidateGroups(users);
			compensatoryUserTasks.add(ut);
		}
		mustBeExecuted = findSameExecutedTasks(newProcessDefinition, allOldTaskObjects);
		
		for(Process process : newProcessDefinition.getProcesses()) {
			if(compensatoryUserTasks.size() > 0) {
				if(addToRunningTasks) {
					if(mustBeExecuted.size() > 0) {
						for(FlowElement fe : process.getFlowElements()) {
							String lastExecutedUserTaskId = mustBeExecuted.get(0);
							if(fe instanceof UserTask && fe.getId().contentEquals(lastExecutedUserTaskId)) {
								addCompensatoryTasksToRunningTasks(compensatoryUserTasks, process, fe);
								break;
							} else {
								if(fe instanceof StartEvent) {
									addCompensatoryTasksToRunningTasks(compensatoryUserTasks, process, fe);
									break;
								}
							}
						}
					}
				} else {
					addCompensatoryTasksToWholeProcess(compensatoryUserTasks, process);
				}
			}
		}
		return newProcessDefinition;
	}

	private void addCompensatoryTasksToRunningTasks(List<UserTask> compensatoryUserTasks, Process process, FlowElement lastExecutedTask) {
		Map<String, List<SequenceFlow>> executingTasks2outgoingFlow = new HashMap<String, List<SequenceFlow>>();
		List<String> execTaskIDs = new ArrayList<String>();
		List<SequenceFlow> splitIncomingFlows = new ArrayList<SequenceFlow>();
		List<SequenceFlow> joinIncomingFlows = new ArrayList<SequenceFlow>();
		List<SequenceFlow> splitOutgoingFlows = new ArrayList<SequenceFlow>();
		List<SequenceFlow> joinOutgoingFlows = new ArrayList<SequenceFlow>();
				
		/* Find executing tasks that have been activated by the execution of lastExecutedTask */
		for(SequenceFlow f : ((FlowNode) lastExecutedTask).getOutgoingFlows()) {
			for(String s : executingTasks) {
				if(f.getTargetRef().contentEquals(s)) {
					execTaskIDs.add(s);
					splitOutgoingFlows.add(f);
					break;
				} else {
					if(process.getFlowElement(s) != null) {
						FlowNode fe = (FlowNode)process.getFlowElement(s);
						for(SequenceFlow f_prime : fe.getIncomingFlows()) {
							if(f.getTargetRef().contentEquals(f_prime.getSourceRef())) {
								if(process.getFlowElement(f.getTargetRef()) instanceof Gateway) {
									execTaskIDs.add(f.getTargetRef());
									splitOutgoingFlows.add(f);
									break;
								}
							}
						}
					}
				}
			}
		}
		
		
		/* Add two parallel gateways (Split and Join) to the process. */
		ParallelGateway parGatewayJoin = new ParallelGateway();
		parGatewayJoin.setId(UUID.randomUUID().toString());
		process.addFlowElement(parGatewayJoin);
		ParallelGateway parGatewaySplit = new ParallelGateway();
		parGatewaySplit.setId(UUID.randomUUID().toString());
		process.addFlowElement(parGatewaySplit);
		
		/* Change source of splitOutgoingFlows from lastExecutedTask to parGatewaySplit */
		for(SequenceFlow flow : splitOutgoingFlows) {
			flow.setSourceRef(parGatewaySplit.getId());
		}
		
		/* Find sequence flows that point to future tasks from current executing tasks */
		if(execTaskIDs.size() > 0) {
			for(String taskId : execTaskIDs) {
				if(process.getFlowElement(taskId) instanceof FlowNode) {
					FlowNode u = (FlowNode)process.getFlowElement(taskId);
					executingTasks2outgoingFlow.put(u.getId(), u.getOutgoingFlows());
					//Add a sequence from current executing to parallel join gateway
					SequenceFlow flow = new SequenceFlow(u.getId(), parGatewayJoin.getId());
					flow.setId(UUID.randomUUID().toString());
					joinIncomingFlows.add(flow);
					process.addFlowElement(flow);
				} else {
					//TODO : LATER 
				}
			}
		} else {
			
		}
		
		/* Change the source of outgoingFlows from current executing tasks to parallel join gateway */
		for (Map.Entry<String, List<SequenceFlow>> entry : executingTasks2outgoingFlow.entrySet()) {
			List<SequenceFlow> outFlows = entry.getValue();
			for(SequenceFlow f: outFlows) {
				f.setSourceRef(parGatewayJoin.getId());
				joinOutgoingFlows.add(f);
			}
		}

		//Add a flow element from lastExecuted to parallel split gateway
		SequenceFlow splitIncomingFlow = new SequenceFlow(lastExecutedTask.getId(), parGatewaySplit.getId());
		splitIncomingFlow.setId(UUID.randomUUID().toString());
		splitIncomingFlows.add(splitIncomingFlow);
		process.addFlowElement(splitIncomingFlow);

		for(int i = 0; i < compensatoryUserTasks.size(); i++) {
			UserTask t = compensatoryUserTasks.get(i);
			if(process.getFlowElement(t.getId()) == null)
				process.addFlowElement(t);
			
			try {
				UserTask nextT = compensatoryUserTasks.get(i+1);
				if(process.getFlowElement(nextT.getId()) == null)
					process.addFlowElement(nextT);
				
				SequenceFlow flow = new SequenceFlow(t.getId(), nextT.getId());
				flow.setId(UUID.randomUUID().toString());
				process.addFlowElement(flow);
			} catch(IndexOutOfBoundsException e) { 
				//System.out.println(e.getMessage());
			}
		}
		
		SequenceFlow splitOutCompensatory = new SequenceFlow(parGatewaySplit.getId(), compensatoryUserTasks.get(0).getId());
		SequenceFlow joinInCompensatory = new SequenceFlow(compensatoryUserTasks.get(compensatoryUserTasks.size()-1).getId(), parGatewayJoin.getId());		
		
		splitOutCompensatory.setId(UUID.randomUUID().toString());
		joinInCompensatory.setId(UUID.randomUUID().toString());
		
		splitOutgoingFlows.add(splitOutCompensatory);
		joinIncomingFlows.add(joinInCompensatory);
	
		process.addFlowElement(splitOutCompensatory);
		process.addFlowElement(joinInCompensatory);
		
		if(joinOutgoingFlows.size() < 1) {
			EndEvent endEvent = new EndEvent();
			endEvent.setId(UUID.randomUUID().toString());
			process.addFlowElement(endEvent);
			SequenceFlow joinOutExtra = new SequenceFlow(parGatewayJoin.getId(), endEvent.getId());	
			process.addFlowElement(joinOutExtra);
			joinOutgoingFlows.add(joinOutExtra);
			/*
			EndEvent endEvent = null;
			for(FlowElement fe : process.getFlowElements()) {
				if(fe instanceof EndEvent)
					endEvent = (EndEvent) fe;
				SequenceFlow joinOutExtra = new SequenceFlow(parGatewayJoin.getId(), fe.getId());	
				process.addFlowElement(joinOutExtra);
				joinOutgoingFlows.add(joinOutExtra);
			}
			*/
		}
		/*
		if(splitIncomingFlows.size() < 1) {
			StartEvent startEvent = null;			
			for(FlowElement fe : process.getFlowElements()) {
				if(fe instanceof StartEvent)
					startEvent = (StartEvent) fe;
				SequenceFlow splitInExtra = new SequenceFlow(fe.getId(), parGatewaySplit.getId());	
				process.addFlowElement(splitInExtra);
				splitIncomingFlows.add(splitInExtra);
			}
		}
		*/
		parGatewayJoin.setIncomingFlows(joinIncomingFlows);
		parGatewayJoin.setOutgoingFlows(joinOutgoingFlows);
		parGatewaySplit.setOutgoingFlows(splitOutgoingFlows);
		parGatewaySplit.setIncomingFlows(splitIncomingFlows);
	}
	
	private void addCompensatoryTasksToWholeProcess(List<UserTask> compensatoryUserTasks, Process process) {

		List<SequenceFlow> splitIncomingFlows = new ArrayList<SequenceFlow>();
		List<SequenceFlow> joinIncomingFlows = new ArrayList<SequenceFlow>();
		List<SequenceFlow> splitOutgoingFlows = new ArrayList<SequenceFlow>();
		List<SequenceFlow> joinOutgoingFlows = new ArrayList<SequenceFlow>();
		
		StartEvent startEvent = null;
		EndEvent endEvent = null;
		
		for(FlowElement fe : process.getFlowElements()) {
			if(fe instanceof StartEvent)
				startEvent = (StartEvent) fe;
			else if (fe instanceof EndEvent)
				endEvent = (EndEvent) fe;
		}

		/* Add two parallel gateways (Split and Join) to the process. */
		ParallelGateway parGatewayJoin = new ParallelGateway();
		parGatewayJoin.setId(UUID.randomUUID().toString());
		process.addFlowElement(parGatewayJoin);
		ParallelGateway parGatewaySplit = new ParallelGateway();
		parGatewaySplit.setId(UUID.randomUUID().toString());
		process.addFlowElement(parGatewaySplit);
		
		SequenceFlow fromStartToGatewayFlow = new SequenceFlow(startEvent.getId(), parGatewaySplit.getId());
		fromStartToGatewayFlow.setId(UUID.randomUUID().toString());
		process.addFlowElement(fromStartToGatewayFlow);
		splitIncomingFlows.add(fromStartToGatewayFlow);
		
		SequenceFlow fromGatewayToEndFlow = new SequenceFlow(parGatewayJoin.getId(), endEvent.getId());
		fromGatewayToEndFlow.setId(UUID.randomUUID().toString());
		process.addFlowElement(fromGatewayToEndFlow);
		joinOutgoingFlows.add(fromGatewayToEndFlow);
		
		for(SequenceFlow flow : startEvent.getOutgoingFlows()) {
			flow.setSourceRef(parGatewaySplit.getId());
			splitOutgoingFlows.add(flow);
		}
		
		for(SequenceFlow flow : endEvent.getIncomingFlows()) {
			flow.setTargetRef(parGatewayJoin.getId());
			joinIncomingFlows.add(flow);
		}

		for(int i = 0; i < compensatoryUserTasks.size(); i++) {
			UserTask t = compensatoryUserTasks.get(i);
			if(process.getFlowElement(t.getId()) == null)
				process.addFlowElement(t);
			
			try {
				UserTask nextT = compensatoryUserTasks.get(i+1);
				if(process.getFlowElement(nextT.getId()) == null)
					process.addFlowElement(nextT);
				
				SequenceFlow flow = new SequenceFlow(t.getId(), nextT.getId());
				flow.setId(UUID.randomUUID().toString());
				process.addFlowElement(flow);
			} catch(IndexOutOfBoundsException e) { 
				//System.out.println(e.getMessage());
			}
		}
		
		SequenceFlow splitOutCompensatory = new SequenceFlow(parGatewaySplit.getId(), compensatoryUserTasks.get(0).getId());
		SequenceFlow joinInCompensatory = new SequenceFlow(compensatoryUserTasks.get(compensatoryUserTasks.size()-1).getId(), parGatewayJoin.getId());		
		
		splitOutCompensatory.setId(UUID.randomUUID().toString());
		joinInCompensatory.setId(UUID.randomUUID().toString());
		
		splitOutgoingFlows.add(splitOutCompensatory);
		joinIncomingFlows.add(joinInCompensatory);
	
		process.addFlowElement(splitOutCompensatory);
		process.addFlowElement(joinInCompensatory);
		
		parGatewayJoin.setIncomingFlows(joinIncomingFlows);
		parGatewayJoin.setOutgoingFlows(joinOutgoingFlows);
		parGatewaySplit.setOutgoingFlows(splitOutgoingFlows);
		parGatewaySplit.setIncomingFlows(splitIncomingFlows);
	}

	private List<String> findSameExecutedTasks(BpmnModel newProcessDefinition, List<TaskObject> allOldTasks) {
		List<String> mustBeExecuted = new ArrayList<String>();
		for(TaskObject oldTask : allOldTasks) {
				for(Process process : newProcessDefinition.getProcesses()) {
					for(FlowElement newFE : process.getFlowElements()) {
						if(newFE instanceof UserTask) {
							if(oldTask.getTaskStatus() == TaskStatus.COMPLETED) {
								if(newFE.getName().trim().toLowerCase().contentEquals(oldTask.getTaskName().trim().toLowerCase())) {
									if(newFE.getDocumentation() != null && oldTask.getTaskDocumentation() != null) {
										findSameTaskReplanningAnnotation(mustBeExecuted, oldTask, newFE);
										break;
								}
							}
						} else if (oldTask.getTaskStatus() == TaskStatus.EXECUTING) {
							//TODO: decide about rules later
							if(newFE.getName().trim().toLowerCase().contentEquals(oldTask.getTaskName().trim().toLowerCase())) {
								executingTasks.add(newFE.getId());
							}
						}
					} 
				}
			}
		}
		removeDuplicates(executingTasks);
		removeDuplicates(mustBeExecuted);
		return mustBeExecuted;
	}
	
	private <T> void removeDuplicates(List<T> list) {
		Set<T> hashSet = new LinkedHashSet<T>();
		hashSet.addAll(list);
		list.clear();
		list.addAll(hashSet);
	}


	private void findSameTaskReplanningAnnotation(List<String> mustBeExecuted, TaskObject oldTask, FlowElement newFE) {
		String documentation = newFE.getDocumentation();
		TaskDocumentation newTaskDocumentation = new TaskDocumentation();
		try{
			StringReader reader = new StringReader(documentation);
			JAXBContext jaxbContext = JAXBContext.newInstance(TaskDocumentation.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			newTaskDocumentation = (TaskDocumentation) jaxbUnmarshaller.unmarshal(reader);
		} catch(JAXBException e ) {
			System.out.println("System cannot parse the Task Documentation for task " + newFE.getId() + ":" + newFE.getName() + " - (" + e.getMessage() + ")");
			e.getStackTrace();
		}
		if(newTaskDocumentation.getReplanningAnnotation() != null && oldTask.getTaskDocumentation().getReplanningAnnotation() != null) {
			if(newTaskDocumentation.getReplanningAnnotation().isIsConfirmationTask() && oldTask.getTaskDocumentation().getReplanningAnnotation().isIsConfirmationTask()) {
				mustBeExecuted.add(newFE.getId());
				
			} else if (newTaskDocumentation.getReplanningAnnotation().isIsRollbackable() && oldTask.getTaskDocumentation().getReplanningAnnotation().isIsRollbackable()) {
				mustBeExecuted.add(newFE.getId());
			} else if(newTaskDocumentation.getReplanningAnnotation().getSource().trim().toLowerCase().contentEquals(oldTask.getTaskDocumentation().getReplanningAnnotation().getSource().trim().toLowerCase()) &&
				newTaskDocumentation.getReplanningAnnotation().getDestination().trim().toLowerCase().contentEquals(oldTask.getTaskDocumentation().getReplanningAnnotation().getDestination().trim().toLowerCase())) {
				findSameTaskBasedOnVariables(mustBeExecuted, oldTask, newFE);
			}
		} else if(newTaskDocumentation.getReplanningAnnotation() == null && oldTask.getTaskDocumentation().getReplanningAnnotation() == null) {
			findSameTaskBasedOnVariables(mustBeExecuted, oldTask, newFE);
		}
	}
	
	private void findSameTaskBasedOnVariables(List<String> mustBeExecuted,
			TaskObject oldTask, FlowElement newFE) {
		if((((UserTask) newFE).getFormProperties() == null && !task2Varibales.containsKey(oldTask.getTaskId())) || 
				(((UserTask) newFE).getFormProperties().size() == 0 && !task2Varibales.containsKey(oldTask.getTaskId()))) {
			mustBeExecuted.add(newFE.getId());
		} else {
			Map<String,String> taskForm = new HashMap<String, String>();
			if(task2Varibales.containsKey(oldTask.getTaskId()))
				taskForm = task2Varibales.get(oldTask.getTaskId());
			if(((UserTask) newFE).getFormProperties() != null && ((UserTask)newFE).getFormProperties().size() > 0) {
				List<FormProperty> feForm = ((UserTask) newFE).getFormProperties();
				if(feForm.size() == taskForm.size()) {
					boolean ok = true;
					for(FormProperty form : feForm) {
						if(!taskForm.containsKey(form.getId())) {
							ok = false;
						}
					}
					if(ok) {
						Map<String, String> temp = task2Varibales.get(oldTask.getTaskId());
						task2Varibales.remove(oldTask);
						task2Varibales.put(newFE.getId(), temp);
						mustBeExecuted.add(newFE.getId());
					}
				}
			}
		}
	}
	
	private Map<String, Object> getInitProcessVariables() {
		Map<String, Object> processVariables = new HashMap<String, Object>();
		List<HistoricData> historyForm = historyService.createProcessInstanceHistoryLogQuery(instanceId).includeFormProperties().singleResult().getHistoricData();
		ArrayList<String> formKeys = new ArrayList<String>();
		for(HistoricData hd : historyForm) {
			formKeys.add(((HistoricFormPropertyEntity)hd).getPropertyId());
		}
		List<HistoricData> historyVariables = historyService.createProcessInstanceHistoryLogQuery(instanceId).includeVariables().singleResult().getHistoricData();
		for(HistoricData hd : historyVariables) {
			if(!formKeys.contains((((HistoricVariableInstanceEntity)hd).getName()))) {
				processVariables.put((((HistoricVariableInstanceEntity)hd).getName()), (((HistoricVariableInstanceEntity)hd).getValue()));
			}
		}
		return processVariables;
	}
	
	private String deployUpdatedProcessModel(String fileNameOrID, boolean newDeployment) {
		ProcessDefinitionFunctions processModel = new ProcessDefinitionFunctions();
		String definitionId = "";
		if(newDeployment) {
			Map<BpmnModel,String> bpmnModel2DeploymentId = processModel.uploadUpdatedProcessDefinition(fileNameOrID);
		
			for(Map.Entry<BpmnModel, String> entry : bpmnModel2DeploymentId.entrySet()) {
				BpmnModel updatedModel = createNewProcessModel(entry.getKey());
				processModel.deleteProcessModelFromRepositoryByDeploymentId(entry.getValue());
				definitionId = processModel.deployNewProcessDefinitionFromBpmnModel(fileNameOrID + "_updated", updatedModel, true);
			}
		} else {
			BpmnModel firstModel = processModel.getBpmnModel(fileNameOrID);
			BpmnModel updatedModel = createNewProcessModel(firstModel);
			processModel.deleteProcessModelFromRepositoryByDefinitionId(fileNameOrID);
			definitionId = processModel.deployNewProcessDefinitionFromBpmnModel(fileNameOrID + "_updated", updatedModel, true);
		}
		return definitionId;
	}
	
	public static void removeTransportPlan(String caseId) {
		dbMan.removeSuspendedCase(caseId);
		dbMan.updateTransportStatus(caseId, StatusCode.TRANSPORTATION_SUSPENDED_REMOVED.getValue());
	}
	
	private Map<String, String> findVaribaleForTask(String taskId) {
		Map<String, String> localVars = new HashMap<String, String>();			
		if(task2Varibales.containsKey(taskId)) 
			localVars = task2Varibales.get(taskId);
		return localVars;
	}
	
	public String doMigration(String newFileNameOrID, String route, TransportRequest tr, boolean newDeployment) {
		try {
			String definitionId = deployUpdatedProcessModel(newFileNameOrID, newDeployment);
			Map<String, Object> processVariables = getInitProcessVariables();
			ProcessInstanceFunctions processInstance = new ProcessInstanceFunctions();
			String newInstanceId = processInstance.createProcessInstance(processVariables, definitionId);
			
			dbMan.updateTransportStatus(instanceId, StatusCode.TRANSPORTATION_SUSPENDED_REPLANNED.getValue());
			dbMan.addTransportOrder(newInstanceId, route, tr.getSource().getDate().toString(), tr.getDest().getDate().toString(), StatusCode.TRANSPORTATION_RUNNING.getValue());
			
			PublishEvent.publishSelectedRoutet(newInstanceId, route);
			dbMan.removeNoCaseRoute(route);
			dbMan.updateCaseIdForRequest(tr.getCase_id(), newInstanceId);
	
			if(processVariables.containsKey("truckId")) {
				dbMan.updateAssetStatus(processVariables.get("truckId").toString(), StatusCode.TRUCK_BUSY_REPLANNED.getValue());
				dbMan.setTruckForTransportationOrder(newInstanceId, processVariables.get("truckId").toString());
				String routeName = route.substring(route.indexOf("Name\":\"") + 7);
				routeName = routeName.substring(0, routeName.indexOf("\",\""));
				PublishEvent.publishVehicleSelectedEvent(processVariables.get("truckId").toString(), routeName, newInstanceId);
			}
			
			Collections.reverse(mustBeExecuted);
			TaskServiceImpl taskService = new TaskServiceImpl(newInstanceId, "Automatic");
			List<String> remainingTasks = executeTasks(definitionId, mustBeExecuted, taskService);
			int oldRemainingSize = remainingTasks.size();
			int loopCounter = (int) Math.pow(oldRemainingSize, 2);
			while(oldRemainingSize > 0 || loopCounter > 0) {
				remainingTasks = executeTasks(definitionId, remainingTasks, taskService);
				if(remainingTasks.size() != oldRemainingSize) {
					oldRemainingSize = remainingTasks.size();
					loopCounter =  (int) Math.pow(oldRemainingSize, 2);
				} else {
					loopCounter -- ;
				}
				if(loopCounter < 0)
					break;
			}
			dbMan.removeSuspendedCase(instanceId);
			taskService = null;
			userTask2confirmationTask.clear();
			userTask2compensatoryTask.clear();
			mustBeExecuted.clear();
			task2Varibales.clear();
			return newInstanceId;
		} catch(Exception e) {
			System.err.println(e.getMessage());
			return "";
		}
	}


	private List<String> executeTasks(String definitionId, List<String> mustBeExecutedTasks, TaskServiceImpl taskService) {
		List<String> remainingTasks = new ArrayList<String>();
		for(String taskId : mustBeExecutedTasks) {
			Map<String, String> localVars = findVaribaleForTask(taskId);
			try {
				boolean executed = taskService.completeTask(localVars, definitionId, taskId);
				if(executed) {
					System.out.println("Task " + taskId + " has been executed automatically and " + localVars.size() + " variables's been updated.");
					if(remainingTasks.size() > 0) {
						List<Integer> toBeRemoved = new ArrayList<Integer>();
						for(int i = remainingTasks.size() - 1; i == 0; i++) {
							boolean reExecuted = taskService.completeTask(localVars, definitionId, remainingTasks.get(i));
							if(reExecuted)	toBeRemoved.add(i);
						}
						for(int i : toBeRemoved)	
							remainingTasks.remove(i);
					}
				} else {
					remainingTasks.add(taskId);
					System.out.println(taskId + " has been added to the remaining tasks.");
				}
			} catch (Exception e) {
				//System.out.println(e.getMessage());
				//System.out.println("Error in task auto execution for migrating task " + taskId);
				e.printStackTrace();
			}
		}
		return remainingTasks;
	}
}

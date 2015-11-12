package main.java.nl.tue.ieis.get.activiti.service;

import java.util.List;

import main.java.nl.tue.ieis.get.activiti.TaskFunctions;
import main.java.nl.tue.ieis.get.activiti.TaskObject;
import main.java.nl.tue.ieis.get.activiti.taskDocumentation.TaskDocumentation;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.rest.common.api.ActivitiUtil;
import org.activiti.rest.common.api.SecuredResource;
import org.restlet.resource.Get;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TaskList extends SecuredResource {
	
	@Get
	public String getProcessInfo() {
		String processInstanceId = null;
		try {
			if(!authenticate()) 
				return null;
			
			processInstanceId = (String) getRequest().getAttributes().get("processInstanceId");
			ProcessInstance instance = ActivitiUtil.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			
			if(instance == null) {
				throw new ActivitiException("Failed to retrieve process instance with id: " + processInstanceId);
			}
			
			ObjectNode responseJSON = new ObjectMapper().createObjectNode();
			responseJSON.put("processInstanceId", instance.getId());
			responseJSON.put("businessKey", instance.getBusinessKey() != null ? instance.getBusinessKey() : "null");
			responseJSON.put("processDefinitionId", instance.getProcessDefinitionId());
			createTaskList(processInstanceId, responseJSON);
			return responseJSON.toString();

		} catch (Exception e) {
			throw new ActivitiException("Failed to retrieve the process instance details for id " + processInstanceId, e);
		}
	}
	
	private void createTaskList(String processInstanceId, ObjectNode responseJSON) {
		TaskFunctions tf = new TaskFunctions();
		List<TaskObject> tasks = tf.showAllTasksForProcessInstance(processInstanceId, loggedInUser);
		if(tasks != null && tasks.size() > 0) {
			ArrayNode tasksJSON = new ObjectMapper().createArrayNode();
			responseJSON.put("tasks", tasksJSON);
			for (TaskObject task : tasks) {
				ObjectNode taskJSON = new ObjectMapper().createObjectNode();
				taskJSON.put("designTaskId", task.getTaskId());
				taskJSON.put("taskName", task.getTaskName() != null ? task.getTaskName() : "null");
				taskJSON.put("taskType", task.getTaskType().toString() != null ? task.getTaskType().toString() : "null");
				taskJSON.put("taskStatus", task.getTaskStatus().toString() != null ? task.getTaskStatus().toString() : "null");
				createEventNotification(task, taskJSON);
				tasksJSON.add(taskJSON);
			}
		}
	}
	
	private void createEventNotification(TaskObject task, ObjectNode responseJSON) {
		if(task.getTaskDocumentation() != null && task.getTaskDocumentation().getQueryAnnotation() != null) {
			List<TaskDocumentation.QueryAnnotation.Query> queries = task.getTaskDocumentation().getQueryAnnotation().getQuery();
			if(queries != null && queries.size() > 0) {
				ArrayNode queriesJson = new ObjectMapper().createArrayNode();
				ObjectMapper mapper = new ObjectMapper();
				responseJSON.put("queries", queriesJson);
				for(TaskDocumentation.QueryAnnotation.Query query : queries) {
					if(query.getScope().getRoles() != null && query.getScope().getRoles().contains("driver")) {
						JsonNode node = mapper.convertValue(query, JsonNode.class);
						queriesJson.add(node);
					}
				}
			}
		}
	}
}

package main.java.nl.tue.ieis.get.activiti.service;

import java.util.List;
import java.util.Map;

import main.java.nl.tue.ieis.get.activiti.TaskFunctions;

import org.activiti.engine.form.FormProperty;
import org.activiti.rest.common.api.SecuredResource;
import org.restlet.resource.Get;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AssociatedFormToTask extends SecuredResource {
	@Get
	public String getProcessInfo() {
		String processInstanceId = null;
		String taskId = null;
		try {
			if(!authenticate()) 
				return null;
			
			processInstanceId = (String) getRequest().getAttributes().get("processInstanceId");
			taskId = (String) getRequest().getAttributes().get("taskId");
			
			if(taskId == null)				return "Task ID should be inserted.";
			if(processInstanceId == null)	return "Process Instance ID should be inserted.";
			
			ObjectNode responseJSON = new ObjectMapper().createObjectNode();
			responseJSON.put("processInstanceId", processInstanceId);
			responseJSON.put("taskId", taskId);
			createTaskList(processInstanceId, taskId, responseJSON);
			return responseJSON.toString();
			
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void createTaskList(String processInstanceId, String taskId, ObjectNode responseJSON) {
		TaskFunctions taskService = new TaskFunctions();
		List<FormProperty> properties = taskService.showForm(processInstanceId, taskId);
		ArrayNode formJSON = new ObjectMapper().createArrayNode();
		responseJSON.put("properties", formJSON);
		
		if(properties != null && properties.size() > 0) {
			for (FormProperty form : properties) {
				ObjectNode formProperties = new ObjectMapper().createObjectNode();
				formProperties.put("id", form.getId());
				formProperties.put("name", form.getName());
				formProperties.put("type", form.getType().getName());
				if(form.getType() instanceof org.activiti.engine.impl.form.EnumFormType) {
					ArrayNode enums = new ObjectMapper().createArrayNode();
					Map<String, String> values = (Map<String, String>) form.getType().getInformation("values");
					if(values != null) {
						for (Map.Entry<String, String> entry : values.entrySet()) {
							ObjectNode val = new ObjectMapper().createObjectNode();
							val.put("id", entry.getKey());
							val.put("value", entry.getValue());
							enums.add(val);
						}
					}
					formProperties.put("enums", enums);
				}
				formProperties.put("isReadable", form.isReadable());
				formProperties.put("isWriteable", form.isWritable());
				formProperties.put("isRequired", form.isRequired());
				formProperties.put("value", form.getValue());
				formJSON.add(formProperties);
			}
		}
	}
}

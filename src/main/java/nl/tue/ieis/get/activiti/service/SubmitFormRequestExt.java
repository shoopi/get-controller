package main.java.nl.tue.ieis.get.activiti.service;

import java.util.List;

import org.activiti.rest.service.api.RestActionRequest;
import org.activiti.rest.service.api.form.RestFormProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

public class SubmitFormRequestExt extends RestActionRequest {

	  protected String processDefinitionId;
	  protected String processInstanceId;
	  protected String taskId;
	  protected List<RestFormProperty> properties;
	  
	  public String getProcessDefinitionId() {
	    return processDefinitionId;
	  }
	  public void setProcessDefinitionId(String processDefinitionId) {
	    this.processDefinitionId = processDefinitionId;
	  }
	  public String getTaskId() {
	    return taskId;
	  }
	  public void setTaskId(String taskId) {
	    this.taskId = taskId;
	  }
	 
	  public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public void setProperties(List<RestFormProperty> properties) {
	    this.properties = properties;
	  }
	  @JsonTypeInfo(use=Id.CLASS, defaultImpl=RestFormProperty.class)
	  public List<RestFormProperty> getProperties() {
	    return properties;
	  }
}
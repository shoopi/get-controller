package main.java.nl.tue.ieis.get.activiti.service;

import java.util.HashMap;
import java.util.Map;

import main.java.nl.tue.ieis.get.control.TaskServiceImpl;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.rest.common.api.SecuredResource;
import org.activiti.rest.service.api.form.RestFormProperty;
import org.restlet.data.Status;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;

public class FormDataResourceExt extends SecuredResource {
  
  @Post
  public String submitForm(SubmitFormRequestExt submitRequest) {
    if (!authenticate()) {
      return null;
    }

    if (submitRequest == null) {
      throw new ResourceException(new Status(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE.getCode(), 
          "A request body was expected when executing the form submit.", null, null));
    }

    if (submitRequest.getTaskId() == null || submitRequest.getProcessDefinitionId() == null || submitRequest.getProcessInstanceId() == null) {
      throw new ActivitiIllegalArgumentException("All the taskId and processDefinitionId and processInstanceId properties have to be provided");
    }
    
    Map<String, String> propertyMap = new HashMap<String, String>();
    if (submitRequest.getProperties() != null) {
      for (RestFormProperty formProperty : submitRequest.getProperties()) {
        propertyMap.put(formProperty.getId(), formProperty.getValue());
      }
    }
    TaskServiceImpl taskService = new TaskServiceImpl(submitRequest.getProcessInstanceId(), loggedInUser);
    try {
	    boolean completed = taskService.completeTask(propertyMap, submitRequest.getProcessDefinitionId(), submitRequest.getTaskId());
	    if(completed) {
	    	return  "Task " + submitRequest.getTaskId() + " from case " + submitRequest.getProcessInstanceId() + " has been executed via Mobile App.";
	    }
	    else
	    	throw new Exception ("Error in submiting Task " + submitRequest.getTaskId() + " from case " + submitRequest.getProcessInstanceId());
    } catch (Exception e) {
    	return "Your request is unseccessful. Please check the input variables. <br/> " + e.getMessage();
    }
  }
}

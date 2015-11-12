package main.java.nl.tue.ieis.get.activiti.service;


import org.activiti.rest.common.api.SecuredResource;
import org.restlet.resource.Get;
import main.java.nl.tue.ieis.get.control.TaskServiceImpl;


public class EventManager extends SecuredResource{
	@Get
	public String getProcessInfo() throws Exception {
		String taskId = null;
		String specId = null;
		String instanceId = null;
		
		try {
			if(!authenticate()) 
				return null;
			
			taskId = (String) getRequest().getAttributes().get("taskDesignId");
			specId = (String) getRequest().getAttributes().get("processSpecificationId");
			instanceId = (String) getRequest().getAttributes().get("processInstanceId");
			
			if(taskId == null) {
				throw new Exception("Please insert Task (Design) ID");
			}
			
			if(specId == null) {
				throw new Exception("Please insert Process Specification ID");
			}
			
			if(instanceId == null) {
				throw new Exception("Please insert Process Instance ID");
			}
			
			TaskServiceImpl activityService = new TaskServiceImpl(instanceId, loggedInUser);
			activityService.setEventEnginePreConfig(specId, taskId);
			activityService.setEventEnginePostConfig();
			
			return "";

		} catch (Exception e) {
			throw new Exception("Failed to execute your request");
		}
	}
}

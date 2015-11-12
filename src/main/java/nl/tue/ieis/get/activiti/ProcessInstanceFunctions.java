package main.java.nl.tue.ieis.get.activiti;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import main.java.nl.tue.ieis.get.activiti.config.ActivitiEngineConfig;
import main.java.nl.tue.ieis.get.map.StatusCode;
import main.java.nl.tue.ieis.get.data.TransportOrderDataManagement;
import main.java.nl.tue.ieis.get.map.Asset;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;

import com.google.gson.Gson;

public class ProcessInstanceFunctions {
	
	private ProcessEngine processEngine = ActivitiEngineConfig.processEngine;
	private RuntimeService runtimeService = processEngine.getRuntimeService();
	
	public String createProcessInstance(Map<String, Object> variable, String processSpecificationId) {
		try {
			RepositoryService repositoryService = processEngine.getRepositoryService();
			/*
			System.out.println(repositoryService.createProcessDefinitionQuery().processDefinitionKey("GetPrototype0.4").singleResult().getId());
			System.out.println(repositoryService.createProcessDefinitionQuery().processDefinitionKey("Intermodal case 2.5.2").singleResult().getId());
			System.out.println(repositoryService.createProcessDefinitionQuery().processDefinitionKey("8.8.14-Prototype").singleResult().getId());
			System.out.println(repositoryService.createProcessDefinitionQuery().processDefinitionKey("Freight shift").singleResult().getId());
			System.out.println(repositoryService.createProcessDefinitionQuery().processDefinitionKey("Export_Old_Process").singleResult().getId());
			System.out.println(repositoryService.createProcessDefinitionQuery().processDefinitionKey("Intermodal case 2.5.2").singleResult().getId());
			*/
			String processDefinitionId = repositoryService.createProcessDefinitionQuery().processDefinitionId(processSpecificationId).singleResult().getId();
			String instanceId = runtimeService.startProcessInstanceById(processDefinitionId, variable).getProcessInstanceId();
			System.out.println("A new instance (id : "+ instanceId + ") of " + processSpecificationId + " has been created.");
			return instanceId;
		} catch (Exception e) {
			//e.printStackTrace();
			System.err.println(e.getMessage() + " in creating a new process instance for process " + processSpecificationId );
			Messagebox.show(e.getMessage(), "Error found", Messagebox.OK, Messagebox.ERROR);
			return null;
		}
	}
	
	
	public List<ProcessInstance> getAllRunningProcessInstance() {
		return (List<ProcessInstance>) runtimeService.createProcessInstanceQuery().active().list();
	}
	
	
	public List<ProcessInstance> getRunningProcessInstanceForDefinition(String processDefinitionId) {
		return (List<ProcessInstance>) runtimeService.createProcessInstanceQuery().processDefinitionId(processDefinitionId).active().list();
	}
	
	public String getProcessSpecIDFromProcessInstanceID(String instanceId) {
		try {
		ProcessInstance selectedProcessInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
		return selectedProcessInstance.getProcessDefinitionId();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<String> getRunningProcessInstanceIDs(String processSpec) {
		ArrayList<String> ids = new ArrayList<String>();
		List<ProcessInstance> instances = (List<ProcessInstance>) runtimeService.createProcessInstanceQuery().processDefinitionId(processSpec).active().list();
		if(instances != null) {
			List<Integer> temp = new ArrayList<Integer>();
			for(ProcessInstance i : (List<ProcessInstance>)instances) {
				try{ 
					temp.add(Integer.parseInt(i.getProcessInstanceId()));
				} catch (Exception e) {e.printStackTrace(); System.out.println(e.getMessage());}
			}
			Collections.sort(temp);
			for(int i : temp) ids.add(String.valueOf(i));
		}
		return ids;
	}
	
	public ArrayList<String> getAllRunningProcessInstanceIDs() {
		ArrayList<String> ids = new ArrayList<String>();		
		List<ProcessInstance> instances = getAllRunningProcessInstance();

		if(instances != null) {
			List<Integer> temp = new ArrayList<Integer>();
			for(ProcessInstance i : (List<ProcessInstance>)instances) {
				try{ 
					temp.add(Integer.parseInt(i.getProcessInstanceId()));
				} catch (Exception e) {e.printStackTrace(); System.out.println(e.getMessage());}
			}
			Collections.sort(temp);
			for(int i : temp) ids.add(String.valueOf(i));
		}
		return ids;
	}
	
	public String suspendProcessInstance(String instanceId, String reason) {
		try {
			runtimeService.suspendProcessInstanceById(instanceId);
			//InstanceMigration.deletedProcessInstance.put(instanceId, InstanceMigration.loadAllTasks(instanceId));
			TransportOrderDataManagement tpdm = new TransportOrderDataManagement();

			Gson gson = new Gson();
			String jsonTasklist = gson.toJson(InstanceMigration.loadAllTasks(instanceId));
			tpdm.addSuspendedCase(instanceId, jsonTasklist);
			
			runtimeService.deleteProcessInstance(instanceId, reason);
			(Sessions.getCurrent()).setAttribute("caseID" , "0");
	    	tpdm.updateTransportDescription(instanceId, reason);
	    	String truckId = tpdm.loadTruckIdByCaseId(instanceId);
	    	if(truckId != null && truckId.length() > 1) {
	    		Asset asset = tpdm.loadAssetById(truckId);
		    		if(asset != null) {
		    		if(asset.getStatus() == StatusCode.TRUCK_BUSY.getValue())
		    			tpdm.updateAssetStatus(truckId, StatusCode.TRUCK_FREE.getValue());
		    		else if(asset.getStatus() == StatusCode.FLIGHT_DIVERSION.getValue())
		    			tpdm.updateAssetStatus(truckId, StatusCode.FLIGHT_BUSY.getValue());
		    		/*
		    		else if(asset.getStatus() == StatusCode.TRAIN_BUSY.getValue())
		    			tpdm.updateAssetStatus(assetId, StatusCode.TRAIN_FREE.getValue());
		    		*/
		    		tpdm.setTruckForTransportationOrder(instanceId, "");
	    		}
	    	}
    		tpdm.updateTransportStatus(instanceId, StatusCode.TRANSPORTATION_SUSPENDED.getValue());
	    	Executions.sendRedirect(ActivitiEngineConfig.projectURL);
	    	return "Case " + instanceId + " has been terminated. \n " + reason;
		} catch (Exception e) {
			//e.printStackTrace();
			System.err.println(e.getMessage() + " in terminating case " + instanceId);
			Executions.sendRedirect(ActivitiEngineConfig.projectURL);
			return e.getMessage();
		}
	}
}

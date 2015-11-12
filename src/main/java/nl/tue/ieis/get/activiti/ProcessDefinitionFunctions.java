package main.java.nl.tue.ieis.get.activiti;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.nl.tue.ieis.get.activiti.config.ActivitiEngineConfig;

import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.GraphicInfo;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;

public class ProcessDefinitionFunctions {
	
	private ProcessEngine processEngine = ActivitiEngineConfig.processEngine;
	private RepositoryService repositoryService = processEngine.getRepositoryService();
	//private HistoryService historyService = processEngine.getHistoryService();

	public int uploadProcessDefinition(String fileName, InputStream file) {
		repositoryService.createDeployment()
			.name("prototype-process").
			addInputStream(fileName + "20.xml", file)
			.deploy();
		System.out.println("number of process definitions: " + repositoryService.createProcessDefinitionQuery().count());
		return (int) repositoryService.createProcessDefinitionQuery().count();
	}
	
	public int uploadProcessDefinition(String fileName) {
		repositoryService.createDeployment()
			.name("prototype-process")
			.addClasspathResource("main/resources/" + fileName + ".bpmn")
			.deploy();
		System.out.println("number of process definitions: " + repositoryService.createProcessDefinitionQuery().count());
		return (int) repositoryService.createProcessDefinitionQuery().count();
	}
	
	public Map<BpmnModel,String> uploadUpdatedProcessDefinition(String fileName) {
		String deploymentId = repositoryService.createDeployment()
			.name("prototype-process")
			.addClasspathResource("main/resources/replanning/" + fileName + ".bpmn")
			.deploy().getId();
		int deploymnetSize = (int) repositoryService.createDeploymentQuery().deploymentId(deploymentId).count();
		String definitionId = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).active().list().get(deploymnetSize-1).getId();
		BpmnModel model = repositoryService.getBpmnModel(definitionId);
		System.out.println("number of process definitions: " + repositoryService.createProcessDefinitionQuery().count());
		Map<BpmnModel, String> output = new HashMap<BpmnModel, String>();
		output.put(model, deploymentId);
		return output;
	}
	
	
	public List<String> getAllProcessModels() {
		List<String> processes = new ArrayList<String>();
		for(ProcessDefinition pr : repositoryService.createProcessDefinitionQuery().active().list()) {
			processes.add(pr.getId());
		}
		return processes;
	}
	
	public BpmnModel getBpmnModel (String processDefinitionId) {
		return repositoryService.getBpmnModel(processDefinitionId);
	}
	
	public String deployNewProcessDefinitionFromBpmnModel(String definitionFileName, BpmnModel bpmnModel, boolean autolayout) {
		//System.out.println("before final deployment: " +  repositoryService.createProcessDefinitionQuery().count());
		
		
		try { 
			if(autolayout) 
				createLayout(bpmnModel);
		} catch(Exception e){
			System.out.println("The process image cannot be automatically generated. " + e.getMessage());
		}
		String deploymentId = repositoryService.createDeployment()
			.name("prototype-process")
			.addBpmnModel("main/resources/replanning/" + definitionFileName + ".bpmn", bpmnModel)
			.disableSchemaValidation()
			.deploy().getId();
		
		//System.out.println("after final deployment: " +  repositoryService.createProcessDefinitionQuery().count());
		int deploymnetSize = (int) repositoryService.createDeploymentQuery().deploymentId(deploymentId).count();
		List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).active().list();
		String definitionId = definitions.get(deploymnetSize-1).getId();
		
		//InputStream xmlFile = repositoryService.getProcessModel(definitionId);
		//writeToFile("c:\\migration\\test.bpmn", xmlFile);
		
		return definitionId;
	}

	private void createLayout(BpmnModel bpmnModel) {
		@SuppressWarnings("unchecked")
		HashMap<String, GraphicInfo> beforeGraphicElements = (HashMap<String, GraphicInfo>) ((HashMap<String, GraphicInfo>) bpmnModel.getLocationMap()).clone();
		BpmnAutoLayout layout = new BpmnAutoLayout(bpmnModel);
		layout.execute();
		Map<String, GraphicInfo> afterGraphicElements = bpmnModel.getLocationMap();
		for (Map.Entry<String, GraphicInfo> entry : beforeGraphicElements.entrySet()) {
		    if(!afterGraphicElements.containsKey(entry.getKey())) {
		    	//TODO: guess the good values for pool and lanes
		    	bpmnModel.addGraphicInfo(entry.getKey(), new GraphicInfo());
		    }
		}
	}
	
	public void deleteProcessModelFromRepositoryByDeploymentId(String deploymentId) {
		repositoryService.deleteDeployment(deploymentId);
	}
	
	public void deleteProcessModelFromRepositoryByDefinitionId(String definitionId) {
		String deploymentId = repositoryService.createProcessDefinitionQuery().processDefinitionId(definitionId).singleResult().getDeploymentId();
		deleteProcessModelFromRepositoryByDeploymentId(deploymentId);
	}
	
	@SuppressWarnings("unused")
	private void writeToFile(String filePath, InputStream inputStream) {
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(new File(filePath));
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
	 
			System.out.println("Write to File has been Done!");
	 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					// outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	 
			}
		}
    }
}

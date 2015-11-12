package main.java.nl.tue.ieis.get.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.nl.tue.ieis.get.activiti.ProcessInstanceFunctions;
import main.java.nl.tue.ieis.get.activiti.config.ActivitiEngineConfig;
import main.java.nl.tue.ieis.get.data.TransportOrderDataManagement;
import main.java.nl.tue.ieis.get.event.subscriber.EventSubscriberFactory;
import main.java.nl.tue.ieis.get.map.StatusCode;
import main.java.nl.tue.ieis.get.services.CaseService;


public class CaseServiceImpl implements CaseService{
	
	ProcessInstanceFunctions instanceFunc = new ProcessInstanceFunctions();
	
	
	public String createNewCase(String specId, Map<String, Object> variables) {
		try {
			String caseId = instanceFunc.createProcessInstance(variables, specId);
			TaskServiceImpl services = new TaskServiceImpl(caseId, "Automatic");
			services.setEventEnginePostConfig();
			return caseId;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
		
	}

	public String terminateCase(String caseID, String reason) {
		EventSubscriberFactory.unsubscribeAll(ActivitiEngineConfig.caseIDEmailAddress + caseID + ActivitiEngineConfig.emailExtension);
		return instanceFunc.suspendProcessInstance(caseID, reason);
	}

	public void uploadSpecification(String specificationId) {
		// TODO Auto-generated method stub
		
	}

	public Map<String, String> readyForUpdateCases() {
		Map<String, String> cases = new HashMap<String, String>();
		TransportOrderDataManagement tpdm = new TransportOrderDataManagement();
		List<String> terminatedCase = tpdm.loadCasesInSpecificTransportStatus(StatusCode.TRANSPORTATION_SUSPENDED.getValue());
		if(terminatedCase != null && terminatedCase.size() > 0) {
			for(String caseId : terminatedCase) {
				cases.put(caseId, tpdm.loadDescriptonByCaseId(caseId));
			}
		}
		return cases;
	}

	public List<String> terminateAllRelatedCases(String caseID, String reason) {
		List<String> output = new ArrayList<String>();
		String processSpec = instanceFunc.getProcessSpecIDFromProcessInstanceID(caseID);
		if(processSpec != null) {
			List<String> runningProcessInstanceIDs4Spec = instanceFunc.getRunningProcessInstanceIDs(processSpec);
			for(String id : runningProcessInstanceIDs4Spec) {
				String msg = instanceFunc.suspendProcessInstance(id, reason);
				EventSubscriberFactory.unsubscribeAll(ActivitiEngineConfig.caseIDEmailAddress + id + ActivitiEngineConfig.emailExtension);
				output.add(msg);
			}
		}
		return output;
	}
}

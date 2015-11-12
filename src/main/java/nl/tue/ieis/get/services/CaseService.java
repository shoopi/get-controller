package main.java.nl.tue.ieis.get.services;

import java.util.List;
import java.util.Map;


public interface CaseService {
	
	public String createNewCase(String specificationId, Map<String, Object> variables);
		
	public String terminateCase(String caseID, String reason);
	
	public void uploadSpecification(String specificationId);
	
	public Map<String, String> readyForUpdateCases();
	
	public List<String> terminateAllRelatedCases (String caseID, String reason); 
	
}

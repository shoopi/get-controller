package main.java.nl.tue.ieis.get.activiti.config;

import main.java.nl.tue.ieis.get.activiti.ProcessDefinitionFunctions;
import main.java.nl.tue.ieis.get.activiti.ProcessInstanceFunctions;
import main.java.nl.tue.ieis.get.data.TransportOrderDataManagement;
import main.java.nl.tue.ieis.get.event.subscriber.EventSubscriberFactory;
import main.java.nl.tue.ieis.get.event.subscriber.HardCodedSubscription;
import main.java.nl.tue.ieis.get.map.StatusCode;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Initiator;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ActivitiEngineConfig implements Initiator{

	public static ProcessEngine processEngine;
	public final static String projectURL = "http://islab1.ieis.tue.nl:6411/GETController/";


	public static String eventEngineEndPointURL = 
			//"http://islab1.ieis.tue.nl:6411";
			"http://bpt.hpi.uni-potsdam.de/GETAggregationWebService";
			//"http://bpt.hpi.uni-potsdam.de/GETPredictionReceiver";
			//"http://131.155.120.245:8585";

			//"http://bpt.hpi.uni-potsdam.de/GETScenario3 ";
	public final static String flightNumber = "24980875";
	
	public final static String hardCodedEmailAddress = "global_case_all@getservice-project.eu";
	public final static String caseIDEmailAddress = "global_case";
	public final static String emailExtension = "@getservice-project.eu";
	public final static String generalLogCorrelationId = "1234";

	public final static String generalGETControllerCompanyId = "667";
	public final static String inlandWaterwayScenarioProcessModel1 = "myProcess:1:28";
	public final static String inlandWaterwayScenarioProcessModel2 = "myProcess:1:28";
	public final static String inlandWaterwayScenarioProcessModel3 = "scenario33:1:32";
	public final static String inlandWaterwayScenarioProcessModel4 = "scenario34:1:36";
	public final static String inlandWaterwayScenarioProcessModel_online1 = "myProcess:2:40";
	public final static String inlandWaterwayScenarioProcessModel_online2 = "myProcess:3:44";
	
	public static ArrayList<String> scenario3_cases = new ArrayList<String>();

	
	@SuppressWarnings("rawtypes")
	public void doInit(Page page, Map args) throws Exception {
		
		if(processEngine == null) {	
			processEngine = ProcessEngines.getDefaultProcessEngine();
			Group admin = processEngine.getIdentityService().newGroup("admin");
			processEngine.getIdentityService().saveGroup(admin);
			
			Group driver = processEngine.getIdentityService().newGroup("driver");
			processEngine.getIdentityService().saveGroup(driver);
			
			User shaya = processEngine.getIdentityService().newUser("shaya");
			shaya.setPassword("shaya");
			shaya.setFirstName("Shaya");
			shaya.setLastName("Pourmirza");
			processEngine.getIdentityService().saveUser(shaya);
			processEngine.getIdentityService().createMembership("shaya", "admin");
			
			User automatic = processEngine.getIdentityService().newUser("Automatic");
			automatic.setPassword("Automatic");
			automatic.setFirstName("GET Service");
			automatic.setLastName("Project");
			processEngine.getIdentityService().saveUser(automatic);
			processEngine.getIdentityService().createMembership("Automatic", "admin");
			
			User exus = processEngine.getIdentityService().newUser("exus");
			exus.setFirstName("Exus");
			exus.setLastName("Company");
			exus.setPassword("exus");
			exus.setEmail("email@test.com");
			exus.setPassword("exus");
			processEngine.getIdentityService().saveUser(exus);
			processEngine.getIdentityService().createMembership("exus", "driver");

			User getwp4 = processEngine.getIdentityService().newUser("getwp4");
			getwp4.setFirstName("IBM");
			getwp4.setLastName("Company");
			getwp4.setPassword("getwp4");
			getwp4.setEmail("email@test.com");
			processEngine.getIdentityService().saveUser(getwp4);
			processEngine.getIdentityService().createMembership("getwp4", "admin");
			
			ProcessDefinitionFunctions processModel = new ProcessDefinitionFunctions();
			processModel.uploadProcessDefinition("GetPrototype0.4");
			processModel.uploadProcessDefinition("Intermodal case 2.5.2");
			processModel.uploadProcessDefinition("8.8.14-Prototype");
			processModel.uploadProcessDefinition("Freight shift");
			processModel.uploadProcessDefinition("Export_Old_Process");
			processModel.uploadProcessDefinition("Scenario3-1-old");
			processModel.uploadProcessDefinition("Scenario3-3-old");
			processModel.uploadProcessDefinition("Scenario3-4-old");
			processModel.uploadProcessDefinition("Scenario3-1-new");
			processModel.uploadProcessDefinition("Scenario3-2-new");

			ProcessInstanceFunctions processInstance = new ProcessInstanceFunctions();

			for(int i = 0 ; i < 1 ; i++) {
				processInstance.createProcessInstance(new HashMap<String, Object>(), "Freight_Shift_Scenario:1:20");
			}
			

			for(int i = 0 ; i < 2 ; i++) {
				processInstance.createProcessInstance(new HashMap<String, Object>(), "prototypeProcessModel:1:8");
			}
			

			for(int i = 0 ; i < 2 ; i++) {
				processInstance.createProcessInstance(new HashMap<String, Object>(), "process_pool1:1:12");
			}

			for(int i = 0 ; i < 2 ; i++) {
				processInstance.createProcessInstance(new HashMap<String, Object>(), "prototype:1:16");
			}
			



			TransportOrderDataManagement tdm = new TransportOrderDataManagement();
			tdm.configDB();
			tdm.fillInDataForPrototype();
			
			for(String caseID : processInstance.getAllRunningProcessInstanceIDs()) {
				EventSubscriberFactory.unsubscribeAll(caseIDEmailAddress + caseID + emailExtension);
			}
			
			try{
				ConfigEventEngine();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}


			for(int i = 0 ; i < 2 ; i++) {
				Scanner in2 = new Scanner(new FileReader(Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/database/routes/route2.txt")));
				Map<String, Object> varibales = new HashMap<String, Object>();
				String truckId = String.valueOf(43423 + i);
				varibales.put("truckId", truckId);
				String instanceId = processInstance.createProcessInstance(varibales, "process_pool1:2:24");
				TransportOrderDataManagement dbMan = new TransportOrderDataManagement();
				dbMan.addTransportOrder(instanceId, in2.nextLine(), "", "", StatusCode.TRANSPORTATION_RUNNING.getValue());

				dbMan.setTruckForTransportationOrder(instanceId, truckId);
				dbMan.addVehicleTraceRecord(truckId, "0.0", "0.0", "0.0", "", StatusCode.TRUCK_BUSY.getValue(), null);
				in2.close();
			}
		}
	}


	public static boolean ConfigEventEngine() throws org.apache.axis2.AxisFault {
		boolean isOK = EventSubscriberFactory.unsubscribeAll(hardCodedEmailAddress);
		//HardCodedSubscription.subscribeUnexpectedEvent(hardCodedEmailAddress);
		HardCodedSubscription.subscribeForRoadTrafficEventType(hardCodedEmailAddress);

		//HardCodedSubscription.subscribeForLockClosedEventType(hardCodedEmailAddress);
		HardCodedSubscription.subscribeForDeadlineExpired(hardCodedEmailAddress);		
		HardCodedSubscription.subscribeForFlightTrace(hardCodedEmailAddress);
		HardCodedSubscription.subscribeVehicleIncident(hardCodedEmailAddress);
		HardCodedSubscription.subsribeForFlightDiversion(hardCodedEmailAddress);
		return isOK;
	}
	
}
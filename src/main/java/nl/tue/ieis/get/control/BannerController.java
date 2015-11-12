package main.java.nl.tue.ieis.get.control;

import main.java.nl.portbase.Log;
import main.java.nl.tue.ieis.get.activiti.ProcessDefinitionFunctions;
import main.java.nl.tue.ieis.get.activiti.config.ActivitiEngineConfig;

import org.activiti.engine.identity.User;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;


public class BannerController extends SelectorComposer<Component> {

	private static final long serialVersionUID = -6360927128036523270L;
	
	@Wire	private Menuitem 	uploadTop, addTop, showTrafficInfo, configEventEngine, updateTop, multilpeTransTop, releaseAsset;
	@Wire	private Window 		loginWin, trafficWin, addWindows, updateWin, configEventEngineWin, regWin, regOrgWin, multiplePlanWin, releaseWin;
	@Wire	private Button 		initLoginBtn, initRegBtn, returnBtn, logoutBtn, updateRouteBtn;
	@Wire	private Textbox 	username, password;
	@Wire	private Label 		loginMsgLabel, userLabel; 
	@Wire	private Div 		userInfo, bannerWin;
	@Wire	private Image 		userPic;
	@Wire	private Timer 		userTimer;
		
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		setUserBanner();
		determineCurrentBanner();
		userTimer.start();
		
		try {
			String updatedRoute = (String)(Sessions.getCurrent()).getAttribute("selectedUpdateJsonRoute");
			if(updatedRoute != null && updatedRoute.length() > 3) {
				showTerminatedCaseWindow();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Listen("onClick = #releaseAsset")
	public void showreleaseWindow() {
		try{
			bannerWin.appendChild(releaseWin);
		} catch (Exception e) { System.out.println(e.getMessage());}
		if (!releaseWin.isVisible()) 
			releaseWin.setVisible(true);
		releaseWin.doHighlighted();
	}
	
	@Listen("onClick = #multilpeTransTop")
	public void showMultipleTransportationWindow() {
		try{
			bannerWin.appendChild(multiplePlanWin);
		} catch (Exception e) { System.out.println(e.getMessage());}
		if (!multiplePlanWin.isVisible()) 
			multiplePlanWin.setVisible(true);
		multiplePlanWin.doHighlighted();
	}
	
	@Listen("onClick = #showTrafficInfo")
	public void showTrafficInfoWindow() {
		try{
			bannerWin.appendChild(trafficWin);
		} catch (Exception e) { System.out.println(e.getMessage());}
		if (!trafficWin.isVisible()) 
			trafficWin.setVisible(true);
		trafficWin.doOverlapped();
	}
	
	
	@Listen("onClick = #configEventEngine")
	public void showEventConfigWindow() {
		try{
			bannerWin.appendChild(configEventEngineWin);
		} catch (Exception e) { System.out.println(e.getMessage());}
		if (!configEventEngineWin.isVisible()) 
			configEventEngineWin.setVisible(true);
		configEventEngineWin.doHighlighted();
	}
	
	
	@Listen("onClick = #updateTop")
	public void showTerminatedCaseWindow() {
		try{
			bannerWin.appendChild(updateWin);
		} catch (Exception e) { System.out.println(e.getMessage());}
		if (!updateWin.isVisible()) 
			updateWin.setVisible(true);
		updateWin.doHighlighted();
	}
	
	@Listen("onClick = #initLoginBtn")
	public void showLoginWin() {
		try{
			bannerWin.appendChild(loginWin);
		} catch (Exception e) { System.out.println(e.getMessage());}
		if (!loginWin.isVisible())
			loginWin.setVisible(true);
		loginWin.doHighlighted();
	}

	@Listen("onClick = #logoutBtn")
	public void logout() {
		User user = (User)(Sessions.getCurrent()).getAttribute("user");
		(Sessions.getCurrent()).setAttribute("user", null);
		System.out.println("User " + user.getFirstName() + " has been logged out.");
		Log log = new Log();

		log.setCorrelationId(ActivitiEngineConfig.generalLogCorrelationId);
		log.setOrganisationId(user.getId());
		log.setLevel("info");
		log.setType("logout");
		log.setMessage("User " + user.getId() + " has been logged out.");
		log.submitLog();
		
		//setUserBanner();
		Executions.sendRedirect("");
	}
	
	@Listen("onClick = #addTop")
	public void showAddTransportWin() {
		try{
			bannerWin.appendChild(addWindows);
		} catch (Exception e) { System.out.println(e.getMessage());}
		if (!addWindows.isVisible()) {
			addWindows.setVisible(true);
		}
		addWindows.doHighlighted();
	}

	@Listen("onClick = #initRegBtn")
	public void showRegWin() {
		try{
			bannerWin.appendChild(regWin);
			bannerWin.appendChild(regOrgWin);
		} catch (Exception e) { System.out.println(e.getMessage());}
		if (!regWin.isVisible()) {
			regWin.setVisible(true);
			regOrgWin.setVisible(false);
		}
		if(regOrgWin.isVisible()) {
			regOrgWin.setVisible(false);
		}
		regWin.doHighlighted();
	}
	
	@Listen("onOK = #loginWin")
	public void onOkLoginWindow(){
		setUserBanner();
	}
	
	@Listen("onTimer = #userTimer")
	public void onTimerCheck(){
		setUserBanner();
	}
	
	@Listen("onUpload = #uploadTop")
	public void uploadSpecification(UploadEvent event) {
		org.zkoss.util.media.Media media = event.getMedia();
		ProcessDefinitionFunctions defFunc = new ProcessDefinitionFunctions();
		try {
			String name = media.getName();
			if(!name.substring(name.length()-5, name.length()).contentEquals(".bpmn")) {
				Messagebox.show("Please upload a file with .bpmn extension." , "Error", Messagebox.OK, Messagebox.ERROR);
			} else {
				int countSpec = defFunc.uploadProcessDefinition(name, media.getStreamData());
				Messagebox.show(media.getName() + " has been deployed successfully. \n Total number of deployed processes: " + countSpec + ".", 
						"Successful Deployment", Messagebox.OK, Messagebox.INFORMATION);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			Messagebox.show(e.getMessage() , "Error", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	private void setUserBanner() {
		User user = (User)(Sessions.getCurrent()).getAttribute("user");
		if(user != null) {
			userInfo.setVisible(true);
			userPic.setContext("adminPicRC");
			initLoginBtn.setVisible(false);
			initRegBtn.setVisible(false);
			userLabel.setValue(user.getFirstName() + " " + user.getLastName() + " (" + user.getId() + ")");
			logoutBtn.setVisible(true);
		} else {			
			userInfo.setVisible(false);
			initLoginBtn.setVisible(true);
			initRegBtn.setVisible(true);
			userLabel.setValue("No User");
			logoutBtn.setVisible(false);
		}
	}
	
	private void determineCurrentBanner() {
		try {
			if(Executions.getCurrent().getDesktop().getRequestPath().contains("gui/mapPlanPopup.zul")) {
				returnBtn.setVisible(true);
			} else {
				returnBtn.setVisible(false);
			}
				
		} catch(Exception e) {}
	}
	
	@Listen("onClose = #updateWin")
	public void doOnClose() {
       (Sessions.getCurrent()).setAttribute("updateSuspendedCase", false);
	}
	
	@Listen("onClick = #returnBtn")
	public void returnToMainPage() {
		Executions.sendRedirect(ActivitiEngineConfig.projectURL);
	}
}

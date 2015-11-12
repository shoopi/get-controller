package main.java.nl.tue.ieis.get.control;

import java.util.Timer;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Center;
import org.zkoss.zul.East;



public class MainViewController extends SelectorComposer<Component> {

	private static final long serialVersionUID = -6360927128036523270L;
	
	@Wire	private East 	activityEast;
	@Wire	private	Center 	osmMapCenter;
	@Wire	private	Timer	timer;

	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		checkTaskHandler();
	}
	
	@Listen("onTimer = #timer")
	public void updateActivityListOpenness() {
		checkTaskHandler();
	}
	
	private void checkTaskHandler(){
		String caseID = (String) (Sessions.getCurrent()).getAttribute("caseID");
		if(caseID == null || caseID == "-1" || caseID == "0")
			activityEast.setOpen(false);
		else
			activityEast.setOpen(true);
	}
}

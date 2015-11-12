package main.java.nl.tue.ieis.get.control;

import main.java.nl.tue.ieis.get.activiti.config.ActivitiEngineConfig;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Messagebox.ClickEvent;

public class EventEngineController extends SelectorComposer<Component> {

	private static final long serialVersionUID = -7352479718101374229L;
	
	@Wire	private Window 		configEventEngineWin;
	@Wire	private Combobox	eventEngineCombobox;
	@Wire	private Comboitem 	freeTextEngine;
	@Wire	private Button 		eventEngineBtn;
	@Wire	private Label		errorLblEventEngine;
	
	private Comboitem defaultComboItem;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		try{
			for(Component c : eventEngineCombobox.getChildren()) {
				if(c instanceof Comboitem) {
					((Comboitem) c).setStyle("color:grey; weight: normal;");
					if(((Comboitem) c).getValue().toString().contentEquals(ActivitiEngineConfig.eventEngineEndPointURL)) {
						eventEngineCombobox.setSelectedItem((Comboitem)c);
						eventEngineCombobox.getSelectedItem().setStyle("color:navy; font-weight: bold;");
						defaultComboItem = eventEngineCombobox.getSelectedItem();
					}
				}
			}
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
		
	@Listen("onClick = #eventEngineBtn")
	public void addActivitiProcessInstance(){
		try {
			for(Component c : eventEngineCombobox.getChildren()) {
				if(c instanceof Comboitem) {
					((Comboitem) c).setStyle("color:grey; weight: normal;");
				}
			}
			
			if(eventEngineCombobox.getSelectedItem() == null || eventEngineCombobox.getSelectedItem().equals(freeTextEngine)) {
				freeTextEngine.setValue(eventEngineCombobox.getValue());
				freeTextEngine.setLabel("[Edit] " + eventEngineCombobox.getValue());
				eventEngineCombobox.setSelectedItem(freeTextEngine);
			} 
			ActivitiEngineConfig.eventEngineEndPointURL = eventEngineCombobox.getSelectedItem().getValue().toString();
			System.err.println("[UPDATE] Event Engine has changed to " + eventEngineCombobox.getSelectedItem().getValue().toString());
			
			try {
				boolean isOk = ActivitiEngineConfig.ConfigEventEngine();
				if(isOk) {
					defaultComboItem = eventEngineCombobox.getSelectedItem();
					String message = "Event Engine has changed to " + eventEngineCombobox.getSelectedItem().getValue().toString();
					Messagebox.Button[] buttons = { Messagebox.Button.OK}; 
					String icon = Messagebox.INFORMATION;
					EventListener<ClickEvent> eventListener = new EventListener<ClickEvent>() {
						public void onEvent(ClickEvent event) {return;}
				   };
				   Messagebox.show(message, "Event Engine Error Message", buttons, icon, eventListener);
				} else {
					showErrorMessageBox("The Endpoint Address is not reachable.");
				}
				
			} catch (org.apache.axis2.AxisFault e) {
				String message = e.getMessage();
				showErrorMessageBox(message);
			}
			eventEngineCombobox.getSelectedItem().setStyle("color:navy; font-weight: bold;");
			configEventEngineWin.detach();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			errorLblEventEngine.setValue(e.getMessage());
		}
	}

	/**
	 * @param message
	 */
	private void showErrorMessageBox(String message) {
		Messagebox.Button[] buttons = { Messagebox.Button.OK}; 
		String icon = Messagebox.ERROR;
		EventListener<ClickEvent> eventListener = new EventListener<ClickEvent>() {
			public void onEvent(ClickEvent event) {
				ActivitiEngineConfig.eventEngineEndPointURL = defaultComboItem.getValue().toString();
				System.err.println("[UPDATE] Event Engine has changed back to " + eventEngineCombobox.getSelectedItem().getValue().toString());
		  }
   };
   Messagebox.show(message, "Event Engine Error Message", buttons, icon, eventListener);
	}
	
	@Listen("onSelect = #eventEngineCombobox")
	public void changeReadonlySetting() {
		try {
			if(eventEngineCombobox.getSelectedItem() == null|| eventEngineCombobox.getSelectedItem().equals(freeTextEngine)) {
				eventEngineCombobox.setReadonly(false);
			} else {
				eventEngineCombobox.setReadonly(true);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

package main.java.nl.tue.ieis.get.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;


import main.java.nl.tue.ieis.get.data.*;
import main.java.nl.tue.ieis.get.event.type.roadTraffic.*;

public class TrafficWindowController extends SelectorComposer<Component> {
	
	private static final long serialVersionUID = 2348163917018549342L;
	private TransportOrderDataManagement tpdm = new TransportOrderDataManagement();
	private List<RoadTrafficEvent> traffics = new ArrayList<RoadTrafficEvent>();
	private List<RoadTrafficEvent> selectedTraffics = new ArrayList<RoadTrafficEvent>();
	
	@Wire	private Listbox trafficListbox;
	@Wire	private Timer 	trafficTimer;
	@Wire	private Window 	trafficWin;
	
	int trafficSize = 0;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		traffics = tpdm.loadAllTraffic();
		trafficSize = traffics.size();
		trafficListbox.setModel(new ListModelList<RoadTrafficEvent>(traffics));
	}
	
	@Listen("onTimer = #trafficTimer")
	public void updateTrafficList() {
		if(trafficWin.isVisible()) {
			traffics = tpdm.loadAllTraffic();	
			if(traffics.size() != trafficSize) {
				trafficListbox.setModel(new ListModelList<RoadTrafficEvent>(traffics));
				trafficSize = traffics.size();
			}
		}
	}
	
	@Listen("onSelect = #trafficListbox")
    public void updateMessage() {
        Set<Listitem> selectedItems = trafficListbox.getSelectedItems();
        for(Listitem i : selectedItems) {
        	RoadTrafficEvent t = (RoadTrafficEvent)i.getValue();
        	if(selectedTraffics.contains(t)) {
        		selectedTraffics.remove(t);
        		Clients.showNotification("This traffic will be removed from the map.");
        	} else{ 
        		selectedTraffics.add(t);
        		Clients.showNotification("This traffic has been added to the map.");
        	}
        }
        (Sessions.getCurrent()).setAttribute("traffics" , selectedTraffics);
    }

}
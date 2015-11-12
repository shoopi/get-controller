package main.java.nl.tue.ieis.get.control;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import main.java.de.ptv.intermodal.IMPoint;
import main.java.de.ptv.intermodal.IMRoute;
import main.java.nl.tue.ieis.get.activiti.TaskFunctions;

import org.zkoss.gmaps.Gmaps;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Center;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.South;
import org.zkoss.zul.Timer;

public class MapController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;
	@Wire	private Timer 			mapTimer;
	@Wire	private Label 			curr_step;
	@Wire	private Progressmeter 	curr_met;
	@Wire	private South 			progressBarSection;
	@Wire	private Gmaps 			gmaps;
	@Wire	private Center 			mapCenter;
	@Wire	private Image 			processImage;

    private String caseID = "0"; // show all
    public static Map<String,Integer> progressPercentage = new HashMap<String,Integer>();
	private MapServiceImpl mapService = new MapServiceImpl();
	private int percentage2Point = 0;
	
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		try {
			gmaps.setZoom(6);
			gmaps.setShowZoomCtrl(true);
			gmaps.setShowLargeCtrl(true);
			
			caseID = (String)(Sessions.getCurrent()).getAttribute("caseID");
		    constructMapRoutePoint(caseID, true);
		    constructProcessModelImage(caseID);
		    
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private void constructMapRoutePoint(String _caseID, boolean changeCenter) {
		try {	
			mapService.showTraffics(gmaps);
		    if(_caseID == null || _caseID == "0") {
		    	mapService.showAllRoutes(gmaps, progressBarSection, changeCenter);
		    	mapService.showAllAssets(gmaps);
		    	mapCenter.setTitle("Live Map for all Running Cases");
	    	} else {
				mapService.showMessageObjects(gmaps);
	    		IMRoute route = mapService.showSelectedRoute(gmaps, _caseID, progressBarSection, changeCenter);
				if(route != null) {
					List<IMPoint> imPoints = mapService.showAssetByCaseId(gmaps, _caseID);
		    		if(imPoints != null && imPoints.size() > 0) {
		    			for (Entry<String, Integer> entry : progressPercentage.entrySet()) {
		    			    if(entry.getKey().contentEquals(_caseID)) {
		    			    	percentage2Point = entry.getValue();
		    			    	calculateRouteMeter();
		    			    	break;
		    			    }
		    			}
		    		}
		    		mapCenter.setTitle("Live map for Case " + _caseID);
				}
	    	}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	@Listen("onTimer = #mapTimer")
	public void refresh() { 
		try{
			constructMapRoutePoint(caseID, false);
			constructProcessModelImage(caseID);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	private void calculateRouteMeter() {
 		try {
 			curr_step.setValue(percentage2Point + "%");
 			curr_met.setValue(percentage2Point);
 		} catch (Exception e) {}
	}
	
	public static void showNotification(String msg) {
		Clients.showNotification(msg);
	}
	
	public void constructProcessModelImage(String caseID) {
		TaskFunctions taskFunc = new TaskFunctions();
		if(caseID != null && caseID != "-1" && caseID != "0") {
			InputStream img = taskFunc.getProcessModelImage(caseID);
			try {
				if(img != null) {
					BufferedImage img2 = ImageIO.read(img);
					processImage.setContent(img2);
				} else {
					
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
	}
}
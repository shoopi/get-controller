package main.java.nl.tue.ieis.get.control;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.gmaps.Gpolyline;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;

import main.java.de.ptv.intermodal.IMPoint;
import main.java.de.ptv.intermodal.IMRoute;
import main.java.nl.tue.ieis.get.event.service.MessageObject;
import main.java.nl.tue.ieis.get.event.subscriber.ProcessReceivedEvent;
import main.java.nl.tue.ieis.get.event.type.roadTraffic.RoadTrafficEvent;
import main.java.nl.tue.ieis.get.map.*;

public class MapServiceImpl {
	
	ProcessPTVPlan pr = new ProcessPTVPlan();
	ProcessReceivedEvent pe = new ProcessReceivedEvent();
	Helper helper = new Helper();
	
	static int speed;
	static DateTime plannedStart;
	static DateTime currentTime;
	
	@SuppressWarnings("deprecation")
	public void showAllRoutes(Gmaps gmaps, Component progressBar,boolean changeCenter) {
		Map<String, IMRoute> routes = pr.laodAllRunningRoutes();
		if(progressBar != null) progressBar.setVisible(false);
		try{
			
			for(Map.Entry<String, IMRoute> routeEntry : routes.entrySet()) {
				if(routeEntry != null) {
					IMRoute route = routeEntry.getValue();
					if(route != null) {
						Gpolyline line = new Gpolyline();
						String lineColor = determineRouteLineColorByCaseId(routeEntry.getKey());
						line.setColor(lineColor);
						line.setOpacity(90);
						if(route.getPolygon() != null) {
							for(IMPoint point : route.getPolygon()) {
								line.addPoint(point.getLatitude(), point.getLongitude(), 1);
							}
						}
						line.setParent(gmaps);
						if(changeCenter)
							gmaps.setCenter(route.getPolygon()[route.getPolygon().length/2].getLatitude(), route.getPolygon()[route.getPolygon().length/2].getLongitude());
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	@SuppressWarnings("deprecation")
	public void showAllRoutes(Gmaps gmaps, List<IMRoute> routes) {
		try{
			if(routes != null) {
				for(IMRoute route : routes) {
					Gpolyline line = new Gpolyline();
					String lineColor = determineRouteLineColorByCounter(routes.indexOf(route) % 10);
					line.setColor(lineColor);
					line.setOpacity(90);
					for(IMPoint point : route.getPolygon()) {
						line.addPoint(point.getLatitude(), point.getLongitude(), 1);
					}
					line.setParent(gmaps);
					gmaps.setCenter(route.getPolygon()[route.getPolygon().length/2].getLatitude(), route.getPolygon()[route.getPolygon().length/2].getLongitude());
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	@SuppressWarnings("deprecation")
	public IMRoute showSelectedRoute(Gmaps gmaps, String caseId, Component progressBar, boolean changeCenter) {
		IMRoute route = pr.loadRouteByCaseID(caseId);
		if(progressBar != null) progressBar.setVisible(true);
		
		Gpolyline line = new Gpolyline();
		line.setOpacity(90);
		String lineColor = determineRouteLineColorByCaseId(caseId);
		line.setColor(lineColor);
		if(route != null) {
			for(IMPoint point : route.getPolygon()) {
				line.addPoint(point.getLatitude(), point.getLongitude(), 1);
			}
			line.setParent(gmaps);
			if(changeCenter)
				gmaps.setCenter(route.getPolygon()[route.getPolygon().length/2].getLatitude(), route.getPolygon()[route.getPolygon().length/2].getLongitude());
			return route;
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public IMRoute showSelectedRoute(Gmaps gmaps, IMRoute route, int routeIndex) {		
		Gpolyline line = new Gpolyline();
		line.setOpacity(90);
		String lineColor = determineRouteLineColorByCounter(routeIndex);
		line.setColor(lineColor);
		if(route != null) {
			for(IMPoint point : route.getPolygon()) {
				line.addPoint(point.getLatitude(), point.getLongitude(), 1);
			}
			line.setParent(gmaps);
			gmaps.setCenter(route.getPolygon()[route.getPolygon().length/2].getLatitude(), route.getPolygon()[route.getPolygon().length/2].getLongitude());
			return route;
		}
		return null;
	}
	
	public List<IMPoint> showAssetByCaseId(Gmaps gmaps, String caseId) {
		List<Asset> assets = helper.loadAssetByCaseID(caseId);
		if(assets != null && assets.size() > 0) {
			List<IMPoint> points = new ArrayList<IMPoint>();
			for(Asset asset : assets) {
				String markerColor = setAssetIcon(asset);
				String message = "source: " + asset.getMobOpId();
				if(asset.getEta() != null && asset.getEta().length() > 1) {
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
						SimpleDateFormat stringFormatter = new SimpleDateFormat("HH:mm:ss' 'EEE, MMM d");
						Date d = sdf.parse(asset.getEta());
						message = message + " <br/>ETA: " + stringFormatter.format(d);
						/*
						Duration dur = new Duration(d.getTime(), (new Date()).getTime());
						message = message + "<br/> Duration Left: " + dur.getStandardDays()
								+ " days, " + dur.getStandardHours() + " hours, " + dur.getStandardMinutes() + " minutes, and " + dur.getStandardSeconds() + " seconds.";
						*/
					} catch (Exception e){
						message = message + " <br/>ETA: " + asset.getEta();
					}
				}
				points.add(showPointOnMap(gmaps, asset.getPoint(), asset.getMobOpId(), markerColor, message));
			}
			return points;
		}
		return null;
	}

	
	public void showAllAssets(Gmaps gmaps) {
		List<Asset> assets = helper.traceAllAssets();
		//assets.addAll(helper.traceAllBusyBarges());
		//assets.addAll(helper.traceAllFreeBarges());
		for(Asset asset : assets) {
			IMPoint point = asset.getPoint();
			String markerColor = setAssetIcon(asset);	
			String message = "source: " + asset.getMobOpId();
			if(asset.getEta() != null && asset.getEta().length() > 1) message = message + " <br/>ETA: " + asset.getEta(); 
			showPointOnMap(gmaps, point, asset.getMobOpId(), markerColor, message);
		}
	}
	
	public List<Asset> showFreeAssets(Gmaps gmaps) {
		List<Asset> assets = helper.traceFreeAssets();
		for(Asset asset : assets) {
			IMPoint point = asset.getPoint();
			String markerColor = setAssetIcon(asset);
			
			String popupMsg = 
					"source: " + asset.getMobOpId() + " <a href='#' onclick=\"document.getElementsByName('selectedTruckTextbox')[0].value = '" + asset.getMobOpId() + "'\"> select </a>";
			showPointOnMap(gmaps, point, asset.getMobOpId(), markerColor, popupMsg);
		}
		return assets;
	}
	
	public List<Asset> showAllFlights(Gmaps gmaps) {
		List<Asset> assets = helper.traceAllFlights();
		if(assets != null) {
			for(Asset asset : assets) {
				IMPoint point = asset.getPoint();
				String markerColor = setAssetIcon(asset);
				
				String popupMsg = 
						"Flight #: " + asset.getMobOpId() + " <a href='#' onclick=\"document.getElementsByName('selectedTruckTextbox')[0].value = '" + asset.getMobOpId() + "'\"> select </a>";
				showPointOnMap(gmaps, point, asset.getMobOpId(), markerColor, popupMsg);
			}
			return assets;
		} else 
			return new ArrayList<Asset>();
	}
	/*
	public List<Asset> showAllBarges(Gmaps gmaps) {
		List<Asset> assets = new ArrayList<Asset>();
		assets.addAll(helper.traceAllBusyBarges());
		assets.addAll(helper.traceAllFreeBarges());
		
		if(assets != null) {
			for(Asset asset : assets) {
				IMPoint point = asset.getPoint();
				String markerColor = setAssetIcon(asset);
				
				String popupMsg = 
						"Barge : " + asset.getMobOpId();
				showPointOnMap(gmaps, point, asset.getMobOpId(), markerColor, popupMsg);
			}
			return assets;
		} else 
			return new ArrayList<Asset>();
	}
	*/
	
	public IMPoint showPointOnMap(Gmaps gmaps, IMPoint point, String sourceId, String markerColor, String popupText) {
		try{
			boolean updateMarker = false;
			if(gmaps.getChildren() != null) {
				for(Component c : gmaps.getChildren()) {
					if(c instanceof Gmarker) {
						if(c.getAttribute("sourceID").toString().contentEquals(sourceId)) {
							((Gmarker) c).setLat(point.getLatitude());
							((Gmarker) c).setLng(point.getLongitude());
							((Gmarker) c).setIconImage(markerColor);
							((Gmarker) c).setContent(popupText);
							updateMarker = true;
						} 
					}
				}
			}
			if(!updateMarker) {
				Gmarker marker = new Gmarker(popupText, point.getLatitude(), point.getLongitude());
				marker.setContent(popupText);
				marker.setAttribute("sourceID", sourceId);
				marker.setIconImage(markerColor);
				marker.setParent(gmaps);
			}
		} catch(Exception e) { 
			//e.printStackTrace(); 
			//System.out.println(e.getMessage());
		}
		return point;
	}

	public void showTraffics(Gmaps gmaps) {
		@SuppressWarnings("unchecked")
		List<RoadTrafficEvent> traffics = (List<RoadTrafficEvent>) (Sessions.getCurrent()).getAttribute("traffics");
		if(traffics != null && traffics.size() > 0) {
			for(RoadTrafficEvent t : traffics) {
				IMPoint p = new IMPoint(t.getAreaCenter().getLongitude(), t.getAreaCenter().getLatitude());
				String msg =  "provider: " + t.getProvider() + "<br/> delay: " + t.getDelay();
				showPointOnMap(gmaps, p, t.getIdentifier(), Icon.WARNING_ON_MAP.getUrl().substring(1), msg);
			}
		}
	}
	
	public void showMessageObjects(Gmaps gmaps) {
		@SuppressWarnings("unchecked")
		List<MessageObject> objects = (List<MessageObject>) (Sessions.getCurrent()).getAttribute("messageObjects");
		if(objects != null && objects.size() > 0) {
			for(MessageObject t : objects) {
				IMPoint p = new IMPoint(Double.parseDouble(t.getLongitude()), Double.parseDouble(t.getLatitude()));
				String msg =  t.getMessage();
				showPointOnMap(gmaps, p, t.getMessage(), t.getIconURL().substring(1), msg);
			}
		}
	}
	
	/*
	public int determineDrivingStatus(IMPoint startPoint, IMPoint endPoint, IMPoint currentPoint) {
		try {
			double routeDistance = FindDistance.distance(endPoint.getLatitude(), endPoint.getLongitude(), startPoint.getLatitude(), startPoint.getLongitude(), 'K');			
			double status = 0.0;
			if(currentPoint != null) {
				double passedDistance = FindDistance.distance(currentPoint.getLatitude(), currentPoint.getLongitude(), startPoint.getLatitude(), startPoint.getLongitude(), 'K');
				status = (double)(passedDistance/routeDistance);
			}
			return (int)(status * 100);
		} catch (Exception e) { System.out.println(e.getMessage()); return 0;}
	}
	*/
	/*
	public void removeAllEventIconOnMap(Gmaps gmaps) {
		if(gmaps.getChildren() != null) {
			Iterator<Component> iterator = gmaps.getChildren().iterator();
			while(iterator.hasNext()) {
				Component c = (Component) iterator.next();
				if(c instanceof Gmarker) {
					if(c.getAttribute("sourceID").toString().length() > 15) {
						iterator.remove();
					}
				}
			}
		}
	}
	*/
	
	private String setAssetIcon(Asset asset) {
		String markerColor = Icon.TRUCK_FREE_ICON.getUrl();
		
		if(asset.getStatus() == StatusCode.TRUCK_BUSY.getValue() || 
				asset.getStatus() == StatusCode.TRUCK_BUSY_REPLANNED.getValue())
			markerColor = Icon.TRUCK_BUSY_ICON.getUrl();
		
		else if (asset.getStatus() == StatusCode.TRUCK_UNEXPECTED_POINT.getValue()) 
			markerColor = Icon.UNEXPECTED_POINT_MAP.getUrl();
		
		else if (asset.getStatus() == StatusCode.FLIGHT_BUSY.getValue()) 
			markerColor = Icon.FLIGHT_BUSY_ICON.getUrl();
		
		 else if (asset.getStatus() == StatusCode.FLIGHT_DIVERSION.getValue()) 
			markerColor = Icon.FLIGHT_DIVERTED_ICON.getUrl();
		
		 else if (asset.getStatus() == StatusCode.TRAIN_BUSY.getValue()) 
			markerColor = Icon.TRAIN_BUSY_ICON.getUrl();
		
		 else if (asset.getStatus() == StatusCode.BARGE_FREE.getValue())
			 markerColor = Icon.BARGE_FREE_ICON.getUrl();
		
		 else if (asset.getStatus() == StatusCode.BARGE_BUSY.getValue())
			 markerColor = Icon.BARGE_BUSY_ICON.getUrl();
		
		return markerColor;
	}
	
	private String determineRouteLineColorByCaseId (String caseId) {
		String lineColor = "grey";
		int rem = Integer.parseInt(caseId) % 15;
		switch(rem) {
			case 1: 	lineColor =	"red"; 		break;
	    	case 2: 	lineColor =	"yellow"; 	break;
	    	case 3: 	lineColor =	"green";  	break;
	    	case 4: 	lineColor =	"blue"; 	break;
	    	case 5: 	lineColor =	"violet"; 	break;
	    	case 6: 	lineColor =	"orange"; 	break;
	    	case 7:		lineColor = "brown"; 	break;
	    	case 8:		lineColor = "purple";	break;
	    	case 9:		lineColor = "maroon";	break;
	    	case 10:	lineColor = "navy";		break;
	    	case 11:	lineColor = "black"; 	break;
	    	case 12:	lineColor = "white";	break;
	    	case 13:	lineColor = "cyan";		break;
	    	case 14:	lineColor = "pink";		break;
	    	case 0: 	lineColor = "magenta";	break;
		}
		return lineColor;
	}
	
	private String determineRouteLineColorByCounter (int routeCounter) {
		String 	lineColor = "grey"; 
		switch(routeCounter) {
		case 0: 	lineColor =	"red"; 		break;
		case 1: 	lineColor =	"yellow"; 	break;
		case 2: 	lineColor =	"green";  	break;
		case 3: 	lineColor =	"blue"; 	break;
		case 4: 	lineColor =	"violet"; 	break;
		case 5: 	lineColor =	"orange"; 	break;
		case 6:		lineColor = "brown"; 	break;
		case 7:		lineColor = "purple";	break;
		case 8:		lineColor = "maroon";	break;
		case 9:		lineColor = "navy";		break;
		}
		return lineColor;
	}
}

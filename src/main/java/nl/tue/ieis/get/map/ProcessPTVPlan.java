package main.java.nl.tue.ieis.get.map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Sessions;

import com.google.gson.Gson;

import main.java.de.ptv.intermodal.IMEmission;
import main.java.de.ptv.intermodal.IMPoint;
import main.java.de.ptv.intermodal.IMRoute;
import main.java.de.ptv.intermodal.IMRouteResponse;
import main.java.de.ptv.intermodal.IMStation;
import main.java.de.ptv.intermodal.IMStationTimeEvent;
import main.java.de.ptv.intermodal.IMStationTimes;
import main.java.de.ptv.xserver.XIntermodalWSServiceStub.PlainPoint;
import main.java.de.ptv.xserver.XIntermodalWSServiceStub.RoutingAlternative;
import main.java.de.ptv.xserver.XIntermodalWSServiceStub.RoutingSegment;
import main.java.de.ptv.xserver.XIntermodalWSServiceStub.StationTimeEvent;
import main.java.nl.tue.ieis.get.data.*;
import main.java.nl.tue.ieis.get.json.rpc.PTVClientReceiver;

public class ProcessPTVPlan {
	

	TransportOrderDataManagement tpdm = new TransportOrderDataManagement();
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	private boolean isPTVLoginRequired = false;
	
	public static void main(String[] args) {
		PTVClientReceiver rjson = new PTVClientReceiver(false);
		String fileName = "c:\\route\\scenario3-offline4.json";
		IMRouteResponse responseObject1 = rjson.getLocalRouteResponse(fileName);
		List<IMRoute> allFoundRoutes = new ArrayList<IMRoute>(); 
		allFoundRoutes.addAll(Arrays.asList(responseObject1.getRoutes()));
		IMRoute route1 = allFoundRoutes.get(0);
		List<IMPoint> points = new ArrayList<IMPoint>();
		points.add(route1.getPolygon()[0]);
		for(int i = 0 ; i < route1.getPolygon().length; i++) {
			if(i % 5 == 0) {
				points.add(route1.getPolygon()[i]);
			}
		}
		route1.setPolygon(points.toArray(new IMPoint[points.size()]));
		Gson gson = new Gson();
		String json = gson.toJson(route1);
	 
		try {
			FileWriter writer = new FileWriter("c:\\route\\one-fifth\\scenario3-offline4.json");
			writer.write(json);
			writer.close();
	 
		} catch (IOException e) {
			e.printStackTrace();
		}
	 
		System.out.println(json);
	}
	
	public List<IMRoute> receiveJsonRouteResponse(String jsonRequest) {
		PTVClientReceiver rjson = new PTVClientReceiver(isPTVLoginRequired);
		IMRouteResponse responseObject = rjson.retreivePTVRoutesObject(jsonRequest, false);
		ArrayList<IMRoute> allFoundRoutes = new ArrayList<IMRoute>(Arrays.asList(responseObject.getRoutes()));
		//TODO: write to file for scenario 2
		return allFoundRoutes;
	}
	
	public List<IMRoute> localJsonRouteResponseForFreightShift1() {
		PTVClientReceiver rjson = new PTVClientReceiver(isPTVLoginRequired);
		String filePath1 = Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/demo/FRA-AMS(1).json");
		String filePath2 = Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/demo/FRA-AMS(2).json");
		String filePath3 = Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/demo/FRA-AMS(3).json");
		IMRouteResponse responseObject1 = rjson.getLocalRouteResponse(filePath1);
		IMRouteResponse responseObject2 = rjson.getLocalRouteResponse(filePath2);
		IMRouteResponse responseObject3 = rjson.getLocalRouteResponse(filePath3);
		List<IMRoute> allFoundRoutes = new ArrayList<IMRoute>(); 
		allFoundRoutes.addAll(Arrays.asList(responseObject1.getRoutes()));
		allFoundRoutes.addAll(Arrays.asList(responseObject2.getRoutes()));
		allFoundRoutes.addAll(Arrays.asList(responseObject3.getRoutes()));
		return allFoundRoutes;
	}
	
	public List<IMRoute> localJsonRouteResponseForFreightShift2() {
		PTVClientReceiver rjson = new PTVClientReceiver(isPTVLoginRequired);
		String filePath1 = Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/demo/FRA-AMS(4).json");
		String filePath2 = Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/demo/FRA-AMS(5).json");
		IMRouteResponse responseObject1 = rjson.getLocalRouteResponse(filePath1);
		IMRouteResponse responseObject2 = rjson.getLocalRouteResponse(filePath2);
		List<IMRoute> allFoundRoutes = new ArrayList<IMRoute>(); 
		allFoundRoutes.addAll(Arrays.asList(responseObject1.getRoutes()));
		allFoundRoutes.addAll(Arrays.asList(responseObject2.getRoutes()));
		return allFoundRoutes;
	}
	
	public List<IMRoute> localJsonRouteResponseForExportCase(boolean update) {
		PTVClientReceiver rjson = new PTVClientReceiver(isPTVLoginRequired);
		String file = "scenario1.json";
		if(update) file = "scenario1-update.json"; 
						  //"PTVResponseAdapted.json";
						//"PTVResponseAdapted_M.json";
		String filePath = Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/demo/scenario1/" + file);
		IMRouteResponse responseObject = rjson.getLocalRouteResponse(filePath);
		ArrayList<IMRoute> allFoundRoutes = new ArrayList<IMRoute>(Arrays.asList(responseObject.getRoutes()));
		
		return allFoundRoutes;
	}
	
	public List<IMRoute> localJsonRouteResponseForInlandWaterway(boolean update) {
		PTVClientReceiver rjson = new PTVClientReceiver(isPTVLoginRequired);
		String file = "PTVResponseScenario3.json";
		if(update) file = "Scenario3/scenario3-online1-2.json"; 
		String filePath1 = Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/demo/" + file);
		IMRouteResponse responseObject1 = rjson.getLocalRouteResponse(filePath1);
		List<IMRoute> allFoundRoutes = new ArrayList<IMRoute>(); 
		//IMRouteResponse responseObject2 = rjson.getLocalRouteResponse(filePath1);

		allFoundRoutes.addAll(Arrays.asList(responseObject1.getRoutes()));
		return allFoundRoutes;
	}
	
	
	
	public static IMRoute localJsonRouteResponseForInlandWaterwayOffline(int routeIndex) {
		//PTVClientReceiver rjson = new PTVClientReceiver(false);
		String fileName = "/WEB-INF/demo/Scenario3/scenario3-offline" + routeIndex + ".json";
		String filePath1 = Sessions.getCurrent().getWebApp().getRealPath(fileName);
		IMRoute returnValues = new IMRoute();
		Gson gson = new Gson();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath1));
			returnValues = gson.fromJson(br, IMRoute.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnValues;
	}
	
	public static IMRoute localJsonRouteResponseForInlandWaterwayOnline(){
		String fileName = "/WEB-INF/demo/Scenario3/scenario3-online1-2.json";
		String filePath1 = Sessions.getCurrent().getWebApp().getRealPath(fileName);
		IMRoute returnValues = new IMRoute();
		Gson gson = new Gson();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath1));
			IMRouteResponse rr = gson.fromJson(br, IMRouteResponse.class);
			if(rr != null && rr.getRoutes() != null) {
				returnValues = rr.getRoutes()[0];
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnValues;
	}
	public Map<String, IMRoute> laodAllRunningRoutes() {
		 Map<String, IMRoute> routes = new  HashMap<String, IMRoute>();
		try {
			Map<String, String> routeMap = tpdm.loadRoutesInSpecificTransportStatus(StatusCode.TRANSPORTATION_RUNNING.getValue());
			routeMap.putAll(tpdm.loadRoutesInSpecificTransportStatus(StatusCode.TRANSPORTATION_UNEXPECTED_EVENT.getValue()));
			routeMap.putAll(tpdm.loadRoutesInSpecificTransportStatus(StatusCode.TRANSPORTATION_FLIGHT_DIVERSION.getValue()));
			routeMap.putAll(tpdm.loadRoutesInSpecificTransportStatus(StatusCode.TRANSPORTATION_FLIGHT_WARNING.getValue()));
			routeMap.putAll(tpdm.loadRoutesInSpecificTransportStatus(StatusCode.TRANSPORTATION_DEADLINE_VIOLATION.getValue()));
			routeMap.putAll(tpdm.loadRoutesInSpecificTransportStatus(StatusCode.TRANSPORTATION_CONGESTION.getValue()));
			routeMap.putAll(tpdm.loadRoutesInSpecificTransportStatus(StatusCode.TRANSPORTATION_LOCK_CLOSED.getValue()));
			
			Gson gson = new Gson();
			if(routeMap != null) {
				for(Map.Entry<String, String> r : routeMap.entrySet()) {
					IMRoute imRoute = gson.fromJson(r.getValue(), IMRoute.class);
					routes.put(r.getKey(), imRoute);
				}
			}
		} catch(Exception e) {
			System.err.println(e.getMessage() + " in loading all routes");
			IMRoute zeroRoute = new IMRoute();
			IMPoint zeroPoint = new IMPoint();
			zeroPoint.setLatitude(0);
			zeroPoint.setLongitude(0);
			IMPoint[] arrayPoint = new IMPoint[1];
			arrayPoint[0] = zeroPoint;
			zeroRoute.setPolygon(arrayPoint);
			routes.put("-1", zeroRoute);
		}
		return routes;
	}
	
	public IMRoute loadRouteByCaseID(String caseID) {
		try {
			String imRouteJsonString = tpdm.loadRouteByCaseId(caseID);
			if(imRouteJsonString != null) {
				Gson gson = new Gson();
				IMRoute imRoute = gson.fromJson(imRouteJsonString, IMRoute.class);
				return imRoute;
			} else {
				IMRoute zeroRoute = new IMRoute();
				IMPoint zeroPoint = new IMPoint();
				zeroPoint.setLatitude(0);
				zeroPoint.setLongitude(0);
				IMPoint[] arrayPoint = new IMPoint[1];
				arrayPoint[0] = zeroPoint;
				zeroRoute.setPolygon(arrayPoint);
				return zeroRoute;
			}
			
		} catch(Exception e) {
			System.err.println(e.getMessage() + " ... cannot load any route for the case " + caseID);
			IMRoute zeroRoute = new IMRoute();
			IMPoint zeroPoint = new IMPoint();
			zeroPoint.setLatitude(0);
			zeroPoint.setLongitude(0);
			IMPoint[] arrayPoint = new IMPoint[1];
			arrayPoint[0] = zeroPoint;
			zeroRoute.setPolygon(arrayPoint);
			return zeroRoute;
		}
	}
	
	public static String getJsonString(IMRoute route) {
		try {
			Gson gson = new Gson();
			return gson.toJson(route);
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	public List<IMRoute> converAlternativeRoutesToIMRoutes(List<RoutingAlternative> routingAlternativeList) {
		List<IMRoute> imRoutes = new ArrayList<IMRoute>();
		for(RoutingAlternative alternativeRoute : routingAlternativeList) {
			try {
				IMRoute route = new IMRoute();
				route.setName(alternativeRoute.getName());
				List<PlainPoint> points = Arrays.asList(alternativeRoute.getPolygon().getWrappedPoints().getPlainPoint());
				List<IMPoint> imPoints = new ArrayList<IMPoint>();
				for(PlainPoint p : points) {
					imPoints.add(new IMPoint(p.getX(), p.getY()));
				}
				route.setPolygon(imPoints.toArray(new IMPoint[imPoints.size()]));
				List<RoutingSegment> stations = Arrays.asList(alternativeRoute.getWrappedItinerary().getRoutingSegment());
				List<IMStation> imStations = new ArrayList<IMStation>();
				for(RoutingSegment st : stations) {
					IMStation imStation = new IMStation();
					imStation.setAccCost(st.getCosts());
					imStation.setAccDistance(st.getDistance());
					IMEmission imEmission1 = new IMEmission();
					imEmission1.setCarbonDioxide(st.getEmission().getCarbonDioxide());
					imStation.setAccEmission(imEmission1);
					imStation.setAccompanied(st.getAccompanied());
					imStation.setAccTime(st.getToStation().getAccTime());
					imStation.setAccTransfers(st.getHandlingTime());
					imStation.setFirstPolygonPointIndex(0);
					imStation.setLastPolygonPointIndex(0);
					//imStation.setLineExtId(st.getLine().getName());
					imStation.setLocationCity(st.getToStation().getStopOff().getCity());
					imStation.setLocationCountry(st.getToStation().getStopOff().getCountry());
					//imStation.setLocationExtId("LocationExtId");
					imStation.setLocationHouseNumber(st.getToStation().getStopOff().getHouseNumber());
					imStation.setLocationName(st.getToStation().getStopOff().getName());
					imStation.setLocationPostalCode(st.getToStation().getStopOff().getPostalCode());
					imStation.setLocationStreet(st.getToStation().getStopOff().getStreet());
					//imStation.setOperatorExtId("ExIdOperator");
					//imStation.setOperatorName("OperatorName");
					imStation.setPoint(new IMPoint(st.getToStation().getStopOff().getPoint().getX(), st.getToStation().getStopOff().getPoint().getY()));
					List<IMStationTimeEvent> imStationTimeEvents = new ArrayList<IMStationTimeEvent>();
					if(st.getToStation().getWrappedStationTimeEvents().getStationTimeEvent() != null) {
						List<StationTimeEvent> stationTimeEvents =  Arrays.asList(st.getToStation().getWrappedStationTimeEvents().getStationTimeEvent());
						for(StationTimeEvent stt : stationTimeEvents) {
							IMStationTimeEvent imStationTimeEvent = new IMStationTimeEvent();
							imStationTimeEvent.setCarbonDioxide(stt.getEmission().getCarbonDioxide());
							imStationTimeEvent.setCost(stt.getCosts());
							imStationTimeEvent.setDuration(stt.getDuration());
							IMEmission imEmission3 = new IMEmission();
							imEmission3.setCarbonDioxide(stt.getEmission().getCarbonDioxide());
							imStationTimeEvent.setEmission(imEmission3);
							imStationTimeEvent.setStartTime(stt.getAccTime());
							imStationTimeEvent.setType(stt.getType());
							imStationTimeEvents.add(imStationTimeEvent);
						}	
					}
					IMStationTimes imStationTime = new IMStationTimes();
					//imStationTime.setArrivalTime(df.format(st.get));
					imStationTime.setCarbonDioxide(st.getEmission().getCarbonDioxide());
					imStationTime.setCosts(st.getCosts());
					//imStationTime.setDeparture();
					//imStationTime.setDepartureTime(df.format(st.getDeparture().getTime()));
					//imStationTime.setEmission(emission);
					imStationTime.setHandlingTime(st.getHandlingTime());
					imStationTime.setWaitingTime(st.getWaitingTime());
					
					imStation.setStationTimes(imStationTime);
					imStation.setTimeEvents(imStationTimeEvents.toArray(new IMStationTimeEvent[imStationTimeEvents.size()]));
					//	st.getTransportMode().getCode()
					imStation.setTransportMode(st.getTransportMode().getName());
					//TODO: Double-check with marcin
					//imStation.setArrival(st.getArrival().getTime().toString());
					//imStation.setDeparture(st.getDeparture().getTime().toString());
					/*
					if(st.getArrival().YEAR > 2000) {
						imStation.setDeadline(st.getToStation(). getArrival().getTime().toString());
					} else if (st.getDeparture().YEAR > 2000) {
						imStation.setDeadline(st.getDeparture().getTime().toString());
					}
					*/
					imStation.setDeadline(st.getClosingTime().getTime().toString());
					
					imStations.add(imStation);
				}
				route.setStations(imStations.toArray(new IMStation[imStations.size()]));
				route.setTotalCost(alternativeRoute.getTotalCosts());
				IMEmission imEmission2 = new IMEmission();
				imEmission2.setCarbonDioxide(alternativeRoute.getTotalEmission().getCarbonDioxide());
				route.setTotalEmission(imEmission2);
				route.setTotalTravelTime(alternativeRoute.getTotalDuration());
				route.setTransfers(alternativeRoute.getNumberOfTransfers());
				route.setTotalDistance(alternativeRoute.getTotalDistance());
				route.setStart(df.format(alternativeRoute.getStartTime().getTime()));
				imRoutes.add(route);
			} catch(Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return imRoutes;
	}
}

package main.java.nl.tue.ieis.get.data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.text.SimpleDateFormat;

import org.zkoss.zk.ui.Sessions;

import main.java.de.ptv.intermodal.IMPoint;
import main.java.nl.tue.ieis.get.activiti.ProcessInstanceFunctions;
import main.java.nl.tue.ieis.get.activiti.config.ActivitiEngineConfig;
import main.java.nl.tue.ieis.get.map.Address;
import main.java.nl.tue.ieis.get.map.AddressTime;
import main.java.nl.tue.ieis.get.map.Asset;
import main.java.nl.tue.ieis.get.map.StatusCode;
import main.java.nl.tue.ieis.get.map.TransportRequest;
import main.java.nl.tue.ieis.get.event.type.roadTraffic.*;
import main.java.nl.tue.ieis.get.event.service.*;

public class TransportOrderDataManagement {

	static Connection conn = DBConnection.getConnection();

	//TODO
	/* ==========================ROUTELOG===================================================*/
	
	public void addTransportOrder(String id, String route, String time1, String time2, int status) {
		try {
			String sql = "INSERT INTO ROUTELOG(ID,ROUTE,TIME1,TIME2,STATUS) VALUES (?,?,?,?,?)"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, route);
			ps.setString(3, time1);
			ps.setString(4, time2);
			ps.setInt(5, status);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " in creating a new routelog.");
			e.printStackTrace();
		}
	}
	
	public void removeTransportOrder(String id) {
		try {
			String sql = "DELETE FROM ROUTELOG WHERE ID = ?"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " in removing a routelog.");
			e.printStackTrace();
		}
	}
	
	public String loadRouteByCaseId (String caseId) {
		String sql = "SELECT ROUTE FROM ROUTELOG WHERE ID = ?";
		String route = null;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, caseId);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
		    	  route = rs.getString("ROUTE");
			}
			ps.close();			
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " in loading a routelog.");
			e.printStackTrace();
		}
		return route;
	}
	
	public int loadTransportStatusByCaseId (String caseId) {
		String sql = "SELECT STATUS FROM ROUTELOG WHERE ID = ?";
		int status = 70;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, caseId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
		    	  status = rs.getInt("STATUS");
			}
			ps.close();
			
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " in loading a routelog status.");
			e.printStackTrace();
		}
		return status;
	}
	
	public void setTruckForTransportationOrder(String id, String mobOpId) {
		try {
			String sql = "UPDATE ROUTELOG SET TRUCK = ? WHERE ID = ?"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, mobOpId);
			ps.setString(2, id);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " in assign an asset for a routelog.");
			e.printStackTrace();
		}
	}
	
	public void setTrainForTransportationOrder(String id, String mobOpId) {
		try {
			String sql = "UPDATE ROUTELOG SET TRAIN = ? WHERE ID = ?"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, mobOpId);
			ps.setString(2, id);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " in assign an asset for a routelog.");
			e.printStackTrace();
		}
	}
	
	public void setBargeForTransportationOrder(String id, String mobOpId) {
		try {
			String sql = "UPDATE ROUTELOG SET BARGE = ? WHERE ID = ?"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, mobOpId);
			ps.setString(2, id);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " in assign an asset for a routelog.");
			e.printStackTrace();
		}
	}
	
	public void setFlightForTransportationOrder(String id, String flight) {
		try {
			String sql = "UPDATE ROUTELOG SET FLIGHT = ? WHERE ID = ?"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, flight);
			ps.setString(2, id);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " in assign a flight for a routelog.");
			e.printStackTrace();
		}
	}
	
	public void updateTransportStatus(String caseId, int status) {
		try {
			String sql = "UPDATE ROUTELOG SET STATUS = ? WHERE ID = ?"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, status);
			ps.setString(2, caseId);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " in upadting the routelog status.");
			e.printStackTrace();
		}
	}
	
	
	public void updateTransportDescription(String caseId, String desc) {
		try {
			String sql = "UPDATE ROUTELOG  SET DESCRIPTION = ?  WHERE ID = ?"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, desc);
			ps.setString(2, caseId);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " in updating the routelog description.");
			e.printStackTrace();
		}
	}
	
	public String loadDescriptonByCaseId (String caseId) {
		String sql = "SELECT DESCRIPTION FROM ROUTELOG WHERE ID = ?";
		String desc = "";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, caseId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				desc = rs.getString("DESCRIPTION");
			}
			ps.close();			
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " in laoding a routelog description.");
			e.printStackTrace();
		}
		return desc;
	}
	/*
	public void loadTransportForCaseId(String caseId) {
		String sqlShowAll = "SELECT * FROM ROUTELOG WHERE ID = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sqlShowAll);
			ps.setString(1, caseId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
		    	  System.out.println("id: " + rs.getString("ID"));
		    	  System.out.println("route: " + rs.getString("ROUTE"));
		    	  System.out.println("time1: " + rs.getString("TIME1"));
		    	  System.out.println("time2: " + rs.getString("TIME2"));
		    	  System.out.println("status: " + rs.getString("STATUS"));
		    	  System.out.println();	 
			}
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	*/
	public String estimateNewCaseId() {
		String sql = "SELECT ID FROM ROUTELOG";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			//check whether also string works or not?
			List<Integer> ids = new ArrayList<Integer>();
			while(rs.next()) {
		    	  ids.add(Integer.parseInt(rs.getString("ID")));
			}
			ps.close();
			Collections.sort(ids);
			if(ids.size() > 0)
				return String.valueOf((ids.get(ids.size()-1))+4);
			else return "6";
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage() + " in determining a new case ID.");
		}
		return "0";
	}
	
	public List<String> loadCasesInSpecificTransportStatus(int status) {
		List<String> ids = new ArrayList<String>();
		String sql = "SELECT ID FROM ROUTELOG WHERE STATUS = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, status);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
		    	  ids.add(rs.getString("ID"));
			}
			ps.close();
			
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " in loading a routelog in a given status.");
			e.printStackTrace();
		}
		return ids;
	}
	
	public Map<String, String> loadRoutesInSpecificTransportStatus (int status) {
		String sql = "SELECT ID, ROUTE FROM ROUTELOG WHERE STATUS = ?";
		Map<String, String> routes = new HashMap<String, String>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, status);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
		    	  routes.put(rs.getString("ID"), rs.getString("ROUTE"));
			}
			ps.close();
			
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " in loading routes in a given status.");
			e.printStackTrace();
		}
		return routes;
	}
	
	public List<String> loadAllRoutes() {
		String sql = "SELECT ROUTE FROM ROUTELOG";
		List<String> routes = new ArrayList<String>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
		    	  routes.add(rs.getString("ROUTE"));
			}
			ps.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " in loading all routes.");
			e.printStackTrace();
		}
		return routes;
	}
	
	public List<String> loadCaseIdByTruck (String mobOpId) {
		String sql = "SELECT ID FROM ROUTELOG WHERE TRUCK = ?";
		List<String> cases = new ArrayList<String>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, mobOpId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				cases.add(rs.getString("ID"));
			}
			ps.close();			
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " in loading asset ids for a specific case id.");
			e.printStackTrace();
		}
		return cases;
	}
	
	public List<String> loadCaseIdByFlight (String flight) {
		String sql = "SELECT ID FROM ROUTELOG WHERE FLIGHT = ?";
		List<String> cases = new ArrayList<String>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, flight);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				cases.add(rs.getString("ID"));
			}
			ps.close();			
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " in loading flight for a specific case id.");
			e.printStackTrace();
		}
		return cases;
	}
	
	public List<String> loadCaseIdByTrain (String train) {
		String sql = "SELECT ID FROM ROUTELOG WHERE TRAIN = ?";
		List<String> cases = new ArrayList<String>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, train);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				cases.add(rs.getString("ID"));
			}
			ps.close();			
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " in loading flight for a specific case id.");
			e.printStackTrace();
		}
		return cases;
	}
	
	public List<String> loadCaseIdByBarge (String barge) {
		String sql = "SELECT ID FROM ROUTELOG WHERE BARGE = ?";
		List<String> cases = new ArrayList<String>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, barge);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				cases.add(rs.getString("ID"));
			}
			ps.close();			
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " in loading barge for a specific case id.");
			e.printStackTrace();
		}
		return cases;
	}
	
	public String loadTruckIdByCaseId (String caseID) {
		String sql = "SELECT TRUCK FROM ROUTELOG WHERE ID = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, caseID);
			ResultSet rs = ps.executeQuery();
			String src = "";
			if(rs.next()) {
		    	  src = rs.getString("TRUCK");
			}
			ps.close();
			return src;
			
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " in loading truck by case id.");
			e.printStackTrace();
		}
		return "";
	}
	
	public String loadFlightIdByCaseId (String caseID) {
		String sql = "SELECT FLIGHT FROM ROUTELOG WHERE ID = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, caseID);
			ResultSet rs = ps.executeQuery();
			String src = "";
			if(rs.next()) {
		    	  src = rs.getString("FLIGHT");
			}
			ps.close();
			return src;
			
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " in loading flight by case id.");
			e.printStackTrace();
		}
		return "";
	}
	
	public String loadTrainIdByCaseId (String caseID) {
		String sql = "SELECT TRAIN FROM ROUTELOG WHERE ID = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, caseID);
			ResultSet rs = ps.executeQuery();
			String src = "";
			if(rs.next()) {
		    	  src = rs.getString("TRAIN");
			}
			ps.close();
			return src;
			
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " in loading train by case id.");
			e.printStackTrace();
		}
		return "";
	}
	
	public String loadBargeIdByCaseId (String caseID) {
		String sql = "SELECT BARGE FROM ROUTELOG WHERE ID = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, caseID);
			ResultSet rs = ps.executeQuery();
			String src = "";
			if(rs.next()) {
		    	  src = rs.getString("BARGE");
			}
			ps.close();
			return src;
			
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " in loading asset by case id.");
			e.printStackTrace();
		}
		return "";
	}
	
	//TODO
	/*======================VEHICLE TRACE================================================*/
	
	public void addVehicleTraceRecord(String mobOpId, String lat, String lng, String alt, String timestamp, int status, String eta) {
		try {
			String sql = "INSERT INTO VEHICLETRACE(MOBOPID, LATITUDE, LONGITUDE, ALTITUDE, TIMESTAMP, STATUS, ETA) VALUES (?,?,?,?,?,?,?)"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, mobOpId);
			ps.setString(2, lat);
			ps.setString(3, lng);
			ps.setString(4, alt);
			ps.setString(5, timestamp);
			ps.setInt(6, status);
			ps.setString(7, eta);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			//e.printStackTrace();
		}
	}
	
	public void updateAssetStatus(String mobOpId, int status){
		try {
			String sql = "UPDATE VEHICLETRACE SET STATUS = ? WHERE MOBOPID = ?"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, status);
			ps.setString(2, mobOpId);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removePreviousVehicleRecord(String mopOpId) {
		try {
			String sql = "DELETE FROM VEHICLETRACE WHERE MOBOPID = ?"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, mopOpId);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean hasPreviousVehicleRecord(String mobOpId) {
		try {
			String sql = "SELECT * FROM VEHICLETRACE WHERE MOBOPID = ?"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, mobOpId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) { ps.close(); return true; }
			else { ps.close(); return false; }
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	public int loadAssetStatusById (String mobOpId) {
		String sql = "SELECT STATUS FROM VEHICLETRACE WHERE MOBOPID = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, mobOpId);
			ResultSet rs = ps.executeQuery();
			int status = 0;
			if(rs.next()) {
		    	  status = rs.getInt("STATUS");
			}
			ps.close();
			return status;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return 568;
		}
	}
	
	public IMPoint loadPointByAssetId (String mobOpId) {
		String sql = "SELECT LATITUDE, LONGITUDE FROM VEHICLETRACE WHERE MOBOPID = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, mobOpId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
			    String longitude = rs.getString("LONGITUDE");
			    String latitude = rs.getString("LATITUDE");
			    if(longitude == null)  longitude = "0.000";
			    if(latitude == null) latitude = "0.0000";
		    	return new IMPoint(Double.parseDouble(longitude), Double.parseDouble(latitude));
			}
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	public List<Asset> loadAllRunningAssets () {
		List<Asset> output = new ArrayList<Asset>();
		String sql = "SELECT * FROM VEHICLETRACE";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
			    String longitude = rs.getString("LONGITUDE");
			    String latitude = rs.getString("LATITUDE");
			    if(longitude == null)  longitude = "0.000";
			    if(latitude == null) latitude = "0.0000";
			    IMPoint point = new IMPoint(Double.parseDouble(longitude), Double.parseDouble(latitude));
			    String source = rs.getString("MOBOPID");
			    int status = rs.getInt("STATUS");
			    String eta = rs.getString("ETA");
			    if(eta == null)	eta = "";
			    	output.add(new Asset(point, status, source, eta));
			}
			ps.close();
			return output;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Asset loadAssetById (String mobOpId) {
		String sql = "SELECT * FROM VEHICLETRACE WHERE MOBOPID = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, mobOpId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
			    String longitude = rs.getString("LONGITUDE");
			    String latitude = rs.getString("LATITUDE");
			    if(longitude == null)  longitude = "0.000";
			    if(latitude == null) latitude = "0.0000";
			    IMPoint point = new IMPoint(Double.parseDouble(longitude), Double.parseDouble(latitude));
			    int status = rs.getInt("STATUS");
			    String eta = rs.getString("ETA");
			    if(eta == null)	eta = "";
			    return new Asset(point, status, mobOpId, eta);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Asset> loadAllFlights () {
		List<Asset> output = new ArrayList<Asset>();
		String sql = "SELECT * FROM VEHICLETRACE WHERE STATUS = ? OR STATUS = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, StatusCode.FLIGHT_BUSY.getValue());
			ps.setInt(2, StatusCode.FLIGHT_DIVERSION.getValue());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
			    String longitude = rs.getString("LONGITUDE");
			    String latitude = rs.getString("LATITUDE");
			    if(longitude == null)  longitude = "0.000";
			    if(latitude == null) latitude = "0.0000";
			    IMPoint point = new IMPoint(Double.parseDouble(longitude), Double.parseDouble(latitude));
			    String source = rs.getString("MOBOPID");
			    int status = rs.getInt("STATUS");
			    String eta = rs.getString("ETA");
			    if(eta == null)	eta = "";
			    output.add(new Asset(point, status, source, eta));
			}
			ps.close();
			return output;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Asset> loadAllRunningBarges () {
		List<Asset> output = new ArrayList<Asset>();
		String sql = "SELECT * FROM VEHICLETRACE WHERE STATUS = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, StatusCode.BARGE_BUSY.getValue());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
			    String longitude = rs.getString("LONGITUDE");
			    String latitude = rs.getString("LATITUDE");
			    if(longitude == null)  longitude = "0.000";
			    if(latitude == null) latitude = "0.0000";
			    IMPoint point = new IMPoint(Double.parseDouble(longitude), Double.parseDouble(latitude));
			    String source = rs.getString("MOBOPID");
			    int status = rs.getInt("STATUS");
			    String eta = rs.getString("ETA");
			    if(eta == null)	eta = "";
			    output.add(new Asset(point, status, source, eta));
			}
			ps.close();
			return output;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Asset> loadAllFreeBarges () {
		List<Asset> output = new ArrayList<Asset>();
		String sql = "SELECT * FROM VEHICLETRACE WHERE STATUS = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, StatusCode.BARGE_FREE.getValue());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
			    String longitude = rs.getString("LONGITUDE");
			    String latitude = rs.getString("LATITUDE");
			    if(longitude == null)  longitude = "0.000";
			    if(latitude == null) latitude = "0.0000";
			    IMPoint point = new IMPoint(Double.parseDouble(longitude), Double.parseDouble(latitude));
			    String source = rs.getString("MOBOPID");
			    int status = rs.getInt("STATUS");
			    String eta = rs.getString("ETA");
			    if(eta == null)	eta = "";
			    output.add(new Asset(point, status, source, eta));
			}
			ps.close();
			return output;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Asset> loadAllAssetsByStatus (int status) {
		List<Asset> output = new ArrayList<Asset>();
		String sql = "SELECT * FROM VEHICLETRACE WHERE STATUS = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, status);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
			    String longitude = rs.getString("LONGITUDE");
			    String latitude = rs.getString("LATITUDE");
			    if(longitude == null)  longitude = "0.000";
			    if(latitude == null) latitude = "0.0000";
			    IMPoint point = new IMPoint(Double.parseDouble(longitude), Double.parseDouble(latitude));
			    String source = rs.getString("MOBOPID");
			    String eta = rs.getString("ETA");
			    if(eta == null)	eta = "";
			    output.add(new Asset(point, status, source, eta));
			}
			ps.close();
			return output;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//TODO
	/*======================Unexpected Event========================================================*/
	public void addUnexpectedEvent(String source, String lat, String lng, int delay, String title) {
		try {
			String sql = "INSERT INTO UNEXPECTED(SOURCE, LATITUDE, LONGITUDE, DELAY, DESCRIPTION) VALUES (?,?,?,?,?)"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, source);
			ps.setString(2, lat);
			ps.setString(3, lng);
			ps.setInt(4, delay);
			ps.setString(5, title);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void removePreviousUnexpected(String source) {
		try {
			String sql = "DELETE FROM UNEXPECTED WHERE SOURCE = ?"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, source);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean hasPreviousUnexpectedRecord(String source) {
		try {
			String sql = "SELECT * FROM UNEXPECTED WHERE SOURCE = ?"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, source);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) { ps.close(); return true; }
			else { ps.close(); return false; }
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public IMPoint loadUnexpectedPointBySource (String src) {
		String sql = "SELECT LATITUDE, LONGITUDE FROM UNEXPECTED WHERE SOURCE = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, src);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
			    String longitude = rs.getString("LONGITUDE");
			    String latitude = rs.getString("LATITUDE");
			    if(longitude == null)  longitude = "0.000";
			    if(latitude == null) latitude = "0.0000";
			    return new IMPoint(Double.parseDouble(longitude), Double.parseDouble(latitude));
			}
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	public String loadUnexpectedTitleByAssetId (String mobOpId) {
		String sql = "SELECT DESCRIPTION FROM UNEXPECTED WHERE SOURCE = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, mobOpId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
		    	return rs.getString("DESCRIPTION");
			}
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	
	/*====================== Event MESSAGE ========================================================*/
	public void addMessageEvent(String caseid, String source, String message, String type, String lat, String lng, String iconUrl) {
		try {
			String sql = "INSERT INTO MESSAGES (CASEID, SOURCE, MESSAGE, TYPE, LATITUDE, LONGITUDE, ICONURL) VALUES (?,?,?,?,?,?,?)"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, caseid);
			ps.setString(2, source);
			ps.setString(3, message);
			ps.setString(4, type);
			ps.setString(5, lat);
			ps.setString(6, lng);
			ps.setString(7, iconUrl);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<MessageObject> loadEventMessageByCaseId(String caseId) {
		List<MessageObject> messages = new ArrayList<MessageObject>();
		String sql = "SELECT * FROM MESSAGES WHERE CASEID = ?";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, caseId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String source = rs.getString("SOURCE");
				String msg = rs.getString("MESSAGE");
				String type = rs.getString("TYPE");
				String lat = rs.getString("LATITUDE");
				String lng = rs.getString("LONGITUDE");
				String iconUrl = rs.getString("ICONURL");
				messages.add(new MessageObject(caseId, source, msg, type, iconUrl, lat, lng));
			}
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messages;
	}
	
	public List<MessageObject> loadEventMessageByCaseIdAndType(String caseId, String type) {
		List<MessageObject> messages = new ArrayList<MessageObject>();
		String sql = "SELECT * FROM MESSAGES WHERE CASEID = ? AND TYPE = ?";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, caseId);
			ps.setString(2, type);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String source = rs.getString("SOURCE");
				String msg = rs.getString("MESSAGE");
				String lat = rs.getString("LATITUDE");
				String lng = rs.getString("LONGITUDE");
				String iconUrl = rs.getString("ICONURL");
				messages.add(new MessageObject(caseId, source, msg, type, iconUrl, lat, lng));
			}
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messages;
	}
	
	public boolean hasPreviousMessageTypeForCase(String caseId, String messageType) {
		try {
			String sql = "SELECT * FROM MESSAGES WHERE CASEID = ? AND TYPE = ?"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, caseId);
			ps.setString(2, messageType);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) { ps.close(); return true; }
			else { ps.close(); return false; }
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void removePreviousMessageTypeForCase(String caseId, String messageType) {
		try {
			String sql = "DELETE FROM MESSAGES WHERE CASEID = ? AND TYPE = ?"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, caseId);
			ps.setString(2, messageType);
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*======================TRAFFIC Event========================================================*/

	public void addTrafficEvent(String providerID, String provider, String lng, String lat,String timestamp, 
			String type, int delay, int length, String cause, String desc, String roadAffected, String identifier, int status, int magnitude) {
		try {
			String sql = "INSERT INTO TRAFFIC(PROVIDER_ID, PROVIDER, LONGITUDE, LATITUDE, "
					+ "TIMESTAMP, TYPE, DELAY,  LENGTH, CAUSE, DESCRIPTION,"
					+ "ROADSAFFECTED, IDENTIFIER, MAGNITUDE, STATUS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, providerID);
			ps.setString(2, provider);
			ps.setString(3, lng);
			ps.setString(4, lat);
			ps.setString(5, timestamp);
			ps.setString(6, type);
			ps.setInt(7, delay);
			ps.setInt(8, length);
			ps.setString(9, cause);
			ps.setString(10, desc);
			ps.setString(11, roadAffected);
			ps.setString(12, identifier);
			ps.setInt(13, magnitude);
			ps.setInt(14, status);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<RoadTrafficEvent> loadAllTraffic() {
		List<RoadTrafficEvent> traffics = new ArrayList<RoadTrafficEvent>();
		String sql = "SELECT * FROM TRAFFIC";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				RoadTrafficEvent rt = new RoadTrafficEvent();
				RoadTrafficEvent.AreaCenter cente = new RoadTrafficEvent.AreaCenter();
				cente.setLatitude(Double.parseDouble(rs.getString("LATITUDE")));
				cente.setLongitude(Double.parseDouble(rs.getString("LONGITUDE")));
				rt.setAreaCenter(cente);
				rt.setCause(rs.getString("CAUSE"));
				rt.setDelay(rs.getInt("DELAY"));
				rt.setDescription(rs.getString("DESCRIPTION"));
				rt.setIdAtProvider(rs.getString("PROVIDER_ID"));
				rt.setIdentifier(rs.getString("IDENTIFIER"));
				rt.setLength(rs.getInt("LENGTH"));
				rt.setMagnitude(rs.getInt("MAGNITUDE"));
				rt.setProvider(rs.getString("PROVIDER"));
				rt.setRoadsAffected(rs.getString("ROADSAFFECTED"));
				rt.setTimestamp(rs.getString("TIMESTAMP"));
				rt.setType(rs.getString("TYPE"));
			    traffics.add(rt);
			}
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return traffics;
	}
	
	
	
	
	/*===================NO CASE ROUTES ============================*/
	public void addNoCaseRoute(String route) {
		try {
			String sql = "INSERT INTO NO_CASE_ROUTE(ROUTE) VALUES (?)"; 
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, route);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void removeNoCaseRoute(String route) {
		try {
			String sql = "DELETE FROM NO_CASE_ROUTE WHERE ROUTE = ?"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, route);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> loadAllNoCaseRoutes() {
		List<String> output = new ArrayList<String>();
		String sql = "SELECT * FROM NO_CASE_ROUTE";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
					output.add(rs.getString("ROUTE"));
			}
			ps.close();
			return output;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/*===================UNPLANNED TRANPORT REQUESTS ============================*/
	public boolean addTransportRequestWithoutCaseID(TransportRequest tr) {
		try {
			String sql = "INSERT INTO REQUESTS(ID, NAME1, STREET1, NUMBER1, ZIPCODE1, CITY1, COUNTRY1, TIME1, NAME2, STREET2, NUMBER2, ZIPCODE2, CITY2, COUNTRY2, TIME2) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, tr.getId());
			ps.setString(2, tr.getSource().getAddress().getName());
			ps.setString(3, tr.getSource().getAddress().getStreet());
			ps.setString(4, tr.getSource().getAddress().getNumber());
			ps.setString(5, tr.getSource().getAddress().getZipcode());
			ps.setString(6, tr.getSource().getAddress().getCity());
			ps.setString(7, tr.getSource().getAddress().getCountry());
			ps.setString(8, tr.getSource().getDate().toString());
			ps.setString(9, tr.getDest().getAddress().getName());
			ps.setString(10, tr.getDest().getAddress().getStreet());
			ps.setString(11, tr.getDest().getAddress().getNumber());
			ps.setString(12, tr.getDest().getAddress().getZipcode());
			ps.setString(13, tr.getDest().getAddress().getCity());
			ps.setString(14, tr.getDest().getAddress().getCountry());
			ps.setString(15, tr.getDest().getDate().toString());
			ps.executeUpdate();
			ps.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean addTransportRequestWithCaseID(TransportRequest tr) {
		try {
			String sql = "INSERT INTO REQUESTS(ID, NAME1, STREET1, NUMBER1, ZIPCODE1, CITY1, COUNTRY1, TIME1, NAME2, STREET2, NUMBER2, ZIPCODE2, CITY2, COUNTRY2, TIME2, CASEID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, tr.getId());
			ps.setString(2, tr.getSource().getAddress().getName());
			ps.setString(3, tr.getSource().getAddress().getStreet());
			ps.setString(4, tr.getSource().getAddress().getNumber());
			ps.setString(5, tr.getSource().getAddress().getZipcode());
			ps.setString(6, tr.getSource().getAddress().getCity());
			ps.setString(7, tr.getSource().getAddress().getCountry());
			ps.setString(8, tr.getSource().getDate().toString());
			ps.setString(9, tr.getDest().getAddress().getName());
			ps.setString(10, tr.getDest().getAddress().getStreet());
			ps.setString(11, tr.getDest().getAddress().getNumber());
			ps.setString(12, tr.getDest().getAddress().getZipcode());
			ps.setString(13, tr.getDest().getAddress().getCity());
			ps.setString(14, tr.getDest().getAddress().getCountry());
			ps.setString(15, tr.getDest().getDate().toString());
			ps.setString(16, tr.getCase_id());
			ps.executeUpdate();
			ps.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean removeTransportRequest(String id) {
		try {
			String sql = "DELETE FROM REQUESTS WHERE ID = ?"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.executeUpdate();
			ps.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public List<TransportRequest> loadAllTransportRequests() {
		List<TransportRequest> output = new ArrayList<TransportRequest>();
		String sql = "SELECT * FROM REQUESTS";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String id = rs.getString("ID");
				String caseId = rs.getString("CASEID");
				Address srcAddress = new Address(rs.getString("NAME1"), rs.getString("STREET1"), rs.getString("NUMBER1"), rs.getString("ZIPCODE1"), rs.getString("CITY1"), rs.getString("COUNTRY1"));
				Address destAddress = new Address(rs.getString("NAME2"), rs.getString("STREET2"), rs.getString("NUMBER2"), rs.getString("ZIPCODE2"), rs.getString("CITY2"), rs.getString("COUNTRY2"));
				Date date1 = new Date();
				Date date2 = new Date();
				
				SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd hh:mm:ss zzz yyyy");
				String dateInString1 = rs.getString("TIME1");
				String dateInString2 = rs.getString("TIME2");
				try {
					date1 = formatter.parse(dateInString1);
					date2 = formatter.parse(dateInString2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				AddressTime source = new AddressTime(srcAddress, date1);
				AddressTime dest = new AddressTime(destAddress, date2);
				output.add(new TransportRequest(id, caseId, source, dest));
			}
			ps.close();
			return output;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
		
	public List<TransportRequest> loadAllTransportRequestsWithoutCaseId() {
		List<TransportRequest> output = new ArrayList<TransportRequest>();
		String sql = "SELECT * FROM REQUESTS WHERE CASEID IS NULL OR CASEID = ''";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String id = rs.getString("ID");
				String caseId = rs.getString("CASEID");
				Address srcAddress = new Address(rs.getString("NAME1"), rs.getString("STREET1"), rs.getString("NUMBER1"), rs.getString("ZIPCODE1"), rs.getString("CITY1"), rs.getString("COUNTRY1"));
				Address destAddress = new Address(rs.getString("NAME2"), rs.getString("STREET2"), rs.getString("NUMBER2"), rs.getString("ZIPCODE2"), rs.getString("CITY2"), rs.getString("COUNTRY2"));
				Date date1 = new Date();
				Date date2 = new Date();
				
				SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy", Locale.US);
				String dateInString1 = rs.getString("TIME1");
				String dateInString2 = rs.getString("TIME2");
				try {
					date1 = formatter.parse(dateInString1);
					date2 = formatter.parse(dateInString2);
				} catch (Exception e) {
				e.printStackTrace();
				}
				AddressTime source = new AddressTime(srcAddress, date1);
				AddressTime dest = new AddressTime(destAddress, date2);
				output.add(new TransportRequest(id, caseId, source, dest));
			}
			ps.close();
			return output;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public TransportRequest loadTransportRequestWithID(String transportRequestId) {
		TransportRequest output = new TransportRequest();
		String sql = "SELECT * FROM REQUESTS WHERE ID = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, transportRequestId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				String id = rs.getString("ID");
				String caseId = rs.getString("CASEID");
				Address srcAddress = new Address(rs.getString("NAME1"), rs.getString("STREET1"), rs.getString("NUMBER1"), rs.getString("ZIPCODE1"), rs.getString("CITY1"), rs.getString("COUNTRY1"));
				Address destAddress = new Address(rs.getString("NAME2"), rs.getString("STREET2"), rs.getString("NUMBER2"), rs.getString("ZIPCODE2"), rs.getString("CITY2"), rs.getString("COUNTRY2"));
				Date date1 = new Date();
				Date date2 = new Date();
				
				SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd hh:mm:ss zzz yyyy");
				String dateInString1 = rs.getString("TIME1");
				String dateInString2 = rs.getString("TIME2");
				try {
					date1 = formatter.parse(dateInString1);
					date2 = formatter.parse(dateInString2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				AddressTime source = new AddressTime(srcAddress, date1);
				AddressTime dest = new AddressTime(destAddress, date2);
				output = new TransportRequest(id, caseId, source, dest);
			}
			ps.close();
			return output;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<TransportRequest> loadTransportRequestswithCaseId(String caseId) {
		List<TransportRequest> output = new ArrayList<TransportRequest>();
		String sql = "SELECT * FROM REQUESTS WHERE CASEID LIKE ?";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
				ps.setString(1, "%" + caseId + "%");
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
				String id = rs.getString("ID");
				Address srcAddress = new Address(rs.getString("NAME1"), rs.getString("STREET1"), rs.getString("NUMBER1"), rs.getString("ZIPCODE1"), rs.getString("CITY1"), rs.getString("COUNTRY1"));
				Address destAddress = new Address(rs.getString("NAME2"), rs.getString("STREET2"), rs.getString("NUMBER2"), rs.getString("ZIPCODE2"), rs.getString("CITY2"), rs.getString("COUNTRY2"));
				Date date1 = new Date();
				Date date2 = new Date();
				
				SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd hh:mm:ss zzz yyyy");
				String dateInString1 = rs.getString("TIME1");
				String dateInString2 = rs.getString("TIME2");
				try {
					date1 = formatter.parse(dateInString1);
					date2 = formatter.parse(dateInString2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				AddressTime source = new AddressTime(srcAddress, date1);
				AddressTime dest = new AddressTime(destAddress, date2);
				output.add(new TransportRequest(id, caseId, source, dest));
			}
			ps.close();
			return output;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void updateCaseIdForRequest(String requestId, String caseId){
		try {
			String sql = "UPDATE REQUESTS SET CASEID = ? WHERE ID = ?"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, caseId);
			ps.setString(2, requestId);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	/*===================SUSPENDED CASES ============================*/
	public void addSuspendedCase(String caseId, String tasklistJson) {
		try {
			String sql = "INSERT INTO SUSPENDED_CASES (ID, TASKOBJECT) VALUES (?,?)"; 
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, caseId);
			ps.setString(2, tasklistJson);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void removeSuspendedCase(String caseId) {
		try {
			String sql = "DELETE FROM SUSPENDED_CASES WHERE ID = ?"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, caseId);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String loadTaskObjectJsonForSuspendedCase(String caseId) {
		String output = "";
		String sql = "SELECT TASKOBJECT FROM SUSPENDED_CASES WHERE ID = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, caseId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				output = rs.getString("TASKOBJECT");
			}
			ps.close();
			return output;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> loadAllSuspendedCaseIDs() {
		List<String> output = new ArrayList<String>();
		String sql = "SELECT ID FROM SUSPENDED_CASES";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
					output.add(rs.getString("ID"));
			}
			ps.close();
			return output;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	//TODO
	/* =======================CONFIG DATABASE===========================================*/
	private void createTableRoutelog() {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			try { 
				stmt.executeUpdate("DROP TABLE ROUTELOG");
			} catch(java.sql.SQLException ex) {System.out.println(ex.getMessage());}

			String sql = "CREATE TABLE ROUTELOG (ID TEXT PRIMARY KEY NOT NULL,ROUTE TEXT, TIME1 TEXT, TIME2 TEXT,STATUS INT, TRUCK TEXT, TRAIN TEXT,"
					+ " BARGE TEXT, FLIGHT TEXT, DESCRIPTION TEXT)"; 
			stmt.executeUpdate(sql);
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private void createTableVehicleTrance() {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			try {
				stmt.executeUpdate("DROP TABLE VEHICLETRACE");
			} catch(java.sql.SQLException ex) {System.out.println(ex.getMessage());}

			String sql = "CREATE TABLE VEHICLETRACE(MOBOPID TEXT PRIMARY KEY NOT NULL, LATITUDE TEXT, LONGITUDE TEXT, ALTITUDE TEXT, TIMESTAMP TEXT, STATUS INT, MODE INT, ETA TEXT)";
			stmt.executeUpdate(sql);
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createTableUnexpected() {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			try {
				stmt.executeUpdate("DROP TABLE UNEXPECTED");
			} catch(java.sql.SQLException ex) {System.out.println(ex.getMessage());}

			String sql = "CREATE TABLE UNEXPECTED (SOURCE TEXT PRIMARY KEY NOT NULL,LATITUDE TEXT, LONGITUDE TEXT, DELAY INT, DESCRIPTION TEXT);"; 
			stmt.executeUpdate(sql);
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void createTableMessages() {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			try {
			stmt.executeUpdate("DROP TABLE MESSAGES");
			} catch(java.sql.SQLException ex) {System.out.println(ex.getMessage());}
			
			String sql = "CREATE TABLE MESSAGES (CASEID TEXT, SOURCE TEXT, MESSAGE TEXT, TYPE TEXT, LATITUDE TEXT, LONGITUDE TEXT, ICONURL TEXT);"; 
			stmt.executeUpdate(sql);
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//Messages
	private void createTableTrafficPoints() {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			try {
				stmt.executeUpdate("DROP TABLE TRAFFIC");
			} catch(java.sql.SQLException ex) {System.out.println(ex.getMessage());}
			
			String sql = "CREATE TABLE TRAFFIC (PROVIDER_ID TEXT, PROVIDER TEXT, LONGITUDE TEXT, LATITUDE TEXT, "
					+ "TIMESTAMP TEXT, TYPE TEXT, DELAY INT,  LENGTH INT, CAUSE TEXT, DESCRIPTION TEXT,"
					+ "ROADSAFFECTED TEXT, IDENTIFIER TEXT, MAGNITUDE INT, STATUS INT);"; 
			stmt.executeUpdate(sql);
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void createTableNoCaseRoutes() {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			try {
				stmt.executeUpdate("DROP TABLE NO_CASE_ROUTE");
			} catch(java.sql.SQLException ex) {System.out.println(ex.getMessage());}
			
			String sql = "CREATE TABLE NO_CASE_ROUTE (ROUTE TEXT);"; 
			stmt.executeUpdate(sql);
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void createTableUnplannedTransportRequest() {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			try {
			stmt.executeUpdate("DROP TABLE REQUESTS");
			} catch(java.sql.SQLException ex) {System.out.println(ex.getMessage());}
			
			String sql = "CREATE TABLE REQUESTS (ID TEXT, NAME1 TEXT, STREET1 TEXT, NUMBER1 TEXT, ZIPCODE1 TEXT, CITY1 TEXT, COUNTRY1 TEXT, TIME1 TEXT,"
											  		   + "NAME2 TEXT, STREET2 TEXT, NUMBER2 TEXT, ZIPCODE2 TEXT, CITY2 TEXT, COUNTRY2 TEXT, TIME2 TEXT, CASEID TEXT);"; 
			stmt.executeUpdate(sql);
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void createTableSuspendedCases() {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			try {
				stmt.executeUpdate("DROP TABLE SUSPENDED_CASES");
			} catch(java.sql.SQLException ex) {System.out.println(ex.getMessage());}
			
			String sql = "CREATE TABLE SUSPENDED_CASES (ID TEXT PRIMARY KEY NOT NULL, TASKOBJECT TEXT);"; 
			stmt.executeUpdate(sql);
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void configDB() {
		createTableRoutelog();
		createTableVehicleTrance();
		createTableUnexpected();
		createTableTrafficPoints();
		createTableMessages();
		createTableNoCaseRoutes();
		createTableUnplannedTransportRequest();
		createTableSuspendedCases();
		System.out.println("SQLite Database tables have been configured");
	}
	
	public void fillInDataForPrototype() {
		Scanner in1;
		try {
			//For Windows Server
			/*
			in1 = new Scanner(new FileReader(Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/database/routes").concat("\\") + "route1.txt"));
			Scanner in2 = new Scanner(new FileReader(Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/database/routes").concat("\\") + "route2.txt"));
			Scanner in3 = new Scanner(new FileReader(Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/database/routes").concat("\\") + "route3.txt"));
			Scanner in4 = new Scanner(new FileReader(Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/database/routes").concat("\\") + "route4.txt"));
			Scanner in5 = new Scanner(new FileReader(Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/database/routes").concat("\\") + "route5.txt"));
			Scanner in6 = new Scanner(new FileReader(Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/database/routes").concat("\\") + "route6.txt"));
			Scanner in7 = new Scanner(new FileReader(Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/database/routes").concat("\\") + "route7.txt"));
			*/
			//Scanner in8 = new Scanner(new FileReader(Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/database/routes").concat("\\") + "FRA-AMS-ROAD.txt"));
			
			//For MAC Server
			in1 = new Scanner(new FileReader(Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/database/routes/route1.txt")));
			Scanner in2 = new Scanner(new FileReader(Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/database/routes/route2.txt")));
			Scanner in3 = new Scanner(new FileReader(Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/database/routes/route3.txt")));
			Scanner in4 = new Scanner(new FileReader(Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/database/routes/route4.txt")));
			//Scanner in5 = new Scanner(new FileReader(Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/database/routes/route5.txt")));
			//Scanner in6 = new Scanner(new FileReader(Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/database/routes/route6.txt")));
			//Scanner in7 = new Scanner(new FileReader(Sessions.getCurrent().getWebApp().getRealPath("/WEB-INF/database/routes/route7.txt")));

			ProcessInstanceFunctions ins = new ProcessInstanceFunctions();
			ArrayList<String> instanceIds = new ArrayList<String>();
			instanceIds= ins.getAllRunningProcessInstanceIDs();
			
			//addTransportOrder(instanceIds.get(0), in8.nextLine(), "Tue Apr 01 17:32:07 CEST 2014", "Thu Apr 17 17:32:08 CEST 2014", StatusCode.TRANSPORTATION_OK.getValue());
			//addTransportOrder(instanceIds.get(1), "", "Tue Apr 01 17:32:07 CEST 2014", "Thu Apr 17 17:32:08 CEST 2014", StatusCode.TRANSPORTATION_OK.getValue());
			//addTransportOrder(instanceIds.get(2), "", "Tue Apr 01 17:32:07 CEST 2014", "Thu Apr 17 17:32:08 CEST 2014", StatusCode.TRANSPORTATION_OK.getValue());
			addTransportOrder(instanceIds.get(0), in1.nextLine(), "Mon Apr 14 17:59:35 CEST 2014", "Fri Apr 25 17:59:37 CEST 2014", StatusCode.TRANSPORTATION_RUNNING.getValue());
			addTransportOrder(instanceIds.get(1), in2.nextLine(), "Thu Apr 10 14:58:30 CEST 2014", "Fri Apr 04 14:58:31 CEST 2014", StatusCode.TRANSPORTATION_RUNNING.getValue());
			addTransportOrder(instanceIds.get(2), in3.nextLine(), "Thu Apr 17 15:03:42 CEST 2014", "Wed Apr 16 15:03:43 CEST 2014", StatusCode.TRANSPORTATION_RUNNING.getValue());
			addTransportOrder(instanceIds.get(3), in4.nextLine(), "Mon Apr 14 15:14:31 CEST 2014", "Sat Apr 26 15:14:33 CEST 2014", StatusCode.TRANSPORTATION_RUNNING.getValue());
			//addTransportOrder(instanceIds.get(4), in5.nextLine(), "Thu Apr 10 15:18:55 CEST 2014", "Fri Apr 25 15:18:56 CEST 2014", StatusCode.TRANSPORTATION_RUNNING.getValue());
			//addTransportOrder(instanceIds.get(5), in6.nextLine(), "Wed Apr 30 15:20:14 CEST 2014", "Wed Apr 09 15:20:15 CEST 2014", StatusCode.TRANSPORTATION_RUNNING.getValue());
			//addTransportOrder(instanceIds.get(6), in7.nextLine(), "Tue Apr 01 17:32:07 CEST 2014", "Thu Apr 17 17:32:08 CEST 2014", StatusCode.TRANSPORTATION_RUNNING.getValue());
			
			addVehicleTraceRecord("124", "0.0", "0.0", "0.0", "", StatusCode.TRAIN_BUSY.getValue(), null);
			addVehicleTraceRecord("1849", "0.0", "0.0", "0.0", "", StatusCode.TRUCK_BUSY.getValue(), null);
			addVehicleTraceRecord("123", "0.0", "0.0", "0.0", "", StatusCode.FLIGHT_BUSY.getValue(), null);
			addVehicleTraceRecord(ActivitiEngineConfig.flightNumber, "0.0", "0.0", "0.0", "", StatusCode.FLIGHT_BUSY.getValue(), null);

			//setFlightForTransportationOrder(instanceIds.get(0), "123");
			setTruckForTransportationOrder(instanceIds.get(2), "124");
			setTruckForTransportationOrder(instanceIds.get(3), "1849");
			
			in1.close(); in2.close(); in3.close(); in4.close(); //in5.close(); in6.close(); in7.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}

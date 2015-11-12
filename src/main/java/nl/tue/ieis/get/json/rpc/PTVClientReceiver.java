package main.java.nl.tue.ieis.get.json.rpc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import main.java.de.ptv.intermodal.IMRouteResponse;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class PTVClientReceiver {
	
	private final String client = "default";
	private final String username = "IMResearch";
    private final String password = "IMResearch";
    private final String language = "en";
    private final boolean kickout = true;
    private String mainURL = "http://80.146.239.188/IMResearch/Utility/";
	private JsonRpcFunctions handler = new JsonRpcFunctions(mainURL);
    
	public PTVClientReceiver(boolean isLoginRequired) {
		if(isLoginRequired)
			login();
	}
	
	private String sendQuery(URL url, String id, String method, Object params, boolean setCookie) {
		String resp = "No Result";
		JSONObject request = new JSONObject();
		request.put("id", id);
		request.put("method", method);
		request.put("params", params);
		try {
			resp = handler.call(url, request.toJSONString(), setCookie);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return resp;
	}
	
	private String login() {
		String loginReq = "No Result";
		JSONObject params = new JSONObject();
		params.put("client", client);
		params.put("username", username);
		params.put("password", password);
		params.put("language", language);
		params.put("kickout", kickout);
		
		URL loginURL = null;
		try {
			loginURL = new URL(mainURL + "LoginHandler.ashx");
			loginReq = sendQuery(loginURL, "2", "Login", params, true);
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return loginReq;
	}
	
	public boolean isLogin() {
		boolean isValid = false;
		URL loginURL = null;
		try {
			loginURL = new URL(mainURL + "LoginHandler.ashx");
			if(sendQuery(loginURL, "1", "LoginIsValid", new JSONObject(), false).contains("true")) 
				isValid = true;
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return isValid;
	}
	
	public String runTestMethod() {
		//check whether login is valid or not?
		String testRequest = "No Result";
		try {
			URL interModalURL = new URL(mainURL + "IntermodalHandler.ashx");
			testRequest = sendQuery(interModalURL, "3", "getTestRequest", new JSONObject(), false);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return testRequest;
	}
	
	public Map<String,IMRouteResponse> retreivePlannedRoutes(String imRouteRequest, boolean defaultSearch){
		
		String plannedRouteJSONFormat = "No Result";
		Map<String, IMRouteResponse> returnValues = new HashMap<String, IMRouteResponse>();
		
		try {
			URL interModalURL = new URL(mainURL + "IntermodalHandler.ashx");
			//JSONObject params = new JSONObject();
			
			JsonObject inputParamJsonObject = new JsonObject();
			JsonParser parser = new JsonParser();
			if(defaultSearch) {
				imRouteRequest = StringUtils.substringAfterLast(imRouteRequest, "result");
				imRouteRequest = StringUtils.substring(imRouteRequest, 2);
				imRouteRequest = StringUtils.substringBeforeLast(imRouteRequest, "}}");
			}
			imRouteRequest = StringUtils.remove(imRouteRequest, ".0000000+02:00");
				
			inputParamJsonObject = (JsonObject) parser.parse(imRouteRequest);
			
			JsonObject params = new JsonObject();
			params.add("imRouteRequest", inputParamJsonObject);
			plannedRouteJSONFormat = sendQuery(interModalURL, "4", "CalculateRoute", params, false);
			ObjectMapper mapper = new ObjectMapper();
			try {
				String imRouteResponse = plannedRouteJSONFormat;
				imRouteResponse = StringUtils.substringAfterLast(imRouteResponse, "result");
				imRouteResponse = StringUtils.substring(imRouteResponse, 2);
				imRouteResponse = StringUtils.substringBeforeLast(imRouteResponse, "}}");

				IMRouteResponse responseObjects = mapper.readValue(imRouteResponse, IMRouteResponse.class);
				returnValues.put(imRouteResponse, responseObjects);
				
				//System.out.println("result: " + imRouteResponse);
				
			} catch (JsonParseException e) {
				e.getMessage();
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.getMessage();
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				e.getMessage();
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return returnValues;
	}
	
	public IMRouteResponse retreivePTVRoutesObject(String imRouteRequest, boolean defaultSearch){
		
		String plannedRouteJSONFormat = "No Result";
		IMRouteResponse returnValues = new IMRouteResponse();
		
		//System.out.println("1: ");
		//System.out.println(imRouteRequest);
		
		try {
			URL interModalURL = new URL(mainURL + "IntermodalHandler.ashx");
			//JSONObject params = new JSONObject();
			
			JsonObject inputParamJsonObject = new JsonObject();
			JsonParser parser = new JsonParser();
			if(defaultSearch) {
				imRouteRequest = StringUtils.substringAfterLast(imRouteRequest, "result");
				imRouteRequest = StringUtils.substring(imRouteRequest, 2);
				imRouteRequest = StringUtils.substringBeforeLast(imRouteRequest, "}}");
			}
			imRouteRequest = StringUtils.remove(imRouteRequest, ".0000000+02:00");
			
			//System.out.println("imReq:");
			//System.out.println(imRouteRequest);
			
			inputParamJsonObject = (JsonObject) parser.parse(imRouteRequest);
			
			JsonObject params = new JsonObject();
			params.add("imRouteRequest", inputParamJsonObject);
			plannedRouteJSONFormat = sendQuery(interModalURL, "4", "CalculateRoute", params, false);
			ObjectMapper mapper = new ObjectMapper();
			try {
				String imRouteResponse = plannedRouteJSONFormat;
				imRouteResponse = StringUtils.substringAfterLast(imRouteResponse, "result");
				imRouteResponse = StringUtils.substring(imRouteResponse, 2);
				imRouteResponse = StringUtils.substringBeforeLast(imRouteResponse, "}}");

				returnValues = mapper.readValue(imRouteResponse, IMRouteResponse.class);
				
			} catch (JsonParseException e) {
				e.getMessage();
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.getMessage();
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				e.getMessage();
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return returnValues;
	}
	
	public IMRouteResponse getLocalRouteResponse(String path) {
		IMRouteResponse returnValues = new IMRouteResponse();
		Gson gson = new Gson();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			returnValues = gson.fromJson(br, IMRouteResponse.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnValues;
	}
}

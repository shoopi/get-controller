package main.java.nl.tue.ieis.get.activiti.service;

import java.io.IOException;
import java.util.List;

import main.java.nl.tue.ieis.get.data.TransportOrderDataManagement;

import org.activiti.rest.common.api.SecuredResource;
import org.restlet.resource.Get;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SelectedRoute extends SecuredResource {
	
	@Get
	public String getProcessInfo() throws Exception {
		try {
			if(!authenticate()) 
				return null;
			
			ObjectNode responseJSON = new ObjectMapper().createObjectNode();
			createNoCaseRouteList(responseJSON);
			return responseJSON.toString();

		} catch (Exception e) {
			throw new Exception("Failed to process your request " +  e.getMessage());
		}
	}
	
	private void createNoCaseRouteList(ObjectNode responseJSON) throws JsonParseException, JsonMappingException, IOException {
		TransportOrderDataManagement dbMan = new TransportOrderDataManagement();
		List<String> routes = dbMan.loadAllNoCaseRoutes();
		ArrayNode routesJsonNode = new ObjectMapper().createArrayNode();
		responseJSON.put("availableRoutes", routesJsonNode);
		if(routes != null && routes.size() > 0) {
			for (String r : routes) {
				ObjectNode route = new ObjectMapper().createObjectNode();
				ObjectMapper mapper = new ObjectMapper();
				JsonNode actualObj = mapper.readValue(r, JsonNode.class);
				route.put("route", actualObj);
				routesJsonNode.add(route);
			}
		}
	}
}

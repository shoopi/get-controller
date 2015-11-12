package main.java.nl.tue.ieis.get.activiti.service;

import main.java.nl.tue.ieis.get.control.LoginController;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.identity.User;
import org.activiti.rest.common.api.SecuredResource;
import org.restlet.resource.Get;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ValidateToken extends SecuredResource{

	@Get
	public String getAuthenticationLevel() {
		String token = null;
		try {
			
			token = (String) getRequest().getAttributes().get("token");

			if(token == null) {
				throw new Exception("Token is not valid. You have entered token=" + token);
			}
			
			User user = LoginController.authorizeUser(token);
			ObjectNode responseJSON = new ObjectMapper().createObjectNode();
			responseJSON.put("OrgName", user.getId());
			responseJSON.put("OrgEmail", user.getEmail());
			loggedInUser = user.getId();
			return responseJSON.toString();

		} catch (Exception e) {
			throw new ActivitiException("Failed to retrieve the user from community passport manager", e);
		}
	}
}

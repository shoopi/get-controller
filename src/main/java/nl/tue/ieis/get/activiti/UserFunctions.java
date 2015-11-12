package main.java.nl.tue.ieis.get.activiti;

import main.java.nl.tue.ieis.get.activiti.config.ActivitiEngineConfig;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;

public class UserFunctions {

	private IdentityService identityService = ActivitiEngineConfig.processEngine.getIdentityService();
		
	public User loadUser(String username, String password) {
		if(identityService.checkPassword(username, password)) {
			return identityService.createUserQuery().userId(username).singleResult();
		}
		return null;
	}
	
	public boolean createUser(String username, String password, String firstname, String lastname, String email, String org_id, String role) {
		User user = identityService.newUser(username);
		user.setPassword(password);
		user.setFirstName(firstname);
		user.setLastName(lastname);
		user.setEmail(email);
		identityService.setUserInfo(username, "org_id", org_id);
		try {
			identityService.saveUser(user);
			identityService.createMembership(username, role);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public String findUserGroups(String userId) {
		try {
			String userGroup = identityService.createGroupQuery().groupMember(userId).singleResult().getId();
			return userGroup;
		} catch (Exception e) {
			return null;
		}
	}
}

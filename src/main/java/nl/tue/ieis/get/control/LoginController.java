package main.java.nl.tue.ieis.get.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.List;

import main.java.nl.portbase.*;
import main.java.nl.tue.ieis.get.activiti.UserFunctions;
import main.java.nl.tue.ieis.get.activiti.config.ActivitiEngineConfig;

import org.activiti.engine.identity.User;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Messagebox.ClickEvent;

import com.google.gson.Gson;


@SuppressWarnings("deprecation")
public class LoginController extends SelectorComposer<Component> {
	
	private static final long serialVersionUID = 8670883952725538263L;
	public static String loggedInUserToken;
	
	@Wire	private	Textbox 	usernameTextbox, passwordTextbox, firstnameTextbox, lastnameTextbox, usernameLoginTextbox, passwordLoginTextbox;
	@Wire 	private Spinner 	org_idSpinner;
	@Wire	private	Combobox 	rolesCombobox, scopeCombobox, scopeCombobox2;
	@Wire	private Comboitem 	adminComboItem, driverComboItem, getCPMComboItem, getCPMComboItem2, getControllerComboItem, getControllerComboItem2;
	@Wire	private Label 		loginMsgLabel, regMsgLabel;
	@Wire	private Button 		loginBtn, regBtn, addNewOrg;
	@Wire	private Window 		loginWin, regWin;
	
	private Window 	regOrgWin;
	private Button 	regOrgBtn;
	private	Textbox usernameTextbox2, passwordTextbox2, firstnameTextbox2, lastnameTextbox2, roleTextbox2, orgShortnameTextbox, orgFullnameTextbox, orgEmailTextbox;
	
	
	private static UserFunctions uf = new UserFunctions();
	private static Gson gson = new Gson();

	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);		
	}
	
	@Listen("onOK = #loginWin")
	public void loginWindowOk() {
		login();
	}
	
	@Listen("onClick = #loginBtn")
	public void loginButtonClick() {
		login();
	}
	
	@Listen("onOK = #regWin")
	public void regWindowOk() {
		registerUser();
	}
	
	@Listen("onClick = #regBtn")
	public void regButtonClick() {
		registerUser();
	}
	
	@Listen("onClick = #addNewOrg")
	public void regOrgButtonClick() {
		addNewOrganization();
	}
	
	private void login() {
		String username = usernameLoginTextbox.getValue();
		String password = passwordLoginTextbox.getValue();
		try{
			if(scopeCombobox2.getSelectedItem().equals(getCPMComboItem2)) {
				CloseableHttpClient httpClient = new DefaultHttpClient();
				HttpGet getRequest = new HttpGet("https://www.rd.npcs.portbase.com/get-core-cpm/api/get/signin/user");
	    		
	    		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
	    		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
	    		HttpClientContext localContext = HttpClientContext.create();
	    		localContext.setCredentialsProvider(credentialsProvider);
	
	    		HttpResponse response = httpClient.execute(getRequest, localContext);
	    		

	    		
				if(response.getStatusLine().getStatusCode() != 200) {
		    		httpClient.close();
	    			throw new RuntimeException("Failed: HTTP error code : " + response.getStatusLine().getStatusCode());
	    		}
	     
	    		BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
	    		String output = br.readLine();
	    		
	    		SignIn signIn = gson.fromJson(output, SignIn.class);
	    		loggedInUserToken = signIn.getToken();
	    		httpClient.close();
			}
			if(uf.loadUser(username, password) != null) {
				User user = uf.loadUser(username, password);
				(Sessions.getCurrent()).setAttribute("user", user);
				loginWin.setVisible(false);
				System.out.println("user " + username + " has logged in to the system.");
				
				logUserLogin(username);
				Executions.sendRedirect("");
			
			} else loginMsgLabel.setValue("Organization has not been found in the Orchestration Engine!");
			
		} catch(Exception e) {
			loginMsgLabel.setValue("Authentication Failed! Please try again.");
			e.printStackTrace();
		}
	}
	
	private void registerUser() {
		if(org_idSpinner.getValue() == null) {
			throw new RuntimeException("Please insert your Organization ID.");
		} else {
			String org_id 	= 	org_idSpinner.getValue().toString();
			String username = 	usernameTextbox.getValue();
			String password = 	passwordTextbox.getValue();
			String firstname= 	firstnameTextbox.getValue();
			String lastname = 	lastnameTextbox.getValue();
			String orgRoles = 	rolesCombobox.getSelectedItem().getValue();
			if(scopeCombobox.getSelectedItem().equals(getCPMComboItem)) {
		        try{
					CloseableHttpClient httpClient = new DefaultHttpClient();
		    		HttpPost postRequest = new HttpPost("https://www.rd.npcs.portbase.com/get-core-cpm/api/get/register/user");
		    		String temp = "organisation_id=" + org_id + "&user_username=" + username + "&user_password=" + password + "&user_firstname=" + firstname + "&user_lastname=" + lastname;
		    		StringEntity input = new StringEntity(temp);
		    		input.setContentType("application/x-www-form-urlencoded");
		    		postRequest.setEntity(input);
		    		HttpResponse response = httpClient.execute(postRequest);
		    		

		    		if(response.getStatusLine().getStatusCode() == 412) {
		    			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			    		String output = br.readLine();
			    		if(output.contains("Make sure the organisation is properly registered first.")) {
			    			String message = "The organization ID is not valid. Do you want to add a new organization?";
			    			Messagebox.Button[] buttons = { Messagebox.Button.YES,Messagebox.Button.NO}; 
			    			String icon = Messagebox.EXCLAMATION;
			    			EventListener<ClickEvent> eventListener = new EventListener<ClickEvent>() {
			    				public void onEvent(ClickEvent event) {
			    					if (Messagebox.ON_YES.equals(event.getName())) {
			    						addNewOrganization();
			    						return;
			    		          } else if(Messagebox.ON_NO.equals(event.getName())) { 
			    						return; 
			    		          }
			    		      }
			    		   };
			    		   Messagebox.show(message, "Add new Organization", buttons, icon, eventListener);
			    			
			    		} else {
			    			System.out.println(output);
			    		}
		    		} else if (response.getStatusLine().getStatusCode() != 200) {
			    		httpClient.close();
		    			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		    		} else {		
		        		boolean done = uf.createUser(username, password, firstname, lastname, "EMAIL", org_id, orgRoles);
		    			if(done) {
		    				System.out.println("User " + username + " from Organization Identifier " + org_id + " has been registered.");
		    				regWin.setVisible(false);
		    				
		    				String msg = "User " + username + " from Organization Identifier " + org_id + " has been registered.";
		    				
		    				Messagebox.show(msg, "User Registration", 0, Messagebox.INFORMATION);
		    				
							logNewUserRegistration(username, firstname, lastname, org_id);
		    				
		    			} else {
		    				regWin.setVisible(false);
		    				Messagebox.show("User " + username + " has 'already' been registered in the GET Controller.", "User Registration", 0, Messagebox.EXCLAMATION);
		    			}
		    		}
		    		httpClient.close();
	    		} catch (MalformedURLException e) {
	    			regMsgLabel.setValue(e.getMessage());
		    		e.printStackTrace();
	    	  } catch (IOException e) {
	    		  regMsgLabel.setValue(e.getMessage());
	    		  e.printStackTrace();
	    	  } 
			} else {
				boolean done = uf.createUser(username, password, firstname, lastname, "EMAIL", org_id, orgRoles);
				if(done) {
					String msg = "User " + username + " from Organization Identifier " + org_id + " has been registered.";
					System.out.println(msg);
					regWin.setVisible(false);
    				Messagebox.show(msg, "User Registration", 0, Messagebox.INFORMATION);

					
					logNewUserRegistration(username, firstname, lastname, org_id);	
				
				} else {
					regWin.setVisible(false);
    				Messagebox.show("User " + username + " has 'already' been registered in the GET Controller.", "User Registration", 0, Messagebox.INFORMATION);

				}
			}
		}
	}
	
	public static User authorizeUser(String token) {
		try{
			CloseableHttpClient httpClient = new DefaultHttpClient();
    		HttpGet getRequest = new HttpGet("https://www.rd.npcs.portbase.com/get-core-cpm/api/get/validate");
    		getRequest.setHeader("Authorization", "Bearer " + token);
    		HttpResponse response = httpClient.execute(getRequest);
			

			if(response.getStatusLine().getStatusCode() != 200) {
	    		httpClient.close();
    			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
    		}
     
    		BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
    		String output = br.readLine();
    		
    		SignIn signIn = gson.fromJson(output, SignIn.class);

			if(uf.loadUser(signIn.getOrganisationName(), signIn.getOrganisationEmail()) != null) {
				User user = uf.loadUser(signIn.getOrganisationName(), signIn.getOrganisationEmail());
				System.out.println("user " + signIn.getOrganisationName() + " has logged in to the system via mobile device.");
				Log log = new Log();
				log.setToken(loggedInUserToken);
				log.setCorrelationId(ActivitiEngineConfig.generalLogCorrelationId);
				log.setOrganisationId(signIn.getOrganisationName());
				log.setLevel("info");
				log.setType("login");
				log.setApplication("GETController");
				log.setActivity("Authentication");
				log.setMessage("user " + signIn.getOrganisationName() + " has logged in to the system via mobile device.");
				log.submitLog();
				httpClient.close();
				return user;
				
			} else {
				System.err.println("Organization has not been found in the Orchestration Engine!");
	    		httpClient.close();
				throw new RuntimeException("Organization has not been found in the Orchestration Engine!");
			}
		} catch(Exception e) {
			System.err.println("Authentication Failed for the user with token= " + token);
			throw new RuntimeException("Authentication Failed for the user with token= " + token);
		}
	}
	
	private void registerOrganizationWithUser() {
		String username 	= 	usernameTextbox2.getValue();
		String password 	= 	passwordTextbox2.getValue();
		String firstname	= 	firstnameTextbox2.getValue();
		String lastname 	= 	lastnameTextbox2.getValue();
		String orgRoles 	= 	roleTextbox2.getValue();
		String orgShortname = 	orgShortnameTextbox.getValue();
		String orgFullname 	=	orgFullnameTextbox.getValue();
		String orgEmail		=	orgEmailTextbox.getValue();
		
		try{
			CloseableHttpClient httpClient = new DefaultHttpClient();
	    	HttpPost postRequest = new HttpPost("https://www.rd.npcs.portbase.com/get-core-cpm/api/get/register/organisation");
    		String temp = "organisation_shortname=" + orgShortname + "&organisation_fullname=" + orgFullname + 
    				"&user_username=" + username + "&user_password=" + password + "&user_firstname=" + firstname + "&user_lastname=" + lastname + "&organisation_email=" + orgEmail;
    		StringEntity input = new StringEntity(temp);
    		input.setContentType("application/x-www-form-urlencoded");
    		postRequest.setEntity(input);
    		HttpResponse response = httpClient.execute(postRequest);
    		

    		if (response.getStatusLine().getStatusCode() != 200) {
    			//Clients.showNotification("Organization " + orgFullname + " has already been registered.");
    			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
	    		String output = br.readLine();
    			//Clients.showNotification(output);
    			throw new RuntimeException("Error - HTTP Code: " + response.getStatusLine().getStatusCode() + "\n" + output);
	    	} else {
		    		String org_id = getOrganisationIdFromResponse(response);
	        		boolean done = uf.createUser(username, password, firstname, lastname, orgEmail, org_id, orgRoles);
	    			if(done) {
	    				System.out.println("Organization " + orgFullname + " (organization ID: " +  org_id + ") has been registered.");
	    				System.out.println("User " + username + " from Organization Identifier " + org_id + " has been registered.");
	    				regOrgWin.setVisible(false);
	    				
	    				String msg = "Organization " + orgFullname + " (organization ID: " +  org_id + ") has been registered.\n" + "User " + 	
	    					username + " from Organization Identifier " + org_id + " has been registered.";
	    				
	    				Messagebox.show(msg, "New Organization and User has been added", 0, org.zkoss.zul.Messagebox.INFORMATION);
	    				
						logNewOrganizationRegistration(orgFullname, org_id);
						logNewUserRegistration(username, firstname, lastname, org_id);
	    				
	    			} else {
	    				regWin.setVisible(false);
	    				Messagebox.show("User " + username + " has 'already' been registered in the GET Controller.", "User Registration", 0, Messagebox.INFORMATION);

	    			}
	    			httpClient.close();

	    		}
    	 	} catch (MalformedURLException e) {
    			regMsgLabel.setValue(e.getMessage());
	    		e.printStackTrace();
    	  } catch (IOException e) {
    		  regMsgLabel.setValue(e.getMessage());
    		  e.printStackTrace();
    	  } 
	}
	
	private void addNewOrganization() {
		for(Component c : regWin.getParent().getChildren()) {
			if(c.getId().contentEquals("regOrgWin")) {
				regOrgWin = (Window)c;
				initOrgRegWindow();
				break;
			}
		}
		regOrgWin.setVisible(true);
		regWin.setVisible(false);
		regOrgWin.doHighlighted();
	}
	
	private void initOrgRegWindow() {
		String username = 	usernameTextbox.getValue();
		String password = 	passwordTextbox.getValue();
		String firstname= 	firstnameTextbox.getValue();
		String lastname = 	lastnameTextbox.getValue();
		String orgRoles = 	rolesCombobox.getSelectedItem().getValue();
		
		List<Component> gridRows = regOrgWin.getChildren().get(1).getChildren().get(1).getChildren();
		for(Component row : gridRows) {
			for(Component c : row.getChildren()) {
				switch (c.getId()) {
				case "usernameTextbox2":	usernameTextbox2 		= (Textbox) c; break;
				case "passwordTextbox2":	passwordTextbox2 		= (Textbox) c; break;
				case "firstnameTextbox2":	firstnameTextbox2 		= (Textbox) c; break;
				case "lastnameTextbox2":	lastnameTextbox2		= (Textbox) c; break;
				case "roleTextbox2":		roleTextbox2			= (Textbox) c; break;
				case "orgShortnameTextbox": orgShortnameTextbox		= (Textbox) c; break;
				case "orgFullnameTextbox":	orgFullnameTextbox		= (Textbox) c; break;
				case "orgEmailTextbox":		orgEmailTextbox			= (Textbox)	c; break;
				case "regOrgCell":
				regOrgBtn = (Button)  c.getChildren().get(0).getChildren().get(0); break;
				}
			}
		}
		
		usernameTextbox2.setValue(username);
		passwordTextbox2.setValue(password);
		firstnameTextbox2.setValue(firstname);
		lastnameTextbox2.setValue(lastname);
		roleTextbox2.setValue(orgRoles);
		regOrgBtn.addEventListener("onClick", new org.zkoss.zk.ui.event.EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				registerOrganizationWithUser();
				
			}
		});
	}
	
	private void logNewUserRegistration(String username, String firstname,
			String lastname, String org_id) {
		Log log = new Log();
		log.setToken(loggedInUserToken);
		log.setCorrelationId(ActivitiEngineConfig.generalLogCorrelationId);
		log.setOrganisationId(org_id);
		log.setLevel("info");
		log.setType("registerUser");
		log.setApplication("GETController");
		log.setActivity("Authentication");
		log.setMessage("User " + username + " (" + firstname + " " + lastname + ") has been registered.");
		log.submitLog();
	}

	private void logNewOrganizationRegistration(String orgFullname, String org_id) {
		Log log = new Log();
		log.setToken(loggedInUserToken);
		log.setCorrelationId(ActivitiEngineConfig.generalLogCorrelationId);
		log.setOrganisationId(org_id);
		log.setLevel("info");
		log.setType("registerOrganization");
		log.setApplication("GETController");
		log.setActivity("Authentication");
		log.setMessage("Organization " + orgFullname + " (ID: " + org_id + ") has been registered.");
		log.submitLog();
	}
	
	private void logUserLogin(String username) {
		Log log = new Log();
		log.setToken(loggedInUserToken);
		log.setCorrelationId(ActivitiEngineConfig.generalLogCorrelationId);
		log.setOrganisationId(username);
		log.setLevel("info");
		log.setType("login");
		log.setApplication("GETController");
		log.setActivity("Authentication");
		log.setMessage("user " + username + " has logged in to the system.");
		log.submitLog();
	}
	
	
	private String getOrganisationIdFromResponse(HttpResponse response)
			throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
		String output = br.readLine();
		String org_id = output.substring((output.indexOf("organisation_id") + 17) , (output.indexOf("shortname") - 2));
		return org_id;
	}
}
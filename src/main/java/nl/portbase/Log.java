package main.java.nl.portbase;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;


@SuppressWarnings("deprecation")
public class Log {
	
	public static void main(String[] args) {
		Log l = new Log();
		l.setCorrelationId("1234");
		l.setMessage("Hello World... (2)");
		l.submitLog();

	}
	
	private String token;
	private String organisationId;
	private String application;
	private String activity;
	private String correlationId;
	private String messageId;
	private String level;
	private String type;
	private String message;
	private String description;

	public String getToken() { return token; }
	public String getOrganisationId() { return organisationId; }
	public String getApplication() { return application; }
	public String getActivity() { return activity; }
	public String getCorrelationId() { return correlationId; }
	public String getMessageId() { return messageId; }
	public String getLevel() { return level; }
	public String getType() { return type; }
	public String getMessage() { return message; }
	public String getDescription() { return description; }

	public void setToken(String token) { this.token = token; }
	public void setOrganisationId(String organisationId) { this.organisationId = organisationId; }
	public void setApplication(String application) { this.application = application; }
	public void setActivity(String activity) { this.activity = activity; }
	public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
	public void setMessageId(String messageId) { this.messageId = messageId; }
	public void setLevel(String level) { this.level = level; }
	public void setType(String type) { this.type = type; }
	public void setMessage(String message) { this.message = message; }
	public void setDescription(String description) { this.description = description; }
	
	
	public void submitLog() {
		try {
			@SuppressWarnings({ "resource" })
			CloseableHttpClient httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost("https://www.rd.npcs.portbase.com/LogManager/log");
			if(token != null)
				postRequest.setHeader("Authorization", "Bearer " + token);
			
			String reqBody = "";
			if(organisationId != null)
				reqBody = reqBody + "organisationId=" + organisationId + "&";
			if(application != null)
				reqBody = reqBody + "application=" + application + "&";
			if(activity != null)
				reqBody = reqBody + "activity=" + activity + "&";
			if(correlationId != null)
				reqBody = reqBody + "correlationId=" + correlationId + "&";
			if(messageId != null)
				reqBody = reqBody + "messageId=" + messageId + "&";
			if(level != null)
				reqBody = reqBody + "level=" + level + "&";
			if(type != null)
				reqBody = reqBody + "type=" + type + "&";
			if(message != null)
				reqBody = reqBody + "message=" + message + "&";
			if(description != null)
				reqBody = reqBody + "description=" + description + "&";
			
			if(reqBody.length() > 0)
				reqBody = reqBody.substring(0 , reqBody.length() - 1);
			
			StringEntity input = new StringEntity(reqBody);
			input.setContentType("application/x-www-form-urlencoded");
			postRequest.setEntity(input);
			HttpResponse response = httpClient.execute(postRequest);
				
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
		 
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output = br.readLine();
			
			if(output.contains("OK")) {
				System.out.println("[LOG] *** [" + reqBody + "] has been submitted.");
			}
		} catch (Exception e) {
	  		System.err.println(e.getMessage() + " in submiting log.");
	  		e.printStackTrace();
  		}
	}
}

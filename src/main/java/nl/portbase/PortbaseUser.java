package main.java.nl.portbase;

public class PortbaseUser {
	private String organisation_id;
	private String user_username;
	private String user_password;
	private String user_firstname;
	private String user_lastname;
	public String getOrganisation_id() {
		return organisation_id;
	}
	public void setOrganisation_id(String organisation_id) {
		this.organisation_id = organisation_id;
	}
	public String getUser_username() {
		return user_username;
	}
	public void setUser_username(String user_username) {
		this.user_username = user_username;
	}
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	public String getUser_firstname() {
		return user_firstname;
	}
	public void setUser_firstname(String user_firstname) {
		this.user_firstname = user_firstname;
	}
	public String getUser_lastname() {
		return user_lastname;
	}
	public void setUser_lastname(String user_lastname) {
		this.user_lastname = user_lastname;
	}
	public PortbaseUser(String organisation_id, String user_username,
			String user_password, String user_firstname, String user_lastname) {
		super();
		this.organisation_id = organisation_id;
		this.user_username = user_username;
		this.user_password = user_password;
		this.user_firstname = user_firstname;
		this.user_lastname = user_lastname;
	}
	
	

}

package main.java.nl.portbase;

public class Organization {
	private String organisation_shortname;
	private String organisation_fullname;
	private String user_username;
	private String user_password;
	private String user_firstname;
	private String user_lastname;
	public String getOrganisation_shortname() {
		return organisation_shortname;
	}
	public void setOrganisation_shortname(String organisation_shortname) {
		this.organisation_shortname = organisation_shortname;
	}
	public String getOrganisation_fullname() {
		return organisation_fullname;
	}
	public void setOrganisation_fullname(String organisation_fullname) {
		this.organisation_fullname = organisation_fullname;
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
	public Organization(String organisation_shortname,
			String organisation_fullname, String user_username,
			String user_password, String user_firstname, String user_lastname) {
		super();
		this.organisation_shortname = organisation_shortname;
		this.organisation_fullname = organisation_fullname;
		this.user_username = user_username;
		this.user_password = user_password;
		this.user_firstname = user_firstname;
		this.user_lastname = user_lastname;
	}
	

}

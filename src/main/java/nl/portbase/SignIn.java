package main.java.nl.portbase;

public class SignIn {

	private String organisationName;
	private String organisationEmail;
	private String[] roles;
	private String token;
	private String validUntil;

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public String getOrganisationEmail() {
		return organisationEmail;
	}

	public void setOrganisationEmail(String organisationEmail) {
		this.organisationEmail = organisationEmail;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(String validUntil) {
		this.validUntil = validUntil;
	}
}

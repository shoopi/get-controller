/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/

package main.java.nl.tue.ieis.get.services;

import java.io.Serializable;

public class SidebarPage implements Serializable{
	private static final long serialVersionUID = 1L;
	String name;
	String label;
	String iconUri;
	String uri;
	public String caseID;

	public SidebarPage(String name, String label, String iconUri, String uri, String caseID) {
		this.name = name;
		this.label = label;
		this.iconUri = iconUri;
		this.uri = uri;
		this.caseID = caseID;
	}

	public String getCaseID() {
		return caseID;
	}

	public void setCaseID(String caseID) {
		this.caseID = caseID;
	}

	public String getName() {
		return name;
	}

	public String getLabel() {
		return label;
	}

	public String getIconUri() {
		return iconUri;
	}

	public String getUri() {
		return uri;
	}
	
	public void setIconUri(String uri) {
		this.iconUri = uri;
	}
}
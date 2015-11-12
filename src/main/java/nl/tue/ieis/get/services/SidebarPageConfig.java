/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package main.java.nl.tue.ieis.get.services;

import java.util.List;

import main.java.nl.tue.ieis.get.event.service.MessageObject;

public interface SidebarPageConfig {
	
	/** get pages of this application **/
	public List<SidebarPage> getPages();
	
	/** get page **/
	public SidebarPage getPage(String name);
	
	public List<MessageObject> updateMessageWindow(String caseId);
}
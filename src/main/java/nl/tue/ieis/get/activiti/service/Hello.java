package main.java.nl.tue.ieis.get.activiti.service;

import org.activiti.rest.common.api.SecuredResource;
import org.restlet.resource.Get;

public class Hello extends SecuredResource {
	
	 @Get
	 public String test(){
	   if (!authenticate()) {
	     return null;
	   }
	 return "GET Service";
	 }

}

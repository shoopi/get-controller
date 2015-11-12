package main.java.nl.tue.ieis.get.activiti.service;

import java.text.SimpleDateFormat;

import main.java.nl.tue.ieis.get.activiti.InstanceMigration;
import main.java.nl.tue.ieis.get.map.Address;
import main.java.nl.tue.ieis.get.map.AddressTime;
import main.java.nl.tue.ieis.get.map.TransportRequest;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.rest.common.api.SecuredResource;
import org.apache.commons.httpclient.util.DateParseException;
import org.restlet.data.Status;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;

public class DynamicAdaptation extends SecuredResource {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	 @Post
	  public String submitForm(DynamicAdaptationRequest submitRequest) {
	    if (!authenticate()) {
	      return null;
	    }

	    if (submitRequest == null) {
	      throw new ResourceException(new Status(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE.getCode(), 
	          "A request body was expected when executing the form submit.", null, null));
	    }

	    if (submitRequest.getOldCase() == null || submitRequest.getNewProcessId() == null) {
	      throw new ActivitiIllegalArgumentException("Both oldCase and newProcessId have to be provided.");
	    }

	    try {
	    	if(submitRequest.getRoute() == null)	submitRequest.setRoute("");
	    	
	    	TransportRequest tr =  new TransportRequest();
	    	AddressTime src = new AddressTime();
	    	AddressTime dst = new AddressTime();
	    	if(submitRequest.getSourceAddress() != null)	
	    		src.setAddress(new Address(submitRequest.getSourceAddress(), "", "", "", "", ""));
	    	if(submitRequest.getDestAddress() != null)		
	    		dst.setAddress(new Address(submitRequest.getDestAddress(), "", "", "", "", ""));
	    	if(submitRequest.getSourceDate() != null)		
	    		try{src.setDate(sdf.parse(submitRequest.getSourceDate()));} 
	    		catch(Exception e) {throw new DateParseException("Source Date is not parsable. Please Enter the mentioned date in the yyyy-MM-dd'T'HH:mm:ss format." + e.getMessage());}
	    	if(submitRequest.getDestDate() != null)			
	    		try{dst.setDate(sdf.parse(submitRequest.getDestDate()));} 	
	    		catch(Exception e) {throw new DateParseException("Destination Date is not parsable. Please Enter the mentioned date in the yyyy-MM-dd'T'HH:mm:ss format." + e.getMessage());}
	    	tr.setSource(src);
	    	tr.setDest(dst);
	    	
			InstanceMigration migration1 = new InstanceMigration(false, submitRequest.getOldCase());
			String done = migration1.doMigration(submitRequest.getNewProcessId(), submitRequest.getRoute(), tr, false);
			
		    if(done.length() > 1) {
		    	return  "Dynamic Adaptation has been successfully done! ... Case: " + submitRequest.getOldCase() + " has been migrated to Case: " + done;
		    }
		    else
		    	throw new Exception ("Error in migrating case: " + submitRequest.getOldCase() + " to the process model id:  " + submitRequest.getNewProcessId());
	    } catch (Exception e) {
	    	throw new ActivitiException( "Your request is unsuccessful." + e.getMessage());
	    }
	  }
}

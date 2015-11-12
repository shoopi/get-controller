package main.java.nl.tue.ieis.get.activiti;


import main.java.nl.tue.ieis.get.activiti.service.*;

import org.activiti.rest.common.api.DefaultResource;
import org.activiti.rest.common.filter.JsonpFilter;
import org.activiti.rest.service.application.ActivitiRestServicesApplication;
import org.activiti.rest.service.application.RestServicesInit;
import org.restlet.Restlet;
import org.restlet.routing.Router;


public class CustomActivitiRestServicesApplication extends ActivitiRestServicesApplication {
	@Override
	public synchronized Restlet createInboundRoot() {
		
		initializeAuthentication();
	    
	    Router router = new Router(getContext());
	    router.attachDefault(DefaultResource.class);
	    RestServicesInit.attachResources(router);
	    
	    //add GET Service own router
	    router.attach("/runtime/tasklist/{processInstanceId}", TaskList.class);
	    router.attach("/security/token={token}", ValidateToken.class);
	    //router.attach("/runtime/event/spec={processSpecificationId},instance={processInstanceId},task={taskDesignId}", EventManager.class);
	    router.attach("/form/execution", FormDataResourceExt.class);
	    router.attach("/form/getVariables/taskId={taskId}&processInstanceId={processInstanceId}", AssociatedFormToTask.class);
	    router.attach("/routes/noCase", SelectedRoute.class);
	    router.attach("/runtime/migration", DynamicAdaptation.class);
	    router.attach("/hello", Hello.class);
	    
	    JsonpFilter jsonpFilter = new JsonpFilter(getContext());
	    authenticator.setNext(jsonpFilter);
	    jsonpFilter.setNext(router);

	    return authenticator;
    }

}

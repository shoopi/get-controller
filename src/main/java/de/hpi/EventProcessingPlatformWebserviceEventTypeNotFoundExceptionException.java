
/**
 * EventProcessingPlatformWebserviceEventTypeNotFoundExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */

package main.java.de.hpi;

public class EventProcessingPlatformWebserviceEventTypeNotFoundExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1438192745331L;
    
    private main.java.de.hpi.EventProcessingPlatformWebserviceStub.EventProcessingPlatformWebserviceEventTypeNotFoundException faultMessage;

    
        public EventProcessingPlatformWebserviceEventTypeNotFoundExceptionException() {
            super("EventProcessingPlatformWebserviceEventTypeNotFoundExceptionException");
        }

        public EventProcessingPlatformWebserviceEventTypeNotFoundExceptionException(java.lang.String s) {
           super(s);
        }

        public EventProcessingPlatformWebserviceEventTypeNotFoundExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public EventProcessingPlatformWebserviceEventTypeNotFoundExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(main.java.de.hpi.EventProcessingPlatformWebserviceStub.EventProcessingPlatformWebserviceEventTypeNotFoundException msg){
       faultMessage = msg;
    }
    
    public main.java.de.hpi.EventProcessingPlatformWebserviceStub.EventProcessingPlatformWebserviceEventTypeNotFoundException getFaultMessage(){
       return faultMessage;
    }
}
    
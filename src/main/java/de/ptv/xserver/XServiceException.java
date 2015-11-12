
/**
 * XServiceException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */

package main.java.de.ptv.xserver;

public class XServiceException extends java.lang.Exception{

    private static final long serialVersionUID = 1439475981969L;
    
    private main.java.de.ptv.xserver.XIntermodalWSServiceStub.XServiceExceptionE faultMessage;

    
        public XServiceException() {
            super("XServiceException");
        }

        public XServiceException(java.lang.String s) {
           super(s);
        }

        public XServiceException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public XServiceException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(main.java.de.ptv.xserver.XIntermodalWSServiceStub.XServiceExceptionE msg){
       faultMessage = msg;
    }
    
    public main.java.de.ptv.xserver.XIntermodalWSServiceStub.XServiceExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    
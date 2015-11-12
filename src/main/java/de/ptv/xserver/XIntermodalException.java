
/**
 * XIntermodalException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */

package main.java.de.ptv.xserver;

public class XIntermodalException extends java.lang.Exception{

    private static final long serialVersionUID = 1439475982056L;
    
    private main.java.de.ptv.xserver.XIntermodalWSServiceStub.XIntermodalExceptionE faultMessage;

    
        public XIntermodalException() {
            super("XIntermodalException");
        }

        public XIntermodalException(java.lang.String s) {
           super(s);
        }

        public XIntermodalException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public XIntermodalException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(main.java.de.ptv.xserver.XIntermodalWSServiceStub.XIntermodalExceptionE msg){
       faultMessage = msg;
    }
    
    public main.java.de.ptv.xserver.XIntermodalWSServiceStub.XIntermodalExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    
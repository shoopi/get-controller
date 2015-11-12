
/**
 * ConstraintViolationException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */

package main.java.de.ptv.xserver;

public class ConstraintViolationException extends java.lang.Exception{

    private static final long serialVersionUID = 1439475982031L;
    
    private main.java.de.ptv.xserver.XIntermodalWSServiceStub.ConstraintViolationExceptionE faultMessage;

    
        public ConstraintViolationException() {
            super("ConstraintViolationException");
        }

        public ConstraintViolationException(java.lang.String s) {
           super(s);
        }

        public ConstraintViolationException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public ConstraintViolationException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(main.java.de.ptv.xserver.XIntermodalWSServiceStub.ConstraintViolationExceptionE msg){
       faultMessage = msg;
    }
    
    public main.java.de.ptv.xserver.XIntermodalWSServiceStub.ConstraintViolationExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    

/**
 * UnexpectedException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */

package main.java.de.ptv.xserver;

public class UnexpectedException extends java.lang.Exception{

    private static final long serialVersionUID = 1439475981992L;
    
    private main.java.de.ptv.xserver.XIntermodalWSServiceStub.UnexpectedExceptionE faultMessage;

    
        public UnexpectedException() {
            super("UnexpectedException");
        }

        public UnexpectedException(java.lang.String s) {
           super(s);
        }

        public UnexpectedException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public UnexpectedException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(main.java.de.ptv.xserver.XIntermodalWSServiceStub.UnexpectedExceptionE msg){
       faultMessage = msg;
    }
    
    public main.java.de.ptv.xserver.XIntermodalWSServiceStub.UnexpectedExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    
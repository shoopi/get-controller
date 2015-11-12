
/**
 * OptimisticRollbackException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */

package main.java.de.ptv.xserver;

public class OptimisticRollbackException extends java.lang.Exception{

    private static final long serialVersionUID = 1439475982018L;
    
    private main.java.de.ptv.xserver.XIntermodalWSServiceStub.OptimisticRollbackExceptionE faultMessage;

    
        public OptimisticRollbackException() {
            super("OptimisticRollbackException");
        }

        public OptimisticRollbackException(java.lang.String s) {
           super(s);
        }

        public OptimisticRollbackException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public OptimisticRollbackException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(main.java.de.ptv.xserver.XIntermodalWSServiceStub.OptimisticRollbackExceptionE msg){
       faultMessage = msg;
    }
    
    public main.java.de.ptv.xserver.XIntermodalWSServiceStub.OptimisticRollbackExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    
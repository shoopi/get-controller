
/**
 * TableNotFoundException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */

package main.java.de.ptv.xserver;

public class TableNotFoundException extends java.lang.Exception{

    private static final long serialVersionUID = 1439475981980L;
    
    private main.java.de.ptv.xserver.XIntermodalWSServiceStub.TableNotFoundExceptionE faultMessage;

    
        public TableNotFoundException() {
            super("TableNotFoundException");
        }

        public TableNotFoundException(java.lang.String s) {
           super(s);
        }

        public TableNotFoundException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public TableNotFoundException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(main.java.de.ptv.xserver.XIntermodalWSServiceStub.TableNotFoundExceptionE msg){
       faultMessage = msg;
    }
    
    public main.java.de.ptv.xserver.XIntermodalWSServiceStub.TableNotFoundExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    
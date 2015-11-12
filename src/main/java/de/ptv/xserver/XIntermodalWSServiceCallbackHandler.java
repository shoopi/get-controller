
/**
 * XIntermodalWSServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */

    package main.java.de.ptv.xserver;

    /**
     *  XIntermodalWSServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class XIntermodalWSServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public XIntermodalWSServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public XIntermodalWSServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for getTruckLines method
            * override this method for handling normal response from getTruckLines operation
            */
           public void receiveResultgetTruckLines(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.GetTruckLinesResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getTruckLines operation
           */
            public void receiveErrorgetTruckLines(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getXProperties method
            * override this method for handling normal response from getXProperties operation
            */
           public void receiveResultgetXProperties(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.GetXPropertiesResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getXProperties operation
           */
            public void receiveErrorgetXProperties(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getSampleRoutingResponseTSC method
            * override this method for handling normal response from getSampleRoutingResponseTSC operation
            */
           public void receiveResultgetSampleRoutingResponseTSC(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.GetSampleRoutingResponseTSCResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getSampleRoutingResponseTSC operation
           */
            public void receiveErrorgetSampleRoutingResponseTSC(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getSampleTARequest method
            * override this method for handling normal response from getSampleTARequest operation
            */
           public void receiveResultgetSampleTARequest(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.GetSampleTARequestResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getSampleTARequest operation
           */
            public void receiveErrorgetSampleTARequest(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for determineTransportAlternatives method
            * override this method for handling normal response from determineTransportAlternatives operation
            */
           public void receiveResultdetermineTransportAlternatives(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.DetermineTransportAlternativesResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from determineTransportAlternatives operation
           */
            public void receiveErrordetermineTransportAlternatives(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getSampleRoutingResponse method
            * override this method for handling normal response from getSampleRoutingResponse operation
            */
           public void receiveResultgetSampleRoutingResponse(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.GetSampleRoutingResponseResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getSampleRoutingResponse operation
           */
            public void receiveErrorgetSampleRoutingResponse(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getSampleTAResponse method
            * override this method for handling normal response from getSampleTAResponse operation
            */
           public void receiveResultgetSampleTAResponse(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.GetSampleTAResponseResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getSampleTAResponse operation
           */
            public void receiveErrorgetSampleTAResponse(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for importTransfers method
            * override this method for handling normal response from importTransfers operation
            */
           public void receiveResultimportTransfers(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.ImportTransfersResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from importTransfers operation
           */
            public void receiveErrorimportTransfers(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getSampleSchedulingResponse method
            * override this method for handling normal response from getSampleSchedulingResponse operation
            */
           public void receiveResultgetSampleSchedulingResponse(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.GetSampleSchedulingResponseResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getSampleSchedulingResponse operation
           */
            public void receiveErrorgetSampleSchedulingResponse(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for importAgencies method
            * override this method for handling normal response from importAgencies operation
            */
           public void receiveResultimportAgencies(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.ImportAgenciesResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from importAgencies operation
           */
            public void receiveErrorimportAgencies(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for calculateRoute method
            * override this method for handling normal response from calculateRoute operation
            */
           public void receiveResultcalculateRoute(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.CalculateRouteResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from calculateRoute operation
           */
            public void receiveErrorcalculateRoute(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getOperators method
            * override this method for handling normal response from getOperators operation
            */
           public void receiveResultgetOperators(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.GetOperatorsResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getOperators operation
           */
            public void receiveErrorgetOperators(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for doLineScheduling method
            * override this method for handling normal response from doLineScheduling operation
            */
           public void receiveResultdoLineScheduling(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.DoLineSchedulingResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from doLineScheduling operation
           */
            public void receiveErrordoLineScheduling(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAllLines method
            * override this method for handling normal response from getAllLines operation
            */
           public void receiveResultgetAllLines(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.GetAllLinesResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAllLines operation
           */
            public void receiveErrorgetAllLines(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getTerminals method
            * override this method for handling normal response from getTerminals operation
            */
           public void receiveResultgetTerminals(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.GetTerminalsResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getTerminals operation
           */
            public void receiveErrorgetTerminals(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getSampleSchedulingRequest method
            * override this method for handling normal response from getSampleSchedulingRequest operation
            */
           public void receiveResultgetSampleSchedulingRequest(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.GetSampleSchedulingRequestResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getSampleSchedulingRequest operation
           */
            public void receiveErrorgetSampleSchedulingRequest(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getSampleRoutingRequestTSC method
            * override this method for handling normal response from getSampleRoutingRequestTSC operation
            */
           public void receiveResultgetSampleRoutingRequestTSC(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.GetSampleRoutingRequestTSCResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getSampleRoutingRequestTSC operation
           */
            public void receiveErrorgetSampleRoutingRequestTSC(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for importTimeTables method
            * override this method for handling normal response from importTimeTables operation
            */
           public void receiveResultimportTimeTables(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.ImportTimeTablesResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from importTimeTables operation
           */
            public void receiveErrorimportTimeTables(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for activateTimeTables method
            * override this method for handling normal response from activateTimeTables operation
            */
           public void receiveResultactivateTimeTables(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.ActivateTimeTablesResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from activateTimeTables operation
           */
            public void receiveErroractivateTimeTables(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for importTerminals method
            * override this method for handling normal response from importTerminals operation
            */
           public void receiveResultimportTerminals(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.ImportTerminalsResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from importTerminals operation
           */
            public void receiveErrorimportTerminals(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getSampleRoutingRequest method
            * override this method for handling normal response from getSampleRoutingRequest operation
            */
           public void receiveResultgetSampleRoutingRequest(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.GetSampleRoutingRequestResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getSampleRoutingRequest operation
           */
            public void receiveErrorgetSampleRoutingRequest(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getTransportModes method
            * override this method for handling normal response from getTransportModes operation
            */
           public void receiveResultgetTransportModes(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.GetTransportModesResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getTransportModes operation
           */
            public void receiveErrorgetTransportModes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for importAirTimetables method
            * override this method for handling normal response from importAirTimetables operation
            */
           public void receiveResultimportAirTimetables(
                    main.java.de.ptv.xserver.XIntermodalWSServiceStub.ImportAirTimetablesResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from importAirTimetables operation
           */
            public void receiveErrorimportAirTimetables(java.lang.Exception e) {
            }
                


    }
    
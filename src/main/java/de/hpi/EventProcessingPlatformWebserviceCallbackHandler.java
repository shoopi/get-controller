
/**
 * EventProcessingPlatformWebserviceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */

    package main.java.de.hpi;

    /**
     *  EventProcessingPlatformWebserviceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class EventProcessingPlatformWebserviceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public EventProcessingPlatformWebserviceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public EventProcessingPlatformWebserviceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for importEvents method
            * override this method for handling normal response from importEvents operation
            */
           public void receiveResultimportEvents(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from importEvents operation
           */
            public void receiveErrorimportEvents(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for storeRouteInSemanticDB method
            * override this method for handling normal response from storeRouteInSemanticDB operation
            */
           public void receiveResultstoreRouteInSemanticDB(
                    main.java.de.hpi.EventProcessingPlatformWebserviceStub.StoreRouteInSemanticDBResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from storeRouteInSemanticDB operation
           */
            public void receiveErrorstoreRouteInSemanticDB(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for registerEventCorrelationRule method
            * override this method for handling normal response from registerEventCorrelationRule operation
            */
           public void receiveResultregisterEventCorrelationRule(
                    main.java.de.hpi.EventProcessingPlatformWebserviceStub.RegisterEventCorrelationRuleResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from registerEventCorrelationRule operation
           */
            public void receiveErrorregisterEventCorrelationRule(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for unregisterQueriesFromQueue method
            * override this method for handling normal response from unregisterQueriesFromQueue operation
            */
           public void receiveResultunregisterQueriesFromQueue(
                    main.java.de.hpi.EventProcessingPlatformWebserviceStub.UnregisterQueriesFromQueueResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from unregisterQueriesFromQueue operation
           */
            public void receiveErrorunregisterQueriesFromQueue(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for unregisterQueryFromQueue method
            * override this method for handling normal response from unregisterQueryFromQueue operation
            */
           public void receiveResultunregisterQueryFromQueue(
                    main.java.de.hpi.EventProcessingPlatformWebserviceStub.UnregisterQueryFromQueueResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from unregisterQueryFromQueue operation
           */
            public void receiveErrorunregisterQueryFromQueue(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for unregisterEventType method
            * override this method for handling normal response from unregisterEventType operation
            */
           public void receiveResultunregisterEventType(
                    main.java.de.hpi.EventProcessingPlatformWebserviceStub.UnregisterEventTypeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from unregisterEventType operation
           */
            public void receiveErrorunregisterEventType(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for registerEventType method
            * override this method for handling normal response from registerEventType operation
            */
           public void receiveResultregisterEventType(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from registerEventType operation
           */
            public void receiveErrorregisterEventType(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for importSemanticEventMapping method
            * override this method for handling normal response from importSemanticEventMapping operation
            */
           public void receiveResultimportSemanticEventMapping(
                    main.java.de.hpi.EventProcessingPlatformWebserviceStub.ImportSemanticEventMappingResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from importSemanticEventMapping operation
           */
            public void receiveErrorimportSemanticEventMapping(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAllEventTypes method
            * override this method for handling normal response from getAllEventTypes operation
            */
           public void receiveResultgetAllEventTypes(
                    main.java.de.hpi.EventProcessingPlatformWebserviceStub.GetAllEventTypesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAllEventTypes operation
           */
            public void receiveErrorgetAllEventTypes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for registerEventAggregationRule method
            * override this method for handling normal response from registerEventAggregationRule operation
            */
           public void receiveResultregisterEventAggregationRule(
                    main.java.de.hpi.EventProcessingPlatformWebserviceStub.RegisterEventAggregationRuleResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from registerEventAggregationRule operation
           */
            public void receiveErrorregisterEventAggregationRule(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for unregisterEventAggregationRule method
            * override this method for handling normal response from unregisterEventAggregationRule operation
            */
           public void receiveResultunregisterEventAggregationRule(
                    main.java.de.hpi.EventProcessingPlatformWebserviceStub.UnregisterEventAggregationRuleResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from unregisterEventAggregationRule operation
           */
            public void receiveErrorunregisterEventAggregationRule(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAttributeNames method
            * override this method for handling normal response from getAttributeNames operation
            */
           public void receiveResultgetAttributeNames(
                    main.java.de.hpi.EventProcessingPlatformWebserviceStub.GetAttributeNamesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAttributeNames operation
           */
            public void receiveErrorgetAttributeNames(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getEventTypeXSD method
            * override this method for handling normal response from getEventTypeXSD operation
            */
           public void receiveResultgetEventTypeXSD(
                    main.java.de.hpi.EventProcessingPlatformWebserviceStub.GetEventTypeXSDResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getEventTypeXSD operation
           */
            public void receiveErrorgetEventTypeXSD(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for registerQueryForQueue method
            * override this method for handling normal response from registerQueryForQueue operation
            */
           public void receiveResultregisterQueryForQueue(
                    main.java.de.hpi.EventProcessingPlatformWebserviceStub.RegisterQueryForQueueResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from registerQueryForQueue operation
           */
            public void receiveErrorregisterQueryForQueue(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for storeRoute method
            * override this method for handling normal response from storeRoute operation
            */
           public void receiveResultstoreRoute(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from storeRoute operation
           */
            public void receiveErrorstoreRoute(java.lang.Exception e) {
            }
                


    }
    
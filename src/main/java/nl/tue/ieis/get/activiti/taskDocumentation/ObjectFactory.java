//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.08 at 12:21:15 PM CEST 
//


package main.java.nl.tue.ieis.get.activiti.taskDocumentation;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the main.java.nl.tue.ieis.get.activiti.taskDocumentation package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: main.java.nl.tue.ieis.get.activiti.taskDocumentation
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TaskDocumentation }
     * 
     */
    public TaskDocumentation createTaskDocumentation() {
        return new TaskDocumentation();
    }

    /**
     * Create an instance of {@link TaskDocumentation.QueryAnnotation }
     * 
     */
    public TaskDocumentation.QueryAnnotation createTaskDocumentationQueryAnnotation() {
        return new TaskDocumentation.QueryAnnotation();
    }

    /**
     * Create an instance of {@link TaskDocumentation.QueryAnnotation.Query }
     * 
     */
    public TaskDocumentation.QueryAnnotation.Query createTaskDocumentationQueryAnnotationQuery() {
        return new TaskDocumentation.QueryAnnotation.Query();
    }

    /**
     * Create an instance of {@link TaskDocumentation.ReplanningAnnotation }
     * 
     */
    public TaskDocumentation.ReplanningAnnotation createTaskDocumentationReplanningAnnotation() {
        return new TaskDocumentation.ReplanningAnnotation();
    }

    /**
     * Create an instance of {@link TaskDocumentation.QueryAnnotation.Query.Scope }
     * 
     */
    public TaskDocumentation.QueryAnnotation.Query.Scope createTaskDocumentationQueryAnnotationQueryScope() {
        return new TaskDocumentation.QueryAnnotation.Query.Scope();
    }

}

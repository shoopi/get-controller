//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.16 at 02:29:06 PM CEST 
//


package main.java.nl.tue.ieis.get.event.type.roadTrafficUpdate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lengthDifference" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="timestamp" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="delayDifference" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="roadsAffectedDifference" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="provider" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idAtProvider" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="currentRoadTrafficID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="previousRoadTrafficID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "lengthDifference",
    "timestamp",
    "delayDifference",
    "roadsAffectedDifference",
    "provider",
    "idAtProvider",
    "currentRoadTrafficID",
    "previousRoadTrafficID"
})
@XmlRootElement(name = "RoadTrafficUpdateEvent")
public class RoadTrafficUpdateEvent {

    protected long lengthDifference;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar timestamp;
    protected long delayDifference;
    @XmlElement(required = true)
    protected String roadsAffectedDifference;
    @XmlElement(required = true)
    protected String provider;
    @XmlElement(required = true)
    protected String idAtProvider;
    @XmlElement(required = true)
    protected String currentRoadTrafficID;
    @XmlElement(required = true)
    protected String previousRoadTrafficID;

    /**
     * Gets the value of the lengthDifference property.
     * 
     */
    public long getLengthDifference() {
        return lengthDifference;
    }

    /**
     * Sets the value of the lengthDifference property.
     * 
     */
    public void setLengthDifference(long value) {
        this.lengthDifference = value;
    }

    /**
     * Gets the value of the timestamp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTimestamp(XMLGregorianCalendar value) {
        this.timestamp = value;
    }

    /**
     * Gets the value of the delayDifference property.
     * 
     */
    public long getDelayDifference() {
        return delayDifference;
    }

    /**
     * Sets the value of the delayDifference property.
     * 
     */
    public void setDelayDifference(long value) {
        this.delayDifference = value;
    }

    /**
     * Gets the value of the roadsAffectedDifference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRoadsAffectedDifference() {
        return roadsAffectedDifference;
    }

    /**
     * Sets the value of the roadsAffectedDifference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRoadsAffectedDifference(String value) {
        this.roadsAffectedDifference = value;
    }

    /**
     * Gets the value of the provider property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvider() {
        return provider;
    }

    /**
     * Sets the value of the provider property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvider(String value) {
        this.provider = value;
    }

    /**
     * Gets the value of the idAtProvider property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAtProvider() {
        return idAtProvider;
    }

    /**
     * Sets the value of the idAtProvider property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAtProvider(String value) {
        this.idAtProvider = value;
    }

    /**
     * Gets the value of the currentRoadTrafficID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrentRoadTrafficID() {
        return currentRoadTrafficID;
    }

    /**
     * Sets the value of the currentRoadTrafficID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrentRoadTrafficID(String value) {
        this.currentRoadTrafficID = value;
    }

    /**
     * Gets the value of the previousRoadTrafficID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPreviousRoadTrafficID() {
        return previousRoadTrafficID;
    }

    /**
     * Sets the value of the previousRoadTrafficID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPreviousRoadTrafficID(String value) {
        this.previousRoadTrafficID = value;
    }

}
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.16 at 02:27:27 PM CEST 
//


package main.java.nl.tue.ieis.get.event.type.transportProgressTransgression;

import java.math.BigInteger;
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
 *         &lt;element name="operatorId" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="latitude" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="longitude" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="distanceRemaining" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="distanceCovered" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="timeElapsed" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="timeRemaining" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="plannedETA" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="predictedETA" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="predictedDelay" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
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
    "operatorId",
    "latitude",
    "longitude",
    "distanceRemaining",
    "distanceCovered",
    "timeElapsed",
    "timeRemaining",
    "plannedETA",
    "predictedETA",
    "predictedDelay",
    "timestamp"
})
@XmlRootElement(name = "TransportProgressTransgression")
public class TransportProgressTransgression {

    @XmlElement(required = true)
    protected BigInteger operatorId;
    protected double latitude;
    protected double longitude;
    protected long distanceRemaining;
    protected long distanceCovered;
    protected long timeElapsed;
    protected long timeRemaining;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar plannedETA;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar predictedETA;
    protected long predictedDelay;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar timestamp;

    /**
     * Gets the value of the operatorId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getOperatorId() {
        return operatorId;
    }

    /**
     * Sets the value of the operatorId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setOperatorId(BigInteger value) {
        this.operatorId = value;
    }

    /**
     * Gets the value of the latitude property.
     * 
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the value of the latitude property.
     * 
     */
    public void setLatitude(double value) {
        this.latitude = value;
    }

    /**
     * Gets the value of the longitude property.
     * 
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the value of the longitude property.
     * 
     */
    public void setLongitude(double value) {
        this.longitude = value;
    }

    /**
     * Gets the value of the distanceRemaining property.
     * 
     */
    public long getDistanceRemaining() {
        return distanceRemaining;
    }

    /**
     * Sets the value of the distanceRemaining property.
     * 
     */
    public void setDistanceRemaining(long value) {
        this.distanceRemaining = value;
    }

    /**
     * Gets the value of the distanceCovered property.
     * 
     */
    public long getDistanceCovered() {
        return distanceCovered;
    }

    /**
     * Sets the value of the distanceCovered property.
     * 
     */
    public void setDistanceCovered(long value) {
        this.distanceCovered = value;
    }

    /**
     * Gets the value of the timeElapsed property.
     * 
     */
    public long getTimeElapsed() {
        return timeElapsed;
    }

    /**
     * Sets the value of the timeElapsed property.
     * 
     */
    public void setTimeElapsed(long value) {
        this.timeElapsed = value;
    }

    /**
     * Gets the value of the timeRemaining property.
     * 
     */
    public long getTimeRemaining() {
        return timeRemaining;
    }

    /**
     * Sets the value of the timeRemaining property.
     * 
     */
    public void setTimeRemaining(long value) {
        this.timeRemaining = value;
    }

    /**
     * Gets the value of the plannedETA property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPlannedETA() {
        return plannedETA;
    }

    /**
     * Sets the value of the plannedETA property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPlannedETA(XMLGregorianCalendar value) {
        this.plannedETA = value;
    }

    /**
     * Gets the value of the predictedETA property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPredictedETA() {
        return predictedETA;
    }

    /**
     * Sets the value of the predictedETA property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPredictedETA(XMLGregorianCalendar value) {
        this.predictedETA = value;
    }

    /**
     * Gets the value of the predictedDelay property.
     * 
     */
    public long getPredictedDelay() {
        return predictedDelay;
    }

    /**
     * Sets the value of the predictedDelay property.
     * 
     */
    public void setPredictedDelay(long value) {
        this.predictedDelay = value;
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

}

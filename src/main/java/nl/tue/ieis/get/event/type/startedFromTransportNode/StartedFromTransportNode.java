package main.java.nl.tue.ieis.get.event.type.startedFromTransportNode;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "latitude",
    "longitude",
    "nodeId",
    "nodeName",
    "operatorId",
    "timestamp"
})
@XmlRootElement(name = "StartedFromTransportNode")
public class StartedFromTransportNode {

    protected double latitude;
    protected double longitude;
    @XmlElement(required = true)
    protected BigInteger nodeId;
    @XmlElement(required = true)
    protected String nodeName;
    @XmlElement(required = true)
    protected BigInteger operatorId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected String timestamp;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double value) {
        this.latitude = value;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double value) {
        this.longitude = value;
    }

    public BigInteger getNodeId() {
        return nodeId;
    }

    public void setNodeId(BigInteger value) {
        this.nodeId = value;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String value) {
        this.nodeName = value;
    }

    public BigInteger getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(BigInteger value) {
        this.operatorId = value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String value) {
        this.timestamp = value;
    }

}

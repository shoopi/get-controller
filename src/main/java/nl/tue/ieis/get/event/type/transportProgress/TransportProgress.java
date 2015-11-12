
package main.java.nl.tue.ieis.get.event.type.transportProgress;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "operatorId",
    "latitude",
    "longitude",
    "distanceRemaining",
    "distanceCovered",
    "timeElapsed",
    "timeRemaining",
    "timestamp"
})
@XmlRootElement(name = "TransportProgress")
public class TransportProgress {

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
    protected String timestamp;

    public BigInteger getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(BigInteger value) {
        this.operatorId = value;
    }

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

    public long getDistanceRemaining() {
        return distanceRemaining;
    }

    public void setDistanceRemaining(long value) {
        this.distanceRemaining = value;
    }

    public long getDistanceCovered() {
        return distanceCovered;
    }

    public void setDistanceCovered(long value) {
        this.distanceCovered = value;
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(long value) {
        this.timeElapsed = value;
    }

    public long getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(long value) {
        this.timeRemaining = value;
    }
    
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String value) {
        this.timestamp = value;
    }

}

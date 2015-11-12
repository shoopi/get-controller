package main.java.nl.tue.ieis.get.event.type.vehicleLocation;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "mobOperID",
    "latitude",
    "longitude",
    "altitude",
    "timestamp"
})
@XmlRootElement(name = "VehicleLocationEvent")
public class VehicleLocationEvent {

    @XmlElement(required = true)
    protected BigInteger mobOperID;
    @XmlElement(required = true)
    protected String latitude;
    @XmlElement(required = true)
    protected String longitude;
    @XmlElement(required = true)
    protected String altitude;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected String timestamp;

    public BigInteger getMobOperID() {
        return mobOperID;
    }

    public void setMobOperID(BigInteger value) {
        this.mobOperID = value;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String value) {
        this.latitude = value;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String value) {
        this.longitude = value;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String value) {
        this.altitude = value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String value) {
        this.timestamp = value;
    }
}

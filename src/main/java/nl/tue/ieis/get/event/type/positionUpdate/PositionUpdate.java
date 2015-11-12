package main.java.nl.tue.ieis.get.event.type.positionUpdate;

import java.math.BigInteger;
/*
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "operatorId",
    "currentLatitude",
    "currentLongitude",
    "previousLatitude",
    "previousLongitude",
    "distance",
    "duration",
    "speed",
    "timestamp",
    "startTime",
    "mode"
})
*/
//@XmlRootElement(name = "PositionUpdate")
public class PositionUpdate {

  //  @XmlElement(required = true)
    protected BigInteger operatorId;
    protected double currentLatitude;
    protected double currentLongitude;
    protected double previousLatitude;
    protected double previousLongitude;
    protected long distance;
    protected long duration;
    protected long speed;
   // @XmlElement(required = true)
    //@XmlSchemaType(name = "dateTime")
    //protected XMLGregorianCalendar timestamp;
    protected String timestamp;

   // @XmlElement(required = true)
    //@XmlSchemaType(name = "dateTime")
    //protected XMLGregorianCalendar startTime;
    protected String startTime;
    protected String mode;
    protected String eta;
    
    


	public String getEta() {
		return eta;
	}


	public void setEta(String eta) {
		this.eta = eta;
	}


	public String getMode() {
		return mode;
	}


	public void setMode(String mode) {
		this.mode = mode;
	}


	public BigInteger getOperatorId() {
        return operatorId;
    }

    
    public void setOperatorId(BigInteger value) {
        this.operatorId = value;
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(double value) {
        this.currentLatitude = value;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(double value) {
        this.currentLongitude = value;
    }

    public double getPreviousLatitude() {
        return previousLatitude;
    }


    public void setPreviousLatitude(double value) {
        this.previousLatitude = value;
    }

    public double getPreviousLongitude() {
        return previousLongitude;
    }

    public void setPreviousLongitude(double value) {
        this.previousLongitude = value;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long value) {
        this.distance = value;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long value) {
        this.duration = value;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long value) {
        this.speed = value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String value) {
        this.timestamp = value;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String value) {
        this.startTime = value;
    }
}

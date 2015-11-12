package main.java.nl.jdr.traces;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "trace"
})
@XmlRootElement(name = "traces")
public class Traces {

    @XmlElement(required = true)
    protected List<Trace> trace;

    public List<Trace> getTrace() {
        if (trace == null) {
            trace = new ArrayList<Trace>();
        }
        return this.trace;
    }
    
    public void setTrace(List<Trace> trace) {
    	this.trace = trace;
    }
    
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "type",
        "source",
        "time",
        "coordinate",
        "mileage",
        "heading",
        "speed",
        "property"
    })
    public static class Trace {

        protected int type;
        protected int source;
        @XmlElement(required = true)
        protected Date time;
        @XmlElement(required = true)
        protected Coordinate coordinate;
        protected int mileage;
        protected int heading;
        protected int speed;
        @XmlElement(required = true)
        protected List<Property> property;

        public int getType() {
            return type;
        }

        public void setType(int value) {
            this.type = value;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int value) {
            this.source = value;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(String value) {
        	//2013-04-30T23:59:01.067
        	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        	try {
				formatter.parse(value);
			} catch (ParseException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
        	//DateFormatter  format = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
            //this.time = format.parseDateTime(value);
        	//this.time = DateTime.parse(value);
        }
        
        public Coordinate getCoordinate() {
            return coordinate;
        }

        public void setCoordinate(Coordinate value) {
            this.coordinate = value;
        }

        public int getMileage() {
            return mileage;
        }

        public void setMileage(int value) {
            this.mileage = value;
        }

        public int getHeading() {
            return heading;
        }

        public void setHeading(int value) {
            this.heading = value;
        }

        public int getSpeed() {
            return speed;
        }

        public void setSpeed(int value) {
            this.speed = value;
        }

        public List<Property> getProperty() {
            if (property == null) {
                property = new ArrayList<Property>();
            }
            return this.property;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "latitude",
            "longitude"
        })
        public static class Coordinate {

            @XmlElement(required = true)
            protected BigDecimal latitude;
            @XmlElement(required = true)
            protected BigDecimal longitude;

            public BigDecimal getLatitude() {
                return latitude;
            }

            public void setLatitude(BigDecimal value) {
                this.latitude = value;
            }

            public BigDecimal getLongitude() {
                return longitude;
            }

            public void setLongitude(BigDecimal value) {
                this.longitude = value;
            }

        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "key",
            "value"
        })
        public static class Property {

            @XmlElement(required = true)
            protected String key;
            protected String value;
            
            public String getKey() {
                return key;
            }

            public void setKey(String value) {
                this.key = value;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

        }

    }

}

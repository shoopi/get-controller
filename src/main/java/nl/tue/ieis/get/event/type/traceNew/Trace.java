//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.06.02 at 02:58:15 PM CEST 
//


package main.java.nl.tue.ieis.get.event.type.traceNew;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="source" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="time" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mileage" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="heading" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="speed" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="property">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="addressmatch">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="coordinate">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="latitude" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="longitude" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="address">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="street" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="city" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="zipcode" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                             &lt;element name="area" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="country" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="quality" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "type",
    "source",
    "time",
    "mileage",
    "heading",
    "speed",
    "property",
    "addressmatch"
})
@XmlRootElement(name = "trace")
public class Trace {

    @XmlElement(required = true)
    protected BigInteger type;
    @XmlElement(required = true)
    protected BigInteger source;
    @XmlElement(required = true)
    protected String time;
    @XmlElement(required = true)
    protected BigInteger mileage;
    @XmlElement(required = true)
    protected BigInteger heading;
    @XmlElement(required = true)
    protected BigInteger speed;
    @XmlElement(required = true)
    protected Trace.Property property;
    @XmlElement(required = true)
    protected Trace.Addressmatch addressmatch;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setType(BigInteger value) {
        this.type = value;
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSource(BigInteger value) {
        this.source = value;
    }

    /**
     * Gets the value of the time property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the value of the time property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTime(String value) {
        this.time = value;
    }

    /**
     * Gets the value of the mileage property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMileage() {
        return mileage;
    }

    /**
     * Sets the value of the mileage property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMileage(BigInteger value) {
        this.mileage = value;
    }

    /**
     * Gets the value of the heading property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getHeading() {
        return heading;
    }

    /**
     * Sets the value of the heading property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setHeading(BigInteger value) {
        this.heading = value;
    }

    /**
     * Gets the value of the speed property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSpeed() {
        return speed;
    }

    /**
     * Sets the value of the speed property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSpeed(BigInteger value) {
        this.speed = value;
    }

    /**
     * Gets the value of the property property.
     * 
     * @return
     *     possible object is
     *     {@link Trace.Property }
     *     
     */
    public Trace.Property getProperty() {
        return property;
    }

    /**
     * Sets the value of the property property.
     * 
     * @param value
     *     allowed object is
     *     {@link Trace.Property }
     *     
     */
    public void setProperty(Trace.Property value) {
        this.property = value;
    }

    /**
     * Gets the value of the addressmatch property.
     * 
     * @return
     *     possible object is
     *     {@link Trace.Addressmatch }
     *     
     */
    public Trace.Addressmatch getAddressmatch() {
        return addressmatch;
    }

    /**
     * Sets the value of the addressmatch property.
     * 
     * @param value
     *     allowed object is
     *     {@link Trace.Addressmatch }
     *     
     */
    public void setAddressmatch(Trace.Addressmatch value) {
        this.addressmatch = value;
    }


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
     *         &lt;element name="coordinate">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="latitude" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="longitude" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="address">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="street" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="city" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="zipcode" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *                   &lt;element name="area" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="country" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="quality" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
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
        "coordinate",
        "address",
        "quality"
    })
    public static class Addressmatch {

        @XmlElement(required = true)
        protected Trace.Addressmatch.Coordinate coordinate;
        @XmlElement(required = true)
        protected Trace.Addressmatch.Address address;
        @XmlElement(required = true)
        protected BigDecimal quality;

        /**
         * Gets the value of the coordinate property.
         * 
         * @return
         *     possible object is
         *     {@link Trace.Addressmatch.Coordinate }
         *     
         */
        public Trace.Addressmatch.Coordinate getCoordinate() {
            return coordinate;
        }

        /**
         * Sets the value of the coordinate property.
         * 
         * @param value
         *     allowed object is
         *     {@link Trace.Addressmatch.Coordinate }
         *     
         */
        public void setCoordinate(Trace.Addressmatch.Coordinate value) {
            this.coordinate = value;
        }

        /**
         * Gets the value of the address property.
         * 
         * @return
         *     possible object is
         *     {@link Trace.Addressmatch.Address }
         *     
         */
        public Trace.Addressmatch.Address getAddress() {
            return address;
        }

        /**
         * Sets the value of the address property.
         * 
         * @param value
         *     allowed object is
         *     {@link Trace.Addressmatch.Address }
         *     
         */
        public void setAddress(Trace.Addressmatch.Address value) {
            this.address = value;
        }

        /**
         * Gets the value of the quality property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getQuality() {
            return quality;
        }

        /**
         * Sets the value of the quality property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setQuality(BigDecimal value) {
            this.quality = value;
        }


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
         *         &lt;element name="street" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="city" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="zipcode" type="{http://www.w3.org/2001/XMLSchema}integer"/>
         *         &lt;element name="area" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="country" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
            "street",
            "city",
            "zipcode",
            "area",
            "country"
        })
        public static class Address {

            @XmlElement(required = true)
            protected String street;
            @XmlElement(required = true)
            protected String city;
            @XmlElement(required = true)
            protected BigInteger zipcode;
            @XmlElement(required = true)
            protected String area;
            @XmlElement(required = true)
            protected String country;

            /**
             * Gets the value of the street property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getStreet() {
                return street;
            }

            /**
             * Sets the value of the street property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setStreet(String value) {
                this.street = value;
            }

            /**
             * Gets the value of the city property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCity() {
                return city;
            }

            /**
             * Sets the value of the city property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCity(String value) {
                this.city = value;
            }

            /**
             * Gets the value of the zipcode property.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getZipcode() {
                return zipcode;
            }

            /**
             * Sets the value of the zipcode property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setZipcode(BigInteger value) {
                this.zipcode = value;
            }

            /**
             * Gets the value of the area property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getArea() {
                return area;
            }

            /**
             * Sets the value of the area property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setArea(String value) {
                this.area = value;
            }

            /**
             * Gets the value of the country property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCountry() {
                return country;
            }

            /**
             * Sets the value of the country property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCountry(String value) {
                this.country = value;
            }

        }


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
         *         &lt;element name="latitude" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="longitude" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
            "latitude",
            "longitude"
        })
        public static class Coordinate {

            @XmlElement(required = true)
            protected String latitude;
            @XmlElement(required = true)
            protected String longitude;

            /**
             * Gets the value of the latitude property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getLatitude() {
                return latitude;
            }

            /**
             * Sets the value of the latitude property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setLatitude(String value) {
                this.latitude = value;
            }

            /**
             * Gets the value of the longitude property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getLongitude() {
                return longitude;
            }

            /**
             * Sets the value of the longitude property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setLongitude(String value) {
                this.longitude = value;
            }

        }

    }


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
     *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}integer"/>
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
        "key",
        "value"
    })
    public static class Property {

        @XmlElement(required = true)
        protected String key;
        @XmlElement(required = true)
        protected String value;

        /**
         * Gets the value of the key property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getKey() {
            return key;
        }

        /**
         * Sets the value of the key property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setKey(String value) {
            this.key = value;
        }

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

    }

}

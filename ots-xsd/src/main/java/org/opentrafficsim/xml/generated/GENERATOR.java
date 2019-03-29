//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.03.29 at 05:02:28 PM CET 
//


package org.opentrafficsim.xml.generated;

import java.io.Serializable;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.djunits.value.vdouble.scalar.Time;
import org.opentrafficsim.xml.bindings.LengthBeginEndAdapter;
import org.opentrafficsim.xml.bindings.TimeAdapter;
import org.opentrafficsim.xml.bindings.types.LengthBeginEnd;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice&gt;
 *           &lt;element name="GTU" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *           &lt;element name="GTUMIX" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;/choice&gt;
 *         &lt;choice&gt;
 *           &lt;element name="ROUTE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *           &lt;element name="ROUTEMIX" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *           &lt;element name="SHORTESTROUTE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *           &lt;element name="SHORTESTROUTEMIX" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="IAT" type="{http://www.opentrafficsim.org/ots}DURATIONDISTTYPE"/&gt;
 *         &lt;element name="STARTTIME" type="{http://www.opentrafficsim.org/ots}TIMETYPE" minOccurs="0"/&gt;
 *         &lt;element name="ENDTIME" type="{http://www.opentrafficsim.org/ots}TIMETYPE" minOccurs="0"/&gt;
 *         &lt;element name="ROOMCHECKER" type="{http://www.opentrafficsim.org/ots}ROOMCHECKERTYPE" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="LINK" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="LANE" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="DIRECTION" use="required" type="{http://www.opentrafficsim.org/ots}GTUDIRECTIONTYPE" /&gt;
 *       &lt;attribute name="POSITION" use="required" type="{http://www.opentrafficsim.org/ots}LENGTHBEGINENDTYPE" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "gtu",
    "gtumix",
    "route",
    "routemix",
    "shortestroute",
    "shortestroutemix",
    "iat",
    "starttime",
    "endtime",
    "roomchecker"
})
@XmlRootElement(name = "GENERATOR")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
public class GENERATOR
    implements Serializable
{

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    private final static long serialVersionUID = 10102L;
    @XmlElement(name = "GTU")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    protected String gtu;
    @XmlElement(name = "GTUMIX")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    protected String gtumix;
    @XmlElement(name = "ROUTE")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    protected String route;
    @XmlElement(name = "ROUTEMIX")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    protected String routemix;
    @XmlElement(name = "SHORTESTROUTE")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    protected String shortestroute;
    @XmlElement(name = "SHORTESTROUTEMIX")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    protected String shortestroutemix;
    @XmlElement(name = "IAT", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    protected DURATIONDISTTYPE iat;
    @XmlElement(name = "STARTTIME", type = String.class)
    @XmlJavaTypeAdapter(TimeAdapter.class)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    protected Time starttime;
    @XmlElement(name = "ENDTIME", type = String.class)
    @XmlJavaTypeAdapter(TimeAdapter.class)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    protected Time endtime;
    @XmlElement(name = "ROOMCHECKER", defaultValue = "CF")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    protected String roomchecker;
    @XmlAttribute(name = "LINK", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    protected String link;
    @XmlAttribute(name = "LANE", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    protected String lane;
    @XmlAttribute(name = "DIRECTION", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    protected String direction;
    @XmlAttribute(name = "POSITION", required = true)
    @XmlJavaTypeAdapter(LengthBeginEndAdapter.class)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    protected LengthBeginEnd position;

    /**
     * Gets the value of the gtu property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public String getGTU() {
        return gtu;
    }

    /**
     * Sets the value of the gtu property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public void setGTU(String value) {
        this.gtu = value;
    }

    /**
     * Gets the value of the gtumix property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public String getGTUMIX() {
        return gtumix;
    }

    /**
     * Sets the value of the gtumix property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public void setGTUMIX(String value) {
        this.gtumix = value;
    }

    /**
     * Gets the value of the route property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public String getROUTE() {
        return route;
    }

    /**
     * Sets the value of the route property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public void setROUTE(String value) {
        this.route = value;
    }

    /**
     * Gets the value of the routemix property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public String getROUTEMIX() {
        return routemix;
    }

    /**
     * Sets the value of the routemix property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public void setROUTEMIX(String value) {
        this.routemix = value;
    }

    /**
     * Gets the value of the shortestroute property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public String getSHORTESTROUTE() {
        return shortestroute;
    }

    /**
     * Sets the value of the shortestroute property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public void setSHORTESTROUTE(String value) {
        this.shortestroute = value;
    }

    /**
     * Gets the value of the shortestroutemix property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public String getSHORTESTROUTEMIX() {
        return shortestroutemix;
    }

    /**
     * Sets the value of the shortestroutemix property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public void setSHORTESTROUTEMIX(String value) {
        this.shortestroutemix = value;
    }

    /**
     * Gets the value of the iat property.
     * 
     * @return
     *     possible object is
     *     {@link DURATIONDISTTYPE }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public DURATIONDISTTYPE getIAT() {
        return iat;
    }

    /**
     * Sets the value of the iat property.
     * 
     * @param value
     *     allowed object is
     *     {@link DURATIONDISTTYPE }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public void setIAT(DURATIONDISTTYPE value) {
        this.iat = value;
    }

    /**
     * Gets the value of the starttime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public Time getSTARTTIME() {
        return starttime;
    }

    /**
     * Sets the value of the starttime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public void setSTARTTIME(Time value) {
        this.starttime = value;
    }

    /**
     * Gets the value of the endtime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public Time getENDTIME() {
        return endtime;
    }

    /**
     * Sets the value of the endtime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public void setENDTIME(Time value) {
        this.endtime = value;
    }

    /**
     * Gets the value of the roomchecker property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public String getROOMCHECKER() {
        return roomchecker;
    }

    /**
     * Sets the value of the roomchecker property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public void setROOMCHECKER(String value) {
        this.roomchecker = value;
    }

    /**
     * Gets the value of the link property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public String getLINK() {
        return link;
    }

    /**
     * Sets the value of the link property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public void setLINK(String value) {
        this.link = value;
    }

    /**
     * Gets the value of the lane property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public String getLANE() {
        return lane;
    }

    /**
     * Sets the value of the lane property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public void setLANE(String value) {
        this.lane = value;
    }

    /**
     * Gets the value of the direction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public String getDIRECTION() {
        return direction;
    }

    /**
     * Sets the value of the direction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public void setDIRECTION(String value) {
        this.direction = value;
    }

    /**
     * Gets the value of the position property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public LengthBeginEnd getPOSITION() {
        return position;
    }

    /**
     * Sets the value of the position property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-29T05:02:28+01:00", comments = "JAXB RI v2.3.0")
    public void setPOSITION(LengthBeginEnd value) {
        this.position = value;
    }

}

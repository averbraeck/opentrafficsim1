//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.04.22 at 09:43:59 PM CEST 
//


package org.opentrafficsim.xml.generated;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.opentrafficsim.xml.bindings.LaneKeepingAdapter;
import org.opentrafficsim.xml.bindings.types.LaneKeepingType;


/**
 * <p>Java class for BASICROADLAYOUT complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BASICROADLAYOUT"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;sequence maxOccurs="unbounded"&gt;
 *           &lt;choice&gt;
 *             &lt;element name="LANE" type="{http://www.opentrafficsim.org/ots}CSELANE" maxOccurs="unbounded" minOccurs="0"/&gt;
 *             &lt;element name="NOTRAFFICLANE" type="{http://www.opentrafficsim.org/ots}CSENOTRAFFICLANE" maxOccurs="unbounded" minOccurs="0"/&gt;
 *             &lt;element name="SHOULDER" type="{http://www.opentrafficsim.org/ots}CSESHOULDER" maxOccurs="unbounded" minOccurs="0"/&gt;
 *             &lt;element name="STRIPE" type="{http://www.opentrafficsim.org/ots}CSESTRIPE" maxOccurs="unbounded" minOccurs="0"/&gt;
 *           &lt;/choice&gt;
 *         &lt;/sequence&gt;
 *         &lt;element ref="{http://www.opentrafficsim.org/ots}SPEEDLIMIT" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="LANEKEEPING" type="{http://www.opentrafficsim.org/ots}LANEKEEPINGTYPE" /&gt;
 *       &lt;attribute ref="{http://www.w3.org/XML/1998/namespace}base"/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BASICROADLAYOUT", propOrder = {
    "laneOrNOTRAFFICLANEOrSHOULDER",
    "speedlimit"
})
@XmlSeeAlso({
    org.opentrafficsim.xml.generated.LINK.ROADLAYOUT.class,
    org.opentrafficsim.xml.generated.ROADLAYOUT.class
})
@Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-22T09:43:59+02:00", comments = "JAXB RI v2.3.0")
public class BASICROADLAYOUT
    implements Serializable
{

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-22T09:43:59+02:00", comments = "JAXB RI v2.3.0")
    private final static long serialVersionUID = 10102L;
    @XmlElements({
        @XmlElement(name = "LANE", type = CSELANE.class),
        @XmlElement(name = "NOTRAFFICLANE", type = CSENOTRAFFICLANE.class),
        @XmlElement(name = "SHOULDER", type = CSESHOULDER.class),
        @XmlElement(name = "STRIPE", type = CSESTRIPE.class)
    })
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-22T09:43:59+02:00", comments = "JAXB RI v2.3.0")
    protected List<Serializable> laneOrNOTRAFFICLANEOrSHOULDER;
    @XmlElement(name = "SPEEDLIMIT")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-22T09:43:59+02:00", comments = "JAXB RI v2.3.0")
    protected List<SPEEDLIMIT> speedlimit;
    @XmlAttribute(name = "LANEKEEPING")
    @XmlJavaTypeAdapter(LaneKeepingAdapter.class)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-22T09:43:59+02:00", comments = "JAXB RI v2.3.0")
    protected LaneKeepingType lanekeeping;
    @XmlAttribute(name = "base", namespace = "http://www.w3.org/XML/1998/namespace")
    @XmlSchemaType(name = "anyURI")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-22T09:43:59+02:00", comments = "JAXB RI v2.3.0")
    protected String base;

    /**
     * Gets the value of the laneOrNOTRAFFICLANEOrSHOULDER property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the laneOrNOTRAFFICLANEOrSHOULDER property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLANEOrNOTRAFFICLANEOrSHOULDER().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CSELANE }
     * {@link CSENOTRAFFICLANE }
     * {@link CSESHOULDER }
     * {@link CSESTRIPE }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-22T09:43:59+02:00", comments = "JAXB RI v2.3.0")
    public List<Serializable> getLANEOrNOTRAFFICLANEOrSHOULDER() {
        if (laneOrNOTRAFFICLANEOrSHOULDER == null) {
            laneOrNOTRAFFICLANEOrSHOULDER = new ArrayList<Serializable>();
        }
        return this.laneOrNOTRAFFICLANEOrSHOULDER;
    }

    /**
     * Gets the value of the speedlimit property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the speedlimit property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSPEEDLIMIT().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SPEEDLIMIT }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-22T09:43:59+02:00", comments = "JAXB RI v2.3.0")
    public List<SPEEDLIMIT> getSPEEDLIMIT() {
        if (speedlimit == null) {
            speedlimit = new ArrayList<SPEEDLIMIT>();
        }
        return this.speedlimit;
    }

    /**
     * Gets the value of the lanekeeping property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-22T09:43:59+02:00", comments = "JAXB RI v2.3.0")
    public LaneKeepingType getLANEKEEPING() {
        return lanekeeping;
    }

    /**
     * Sets the value of the lanekeeping property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-22T09:43:59+02:00", comments = "JAXB RI v2.3.0")
    public void setLANEKEEPING(LaneKeepingType value) {
        this.lanekeeping = value;
    }

    /**
     * Gets the value of the base property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-22T09:43:59+02:00", comments = "JAXB RI v2.3.0")
    public String getBase() {
        return base;
    }

    /**
     * Sets the value of the base property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-22T09:43:59+02:00", comments = "JAXB RI v2.3.0")
    public void setBase(String value) {
        this.base = value;
    }

}
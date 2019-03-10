//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.03.10 at 04:40:02 AM CET 
//


package org.opentrafficsim.xml.generated;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Speed;
import org.opentrafficsim.xml.bindings.LaneKeepingAdapter;
import org.opentrafficsim.xml.bindings.LengthAdapter;
import org.opentrafficsim.xml.bindings.SpeedAdapter;
import org.opentrafficsim.xml.bindings.types.LaneKeepingType;


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
 *         &lt;element name="SPEEDLIMIT" maxOccurs="unbounded"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute name="GTUTYPE" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                 &lt;attribute name="LEGALSPEEDLIMIT" use="required" type="{http://www.opentrafficsim.org/ots}SPEEDTYPE" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="NAME" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="DEFAULTLANEWIDTH" use="required" type="{http://www.opentrafficsim.org/ots}LENGTHTYPE" /&gt;
 *       &lt;attribute name="DEFAULTLANEKEEPING" use="required" type="{http://www.opentrafficsim.org/ots}LANEKEEPINGTYPE" /&gt;
 *       &lt;attribute name="DEFAULTOVERTAKING" use="required" type="{http://www.opentrafficsim.org/ots}OVERTAKINGTYPE" /&gt;
 *       &lt;attribute ref="{http://www.w3.org/XML/1998/namespace}base"/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "speedlimit"
})
@XmlRootElement(name = "ROADTYPE")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
public class ROADTYPE {

    @XmlElement(name = "SPEEDLIMIT", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
    protected List<ROADTYPE.SPEEDLIMIT> speedlimit;
    @XmlAttribute(name = "NAME", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
    protected String name;
    @XmlAttribute(name = "DEFAULTLANEWIDTH", required = true)
    @XmlJavaTypeAdapter(LengthAdapter.class)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
    protected Length defaultlanewidth;
    @XmlAttribute(name = "DEFAULTLANEKEEPING", required = true)
    @XmlJavaTypeAdapter(LaneKeepingAdapter.class)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
    protected LaneKeepingType defaultlanekeeping;
    @XmlAttribute(name = "DEFAULTOVERTAKING", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
    protected String defaultovertaking;
    @XmlAttribute(name = "base", namespace = "http://www.w3.org/XML/1998/namespace")
    @XmlSchemaType(name = "anyURI")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
    protected String base;

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
     * {@link ROADTYPE.SPEEDLIMIT }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
    public List<ROADTYPE.SPEEDLIMIT> getSPEEDLIMIT() {
        if (speedlimit == null) {
            speedlimit = new ArrayList<ROADTYPE.SPEEDLIMIT>();
        }
        return this.speedlimit;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
    public String getNAME() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
    public void setNAME(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the defaultlanewidth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
    public Length getDEFAULTLANEWIDTH() {
        return defaultlanewidth;
    }

    /**
     * Sets the value of the defaultlanewidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
    public void setDEFAULTLANEWIDTH(Length value) {
        this.defaultlanewidth = value;
    }

    /**
     * Gets the value of the defaultlanekeeping property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
    public LaneKeepingType getDEFAULTLANEKEEPING() {
        return defaultlanekeeping;
    }

    /**
     * Sets the value of the defaultlanekeeping property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
    public void setDEFAULTLANEKEEPING(LaneKeepingType value) {
        this.defaultlanekeeping = value;
    }

    /**
     * Gets the value of the defaultovertaking property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
    public String getDEFAULTOVERTAKING() {
        return defaultovertaking;
    }

    /**
     * Sets the value of the defaultovertaking property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
    public void setDEFAULTOVERTAKING(String value) {
        this.defaultovertaking = value;
    }

    /**
     * Gets the value of the base property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
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
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
    public void setBase(String value) {
        this.base = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;attribute name="GTUTYPE" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *       &lt;attribute name="LEGALSPEEDLIMIT" use="required" type="{http://www.opentrafficsim.org/ots}SPEEDTYPE" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
    public static class SPEEDLIMIT {

        @XmlAttribute(name = "GTUTYPE", required = true)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
        protected String gtutype;
        @XmlAttribute(name = "LEGALSPEEDLIMIT", required = true)
        @XmlJavaTypeAdapter(SpeedAdapter.class)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
        protected Speed legalspeedlimit;

        /**
         * Gets the value of the gtutype property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
        public String getGTUTYPE() {
            return gtutype;
        }

        /**
         * Sets the value of the gtutype property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
        public void setGTUTYPE(String value) {
            this.gtutype = value;
        }

        /**
         * Gets the value of the legalspeedlimit property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
        public Speed getLEGALSPEEDLIMIT() {
            return legalspeedlimit;
        }

        /**
         * Sets the value of the legalspeedlimit property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-10T04:40:02+01:00", comments = "JAXB RI v2.3.0")
        public void setLEGALSPEEDLIMIT(Speed value) {
            this.legalspeedlimit = value;
        }

    }

}

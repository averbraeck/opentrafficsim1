//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.03.01 at 04:16:12 PM CET 
//


package org.opentrafficsim.xml.generated;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="NAME" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="GTUTYPE" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="LENGTH" use="required" type="{http://www.opentrafficsim.org/ots}LENGTHDISTTYPE" /&gt;
 *       &lt;attribute name="WIDTH" use="required" type="{http://www.opentrafficsim.org/ots}LENGTHDISTTYPE" /&gt;
 *       &lt;attribute name="MAXSPEED" use="required" type="{http://www.opentrafficsim.org/ots}SPEEDDISTTYPE" /&gt;
 *       &lt;attribute ref="{http://www.w3.org/XML/1998/namespace}base"/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "GTU")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
public class GTU {

    @XmlAttribute(name = "NAME", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    protected String name;
    @XmlAttribute(name = "GTUTYPE", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    protected String gtutype;
    @XmlAttribute(name = "LENGTH", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    protected String length;
    @XmlAttribute(name = "WIDTH", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    protected String width;
    @XmlAttribute(name = "MAXSPEED", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    protected String maxspeed;
    @XmlAttribute(name = "base", namespace = "http://www.w3.org/XML/1998/namespace")
    @XmlSchemaType(name = "anyURI")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    protected String base;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
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
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    public void setNAME(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the gtutype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
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
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    public void setGTUTYPE(String value) {
        this.gtutype = value;
    }

    /**
     * Gets the value of the length property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    public String getLENGTH() {
        return length;
    }

    /**
     * Sets the value of the length property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    public void setLENGTH(String value) {
        this.length = value;
    }

    /**
     * Gets the value of the width property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    public String getWIDTH() {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    public void setWIDTH(String value) {
        this.width = value;
    }

    /**
     * Gets the value of the maxspeed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    public String getMAXSPEED() {
        return maxspeed;
    }

    /**
     * Sets the value of the maxspeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    public void setMAXSPEED(String value) {
        this.maxspeed = value;
    }

    /**
     * Gets the value of the base property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
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
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    public void setBase(String value) {
        this.base = value;
    }

}

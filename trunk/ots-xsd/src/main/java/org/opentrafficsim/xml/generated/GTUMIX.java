//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.03.01 at 04:16:12 PM CET 
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
 *         &lt;element name="GTU" maxOccurs="unbounded"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute name="NAME" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                 &lt;attribute name="WEIGHT" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="NAME" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
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
    "gtu"
})
@XmlRootElement(name = "GTUMIX")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
public class GTUMIX {

    @XmlElement(name = "GTU", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    protected List<GTUMIX.GTU> gtu;
    @XmlAttribute(name = "NAME", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    protected String name;
    @XmlAttribute(name = "base", namespace = "http://www.w3.org/XML/1998/namespace")
    @XmlSchemaType(name = "anyURI")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    protected String base;

    /**
     * Gets the value of the gtu property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the gtu property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGTU().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GTUMIX.GTU }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    public List<GTUMIX.GTU> getGTU() {
        if (gtu == null) {
            gtu = new ArrayList<GTUMIX.GTU>();
        }
        return this.gtu;
    }

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
     *       &lt;attribute name="WEIGHT" use="required" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    public static class GTU {

        @XmlAttribute(name = "NAME", required = true)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
        protected String name;
        @XmlAttribute(name = "WEIGHT", required = true)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
        protected double weight;

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
         * Gets the value of the weight property.
         * 
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
        public double getWEIGHT() {
            return weight;
        }

        /**
         * Sets the value of the weight property.
         * 
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
        public void setWEIGHT(double value) {
            this.weight = value;
        }

    }

}

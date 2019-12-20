//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.12.19 at 02:14:27 PM CET 
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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.djunits.value.vdouble.scalar.Time;
import org.opentrafficsim.xml.bindings.TimeAdapter;


/**
 * <p>Java class for CONTROLTYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CONTROLTYPE"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SIGNALGROUP" maxOccurs="unbounded"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="TRAFFICLIGHT" maxOccurs="unbounded"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;attribute name="LINK" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                           &lt;attribute name="LANE" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                           &lt;attribute name="TRAFFICLIGHTID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *                 &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="STARTTIME" type="{http://www.opentrafficsim.org/ots}TIMETYPE" /&gt;
 *       &lt;attribute name="ENDTIME" type="{http://www.opentrafficsim.org/ots}TIMETYPE" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CONTROLTYPE", propOrder = {
    "signalgroup"
})
@XmlSeeAlso({
    org.opentrafficsim.xml.generated.CONTROL.FIXEDTIME.class,
    RESPONSIVECONTROLTYPE.class
})
@Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
public class CONTROLTYPE
    implements Serializable
{

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
    private final static long serialVersionUID = 10102L;
    @XmlElement(name = "SIGNALGROUP", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
    protected List<CONTROLTYPE.SIGNALGROUP> signalgroup;
    @XmlAttribute(name = "ID", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
    protected String id;
    @XmlAttribute(name = "STARTTIME")
    @XmlJavaTypeAdapter(TimeAdapter.class)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
    protected Time starttime;
    @XmlAttribute(name = "ENDTIME")
    @XmlJavaTypeAdapter(TimeAdapter.class)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
    protected Time endtime;

    /**
     * Gets the value of the signalgroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signalgroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSIGNALGROUP().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CONTROLTYPE.SIGNALGROUP }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
    public List<CONTROLTYPE.SIGNALGROUP> getSIGNALGROUP() {
        if (signalgroup == null) {
            signalgroup = new ArrayList<CONTROLTYPE.SIGNALGROUP>();
        }
        return this.signalgroup;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the starttime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
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
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
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
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
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
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
    public void setENDTIME(Time value) {
        this.endtime = value;
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
     *       &lt;sequence&gt;
     *         &lt;element name="TRAFFICLIGHT" maxOccurs="unbounded"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;attribute name="LINK" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                 &lt;attribute name="LANE" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                 &lt;attribute name="TRAFFICLIGHTID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *       &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "trafficlight"
    })
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
    public static class SIGNALGROUP
        implements Serializable
    {

        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
        private final static long serialVersionUID = 10102L;
        @XmlElement(name = "TRAFFICLIGHT", required = true)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
        protected List<CONTROLTYPE.SIGNALGROUP.TRAFFICLIGHT> trafficlight;
        @XmlAttribute(name = "ID", required = true)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
        protected String id;

        /**
         * Gets the value of the trafficlight property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the trafficlight property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTRAFFICLIGHT().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CONTROLTYPE.SIGNALGROUP.TRAFFICLIGHT }
         * 
         * 
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
        public List<CONTROLTYPE.SIGNALGROUP.TRAFFICLIGHT> getTRAFFICLIGHT() {
            if (trafficlight == null) {
                trafficlight = new ArrayList<CONTROLTYPE.SIGNALGROUP.TRAFFICLIGHT>();
            }
            return this.trafficlight;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
        public String getID() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
        public void setID(String value) {
            this.id = value;
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
         *       &lt;attribute name="LINK" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *       &lt;attribute name="LANE" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *       &lt;attribute name="TRAFFICLIGHTID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
        public static class TRAFFICLIGHT
            implements Serializable
        {

            @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
            private final static long serialVersionUID = 10102L;
            @XmlAttribute(name = "LINK", required = true)
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
            protected String link;
            @XmlAttribute(name = "LANE", required = true)
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
            protected String lane;
            @XmlAttribute(name = "TRAFFICLIGHTID", required = true)
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
            protected String trafficlightid;

            /**
             * Gets the value of the link property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
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
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
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
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
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
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
            public void setLANE(String value) {
                this.lane = value;
            }

            /**
             * Gets the value of the trafficlightid property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
            public String getTRAFFICLIGHTID() {
                return trafficlightid;
            }

            /**
             * Sets the value of the trafficlightid property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-12-19T02:14:27+01:00", comments = "JAXB RI v2.3.0")
            public void setTRAFFICLIGHTID(String value) {
                this.trafficlightid = value;
            }

        }

    }

}

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.02.09 at 02:46:07 PM CET 
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
import org.opentrafficsim.xml.bindings.LengthBeginEndAdapter;
import org.opentrafficsim.xml.bindings.types.LengthBeginEnd;


/**
 * <p>Java class for RESPONSIVECONTROLTYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RESPONSIVECONTROLTYPE"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.opentrafficsim.org/ots}CONTROLTYPE"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SENSOR" maxOccurs="unbounded"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;choice&gt;
 *                   &lt;element name="MULTIPLELANE"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="ENTRYLINK" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="ENTRYLANE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="ENTRYPOSITION" type="{http://www.opentrafficsim.org/ots}LENGTHBEGINENDTYPE"/&gt;
 *                             &lt;element name="INTERMEDIATELANES" maxOccurs="unbounded" minOccurs="0"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="LINK" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                                       &lt;element name="LANE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                                     &lt;/sequence&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="EXITLINK" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="EXITLANE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="EXITPOSITION" type="{http://www.opentrafficsim.org/ots}LENGTHBEGINENDTYPE"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="SINGLELANE"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="LINK" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="LANE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="ENTRYPOSITION" type="{http://www.opentrafficsim.org/ots}LENGTHBEGINENDTYPE"/&gt;
 *                             &lt;element name="EXITPOSITION" type="{http://www.opentrafficsim.org/ots}LENGTHBEGINENDTYPE"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/choice&gt;
 *                 &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RESPONSIVECONTROLTYPE", propOrder = {
    "sensor"
})
@XmlSeeAlso({
    org.opentrafficsim.xml.generated.CONTROL.TRAFCOD.class
})
@Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
public class RESPONSIVECONTROLTYPE
    extends CONTROLTYPE
    implements Serializable
{

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
    private final static long serialVersionUID = 10102L;
    @XmlElement(name = "SENSOR", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
    protected List<RESPONSIVECONTROLTYPE.SENSOR> sensor;

    /**
     * Gets the value of the sensor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sensor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSENSOR().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RESPONSIVECONTROLTYPE.SENSOR }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
    public List<RESPONSIVECONTROLTYPE.SENSOR> getSENSOR() {
        if (sensor == null) {
            sensor = new ArrayList<RESPONSIVECONTROLTYPE.SENSOR>();
        }
        return this.sensor;
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
     *       &lt;choice&gt;
     *         &lt;element name="MULTIPLELANE"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="ENTRYLINK" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="ENTRYLANE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="ENTRYPOSITION" type="{http://www.opentrafficsim.org/ots}LENGTHBEGINENDTYPE"/&gt;
     *                   &lt;element name="INTERMEDIATELANES" maxOccurs="unbounded" minOccurs="0"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="LINK" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                             &lt;element name="LANE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="EXITLINK" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="EXITLANE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="EXITPOSITION" type="{http://www.opentrafficsim.org/ots}LENGTHBEGINENDTYPE"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="SINGLELANE"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="LINK" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="LANE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="ENTRYPOSITION" type="{http://www.opentrafficsim.org/ots}LENGTHBEGINENDTYPE"/&gt;
     *                   &lt;element name="EXITPOSITION" type="{http://www.opentrafficsim.org/ots}LENGTHBEGINENDTYPE"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/choice&gt;
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
        "multiplelane",
        "singlelane"
    })
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
    public static class SENSOR
        implements Serializable
    {

        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        private final static long serialVersionUID = 10102L;
        @XmlElement(name = "MULTIPLELANE")
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        protected RESPONSIVECONTROLTYPE.SENSOR.MULTIPLELANE multiplelane;
        @XmlElement(name = "SINGLELANE")
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        protected RESPONSIVECONTROLTYPE.SENSOR.SINGLELANE singlelane;
        @XmlAttribute(name = "ID", required = true)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        protected String id;

        /**
         * Gets the value of the multiplelane property.
         * 
         * @return
         *     possible object is
         *     {@link RESPONSIVECONTROLTYPE.SENSOR.MULTIPLELANE }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        public RESPONSIVECONTROLTYPE.SENSOR.MULTIPLELANE getMULTIPLELANE() {
            return multiplelane;
        }

        /**
         * Sets the value of the multiplelane property.
         * 
         * @param value
         *     allowed object is
         *     {@link RESPONSIVECONTROLTYPE.SENSOR.MULTIPLELANE }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        public void setMULTIPLELANE(RESPONSIVECONTROLTYPE.SENSOR.MULTIPLELANE value) {
            this.multiplelane = value;
        }

        /**
         * Gets the value of the singlelane property.
         * 
         * @return
         *     possible object is
         *     {@link RESPONSIVECONTROLTYPE.SENSOR.SINGLELANE }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        public RESPONSIVECONTROLTYPE.SENSOR.SINGLELANE getSINGLELANE() {
            return singlelane;
        }

        /**
         * Sets the value of the singlelane property.
         * 
         * @param value
         *     allowed object is
         *     {@link RESPONSIVECONTROLTYPE.SENSOR.SINGLELANE }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        public void setSINGLELANE(RESPONSIVECONTROLTYPE.SENSOR.SINGLELANE value) {
            this.singlelane = value;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
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
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
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
         *       &lt;sequence&gt;
         *         &lt;element name="ENTRYLINK" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="ENTRYLANE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="ENTRYPOSITION" type="{http://www.opentrafficsim.org/ots}LENGTHBEGINENDTYPE"/&gt;
         *         &lt;element name="INTERMEDIATELANES" maxOccurs="unbounded" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="LINK" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *                   &lt;element name="LANE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="EXITLINK" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="EXITLANE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="EXITPOSITION" type="{http://www.opentrafficsim.org/ots}LENGTHBEGINENDTYPE"/&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "entrylink",
            "entrylane",
            "entryposition",
            "intermediatelanes",
            "exitlink",
            "exitlane",
            "exitposition"
        })
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        public static class MULTIPLELANE
            implements Serializable
        {

            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            private final static long serialVersionUID = 10102L;
            @XmlElement(name = "ENTRYLINK", required = true)
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            protected String entrylink;
            @XmlElement(name = "ENTRYLANE", required = true)
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            protected String entrylane;
            @XmlElement(name = "ENTRYPOSITION", required = true, type = String.class)
            @XmlJavaTypeAdapter(LengthBeginEndAdapter.class)
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            protected LengthBeginEnd entryposition;
            @XmlElement(name = "INTERMEDIATELANES")
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            protected List<RESPONSIVECONTROLTYPE.SENSOR.MULTIPLELANE.INTERMEDIATELANES> intermediatelanes;
            @XmlElement(name = "EXITLINK", required = true)
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            protected String exitlink;
            @XmlElement(name = "EXITLANE", required = true)
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            protected String exitlane;
            @XmlElement(name = "EXITPOSITION", required = true, type = String.class)
            @XmlJavaTypeAdapter(LengthBeginEndAdapter.class)
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            protected LengthBeginEnd exitposition;

            /**
             * Gets the value of the entrylink property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public String getENTRYLINK() {
                return entrylink;
            }

            /**
             * Sets the value of the entrylink property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public void setENTRYLINK(String value) {
                this.entrylink = value;
            }

            /**
             * Gets the value of the entrylane property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public String getENTRYLANE() {
                return entrylane;
            }

            /**
             * Sets the value of the entrylane property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public void setENTRYLANE(String value) {
                this.entrylane = value;
            }

            /**
             * Gets the value of the entryposition property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public LengthBeginEnd getENTRYPOSITION() {
                return entryposition;
            }

            /**
             * Sets the value of the entryposition property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public void setENTRYPOSITION(LengthBeginEnd value) {
                this.entryposition = value;
            }

            /**
             * Gets the value of the intermediatelanes property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the intermediatelanes property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getINTERMEDIATELANES().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link RESPONSIVECONTROLTYPE.SENSOR.MULTIPLELANE.INTERMEDIATELANES }
             * 
             * 
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public List<RESPONSIVECONTROLTYPE.SENSOR.MULTIPLELANE.INTERMEDIATELANES> getINTERMEDIATELANES() {
                if (intermediatelanes == null) {
                    intermediatelanes = new ArrayList<RESPONSIVECONTROLTYPE.SENSOR.MULTIPLELANE.INTERMEDIATELANES>();
                }
                return this.intermediatelanes;
            }

            /**
             * Gets the value of the exitlink property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public String getEXITLINK() {
                return exitlink;
            }

            /**
             * Sets the value of the exitlink property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public void setEXITLINK(String value) {
                this.exitlink = value;
            }

            /**
             * Gets the value of the exitlane property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public String getEXITLANE() {
                return exitlane;
            }

            /**
             * Sets the value of the exitlane property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public void setEXITLANE(String value) {
                this.exitlane = value;
            }

            /**
             * Gets the value of the exitposition property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public LengthBeginEnd getEXITPOSITION() {
                return exitposition;
            }

            /**
             * Sets the value of the exitposition property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public void setEXITPOSITION(LengthBeginEnd value) {
                this.exitposition = value;
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
             *         &lt;element name="LINK" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
             *         &lt;element name="LANE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
             *       &lt;/sequence&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "link",
                "lane"
            })
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public static class INTERMEDIATELANES
                implements Serializable
            {

                @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
                private final static long serialVersionUID = 10102L;
                @XmlElement(name = "LINK", required = true)
                @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
                protected String link;
                @XmlElement(name = "LANE", required = true)
                @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
                protected String lane;

                /**
                 * Gets the value of the link property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
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
                @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
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
                @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
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
                @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
                public void setLANE(String value) {
                    this.lane = value;
                }

            }

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
         *         &lt;element name="LINK" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="LANE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="ENTRYPOSITION" type="{http://www.opentrafficsim.org/ots}LENGTHBEGINENDTYPE"/&gt;
         *         &lt;element name="EXITPOSITION" type="{http://www.opentrafficsim.org/ots}LENGTHBEGINENDTYPE"/&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "link",
            "lane",
            "entryposition",
            "exitposition"
        })
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        public static class SINGLELANE
            implements Serializable
        {

            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            private final static long serialVersionUID = 10102L;
            @XmlElement(name = "LINK", required = true)
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            protected String link;
            @XmlElement(name = "LANE", required = true)
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            protected String lane;
            @XmlElement(name = "ENTRYPOSITION", required = true, type = String.class)
            @XmlJavaTypeAdapter(LengthBeginEndAdapter.class)
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            protected LengthBeginEnd entryposition;
            @XmlElement(name = "EXITPOSITION", required = true, type = String.class)
            @XmlJavaTypeAdapter(LengthBeginEndAdapter.class)
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            protected LengthBeginEnd exitposition;

            /**
             * Gets the value of the link property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
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
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
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
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
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
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public void setLANE(String value) {
                this.lane = value;
            }

            /**
             * Gets the value of the entryposition property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public LengthBeginEnd getENTRYPOSITION() {
                return entryposition;
            }

            /**
             * Sets the value of the entryposition property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public void setENTRYPOSITION(LengthBeginEnd value) {
                this.entryposition = value;
            }

            /**
             * Gets the value of the exitposition property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public LengthBeginEnd getEXITPOSITION() {
                return exitposition;
            }

            /**
             * Sets the value of the exitposition property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public void setEXITPOSITION(LengthBeginEnd value) {
                this.exitposition = value;
            }

        }

    }

}

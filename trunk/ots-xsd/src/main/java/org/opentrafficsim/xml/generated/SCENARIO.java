//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.04.07 at 05:06:58 PM CEST 
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
import javax.xml.bind.annotation.XmlRootElement;
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
 *         &lt;element name="INPUTPARAMETERS" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;choice maxOccurs="unbounded"&gt;
 *                   &lt;element name="ACCELERATION" type="{http://www.opentrafficsim.org/ots}PARAMETERACCELERATION"/&gt;
 *                   &lt;element name="ACCELERATIONDIST" type="{http://www.opentrafficsim.org/ots}PARAMETERACCELERATIONDIST"/&gt;
 *                   &lt;element name="BOOLEAN" type="{http://www.opentrafficsim.org/ots}PARAMETERBOOLEAN"/&gt;
 *                   &lt;element name="CLASS" type="{http://www.opentrafficsim.org/ots}PARAMETERCLASS"/&gt;
 *                   &lt;element name="DOUBLE" type="{http://www.opentrafficsim.org/ots}PARAMETERDOUBLE"/&gt;
 *                   &lt;element name="DOUBLEDIST" type="{http://www.opentrafficsim.org/ots}PARAMETERDOUBLEDIST"/&gt;
 *                   &lt;element name="DURATION" type="{http://www.opentrafficsim.org/ots}PARAMETERDURATION"/&gt;
 *                   &lt;element name="DURATIONDIST" type="{http://www.opentrafficsim.org/ots}PARAMETERDURATIONDIST"/&gt;
 *                   &lt;element name="FLOAT" type="{http://www.opentrafficsim.org/ots}PARAMETERFLOAT"/&gt;
 *                   &lt;element name="FLOATDIST" type="{http://www.opentrafficsim.org/ots}PARAMETERFLOATDIST"/&gt;
 *                   &lt;element name="FRACTION" type="{http://www.opentrafficsim.org/ots}PARAMETERFRACTION"/&gt;
 *                   &lt;element name="FREQUENCY" type="{http://www.opentrafficsim.org/ots}PARAMETERFREQUENCY"/&gt;
 *                   &lt;element name="FREQUENCYDIST" type="{http://www.opentrafficsim.org/ots}PARAMETERFREQUENCYDIST"/&gt;
 *                   &lt;element name="INTEGER" type="{http://www.opentrafficsim.org/ots}PARAMETERINTEGER"/&gt;
 *                   &lt;element name="INTEGERDIST" type="{http://www.opentrafficsim.org/ots}PARAMETERINTEGERDIST"/&gt;
 *                   &lt;element name="LENGTH" type="{http://www.opentrafficsim.org/ots}PARAMETERLENGTH"/&gt;
 *                   &lt;element name="LENGTHDIST" type="{http://www.opentrafficsim.org/ots}PARAMETERLENGTHDIST"/&gt;
 *                   &lt;element name="LINEARDENSITY" type="{http://www.opentrafficsim.org/ots}PARAMETERLINEARDENSITY"/&gt;
 *                   &lt;element name="LINEARDENSITYDIST" type="{http://www.opentrafficsim.org/ots}PARAMETERLINEARDENSITYDIST"/&gt;
 *                   &lt;element name="LONG" type="{http://www.opentrafficsim.org/ots}PARAMETERLONG"/&gt;
 *                   &lt;element name="LONGDIST" type="{http://www.opentrafficsim.org/ots}PARAMETERLONGDIST"/&gt;
 *                   &lt;element name="SPEED" type="{http://www.opentrafficsim.org/ots}PARAMETERSPEED"/&gt;
 *                   &lt;element name="SPEEDDIST" type="{http://www.opentrafficsim.org/ots}PARAMETERSPEEDDIST"/&gt;
 *                   &lt;element name="STRING" type="{http://www.opentrafficsim.org/ots}PARAMETERSTRING"/&gt;
 *                 &lt;/choice&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="OD" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="CONTROL" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="MODELIDREFERRAL" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                 &lt;attribute name="MODELID" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "inputparameters",
    "od",
    "control",
    "modelidreferral"
})
@XmlRootElement(name = "SCENARIO")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
public class SCENARIO
    implements Serializable
{

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
    private final static long serialVersionUID = 10102L;
    @XmlElement(name = "INPUTPARAMETERS")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
    protected SCENARIO.INPUTPARAMETERS inputparameters;
    @XmlElement(name = "OD")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
    protected List<SCENARIO.OD> od;
    @XmlElement(name = "CONTROL")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
    protected List<SCENARIO.CONTROL> control;
    @XmlElement(name = "MODELIDREFERRAL")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
    protected List<SCENARIO.MODELIDREFERRAL> modelidreferral;
    @XmlAttribute(name = "ID")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
    protected String id;

    /**
     * Gets the value of the inputparameters property.
     * 
     * @return
     *     possible object is
     *     {@link SCENARIO.INPUTPARAMETERS }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
    public SCENARIO.INPUTPARAMETERS getINPUTPARAMETERS() {
        return inputparameters;
    }

    /**
     * Sets the value of the inputparameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link SCENARIO.INPUTPARAMETERS }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
    public void setINPUTPARAMETERS(SCENARIO.INPUTPARAMETERS value) {
        this.inputparameters = value;
    }

    /**
     * Gets the value of the od property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the od property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOD().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SCENARIO.OD }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
    public List<SCENARIO.OD> getOD() {
        if (od == null) {
            od = new ArrayList<SCENARIO.OD>();
        }
        return this.od;
    }

    /**
     * Gets the value of the control property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the control property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCONTROL().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SCENARIO.CONTROL }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
    public List<SCENARIO.CONTROL> getCONTROL() {
        if (control == null) {
            control = new ArrayList<SCENARIO.CONTROL>();
        }
        return this.control;
    }

    /**
     * Gets the value of the modelidreferral property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the modelidreferral property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMODELIDREFERRAL().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SCENARIO.MODELIDREFERRAL }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
    public List<SCENARIO.MODELIDREFERRAL> getMODELIDREFERRAL() {
        if (modelidreferral == null) {
            modelidreferral = new ArrayList<SCENARIO.MODELIDREFERRAL>();
        }
        return this.modelidreferral;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
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
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
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
     *       &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
    public static class CONTROL
        implements Serializable
    {

        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
        private final static long serialVersionUID = 10102L;
        @XmlAttribute(name = "ID", required = true)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
        protected String id;

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
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
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
        public void setID(String value) {
            this.id = value;
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
     *       &lt;choice maxOccurs="unbounded"&gt;
     *         &lt;element name="ACCELERATION" type="{http://www.opentrafficsim.org/ots}PARAMETERACCELERATION"/&gt;
     *         &lt;element name="ACCELERATIONDIST" type="{http://www.opentrafficsim.org/ots}PARAMETERACCELERATIONDIST"/&gt;
     *         &lt;element name="BOOLEAN" type="{http://www.opentrafficsim.org/ots}PARAMETERBOOLEAN"/&gt;
     *         &lt;element name="CLASS" type="{http://www.opentrafficsim.org/ots}PARAMETERCLASS"/&gt;
     *         &lt;element name="DOUBLE" type="{http://www.opentrafficsim.org/ots}PARAMETERDOUBLE"/&gt;
     *         &lt;element name="DOUBLEDIST" type="{http://www.opentrafficsim.org/ots}PARAMETERDOUBLEDIST"/&gt;
     *         &lt;element name="DURATION" type="{http://www.opentrafficsim.org/ots}PARAMETERDURATION"/&gt;
     *         &lt;element name="DURATIONDIST" type="{http://www.opentrafficsim.org/ots}PARAMETERDURATIONDIST"/&gt;
     *         &lt;element name="FLOAT" type="{http://www.opentrafficsim.org/ots}PARAMETERFLOAT"/&gt;
     *         &lt;element name="FLOATDIST" type="{http://www.opentrafficsim.org/ots}PARAMETERFLOATDIST"/&gt;
     *         &lt;element name="FRACTION" type="{http://www.opentrafficsim.org/ots}PARAMETERFRACTION"/&gt;
     *         &lt;element name="FREQUENCY" type="{http://www.opentrafficsim.org/ots}PARAMETERFREQUENCY"/&gt;
     *         &lt;element name="FREQUENCYDIST" type="{http://www.opentrafficsim.org/ots}PARAMETERFREQUENCYDIST"/&gt;
     *         &lt;element name="INTEGER" type="{http://www.opentrafficsim.org/ots}PARAMETERINTEGER"/&gt;
     *         &lt;element name="INTEGERDIST" type="{http://www.opentrafficsim.org/ots}PARAMETERINTEGERDIST"/&gt;
     *         &lt;element name="LENGTH" type="{http://www.opentrafficsim.org/ots}PARAMETERLENGTH"/&gt;
     *         &lt;element name="LENGTHDIST" type="{http://www.opentrafficsim.org/ots}PARAMETERLENGTHDIST"/&gt;
     *         &lt;element name="LINEARDENSITY" type="{http://www.opentrafficsim.org/ots}PARAMETERLINEARDENSITY"/&gt;
     *         &lt;element name="LINEARDENSITYDIST" type="{http://www.opentrafficsim.org/ots}PARAMETERLINEARDENSITYDIST"/&gt;
     *         &lt;element name="LONG" type="{http://www.opentrafficsim.org/ots}PARAMETERLONG"/&gt;
     *         &lt;element name="LONGDIST" type="{http://www.opentrafficsim.org/ots}PARAMETERLONGDIST"/&gt;
     *         &lt;element name="SPEED" type="{http://www.opentrafficsim.org/ots}PARAMETERSPEED"/&gt;
     *         &lt;element name="SPEEDDIST" type="{http://www.opentrafficsim.org/ots}PARAMETERSPEEDDIST"/&gt;
     *         &lt;element name="STRING" type="{http://www.opentrafficsim.org/ots}PARAMETERSTRING"/&gt;
     *       &lt;/choice&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "accelerationOrACCELERATIONDISTOrBOOLEAN"
    })
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
    public static class INPUTPARAMETERS
        implements Serializable
    {

        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
        private final static long serialVersionUID = 10102L;
        @XmlElements({
            @XmlElement(name = "ACCELERATION", type = PARAMETERACCELERATION.class),
            @XmlElement(name = "ACCELERATIONDIST", type = PARAMETERACCELERATIONDIST.class),
            @XmlElement(name = "BOOLEAN", type = PARAMETERBOOLEAN.class),
            @XmlElement(name = "CLASS", type = PARAMETERCLASS.class),
            @XmlElement(name = "DOUBLE", type = PARAMETERDOUBLE.class),
            @XmlElement(name = "DOUBLEDIST", type = PARAMETERDOUBLEDIST.class),
            @XmlElement(name = "DURATION", type = PARAMETERDURATION.class),
            @XmlElement(name = "DURATIONDIST", type = PARAMETERDURATIONDIST.class),
            @XmlElement(name = "FLOAT", type = PARAMETERFLOAT.class),
            @XmlElement(name = "FLOATDIST", type = PARAMETERFLOATDIST.class),
            @XmlElement(name = "FRACTION", type = PARAMETERFRACTION.class),
            @XmlElement(name = "FREQUENCY", type = PARAMETERFREQUENCY.class),
            @XmlElement(name = "FREQUENCYDIST", type = PARAMETERFREQUENCYDIST.class),
            @XmlElement(name = "INTEGER", type = PARAMETERINTEGER.class),
            @XmlElement(name = "INTEGERDIST", type = PARAMETERINTEGERDIST.class),
            @XmlElement(name = "LENGTH", type = PARAMETERLENGTH.class),
            @XmlElement(name = "LENGTHDIST", type = PARAMETERLENGTHDIST.class),
            @XmlElement(name = "LINEARDENSITY", type = PARAMETERLINEARDENSITY.class),
            @XmlElement(name = "LINEARDENSITYDIST", type = PARAMETERLINEARDENSITYDIST.class),
            @XmlElement(name = "LONG", type = PARAMETERLONG.class),
            @XmlElement(name = "LONGDIST", type = PARAMETERLONGDIST.class),
            @XmlElement(name = "SPEED", type = PARAMETERSPEED.class),
            @XmlElement(name = "SPEEDDIST", type = PARAMETERSPEEDDIST.class),
            @XmlElement(name = "STRING", type = PARAMETERSTRING.class)
        })
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
        protected List<Serializable> accelerationOrACCELERATIONDISTOrBOOLEAN;

        /**
         * Gets the value of the accelerationOrACCELERATIONDISTOrBOOLEAN property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the accelerationOrACCELERATIONDISTOrBOOLEAN property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getACCELERATIONOrACCELERATIONDISTOrBOOLEAN().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link PARAMETERACCELERATION }
         * {@link PARAMETERACCELERATIONDIST }
         * {@link PARAMETERBOOLEAN }
         * {@link PARAMETERCLASS }
         * {@link PARAMETERDOUBLE }
         * {@link PARAMETERDOUBLEDIST }
         * {@link PARAMETERDURATION }
         * {@link PARAMETERDURATIONDIST }
         * {@link PARAMETERFLOAT }
         * {@link PARAMETERFLOATDIST }
         * {@link PARAMETERFRACTION }
         * {@link PARAMETERFREQUENCY }
         * {@link PARAMETERFREQUENCYDIST }
         * {@link PARAMETERINTEGER }
         * {@link PARAMETERINTEGERDIST }
         * {@link PARAMETERLENGTH }
         * {@link PARAMETERLENGTHDIST }
         * {@link PARAMETERLINEARDENSITY }
         * {@link PARAMETERLINEARDENSITYDIST }
         * {@link PARAMETERLONG }
         * {@link PARAMETERLONGDIST }
         * {@link PARAMETERSPEED }
         * {@link PARAMETERSPEEDDIST }
         * {@link PARAMETERSTRING }
         * 
         * 
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
        public List<Serializable> getACCELERATIONOrACCELERATIONDISTOrBOOLEAN() {
            if (accelerationOrACCELERATIONDISTOrBOOLEAN == null) {
                accelerationOrACCELERATIONDISTOrBOOLEAN = new ArrayList<Serializable>();
            }
            return this.accelerationOrACCELERATIONDISTOrBOOLEAN;
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
     *       &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *       &lt;attribute name="MODELID" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
    public static class MODELIDREFERRAL
        implements Serializable
    {

        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
        private final static long serialVersionUID = 10102L;
        @XmlAttribute(name = "ID")
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
        protected String id;
        @XmlAttribute(name = "MODELID")
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
        protected String modelid;

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
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
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
        public void setID(String value) {
            this.id = value;
        }

        /**
         * Gets the value of the modelid property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
        public String getMODELID() {
            return modelid;
        }

        /**
         * Sets the value of the modelid property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
        public void setMODELID(String value) {
            this.modelid = value;
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
     *       &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
    public static class OD
        implements Serializable
    {

        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
        private final static long serialVersionUID = 10102L;
        @XmlAttribute(name = "ID", required = true)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
        protected String id;

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
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
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-07T05:06:58+02:00", comments = "JAXB RI v2.3.0")
        public void setID(String value) {
            this.id = value;
        }

    }

}
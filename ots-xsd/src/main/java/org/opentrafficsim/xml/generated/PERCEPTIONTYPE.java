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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.opentrafficsim.xml.bindings.ClassNameAdapter;


/**
 * <p>Java class for PERCEPTIONTYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PERCEPTIONTYPE"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CATEGORY" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;simpleContent&gt;
 *               &lt;restriction base="&lt;http://www.opentrafficsim.org/ots&gt;CLASSATTRIBUTETYPE"&gt;
 *               &lt;/restriction&gt;
 *             &lt;/simpleContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="HEADWAYGTUTYPE" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;choice&gt;
 *                   &lt;element name="WRAP" type="{http://www.w3.org/2001/XMLSchema}anyType"/&gt;
 *                   &lt;element name="PERCEIVED"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="ESTIMATION"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;pattern value="NONE|UNDERESTIMATION|OVERESTIMATION"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="ANTICIPATION"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;pattern value="NONE|CONSTANTSPEED|CONSTANTACCELERATION"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/choice&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="MENTAL" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;choice&gt;
 *                   &lt;element name="FULLER"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="TASK" type="{http://www.opentrafficsim.org/ots}CLASSNAMETYPE" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                             &lt;element name="BEHAVIORALADAPTATION" maxOccurs="unbounded" minOccurs="0"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;simpleContent&gt;
 *                                   &lt;restriction base="&lt;http://www.opentrafficsim.org/ots&gt;CLASSATTRIBUTETYPE"&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/simpleContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="TASKMANAGER" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;pattern value="SUMMATIVE|ANTICIPATIONRELIANCE"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/choice&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PERCEPTIONTYPE", propOrder = {
    "category",
    "headwaygtutype",
    "mental"
})
@Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
public class PERCEPTIONTYPE
    implements Serializable
{

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
    private final static long serialVersionUID = 10102L;
    @XmlElement(name = "CATEGORY")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
    protected List<PERCEPTIONTYPE.CATEGORY> category;
    @XmlElement(name = "HEADWAYGTUTYPE")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
    protected PERCEPTIONTYPE.HEADWAYGTUTYPE headwaygtutype;
    @XmlElement(name = "MENTAL")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
    protected PERCEPTIONTYPE.MENTAL mental;

    /**
     * Gets the value of the category property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the category property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCATEGORY().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PERCEPTIONTYPE.CATEGORY }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
    public List<PERCEPTIONTYPE.CATEGORY> getCATEGORY() {
        if (category == null) {
            category = new ArrayList<PERCEPTIONTYPE.CATEGORY>();
        }
        return this.category;
    }

    /**
     * Gets the value of the headwaygtutype property.
     * 
     * @return
     *     possible object is
     *     {@link PERCEPTIONTYPE.HEADWAYGTUTYPE }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
    public PERCEPTIONTYPE.HEADWAYGTUTYPE getHEADWAYGTUTYPE() {
        return headwaygtutype;
    }

    /**
     * Sets the value of the headwaygtutype property.
     * 
     * @param value
     *     allowed object is
     *     {@link PERCEPTIONTYPE.HEADWAYGTUTYPE }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
    public void setHEADWAYGTUTYPE(PERCEPTIONTYPE.HEADWAYGTUTYPE value) {
        this.headwaygtutype = value;
    }

    /**
     * Gets the value of the mental property.
     * 
     * @return
     *     possible object is
     *     {@link PERCEPTIONTYPE.MENTAL }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
    public PERCEPTIONTYPE.MENTAL getMENTAL() {
        return mental;
    }

    /**
     * Sets the value of the mental property.
     * 
     * @param value
     *     allowed object is
     *     {@link PERCEPTIONTYPE.MENTAL }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
    public void setMENTAL(PERCEPTIONTYPE.MENTAL value) {
        this.mental = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;simpleContent&gt;
     *     &lt;restriction base="&lt;http://www.opentrafficsim.org/ots&gt;CLASSATTRIBUTETYPE"&gt;
     *     &lt;/restriction&gt;
     *   &lt;/simpleContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
    public static class CATEGORY
        extends CLASSATTRIBUTETYPE
        implements Serializable
    {

        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        private final static long serialVersionUID = 10102L;

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
     *         &lt;element name="WRAP" type="{http://www.w3.org/2001/XMLSchema}anyType"/&gt;
     *         &lt;element name="PERCEIVED"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="ESTIMATION"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;pattern value="NONE|UNDERESTIMATION|OVERESTIMATION"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="ANTICIPATION"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;pattern value="NONE|CONSTANTSPEED|CONSTANTACCELERATION"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
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
        "wrap",
        "perceived"
    })
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
    public static class HEADWAYGTUTYPE
        implements Serializable
    {

        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        private final static long serialVersionUID = 10102L;
        @XmlElement(name = "WRAP")
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        protected Object wrap;
        @XmlElement(name = "PERCEIVED")
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        protected PERCEPTIONTYPE.HEADWAYGTUTYPE.PERCEIVED perceived;

        /**
         * Gets the value of the wrap property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        public Object getWRAP() {
            return wrap;
        }

        /**
         * Sets the value of the wrap property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        public void setWRAP(Object value) {
            this.wrap = value;
        }

        /**
         * Gets the value of the perceived property.
         * 
         * @return
         *     possible object is
         *     {@link PERCEPTIONTYPE.HEADWAYGTUTYPE.PERCEIVED }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        public PERCEPTIONTYPE.HEADWAYGTUTYPE.PERCEIVED getPERCEIVED() {
            return perceived;
        }

        /**
         * Sets the value of the perceived property.
         * 
         * @param value
         *     allowed object is
         *     {@link PERCEPTIONTYPE.HEADWAYGTUTYPE.PERCEIVED }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        public void setPERCEIVED(PERCEPTIONTYPE.HEADWAYGTUTYPE.PERCEIVED value) {
            this.perceived = value;
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
         *         &lt;element name="ESTIMATION"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;pattern value="NONE|UNDERESTIMATION|OVERESTIMATION"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="ANTICIPATION"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;pattern value="NONE|CONSTANTSPEED|CONSTANTACCELERATION"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
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
            "estimation",
            "anticipation"
        })
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        public static class PERCEIVED
            implements Serializable
        {

            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            private final static long serialVersionUID = 10102L;
            @XmlElement(name = "ESTIMATION", required = true)
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            protected String estimation;
            @XmlElement(name = "ANTICIPATION", required = true)
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            protected String anticipation;

            /**
             * Gets the value of the estimation property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public String getESTIMATION() {
                return estimation;
            }

            /**
             * Sets the value of the estimation property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public void setESTIMATION(String value) {
                this.estimation = value;
            }

            /**
             * Gets the value of the anticipation property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public String getANTICIPATION() {
                return anticipation;
            }

            /**
             * Sets the value of the anticipation property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public void setANTICIPATION(String value) {
                this.anticipation = value;
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
     *       &lt;choice&gt;
     *         &lt;element name="FULLER"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="TASK" type="{http://www.opentrafficsim.org/ots}CLASSNAMETYPE" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                   &lt;element name="BEHAVIORALADAPTATION" maxOccurs="unbounded" minOccurs="0"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;simpleContent&gt;
     *                         &lt;restriction base="&lt;http://www.opentrafficsim.org/ots&gt;CLASSATTRIBUTETYPE"&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/simpleContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="TASKMANAGER" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;pattern value="SUMMATIVE|ANTICIPATIONRELIANCE"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
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
        "fuller"
    })
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
    public static class MENTAL
        implements Serializable
    {

        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        private final static long serialVersionUID = 10102L;
        @XmlElement(name = "FULLER")
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        protected PERCEPTIONTYPE.MENTAL.FULLER fuller;

        /**
         * Gets the value of the fuller property.
         * 
         * @return
         *     possible object is
         *     {@link PERCEPTIONTYPE.MENTAL.FULLER }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        public PERCEPTIONTYPE.MENTAL.FULLER getFULLER() {
            return fuller;
        }

        /**
         * Sets the value of the fuller property.
         * 
         * @param value
         *     allowed object is
         *     {@link PERCEPTIONTYPE.MENTAL.FULLER }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        public void setFULLER(PERCEPTIONTYPE.MENTAL.FULLER value) {
            this.fuller = value;
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
         *         &lt;element name="TASK" type="{http://www.opentrafficsim.org/ots}CLASSNAMETYPE" maxOccurs="unbounded" minOccurs="0"/&gt;
         *         &lt;element name="BEHAVIORALADAPTATION" maxOccurs="unbounded" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;simpleContent&gt;
         *               &lt;restriction base="&lt;http://www.opentrafficsim.org/ots&gt;CLASSATTRIBUTETYPE"&gt;
         *               &lt;/restriction&gt;
         *             &lt;/simpleContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="TASKMANAGER" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;pattern value="SUMMATIVE|ANTICIPATIONRELIANCE"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
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
            "task",
            "behavioraladaptation",
            "taskmanager"
        })
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
        public static class FULLER
            implements Serializable
        {

            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            private final static long serialVersionUID = 10102L;
            @XmlElement(name = "TASK", type = String.class)
            @XmlJavaTypeAdapter(ClassNameAdapter.class)
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            protected List<Class> task;
            @XmlElement(name = "BEHAVIORALADAPTATION")
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            protected List<PERCEPTIONTYPE.MENTAL.FULLER.BEHAVIORALADAPTATION> behavioraladaptation;
            @XmlElement(name = "TASKMANAGER")
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            protected String taskmanager;

            /**
             * Gets the value of the task property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the task property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getTASK().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link String }
             * 
             * 
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public List<Class> getTASK() {
                if (task == null) {
                    task = new ArrayList<Class>();
                }
                return this.task;
            }

            /**
             * Gets the value of the behavioraladaptation property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the behavioraladaptation property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getBEHAVIORALADAPTATION().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link PERCEPTIONTYPE.MENTAL.FULLER.BEHAVIORALADAPTATION }
             * 
             * 
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public List<PERCEPTIONTYPE.MENTAL.FULLER.BEHAVIORALADAPTATION> getBEHAVIORALADAPTATION() {
                if (behavioraladaptation == null) {
                    behavioraladaptation = new ArrayList<PERCEPTIONTYPE.MENTAL.FULLER.BEHAVIORALADAPTATION>();
                }
                return this.behavioraladaptation;
            }

            /**
             * Gets the value of the taskmanager property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public String getTASKMANAGER() {
                return taskmanager;
            }

            /**
             * Sets the value of the taskmanager property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public void setTASKMANAGER(String value) {
                this.taskmanager = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;simpleContent&gt;
             *     &lt;restriction base="&lt;http://www.opentrafficsim.org/ots&gt;CLASSATTRIBUTETYPE"&gt;
             *     &lt;/restriction&gt;
             *   &lt;/simpleContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
            public static class BEHAVIORALADAPTATION
                extends CLASSATTRIBUTETYPE
                implements Serializable
            {

                @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
                private final static long serialVersionUID = 10102L;

            }

        }

    }

}

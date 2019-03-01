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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.djunits.value.vdouble.scalar.Acceleration;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Speed;
import org.opentrafficsim.xml.bindings.AccelerationAdapter;
import org.opentrafficsim.xml.bindings.LengthAdapter;
import org.opentrafficsim.xml.bindings.SpeedAdapter;


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
 *         &lt;element name="SPEEDGTUCOLORER" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute name="MAXSPEED" use="required" type="{http://www.opentrafficsim.org/ots}SPEEDTYPE" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ACCELERATIONGTUCOLORER" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute name="MAXDECELERATION" use="required" type="{http://www.opentrafficsim.org/ots}ACCELERATIONTYPE" /&gt;
 *                 &lt;attribute name="MAXACCELERATION" use="required" type="{http://www.opentrafficsim.org/ots}ACCELERATIONTYPE" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="LANECHANGEURGEGTUCOLORER" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute name="MINLANECHANGEDISTANCE" use="required" type="{http://www.opentrafficsim.org/ots}LENGTHTYPE" /&gt;
 *                 &lt;attribute name="HORIZON" use="required" type="{http://www.opentrafficsim.org/ots}LENGTHTYPE" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
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
    "speedgtucolorer",
    "accelerationgtucolorer",
    "lanechangeurgegtucolorer"
})
@XmlRootElement(name = "GLOBAL")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
public class GLOBAL {

    @XmlElement(name = "SPEEDGTUCOLORER")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    protected GLOBAL.SPEEDGTUCOLORER speedgtucolorer;
    @XmlElement(name = "ACCELERATIONGTUCOLORER")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    protected GLOBAL.ACCELERATIONGTUCOLORER accelerationgtucolorer;
    @XmlElement(name = "LANECHANGEURGEGTUCOLORER")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    protected GLOBAL.LANECHANGEURGEGTUCOLORER lanechangeurgegtucolorer;
    @XmlAttribute(name = "base", namespace = "http://www.w3.org/XML/1998/namespace")
    @XmlSchemaType(name = "anyURI")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    protected String base;

    /**
     * Gets the value of the speedgtucolorer property.
     * 
     * @return
     *     possible object is
     *     {@link GLOBAL.SPEEDGTUCOLORER }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    public GLOBAL.SPEEDGTUCOLORER getSPEEDGTUCOLORER() {
        return speedgtucolorer;
    }

    /**
     * Sets the value of the speedgtucolorer property.
     * 
     * @param value
     *     allowed object is
     *     {@link GLOBAL.SPEEDGTUCOLORER }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    public void setSPEEDGTUCOLORER(GLOBAL.SPEEDGTUCOLORER value) {
        this.speedgtucolorer = value;
    }

    /**
     * Gets the value of the accelerationgtucolorer property.
     * 
     * @return
     *     possible object is
     *     {@link GLOBAL.ACCELERATIONGTUCOLORER }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    public GLOBAL.ACCELERATIONGTUCOLORER getACCELERATIONGTUCOLORER() {
        return accelerationgtucolorer;
    }

    /**
     * Sets the value of the accelerationgtucolorer property.
     * 
     * @param value
     *     allowed object is
     *     {@link GLOBAL.ACCELERATIONGTUCOLORER }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    public void setACCELERATIONGTUCOLORER(GLOBAL.ACCELERATIONGTUCOLORER value) {
        this.accelerationgtucolorer = value;
    }

    /**
     * Gets the value of the lanechangeurgegtucolorer property.
     * 
     * @return
     *     possible object is
     *     {@link GLOBAL.LANECHANGEURGEGTUCOLORER }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    public GLOBAL.LANECHANGEURGEGTUCOLORER getLANECHANGEURGEGTUCOLORER() {
        return lanechangeurgegtucolorer;
    }

    /**
     * Sets the value of the lanechangeurgegtucolorer property.
     * 
     * @param value
     *     allowed object is
     *     {@link GLOBAL.LANECHANGEURGEGTUCOLORER }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
    public void setLANECHANGEURGEGTUCOLORER(GLOBAL.LANECHANGEURGEGTUCOLORER value) {
        this.lanechangeurgegtucolorer = value;
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
     *       &lt;attribute name="MAXDECELERATION" use="required" type="{http://www.opentrafficsim.org/ots}ACCELERATIONTYPE" /&gt;
     *       &lt;attribute name="MAXACCELERATION" use="required" type="{http://www.opentrafficsim.org/ots}ACCELERATIONTYPE" /&gt;
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
    public static class ACCELERATIONGTUCOLORER {

        @XmlAttribute(name = "MAXDECELERATION", required = true)
        @XmlJavaTypeAdapter(AccelerationAdapter.class)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
        protected Acceleration maxdeceleration;
        @XmlAttribute(name = "MAXACCELERATION", required = true)
        @XmlJavaTypeAdapter(AccelerationAdapter.class)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
        protected Acceleration maxacceleration;

        /**
         * Gets the value of the maxdeceleration property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
        public Acceleration getMAXDECELERATION() {
            return maxdeceleration;
        }

        /**
         * Sets the value of the maxdeceleration property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
        public void setMAXDECELERATION(Acceleration value) {
            this.maxdeceleration = value;
        }

        /**
         * Gets the value of the maxacceleration property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
        public Acceleration getMAXACCELERATION() {
            return maxacceleration;
        }

        /**
         * Sets the value of the maxacceleration property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
        public void setMAXACCELERATION(Acceleration value) {
            this.maxacceleration = value;
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
     *       &lt;attribute name="MINLANECHANGEDISTANCE" use="required" type="{http://www.opentrafficsim.org/ots}LENGTHTYPE" /&gt;
     *       &lt;attribute name="HORIZON" use="required" type="{http://www.opentrafficsim.org/ots}LENGTHTYPE" /&gt;
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
    public static class LANECHANGEURGEGTUCOLORER {

        @XmlAttribute(name = "MINLANECHANGEDISTANCE", required = true)
        @XmlJavaTypeAdapter(LengthAdapter.class)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
        protected Length minlanechangedistance;
        @XmlAttribute(name = "HORIZON", required = true)
        @XmlJavaTypeAdapter(LengthAdapter.class)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
        protected Length horizon;

        /**
         * Gets the value of the minlanechangedistance property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
        public Length getMINLANECHANGEDISTANCE() {
            return minlanechangedistance;
        }

        /**
         * Sets the value of the minlanechangedistance property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
        public void setMINLANECHANGEDISTANCE(Length value) {
            this.minlanechangedistance = value;
        }

        /**
         * Gets the value of the horizon property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
        public Length getHORIZON() {
            return horizon;
        }

        /**
         * Sets the value of the horizon property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
        public void setHORIZON(Length value) {
            this.horizon = value;
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
     *       &lt;attribute name="MAXSPEED" use="required" type="{http://www.opentrafficsim.org/ots}SPEEDTYPE" /&gt;
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
    public static class SPEEDGTUCOLORER {

        @XmlAttribute(name = "MAXSPEED", required = true)
        @XmlJavaTypeAdapter(SpeedAdapter.class)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
        protected Speed maxspeed;

        /**
         * Gets the value of the maxspeed property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-01T04:16:12+01:00", comments = "JAXB RI v2.3.0")
        public Speed getMAXSPEED() {
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
        public void setMAXSPEED(Speed value) {
            this.maxspeed = value;
        }

    }

}

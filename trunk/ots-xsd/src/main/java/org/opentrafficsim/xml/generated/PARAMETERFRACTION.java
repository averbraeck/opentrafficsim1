//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.04.05 at 02:11:24 PM CEST 
//


package org.opentrafficsim.xml.generated;

import java.io.Serializable;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.opentrafficsim.xml.bindings.FractionAdapter;


/**
 * <p>Java class for PARAMETERFRACTION complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PARAMETERFRACTION"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base="&lt;http://www.opentrafficsim.org/ots&gt;FRACTIONTYPE"&gt;
 *       &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PARAMETERFRACTION", propOrder = {
    "value"
})
@XmlSeeAlso({
    org.opentrafficsim.xml.generated.MODELTYPE.MODELPARAMETERS.FRACTION.class
})
@Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-05T02:11:24+02:00", comments = "JAXB RI v2.3.0")
public class PARAMETERFRACTION implements Serializable
{

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-05T02:11:24+02:00", comments = "JAXB RI v2.3.0")
    private final static long serialVersionUID = 10102L;
    @XmlValue
    @XmlJavaTypeAdapter(FractionAdapter.class)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-05T02:11:24+02:00", comments = "JAXB RI v2.3.0")
    protected Double value;
    @XmlAttribute(name = "ID")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-05T02:11:24+02:00", comments = "JAXB RI v2.3.0")
    protected String id;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-05T02:11:24+02:00", comments = "JAXB RI v2.3.0")
    public Double getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-05T02:11:24+02:00", comments = "JAXB RI v2.3.0")
    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-05T02:11:24+02:00", comments = "JAXB RI v2.3.0")
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
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-05T02:11:24+02:00", comments = "JAXB RI v2.3.0")
    public void setID(String value) {
        this.id = value;
    }

}

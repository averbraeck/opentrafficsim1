//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.10.31 at 04:44:31 AM CET 
//


package org.opentrafficsim.xml.generated;

import java.io.Serializable;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PARAMETERTYPEFLOAT complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PARAMETERTYPEFLOAT"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.opentrafficsim.org/ots}PARAMETERTYPE"&gt;
 *       &lt;attribute name="DEFAULT" type="{http://www.w3.org/2001/XMLSchema}float" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PARAMETERTYPEFLOAT")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2020-10-31T04:44:31+01:00", comments = "JAXB RI v2.3.0")
public class PARAMETERTYPEFLOAT
    extends PARAMETERTYPE
    implements Serializable
{

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-10-31T04:44:31+01:00", comments = "JAXB RI v2.3.0")
    private final static long serialVersionUID = 10102L;
    @XmlAttribute(name = "DEFAULT")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-10-31T04:44:31+01:00", comments = "JAXB RI v2.3.0")
    protected Float _default;

    /**
     * Gets the value of the default property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-10-31T04:44:31+01:00", comments = "JAXB RI v2.3.0")
    public Float getDEFAULT() {
        return _default;
    }

    /**
     * Sets the value of the default property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-10-31T04:44:31+01:00", comments = "JAXB RI v2.3.0")
    public void setDEFAULT(Float value) {
        this._default = value;
    }

}

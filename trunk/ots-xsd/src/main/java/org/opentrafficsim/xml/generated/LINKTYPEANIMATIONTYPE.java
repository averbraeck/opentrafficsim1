//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.02.09 at 02:46:07 PM CET 
//


package org.opentrafficsim.xml.generated;

import java.awt.Color;
import java.io.Serializable;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.opentrafficsim.xml.bindings.ColorAdapter;


/**
 * <p>Java class for LINKTYPEANIMATIONTYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LINKTYPEANIMATIONTYPE"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="COLOR" use="required" type="{http://www.opentrafficsim.org/ots}COLORTYPE" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LINKTYPEANIMATIONTYPE")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
public class LINKTYPEANIMATIONTYPE
    implements Serializable
{

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
    private final static long serialVersionUID = 10102L;
    @XmlAttribute(name = "ID", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
    protected String id;
    @XmlAttribute(name = "COLOR", required = true)
    @XmlJavaTypeAdapter(ColorAdapter.class)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
    protected Color color;

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
     * Gets the value of the color property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
    public Color getCOLOR() {
        return color;
    }

    /**
     * Sets the value of the color property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-09T02:46:07+01:00", comments = "JAXB RI v2.3.0")
    public void setCOLOR(Color value) {
        this.color = value;
    }

}

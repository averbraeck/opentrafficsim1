//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.10.31 at 11:54:56 AM CET 
//


package org.opentrafficsim.xml.generated;

import java.io.Serializable;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.opentrafficsim.xml.bindings.DrivingDirectionAdapter;
import org.opentrafficsim.xml.bindings.types.DrivingDirectionType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="GTUTYPE" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="DIRECTION" use="required" type="{http://www.opentrafficsim.org/ots}DRIVINGDIRECTIONTYPE" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "COMPATIBILITY")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2020-10-31T11:54:56+01:00", comments = "JAXB RI v2.3.0")
public class COMPATIBILITY
    implements Serializable
{

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-10-31T11:54:56+01:00", comments = "JAXB RI v2.3.0")
    private final static long serialVersionUID = 10102L;
    @XmlAttribute(name = "GTUTYPE", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-10-31T11:54:56+01:00", comments = "JAXB RI v2.3.0")
    protected String gtutype;
    @XmlAttribute(name = "DIRECTION", required = true)
    @XmlJavaTypeAdapter(DrivingDirectionAdapter.class)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-10-31T11:54:56+01:00", comments = "JAXB RI v2.3.0")
    protected DrivingDirectionType direction;

    /**
     * Gets the value of the gtutype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-10-31T11:54:56+01:00", comments = "JAXB RI v2.3.0")
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
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-10-31T11:54:56+01:00", comments = "JAXB RI v2.3.0")
    public void setGTUTYPE(String value) {
        this.gtutype = value;
    }

    /**
     * Gets the value of the direction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-10-31T11:54:56+01:00", comments = "JAXB RI v2.3.0")
    public DrivingDirectionType getDIRECTION() {
        return direction;
    }

    /**
     * Sets the value of the direction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-10-31T11:54:56+01:00", comments = "JAXB RI v2.3.0")
    public void setDIRECTION(DrivingDirectionType value) {
        this.direction = value;
    }

}

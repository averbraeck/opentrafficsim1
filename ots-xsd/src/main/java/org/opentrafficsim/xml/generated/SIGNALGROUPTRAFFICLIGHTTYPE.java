//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.03.09 at 01:43:06 AM CET 
//


package org.opentrafficsim.xml.generated;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SIGNALGROUPTRAFFICLIGHTTYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SIGNALGROUPTRAFFICLIGHTTYPE"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.opentrafficsim.org/ots}TRAFFICLIGHTTYPE"&gt;
 *       &lt;attribute name="LINK" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SIGNALGROUPTRAFFICLIGHTTYPE")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-09T01:43:06+01:00", comments = "JAXB RI v2.3.0")
public class SIGNALGROUPTRAFFICLIGHTTYPE
    extends TRAFFICLIGHTTYPE
{

    @XmlAttribute(name = "LINK", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-09T01:43:06+01:00", comments = "JAXB RI v2.3.0")
    protected String link;

    /**
     * Gets the value of the link property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-09T01:43:06+01:00", comments = "JAXB RI v2.3.0")
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
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-09T01:43:06+01:00", comments = "JAXB RI v2.3.0")
    public void setLINK(String value) {
        this.link = value;
    }

}

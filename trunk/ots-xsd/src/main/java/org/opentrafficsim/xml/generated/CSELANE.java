//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.03.27 at 12:40:14 AM CET 
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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CSELANE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CSELANE"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.opentrafficsim.org/ots}CROSSSECTIONELEMENT"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.opentrafficsim.org/ots}SPEEDLIMIT" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="LANETYPE" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="DESIGNDIRECTION" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CSELANE", propOrder = {
    "speedlimit"
})
@Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-27T12:40:14+01:00", comments = "JAXB RI v2.3.0")
public class CSELANE
    extends CROSSSECTIONELEMENT
    implements Serializable
{

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-27T12:40:14+01:00", comments = "JAXB RI v2.3.0")
    private final static long serialVersionUID = 10102L;
    @XmlElement(name = "SPEEDLIMIT")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-27T12:40:14+01:00", comments = "JAXB RI v2.3.0")
    protected List<SPEEDLIMIT> speedlimit;
    @XmlAttribute(name = "ID", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-27T12:40:14+01:00", comments = "JAXB RI v2.3.0")
    protected String id;
    @XmlAttribute(name = "LANETYPE", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-27T12:40:14+01:00", comments = "JAXB RI v2.3.0")
    protected String lanetype;
    @XmlAttribute(name = "DESIGNDIRECTION", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-27T12:40:14+01:00", comments = "JAXB RI v2.3.0")
    protected boolean designdirection;

    /**
     * Gets the value of the speedlimit property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the speedlimit property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSPEEDLIMIT().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SPEEDLIMIT }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-27T12:40:14+01:00", comments = "JAXB RI v2.3.0")
    public List<SPEEDLIMIT> getSPEEDLIMIT() {
        if (speedlimit == null) {
            speedlimit = new ArrayList<SPEEDLIMIT>();
        }
        return this.speedlimit;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-27T12:40:14+01:00", comments = "JAXB RI v2.3.0")
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
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-27T12:40:14+01:00", comments = "JAXB RI v2.3.0")
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the lanetype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-27T12:40:14+01:00", comments = "JAXB RI v2.3.0")
    public String getLANETYPE() {
        return lanetype;
    }

    /**
     * Sets the value of the lanetype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-27T12:40:14+01:00", comments = "JAXB RI v2.3.0")
    public void setLANETYPE(String value) {
        this.lanetype = value;
    }

    /**
     * Gets the value of the designdirection property.
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-27T12:40:14+01:00", comments = "JAXB RI v2.3.0")
    public boolean isDESIGNDIRECTION() {
        return designdirection;
    }

    /**
     * Sets the value of the designdirection property.
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-27T12:40:14+01:00", comments = "JAXB RI v2.3.0")
    public void setDESIGNDIRECTION(boolean value) {
        this.designdirection = value;
    }

}

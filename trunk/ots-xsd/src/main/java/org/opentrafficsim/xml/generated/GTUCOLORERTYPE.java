//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.04.05 at 02:11:24 PM CEST 
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.opentrafficsim.xml.bindings.ClassNameAdapter;


/**
 * <p>Java class for GTUCOLORERTYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GTUCOLORERTYPE"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PARAMETER" type="{http://www.opentrafficsim.org/ots}PARAMETERTYPE" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="ID" default="DEFAULT"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;pattern value="(\ {0})DEFAULT|ID|SPEED|ACCELERATION"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="CLASS" type="{http://www.opentrafficsim.org/ots}CLASSNAMETYPE" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GTUCOLORERTYPE", propOrder = {
    "parameter"
})
@Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-05T02:11:24+02:00", comments = "JAXB RI v2.3.0")
public class GTUCOLORERTYPE
    implements Serializable
{

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-05T02:11:24+02:00", comments = "JAXB RI v2.3.0")
    private final static long serialVersionUID = 10102L;
    @XmlElement(name = "PARAMETER")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-05T02:11:24+02:00", comments = "JAXB RI v2.3.0")
    protected List<PARAMETERTYPE> parameter;
    @XmlAttribute(name = "ID")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-05T02:11:24+02:00", comments = "JAXB RI v2.3.0")
    protected String id;
    @XmlAttribute(name = "CLASS")
    @XmlJavaTypeAdapter(ClassNameAdapter.class)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-05T02:11:24+02:00", comments = "JAXB RI v2.3.0")
    protected Class _class;

    /**
     * Gets the value of the parameter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPARAMETER().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PARAMETERTYPE }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-05T02:11:24+02:00", comments = "JAXB RI v2.3.0")
    public List<PARAMETERTYPE> getPARAMETER() {
        if (parameter == null) {
            parameter = new ArrayList<PARAMETERTYPE>();
        }
        return this.parameter;
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
        if (id == null) {
            return "DEFAULT";
        } else {
            return id;
        }
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

    /**
     * Gets the value of the class property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-05T02:11:24+02:00", comments = "JAXB RI v2.3.0")
    public Class getCLASS() {
        return _class;
    }

    /**
     * Sets the value of the class property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-04-05T02:11:24+02:00", comments = "JAXB RI v2.3.0")
    public void setCLASS(Class value) {
        this._class = value;
    }

}

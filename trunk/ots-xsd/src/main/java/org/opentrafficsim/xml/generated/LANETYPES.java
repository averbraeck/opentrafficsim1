//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.03.23 at 01:31:45 PM CET 
//


package org.opentrafficsim.xml.generated;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element ref="{http://www.opentrafficsim.org/ots}LANETYPE" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    "lanetype"
})
@XmlRootElement(name = "LANETYPES")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-23T01:31:45+01:00", comments = "JAXB RI v2.3.0")
public class LANETYPES {

    @XmlElement(name = "LANETYPE")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-23T01:31:45+01:00", comments = "JAXB RI v2.3.0")
    protected List<LANETYPE> lanetype;

    /**
     * Gets the value of the lanetype property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lanetype property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLANETYPE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LANETYPE }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-23T01:31:45+01:00", comments = "JAXB RI v2.3.0")
    public List<LANETYPE> getLANETYPE() {
        if (lanetype == null) {
            lanetype = new ArrayList<LANETYPE>();
        }
        return this.lanetype;
    }

}

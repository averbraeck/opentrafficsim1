//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.03.18 at 01:12:17 AM CET 
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
 *         &lt;element ref="{http://www.opentrafficsim.org/ots}GTUTYPE" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    "gtutype"
})
@XmlRootElement(name = "GTUTYPES")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-18T01:12:17+01:00", comments = "JAXB RI v2.3.0")
public class GTUTYPES {

    @XmlElement(name = "GTUTYPE")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-18T01:12:17+01:00", comments = "JAXB RI v2.3.0")
    protected List<GTUTYPE> gtutype;

    /**
     * Gets the value of the gtutype property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the gtutype property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGTUTYPE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GTUTYPE }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-18T01:12:17+01:00", comments = "JAXB RI v2.3.0")
    public List<GTUTYPE> getGTUTYPE() {
        if (gtutype == null) {
            gtutype = new ArrayList<GTUTYPE>();
        }
        return this.gtutype;
    }

}

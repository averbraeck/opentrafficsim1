//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.03.27 at 12:40:14 AM CET 
//


package org.opentrafficsim.xml.generated;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for parseType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="parseType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *     &lt;enumeration value="xml"/&gt;
 *     &lt;enumeration value="text"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "parseType", namespace = "http://www.w3.org/2001/XInclude")
@XmlEnum
@Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-27T12:40:14+01:00", comments = "JAXB RI v2.3.0")
public enum ParseType {

    @XmlEnumValue("xml")
    XML("xml"),
    @XmlEnumValue("text")
    TEXT("text");
    private final String value;

    ParseType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ParseType fromValue(String v) {
        for (ParseType c: ParseType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

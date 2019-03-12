//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2016.11.03 at 01:02:34 PM CET
//

package org.opentrafficsim.road.network.factory.vissim.xsd;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import org.opentrafficsim.road.network.factory.vissim.GTUTYPE;

/**
 * This object contains factory methods for each Java content interface and Java element interface generated in the
 * org.opentrafficsim.road.network.factory.vissim package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java representation for XML content. The Java
 * representation of XML content can consist of schema derived interfaces and classes representing the binding of schema type
 * definitions, element declarations and model groups. Factory methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Fallback_QNAME = new QName("http://www.w3.org/2001/XInclude", "fallback");

    private final static QName _Include_QNAME = new QName("http://www.w3.org/2001/XInclude", "include");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package:
     * org.opentrafficsim.road.network.factory.vissim
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GLOBAL }
     */
    public GLOBAL createGLOBAL() {
        return new GLOBAL();
    }

    /**
     * Create an instance of {@link GTUMIX }
     */
    public GTUMIX createGTUMIX() {
        return new GTUMIX();
    }

    /**
     * Create an instance of {@link ROADTYPE }
     */
    public ROADTYPE createROADTYPE() {
        return new ROADTYPE();
    }

    /**
     * Create an instance of {@link LANETYPE }
     */
    public LANETYPE createLANETYPE() {
        return new LANETYPE();
    }

    /**
     * Create an instance of {@link ROADLAYOUT }
     */
    public ROADLAYOUT createROADLAYOUT() {
        return new ROADLAYOUT();
    }

    /**
     * Create an instance of {@link LINK }
     */
    public LINK createLINK() {
        return new LINK();
    }

    /**
     * Create an instance of {@link ROUTEMIX }
     */
    public ROUTEMIX createROUTEMIX() {
        return new ROUTEMIX();
    }

    /**
     * Create an instance of {@link SHORTESTROUTEMIX }
     */
    public SHORTESTROUTEMIX createSHORTESTROUTEMIX() {
        return new SHORTESTROUTEMIX();
    }

    /**
     * Create an instance of {@link ROADLAYOUT.LANE }
     */
    public ROADLAYOUT.LANE createROADLAYOUTLANE() {
        return new ROADLAYOUT.LANE();
    }

    /**
     * Create an instance of {@link org.opentrafficsim.road.network.factory.vissim.ROUTE }
     */
    public org.opentrafficsim.road.network.factory.vissim.xsd.ROUTE createROUTE() {
        return new org.opentrafficsim.road.network.factory.vissim.xsd.ROUTE();
    }

    /**
     * Create an instance of {@link NETWORK }
     */
    public NETWORK createNETWORK() {
        return new NETWORK();
    }

    /**
     * Create an instance of {@link DEFINITIONS }
     */
    public DEFINITIONS createDEFINITIONS() {
        return new DEFINITIONS();
    }

    /**
     * Create an instance of {@link GLOBAL.SPEEDGTUCOLORER }
     */
    public GLOBAL.SPEEDGTUCOLORER createGLOBALSPEEDGTUCOLORER() {
        return new GLOBAL.SPEEDGTUCOLORER();
    }

    /**
     * Create an instance of {@link GLOBAL.ACCELERATIONGTUCOLORER }
     */
    public GLOBAL.ACCELERATIONGTUCOLORER createGLOBALACCELERATIONGTUCOLORER() {
        return new GLOBAL.ACCELERATIONGTUCOLORER();
    }

    /**
     * Create an instance of {@link GLOBAL.LANECHANGEURGEGTUCOLORER }
     */
    public GLOBAL.LANECHANGEURGEGTUCOLORER createGLOBALLANECHANGEURGEGTUCOLORER() {
        return new GLOBAL.LANECHANGEURGEGTUCOLORER();
    }

    /**
     * Create an instance of {@link IncludeType }
     */
    public IncludeType createIncludeType() {
        return new IncludeType();
    }

    /**
     * Create an instance of {@link GTUTYPE }
     */
    public GTUTYPE createGTUTYPE() {
        return new GTUTYPE();
    }

    /**
     * Create an instance of {@link org.opentrafficsim.road.network.factory.vissim.GTU }
     */
    public org.opentrafficsim.road.network.factory.vissim.xsd.GTU createGTU() {
        return new org.opentrafficsim.road.network.factory.vissim.xsd.GTU();
    }

    /**
     * Create an instance of {@link GTUMIX.GTU }
     */
    public GTUMIX.GTU createGTUMIXGTU() {
        return new GTUMIX.GTU();
    }

    /**
     * Create an instance of {@link ROADTYPE.SPEEDLIMIT }
     */
    public ROADTYPE.SPEEDLIMIT createROADTYPESPEEDLIMIT() {
        return new ROADTYPE.SPEEDLIMIT();
    }

    /**
     * Create an instance of {@link LANETYPE.SPEEDLIMIT }
     */
    public LANETYPE.SPEEDLIMIT createLANETYPESPEEDLIMIT() {
        return new LANETYPE.SPEEDLIMIT();
    }

    /**
     * Create an instance of {@link ROADLAYOUT.SPEEDLIMIT }
     */
    public ROADLAYOUT.SPEEDLIMIT createROADLAYOUTSPEEDLIMIT() {
        return new ROADLAYOUT.SPEEDLIMIT();
    }

    /**
     * Create an instance of {@link ROADLAYOUT.NOTRAFFICLANE }
     */
    public ROADLAYOUT.NOTRAFFICLANE createROADLAYOUTNOTRAFFICLANE() {
        return new ROADLAYOUT.NOTRAFFICLANE();
    }

    /**
     * Create an instance of {@link ROADLAYOUT.SHOULDER }
     */
    public ROADLAYOUT.SHOULDER createROADLAYOUTSHOULDER() {
        return new ROADLAYOUT.SHOULDER();
    }

    /**
     * Create an instance of {@link ROADLAYOUT.STRIPE }
     */
    public ROADLAYOUT.STRIPE createROADLAYOUTSTRIPE() {
        return new ROADLAYOUT.STRIPE();
    }

    /**
     * Create an instance of {@link NODE }
     */
    public NODE createNODE() {
        return new NODE();
    }

    /**
     * Create an instance of {@link LINK.CLOTHOID }
     */
    public LINK.CLOTHOID createLINKCLOTHOID() {
        return new LINK.CLOTHOID();
    }

    /**
     * Create an instance of {@link LINK.STRAIGHT }
     */
    public LINK.STRAIGHT createLINKSTRAIGHT() {
        return new LINK.STRAIGHT();
    }

    /**
     * Create an instance of {@link LINK.ARC }
     */
    public LINK.ARC createLINKARC() {
        return new LINK.ARC();
    }

    /**
     * Create an instance of {@link LINK.POLYLINE }
     */
    public LINK.POLYLINE createLINKPOLYLINE() {
        return new LINK.POLYLINE();
    }

    /**
     * Create an instance of {@link LINK.LANEOVERRIDE }
     */
    public LINK.LANEOVERRIDE createLINKLANEOVERRIDE() {
        return new LINK.LANEOVERRIDE();
    }

    /**
     * Create an instance of {@link LINK.GENERATOR }
     */
    public LINK.GENERATOR createLINKGENERATOR() {
        return new LINK.GENERATOR();
    }

    /**
     * Create an instance of {@link LINK.LISTGENERATOR }
     */
    public LINK.LISTGENERATOR createLINKLISTGENERATOR() {
        return new LINK.LISTGENERATOR();
    }

    /**
     * Create an instance of {@link LINK.FILL }
     */
    public LINK.FILL createLINKFILL() {
        return new LINK.FILL();
    }

    /**
     * Create an instance of {@link LINK.BLOCK }
     */
    public LINK.BLOCK createLINKBLOCK() {
        return new LINK.BLOCK();
    }

    /**
     * Create an instance of {@link LINK.SENSOR }
     */
    public LINK.SENSOR createLINKSENSOR() {
        return new LINK.SENSOR();
    }

    /**
     * Create an instance of {@link LINK.TRAFFICLIGHT }
     */
    public LINK.TRAFFICLIGHT createLINKTRAFFICLIGHT() {
        return new LINK.TRAFFICLIGHT();
    }

    /**
     * Create an instance of {@link LINK.SINK }
     */
    public LINK.SINK createLINKSINK() {
        return new LINK.SINK();
    }

    /**
     * Create an instance of {@link ROUTEMIX.ROUTE }
     */
    public ROUTEMIX.ROUTE createROUTEMIXROUTE() {
        return new ROUTEMIX.ROUTE();
    }

    /**
     * Create an instance of {@link org.opentrafficsim.road.network.factory.vissim.SHORTESTROUTE }
     */
    public org.opentrafficsim.road.network.factory.vissim.xsd.SHORTESTROUTE createSHORTESTROUTE() {
        return new org.opentrafficsim.road.network.factory.vissim.xsd.SHORTESTROUTE();
    }

    /**
     * Create an instance of {@link SHORTESTROUTEMIX.SHORTESTROUTE }
     */
    public SHORTESTROUTEMIX.SHORTESTROUTE createSHORTESTROUTEMIXSHORTESTROUTE() {
        return new SHORTESTROUTEMIX.SHORTESTROUTE();
    }

    /**
     * Create an instance of {@link FallbackType }
     */
    public FallbackType createFallbackType() {
        return new FallbackType();
    }

    /**
     * Create an instance of {@link ROADLAYOUT.LANE.SPEEDLIMIT }
     */
    public ROADLAYOUT.LANE.SPEEDLIMIT createROADLAYOUTLANESPEEDLIMIT() {
        return new ROADLAYOUT.LANE.SPEEDLIMIT();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FallbackType }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2001/XInclude", name = "fallback")
    public JAXBElement<FallbackType> createFallback(FallbackType value) {
        return new JAXBElement<FallbackType>(_Fallback_QNAME, FallbackType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IncludeType }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2001/XInclude", name = "include")
    public JAXBElement<IncludeType> createInclude(IncludeType value) {
        return new JAXBElement<IncludeType>(_Include_QNAME, IncludeType.class, null, value);
    }

}
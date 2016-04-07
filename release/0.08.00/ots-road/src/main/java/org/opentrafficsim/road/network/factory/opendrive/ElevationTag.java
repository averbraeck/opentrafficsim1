package org.opentrafficsim.road.network.factory.opendrive;

import org.djunits.unit.LengthUnit;
import org.djunits.value.vdouble.scalar.Length;
import org.opentrafficsim.core.network.NetworkException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * <p>
 * Copyright (c) 2013-2015 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate: 2015-07-24 02:58:59 +0200 (Fri, 24 Jul 2015) $, @version $Revision: 1147 $, by $Author: averbraeck $,
 * initial version Jul 23, 2015 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
class ElevationTag
{
    /** Start position (s-coordinate). */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    Length.Rel s = null;

    /** The a coefficient. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    Length.Rel a = null;

    /** The b coefficient. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    Length.Rel b = null;

    /** The c coefficient. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    Length.Rel c = null;

    /** The d coefficient. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    Length.Rel d = null;

    /** Elevation = a + b*ds + c*ds2 + d*ds3 */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    Length.Rel elevation = null;

    /**
     * Parse the attributes of the road tag. The sub-elements are parsed in separate classes.
     * @param node the top-level road node
     * @param parser the parser with the lists of information
     * @return the generated RoadTag for further reference
     * @throws SAXException when parsing of the tag fails
     * @throws NetworkException when parsing of the tag fails
     */
    @SuppressWarnings("checkstyle:needbraces")
    static ElevationTag parseElevation(final Node node, final OpenDriveNetworkLaneParser parser) throws SAXException,
        NetworkException
    {
        NamedNodeMap attributes = node.getAttributes();
        ElevationTag elevationTag = new ElevationTag();

        Node s = attributes.getNamedItem("s");
        if (s == null)
            throw new SAXException("Geometry: missing attribute s");
        elevationTag.s = new Length.Rel(Double.parseDouble(s.getNodeValue().trim()), LengthUnit.METER);

        Node a = attributes.getNamedItem("a");
        if (a == null)
            throw new SAXException("Geometry: missing attribute a");
        elevationTag.a = new Length.Rel(Double.parseDouble(a.getNodeValue().trim()), LengthUnit.METER);

        Node b = attributes.getNamedItem("b");
        if (b == null)
            throw new SAXException("Geometry: missing attribute b");
        elevationTag.b = new Length.Rel(Double.parseDouble(b.getNodeValue().trim()), LengthUnit.METER);

        Node c = attributes.getNamedItem("c");
        if (c == null)
            throw new SAXException("Geometry: missing attribute c");
        elevationTag.c = new Length.Rel(Double.parseDouble(c.getNodeValue().trim()), LengthUnit.METER);

        Node d = attributes.getNamedItem("d");
        if (d == null)
            throw new SAXException("Geometry: missing attribute d");
        elevationTag.d = new Length.Rel(Double.parseDouble(d.getNodeValue().trim()), LengthUnit.METER);

        elevationTag.elevation =
            new Length.Rel(elevationTag.a.plus(elevationTag.b.multiplyBy(elevationTag.s.doubleValue()))
                .plus(elevationTag.c.multiplyBy(Math.pow(elevationTag.s.doubleValue(), 2)))
                .plus(elevationTag.d.multiplyBy(Math.pow(elevationTag.s.doubleValue(), 3))));

        return elevationTag;
    }
}

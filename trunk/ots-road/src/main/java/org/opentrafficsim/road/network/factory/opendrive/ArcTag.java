package org.opentrafficsim.road.network.factory.opendrive;

import org.djunits.unit.AnglePlaneUnit;
import org.djunits.value.vdouble.scalar.AnglePlane;
import org.opentrafficsim.core.network.NetworkException;
import org.opentrafficsim.road.network.factory.XMLParser;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
class ArcTag 
{
    /** degree of the curve at the start(s-coordinate?). */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    AnglePlane.Rel curvature = null;


    /**
     * Parse the attributes of the road.type tag. The sub-elements are parsed in separate classes.
     * @param nodeList the list of subnodes of the road node
     * @param parser the parser with the lists of information
     * @param geometryTag the GeometryTag to which this element belongs
     * @throws SAXException when parsing of the tag fails
     * @throws NetworkException when parsing of the tag fails
     */
    @SuppressWarnings("checkstyle:needbraces")
    static void parseArc(final NodeList nodeList, final OpenDriveNetworkLaneParser parser, final GeometryTag geometryTag)
        throws SAXException, NetworkException
    {
        int typeCount = 0;
        for (Node node : XMLParser.getNodes(nodeList, "arc"))
        {
            typeCount++;
            ArcTag arcTag = new ArcTag();
            NamedNodeMap attributes = node.getAttributes();

            Node curvature = attributes.getNamedItem("curvature");
            if (curvature != null)
                arcTag.curvature = new AnglePlane.Rel(Double.parseDouble(curvature.getNodeValue().trim()), AnglePlaneUnit.DEGREE);

            geometryTag.arcTag = arcTag;
        }

        if (typeCount > 1)
            throw new SAXException("ROAD: more than one arc tag!");
    }
}

package org.opentrafficsim.road.network.factory.xml;

import org.opentrafficsim.core.network.NetworkException;
import org.opentrafficsim.road.network.factory.xml.CrossSectionElementTag.ElementType;
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
class SinkTag
{
    /** Position of the sink on the link, relative to the design line, stored as a string to parse when the length is known. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    String positionStr = null;

    /**
     * Parse the SINK tag.
     * @param node the SINK node to parse
     * @param parser the parser with the lists of information
     * @param linkTag the parent LINK tag
     * @throws SAXException when parsing of the tag fails
     * @throws NetworkException when parsing of the tag fails
     */
    @SuppressWarnings("checkstyle:needbraces")
    static void parseSink(final Node node, final XmlNetworkLaneParser parser, final LinkTag linkTag)
        throws SAXException, NetworkException
    {
        NamedNodeMap attributes = node.getAttributes();
        SinkTag sinkTag = new SinkTag();

        if (attributes.getNamedItem("LANE") == null)
            throw new SAXException("SINK: missing attribute LANE" + " for link " + linkTag.name);
        String laneName = attributes.getNamedItem("LANE").getNodeValue().trim();
        if (linkTag.roadTypeTag == null)
            throw new NetworkException("SINK: LANE " + laneName + " no ROADTYPE for link " + linkTag.name);
        CrossSectionElementTag cseTag = linkTag.roadTypeTag.cseTags.get(laneName);
        if (cseTag == null)
            throw new NetworkException("SINK: LANE " + laneName + " not found in elements of link " + linkTag.name
                + " - roadtype " + linkTag.roadTypeTag.name);
        if (cseTag.elementType != ElementType.LANE)
            throw new NetworkException("SINK: LANE " + laneName + " not a real GTU lane for link " + linkTag.name
                + " - roadtype " + linkTag.roadTypeTag.name);
        if (linkTag.generatorTags.containsKey(laneName))
            throw new SAXException("SINK for LANE with NAME " + laneName + " defined twice");

        Node position = attributes.getNamedItem("POSITION");
        if (position == null)
            throw new NetworkException("SINK: POSITION element not found in elements of link " + linkTag.name
                + " - roadtype " + linkTag.roadTypeTag.name);
        sinkTag.positionStr = position.getNodeValue().trim();

        linkTag.sinkTags.put(laneName, sinkTag);
    }
}

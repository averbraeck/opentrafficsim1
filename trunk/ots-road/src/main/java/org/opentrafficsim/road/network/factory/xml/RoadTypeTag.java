package org.opentrafficsim.road.network.factory.xml;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Speed;
import org.opentrafficsim.core.network.NetworkException;
import org.opentrafficsim.core.network.factory.xml.units.LengthUnits;
import org.opentrafficsim.core.network.factory.xml.units.SpeedUnits;
import org.opentrafficsim.road.network.factory.XMLParser;
import org.opentrafficsim.road.network.factory.xml.units.LaneAttributes;
import org.opentrafficsim.road.network.lane.changing.LaneKeepingPolicy;
import org.opentrafficsim.road.network.lane.changing.OvertakingConditions;
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
class RoadTypeTag implements Serializable
{
    /** */
    private static final long serialVersionUID = 20150723L;

    /** Name. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    String name = null;

    /** Default speed. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    Speed speed = null;

    /** Default lane width. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    Length.Rel width = null;

    /** CrossSectionElementTags, order is important, so a LinkedHashMap. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    Map<String, CrossSectionElementTag> cseTags = new LinkedHashMap<>();

    /** The lane keeping policy, i.e., keep left, keep right or keep lane. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    LaneKeepingPolicy laneKeepingPolicy = null;

    /** The overtaking conditions for the lanes of this road type. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    OvertakingConditions overtakingConditions = null;

    /**
     * Parse the ROADTYPE tags. Delegates to a separate method because the RoadTypeTag can also occur inside a LINK tag. In the
     * latter case, it should not be stored in the central map. When this parseRoadTypes method is called, the tags <b>are</b>
     * stored in the central map in the parser class.
     * @param nodeList nodeList the top-level nodes of the XML-file
     * @param parser the parser with the lists of information
     * @throws SAXException when parsing of the tag fails
     * @throws NetworkException when parsing of the tag fails
     */
    @SuppressWarnings("checkstyle:needbraces")
    static void parseRoadTypes(final NodeList nodeList, final XmlNetworkLaneParser parser) throws SAXException,
        NetworkException
    {
        for (Node node : XMLParser.getNodes(nodeList, "ROADTYPE"))
        {
            RoadTypeTag roadTypeTag = parseRoadType(node, parser);
            parser.roadTypeTags.put(roadTypeTag.name, roadTypeTag);
        }
    }

    /**
     * Parse the ROADTYPE tags.
     * @param node the ROADTYPE nodes of the XML-file
     * @param parser the parser with the lists of information
     * @return the parsed RoadTypeTag
     * @throws SAXException when parsing of the tag fails
     * @throws NetworkException when parsing of the tag fails
     */
    @SuppressWarnings("checkstyle:needbraces")
    static RoadTypeTag parseRoadType(final Node node, final XmlNetworkLaneParser parser) throws SAXException,
        NetworkException
    {
        NamedNodeMap attributes = node.getAttributes();
        RoadTypeTag roadTypeTag = new RoadTypeTag();

        Node name = attributes.getNamedItem("NAME");
        if (name == null)
            throw new SAXException("ROADTYPE: missing attribute NAME");
        roadTypeTag.name = name.getNodeValue().trim();
        if (parser.roadTypeTags.keySet().contains(roadTypeTag.name))
            throw new SAXException("ROADTYPE: NAME " + roadTypeTag.name + " defined twice");

        Node width = attributes.getNamedItem("WIDTH");
        if (width != null)
            roadTypeTag.width = LengthUnits.parseLengthRel(width.getNodeValue());

        Node speed = attributes.getNamedItem("SPEED");
        if (speed != null)
            roadTypeTag.speed = SpeedUnits.parseSpeedAbs(speed.getNodeValue());

        Node lkp = attributes.getNamedItem("LANEKEEPING");
        if (lkp != null)
            roadTypeTag.laneKeepingPolicy = LaneAttributes.parseLaneKeepingPolicy(lkp.getNodeValue().trim());

        Node oc = attributes.getNamedItem("OVERTAKING");
        if (oc != null)
            roadTypeTag.overtakingConditions =
                LaneAttributes.parseOvertakingConditions(oc.getNodeValue().trim(), parser);

        int cseCount = 0;

        for (Node laneNode : XMLParser.getNodes(node.getChildNodes(), "LANE"))
        {
            CrossSectionElementTag.parseLane(laneNode, parser, roadTypeTag);
            cseCount++;
        }

        for (Node ntlNode : XMLParser.getNodes(node.getChildNodes(), "NOTRAFFICLANE"))
        {
            CrossSectionElementTag.parseNoTrafficLane(ntlNode, parser, roadTypeTag);
            cseCount++;
        }

        for (Node stripeNode : XMLParser.getNodes(node.getChildNodes(), "STRIPE"))
        {
            CrossSectionElementTag.parseStripe(stripeNode, parser, roadTypeTag);
            cseCount++;
        }

        for (Node shoulderNode : XMLParser.getNodes(node.getChildNodes(), "SHOULDER"))
        {
            CrossSectionElementTag.parseShoulder(shoulderNode, parser, roadTypeTag);
            cseCount++;
        }

        if (cseCount == 0)
            throw new NetworkException("ROADTYPE: No elements defined for road type " + roadTypeTag.name);

        return roadTypeTag;
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        return "RoadTypeTag [name=" + this.name + ", speed=" + this.speed + ", width=" + this.width + ", cseTags="
                + this.cseTags + ", laneKeepingPolicy=" + this.laneKeepingPolicy + ", overtakingConditions="
                + this.overtakingConditions + "]";
    }
}

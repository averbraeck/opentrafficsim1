package org.opentrafficsim.road.network.factory.xml.network;

import java.util.HashMap;
import java.util.List;

import org.djunits.value.vdouble.scalar.Acceleration;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Speed;
import org.djutils.logger.CategoryLogger;
import org.opentrafficsim.base.logger.Cat;
import org.opentrafficsim.core.compatibility.GTUCompatibility;
import org.opentrafficsim.core.distributions.Generator;
import org.opentrafficsim.core.gtu.GTUType;
import org.opentrafficsim.core.gtu.TemplateGTUType;
import org.opentrafficsim.core.network.LinkType;
import org.opentrafficsim.core.network.LongitudinalDirectionality;
import org.opentrafficsim.road.network.OTSRoadNetwork;
import org.opentrafficsim.road.network.factory.xml.XmlParserException;
import org.opentrafficsim.road.network.factory.xml.units.Generators;
import org.opentrafficsim.road.network.lane.LaneType;
import org.opentrafficsim.xml.generated.COMPATIBILITY;
import org.opentrafficsim.xml.generated.DEFINITIONS;
import org.opentrafficsim.xml.generated.GTUTEMPLATE;
import org.opentrafficsim.xml.generated.GTUTEMPLATES;
import org.opentrafficsim.xml.generated.GTUTYPE;
import org.opentrafficsim.xml.generated.GTUTYPES;
import org.opentrafficsim.xml.generated.LANETYPE;
import org.opentrafficsim.xml.generated.LANETYPES;
import org.opentrafficsim.xml.generated.LINKTYPE;
import org.opentrafficsim.xml.generated.LINKTYPES;
import org.opentrafficsim.xml.generated.ROADLAYOUT;
import org.opentrafficsim.xml.generated.ROADLAYOUTS;

import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * DefinitionParser parses the XML nodes of the DEFINITIONS tag: GTUTYPE, GTUTEMPLATE, LINKTYPE, LANETYPE and ROADLAYOUT. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public final class DefinitionsParser
{
    /** */
    private DefinitionsParser()
    {
        // utility class
    }

    /**
     * Parse the DEFINITIONS tag in the OTS XML file.
     * @param definitions the DEFINTIONS tag
     * @param otsNetwork the network
     * @param overwriteDefaults overwrite default definitions in otsNetwork or not
     * @param roadLayoutList temporary storage for the road layouts
     * @throws XmlParserException on parsing error
     */
    public static void parseDefinitions(final DEFINITIONS definitions, final OTSRoadNetwork otsNetwork,
            final boolean overwriteDefaults, final List<RoadLayout> roadLayoutList) throws XmlParserException
    {
        parseGtuTypes(definitions, otsNetwork, overwriteDefaults);
        parseLinkTypes(definitions, otsNetwork, overwriteDefaults);
        parseLaneTypes(definitions, otsNetwork, overwriteDefaults);
        parseGtuTemplates(definitions, otsNetwork, overwriteDefaults);
        parseRoadLayouts(definitions, otsNetwork, roadLayoutList);
    }

    /**
     * Parse the GTUTYPES tag in the OTS XML file.
     * @param definitions the DEFINTIONS tag
     * @param otsNetwork the network
     * @param overwriteDefaults overwrite default definitions in otsNetwork or not
     * @throws XmlParserException on parsing error
     */
    public static void parseGtuTypes(final DEFINITIONS definitions, final OTSRoadNetwork otsNetwork,
            final boolean overwriteDefaults) throws XmlParserException
    {
        for (Object object : definitions.getIncludeAndGTUTYPESAndGTUTEMPLATES())
        {
            if (object instanceof GTUTYPES)
            {
                for (GTUTYPE gtuTag : ((GTUTYPES) object).getGTUTYPE())
                {
                    GTUType networkGtuType = otsNetwork.getGtuTypes().get(gtuTag.getNAME());
                    if (networkGtuType == null || (networkGtuType != null && !gtuTag.isDEFAULT())
                            || (networkGtuType != null && gtuTag.isDEFAULT() && overwriteDefaults))
                    {
                        if (gtuTag.getPARENT() != null)
                        {
                            GTUType parent = otsNetwork.getGtuType(gtuTag.getPARENT());
                            if (parent == null)
                            {
                                throw new XmlParserException(
                                        "GTUType " + gtuTag.getNAME() + " parent " + gtuTag.getPARENT() + " not found");
                            }
                            GTUType gtuType = new GTUType(gtuTag.getNAME(), parent);
                            CategoryLogger.filter(Cat.PARSER).trace("Added GTUType {}", gtuType);
                        }
                        else
                        {
                            GTUType gtuType = new GTUType(gtuTag.getNAME(), otsNetwork);
                            CategoryLogger.filter(Cat.PARSER).trace("Added GTUType {}", gtuType);
                        }
                    }
                    else
                        CategoryLogger.filter(Cat.PARSER).trace("Did NOT add GTUType {}", gtuTag.getNAME());
                }
            }
        }
    }

    /**
     * Parse the LINKTYPES tag in the OTS XML file.
     * @param definitions the DEFINTIONS tag
     * @param otsNetwork the network
     * @param overwriteDefaults overwrite default definitions in otsNetwork or not
     * @throws XmlParserException on parsing error
     */
    public static void parseLinkTypes(final DEFINITIONS definitions, final OTSRoadNetwork otsNetwork,
            final boolean overwriteDefaults) throws XmlParserException
    {
        for (Object object : definitions.getIncludeAndGTUTYPESAndGTUTEMPLATES())
        {
            if (object instanceof LINKTYPES)
            {
                for (LINKTYPE linkTag : ((LINKTYPES) object).getLINKTYPE())
                {
                    LinkType networkLinkType = otsNetwork.getLinkTypes().get(linkTag.getNAME());
                    if (networkLinkType == null || (networkLinkType != null && !linkTag.isDEFAULT())
                            || (networkLinkType != null && linkTag.isDEFAULT() && overwriteDefaults))
                    {
                        GTUCompatibility<LinkType> compatibility = new GTUCompatibility<>((LinkType) null);
                        for (COMPATIBILITY compTag : linkTag.getCOMPATIBILITY())
                        {
                            GTUType gtuType = otsNetwork.getGtuType(compTag.getGTUTYPE());
                            if (gtuType == null)
                            {
                                throw new XmlParserException("LinkType " + linkTag.getNAME() + ".compatibility: GTUType "
                                        + compTag.getGTUTYPE() + " not found");
                            }
                            compatibility.addAllowedGTUType(gtuType,
                                    LongitudinalDirectionality.valueOf(compTag.getDIRECTION().toString()));
                        }
                        LinkType parent = otsNetwork.getLinkType(linkTag.getPARENT());
                        LinkType linkType = new LinkType(linkTag.getNAME(), parent, compatibility, otsNetwork);
                        CategoryLogger.filter(Cat.PARSER).trace("Added LinkType {}", linkType);
                    }
                    else
                        CategoryLogger.filter(Cat.PARSER).trace("Did NOT add LinkType {}", linkTag.getNAME());
                }
            }
        }
    }

    /**
     * Parse the LANETYPES tag in the OTS XML file.
     * @param definitions the DEFINTIONS tag
     * @param otsNetwork the network
     * @param overwriteDefaults overwrite default definitions in otsNetwork or not
     * @throws XmlParserException on parsing error
     */
    public static void parseLaneTypes(final DEFINITIONS definitions, final OTSRoadNetwork otsNetwork,
            final boolean overwriteDefaults) throws XmlParserException
    {
        for (Object object : definitions.getIncludeAndGTUTYPESAndGTUTEMPLATES())
        {
            if (object instanceof LANETYPES)
            {
                for (LANETYPE laneTag : ((LANETYPES) object).getLANETYPE())
                {
                    LaneType networkLaneType = otsNetwork.getLaneTypes().get(laneTag.getNAME());
                    if (networkLaneType == null || (networkLaneType != null && !laneTag.isDEFAULT())
                            || (networkLaneType != null && laneTag.isDEFAULT() && overwriteDefaults))
                    {
                        GTUCompatibility<LaneType> compatibility = new GTUCompatibility<>((LaneType) null);
                        for (COMPATIBILITY compTag : laneTag.getCOMPATIBILITY())
                        {
                            GTUType gtuType = otsNetwork.getGtuType(compTag.getGTUTYPE());
                            if (gtuType == null)
                            {
                                throw new XmlParserException("LaneType " + laneTag.getNAME() + ".compatibility: GTUType "
                                        + compTag.getGTUTYPE() + " not found");
                            }
                            compatibility.addAllowedGTUType(gtuType,
                                    LongitudinalDirectionality.valueOf(compTag.getDIRECTION().toString()));
                        }
                        if (laneTag.getPARENT() != null)
                        {
                            LaneType parent = otsNetwork.getLaneType(laneTag.getPARENT());
                            if (parent == null)
                            {
                                throw new XmlParserException(
                                        "LaneType " + laneTag.getNAME() + " parent " + laneTag.getPARENT() + " not found");
                            }
                            LaneType laneType = new LaneType(laneTag.getNAME(), parent, compatibility, otsNetwork);
                            CategoryLogger.filter(Cat.PARSER).trace("Added LaneType {}", laneType);
                        }
                        else
                        {
                            LaneType laneType = new LaneType(laneTag.getNAME(), compatibility, otsNetwork);
                            CategoryLogger.filter(Cat.PARSER).trace("Added LaneType {}", laneType);
                        }
                    }
                    else
                        CategoryLogger.filter(Cat.PARSER).trace("Did NOT add LaneType {}", laneTag.getNAME());
                }
            }
        }
    }

    /**
     * Parse the GTUTEMPLATES tag in the OTS XML file.
     * @param definitions the DEFINTIONS tag
     * @param otsNetwork the network
     * @param overwriteDefaults overwrite default definitions in otsNetwork or not
     * @throws XmlParserException on parsing error
     */
    public static void parseGtuTemplates(final DEFINITIONS definitions, final OTSRoadNetwork otsNetwork,
            final boolean overwriteDefaults) throws XmlParserException
    {
        for (Object object : definitions.getIncludeAndGTUTYPESAndGTUTEMPLATES())
        {
            // TODO: define random streams in GLOBAL tag, and access and parse them in the distributions
            StreamInterface stream = new MersenneTwister(2L);
            if (object instanceof GTUTEMPLATES)
            {
                for (GTUTEMPLATE templateTag : ((GTUTEMPLATES) object).getGTUTEMPLATE())
                {
                    GTUType gtuType = otsNetwork.getGtuType(templateTag.getGTUTYPE());
                    if (gtuType == null)
                    {
                        throw new XmlParserException(
                                "GTUTemplate " + templateTag.getNAME() + " GTUType " + templateTag.getGTUTYPE() + " not found");
                    }
                    if (GTUType.TEMPLATES.get(otsNetwork) == null)
                    {
                        GTUType.TEMPLATES.put(otsNetwork, new HashMap<>());
                    }
                    TemplateGTUType existingTemplate = GTUType.TEMPLATES.get(otsNetwork).get(gtuType);
                    if (existingTemplate == null || (existingTemplate != null && !templateTag.isDEFAULT())
                            || (existingTemplate != null && templateTag.isDEFAULT() && overwriteDefaults))
                    {
                        Generator<Length> lengthGenerator = Generators.makeLengthGenerator(stream, templateTag.getLENGTHDIST());
                        Generator<Length> widthGenerator = Generators.makeLengthGenerator(stream, templateTag.getWIDTHDIST());
                        Generator<Speed> maximumSpeedGenerator =
                                Generators.makeSpeedGenerator(stream, templateTag.getMAXSPEEDDIST());
                        Generator<Acceleration> maximumAccelerationGenerator =
                                Generators.makeAccelerationGenerator(stream, templateTag.getMAXACCELERATIONDIST());
                        Generator<Acceleration> maximumDecelerationGenerator =
                                Generators.makeDecelerationGenerator(stream, templateTag.getMAXDECELERATIONDIST());
                        TemplateGTUType templateGTUType = new TemplateGTUType(gtuType, lengthGenerator, widthGenerator,
                                maximumSpeedGenerator, maximumAccelerationGenerator, maximumDecelerationGenerator);
                        GTUType.TEMPLATES.get(otsNetwork).put(gtuType, templateGTUType);
                        CategoryLogger.filter(Cat.PARSER).trace("Added TemplateGTUType {}", templateGTUType);
                    }
                    else
                        CategoryLogger.filter(Cat.PARSER).trace("Did NOT add TemplateGTUType {}", templateTag.getNAME());
                }
            }
        }
    }

    /**
     * Parse the ROADLAYOUTS tag in the OTS XML file.
     * @param definitions the DEFINTIONS tag
     * @param otsNetwork the network
     * @param roadLayoutList temporary storage for the road layouts
     * @throws XmlParserException on parsing error
     */
    public static void parseRoadLayouts(final DEFINITIONS definitions, final OTSRoadNetwork otsNetwork,
            final List<RoadLayout> roadLayoutList) throws XmlParserException
    {
        for (Object object : definitions.getIncludeAndGTUTYPESAndGTUTEMPLATES())
        {
            if (object instanceof ROADLAYOUTS)
            {
                for (ROADLAYOUT layoutTag : ((ROADLAYOUTS) object).getROADLAYOUT())
                {
                }
            }
        }
    }

}

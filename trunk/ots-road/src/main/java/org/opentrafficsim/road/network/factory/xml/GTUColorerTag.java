package org.opentrafficsim.road.network.factory.xml;

import org.opentrafficsim.core.OTS_SCALAR;
import org.opentrafficsim.core.gtu.animation.AccelerationGTUColorer;
import org.opentrafficsim.core.gtu.animation.GTUColorer;
import org.opentrafficsim.core.gtu.animation.IDGTUColorer;
import org.opentrafficsim.core.gtu.animation.SwitchableGTUColorer;
import org.opentrafficsim.core.gtu.animation.VelocityGTUColorer;
import org.opentrafficsim.road.gtu.animation.LaneChangeUrgeGTUColorer;
import org.xml.sax.SAXException;

/**
 * <p>
 * Copyright (c) 2013-2015 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate: 2015-07-24 02:58:59 +0200 (Fri, 24 Jul 2015) $, @version $Revision: 1147 $, by $Author: averbraeck $,
 * initial version Jul 28, 2015 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
final class GTUColorerTag implements OTS_SCALAR
{
    /** Utility class. */
    private GTUColorerTag()
    {
        // do not instantiate
    }

    /**
     * Parses the right GTUColorer from ID|VELOCITY|ACCELERATION|LANECHANGEURGE|SWITCHABLE.
     * @param name name of the GTUColorer
     * @param globalTag to define the default parameters of the colorers
     * @return the corresponding GTUColorer
     * @throws SAXException in case the name does not correspond to a known GTUColorer
     */
    static GTUColorer parseGTUColorer(final String name, final GlobalTag globalTag) throws SAXException
    {
        switch (name)
        {
            case "ID":
                return new IDGTUColorer();

            case "VELOCITY":
                return makeVelocityGTUColorer(globalTag);

            case "ACCELERATION":
                return makeAccelerationGTUColorer(globalTag);

            case "LANECHANGEURGE":
                return makeLaneChangeUrgeGTUColorer(globalTag);

            case "SWITCHABLE":
                return makeSwitchableGTUColorer(globalTag);

            default:
                throw new SAXException("GTUCOLORER: unknown name " + name
                    + " not one of ID|VELOCITY|ACCELERATION|LANECHANGEURGE|SWITCHABLE");
        }
    }

    /**
     * @param globalTag to define the default parameters of the colorers
     * @return the corresponding GTUColorer
     */
    static GTUColorer makeVelocityGTUColorer(final GlobalTag globalTag)
    {
        if (globalTag.velocityGTUColorerMaxSpeed != null)
        {
            return new VelocityGTUColorer(globalTag.velocityGTUColorerMaxSpeed);
        }
        return new VelocityGTUColorer(new Speed.Abs(100.0, KM_PER_HOUR));
    }

    /**
     * @param globalTag to define the default parameters of the colorers
     * @return the corresponding GTUColorer
     */
    static GTUColorer makeAccelerationGTUColorer(final GlobalTag globalTag)
    {
        // TODO use parameters for AccelerationGTUColorer
        return new AccelerationGTUColorer(new Acceleration.Abs(1.0, METER_PER_SECOND_2), new Acceleration.Abs(1.0,
            METER_PER_SECOND_2));
    }

    /**
     * @param globalTag to define the default parameters of the colorers
     * @return the corresponding GTUColorer
     */
    static GTUColorer makeLaneChangeUrgeGTUColorer(final GlobalTag globalTag)
    {
        // TODO use parameters for LaneChangeUrgeGTUColorer
        return new LaneChangeUrgeGTUColorer(new Length.Rel(100.0, METER), new Length.Rel(100.0, METER));
    }

    /**
     * @param globalTag to define the default parameters of the colorers
     * @return the corresponding GTUColorer
     */
    static GTUColorer makeSwitchableGTUColorer(final GlobalTag globalTag)
    {
        GTUColorer[] gtuColorers =
            new GTUColorer[]{new IDGTUColorer(), makeVelocityGTUColorer(globalTag), makeAccelerationGTUColorer(globalTag),
                makeLaneChangeUrgeGTUColorer(globalTag)};
        // TODO default colorer
        return new SwitchableGTUColorer(0, gtuColorers);
    }
}

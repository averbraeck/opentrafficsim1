/**
 * 
 */
package org.opentrafficsim.water;

import org.locationtech.jts.geom.Coordinate;

/**
 * Base abstract class for a named, located object that produces events.
 * <p>
 * Copyright (c) 2013-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/current/license.html">OpenTrafficSim License</a>.
 * </p>
 * <p>
 * Based on software from the IDVV project, which is Copyright (c) 2013 Rijkswaterstaat - Dienst Water, Verkeer en Leefomgeving
 * and licensed without restrictions to Delft University of Technology, including the right to sub-license sources and derived
 * products to third parties.
 * </p>
 * $LastChangedDate: 2015-07-24 02:58:59 +0200 (Fri, 24 Jul 2015) $, @version $Revision: 1147 $, by $Author: averbraeck $,
 * initial version Nov 6, 2016 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public abstract class AbstractNamedLocatedEventProducer extends AbstractLocatedEventProducer implements Named
{
    /** */
    private static final long serialVersionUID = 1L;

    /** name. */
    private String name;

    /**
     * @param name String; the name
     * @param coordinate Coordinate; the coordinate
     */
    public AbstractNamedLocatedEventProducer(final String name, final Coordinate coordinate)
    {
        super(coordinate);
        this.name = name;
    }

    /** {@inheritDoc} */
    @Override
    public final String getName()
    {
        return this.name;
    }

}
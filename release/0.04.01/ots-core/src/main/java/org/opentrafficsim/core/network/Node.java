package org.opentrafficsim.core.network;

import java.io.Serializable;
import java.util.Set;

import javax.media.j3d.Bounds;

import nl.tudelft.simulation.dsol.animation.LocatableInterface;
import nl.tudelft.simulation.language.d3.DirectedPoint;

import org.djunits.value.vdouble.scalar.Angle;
import org.opentrafficsim.core.geometry.OTSPoint3D;

/**
 * The Node is a point with an id. It is used in the network to connect Links.
 * <p>
 * Copyright (c) 2013-2015 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate$, @version $Revision$, by $Author$,
 * initial version Aug 19, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.citg.tudelft.nl">Guus Tamminga</a>
 */
public interface Node extends LocatableInterface, Serializable
{
    /** @return node id. */
    String getId();

    /** @return point. */
    OTSPoint3D getPoint();

    /** @return the 3D direction. "East" is 0 degrees. "North" is 90 degrees (1/2 pi radians). */
    Angle.Abs getDirection();

    /** @return the slope as an angle. Horizontal is 0 degrees. */
    Angle.Abs getSlope();

    /**
     * Add an incoming link to this Node.
     * @param linkIn the link to add.
     */
    void addLinkIn(Link linkIn);

    /**
     * Add an outgoing link to this Node.
     * @param linkOut the link to add.
     */
    void addLinkOut(final Link linkOut);

    /** @return linksIn. */
    Set<Link> getLinksIn();

    /** @return linksOut. */
    Set<Link> getLinksOut();

    /** {@inheritDoc} */
    @Override
    DirectedPoint getLocation();

    /** {@inheritDoc} */
    @Override
    Bounds getBounds();
}
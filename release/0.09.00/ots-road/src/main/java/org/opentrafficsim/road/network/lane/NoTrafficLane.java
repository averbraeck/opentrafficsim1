package org.opentrafficsim.road.network.lane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.djunits.unit.SpeedUnit;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Speed;
import org.opentrafficsim.core.geometry.OTSGeometryException;
import org.opentrafficsim.core.gtu.GTUType;
import org.opentrafficsim.core.network.LongitudinalDirectionality;
import org.opentrafficsim.core.network.NetworkException;
import org.opentrafficsim.road.network.lane.changing.OvertakingConditions;

/**
 * Lane without traffic, e.g. emergency lane next to highway.
 * <p>
 * Copyright (c) 2013-2015 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate: 2015-09-16 19:20:07 +0200 (Wed, 16 Sep 2015) $, @version $Revision: 1405 $, by $Author: averbraeck $,
 * initial version Feb 28, 2015 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class NoTrafficLane extends Lane
{
    /** */
    private static final long serialVersionUID = 20150228L;

    /** Map that tells that directionality is NONE for all GTU types. */
    private static final Map<GTUType, LongitudinalDirectionality> DIRECTIONALITY_NONE = new HashMap<>();

    /** Map that tells that speed is 0.0 for all GTU Types. */
    private static final Map<GTUType, Speed> SPEED_NULL = new HashMap<>();

    /** The overtaking rules for a no-traffic lane. */
    private static final OvertakingConditions NO_OVERTAKING = new OvertakingConditions.None();

    static
    {
        DIRECTIONALITY_NONE.put(GTUType.ALL, LongitudinalDirectionality.DIR_NONE);
        SPEED_NULL.put(GTUType.ALL, new Speed(0.0, SpeedUnit.SI));
    }

    /**
     * @param parentLink Cross Section Link to which the element belongs.
     * @param id String; the id of the lane. Should be unique within the parentLink.
     * @param lateralOffsetAtStart Length; the lateral offset of the design line of the new CrossSectionLink with respect to
     *            the design line of the parent Link at the start of the parent Link
     * @param lateralOffsetAtEnd Length; the lateral offset of the design line of the new CrossSectionLink with respect to
     *            the design line of the parent Link at the end of the parent Link
     * @param beginWidth Length; start width, positioned <i>symmetrically around</i> the design line
     * @param endWidth Length; end width, positioned <i>symmetrically around</i> the design line
     * @throws OTSGeometryException when creation of the geometry fails
     * @throws NetworkException when id equal to null or not unique
     */
    @SuppressWarnings("checkstyle:parameternumber")
    public NoTrafficLane(final CrossSectionLink parentLink, final String id, final Length lateralOffsetAtStart,
        final Length lateralOffsetAtEnd, final Length beginWidth, final Length endWidth)
        throws OTSGeometryException, NetworkException
    {
        super(parentLink, id, lateralOffsetAtStart, lateralOffsetAtEnd, beginWidth, endWidth, LaneType.NONE,
            DIRECTIONALITY_NONE, SPEED_NULL, NO_OVERTAKING);
    }

    /**
     * @param parentLink Cross Section Link to which the element belongs.
     * @param id String; the id of the lane. Should be unique within the parentLink.
     * @param lateralOffset Length; the lateral offset of the design line of the new CrossSectionLink with respect to the
     *            design line of the parent Link
     * @param width Length; width, positioned <i>symmetrically around</i> the design line
     * @throws OTSGeometryException when creation of the geometry fails
     * @throws NetworkException when id equal to null or not unique
     */
    @SuppressWarnings("checkstyle:parameternumber")
    public NoTrafficLane(final CrossSectionLink parentLink, final String id, final Length lateralOffset,
        final Length width) throws OTSGeometryException, NetworkException
    {
        super(parentLink, id, lateralOffset, width, LaneType.NONE, DIRECTIONALITY_NONE, SPEED_NULL, NO_OVERTAKING);
    }

    /**
     * @param parentLink Cross Section Link to which the element belongs.
     * @param id String; the id of the lane. Should be unique within the parentLink.
     * @param crossSectionSlices The offsets and widths at positions along the line, relative to the design line of the parent
     *            link. If there is just one with and offset, there should just be one element in the list with Length = 0.
     *            If there are more slices, the last one should be at the length of the design line. If not, a NetworkException
     *            is thrown.
     * @throws OTSGeometryException when creation of the geometry fails
     * @throws NetworkException when id equal to null or not unique
     */
    @SuppressWarnings("checkstyle:parameternumber")
    public NoTrafficLane(final CrossSectionLink parentLink, final String id,
        final List<CrossSectionSlice> crossSectionSlices) throws OTSGeometryException, NetworkException
    {
        super(parentLink, id, crossSectionSlices, LaneType.NONE, DIRECTIONALITY_NONE, SPEED_NULL, NO_OVERTAKING);
    }

    /** {@inheritDoc} */
    @Override
    protected final double getZ()
    {
        return -0.00005;
    }
}

package org.opentrafficsim.road.network.lane.object.detector;

import org.djunits.value.vdouble.scalar.Length;
import org.opentrafficsim.core.dsol.OtsSimulatorInterface;
import org.opentrafficsim.core.gtu.RelativePosition;
import org.opentrafficsim.core.network.NetworkException;
import org.opentrafficsim.road.gtu.lane.LaneBasedGtu;
import org.opentrafficsim.road.network.lane.Lane;

/**
 * A SinkDetector is a detector that deletes every GTU that hits it.
 * <p>
 * Copyright (c) 2013-2023 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands.<br>
 * All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 */
public class SinkDetector extends LaneDetector
{
    /** */
    private static final long serialVersionUID = 20150130L;

    /**
     * @param lane Lane; the lane that triggers the deletion of the GTU.
     * @param position Length; the position of the detector
     * @param simulator OtsSimulatorInterface; the simulator to enable animation.
     * @param detectorType DetectorType; detector type.
     * @throws NetworkException when the position on the lane is out of bounds w.r.t. the center line of the lane
     */
    public SinkDetector(final Lane lane, final Length position, final OtsSimulatorInterface simulator,
            final DetectorType detectorType) throws NetworkException
    {
        super("SINK@" + lane.getFullId() + "." + position, lane, position, RelativePosition.FRONT, simulator,
                makeGeometry(lane, position, 1.0), detectorType);
    }

    /** {@inheritDoc} */
    @Override
    public final void triggerResponse(final LaneBasedGtu gtu)
    {
        gtu.destroy();
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        return "SinkDetector [Lane=" + this.getLane() + "]";
    }

}

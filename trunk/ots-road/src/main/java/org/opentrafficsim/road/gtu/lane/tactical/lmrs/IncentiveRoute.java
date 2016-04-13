package org.opentrafficsim.road.gtu.lane.tactical.lmrs;

import org.opentrafficsim.road.gtu.lane.LaneBasedGTU;
import org.opentrafficsim.road.gtu.lane.perception.LanePerception;

/**
 * Determines desire by assessing the number of required lane change to be performed and the distance within which these have to
 * be performed. Desire starts to increase from 0 linearly over a distance of x0 per required lane change, or per v*t0 per
 * required lane change. For v&gt;x0/t0 this gives that remaining time is critical, while for v&lt;x0/t0 remaining space is
 * critical. The desire is set towards the adjacent lane with a better situation. Negative desire towards the other lane, the
 * extent of which pertains to the other adjacent lane, is also set. For tapers there are special considerations as lane changes
 * are possible and favorable to both directions.
 * <p>
 * Copyright (c) 2013-2016 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Apr 13, 2016 <br>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class IncentiveRoute implements MandatoryIncentive
{

    /** {@inheritDoc} */
    @Override
    public Desire determineDesire(final LaneBasedGTU gtu, final LanePerception perception)
    {
        return new Desire(0, 0);
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        return "IncentiveRoute []";
    }

}

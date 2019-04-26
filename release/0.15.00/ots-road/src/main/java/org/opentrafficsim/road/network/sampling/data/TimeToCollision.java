package org.opentrafficsim.road.network.sampling.data;

import java.util.HashMap;
import java.util.Map;

import org.djunits.unit.DurationUnit;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Speed;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.opentrafficsim.core.gtu.GTUDirectionality;
import org.opentrafficsim.core.gtu.GTUException;
import org.opentrafficsim.core.gtu.RelativePosition;
import org.opentrafficsim.kpi.interfaces.GtuDataInterface;
import org.opentrafficsim.kpi.sampling.data.ExtendedDataTypeDuration;
import org.opentrafficsim.road.gtu.lane.LaneBasedGTU;
import org.opentrafficsim.road.network.lane.DirectedLanePosition;
import org.opentrafficsim.road.network.lane.Lane;
import org.opentrafficsim.road.network.sampling.GtuData;

import nl.tudelft.simulation.language.Throw;

/**
 * <p>
 * Copyright (c) 2013-2017 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version 28 feb. 2017 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class TimeToCollision extends ExtendedDataTypeDuration
{

    /**
     * 
     */
    public TimeToCollision()
    {
        super("timeToCollision");
    }

    /** {@inheritDoc} */
    @Override
    public final FloatDuration getValue(final GtuDataInterface gtu)
    {
        Throw.when(!(gtu instanceof GtuData), IllegalArgumentException.class,
                "Extended data type ReferenceSpeed can only be used with GtuData.");
        LaneBasedGTU gtuObj = ((GtuData) gtu).getGtu();
        try
        {
            DirectedLanePosition ref = gtuObj.getReferencePosition();
            Map<Lane, GTUDirectionality> map = new HashMap<>();
            map.put(ref.getLane(), ref.getGtuDirection());
            Length pos = ref.getPosition();
            Length cumulDist = Length.ZERO; // from start of lane
            Time now = gtuObj.getSimulator().getSimulatorTime().getTime();
            LaneBasedGTU next = null;
            while (map.size() == 1)
            {
                Lane lane = map.keySet().iterator().next();
                GTUDirectionality dir = map.get(lane);
                if (cumulDist.gt0())
                {
                    pos = dir.isPlus() ? Length.ZERO : lane.getLength();
                }
                next = lane.getGtuAhead(pos, dir, RelativePosition.REAR, now);
                if (next == null)
                {
                    cumulDist = cumulDist.plus(lane.getLength());
                    map = lane.downstreamLanes(dir, gtuObj.getGTUType());
                }
                else
                {
                    // gtu found, calculate TTC
                    if (next.getSpeed().ge(gtuObj.getSpeed()))
                    {
                        return new FloatDuration(Double.NaN, DurationUnit.SI);
                    }
                    Length ownPos = gtuObj.position(ref.getLane(), gtuObj.getFront());
                    Length nextPos = next.position(lane, next.getRear());
                    Length dist = nextPos.minus(ownPos).plus(cumulDist);
                    Speed dv = gtuObj.getSpeed().minus(next.getSpeed());
                    return new FloatDuration(dist.si / dv.si, DurationUnit.SI);
                }
            }
            return new FloatDuration(Float.NaN, DurationUnit.SI);
        }
        catch (GTUException exception)
        {
            // GTU was destroyed and is without a reference location
            return new FloatDuration(Float.NaN, DurationUnit.SI);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "TTC";
    }

}
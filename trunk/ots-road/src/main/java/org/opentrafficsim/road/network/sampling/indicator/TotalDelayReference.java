package org.opentrafficsim.road.network.sampling.indicator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.djunits.unit.TimeUnit;
import org.djunits.value.ValueException;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatSpeed;
import org.djunits.value.vfloat.vector.FloatLengthVector;
import org.opentrafficsim.kpi.sampling.Query;
import org.opentrafficsim.kpi.sampling.SamplingException;
import org.opentrafficsim.kpi.sampling.Trajectory;
import org.opentrafficsim.kpi.sampling.TrajectoryGroup;
import org.opentrafficsim.kpi.sampling.indicator.AbstractIndicator;
import org.opentrafficsim.road.network.sampling.data.ReferenceSpeed;

import nl.tudelft.simulation.language.Throw;

/**
 * <p>
 * Copyright (c) 2013-2016 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version 21 nov. 2016 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */

public class TotalDelayReference extends AbstractIndicator<Duration>
{

    /** Reference speed extended data type. */
    private static final ReferenceSpeed REF_SPEED_TYPE = new ReferenceSpeed();

    /** {@inheritDoc} */
    @Override
    protected final Duration calculate(final Query query, final Time startTime, final Time endTime)
    {
        Map<String, Duration> gtuTimes = new HashMap<>();
        Map<String, Duration> gtuRefTimes = new HashMap<>();
        for (TrajectoryGroup trajectoryGroup : query.getTrajectoryGroups(startTime, endTime))
        {
            try
            {
                for (Trajectory trajectory : trajectoryGroup.getTrajectories())
                {
                    Duration sumTime;
                    Duration sumRefTime;
                    if (gtuTimes.containsKey(trajectory.getGtuId()))
                    {
                        sumTime = gtuTimes.get(trajectory.getGtuId());
                        sumRefTime = gtuRefTimes.get(trajectory.getGtuId());
                    }
                    else
                    {
                        sumTime = Duration.ZERO;
                        sumRefTime = Duration.ZERO;
                    }
                    Throw.when(!trajectory.contains(REF_SPEED_TYPE), UnsupportedOperationException.class,
                            "TotalDelayReference can only work with trajectories that have %s extended data.",
                            REF_SPEED_TYPE.getId());
                    List<FloatSpeed> refSpeed = trajectory.getExtendedData(REF_SPEED_TYPE);
                    FloatLengthVector x = trajectory.getPosition();
                    for (int i = 1; i < refSpeed.size(); i++)
                    {
                        double refV;
                        if (!Double.isNaN(refSpeed.get(i).si))
                        {
                            refV = refSpeed.get(i - 1).si;
                        }
                        else
                        {
                            refV = (refSpeed.get(i - 1).si + refSpeed.get(i).si) / 2.0;
                        }
                        double dx = x.get(i).si - x.get(i - 1).si;
                        sumRefTime = sumRefTime.plus(new Duration(dx / refV, TimeUnit.SI));
                    }
                    gtuTimes.put(trajectory.getGtuId(), sumTime.plus(trajectory.getTotalDuration()));
                    gtuRefTimes.put(trajectory.getGtuId(), sumRefTime);
                }
            }
            catch (SamplingException | ValueException exception)
            {
                throw new RuntimeException("Exception while trying to determine delay in trajectory.", exception);
            }
        }
        Duration delaySum = Duration.ZERO;
        for (String id : gtuTimes.keySet())
        {
            Duration gtuTime = gtuTimes.get(id);
            Duration gtuRefTime = gtuTimes.get(id);
            if (gtuTime.gt(gtuRefTime))
            {
                delaySum = delaySum.plus(gtuTime.minus(gtuRefTime));
            }
        }
        return delaySum;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return "TotalDelayReference";
    }

}

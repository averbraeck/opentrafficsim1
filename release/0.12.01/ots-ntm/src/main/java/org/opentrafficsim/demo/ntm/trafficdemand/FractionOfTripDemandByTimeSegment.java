package org.opentrafficsim.demo.ntm.trafficdemand;

import org.djunits.unit.TimeUnit;
import org.djunits.value.vdouble.scalar.DoubleScalar;

/**
 * <p>
 * Copyright (c) 2013-2016 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate$, @version $Revision$, by $Author$,
 * initial version 12 Sep 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://Hansvanlint.weblog.tudelft.nl">Hans van Lint</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.citg.tudelft.nl">Guus Tamminga</a>
 * @author <a href="http://www.citg.tudelft.nl">Yufei Yuan</a>
 */
public class FractionOfTripDemandByTimeSegment
{

    /** StartTime of the segment in Calendar Time. */
    private DoubleScalar.Abs<TimeUnit> timeSinceMidnight;

    /** Length of a time segment. */
    private DoubleScalar.Rel<TimeUnit> duration;

    /** Relative amount of trips. */
    private double shareOfDemand;

    /**
     * @param timeSinceMidnight by time of day (HH:MM:SS)
     * @param duration length of this time segment
     * @param shareOfDemand amount of trips of this segment relatively to the total simulation period
     */
    public FractionOfTripDemandByTimeSegment(final DoubleScalar.Abs<TimeUnit> timeSinceMidnight,
        final DoubleScalar.Rel<TimeUnit> duration, final double shareOfDemand)
    {
        this.timeSinceMidnight = timeSinceMidnight;
        this.duration = duration;
        this.shareOfDemand = shareOfDemand;
    }

    /**
     * @return shareOfDemand
     */
    public final double getShareOfDemand()
    {
        return this.shareOfDemand;
    }

    /**
     * @param shareOfDemand set shareOfDemand
     */
    public final void setShareOfDemand(final double shareOfDemand)
    {
        this.shareOfDemand = shareOfDemand;
    }

    /**
     * @return timeSinceMidnight
     */
    public final DoubleScalar.Abs<TimeUnit> getTimeSinceMidnight()
    {
        return this.timeSinceMidnight;
    }

    /**
     * @param timeSinceMidnight set timeSinceMidnight
     */
    public final void setTimeSinceMidnight(final DoubleScalar.Abs<TimeUnit> timeSinceMidnight)
    {
        this.timeSinceMidnight = timeSinceMidnight;
    }

    /**
     * @return duration
     */
    public final DoubleScalar.Rel<TimeUnit> getDuration()
    {
        return this.duration;
    }

    /**
     * @param duration set duration
     */
    public final void setDuration(final DoubleScalar.Rel<TimeUnit> duration)
    {
        this.duration = duration;
    }

}

package org.opentrafficsim.road.gtu.animation;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.djunits.value.vdouble.scalar.Duration;
import org.opentrafficsim.base.parameters.ParameterTypes;
import org.opentrafficsim.base.parameters.Parameters;
import org.opentrafficsim.core.gtu.GTU;
import org.opentrafficsim.core.gtu.animation.ColorInterpolator;
import org.opentrafficsim.core.gtu.animation.GTUColorer;

/**
 * Color on a scale from Tmin to Tmax parameters, or two given limits.
 * <p>
 * Copyright (c) 2013-2016 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version 20 apr. 2017 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class DesiredHeadwayColorer implements GTUColorer, Serializable
{

    /** */
    private static final long serialVersionUID = 20170420L;

    /** Minimum value on input scale. */
    public final Duration tMin;

    /** Maximum value on input scale. */
    public final Duration tMax;

    /** The legend. */
    private final List<LegendEntry> legend;

    /** Low color. */
    private static final Color LOW = Color.RED;

    /** Middle color. */
    private static final Color MIDDLE = Color.YELLOW;

    /** High color. */
    private static final Color HIGH = Color.GREEN;

    /** Unknown color. */
    protected static final Color UNKNOWN = Color.WHITE;

    /**
     * Constructor using Tmin and Tmax parameters.
     */
    public DesiredHeadwayColorer()
    {
        this.legend = new ArrayList<>(4);
        this.legend.add(new LegendEntry(LOW, "Tmin", "Tmin"));
        this.legend.add(new LegendEntry(MIDDLE, "Mean", "Mean"));
        this.legend.add(new LegendEntry(HIGH, "Tmax", "Tmax"));
        this.legend.add(new LegendEntry(UNKNOWN, "Unknown", "Unknown"));
        this.tMin = null;
        this.tMax = null;
    }

    /**
     * Constructor using input Tmin and Tmax.
     * @param tMin Duration; minimum headway
     * @param tMax Duration; maximum headway
     */
    public DesiredHeadwayColorer(final Duration tMin, final Duration tMax)
    {
        String format = "%.2fs";
        this.legend = new ArrayList<>(4);
        this.legend.add(new LegendEntry(LOW, String.format(format, tMin.si), "Tmin"));
        this.legend.add(new LegendEntry(MIDDLE, String.format(format, (tMin.si + tMax.si) / 2.0), "Mean"));
        this.legend.add(new LegendEntry(HIGH, String.format(format, tMax.si), "Tmax"));
        this.legend.add(new LegendEntry(UNKNOWN, "Unknown", "Unknown"));
        this.tMin = tMin;
        this.tMax = tMax;
    }

    /** {@inheritDoc} */
    @Override
    public final Color getColor(final GTU gtu)
    {
        Parameters params = gtu.getParameters();
        Double minT;
        Double maxT;
        if (this.tMin == null)
        {
            minT = params.getParameterOrNull(ParameterTypes.TMIN).si;
            maxT = params.getParameterOrNull(ParameterTypes.TMAX).si;
        }
        else
        {
            minT = this.tMin.si;
            maxT = this.tMax.si;
        }
        Double t = params.getParameterOrNull(ParameterTypes.T).si;
        if (minT == null || maxT == null || t == null)
        {
            return UNKNOWN;
        }
        if (t <= minT)
        {
            return LOW;
        }
        if (t >= maxT)
        {
            return HIGH;
        }
        double tMean = (minT + maxT) / 2.0;
        if (t < tMean)
        {
            return ColorInterpolator.interpolateColor(LOW, MIDDLE, (t - minT) / (tMean - minT));
        }
        return ColorInterpolator.interpolateColor(MIDDLE, HIGH, (t - tMean) / (maxT - tMean));
    }

    /** {@inheritDoc} */
    @Override
    public final List<LegendEntry> getLegend()
    {
        return this.legend;
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        return "Desired headway";
    }

}

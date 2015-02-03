package org.opentrafficsim.core.value.vfloat.scalar;

import nl.tudelft.simulation.jstats.distributions.DistContinuous;

import org.opentrafficsim.core.unit.Unit;
import org.opentrafficsim.core.value.Absolute;

/**
 * <p>
 * Copyright (c) 2013-2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version Feb 2, 2015 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <U> the unit type.
 */
public abstract class DistContinuousFloatScalar<U extends Unit<U>>
{
    /** the wrapped distribution function. */
    private final DistContinuous distribution;

    /** the unit. */
    private final U unit;

    /**
     * @param distribution the wrapped distribution function.
     * @param unit the unit.
     */
    protected DistContinuousFloatScalar(final DistContinuous distribution, final U unit)
    {
        super();
        this.distribution = distribution;
        this.unit = unit;
    }

    /**
     * @return distribution.
     */
    public final DistContinuous getDistribution()
    {
        return this.distribution;
    }

    /**
     * @return unit.
     */
    public final U getUnit()
    {
        return this.unit;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return "DistFloatScalar [distribution=" + this.distribution + ", unit=" + this.unit + "]";
    }

    /**
     * Absolute value.
     * @param <U> Unit
     */
    public static class Abs<U extends Unit<U>> extends DistContinuousFloatScalar<U> implements Absolute
    {
        /**
         * @param distribution the wrapped distribution function.
         * @param unit the unit.
         */
        protected Abs(final DistContinuous distribution, final U unit)
        {
            super(distribution, unit);
        }

        /**
         * @return a drawn number from the distribution in the given unit.
         */
        public final FloatScalar.Abs<U> draw()
        {
            return new FloatScalar.Abs<U>((float) getDistribution().draw(), getUnit());
        }
    }

    /**
     * Relative value.
     * @param <U> Unit
     */
    public static class Rel<U extends Unit<U>> extends DistContinuousFloatScalar<U> implements Absolute
    {
        /**
         * @param distribution the wrapped distribution function.
         * @param unit the unit.
         */
        protected Rel(final DistContinuous distribution, final U unit)
        {
            super(distribution, unit);
        }

        /**
         * @return a drawn number from the distribution in the given unit.
         */
        public final FloatScalar.Rel<U> draw()
        {
            return new FloatScalar.Rel<U>((float) getDistribution().draw(), getUnit());
        }
    }

}

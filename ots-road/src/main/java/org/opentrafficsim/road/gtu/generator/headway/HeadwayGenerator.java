package org.opentrafficsim.road.gtu.generator.headway;

import org.djunits.unit.DurationUnit;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Frequency;
import org.opentrafficsim.base.parameters.ParameterException;
import org.opentrafficsim.core.distributions.Generator;
import org.opentrafficsim.core.distributions.ProbabilityException;
import org.opentrafficsim.core.dsol.OtsSimulatorInterface;

/**
 * <p>
 * Copyright (c) 2013-2023 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved.
 * <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 * @author <a href="https://dittlab.tudelft.nl">Wouter Schakel</a>
 */
public class HeadwayGenerator implements Generator<Duration>
{
    /** the simulator. */
    private final OtsSimulatorInterface simulator;

    /** Demand level. */
    private final Frequency demand;

    /**
     * @param simulator OtsSimulatorInterface; the simulator
     * @param demand Frequency; demand
     */
    HeadwayGenerator(final OtsSimulatorInterface simulator, final Frequency demand)
    {
        this.simulator = simulator;
        this.demand = demand;
    }

    /** {@inheritDoc} */
    @Override
    public Duration draw() throws ProbabilityException, ParameterException
    {
        return new Duration(
                -Math.log(this.simulator.getModel().getStream("headwayGeneration").nextDouble()) / this.demand.si,
                DurationUnit.SI);
    }

}
package org.opentrafficsim.core.units.distributions;

import org.djunits.unit.ElectricalResistanceUnit;
import org.djunits.value.vdouble.scalar.ElectricalResistance;

import nl.tudelft.simulation.jstats.distributions.DistContinuous;

/**
 * Continuously distributed electrical resistance.
 * <p>
 * Copyright (c) 2013-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
// This class was automatically generated
public class ContinuousDistElectricalResistance
        extends ContinuousDistDoubleScalar.Rel<ElectricalResistance, ElectricalResistanceUnit>
{

    /** */
    private static final long serialVersionUID = 20180829L;

    /**
     * @param distribution distribution
     * @param unit units
     */
    public ContinuousDistElectricalResistance(final DistContinuous distribution, final ElectricalResistanceUnit unit)
    {
        super(distribution, unit);

    }

    @Override
    public ElectricalResistance draw()
    {
        return new ElectricalResistance(getDistribution().draw(), (ElectricalResistanceUnit) getDisplayUnit());
    }

    @Override
    public final String toString()
    {
        return "ContinuousDistElectricalResistance []";
    }

}

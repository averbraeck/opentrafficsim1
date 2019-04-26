package org.opentrafficsim.core.gtu;

import java.io.Serializable;

import org.djunits.unit.LengthUnit;
import org.djunits.unit.SpeedUnit;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Speed;
import org.opentrafficsim.core.dsol.OTSDEVSSimulatorInterface;
import org.opentrafficsim.core.units.distributions.ContinuousDistDoubleScalar;

/**
 * TemplateGTUType stores most of the information that is needed to generate a GTU.
 * <p>
 * Copyright (c) 2013-2015 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate$, @version $Revision$, by $Author$,
 * initial version Jul 8, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class TemplateGTUType implements Serializable
{
    /** */
    private static final long serialVersionUID = 20141230L;

    /** The type of the GTU. */
    private final GTUType gtuType;

    /** distribution of the length of the GTU. */
    private final ContinuousDistDoubleScalar.Rel<Length.Rel, LengthUnit> lengthDist;

    /** distribution of the width of the GTU. */
    private final ContinuousDistDoubleScalar.Rel<Length.Rel, LengthUnit> widthDist;

    /** distribution of the maximum speed of the GTU. */
    private final ContinuousDistDoubleScalar.Rel<Speed, SpeedUnit> maximumSpeedDist;

    /** the simulator. */
    private final OTSDEVSSimulatorInterface simulator;

    /**
     * @param id The id of the GTUType to make it identifiable.
     * @param lengthDist the length of the GTU type (parallel with driving direction).
     * @param widthDist the width of the GTU type (perpendicular to driving direction).
     * @param maximumSpeedDist the maximum speed of the GTU type (in the driving direction).
     * @param simulator the simulator.
     * @throws GTUException when GTUType defined more than once
     */
    public TemplateGTUType(final String id, final ContinuousDistDoubleScalar.Rel<Length.Rel, LengthUnit> lengthDist,
        final ContinuousDistDoubleScalar.Rel<Length.Rel, LengthUnit> widthDist,
        final ContinuousDistDoubleScalar.Rel<Speed, SpeedUnit> maximumSpeedDist,
        final OTSDEVSSimulatorInterface simulator) throws GTUException
    {
        this.gtuType = GTUType.makeGTUType(id);
        this.lengthDist = lengthDist;
        this.widthDist = widthDist;
        this.maximumSpeedDist = maximumSpeedDist;
        this.simulator = simulator;
    }

    /**
     * @return length.
     */
    public final Length.Rel getLength()
    {
        return this.lengthDist.draw();
    }

    /**
     * @return width.
     */
    public final Length.Rel getWidth()
    {
        return this.widthDist.draw();
    }

    /**
     * @return maximumVelocity.
     */
    public final Speed getMaximumVelocity()
    {
        return this.maximumSpeedDist.draw();
    }

    /**
     * @return simulator.
     */
    public final OTSDEVSSimulatorInterface getSimulator()
    {
        return this.simulator;
    }

    /**
     * @return gtuType.
     */
    @SuppressWarnings("checkstyle:designforextension")
    public GTUType getGtuType()
    {
        return this.gtuType;
    }
}
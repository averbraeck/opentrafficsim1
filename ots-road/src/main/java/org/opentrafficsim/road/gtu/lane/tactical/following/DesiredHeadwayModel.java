package org.opentrafficsim.road.gtu.lane.tactical.following;

import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Speed;
import org.opentrafficsim.base.parameters.ParameterException;
import org.opentrafficsim.base.parameters.Parameters;

/**
 * Desired headway model.
 * <p>
 * Copyright (c) 2013-2023 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 * @author <a href="https://dittlab.tudelft.nl">Wouter Schakel</a>
 */
public interface DesiredHeadwayModel
{

    /**
     * Determines the desired headway in equilibrium conditions, i.e. no speed difference with the leader.
     * @param parameters Parameters; parameters
     * @param speed Speed; speed to determine the desired headway at
     * @throws ParameterException if parameter exception occurs
     * @return desired headway
     */
    Length desiredHeadway(Parameters parameters, Speed speed) throws ParameterException;

}

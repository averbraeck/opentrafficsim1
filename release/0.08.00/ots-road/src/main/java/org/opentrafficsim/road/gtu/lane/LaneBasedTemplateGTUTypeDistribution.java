package org.opentrafficsim.road.gtu.lane;

import org.opentrafficsim.core.distributions.Distribution;
import org.opentrafficsim.core.distributions.ProbabilityException;
import org.opentrafficsim.core.dsol.OTSDEVSSimulatorInterface;

/**
 * Distribution of LaneBasedTemplateGTUType.
 * <p>
 * Copyright (c) 2013-2015 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Mar 9, 2016 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class LaneBasedTemplateGTUTypeDistribution implements LaneBasedGTUCharacteristicsGenerator
{

    /** The Distribution of lane based template GTU types. */
    private final Distribution<LaneBasedTemplateGTUType> distribution;

    /**
     * Construct a new LaneBasedTemplateGTUTypeDistribution.
     * @param distributionOfLanebasedTemplateGTUType the distribution of LaneBasedTemplateGTUTypes
     */
    public LaneBasedTemplateGTUTypeDistribution(
            final Distribution<LaneBasedTemplateGTUType> distributionOfLanebasedTemplateGTUType)
    {
        this.distribution = distributionOfLanebasedTemplateGTUType;
    }

    /** {@inheritDoc} */
    @Override
    public LaneBasedGTUCharacteristics draw() throws ProbabilityException
    {
        return this.distribution.draw().draw();
    }

    /** {@inheritDoc} */
    @Override
    public OTSDEVSSimulatorInterface getSimulator() throws ProbabilityException
    {
        return this.distribution.get(0).getObject().getSimulator();
    }

}

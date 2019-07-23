package org.opentrafficsim.road.gtu.lane.tactical.lmrs;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import org.opentrafficsim.core.gtu.GTUException;
import org.opentrafficsim.core.gtu.behavioralcharacteristics.BehavioralCharacteristics;
import org.opentrafficsim.core.gtu.behavioralcharacteristics.ParameterTypes;
import org.opentrafficsim.road.gtu.lane.LaneBasedGTU;
import org.opentrafficsim.road.gtu.lane.perception.PerceptionFactory;
import org.opentrafficsim.road.gtu.lane.tactical.LaneBasedTacticalPlannerFactory;
import org.opentrafficsim.road.gtu.lane.tactical.following.CarFollowingModel;
import org.opentrafficsim.road.gtu.lane.tactical.following.CarFollowingModelFactory;
import org.opentrafficsim.road.gtu.lane.tactical.util.ConflictUtil;
import org.opentrafficsim.road.gtu.lane.tactical.util.lmrs.LmrsParameters;
import org.opentrafficsim.road.gtu.lane.tactical.util.lmrs.MandatoryIncentive;
import org.opentrafficsim.road.gtu.lane.tactical.util.lmrs.Synchronization;
import org.opentrafficsim.road.gtu.lane.tactical.util.lmrs.VoluntaryIncentive;

/**
 * Factory for a tactical planner using LMRS with any car-following model.
 * <p>
 * Copyright (c) 2013-2017 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/current/license.html">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Aug 2, 2016 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */

public class LMRSFactory implements LaneBasedTacticalPlannerFactory<LMRS>, Serializable
{

    /** */
    private static final long serialVersionUID = 20160811L;

    /** Constructor for the car-following model. */
    private final CarFollowingModelFactory<? extends CarFollowingModel> carFollowingModelFactory;

    /** Default set of parameters for the car-following model. */
    private final BehavioralCharacteristics defaultCarFollowingBehavioralCharacteristics;

    /** Factory for perception. */
    private final PerceptionFactory perceptionFactory;

    /** Type of synchronization. */
    private final Synchronization synchronization;

    /** Mandatory incentives. */
    private final Set<MandatoryIncentive> mandatoryIncentives = new LinkedHashSet<>();

    /** Mandatory incentives. */
    private final Set<VoluntaryIncentive> voluntaryIncentives = new LinkedHashSet<>();

    /** Mandatory incentives. */
    private final Set<AccelerationIncentive> accelerationIncentives = new LinkedHashSet<>();

    /**
     * Constructor using default incentives and passive synchronization.
     * @param carFollowingModelFactory factory of the car-following model
     * @param defaultCarFollowingBehavioralCharacteristics default set of parameters for the car-following model
     * @param perceptionFactory perception factory
     * @throws GTUException if the supplied car-following model does not have an accessible empty constructor
     */
    public LMRSFactory(final CarFollowingModelFactory<? extends CarFollowingModel> carFollowingModelFactory,
            final BehavioralCharacteristics defaultCarFollowingBehavioralCharacteristics,
            final PerceptionFactory perceptionFactory) throws GTUException
    {
        this.carFollowingModelFactory = carFollowingModelFactory;
        this.defaultCarFollowingBehavioralCharacteristics = defaultCarFollowingBehavioralCharacteristics;
        this.perceptionFactory = perceptionFactory;
        this.synchronization = Synchronization.PASSIVE;
    }

    /**
     * Constructor with full control over incentives and type of synchronization.
     * @param carFollowingModelFactory factory of the car-following model
     * @param defaultCarFollowingBehavioralCharacteristics default set of parameters for the car-following model
     * @param perceptionFactory perception factory
     * @param synchronization type of synchronization
     * @param mandatoryIncentives mandatory incentives
     * @param voluntaryIncentives voluntary incentives
     * @param accelerationIncentives acceleration incentives
     * @throws GTUException if the supplied car-following model does not have an accessible empty constructor
     */
    public LMRSFactory(final CarFollowingModelFactory<? extends CarFollowingModel> carFollowingModelFactory,
            final BehavioralCharacteristics defaultCarFollowingBehavioralCharacteristics,
            final PerceptionFactory perceptionFactory, final Synchronization synchronization,
            final Set<MandatoryIncentive> mandatoryIncentives, final Set<VoluntaryIncentive> voluntaryIncentives,
            final Set<AccelerationIncentive> accelerationIncentives) throws GTUException
    {
        this.carFollowingModelFactory = carFollowingModelFactory;
        this.defaultCarFollowingBehavioralCharacteristics = defaultCarFollowingBehavioralCharacteristics;
        this.perceptionFactory = perceptionFactory;
        this.synchronization = synchronization;
        this.mandatoryIncentives.addAll(mandatoryIncentives);
        this.voluntaryIncentives.addAll(voluntaryIncentives);
        this.accelerationIncentives.addAll(accelerationIncentives);
    }

    /** {@inheritDoc} */
    @Override
    public final BehavioralCharacteristics getDefaultBehavioralCharacteristics()
    {
        BehavioralCharacteristics behavioralCharacteristics = new BehavioralCharacteristics();
        behavioralCharacteristics.setDefaultParameters(ParameterTypes.class);
        behavioralCharacteristics.setDefaultParameters(LmrsParameters.class);
        behavioralCharacteristics.setDefaultParameters(ConflictUtil.class);
        behavioralCharacteristics.setAll(this.defaultCarFollowingBehavioralCharacteristics);
        return behavioralCharacteristics;
    }

    /** {@inheritDoc} */
    @Override
    public final LMRS create(final LaneBasedGTU gtu) throws GTUException
    {

        LMRS lmrs = new LMRS(this.carFollowingModelFactory.generateCarFollowingModel(), gtu,
                this.perceptionFactory.generatePerception(gtu), this.synchronization);
        if (this.mandatoryIncentives.isEmpty())
        {
            lmrs.setDefaultIncentives();
        }
        else
        {
            this.mandatoryIncentives.forEach(incentive -> lmrs.addMandatoryIncentive(incentive));
            this.voluntaryIncentives.forEach(incentive -> lmrs.addVoluntaryIncentive(incentive));
            this.accelerationIncentives.forEach(incentive -> lmrs.addAccelerationIncentive(incentive));
        }
        return lmrs;
    }

    /** {@inheritDoc} */
    public final String toString()
    {
        return "LMRSFactory [car-following=" + this.carFollowingModelFactory + "]";
    }

}
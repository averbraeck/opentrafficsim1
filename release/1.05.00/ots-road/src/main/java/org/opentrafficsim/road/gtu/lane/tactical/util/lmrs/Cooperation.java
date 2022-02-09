package org.opentrafficsim.road.gtu.lane.tactical.util.lmrs;

import org.djunits.unit.AccelerationUnit;
import org.djunits.value.vdouble.scalar.Acceleration;
import org.djunits.value.vdouble.scalar.Speed;
import org.opentrafficsim.base.parameters.ParameterException;
import org.opentrafficsim.base.parameters.ParameterTypes;
import org.opentrafficsim.base.parameters.Parameters;
import org.opentrafficsim.core.gtu.perception.EgoPerception;
import org.opentrafficsim.core.gtu.plan.operational.OperationalPlanException;
import org.opentrafficsim.core.network.LateralDirectionality;
import org.opentrafficsim.road.gtu.lane.LaneBasedGTU;
import org.opentrafficsim.road.gtu.lane.perception.LanePerception;
import org.opentrafficsim.road.gtu.lane.perception.PerceptionCollectable;
import org.opentrafficsim.road.gtu.lane.perception.RelativeLane;
import org.opentrafficsim.road.gtu.lane.perception.categories.neighbors.NeighborsPerception;
import org.opentrafficsim.road.gtu.lane.perception.headway.HeadwayGTU;
import org.opentrafficsim.road.gtu.lane.tactical.following.CarFollowingModel;
import org.opentrafficsim.road.network.speed.SpeedLimitInfo;

/**
 * Different forms of cooperation.
 * <p>
 * Copyright (c) 2013-2022 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version 2 mrt. 2018 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public interface Cooperation extends LmrsParameters
{

    /** Simple passive cooperation. */
    Cooperation PASSIVE = new Cooperation()
    {
        @Override
        public Acceleration cooperate(final LanePerception perception, final Parameters params, final SpeedLimitInfo sli,
                final CarFollowingModel cfm, final LateralDirectionality lat, final Desire ownDesire)
                throws ParameterException, OperationalPlanException
        {
            if ((lat.isLeft() && !perception.getLaneStructure().getExtendedCrossSection().contains(RelativeLane.LEFT))
                    || (lat.isRight() && !perception.getLaneStructure().getExtendedCrossSection().contains(RelativeLane.RIGHT)))
            {
                return new Acceleration(Double.MAX_VALUE, AccelerationUnit.SI);
            }
            Acceleration b = params.getParameter(ParameterTypes.B);
            Acceleration a = new Acceleration(Double.MAX_VALUE, AccelerationUnit.SI);
            double dCoop = params.getParameter(DCOOP);
            Speed ownSpeed = perception.getPerceptionCategory(EgoPerception.class).getSpeed();
            RelativeLane relativeLane = new RelativeLane(lat, 1);
            for (HeadwayGTU leader : Synchronization.removeAllUpstreamOfConflicts(Synchronization.removeAllUpstreamOfConflicts(
                    perception.getPerceptionCategory(NeighborsPerception.class).getLeaders(relativeLane), perception,
                    relativeLane), perception, RelativeLane.CURRENT))
            {
                Parameters params2 = leader.getParameters();
                double desire = lat.equals(LateralDirectionality.LEFT) ? params2.getParameter(DRIGHT)
                        : lat.equals(LateralDirectionality.RIGHT) ? params2.getParameter(DLEFT) : 0;
                if (desire >= dCoop && (leader.getSpeed().gt0() || leader.getDistance().gt0()))
                {
                    Acceleration aSingle = LmrsUtil.singleAcceleration(leader.getDistance(), ownSpeed, leader.getSpeed(),
                            desire, params, sli, cfm);
                    a = Acceleration.min(a, aSingle);
                }
            }
            return Acceleration.max(a, b.neg());
        }
    };

    /** Same as passive cooperation, except that cooperation is fully ignored if the potential lane changer brakes heavily. */
    Cooperation PASSIVE_MOVING = new Cooperation()
    {
        @Override
        public Acceleration cooperate(final LanePerception perception, final Parameters params, final SpeedLimitInfo sli,
                final CarFollowingModel cfm, final LateralDirectionality lat, final Desire ownDesire)
                throws ParameterException, OperationalPlanException
        {
            if ((lat.isLeft() && !perception.getLaneStructure().getExtendedCrossSection().contains(RelativeLane.LEFT))
                    || (lat.isRight() && !perception.getLaneStructure().getExtendedCrossSection().contains(RelativeLane.RIGHT)))
            {
                return new Acceleration(Double.MAX_VALUE, AccelerationUnit.SI);
            }
            Acceleration bCrit = params.getParameter(ParameterTypes.BCRIT);
            Acceleration b = params.getParameter(ParameterTypes.B);
            Acceleration a = new Acceleration(Double.MAX_VALUE, AccelerationUnit.SI);
            double dCoop = params.getParameter(DCOOP);
            Speed ownSpeed = perception.getPerceptionCategory(EgoPerception.class).getSpeed();
            RelativeLane relativeLane = new RelativeLane(lat, 1);
            NeighborsPerception neighbours = perception.getPerceptionCategory(NeighborsPerception.class);
            PerceptionCollectable<HeadwayGTU, LaneBasedGTU> leaders = neighbours.getLeaders(RelativeLane.CURRENT);
            Speed thresholdSpeed = Speed.instantiateSI(40.0 / 3.6);
            boolean leaderInCongestion = leaders.isEmpty() ? false : leaders.first().getSpeed().lt(thresholdSpeed);
            for (HeadwayGTU leader : Synchronization.removeAllUpstreamOfConflicts(
                    Synchronization.removeAllUpstreamOfConflicts(neighbours.getLeaders(relativeLane), perception, relativeLane),
                    perception, RelativeLane.CURRENT))
            {
                Parameters params2 = leader.getParameters();
                double desire = lat.equals(LateralDirectionality.LEFT) ? params2.getParameter(DRIGHT)
                        : lat.equals(LateralDirectionality.RIGHT) ? params2.getParameter(DLEFT) : 0;
                // TODO: only cooperate if merger still quite fast or there's congestion downstream anyway (which we can better
                // estimate than only considering the direct leader
                if (desire >= dCoop && (leader.getSpeed().gt0() || leader.getDistance().gt0())
                        && (leader.getSpeed().ge(thresholdSpeed) || leaderInCongestion))
                {
                    Acceleration aSingle = LmrsUtil.singleAcceleration(leader.getDistance(), ownSpeed, leader.getSpeed(),
                            desire, params, sli, cfm);
                    a = Acceleration.min(a, aSingle);
                }
            }
            return Acceleration.max(a, bCrit.neg());
        }
    };

    /** Cooperation similar to the default, with nuanced differences of when to ignore. */
    Cooperation ACTIVE = new Cooperation()
    {
        @Override
        public Acceleration cooperate(final LanePerception perception, final Parameters params, final SpeedLimitInfo sli,
                final CarFollowingModel cfm, final LateralDirectionality lat, final Desire ownDesire)
                throws ParameterException, OperationalPlanException
        {

            if ((lat.isLeft() && !perception.getLaneStructure().getExtendedCrossSection().contains(RelativeLane.LEFT))
                    || (lat.isRight() && !perception.getLaneStructure().getExtendedCrossSection().contains(RelativeLane.RIGHT)))
            {
                return new Acceleration(Double.MAX_VALUE, AccelerationUnit.SI);
            }
            Acceleration a = new Acceleration(Double.MAX_VALUE, AccelerationUnit.SI);
            double dCoop = params.getParameter(DCOOP);
            Speed ownSpeed = perception.getPerceptionCategory(EgoPerception.class).getSpeed();
            RelativeLane relativeLane = new RelativeLane(lat, 1);
            for (HeadwayGTU leader : Synchronization.removeAllUpstreamOfConflicts(Synchronization.removeAllUpstreamOfConflicts(
                    perception.getPerceptionCategory(NeighborsPerception.class).getLeaders(relativeLane), perception,
                    relativeLane), perception, RelativeLane.CURRENT))
            {
                Parameters params2 = leader.getParameters();
                double desire = lat.equals(LateralDirectionality.LEFT) ? params2.getParameter(DRIGHT)
                        : lat.equals(LateralDirectionality.RIGHT) ? params2.getParameter(DLEFT) : 0;
                if (desire >= dCoop && leader.getDistance().gt0()
                        && leader.getAcceleration().gt(params.getParameter(ParameterTypes.BCRIT).neg()))
                {
                    Acceleration aSingle = LmrsUtil.singleAcceleration(leader.getDistance(), ownSpeed, leader.getSpeed(),
                            desire, params, sli, cfm);
                    a = Acceleration.min(a, Synchronization.gentleUrgency(aSingle, desire, params));
                }
            }
            return a;
        }
    };

    /**
     * Determine acceleration for cooperation.
     * @param perception LanePerception; perception
     * @param params Parameters; parameters
     * @param sli SpeedLimitInfo; speed limit info
     * @param cfm CarFollowingModel; car-following model
     * @param lat LateralDirectionality; lateral direction for cooperation
     * @param ownDesire Desire; own lane change desire
     * @return acceleration for synchronization
     * @throws ParameterException if a parameter is not defined
     * @throws OperationalPlanException perception exception
     */
    Acceleration cooperate(LanePerception perception, Parameters params, SpeedLimitInfo sli, CarFollowingModel cfm,
            LateralDirectionality lat, Desire ownDesire) throws ParameterException, OperationalPlanException;
}

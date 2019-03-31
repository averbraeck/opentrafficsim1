package org.opentrafficsim.road.gtu.lane.tactical.util.lmrs;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.SortedSet;

import org.djunits.value.vdouble.scalar.Acceleration;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Speed;
import org.djunits.value.vdouble.scalar.Time;
import org.djutils.exceptions.Try;
import org.opentrafficsim.base.parameters.ParameterException;
import org.opentrafficsim.base.parameters.ParameterTypeAcceleration;
import org.opentrafficsim.base.parameters.ParameterTypeDuration;
import org.opentrafficsim.base.parameters.ParameterTypes;
import org.opentrafficsim.base.parameters.Parameters;
import org.opentrafficsim.core.gtu.GTUException;
import org.opentrafficsim.core.gtu.TurnIndicatorIntent;
import org.opentrafficsim.core.gtu.perception.EgoPerception;
import org.opentrafficsim.core.gtu.plan.operational.OperationalPlanException;
import org.opentrafficsim.core.network.LateralDirectionality;
import org.opentrafficsim.core.network.NetworkException;
import org.opentrafficsim.road.gtu.lane.LaneBasedGTU;
import org.opentrafficsim.road.gtu.lane.perception.InfrastructureLaneChangeInfo;
import org.opentrafficsim.road.gtu.lane.perception.LanePerception;
import org.opentrafficsim.road.gtu.lane.perception.PerceptionCollectable;
import org.opentrafficsim.road.gtu.lane.perception.RelativeLane;
import org.opentrafficsim.road.gtu.lane.perception.categories.InfrastructurePerception;
import org.opentrafficsim.road.gtu.lane.perception.categories.IntersectionPerception;
import org.opentrafficsim.road.gtu.lane.perception.categories.neighbors.NeighborsPerception;
import org.opentrafficsim.road.gtu.lane.perception.headway.HeadwayConflict;
import org.opentrafficsim.road.gtu.lane.perception.headway.HeadwayGTU;
import org.opentrafficsim.road.gtu.lane.perception.headway.HeadwayTrafficLight;
import org.opentrafficsim.road.gtu.lane.plan.operational.LaneChange;
import org.opentrafficsim.road.gtu.lane.plan.operational.SimpleOperationalPlan;
import org.opentrafficsim.road.gtu.lane.tactical.Synchronizable;
import org.opentrafficsim.road.gtu.lane.tactical.following.CarFollowingModel;
import org.opentrafficsim.road.gtu.lane.tactical.util.CarFollowingUtil;
import org.opentrafficsim.road.network.speed.SpeedLimitInfo;
import org.opentrafficsim.road.network.speed.SpeedLimitProspect;

/**
 * <p>
 * Copyright (c) 2013-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/current/license.html">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Jul 26, 2016 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public final class LmrsUtil implements LmrsParameters
{

    /** Fixed model time step. */
    public static final ParameterTypeDuration DT = ParameterTypes.DT;

    /** Minimum car-following headway. */
    public static final ParameterTypeDuration TMIN = ParameterTypes.TMIN;

    /** Current car-following headway. */
    public static final ParameterTypeDuration T = ParameterTypes.T;

    /** Maximum car-following headway. */
    public static final ParameterTypeDuration TMAX = ParameterTypes.TMAX;

    /** Headway relaxation time. */
    public static final ParameterTypeDuration TAU = ParameterTypes.TAU;

    /** Maximum critical deceleration, e.g. stop/go at traffic light. */
    public static final ParameterTypeAcceleration BCRIT = ParameterTypes.BCRIT;

    /**
     * Do not instantiate.
     */
    private LmrsUtil()
    {
        //
    }

    /**
     * Determines a simple representation of an operational plan.
     * @param gtu LaneBasedGTU; gtu
     * @param startTime Time; start time
     * @param carFollowingModel CarFollowingModel; car-following model
     * @param laneChange LaneChange; lane change status
     * @param lmrsData LmrsData; LMRS data
     * @param perception LanePerception; perception
     * @param mandatoryIncentives LinkedHashSet&lt;MandatoryIncentive&gt;; set of mandatory lane change incentives
     * @param voluntaryIncentives LinkedHashSet&lt;VoluntaryIncentive&gt;; set of voluntary lane change incentives
     * @return simple operational plan
     * @throws GTUException gtu exception
     * @throws NetworkException network exception
     * @throws ParameterException parameter exception
     * @throws OperationalPlanException operational plan exception
     */
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:methodlength"})
    public static SimpleOperationalPlan determinePlan(final LaneBasedGTU gtu, final Time startTime,
            final CarFollowingModel carFollowingModel, final LaneChange laneChange, final LmrsData lmrsData,
            final LanePerception perception, final LinkedHashSet<MandatoryIncentive> mandatoryIncentives,
            final LinkedHashSet<VoluntaryIncentive> voluntaryIncentives)
            throws GTUException, NetworkException, ParameterException, OperationalPlanException
    {

        // obtain objects to get info
        InfrastructurePerception infra = perception.getPerceptionCategory(InfrastructurePerception.class);
        SpeedLimitProspect slp = infra.getSpeedLimitProspect(RelativeLane.CURRENT);
        SpeedLimitInfo sli = slp.getSpeedLimitInfo(Length.ZERO);
        Parameters params = gtu.getParameters();
        EgoPerception<?, ?> ego = perception.getPerceptionCategory(EgoPerception.class);
        Speed speed = ego.getSpeed();
        NeighborsPerception neighbors = perception.getPerceptionCategory(NeighborsPerception.class);
        PerceptionCollectable<HeadwayGTU, LaneBasedGTU> leaders = neighbors.getLeaders(RelativeLane.CURRENT);

        // regular car-following
        Acceleration a;
        if (lmrsData.isHumanLongitudinalControl())
        {
            lmrsData.getTailGating().tailgate(perception, params);
            if (!leaders.isEmpty() && lmrsData.isNewLeader(leaders.first()))
            {
                initHeadwayRelaxation(params, leaders.first());
            }
            a = gtu.getCarFollowingAcceleration();
        }
        else
        {
            a = Acceleration.POS_MAXVALUE;
        }

        // during a lane change, both leaders are followed
        LateralDirectionality initiatedLaneChange;
        TurnIndicatorIntent turnIndicatorStatus = TurnIndicatorIntent.NONE;
        if (laneChange.isChangingLane())
        {
            RelativeLane secondLane = laneChange.getSecondLane(gtu);
            initiatedLaneChange = LateralDirectionality.NONE;
            PerceptionCollectable<HeadwayGTU, LaneBasedGTU> secondLeaders = neighbors.getLeaders(secondLane);
            Acceleration aSecond = carFollowingModel.followingAcceleration(params, speed, sli, secondLeaders);
            if (!secondLeaders.isEmpty() && lmrsData.isNewLeader(secondLeaders.first()))
            {
                initHeadwayRelaxation(params, secondLeaders.first());
            }
            a = Acceleration.min(a, aSecond);
        }
        else
        {

            // determine lane change desire based on incentives
            Desire desire = getLaneChangeDesire(params, perception, carFollowingModel, mandatoryIncentives, voluntaryIncentives,
                    lmrsData.getDesireMap());

            // lane change decision
            double dFree = params.getParameter(DFREE);
            initiatedLaneChange = LateralDirectionality.NONE;
            turnIndicatorStatus = TurnIndicatorIntent.NONE;
            if (desire.leftIsLargerOrEqual() && desire.getLeft() >= dFree)
            {
                if (acceptLaneChange(perception, params, sli, carFollowingModel, desire.getLeft(), speed, a,
                        LateralDirectionality.LEFT, lmrsData.getGapAcceptance()))
                {
                    // change left
                    initiatedLaneChange = LateralDirectionality.LEFT;
                    turnIndicatorStatus = TurnIndicatorIntent.LEFT;
                    params.setParameter(DLC, desire.getLeft());
                    setDesiredHeadway(params, desire.getLeft());
                    leaders = neighbors.getLeaders(RelativeLane.LEFT);
                    if (!leaders.isEmpty())
                    {
                        // don't respond on its lane change desire, but remember it such that it isn't a new leader in the next
                        // step
                        lmrsData.isNewLeader(leaders.first());
                    }
                    a = Acceleration.min(a, carFollowingModel.followingAcceleration(params, speed, sli,
                            neighbors.getLeaders(RelativeLane.LEFT)));
                }
            }
            else if (!desire.leftIsLargerOrEqual() && desire.getRight() >= dFree)
            {
                if (acceptLaneChange(perception, params, sli, carFollowingModel, desire.getRight(), speed, a,
                        LateralDirectionality.RIGHT, lmrsData.getGapAcceptance()))
                {
                    // change right
                    initiatedLaneChange = LateralDirectionality.RIGHT;
                    turnIndicatorStatus = TurnIndicatorIntent.RIGHT;
                    params.setParameter(DLC, desire.getRight());
                    setDesiredHeadway(params, desire.getRight());
                    leaders = neighbors.getLeaders(RelativeLane.RIGHT);
                    if (!leaders.isEmpty())
                    {
                        // don't respond on its lane change desire, but remember it such that it isn't a new leader in the next
                        // step
                        lmrsData.isNewLeader(leaders.first());
                    }
                    a = Acceleration.min(a, carFollowingModel.followingAcceleration(params, speed, sli,
                            neighbors.getLeaders(RelativeLane.RIGHT)));
                }
            }
            if (!initiatedLaneChange.isNone())
            {
                SortedSet<InfrastructureLaneChangeInfo> set = infra.getInfrastructureLaneChangeInfo(RelativeLane.CURRENT);
                if (!set.isEmpty())
                {
                    Length boundary = null;
                    for (InfrastructureLaneChangeInfo info : set)
                    {
                        int n = info.getRequiredNumberOfLaneChanges();
                        if (n > 1)
                        {
                            Length thisBoundary = info.getRemainingDistance()
                                    .minus(Synchronization.requiredBufferSpace(speed, info.getRequiredNumberOfLaneChanges(),
                                            params.getParameter(ParameterTypes.LOOKAHEAD),
                                            params.getParameter(ParameterTypes.T0), params.getParameter(ParameterTypes.LCDUR),
                                            params.getParameter(DCOOP)));
                            if (thisBoundary.le0())
                            {
                                thisBoundary = info.getRemainingDistance().divideBy(info.getRequiredNumberOfLaneChanges());
                            }
                            boundary = boundary == null || thisBoundary.si < boundary.si ? thisBoundary : boundary;
                        }
                    }
                    laneChange.setBoundary(boundary);
                }
                params.setParameter(DLEFT, 0.0);
                params.setParameter(DRIGHT, 0.0);
            }
            else
            {
                params.setParameter(DLEFT, desire.getLeft());
                params.setParameter(DRIGHT, desire.getRight());
            }

            // take action if we cannot change lane
            Acceleration aSync;

            // synchronize
            double dSync = params.getParameter(DSYNC);
            if (desire.leftIsLargerOrEqual() && desire.getLeft() >= dSync)
            {
                Synchronizable.State state;
                if (desire.getLeft() >= params.getParameter(DCOOP))
                {
                    // switch on left indicator
                    turnIndicatorStatus = TurnIndicatorIntent.LEFT;
                    state = Synchronizable.State.INDICATING;
                }
                else
                {
                    state = Synchronizable.State.SYNCHRONIZING;
                }
                aSync = lmrsData.getSynchronization().synchronize(perception, params, sli, carFollowingModel, desire.getLeft(),
                        LateralDirectionality.LEFT, lmrsData);
                a = applyAcceleration(a, aSync, lmrsData, state);
            }
            else if (!desire.leftIsLargerOrEqual() && desire.getRight() >= dSync)
            {
                Synchronizable.State state;
                if (desire.getRight() >= params.getParameter(DCOOP))
                {
                    // switch on right indicator
                    turnIndicatorStatus = TurnIndicatorIntent.RIGHT;
                    state = Synchronizable.State.INDICATING;
                }
                else
                {
                    state = Synchronizable.State.SYNCHRONIZING;
                }
                aSync = lmrsData.getSynchronization().synchronize(perception, params, sli, carFollowingModel, desire.getRight(),
                        LateralDirectionality.RIGHT, lmrsData);
                a = applyAcceleration(a, aSync, lmrsData, state);
            }
            else
            {
                lmrsData.setSynchronizationState(Synchronizable.State.NONE);
            }

            // cooperate
            aSync = lmrsData.getCooperation().cooperate(perception, params, sli, carFollowingModel, LateralDirectionality.LEFT,
                    desire);
            a = applyAcceleration(a, aSync, lmrsData, Synchronizable.State.COOPERATING);
            aSync = lmrsData.getCooperation().cooperate(perception, params, sli, carFollowingModel, LateralDirectionality.RIGHT,
                    desire);
            a = applyAcceleration(a, aSync, lmrsData, Synchronizable.State.COOPERATING);

            // relaxation
            exponentialHeadwayRelaxation(params);

        }
        lmrsData.finalizeStep();

        SimpleOperationalPlan simplePlan = new SimpleOperationalPlan(a, params.getParameter(DT), initiatedLaneChange);
        if (turnIndicatorStatus.isLeft())
        {
            simplePlan.setIndicatorIntentLeft();
        }
        else if (turnIndicatorStatus.isRight())
        {
            simplePlan.setIndicatorIntentRight();
        }
        return simplePlan;

    }

    /**
     * Minimizes the acceleration and sets the synchronization state if applicable.
     * @param a Acceleration; previous acceleration
     * @param aNew Acceleration; new acceleration
     * @param lmrsData LmrsData; lmrs data
     * @param state Synchronizable.State; synchronization state
     * @return Acceleration; minimized acceleration
     */
    private static Acceleration applyAcceleration(final Acceleration a, final Acceleration aNew, final LmrsData lmrsData,
            final Synchronizable.State state)
    {
        if (a.si < aNew.si)
        {
            return a;
        }
        lmrsData.setSynchronizationState(state);
        return aNew;
    }

    /**
     * Sets the headway as a response to a new leader.
     * @param params Parameters; parameters
     * @param leader HeadwayGTU; leader
     * @throws ParameterException if DLC is not present
     */
    private static void initHeadwayRelaxation(final Parameters params, final HeadwayGTU leader) throws ParameterException
    {
        Double dlc = leader.getParameters().getParameterOrNull(DLC);
        if (dlc != null)
        {
            setDesiredHeadway(params, dlc);
        }
        // else could not be perceived
    }

    /**
     * Updates the desired headway following an exponential shape approximated with fixed time step <tt>DT</tt>.
     * @param params Parameters; parameters
     * @throws ParameterException in case of a parameter exception
     */
    private static void exponentialHeadwayRelaxation(final Parameters params) throws ParameterException
    {
        double ratio = params.getParameter(DT).si / params.getParameter(TAU).si;
        params.setParameter(T,
                Duration.interpolate(params.getParameter(T), params.getParameter(TMAX), ratio <= 1.0 ? ratio : 1.0));
    }

    /**
     * Determines lane change desire for the given RSU. Mandatory desire is deduced as the maximum of a set of mandatory
     * incentives, while voluntary desires are added. Depending on the level of mandatory lane change desire, voluntary desire
     * may be included partially. If both are positive or negative, voluntary desire is fully included. Otherwise, voluntary
     * desire is less considered within the range dSync &lt; |mandatory| &lt; dCoop. The absolute value is used as large
     * negative mandatory desire may also dominate voluntary desire.
     * @param parameters Parameters; parameters
     * @param perception LanePerception; perception
     * @param carFollowingModel CarFollowingModel; car-following model
     * @param mandatoryIncentives LinkedHashSet&lt;MandatoryIncentive&gt;; mandatory incentives
     * @param voluntaryIncentives LinkedHashSet&lt;VoluntaryIncentive&gt;; voluntary incentives
     * @param desireMap Map&lt;Class&lt;? extends Incentive&gt;,Desire&gt;; map where calculated desires are stored in
     * @return lane change desire for gtu
     * @throws ParameterException if a parameter is not defined
     * @throws GTUException if there is no mandatory incentive, the model requires at least one
     * @throws OperationalPlanException perception exception
     */
    public static Desire getLaneChangeDesire(final Parameters parameters, final LanePerception perception,
            final CarFollowingModel carFollowingModel, final LinkedHashSet<MandatoryIncentive> mandatoryIncentives,
            final LinkedHashSet<VoluntaryIncentive> voluntaryIncentives,
            final Map<Class<? extends Incentive>, Desire> desireMap)
            throws ParameterException, GTUException, OperationalPlanException
    {

        double dSync = parameters.getParameter(DSYNC);
        double dCoop = parameters.getParameter(DCOOP);

        // Mandatory desire
        double dLeftMandatory = 0.0;
        double dRightMandatory = 0.0;
        Desire mandatoryDesire = new Desire(dLeftMandatory, dRightMandatory);
        for (MandatoryIncentive incentive : mandatoryIncentives)
        {
            Desire d = incentive.determineDesire(parameters, perception, carFollowingModel, mandatoryDesire);
            desireMap.put(incentive.getClass(), d);
            dLeftMandatory = Math.abs(d.getLeft()) > Math.abs(dLeftMandatory) ? d.getLeft() : dLeftMandatory;
            dRightMandatory = Math.abs(d.getRight()) > Math.abs(dRightMandatory) ? d.getRight() : dRightMandatory;
            mandatoryDesire = new Desire(dLeftMandatory, dRightMandatory);
        }

        // Voluntary desire
        double dLeftVoluntary = 0;
        double dRightVoluntary = 0;
        Desire voluntaryDesire = new Desire(dLeftVoluntary, dRightVoluntary);
        for (VoluntaryIncentive incentive : voluntaryIncentives)
        {
            Desire d = incentive.determineDesire(parameters, perception, carFollowingModel, mandatoryDesire, voluntaryDesire);
            desireMap.put(incentive.getClass(), d);
            dLeftVoluntary += d.getLeft();
            dRightVoluntary += d.getRight();
            voluntaryDesire = new Desire(dLeftVoluntary, dRightVoluntary);
        }

        // Total desire
        double thetaLeft = 0;
        double dLeftMandatoryAbs = Math.abs(dLeftMandatory);
        double dRightMandatoryAbs = Math.abs(dRightMandatory);
        if (dLeftMandatoryAbs <= dSync || dLeftMandatory * dLeftVoluntary >= 0)
        {
            // low mandatory desire, or same sign
            thetaLeft = 1;
        }
        else if (dSync < dLeftMandatoryAbs && dLeftMandatoryAbs < dCoop && dLeftMandatory * dLeftVoluntary < 0)
        {
            // linear from 1 at dSync to 0 at dCoop
            thetaLeft = (dCoop - dLeftMandatoryAbs) / (dCoop - dSync);
        }
        double thetaRight = 0;
        if (dRightMandatoryAbs <= dSync || dRightMandatory * dRightVoluntary >= 0)
        {
            // low mandatory desire, or same sign
            thetaRight = 1;
        }
        else if (dSync < dRightMandatoryAbs && dRightMandatoryAbs < dCoop && dRightMandatory * dRightVoluntary < 0)
        {
            // linear from 1 at dSync to 0 at dCoop
            thetaRight = (dCoop - dRightMandatoryAbs) / (dCoop - dSync);
        }
        return new Desire(dLeftMandatory + thetaLeft * dLeftVoluntary, dRightMandatory + thetaRight * dRightVoluntary);

    }

    /**
     * Determine whether a lane change is acceptable (gap, lane markings, etc.).
     * @param perception LanePerception; perception
     * @param params Parameters; parameters
     * @param sli SpeedLimitInfo; speed limit info
     * @param cfm CarFollowingModel; car-following model
     * @param desire double; level of lane change desire
     * @param ownSpeed Speed; own speed
     * @param ownAcceleration Acceleration; current car-following acceleration
     * @param lat LateralDirectionality; lateral direction for synchronization
     * @param gapAcceptance GapAcceptance; gap-acceptance model
     * @return whether a gap is acceptable
     * @throws ParameterException if a parameter is not defined
     * @throws OperationalPlanException perception exception
     */
    static boolean acceptLaneChange(final LanePerception perception, final Parameters params, final SpeedLimitInfo sli,
            final CarFollowingModel cfm, final double desire, final Speed ownSpeed, final Acceleration ownAcceleration,
            final LateralDirectionality lat, final GapAcceptance gapAcceptance)
            throws ParameterException, OperationalPlanException
    {

        // beyond start distance
        boolean beyond = Try.assign(() -> perception.getGtu().laneChangeAllowed(), "Cannot obtain GTU.");
        if (!beyond)
        {
            return false;
        }

        // legal?
        InfrastructurePerception infra = perception.getPerceptionCategory(InfrastructurePerception.class);
        if (infra.getLegalLaneChangePossibility(RelativeLane.CURRENT, lat).si <= 0.0)
        {
            return false;
        }

        // other causes for deceleration
        IntersectionPerception intersection = perception.getPerceptionCategoryOrNull(IntersectionPerception.class);
        if (intersection != null)
        {
            // conflicts alongside?
            if ((lat.isLeft() && intersection.isAlongsideConflictLeft())
                    || (lat.isRight() && intersection.isAlongsideConflictRight()))
            {
                return false;
            }
            if (quickIntersectionScan(params, sli, cfm, ownSpeed, lat, intersection).lt(params.getParameter(BCRIT).neg()))
            {
                return false;
            }
        }

        // safe regarding neighbors?
        return gapAcceptance.acceptGap(perception, params, sli, cfm, desire, ownSpeed, ownAcceleration, lat);
    }

    /**
     * Returns a quickly determined acceleration to consider on an adjacent lane, following from conflicts and traffic lights.
     * @param params Parameters; parameters
     * @param sli SpeedLimitInfo; speed limit info
     * @param cfm CarFollowingModel; car-following model
     * @param ownSpeed Speed; own speed
     * @param lat LateralDirectionality; lateral direction for synchronization
     * @param intersection IntersectionPerception; intersection perception
     * @return a quickly determined acceleration to consider on an adjacent lane, following from conflicts and traffic lights
     * @throws ParameterException if a parameter is not defined
     */
    private static Acceleration quickIntersectionScan(final Parameters params, final SpeedLimitInfo sli,
            final CarFollowingModel cfm, final Speed ownSpeed, final LateralDirectionality lat,
            final IntersectionPerception intersection) throws ParameterException
    {
        Acceleration a = Acceleration.POSITIVE_INFINITY;
        if (intersection != null)
        {
            RelativeLane lane = lat.isRight() ? RelativeLane.RIGHT : RelativeLane.LEFT;
            Iterable<HeadwayConflict> iterable = intersection.getConflicts(lane);
            if (iterable != null)
            {
                Iterator<HeadwayConflict> conflicts = iterable.iterator();
                if (conflicts.hasNext())
                {
                    a = Acceleration.min(a, CarFollowingUtil.followSingleLeader(cfm, params, ownSpeed, sli,
                            conflicts.next().getDistance(), Speed.ZERO));
                }
                Iterator<HeadwayTrafficLight> trafficLights = intersection.getTrafficLights(lane).iterator();
                if (trafficLights.hasNext())
                {
                    HeadwayTrafficLight trafficLight = trafficLights.next();
                    if (trafficLight.getTrafficLightColor().isRedOrYellow())
                    {
                        a = Acceleration.min(a, CarFollowingUtil.followSingleLeader(cfm, params, ownSpeed, sli,
                                trafficLight.getDistance(), Speed.ZERO));
                    }
                }
            }
        }
        return a;
    }

    /**
     * Sets value for T depending on level of lane change desire.
     * @param params Parameters; parameters
     * @param desire double; lane change desire
     * @throws ParameterException if T, TMIN or TMAX is not in the parameters
     */
    static void setDesiredHeadway(final Parameters params, final double desire) throws ParameterException
    {
        double limitedDesire = desire < 0 ? 0 : desire > 1 ? 1 : desire;
        double tDes = limitedDesire * params.getParameter(TMIN).si + (1 - limitedDesire) * params.getParameter(TMAX).si;
        double t = params.getParameter(T).si;
        params.setParameterResettable(T, Duration.createSI(tDes < t ? tDes : t));
    }

    /**
     * Resets value for T depending on level of lane change desire.
     * @param params Parameters; parameters
     * @throws ParameterException if T is not in the parameters
     */
    static void resetDesiredHeadway(final Parameters params) throws ParameterException
    {
        params.resetParameter(T);
    }

    /**
     * Determine acceleration from car-following with desire-adjusted headway.
     * @param distance Length; distance from follower to leader
     * @param followerSpeed Speed; speed of follower
     * @param leaderSpeed Speed; speed of leader
     * @param desire double; level of lane change desire
     * @param params Parameters; parameters
     * @param sli SpeedLimitInfo; speed limit info
     * @param cfm CarFollowingModel; car-following model
     * @return acceleration from car-following
     * @throws ParameterException if a parameter is not defined
     */
    public static Acceleration singleAcceleration(final Length distance, final Speed followerSpeed, final Speed leaderSpeed,
            final double desire, final Parameters params, final SpeedLimitInfo sli, final CarFollowingModel cfm)
            throws ParameterException
    {
        // set T
        setDesiredHeadway(params, desire);
        // calculate acceleration
        Acceleration a = CarFollowingUtil.followSingleLeader(cfm, params, followerSpeed, sli, distance, leaderSpeed);
        // reset T
        resetDesiredHeadway(params);
        return a;
    }

}
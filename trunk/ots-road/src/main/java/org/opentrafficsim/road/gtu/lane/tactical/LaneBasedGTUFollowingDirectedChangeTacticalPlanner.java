package org.opentrafficsim.road.gtu.lane.tactical;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.djunits.unit.AccelerationUnit;
import org.djunits.unit.TimeUnit;
import org.djunits.value.vdouble.scalar.Acceleration;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Time;
import org.opentrafficsim.core.gtu.GTUDirectionality;
import org.opentrafficsim.core.gtu.GTUException;
import org.opentrafficsim.core.gtu.TurnIndicatorStatus;
import org.opentrafficsim.core.gtu.behavioralcharacteristics.BehavioralCharacteristics;
import org.opentrafficsim.core.gtu.behavioralcharacteristics.ParameterException;
import org.opentrafficsim.core.gtu.behavioralcharacteristics.ParameterTypes;
import org.opentrafficsim.core.gtu.plan.operational.OperationalPlan;
import org.opentrafficsim.core.gtu.plan.operational.OperationalPlan.Segment;
import org.opentrafficsim.core.gtu.plan.operational.OperationalPlanException;
import org.opentrafficsim.core.network.LateralDirectionality;
import org.opentrafficsim.core.network.NetworkException;
import org.opentrafficsim.road.gtu.lane.AbstractLaneBasedGTU;
import org.opentrafficsim.road.gtu.lane.LaneBasedGTU;
import org.opentrafficsim.road.gtu.lane.perception.LanePerception;
import org.opentrafficsim.road.gtu.lane.perception.categories.DefaultSimplePerception;
import org.opentrafficsim.road.gtu.lane.perception.headway.Headway;
import org.opentrafficsim.road.gtu.lane.tactical.directedlanechange.DirectedAltruistic;
import org.opentrafficsim.road.gtu.lane.tactical.directedlanechange.DirectedEgoistic;
import org.opentrafficsim.road.gtu.lane.tactical.directedlanechange.DirectedLaneChangeModel;
import org.opentrafficsim.road.gtu.lane.tactical.directedlanechange.DirectedLaneMovementStep;
import org.opentrafficsim.road.gtu.lane.tactical.following.AccelerationStep;
import org.opentrafficsim.road.gtu.lane.tactical.following.GTUFollowingModelOld;
import org.opentrafficsim.road.network.lane.Lane;

import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * Lane-based tactical planner that implements car following behavior and rule-based lane change. This tactical planner
 * retrieves the car following model from the strategical planner and will generate an operational plan for the GTU.
 * <p>
 * A lane change occurs when:
 * <ol>
 * <li>The route indicates that the current lane does not lead to the destination; main choices are the time when the GTU
 * switches to the "right" lane, and what should happen when the split gets closer and the lane change has failed. Observations
 * indicate that vehicles if necessary stop in their current lane until they can go to the desired lane. A lane drop is
 * automatically part of this implementation, because the lane with a lane drop will not lead to the GTU's destination.</li>
 * <li>The desired speed of the vehicle is a particular delta-speed higher than its predecessor, the headway to the predecessor
 * in the current lane has exceeded a certain value, it is allowed to change to the target lane, the target lane lies on the
 * GTU's route, and the gap in the target lane is acceptable (including the evaluation of the perceived speed of a following GTU
 * in the target lane).</li>
 * <li>The current lane is not the optimum lane given the traffic rules (for example, to keep right), the headway to the
 * predecessor on the target lane is greater than a certain value, the speed of the predecessor on the target lane is greater
 * than or equal to our speed, the target lane is on the route, it is allowed to switch to the target lane, and the gap at the
 * target lane is acceptable (including the perceived speed of any vehicle in front or behind on the target lane).</li>
 * </ol>
 * <p>
 * This lane-based tactical planner makes decisions based on headway (GTU following model). It can ask the strategic planner for
 * assistance on the route to take when the network splits.
 * <p>
 * Copyright (c) 2013-2016 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * $LastChangedDate: 2015-07-24 02:58:59 +0200 (Fri, 24 Jul 2015) $, @version $Revision: 1147 $, by $Author: averbraeck $,
 * initial version Nov 25, 2015 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class LaneBasedGTUFollowingDirectedChangeTacticalPlanner extends AbstractLaneBasedTacticalPlanner
{
    /** */
    private static final long serialVersionUID = 20160129L;

    /** Earliest next lane change time (unless we HAVE to change lanes). */
    private Time earliestNextLaneChangeTime = Time.ZERO;

    /** Lane we changed to at instantaneous lane change. */
    private Lane laneAfterLaneChange = null;

    /** Position on the reference lane. */
    private Length posAfterLaneChange = null;

    /** When a failure in planning occurs, should we destroy the GTU to avoid halting of the model? */
    private boolean destroyGtuOnFailure = false;

    /**
     * Instantiated a tactical planner with just GTU following behavior and no lane changes.
     * @param carFollowingModel Car-following model.
     * @param gtu GTU
     */
    public LaneBasedGTUFollowingDirectedChangeTacticalPlanner(final GTUFollowingModelOld carFollowingModel,
            final LaneBasedGTU gtu)
    {
        super(carFollowingModel, gtu);
        getPerception().addPerceptionCategory(new DefaultSimplePerception(getPerception()));
    }

    /**
     * Returns the car-following model.
     * @return The car-following model.
     */
    public final GTUFollowingModelOld getCarFollowingModelOld()
    {
        return (GTUFollowingModelOld) super.getCarFollowingModel();
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:methodlength")
    public final OperationalPlan generateOperationalPlan(final Time startTime, final DirectedPoint locationAtStartTime)
            throws OperationalPlanException, NetworkException, GTUException, ParameterException
    {
        try
        {
            // ask Perception for the local situation
            LaneBasedGTU laneBasedGTU = getGtu();
            DefaultSimplePerception simplePerception = getPerception().getPerceptionCategory(DefaultSimplePerception.class);
            BehavioralCharacteristics behavioralCharacteristics = laneBasedGTU.getBehavioralCharacteristics();

            // start with the turn indicator off -- this can change during the method
            laneBasedGTU.setTurnIndicatorStatus(TurnIndicatorStatus.NONE);

            // if the GTU's maximum speed is zero (block), generate a stand still plan for one second
            if (laneBasedGTU.getMaximumSpeed().si < OperationalPlan.DRIFTING_SPEED_SI)
            {
                return new OperationalPlan(getGtu(), locationAtStartTime, startTime, new Duration(1.0, TimeUnit.SECOND));
            }

            // perceive the forward headway, accessible lanes and speed limit.
            simplePerception.updateForwardHeadwayGTU();
            simplePerception.updateForwardHeadwayObject();
            simplePerception.updateAccessibleAdjacentLanesLeft();
            simplePerception.updateAccessibleAdjacentLanesRight();
            simplePerception.updateSpeedLimit();

            // find out where we are going
            Length forwardHeadway = behavioralCharacteristics.getParameter(ParameterTypes.LOOKAHEAD);
            LanePathInfo lanePathInfo = buildLanePathInfo(laneBasedGTU, forwardHeadway);
            NextSplitInfo nextSplitInfo = determineNextSplit(laneBasedGTU, forwardHeadway);
            Set<Lane> correctLanes = laneBasedGTU.positions(laneBasedGTU.getReference()).keySet();
            correctLanes.retainAll(nextSplitInfo.getCorrectCurrentLanes());

            // Step 1: Do we want to change lanes because of the current lane not leading to our destination?
            if (lanePathInfo.getPath().getLength().lt(forwardHeadway) && correctLanes.isEmpty())
            {
                LateralDirectionality direction = determineLeftRight(laneBasedGTU, nextSplitInfo);
                if (direction != null)
                {
                    getGtu().setTurnIndicatorStatus(direction.isLeft() ? TurnIndicatorStatus.LEFT : TurnIndicatorStatus.RIGHT);
                    if (canChange(laneBasedGTU, getPerception(), lanePathInfo, direction))
                    {
                        DirectedPoint newLocation = changeLane(laneBasedGTU, direction);
                        lanePathInfo = buildLanePathInfo(laneBasedGTU, forwardHeadway, this.laneAfterLaneChange,
                                this.posAfterLaneChange, laneBasedGTU.getDirection(this.laneAfterLaneChange));
                        return currentLanePlan(laneBasedGTU, startTime, newLocation, lanePathInfo);
                    }
                }
            }

            // Condition, if we have just changed lane, let's not change immediately again.
            if (getGtu().getSimulator().getSimulatorTime().getTime().lt(this.earliestNextLaneChangeTime))
            {
                return currentLanePlan(laneBasedGTU, startTime, locationAtStartTime, lanePathInfo);
            }

            // Step 2. Do we want to change lanes to the left because of predecessor speed on the current lane?
            // And does the lane left of us bring us to our destination as well?
            Set<Lane> leftLanes = simplePerception.getAccessibleAdjacentLanesLeft().get(lanePathInfo.getReferenceLane());
            if (nextSplitInfo.isSplit())
            {
                leftLanes.retainAll(nextSplitInfo.getCorrectCurrentLanes());
            }
            if (!leftLanes.isEmpty() && laneBasedGTU.getSpeed().si > 4.0) // only if we are driving...
            {
                simplePerception.updateBackwardHeadway();
                simplePerception.updateParallelHeadwaysLeft();
                simplePerception.updateNeighboringHeadwaysLeft();
                if (simplePerception.getParallelHeadwaysLeft().isEmpty())
                {
                    Collection<Headway> sameLaneTraffic = new HashSet<>();
                    // TODO should it be getObjectType().isGtu() or !getObjectType().isDistanceOnly() ?
                    // XXX Object & GTU
                    if (simplePerception.getForwardHeadwayGTU() != null
                            && simplePerception.getForwardHeadwayGTU().getObjectType().isGtu())
                    {
                        sameLaneTraffic.add(simplePerception.getForwardHeadwayGTU());
                    }
                    if (simplePerception.getBackwardHeadway() != null
                            && simplePerception.getBackwardHeadway().getObjectType().isGtu())
                    {
                        sameLaneTraffic.add(simplePerception.getBackwardHeadway());
                    }
                    DirectedLaneChangeModel dlcm = new DirectedAltruistic(getPerception());
                    DirectedLaneMovementStep dlms = dlcm.computeLaneChangeAndAcceleration(laneBasedGTU,
                            LateralDirectionality.LEFT, sameLaneTraffic, simplePerception.getNeighboringHeadwaysLeft(),
                            behavioralCharacteristics.getParameter(ParameterTypes.LOOKAHEAD), simplePerception.getSpeedLimit(),
                            new Acceleration(1.0, AccelerationUnit.SI), new Acceleration(0.5, AccelerationUnit.SI),
                            new Duration(0.5, TimeUnit.SECOND));
                    if (dlms.getLaneChange() != null)
                    {
                        getGtu().setTurnIndicatorStatus(TurnIndicatorStatus.LEFT);
                        if (canChange(laneBasedGTU, getPerception(), lanePathInfo, LateralDirectionality.LEFT))
                        {
                            DirectedPoint newLocation = changeLane(laneBasedGTU, LateralDirectionality.LEFT);
                            lanePathInfo = buildLanePathInfo(laneBasedGTU, forwardHeadway, this.laneAfterLaneChange,
                                    this.posAfterLaneChange, laneBasedGTU.getDirection(this.laneAfterLaneChange));
                            return currentLanePlan(laneBasedGTU, startTime, newLocation, lanePathInfo);
                        }
                    }
                }
            }

            // Step 3. Do we want to change lanes to the right because of TODO traffic rules?
            Set<Lane> rightLanes = simplePerception.getAccessibleAdjacentLanesRight().get(lanePathInfo.getReferenceLane());
            if (nextSplitInfo.isSplit())
            {
                rightLanes.retainAll(nextSplitInfo.getCorrectCurrentLanes());
            }
            if (!rightLanes.isEmpty() && laneBasedGTU.getSpeed().si > 4.0) // only if we are driving...
            {
                simplePerception.updateBackwardHeadway();
                simplePerception.updateParallelHeadwaysRight();
                simplePerception.updateNeighboringHeadwaysRight();
                if (simplePerception.getParallelHeadwaysRight().isEmpty())
                {
                    Collection<Headway> sameLaneTraffic = new HashSet<>();
                    // TODO should it be getObjectType().isGtu() or !getObjectType().isDistanceOnly() ?
                    // XXX GTU & Object
                    if (simplePerception.getForwardHeadwayGTU() != null
                            && simplePerception.getForwardHeadwayGTU().getObjectType().isGtu())
                    {
                        sameLaneTraffic.add(simplePerception.getForwardHeadwayGTU());
                    }
                    if (simplePerception.getBackwardHeadway() != null
                            && simplePerception.getBackwardHeadway().getObjectType().isGtu())
                    {
                        sameLaneTraffic.add(simplePerception.getBackwardHeadway());
                    }
                    DirectedLaneChangeModel dlcm = new DirectedAltruistic(getPerception());
                    DirectedLaneMovementStep dlms = dlcm.computeLaneChangeAndAcceleration(laneBasedGTU,
                            LateralDirectionality.RIGHT, sameLaneTraffic, simplePerception.getNeighboringHeadwaysRight(),
                            behavioralCharacteristics.getParameter(ParameterTypes.LOOKAHEAD), simplePerception.getSpeedLimit(),
                            new Acceleration(1.0, AccelerationUnit.SI), new Acceleration(0.5, AccelerationUnit.SI),
                            new Duration(0.5, TimeUnit.SECOND));
                    if (dlms.getLaneChange() != null)
                    {
                        getGtu().setTurnIndicatorStatus(TurnIndicatorStatus.RIGHT);
                        if (canChange(laneBasedGTU, getPerception(), lanePathInfo, LateralDirectionality.RIGHT))
                        {
                            DirectedPoint newLocation = changeLane(laneBasedGTU, LateralDirectionality.RIGHT);
                            lanePathInfo = buildLanePathInfo(laneBasedGTU, forwardHeadway, this.laneAfterLaneChange,
                                    this.posAfterLaneChange, laneBasedGTU.getDirection(this.laneAfterLaneChange));
                            return currentLanePlan(laneBasedGTU, startTime, newLocation, lanePathInfo);
                        }
                    }
                }
            }

            return currentLanePlan(laneBasedGTU, startTime, locationAtStartTime, lanePathInfo);
        }
        catch (GTUException | NetworkException | OperationalPlanException exception)
        {
            if (isDestroyGtuOnFailure())
            {
                System.err.println("LaneBasedGTUFollowingChange0TacticalPlanner.generateOperationalPlan() failed for "
                        + getGtu() + " because of " + exception.getMessage() + " -- GTU destroyed");
                getGtu().destroy();
                return new OperationalPlan(getGtu(), locationAtStartTime, startTime, new Duration(1.0, TimeUnit.SECOND));
            }
            throw exception;
        }
    }

    /**
     * Make a plan for the current lane.
     * @param laneBasedGTU the gtu to generate the plan for
     * @param startTime the time from which the new operational plan has to be operational
     * @param locationAtStartTime the location of the GTU at the start time of the new plan
     * @param lanePathInfo the lane path for the current lane.
     * @return An operation plan for staying in the current lane.
     * @throws OperationalPlanException when there is a problem planning a path in the network
     * @throws GTUException when there is a problem with the state of the GTU when planning a path
     * @throws ParameterException in case LOOKAHEAD parameter cannot be found
     * @throws NetworkException in case the headways to GTUs or objects cannot be calculated
     */
    private OperationalPlan currentLanePlan(final LaneBasedGTU laneBasedGTU, final Time startTime,
            final DirectedPoint locationAtStartTime, final LanePathInfo lanePathInfo)
            throws OperationalPlanException, GTUException, ParameterException, NetworkException
    {
        DefaultSimplePerception simplePerception = getPerception().getPerceptionCategory(DefaultSimplePerception.class);

        // No lane change. Continue on current lane.
        AccelerationStep accelerationStep = mostLimitingAccelerationStep(lanePathInfo, simplePerception.getForwardHeadwayGTU(),
                simplePerception.getForwardHeadwayObject());

        // see if we have to continue standing still. In that case, generate a stand still plan
        if (accelerationStep.getAcceleration().si < 1E-6 && laneBasedGTU.getSpeed().si < OperationalPlan.DRIFTING_SPEED_SI)
        {
            return new OperationalPlan(laneBasedGTU, locationAtStartTime, startTime, accelerationStep.getDuration());
        }

        // build a list of lanes forward, with a maximum headway.
        List<Segment> operationalPlanSegmentList = new ArrayList<>();
        if (accelerationStep.getAcceleration().si == 0.0)
        {
            Segment segment = new OperationalPlan.SpeedSegment(accelerationStep.getDuration());
            operationalPlanSegmentList.add(segment);
        }
        else
        {
            Segment segment =
                    new OperationalPlan.AccelerationSegment(accelerationStep.getDuration(), accelerationStep.getAcceleration());
            operationalPlanSegmentList.add(segment);
        }
        OperationalPlan op = new OperationalPlan(laneBasedGTU, lanePathInfo.getPath(), startTime, laneBasedGTU.getSpeed(),
                operationalPlanSegmentList);
        return op;
    }

    /**
     * We are not on a lane that leads to our destination. Determine whether the lateral direction to go is left or right.
     * @param laneBasedGTU the gtu
     * @param nextSplitInfo the information about the next split
     * @return the lateral direction to go, or null if this cannot be determined
     */
    private LateralDirectionality determineLeftRight(final LaneBasedGTU laneBasedGTU, final NextSplitInfo nextSplitInfo)
    {
        // are the lanes in nextSplitInfo.getCorrectCurrentLanes() left or right of the current lane(s) of the GTU?
        try
        {
            Set<Lane> lanes = laneBasedGTU.positions(laneBasedGTU.getReference()).keySet();
            for (Lane correctLane : nextSplitInfo.getCorrectCurrentLanes())
            {
                for (Lane currentLane : lanes)
                {
                    if (correctLane.getParentLink().equals(currentLane.getParentLink()))
                    {
                        double deltaOffset =
                                correctLane.getDesignLineOffsetAtBegin().si - currentLane.getDesignLineOffsetAtBegin().si;
                        if (laneBasedGTU.getDirection(currentLane).equals(GTUDirectionality.DIR_PLUS))
                        {
                            return deltaOffset > 0 ? LateralDirectionality.LEFT : LateralDirectionality.RIGHT;
                        }
                        else
                        {
                            return deltaOffset < 0 ? LateralDirectionality.LEFT : LateralDirectionality.RIGHT;
                        }
                    }
                }
            }
        }
        catch (GTUException exception)
        {
            System.err.println(
                    "Exception in LaneBasedGTUFollowingChange0TacticalPlanner.determineLeftRight: " + exception.getMessage());
        }
        return null;
    }

    /**
     * See if a lane change in the given direction if possible.
     * @param gtu the GTU that has to make the lane change
     * @param perception the perception, where forward headway, accessible lanes and speed limit have been assessed
     * @param lanePathInfo the information for the path on the current lane
     * @param direction the lateral direction, either LEFT or RIGHT
     * @return whether a lane change is possible.
     * @throws NetworkException when there is a network inconsistency in updating the perception
     * @throws GTUException when there is an issue retrieving GTU information for the perception update
     * @throws ParameterException when there is a parameter problem.
     * @throws OperationalPlanException in case a perception category is not present
     */
    private boolean canChange(final LaneBasedGTU gtu, final LanePerception perception, final LanePathInfo lanePathInfo,
            final LateralDirectionality direction)
            throws GTUException, NetworkException, ParameterException, OperationalPlanException
    {
        
        if (!((AbstractLaneBasedGTU) gtu).isSafeToChange())
        {
            return false;
        }
        
        Collection<Headway> otherLaneTraffic;
        DefaultSimplePerception simplePerception = getPerception().getPerceptionCategory(DefaultSimplePerception.class);
        simplePerception.updateForwardHeadwayGTU();
        simplePerception.updateForwardHeadwayObject();
        simplePerception.updateBackwardHeadway();
        if (direction.isLeft())
        {
            simplePerception.updateParallelHeadwaysLeft();
            simplePerception.updateNeighboringHeadwaysLeft();
            otherLaneTraffic = simplePerception.getNeighboringHeadwaysLeft();
        }
        else if (direction.isRight())
        {
            simplePerception.updateParallelHeadwaysRight();
            simplePerception.updateNeighboringHeadwaysRight();
            otherLaneTraffic = simplePerception.getNeighboringHeadwaysRight();
        }
        else
        {
            throw new GTUException("Lateral direction is neither LEFT nor RIGHT during a lane change");
        }
        if (!simplePerception.getParallelHeadways(direction).isEmpty())
        {
            return false;
        }

        Collection<Headway> sameLaneTraffic = new HashSet<>();
        // TODO should it be getObjectType().isGtu() or !getObjectType().isDistanceOnly() ?
        // XXX Object & GTU
        if (simplePerception.getForwardHeadwayGTU() != null && perception.getPerceptionCategory(DefaultSimplePerception.class)
                .getForwardHeadwayGTU().getObjectType().isGtu())
        {
            sameLaneTraffic.add(simplePerception.getForwardHeadwayGTU());
        }
        if (simplePerception.getBackwardHeadway() != null && simplePerception.getBackwardHeadway().getObjectType().isGtu())
        {
            sameLaneTraffic.add(simplePerception.getBackwardHeadway());
        }

        // TODO make type of plan (Egoistic, Altruistic) parameter of the class
        DirectedLaneChangeModel dlcm = new DirectedEgoistic(getPerception());
        // TODO make the elasticities 2.0 and 0.1 parameters of the class
        DirectedLaneMovementStep dlms = dlcm.computeLaneChangeAndAcceleration(gtu, direction, sameLaneTraffic, otherLaneTraffic,
                gtu.getBehavioralCharacteristics().getParameter(ParameterTypes.LOOKAHEAD), simplePerception.getSpeedLimit(),
                new Acceleration(2.0, AccelerationUnit.SI), new Acceleration(0.1, AccelerationUnit.SI),
                new Duration(0.5, TimeUnit.SECOND));
        if (dlms.getLaneChange() == null)
        {
            return false;
        }

        return true;
    }

    /**
     * Change lanes instantaneously.
     * @param gtu the gtu
     * @param direction the direction
     * @return the new location of the GTU after the lane change
     * @throws GTUException in case the enter lane fails
     */
    private DirectedPoint changeLane(final LaneBasedGTU gtu, final LateralDirectionality direction) throws GTUException
    {
        gtu.changeLaneInstantaneously(direction);

        // stay at least 15 seconds in the current lane (unless we HAVE to change lanes)
        this.earliestNextLaneChangeTime =
                gtu.getSimulator().getSimulatorTime().getTime().plus(new Duration(15, TimeUnit.SECOND));

        // make sure out turn indicator is on!
        gtu.setTurnIndicatorStatus(direction.isLeft() ? TurnIndicatorStatus.LEFT : TurnIndicatorStatus.RIGHT);

        this.laneAfterLaneChange = gtu.getReferencePosition().getLane();
        this.posAfterLaneChange = gtu.getReferencePosition().getPosition();
        return gtu.getLocation();
    }

    /**
     * Calculate which Headway in front of us is leading to the most limiting acceleration step (i.e. to the lowest or most
     * negative acceleration). There could, e.g. be a GTU in front of us, a speed sign in front of us, and a traffic light in
     * front of the GTU and speed sign. This method will return the acceleration based on the headway that limits us most.<br>
     * The method can e.g., be called with:
     * <code>mostLimitingHeadway(simplePerception.getForwardHeadwayGTU(), simplePerception.getForwardHeadwayObject());</code>
     * @param lanePathInfo the lane path info that was calculated for this GTU.
     * @param headways zero or more headways specifying possible limitations on our acceleration.
     * @return the acceleration based on the most limiting headway.
     * @throws OperationalPlanException in case the PerceptionCategory cannot be found
     * @throws ParameterException in case LOOKAHEAD parameter cannot be found
     * @throws GTUException in case the AccelerationStep cannot be calculated
     * @throws NetworkException in case the headways to GTUs or objects cannot be calculated
     */
    private AccelerationStep mostLimitingAccelerationStep(final LanePathInfo lanePathInfo, final Headway... headways)
            throws OperationalPlanException, ParameterException, GTUException, NetworkException
    {
        DefaultSimplePerception simplePerception = getPerception().getPerceptionCategory(DefaultSimplePerception.class);
        simplePerception.updateForwardHeadwayGTU();
        simplePerception.updateForwardHeadwayObject();
        Length maxDistance = Length.min(getGtu().getBehavioralCharacteristics().getParameter(ParameterTypes.LOOKAHEAD),
                lanePathInfo.getPath().getLength().minus(getGtu().getLength().multiplyBy(2.0)));
        AccelerationStep mostLimitingAccelerationStep = getCarFollowingModelOld().computeAccelerationStepWithNoLeader(getGtu(),
                maxDistance, simplePerception.getSpeedLimit());
        for (Headway headway : headways)
        {
            if (headway != null && headway.getDistance().lt(maxDistance))
            {
                AccelerationStep accelerationStep = getCarFollowingModelOld().computeAccelerationStep(getGtu(),
                        headway.getSpeed(), headway.getDistance(), maxDistance, simplePerception.getSpeedLimit());
                if (accelerationStep.getAcceleration().lt(mostLimitingAccelerationStep.getAcceleration()))
                {
                    mostLimitingAccelerationStep = accelerationStep;
                }
            }
        }
        return mostLimitingAccelerationStep;
    }

    /**
     * @return destroyGtuOnFailure, indicating when a failure in planning occurs, whether we should destroy the GTU to avoid
     *         halting of the model
     */
    public final boolean isDestroyGtuOnFailure()
    {
        return this.destroyGtuOnFailure;
    }

    /**
     * When a failure in planning occurs, should we destroy the GTU to avoid halting of the model?
     * @param destroyGtuOnFailure set destroyGtuOnFailure to true or false
     */
    public final void setDestroyGtuOnFailure(final boolean destroyGtuOnFailure)
    {
        this.destroyGtuOnFailure = destroyGtuOnFailure;
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        return "LaneBasedGTUFollowingChange0TacticalPlanner [earliestNexLaneChangeTime=" + this.earliestNextLaneChangeTime
                + ", referenceLane=" + this.laneAfterLaneChange + ", referencePos=" + this.posAfterLaneChange
                + ", destroyGtuOnFailure=" + this.destroyGtuOnFailure + "]";
    }

}

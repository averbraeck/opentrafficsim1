package org.opentrafficsim.road.network.sampling;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.djunits.unit.TimeUnit;
import org.djunits.value.vdouble.scalar.Acceleration;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Speed;
import org.opentrafficsim.core.Throw;
import org.opentrafficsim.core.dsol.OTSDEVSSimulatorInterface;
import org.opentrafficsim.core.gtu.GTUDirectionality;
import org.opentrafficsim.core.gtu.GTUException;
import org.opentrafficsim.core.gtu.RelativePosition;
import org.opentrafficsim.road.gtu.lane.LaneBasedGTU;
import org.opentrafficsim.road.network.lane.Lane;
import org.opentrafficsim.road.network.lane.LaneDirection;
import org.opentrafficsim.road.network.sampling.meta.MetaData;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;

/**
 * Sampling is the highest level organizer for sampling.
 * <p>
 * Copyright (c) 2013-2016 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Sep 22, 2016 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class Sampling implements EventListenerInterface
{

    /** Map with all sampling data. */
    private final Map<LaneDirection, TrajectoryGroup> trajectories = new HashMap<>();

    /** End times of active samplings. */
    private final Map<LaneDirection, Duration> endTimes = new HashMap<>();

    /** Simulator. */
    private final OTSDEVSSimulatorInterface simulator;

    /** Registration of current trajectories of each GTU per lane. */
    private final Map<String, Map<Lane, Trajectory>> trajectoryPerGtu = new HashMap<>();

    /**
     * Constructor.
     * @param simulator simulator
     */
    public Sampling(final OTSDEVSSimulatorInterface simulator)
    {
        this.simulator = simulator;
    }

    /**
     * @param spaceTimeRegion space-time region
     * @throws IllegalStateException if data is not available from the requested start time
     */
    public final void registerSpaceTimeRegion(final SpaceTimeRegion spaceTimeRegion)
    {
        Duration firstPossibleDataTime;
        if (this.trajectories.containsKey(spaceTimeRegion.getLaneDirection()))
        {
            firstPossibleDataTime = this.trajectories.get(spaceTimeRegion.getLaneDirection()).getStartTime();
        }
        else
        {
            firstPossibleDataTime = new Duration(this.simulator.getSimulatorTime().getTime().si, TimeUnit.SI);
        }
        Throw.when(spaceTimeRegion.getStartTime().lt(firstPossibleDataTime), IllegalStateException.class,
                "Space time region with start time %s is defined while data is available from %s onwards.",
                spaceTimeRegion.getStartTime(), firstPossibleDataTime);
        if (this.trajectories.containsKey(spaceTimeRegion.getLaneDirection()))
        {
            this.endTimes.put(spaceTimeRegion.getLaneDirection(),
                    Duration.max(this.endTimes.get(spaceTimeRegion.getLaneDirection()), spaceTimeRegion.getEndTime()));
        }
        else
        {
            this.endTimes.put(spaceTimeRegion.getLaneDirection(), spaceTimeRegion.getEndTime());
            try
            {
                this.simulator.scheduleEventNow(this, this, "startRecording",
                        new Object[] { spaceTimeRegion.getStartTime(), spaceTimeRegion.getLaneDirection() });
            }
            catch (SimRuntimeException exception)
            {
                throw new RuntimeException("Cannot start recording.", exception);
            }
        }
        try
        {
            this.simulator.scheduleEventAbs(
                    this.simulator.getSimulatorTime().plus(this.endTimes.get(spaceTimeRegion.getLaneDirection())), this, this,
                    "stopRecording", new Object[] { spaceTimeRegion.getLaneDirection() });
        }
        catch (SimRuntimeException exception)
        {
            throw new RuntimeException("Cannot stop recording.", exception);
        }
    }

    /**
     * Start recording at the given time (which should be the current time) on the given lane direction.
     * @param time current time
     * @param laneDirection lane direction
     */
    public final void startRecording(final Duration time, final LaneDirection laneDirection)
    {
        if (this.trajectories.containsKey(laneDirection))
        {
            return;
        }
        this.trajectories.put(laneDirection, new TrajectoryGroup(time, laneDirection));
        laneDirection.getLane().addListener(this, Lane.GTU_ADD_EVENT, true);
        laneDirection.getLane().addListener(this, Lane.GTU_REMOVE_EVENT, true);
    }

    /**
     * Stop recording at given lane direction.
     * @param time to stop
     * @param laneDirection lane direction
     */
    public final void stopRecording(final Duration time, final LaneDirection laneDirection)
    {
        if (!this.trajectories.containsKey(laneDirection) || this.endTimes.get(laneDirection).gt(time))
        {
            return;
        }
        this.trajectories.remove(laneDirection);
        laneDirection.getLane().removeListener(this, Lane.GTU_ADD_EVENT);
        laneDirection.getLane().removeListener(this, Lane.GTU_REMOVE_EVENT);
    }

    /** {@inheritDoc} */
    @Override
    public final void notify(final EventInterface event) throws RemoteException
    {
        if (event.getType().equals(LaneBasedGTU.LANEBASED_MOVE_EVENT))
        {
            // Payload: [String gtuId, DirectedPoint position, Speed speed, Acceleration acceleration, TurnIndicatorStatus
            // turnIndicatorStatus, Length odometer, Lane referenceLane, Length positionOnReferenceLane]
            Object[] payload = (Object[]) event.getContent();
            String gtuId = (String) payload[0];
            if (this.trajectoryPerGtu.containsKey(gtuId) && this.trajectoryPerGtu.get(gtuId).containsKey(payload[6]))
            {
                this.trajectoryPerGtu.get(gtuId).get(payload[6]).add((Length) payload[7], (Speed) payload[2],
                        (Acceleration) payload[3], new Duration(this.simulator.getSimulatorTime().get().si, TimeUnit.SI));
            }
        }
        else if (event.getType().equals(Lane.GTU_ADD_EVENT))
        {
            // Payload: Object[] {String gtuId, LaneBasedGTU gtu, int count_after_addition}
            Lane lane = (Lane) event.getSource();
            // TODO GTUDirectionality from Lane.GTU_ADD_EVENT
            LaneDirection laneDirection = new LaneDirection(lane, GTUDirectionality.DIR_PLUS);
            if (!this.trajectories.containsKey(laneDirection))
            {
                // we are not sampling this LaneDirection
                return;
            }
            Object[] payload = (Object[]) event.getContent();
            String gtuId = (String) payload[0];
            LaneBasedGTU gtu = (LaneBasedGTU) payload[1];
            Length distance;
            try
            {
                // TODO Length from Lane.GTU_ADD_EVENT
                distance = gtu.position(lane, RelativePosition.REFERENCE_POSITION);
            }
            catch (GTUException exception)
            {
                throw new RuntimeException(exception);
            }
            Speed speed = gtu.getSpeed();
            Acceleration acceleration = gtu.getAcceleration();
            Duration time = new Duration(this.simulator.getSimulatorTime().getTime().si, TimeUnit.SI);
            boolean longitudinalEntry = false;
            // TODO MetaData specific for GTU, incorporate getMetaData(LaneBasedGTU gtu) in MetaDataType and subclasses
            // Keep list of all MetaDataTypes of registered queries, i.e. registerMetaDataTypes(), invoke all
            Trajectory trajectory = new Trajectory(gtu, longitudinalEntry, new MetaData());
            trajectory.add(distance, speed, acceleration, time);
            if (!this.trajectoryPerGtu.containsKey(gtuId))
            {
                Map<Lane, Trajectory> map = new HashMap<>();
                this.trajectoryPerGtu.put(gtuId, map);
            }
            this.trajectoryPerGtu.get(gtuId).put(lane, trajectory);
            this.trajectories.get(laneDirection).addTrajectory(trajectory);
            gtu.addListener(this, LaneBasedGTU.LANEBASED_MOVE_EVENT, true);
        }
        else if (event.getType().equals(Lane.GTU_REMOVE_EVENT))
        {
            // Payload: Object[] {String gtuId, LaneBasedGTU gtu, int count_after_removal}
            Object[] payload = (Object[]) event.getContent();
            String gtuId = (String) payload[0];
            LaneBasedGTU gtu = (LaneBasedGTU) payload[1];
            Lane lane = (Lane) event.getSource();
            if (this.trajectoryPerGtu.get(gtuId) != null)
            {
                this.trajectoryPerGtu.get(gtuId).remove(lane);
                if (this.trajectoryPerGtu.get(gtuId).isEmpty())
                {
                    this.trajectoryPerGtu.remove(gtuId);
                }
            }
            gtu.removeListener(this, LaneBasedGTU.LANEBASED_MOVE_EVENT);
        }

    }

    /**
     * Returns the trajectory group of given lane direction.
     * @param laneDirection lane direction
     * @return trajectory group of given lane direction, {@code null} if none
     */
    public final TrajectoryGroup getTrajectoryGroup(final LaneDirection laneDirection)
    {
        return this.trajectories.get(laneDirection);
    }

    /** {@inheritDoc} */
    @Override
    public final int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.endTimes == null) ? 0 : this.endTimes.hashCode());
        result = prime * result + ((this.trajectories == null) ? 0 : this.trajectories.hashCode());
        result = prime * result + ((this.trajectoryPerGtu == null) ? 0 : this.trajectoryPerGtu.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public final boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        Sampling other = (Sampling) obj;
        if (this.endTimes == null)
        {
            if (other.endTimes != null)
            {
                return false;
            }
        }
        else if (!this.endTimes.equals(other.endTimes))
        {
            return false;
        }
        if (this.trajectories == null)
        {
            if (other.trajectories != null)
            {
                return false;
            }
        }
        else if (!this.trajectories.equals(other.trajectories))
        {
            return false;
        }
        if (this.trajectoryPerGtu == null)
        {
            if (other.trajectoryPerGtu != null)
            {
                return false;
            }
        }
        else if (!this.trajectoryPerGtu.equals(other.trajectoryPerGtu))
        {
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        return "Sampling [simulator=" + this.simulator + "]";
    }

}
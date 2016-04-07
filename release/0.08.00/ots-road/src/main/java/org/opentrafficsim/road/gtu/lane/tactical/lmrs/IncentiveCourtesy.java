package org.opentrafficsim.road.gtu.lane.tactical.lmrs;

import org.opentrafficsim.road.gtu.lane.LaneBasedGTU;
import org.opentrafficsim.road.gtu.lane.perception.LanePerception;

/**
 * Determines lane change desire for courtesy lane changes, which are performed to supply space for other drivers. In
 * case drivers in adjacent lanes have desire to change to the current lane, the driver has desire to change to the 
 * other adjacent lane. The level of desire depends on lane change courtesy, as well as the distance of the leading
 * vehicle for which desire exists. This desire exists for only a single vehicle, i.e. the one giving maximum desire. A
 * negative desire may also result for leaders in the 2nd adjacent lane desiring to change to the 1st adjacent lane. By 
 * not changing to the 1st adjacent lane, room is reserved for the leader on the 2nd adjacent lane. 
 * @author Wouter Schakel
 */
public class IncentiveCourtesy implements VoluntaryIncentive {

	/** {@inheritDoc} */
	@Override
	public Desire determineDesire(final LaneBasedGTU gtu, final LanePerception perception, Desire mandatory) {
		return new Desire(0, 0);
	}

}

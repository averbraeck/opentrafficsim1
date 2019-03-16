package org.opentrafficsim.core.gtu.lane.changing;

import java.rmi.RemoteException;
import java.util.Collection;

import org.opentrafficsim.core.gtu.following.HeadwayGTU;
import org.opentrafficsim.core.gtu.lane.LaneBasedGTU;
import org.opentrafficsim.core.unit.AccelerationUnit;
import org.opentrafficsim.core.unit.SpeedUnit;
import org.opentrafficsim.core.value.vdouble.scalar.DoubleScalar;

/**
 * All lane change models must implement this interface.
 * <p>
 * Copyright (c) 2013-2015 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights
 * reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author: pknoppers
 *          $, initial version 3 nov. 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://Hansvanlint.weblog.tudelft.nl">Hans van Lint</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.citg.tudelft.nl">Guus Tamminga</a>
 * @author <a href="http://www.citg.tudelft.nl">Yufei Yuan</a>
 */
public interface LaneChangeModel
{
    /**
     * Compute the acceleration and lane change.
     * @param gtu GTU; the GTU for which the acceleration and lane change is computed
     * @param sameLaneTraffic Collection&lt;GTU&gt;; the set of observable GTUs in the current lane (can not be null and
     *            may include the <code>gtu</code>)
     * @param rightLaneTraffic Collection&lt;GTU&gt;; the set of observable GTUs in the adjacent lane where GTUs should
     *            drive in the absence of other traffic (must be null if there is no such lane)
     * @param leftLaneTraffic Collection&lt;GTU&gt;; the set of observable GTUs in the adjacent lane into which GTUs
     *            should merge to overtake other traffic (must be null if there is no such lane)
     * @param speedLimit DoubleScalarAbs&lt;SpeedUnit&gt;; the local speed limit
     * @param preferredLaneRouteIncentive DoubleScalar.Rel&lt;AccelerationUnit&gt;; route incentive to merge to the
     *            adjacent lane where GTUs should drive in the absence of other traffic
     * @param laneChangeThreshold DoubleScalar.Rel&lt;AccelerationUnit&gt;; threshold that prevents lane changes that
     *            have very little benefit
     * @param nonPreferredLaneRouteIncentive DoubleScalar.Rel&lt;AccelerationUnit&gt;; route incentive to merge to the
     *            adjacent lane into which GTUs should merge to overtake other traffic
     * @return LaneMovementStep; the result of the lane change and GTU following model
     * @throws RemoteException in case the simulation time cannot be retrieved.
     */
    @SuppressWarnings("checkstyle:parameternumber")
    LaneMovementStep computeLaneChangeAndAcceleration(final LaneBasedGTU<?> gtu,
            final Collection<HeadwayGTU> sameLaneTraffic, final Collection<HeadwayGTU> rightLaneTraffic,
            final Collection<HeadwayGTU> leftLaneTraffic, final DoubleScalar.Abs<SpeedUnit> speedLimit,
            final DoubleScalar.Rel<AccelerationUnit> preferredLaneRouteIncentive,
            final DoubleScalar.Rel<AccelerationUnit> laneChangeThreshold,
            final DoubleScalar.Rel<AccelerationUnit> nonPreferredLaneRouteIncentive) throws RemoteException;

    /**
     * Return the name of this GTU following model.
     * @return String; just the name of the GTU following model
     */
    String getName();

    /**
     * Return complete textual information about this instantiation of this GTU following model.
     * @return String; the name and parameter values of the GTU following model
     */
    String getLongName();

}
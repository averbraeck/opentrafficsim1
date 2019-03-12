package org.opentrafficsim.road.gtu.following;

import java.util.Collection;

import org.djunits.unit.AccelerationUnit;
import org.djunits.unit.LengthUnit;
import org.djunits.unit.TimeUnit;
import org.opentrafficsim.core.network.NetworkException;
import org.opentrafficsim.road.gtu.lane.LaneBasedGTU;

/**
 * Code shared between various car following models.
 * <p>
 * Copyright (c) 2013-2015 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision: 1408 $, $LastChangedDate: 2015-09-24 15:17:25 +0200 (Thu, 24 Sep 2015) $, by $Author: pknoppers $,
 *          initial version 19 feb. 2015 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://Hansvanlint.weblog.tudelft.nl">Hans van Lint</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.citg.tudelft.nl">Guus Tamminga</a>
 * @author <a href="http://www.citg.tudelft.nl">Yufei Yuan</a>
 */
public abstract class AbstractGTUFollowingModel implements GTUFollowingModel
{
    /** Prohibitive deceleration used to construct the TOODANGEROUS result below. */
    private static final AccelerationStep PROHIBITIVEACCELERATIONSTEP = new AccelerationStep(new Acceleration.Abs(
        Double.NEGATIVE_INFINITY, AccelerationUnit.SI), new Time.Abs(Double.NaN, TimeUnit.SI));

    /** Return value if lane change causes immediate collision. */
    public static final DualAccelerationStep TOODANGEROUS = new DualAccelerationStep(PROHIBITIVEACCELERATIONSTEP,
        PROHIBITIVEACCELERATIONSTEP);

    /** {@inheritDoc} */
    @Override
    public final DualAccelerationStep computeAcceleration(final LaneBasedGTU referenceGTU,
        final Collection<HeadwayGTU> otherGTUs, final Speed.Abs speedLimit) throws NetworkException
    {
        Time.Abs when = referenceGTU.getSimulator().getSimulatorTime().getTime();
        // Find out if there is an immediate collision
        for (HeadwayGTU headwayGTU : otherGTUs)
        {
            if (headwayGTU.getOtherGTU() != referenceGTU && null == headwayGTU.getDistance())
            {
                return TOODANGEROUS;
            }
        }
        AccelerationStep followerAccelerationStep = null;
        AccelerationStep referenceGTUAccelerationStep = null;
        GTUFollowingModel gfm = referenceGTU.getGTUFollowingModel();
        // Find the leader and the follower that cause/experience the least positive (most negative) acceleration.
        for (HeadwayGTU headwayGTU : otherGTUs)
        {
            if (null == headwayGTU.getOtherGTU())
            {
                System.out.println("FollowAcceleration.acceleration: Cannot happen");
            }
            if (headwayGTU.getOtherGTU() == referenceGTU)
            {
                continue;
            }
            if (headwayGTU.getDistanceSI() < 0)
            {
                // This one is behind
                AccelerationStep as =
                    gfm.computeAcceleration(headwayGTU.getOtherGTU(), referenceGTU.getLongitudinalVelocity(when),
                        new Length.Rel(-headwayGTU.getDistanceSI(), LengthUnit.SI), speedLimit);
                if (null == followerAccelerationStep || as.getAcceleration().lt(followerAccelerationStep.getAcceleration()))
                {
                    // if (as.getAcceleration().getSI() < -gfm.maximumSafeDeceleration().getSI())
                    // {
                    // return TOODANGEROUS;
                    // }
                    followerAccelerationStep = as;
                }
            }
            else
            {
                // This one is ahead
                AccelerationStep as =
                    gfm.computeAcceleration(referenceGTU, headwayGTU.getOtherGTU().getLongitudinalVelocity(when), headwayGTU
                        .getDistance(), speedLimit);
                if (null == referenceGTUAccelerationStep
                    || as.getAcceleration().lt(referenceGTUAccelerationStep.getAcceleration()))
                {
                    referenceGTUAccelerationStep = as;
                }
            }
        }
        if (null == followerAccelerationStep)
        {
            followerAccelerationStep = gfm.computeAccelerationWithNoLeader(referenceGTU, speedLimit);
        }
        if (null == referenceGTUAccelerationStep)
        {
            referenceGTUAccelerationStep = gfm.computeAccelerationWithNoLeader(referenceGTU, speedLimit);
        }
        return new DualAccelerationStep(referenceGTUAccelerationStep, followerAccelerationStep);
    }

    /** {@inheritDoc} */
    @Override
    public final AccelerationStep computeAcceleration(final LaneBasedGTU follower, final Speed.Abs leaderSpeed,
        final Length.Rel headway, final Speed.Abs speedLimit) 
    {
        final Time.Abs currentTime = follower.getNextEvaluationTime();
        final Speed.Abs followerSpeed = follower.getLongitudinalVelocity(currentTime);
        final Speed.Abs followerMaximumSpeed = follower.getMaximumVelocity();
        Acceleration.Abs newAcceleration =
            computeAcceleration(followerSpeed, followerMaximumSpeed, leaderSpeed, headway, speedLimit);
        Time.Abs nextEvaluationTime = currentTime;
        nextEvaluationTime = nextEvaluationTime.plus(getStepSize());
        return new AccelerationStep(newAcceleration, nextEvaluationTime);

    }

    /** {@inheritDoc} */
    @Override
    public final AccelerationStep computeAccelerationWithNoLeader(final LaneBasedGTU gtu, final Speed.Abs speedLimit)
        throws NetworkException
    {
        Length.Rel stopDistance = new Length.Rel(gtu.getMaximumVelocity().si * gtu.getMaximumVelocity().si 
                / (2.0 * maximumSafeDeceleration().si), LengthUnit.SI);
        return computeAcceleration(gtu, gtu.getLongitudinalVelocity(), stopDistance, speedLimit);
        /*-
        return computeAcceleration(gtu, gtu.getLongitudinalVelocity(), Calc.speedSquaredDividedByDoubleAcceleration(gtu
            .getMaximumVelocity(), maximumSafeDeceleration()), speedLimit);
            */
    }

    /** {@inheritDoc} */
    @Override
    public final Length.Rel minimumHeadway(final Speed.Abs followerSpeed, final Speed.Abs leaderSpeed,
        final Length.Rel precision, final Speed.Abs speedLimit, final Speed.Abs followerMaximumSpeed) 
    {
        if (precision.getSI() <= 0)
        {
            throw new Error("Precision has bad value (must be > 0; got " + precision + ")");
        }
        double maximumDeceleration = -maximumSafeDeceleration().getSI();
        // Find a decent interval to bisect
        double minimumSI = 0;
        double minimumSIDeceleration =
            computeAcceleration(followerSpeed, followerSpeed, leaderSpeed, new Length.Rel(minimumSI, LengthUnit.SI),
                speedLimit).getSI();
        if (minimumSIDeceleration >= maximumDeceleration)
        {
            // Weird... The GTU following model allows zero headway
            return new Length.Rel(0, LengthUnit.SI);
        }
        double maximumSI = 1; // this is - deliberately - way too small
        double maximumSIDeceleration = Double.NaN;
        // Double the value of maximumSI until the resulting deceleration is less severe than the maximum
        for (int step = 0; step < 20; step++)
        {
            maximumSIDeceleration =
                computeAcceleration(followerSpeed, followerMaximumSpeed, leaderSpeed,
                    new Length.Rel(maximumSI, LengthUnit.SI), speedLimit).getSI();
            if (maximumSIDeceleration > maximumDeceleration)
            {
                break;
            }
            maximumSI *= 2;
        }
        if (maximumSIDeceleration < maximumDeceleration)
        {
            throw new Error("Cannot find headway that results in an acceptable deceleration");
        }
        // Now bisect until the error is less than the requested precision
        final int maximumStep = (int) Math.ceil(Math.log((maximumSI - minimumSI) / precision.getSI()) / Math.log(2));
        for (int step = 0; step < maximumStep; step++)
        {
            double midSI = (minimumSI + maximumSI) / 2;
            double midSIAcceleration =
                computeAcceleration(followerSpeed, followerMaximumSpeed, leaderSpeed, new Length.Rel(midSI, LengthUnit.SI),
                    speedLimit).getSI();
            if (midSIAcceleration < maximumDeceleration)
            {
                minimumSI = midSI;
            }
            else
            {
                maximumSI = midSI;
            }
        }
        Length.Rel result = new Length.Rel((minimumSI + maximumSI) / 2, LengthUnit.SI);
        computeAcceleration(followerSpeed, followerMaximumSpeed, leaderSpeed, new Length.Rel(result.getSI(), LengthUnit.SI),
            speedLimit).getSI();
        return result;
    }
}
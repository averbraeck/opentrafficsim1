package org.opentrafficsim.road.gtu.lane.perception.headway;

import org.djunits.value.vdouble.scalar.Acceleration;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Speed;
import org.opentrafficsim.core.gtu.GTUException;
import org.opentrafficsim.core.gtu.GTUType;
import org.opentrafficsim.core.gtu.behavioralcharacteristics.BehavioralCharacteristics;
import org.opentrafficsim.core.network.NetworkException;
import org.opentrafficsim.core.network.route.Route;
import org.opentrafficsim.road.gtu.lane.LaneBasedGTU;
import org.opentrafficsim.road.gtu.lane.tactical.following.CarFollowingModel;
import org.opentrafficsim.road.network.speed.SpeedLimitInfo;
import org.opentrafficsim.road.network.speed.SpeedLimitTypes;

/**
 * Container for a reference to information about a (lane based) GTU and a headway. The Headway can store information about GTUs
 * or objects ahead of the reference GTU, behind the reference GTU, or (partially) parallel to the reference GTU. In addition to
 * the (perceived) headway, several other pieces of information can be stored, such as (perceived) speed, (perceived)
 * acceleration, (perceived) turn indicators, and (perceived) braking lights. <br>
 * This particular version returns behavioral information about the observed GTU objects based on their real state.<br>
 * Special care must be taken in curves when perceiving headway of a GTU or object on an adjacent lane.The question is whether
 * we perceive the parallel or ahead/behind based on a line perpendicular to the front/back of the GTU (rectangular), or
 * perpendicular to the center line of the lane (wedge-shaped in case of a curve). The difficulty of a wedge-shaped situation is
 * that reciprocity might be violated: in case of a clothoid, for instance, it is not sure that the point on the center line
 * when projected from lane 1 to lane 2 is the same as the projection from lane 2 to lane 1. The same holds for shapes with
 * sharp bends. Therefore, algorithms implementing headway should only project the <i>reference point</i> of the reference GTU
 * on the center line of the adjacent lane, and then calculate the forward position and backward position on the adjacent lane
 * based on the reference point. Still, our human perception of what is parallel and what not, is not reflected by fractional
 * positions. See examples in
 * <a href= "http://simulation.tudelft.nl:8085/browse/OTS-113">http://simulation.tudelft.nl:8085/browse/OTS-113</a>.
 * <p>
 * Copyright (c) 2013-2017 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * $LastChangedDate: 2015-07-24 02:58:59 +0200 (Fri, 24 Jul 2015) $, @version $Revision: 1147 $, by $Author: averbraeck $,
 * initial version May 27, 2016 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class HeadwayGTURealCopy extends AbstractHeadwayGTU
{
    /** */
    private static final long serialVersionUID = 20160527L;

    /** stored car following model of the observed GTU. */
    private final CarFollowingModel carFollowingModel;

    /** stored behavioral characteristics of the observed GTU. */
    private final BehavioralCharacteristics behavioralCharacteristics;

    /** stored speed limit info of the observed GTU. */
    private final SpeedLimitInfo speedLimitInfo;

    /** stored route of the observed GTU. */
    private final Route route;

    /**
     * Private constructor for copies.
     * @param id id
     * @param gtuType GTU type
     * @param distance distance
     * @param length length
     * @param speed speed
     * @param acceleration acceleration
     * @param carFollowingModel car-following model
     * @param behavioralCharacteristics behavioral characteristics
     * @param speedLimitInfo speed limit info
     * @param route route
     * @param gtuStatus gtu status
     * @throws GTUException when id is null, objectType is null, or parameters are inconsistent
     */
    @SuppressWarnings("checkstyle:parameternumber")
    HeadwayGTURealCopy(final String id, final GTUType gtuType, final Length distance, final Length length, final Speed speed,
            final Acceleration acceleration, final CarFollowingModel carFollowingModel,
            final BehavioralCharacteristics behavioralCharacteristics, final SpeedLimitInfo speedLimitInfo, final Route route,
            final GTUStatus... gtuStatus) throws GTUException
    {
        super(id, gtuType, distance, true, length, speed, acceleration, gtuStatus);
        this.carFollowingModel = carFollowingModel;
        this.behavioralCharacteristics = behavioralCharacteristics;
        this.speedLimitInfo = speedLimitInfo;
        this.route = route;
    }

    /**
     * Construct a new Headway information object, for a GTU ahead of us or behind us.
     * @param gtu the observed GTU, can not be null.
     * @param distance the distance to the other object; if this constructor is used, distance cannot be null.
     * @param gtuStatus the observable characteristics of the GTU.
     * @throws GTUException when id is null, objectType is null, or parameters are inconsistent
     */
    public HeadwayGTURealCopy(final LaneBasedGTU gtu, final Length distance, final GTUStatus... gtuStatus) throws GTUException
    {
        super(gtu.getId(), gtu.getGTUType(), distance, true, gtu.getLength(), gtu.getSpeed(), gtu.getAcceleration(), gtuStatus);
        this.carFollowingModel = gtu.getTacticalPlanner().getCarFollowingModel();
        this.behavioralCharacteristics = new BehavioralCharacteristics(gtu.getBehavioralCharacteristics());
        this.speedLimitInfo = getSpeedLimitInfo(gtu);
        this.route = gtu.getStrategicalPlanner().getRoute();
    }

    /**
     * Construct a new Headway information object, for a GTU parallel with us.
     * @param gtu the observed GTU, can not be null.
     * @param overlapFront the front-front distance to the other GTU; if this constructor is used, this value cannot be null.
     * @param overlap the 'center' overlap with the other GTU; if this constructor is used, this value cannot be null.
     * @param overlapRear the rear-rear distance to the other GTU; if this constructor is used, this value cannot be null.
     * @throws GTUException when id is null, or parameters are inconsistent
     */
    public HeadwayGTURealCopy(final LaneBasedGTU gtu, final Length overlapFront, final Length overlap, final Length overlapRear)
            throws GTUException
    {
        super(gtu.getId(), gtu.getGTUType(), overlapFront, overlap, overlapRear, true, gtu.getLength(), gtu.getSpeed(),
                gtu.getAcceleration());
        this.carFollowingModel = gtu.getTacticalPlanner().getCarFollowingModel();
        this.behavioralCharacteristics = new BehavioralCharacteristics(gtu.getBehavioralCharacteristics());
        this.speedLimitInfo = getSpeedLimitInfo(gtu);
        this.route = gtu.getStrategicalPlanner().getRoute();
    }

    /**
     * Creates speed limit prospect for given GTU.
     * @param gtu gtu to the the speed limit prospect for
     * @return speed limit prospect for given GTU
     */
    // TODO this is a simple fix
    private SpeedLimitInfo getSpeedLimitInfo(final LaneBasedGTU gtu)
    {
        SpeedLimitInfo sli = new SpeedLimitInfo();
        sli.addSpeedInfo(SpeedLimitTypes.MAX_VEHICLE_SPEED, gtu.getMaximumSpeed());
        try
        {
            sli.addSpeedInfo(SpeedLimitTypes.FIXED_SIGN, gtu.getReferencePosition().getLane().getSpeedLimit(gtu.getGTUType()));
        }
        catch (NetworkException | GTUException exception)
        {
            throw new RuntimeException("Could not obtain speed limit from lane for perception.", exception);
        }
        return sli;
    }

    /** {@inheritDoc} */
    @Override
    public final CarFollowingModel getCarFollowingModel()
    {
        return this.carFollowingModel;
    }

    /** {@inheritDoc} */
    @Override
    public final BehavioralCharacteristics getBehavioralCharacteristics()
    {
        return this.behavioralCharacteristics;
    }

    /** {@inheritDoc} */
    @Override
    public final SpeedLimitInfo getSpeedLimitInfo()
    {
        return this.speedLimitInfo;
    }

    /** {@inheritDoc} */
    @Override
    public final Route getRoute()
    {
        return this.route;
    }

    /** {@inheritDoc} */
    @Override
    public AbstractHeadwayGTU moved(final Length headway, final Speed speed, final Acceleration acceleration)
    {
        try
        {
            return new HeadwayGTURealCopy(getId(), getGtuType(), headway, getLength(), speed, acceleration,
                    getCarFollowingModel(), getBehavioralCharacteristics(), getSpeedLimitInfo(), getRoute(), getGtuStatus());
        }
        catch (GTUException exception)
        {
            // input should be consistent
            throw new RuntimeException("Exception while copying Headway GTU.", exception);
        }
    }

}

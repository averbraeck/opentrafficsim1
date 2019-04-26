package org.opentrafficsim.road.network.lane.object.sensor;

import java.awt.Color;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.language.Throw;

import org.djunits.value.vdouble.scalar.Length;
import org.opentrafficsim.core.compatibility.Compatible;
import org.opentrafficsim.core.dsol.OTSDEVSSimulatorInterface;
import org.opentrafficsim.core.dsol.OTSSimulatorInterface;
import org.opentrafficsim.core.gtu.RelativePosition;
import org.opentrafficsim.core.network.NetworkException;
import org.opentrafficsim.road.gtu.lane.LaneBasedGTU;
import org.opentrafficsim.road.network.animation.SensorAnimation;
import org.opentrafficsim.road.network.lane.CrossSectionElement;
import org.opentrafficsim.road.network.lane.Lane;

/**
 * Sensor that prints which GTU triggers it.
 * <p>
 * Copyright (c) 2013-2017 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands.<br>
 * All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate: 2015-08-12 16:37:45 +0200 (Wed, 12 Aug 2015) $, @version $Revision: 1240 $, by $Author: averbraeck $,
 * initial version an 30, 2015 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class SimpleReportingSensor extends AbstractSensor
{
    /** */
    private static final long serialVersionUID = 20150130L;

    /**
     * Construct a new SimpleReportingSensor.
     * @param lane Lane; the lane on which the new SimpleReportingSensor will be located
     * @param position Length; the position of the sensor along the lane
     * @param triggerPosition RelativePosition.TYPE; the relative position type (e.g., FRONT, BACK) of the vehicle that triggers
     *            the sensor
     * @param id String; the id of the new SimpleReportingSensor
     * @param simulator OTSDEVSSimulatorInterface; the simulator to enable animation
     * @param compatible Compatible; object that can decide if a particular GTU type in a particular driving direction will
     *            trigger the new SimpleReportingSensor
     * @throws NetworkException when the position on the lane is out of bounds w.r.t. the center line of the lane
     */
    public SimpleReportingSensor(final String id, final Lane lane, final Length position,
            final RelativePosition.TYPE triggerPosition, final OTSDEVSSimulatorInterface simulator,
            final Compatible compatible) throws NetworkException
    {
        super(id, lane, position, triggerPosition, simulator, compatible);
        try
        {
            new SensorAnimation(this, position, simulator, Color.YELLOW);
        }
        catch (RemoteException | NamingException exception)
        {
            exception.printStackTrace();
        }
    }

    /** {@inheritDoc} */
    @Override
    public final void triggerResponse(final LaneBasedGTU gtu)
    {
        System.out.println(this + " triggered by " + getPositionType().getName() + " of " + gtu);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public SimpleReportingSensor clone(final CrossSectionElement newCSE, final OTSSimulatorInterface newSimulator,
            final boolean animation) throws NetworkException
    {
        Throw.when(!(newCSE instanceof Lane), NetworkException.class, "sensors can only be cloned for Lanes");
        Throw.when(!(newSimulator instanceof OTSDEVSSimulatorInterface), NetworkException.class,
                "simulator should be a DEVSSimulator");
        return new SimpleReportingSensor(getId(), (Lane) newCSE, getLongitudinalPosition(), getPositionType(),
                (OTSDEVSSimulatorInterface) newSimulator, getDetectedGTUTypes());

        // the sensor creates its own animation (for now)
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        return "SimpleReportingSensor []";
    }

}
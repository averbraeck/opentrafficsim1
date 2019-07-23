package org.opentrafficsim.road.gtu.lane;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.SimRuntimeException;

import org.djunits.unit.TimeUnit;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Time;
import org.opentrafficsim.core.dsol.OTSAnimatorInterface;
import org.opentrafficsim.core.dsol.OTSDEVSSimulatorInterface;
import org.opentrafficsim.core.gtu.GTUException;
import org.opentrafficsim.core.network.NetworkException;
import org.opentrafficsim.road.gtu.animation.DefaultBlockOnOffAnimation;
import org.opentrafficsim.road.network.factory.opendrive.TrafficLightState;
import org.opentrafficsim.road.network.lane.Lane;

/**
 * Special GTU that cannot move, but it can be seen by other GTUs.
 * <p>
 * Copyright (c) 2013-2015 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision: 1155 $, $LastChangedDate: 2015-07-26 01:01:13 +0200 (Sun, 26 Jul 2015) $, by $Author: averbraeck $,
 *          initial version 15 jul. 2015 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class LaneBlockOnOff extends AbstractTrafficLight
{
    /** */
    private static final long serialVersionUID = 20150624L;
    
    private TrafficLightState currentState = TrafficLightState.RED;


    /**
     * @param name the name of the OnOffTrafficLight
     * @param lane The lane where the block has to be put
     * @param position the position on the lane as a length
     * @param simulator the simulator to avoid NullPointerExceptions
     * @throws GTUException when GTU cannot be created.
     * @throws NamingException if an error occurs when adding the animation handler
     * @throws NetworkException when the GTU cannot be placed on the given lane
     */
    public LaneBlockOnOff(final String name, final Lane lane, final Length.Rel position,
        final OTSDEVSSimulatorInterface simulator) throws GTUException, NetworkException, NamingException
    {
        super(name, lane, position, simulator);

        try
        {
            new DefaultBlockOnOffAnimation(this, getSimulator());
            // animation
            if (simulator instanceof OTSAnimatorInterface)
            {
                // TODO
            }
            //getSimulator().scheduleEventRel(new Time.Rel(60.0, TimeUnit.SECOND), this, this, "changeColorTime", null);
        }
        catch (RemoteException exception)
        {
            exception.printStackTrace();
        }
    }
    
    
    /**
     * 
     */
    public void setGreen()
    {
        setBlocked(!isBlocked());
        this.currentState = TrafficLightState.GREEN;

        try
        {
            getSimulator().scheduleEventRel(new Time.Rel(15.0, TimeUnit.SECOND), this, this, "setYellow", null);
        }
        catch (SimRuntimeException exception)
        {
            exception.printStackTrace();
        }
    }
    
    /**
     * 
     */
    protected void setYellow()
    {
        //setBlocked(!isBlocked());
        this.currentState = TrafficLightState.YELLOW;

        try
        {
            getSimulator().scheduleEventRel(new Time.Rel(9.0, TimeUnit.SECOND), this, this, "setRed", null);
        }
        catch (SimRuntimeException exception)
        {
            exception.printStackTrace();
        }
    }
    
    /**
     * 
     */
    protected void setRed()
    {
        setBlocked(!isBlocked());
        this.currentState = TrafficLightState.RED;
    }
    

    /**
     * @return currentState
     */
    public TrafficLightState getCurrentState()
    {
        return this.currentState;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "LaneBlockOnOff [lane=" + this.lane + ", position=" + this.position + "]";
    }

}
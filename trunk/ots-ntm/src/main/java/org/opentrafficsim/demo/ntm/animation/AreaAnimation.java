package org.opentrafficsim.demo.ntm.animation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.image.ImageObserver;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.animation.D2.Renderable2D;

import org.opentrafficsim.core.dsol.OTSSimulatorInterface;
import org.opentrafficsim.demo.ntm.Area;

/**
 * <p>
 * Copyright (c) 2013-2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights
 * reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version Sep 13, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class AreaAnimation extends Renderable2D
{
    /**
     * @param source
     * @param simulator
     * @throws NamingException
     * @throws RemoteException
     */
    public AreaAnimation(Area source, OTSSimulatorInterface simulator) throws NamingException, RemoteException
    {
        super(source, simulator);
    }

    /** {@inheritDoc} */
    @Override
    public void paint(Graphics2D graphics, ImageObserver observer) throws RemoteException
    {
        for (Path2D polygon : ((Area) getSource()).getPolygons())
        {
            // graphics.setPaint(Color.LIGHT_GRAY);
            // graphics.fill(polygon);
            graphics.setColor(Color.GRAY);
            graphics.draw(polygon);
        }
    }

}

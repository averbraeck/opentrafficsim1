package org.opentrafficsim.demo.geometry.shape;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.animation.D2.AnimationPanel;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.gui.swing.DSOLApplication;
import nl.tudelft.simulation.dsol.gui.swing.DSOLPanel;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.event.Event;

import org.opentrafficsim.core.dsol.OTSDEVSAnimator;
import org.opentrafficsim.core.dsol.OTSReplication;
import org.opentrafficsim.core.dsol.OTSSimTimeDouble;
import org.opentrafficsim.core.unit.TimeUnit;
import org.opentrafficsim.core.value.vdouble.scalar.DoubleScalar;

/**
 * <p>
 * Copyright (c) 2013-2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate$, @version $Revision$, by $Author$, initial versionAug 15, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class ShapeTestApplication extends DSOLApplication
{
    /**
     * @param title String title of the application window
     * @param panel DSOLPanel
     */
    public ShapeTestApplication(final String title,
        final DSOLPanel<DoubleScalar.Abs<TimeUnit>, DoubleScalar.Rel<TimeUnit>, OTSSimTimeDouble> panel)
    {
        super(title, panel);
    }

    /** */
    private static final long serialVersionUID = 20140819L;

    /**
     * @param args String[]; command line arguments
     * @throws SimRuntimeException on ???
     * @throws NamingException on ???
     * @throws IOException on ???
     */
    public static void main(final String[] args) throws SimRuntimeException, NamingException, IOException
    {
        ShapeModel model = new ShapeModel();
        OTSDEVSAnimator simulator = new OTSDEVSAnimator();
        OTSReplication replication =
            new OTSReplication("rep1", new OTSSimTimeDouble(new DoubleScalar.Abs<TimeUnit>(0.0, TimeUnit.SECOND)),
                new DoubleScalar.Rel<TimeUnit>(0.0, TimeUnit.SECOND),
                new DoubleScalar.Rel<TimeUnit>(7200.0, TimeUnit.SECOND), model);
        simulator.initialize(replication, ReplicationMode.TERMINATING);

        DSOLPanel<DoubleScalar.Abs<TimeUnit>, DoubleScalar.Rel<TimeUnit>, OTSSimTimeDouble> panel =
            new DSOLPanel<DoubleScalar.Abs<TimeUnit>, DoubleScalar.Rel<TimeUnit>, OTSSimTimeDouble>(model, simulator);

        Rectangle2D extent = new Rectangle2D.Double(65000.0, 440000.0, 55000.0, 30000.0);
        Dimension size = new Dimension(1024, 768);
        AnimationPanel animationPanel = new AnimationPanel(extent, size, simulator);
        panel.getTabbedPane().addTab(0, "animation", animationPanel);

        // tell the animation panel to update its statistics
        // TODO should be done automatically in DSOL!
        animationPanel.notify(new Event(SimulatorInterface.START_REPLICATION_EVENT, simulator, null));

        new ShapeTestApplication("Network Transmission Model", panel);
    }

}

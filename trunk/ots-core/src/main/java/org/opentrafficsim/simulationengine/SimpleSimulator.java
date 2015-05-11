package org.opentrafficsim.simulationengine;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.gui.swing.DSOLPanel;

import org.opentrafficsim.core.dsol.OTSDEVSSimulator;
import org.opentrafficsim.core.dsol.OTSModelInterface;
import org.opentrafficsim.core.dsol.OTSReplication;
import org.opentrafficsim.core.dsol.OTSSimTimeDouble;
import org.opentrafficsim.core.unit.TimeUnit;
import org.opentrafficsim.core.value.vdouble.scalar.DoubleScalar;

/**
 * Construct a DSOL DEVSSimulator or DEVSAnimator the easy way.
 * <p>
 * Copyright (c) 2013-2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights
 * reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version 12 nov. 2014 <br>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class SimpleSimulator extends OTSDEVSSimulator implements SimpleSimulation
{
    /** */
    private static final long serialVersionUID = 20150510L;

    /** Counter for replication. */
    private int lastReplication = 0;

    /** The JPanel that contains the simulator controls, a status bar and a JTabbedPane with switchable sub panels. */
    private final DSOLPanel<DoubleScalar.Abs<TimeUnit>, DoubleScalar.Rel<TimeUnit>, OTSSimTimeDouble> panel;

    /**
     * Create a simulation engine without animation; the easy way. PauseOnError is set to true;
     * @param startTime OTSSimTimeDouble; the start time of the simulation
     * @param warmupPeriod DoubleScalar.Rel&lt;TimeUnit&gt;; the warm up period of the simulation (use new
     *            DoubleScalar.Rel&lt;TimeUnit&gt;(0, TimeUnit.SECOND) if you don't know what this is)
     * @param runLength DoubleScalar.Rel&lt;TimeUnit&gt;; the duration of the simulation
     * @param model OTSModelInterface; the simulation to execute
     * @throws RemoteException on communications failure
     * @throws SimRuntimeException on ???
     * @throws NamingException when the context for the replication cannot be created
     */
    public SimpleSimulator(final DoubleScalar.Abs<TimeUnit> startTime, final DoubleScalar.Rel<TimeUnit> warmupPeriod,
            final DoubleScalar.Rel<TimeUnit> runLength, final OTSModelInterface model) throws RemoteException,
            SimRuntimeException, NamingException
    {
        setPauseOnError(true);
        initialize(new OTSReplication("rep" + ++this.lastReplication, new OTSSimTimeDouble(startTime), warmupPeriod,
                runLength, model), ReplicationMode.TERMINATING);
        this.panel =
                new DSOLPanel<DoubleScalar.Abs<TimeUnit>, DoubleScalar.Rel<TimeUnit>, OTSSimTimeDouble>(model, this);
    }

    /**
     * {@inheritDoc}
     */
    public final DSOLPanel<DoubleScalar.Abs<TimeUnit>, DoubleScalar.Rel<TimeUnit>, OTSSimTimeDouble> getPanel()
    {
        return this.panel;
    }

    /**
     * {@inheritDoc}
     */
    public final void runUpTo(final DoubleScalar.Abs<TimeUnit> when) throws SimRuntimeException
    {
        scheduleEvent(when, SimEventInterface.MAX_PRIORITY, this, this, "autoPauseSimulator", null);
        while (getSimulatorTime().get().getSI() < when.getSI())
        {
            step();
        }
    }

    /**
     * Pause the simulator.
     */
    @SuppressWarnings("unused")
    private void autoPauseSimulator()
    {
        if (isRunning())
        {
            stop();
        }
    }

    /**
     * {@inheritDoc}
     */
    public final SimEvent<OTSSimTimeDouble> scheduleEvent(final DoubleScalar.Abs<TimeUnit> executionTime,
            final short priority, final Object source, final Object target, final String method, final Object[] args)
            throws SimRuntimeException
    {
        SimEvent<OTSSimTimeDouble> result =
                new SimEvent<OTSSimTimeDouble>(new OTSSimTimeDouble(new DoubleScalar.Abs<TimeUnit>(
                        executionTime.getSI(), TimeUnit.SECOND)), priority, source, target, method, args);
        scheduleEvent(result);
        return result;
    }

}

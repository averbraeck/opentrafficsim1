package org.opentrafficsim.road.animation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.opentrafficsim.base.modelproperties.Property;
import org.opentrafficsim.core.dsol.OTSDEVSSimulatorInterface;
import org.opentrafficsim.core.dsol.OTSModelInterface;
import org.opentrafficsim.core.dsol.OTSSimTimeDouble;
import org.opentrafficsim.core.gtu.Try;
import org.opentrafficsim.core.gtu.animation.GTUColorer;
import org.opentrafficsim.core.network.OTSLink;
import org.opentrafficsim.core.network.OTSNetwork;
import org.opentrafficsim.core.network.OTSNode;
import org.opentrafficsim.road.gtu.animation.DefaultSwitchableGTUColorer;
import org.opentrafficsim.road.gtu.generator.GTUGenerator;
import org.opentrafficsim.road.network.lane.object.SpeedSign;
import org.opentrafficsim.simulationengine.AbstractWrappableAnimation;
import org.opentrafficsim.simulationengine.AbstractWrappableSimulation;
import org.opentrafficsim.simulationengine.OTSSimulationException;
import org.opentrafficsim.simulationengine.SimpleSimulatorInterface;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;
import nl.tudelft.simulation.language.Throw;

/**
 * Template for simulation script.
 * <p>
 * Copyright (c) 2013-2017 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version 9 apr. 2018 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public abstract class AbstractSimulationScript implements EventListenerInterface
{

    /** Name. */
    final String name;

    /** Description. */
    final String description;

    /** The simulator. */
    OTSDEVSSimulatorInterface simulator;

    /** The network. */
    OTSNetwork network;

    /** Properties as String value, e.g. from command line. */
    private final Map<String, String> props = new HashMap<>();

    /** GTU colorer. */
    GTUColorer gtuColorer = new DefaultSwitchableGTUColorer();

    /**
     * Constructor;
     * @param name String; name
     * @param description String; description
     * @param properties String[]; properties as name-value pairs
     */
    protected AbstractSimulationScript(final String name, final String description, final String[] properties)
    {
        this.name = name;
        this.description = description;
        this.props.put("seed", "1");
        this.props.put("startTime", "0");
        this.props.put("warmupTime", "0");
        this.props.put("simulationTime", "3600");
        this.props.put("autorun", "false");
        setDefaultProperties();
        for (int i = 0; i < properties.length; i += 2)
        {
            this.props.put(properties[i], properties[i + 1]);
        }
    }

    /**
     * Sets a property.
     * @param propertyName String; property name
     * @param propertyValue Object; property value
     */
    public final void setProperty(final String propertyName, final Object propertyValue)
    {
        this.props.put(propertyName, propertyValue.toString());
    }

    /**
     * Returns the String value of given property.
     * @param propertyName String; property name
     * @return String; value of property
     */
    public final String getProperty(final String propertyName)
    {
        String p = this.props.get(propertyName);
        Throw.when(p == null, IllegalStateException.class, "Property %s is not given.", propertyName);
        return p;
    }

    /**
     * Returns the double value of given property.
     * @param propertyName String; property name
     * @return double; value of property
     */
    public final double getDoubleProperty(final String propertyName)
    {
        return Double.parseDouble(getProperty(propertyName));
    }

    /**
     * Returns the boolean value of given property.
     * @param propertyName String; property name
     * @return double; value of property
     */
    public final boolean getBooleanProperty(final String propertyName)
    {
        return Boolean.parseBoolean(getProperty(propertyName));
    }

    /**
     * Returns the int value of given property.
     * @param propertyName String; property name
     * @return int; value of property
     */
    public final int getIntegerProperty(final String propertyName)
    {
        return Integer.parseInt(getProperty(propertyName));
    }

    /**
     * Returns the Duration value of given property.
     * @param propertyName String; property name
     * @return Duration; value of property
     */
    public final Duration getDurationProperty(final String propertyName)
    {
        return Duration.createSI(getDoubleProperty(propertyName));
    }

    /**
     * Returns the Time value of given property.
     * @param propertyName String; property name
     * @return Time; value of property
     */
    public final Time getTimeProperty(final String propertyName)
    {
        return Time.createSI(getDoubleProperty(propertyName));
    }

    /**
     * Set GTU colorer.
     * @param colorer GTUColorer; GTU colorer
     */
    public final void setGtuColorer(final GTUColorer colorer)
    {
        this.gtuColorer = colorer;
    }

    /**
     * Returns the GTU colorer.
     * @return returns the GTU colorer
     */
    public final GTUColorer getGtuColorer()
    {
        return this.gtuColorer;
    }

    /**
     * Starts the simulation.
     */
    public final void start()
    {
        Time startTime = getTimeProperty("startTime");
        Duration warmupTime = getDurationProperty("warmupTime");
        Duration simulationTime = getDurationProperty("simulationTime");
        if (getBooleanProperty("autorun"))
        {

            ScriptSimulation scriptSimulation = this.new ScriptSimulation();
            try
            {
                OTSDEVSSimulatorInterface sim =
                        scriptSimulation.buildSimulator(startTime, warmupTime, simulationTime, new ArrayList<Property<?>>());
                sim.addListener(this, SimulatorInterface.END_OF_REPLICATION_EVENT);
                double tReport = 60.0;
                Time t = sim.getSimulatorTime().getTime();
                while (t.si < simulationTime.si)
                {
                    sim.step();
                    t = sim.getSimulatorTime().getTime();
                    if (t.si >= tReport)
                    {
                        System.out.println("Simulation time is " + t);
                        tReport += 60.0;
                    }
                }
                sim.stop(); // end of simulation event
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }
        else
        {
            Try.execute(() -> new ScriptAnimation().buildAnimator(startTime, warmupTime, simulationTime,
                    new ArrayList<Property<?>>(), null, true), RuntimeException.class, "Exception from properties.");
        }
    }

    /** {@inheritDoc} */
    @Override
    public final void notify(EventInterface event) throws RemoteException
    {
        if (event.getType().equals(SimulatorInterface.END_OF_REPLICATION_EVENT))
        {
            onSimulationEnd();
            // solve bug that event is fired twice
            AbstractSimulationScript.this.simulator.removeListener(AbstractSimulationScript.this,
                    SimulatorInterface.END_OF_REPLICATION_EVENT);
        }
    }

    // Overriddable methods

    /**
     * Sets the animation toggles. May be overridden.
     * @param animation AbstractWrappableAnimation; animation to set the toggle on
     */
    protected void addAnimationToggles(final AbstractWrappableAnimation animation)
    {
        AnimationToggles.setIconAnimationTogglesFull(animation);
        animation.toggleAnimationClass(OTSLink.class);
        animation.toggleAnimationClass(OTSNode.class);
        animation.toggleAnimationClass(GTUGenerator.class);
        animation.showAnimationClass(SpeedSign.class);
    }

    /**
     * Adds taps to the animation. May be overridden.
     * @param sim SimpleSimulatorInterface; simulator
     */
    protected void addTabs(final SimpleSimulatorInterface sim)
    {
        //
    }

    /**
     * Sets the default properties. Can be overridden and use method {@code setProperty()}. Default implementation does nothing.
     */
    protected void setDefaultProperties()
    {
        //
    }

    /**
     * Method that is called when the simulation has ended. This can be used to store data.
     */
    protected void onSimulationEnd()
    {
        //
    }

    // Abstract methods

    /**
     * Sets up the simulation based on provided properties. Properties can be obtained with {@code getProperty()}. Setting up a
     * simulation should at least create a network and some demand. Additionally this may setup traffic control, sampling, etc.
     * @param sim OTSDEVSSimulatorInterface; simulator
     * @return OTSNetwork; network
     * @throws Exception on any exception
     */
    protected abstract OTSNetwork setupSimulation(OTSDEVSSimulatorInterface sim) throws Exception;

    // Nested classes

    /**
     * Simulation.
     * <p>
     * Copyright (c) 2013-2017 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved.
     * <br>
     * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
     * <p>
     * @version $Revision$, $LastChangedDate$, by $Author$, initial version 9 apr. 2018 <br>
     * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
     * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
     */
    public class ScriptSimulation extends AbstractWrappableSimulation
    {
        /** */
        private static final long serialVersionUID = 20180409L;

        /** {@inheritDoc} */
        @Override
        public String shortName()
        {
            return AbstractSimulationScript.this.name;
        }

        /** {@inheritDoc} */
        @Override
        public String description()
        {
            return AbstractSimulationScript.this.description;
        }

        /** {@inheritDoc} */
        @Override
        protected OTSModelInterface makeModel() throws OTSSimulationException
        {
            return new ScriptModel();
        }
    }

    /**
     * Animated simulation.
     * <p>
     * Copyright (c) 2013-2017 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved.
     * <br>
     * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
     * <p>
     * @version $Revision$, $LastChangedDate$, by $Author$, initial version 9 apr. 2018 <br>
     * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
     * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
     */
    public class ScriptAnimation extends AbstractWrappableAnimation
    {
        /** */
        private static final long serialVersionUID = 20180409L;

        /** {@inheritDoc} */
        @Override
        public String shortName()
        {
            return AbstractSimulationScript.this.name;
        }

        /** {@inheritDoc} */
        @Override
        public String description()
        {
            return AbstractSimulationScript.this.description;
        }

        /** {@inheritDoc} */
        @Override
        protected OTSModelInterface makeModel() throws OTSSimulationException
        {
            return new ScriptModel();
        }

        /** {@inheritDoc} */
        @Override
        protected final void addAnimationToggles()
        {
            AbstractSimulationScript.this.addAnimationToggles(this);
        }

        /** {@inheritDoc} */
        @Override
        protected final void addTabs(final SimpleSimulatorInterface sim)
        {
            AbstractSimulationScript.this.addTabs(sim);
        }

        /** {@inheritDoc} */
        @Override
        public final GTUColorer getColorer()
        {
            return AbstractSimulationScript.this.gtuColorer;
        }
    }

    /**
     * Model.
     * <p>
     * Copyright (c) 2013-2017 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved.
     * <br>
     * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
     * <p>
     * @version $Revision$, $LastChangedDate$, by $Author$, initial version 9 apr. 2018 <br>
     * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
     * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
     */
    private class ScriptModel implements OTSModelInterface
    {
        /** */
        private static final long serialVersionUID = 20180409L;

        /**
         * 
         */
        public ScriptModel()
        {
        }

        /** {@inheritDoc} */
        @Override
        public void constructModel(SimulatorInterface<Time, Duration, OTSSimTimeDouble> sim)
                throws SimRuntimeException, RemoteException
        {
            AbstractSimulationScript.this.simulator = (OTSDEVSSimulatorInterface) sim;
            Map<String, StreamInterface> streams = new HashMap<>();
            StreamInterface stream = new MersenneTwister(Long.valueOf(getProperty("seed")));
            streams.put("generation", stream);
            sim.getReplication().setStreams(streams);
            AbstractSimulationScript.this.network =
                    Try.assign(() -> AbstractSimulationScript.this.setupSimulation((OTSDEVSSimulatorInterface) sim),
                            RuntimeException.class, "Exception while setting up simulation.");
            AbstractSimulationScript.this.simulator.addListener(AbstractSimulationScript.this,
                    SimulatorInterface.END_OF_REPLICATION_EVENT);
        }

        /** {@inheritDoc} */
        @Override
        public SimulatorInterface<Time, Duration, OTSSimTimeDouble> getSimulator() throws RemoteException
        {
            return AbstractSimulationScript.this.simulator;
        }

        /** {@inheritDoc} */
        @Override
        public OTSNetwork getNetwork()
        {
            return AbstractSimulationScript.this.network;
        }
    }

}

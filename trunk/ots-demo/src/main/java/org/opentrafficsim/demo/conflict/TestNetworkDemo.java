package org.opentrafficsim.demo.conflict;

import static org.opentrafficsim.core.gtu.GTUType.VEHICLE;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.naming.NamingException;
import javax.swing.SwingUtilities;

import org.djunits.unit.DurationUnit;
import org.djunits.unit.LengthUnit;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Time;
import org.opentrafficsim.base.modelproperties.Property;
import org.opentrafficsim.base.modelproperties.PropertyException;
import org.opentrafficsim.core.dsol.OTSModelInterface;
import org.opentrafficsim.core.network.OTSNetwork;
import org.opentrafficsim.road.animation.AnimationToggles;
import org.opentrafficsim.road.network.factory.xml.XmlNetworkLaneParser;
import org.opentrafficsim.road.network.lane.CrossSectionLink;
import org.opentrafficsim.road.network.lane.conflict.ConflictBuilder;
import org.opentrafficsim.road.network.lane.conflict.LaneCombinationList;
import org.opentrafficsim.simulationengine.AbstractWrappableAnimation;
import org.opentrafficsim.simulationengine.OTSSimulationException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.language.io.URLResource;

/**
 * <p>
 * Copyright (c) 2013-2017 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version 11 dec. 2016 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class TestNetworkDemo extends AbstractWrappableAnimation
{

    /** */
    private static final long serialVersionUID = 20161211L;

    /** {@inheritDoc} */
    @Override
    protected final OTSModelInterface makeModel() throws OTSSimulationException
    {
        return new TestNetworkModel();
    }

    /** {@inheritDoc} */
    @Override
    protected final void addAnimationToggles()
    {
        AnimationToggles.setTextAnimationTogglesStandard(this);
    }

    /** {@inheritDoc} */
    @Override
    public final String shortName()
    {
        return "Test network demonstration";
    }

    /** {@inheritDoc} */
    @Override
    public final String description()
    {
        return "Test network demonstration";
    }

    /**
     * The simulation model.
     */
    class TestNetworkModel implements OTSModelInterface
    {

        /** */
        private static final long serialVersionUID = 20161211L;

        /** The network. */
        private OTSNetwork network;

        /** Simulator. */
        private DEVSSimulatorInterface.TimeDoubleUnit simulator;

        /** {@inheritDoc} */
        @Override
        public void constructModel(final SimulatorInterface<Time, Duration, SimTimeDoubleUnit> arg0)
                throws SimRuntimeException
        {
            this.simulator = (DEVSSimulatorInterface.TimeDoubleUnit) arg0;
            try
            {
                URL url = URLResource.getResource("/conflict/Test-Network-14.xml");
                XmlNetworkLaneParser nlp = new XmlNetworkLaneParser(this.simulator);
                this.network = nlp.build(url, true);

                LaneCombinationList ignoreList = new LaneCombinationList();
                // ignoreList.addLinkCombination((CrossSectionLink) this.network.getLink("L_D3b-D3a"),
                // (CrossSectionLink) this.network.getLink("L_B3a-A3b"));
                // ignoreList.addLinkCombination((CrossSectionLink) this.network.getLink("L_A3a-D3a"),
                // (CrossSectionLink) this.network.getLink("L_C3b-B3b"));
                // ignoreList.addLinkCombination((CrossSectionLink) this.network.getLink("L_H3b-H3a"),
                // (CrossSectionLink) this.network.getLink("L_F3a-E3b"));
                // ignoreList.addLinkCombination((CrossSectionLink) this.network.getLink("L_E3a-H3a"),
                // (CrossSectionLink) this.network.getLink("L_G3b-F3b"));
                LaneCombinationList permittedList = new LaneCombinationList();
                permittedList.addLinkCombination((CrossSectionLink) this.network.getLink("L_D3b-D3a"),
                        (CrossSectionLink) this.network.getLink("L_B3a-A3b"));
                permittedList.addLinkCombination((CrossSectionLink) this.network.getLink("L_A3a-D3a"),
                        (CrossSectionLink) this.network.getLink("L_C3b-B3b"));
                permittedList.addLinkCombination((CrossSectionLink) this.network.getLink("L_H3b-H3a"),
                        (CrossSectionLink) this.network.getLink("L_F3a-E3b"));
                permittedList.addLinkCombination((CrossSectionLink) this.network.getLink("L_E3a-H3a"),
                        (CrossSectionLink) this.network.getLink("L_G3b-F3b"));
                ConflictBuilder.buildConflicts(this.network, VEHICLE, this.simulator,
                        new ConflictBuilder.FixedWidthGenerator(new Length(2.0, LengthUnit.SI)), ignoreList, permittedList);
                // new ConflictBuilder.FixedWidthGenerator(new Length(1.0, LengthUnit.SI))
                // ConflictBuilder.DEFAULT_WIDTH_GENERATOR

            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }

        /** {@inheritDoc} */
        @Override
        public SimulatorInterface<Time, Duration, SimTimeDoubleUnit> getSimulator()
        {
            return this.simulator;
        }

        /** {@inheritDoc} */
        @Override
        public OTSNetwork getNetwork()
        {
            return this.network;
        }

    }

    /**
     * Main program.
     * @param args String[]; the command line arguments (not used)
     * @throws SimRuntimeException should never happen
     */
    public static void main(final String[] args) throws SimRuntimeException
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    TestNetworkDemo animation = new TestNetworkDemo();
                    // 1 hour simulation run for testing
                    animation.buildAnimator(Time.ZERO, Duration.ZERO, new Duration(60.0, DurationUnit.MINUTE),
                            new ArrayList<Property<?>>(), null, true);
                }
                catch (SimRuntimeException | NamingException | OTSSimulationException | PropertyException exception)
                {
                    exception.printStackTrace();
                }
            }
        });
    }

}

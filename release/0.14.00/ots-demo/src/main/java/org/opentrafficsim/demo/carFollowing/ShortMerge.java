package org.opentrafficsim.demo.carFollowing;

import static org.opentrafficsim.road.gtu.lane.RoadGTUTypes.CAR;
import static org.opentrafficsim.road.gtu.lane.RoadGTUTypes.TRUCK;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.djunits.unit.AccelerationUnit;
import org.djunits.unit.FrequencyUnit;
import org.djunits.unit.LengthUnit;
import org.djunits.unit.SpeedUnit;
import org.djunits.unit.TimeUnit;
import org.djunits.value.vdouble.scalar.Acceleration;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Frequency;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Speed;
import org.djunits.value.vdouble.scalar.Time;
import org.opentrafficsim.base.modelproperties.Property;
import org.opentrafficsim.core.distributions.Distribution.FrequencyAndObject;
import org.opentrafficsim.core.distributions.Generator;
import org.opentrafficsim.core.distributions.ProbabilityException;
import org.opentrafficsim.core.dsol.OTSDEVSSimulatorInterface;
import org.opentrafficsim.core.dsol.OTSModelInterface;
import org.opentrafficsim.core.dsol.OTSSimTimeDouble;
import org.opentrafficsim.core.gtu.GTUDirectionality;
import org.opentrafficsim.core.gtu.GTUException;
import org.opentrafficsim.core.gtu.GTUType;
import org.opentrafficsim.core.gtu.animation.GTUColorer;
import org.opentrafficsim.core.gtu.behavioralcharacteristics.BehavioralCharacteristics;
import org.opentrafficsim.core.gtu.behavioralcharacteristics.BehavioralCharacteristicsFactory;
import org.opentrafficsim.core.gtu.behavioralcharacteristics.BehavioralCharacteristicsFactoryByType;
import org.opentrafficsim.core.gtu.behavioralcharacteristics.ParameterException;
import org.opentrafficsim.core.gtu.behavioralcharacteristics.ParameterTypes;
import org.opentrafficsim.core.idgenerator.IdGenerator;
import org.opentrafficsim.core.network.NetworkException;
import org.opentrafficsim.core.network.OTSLink;
import org.opentrafficsim.core.network.OTSNetwork;
import org.opentrafficsim.core.network.OTSNode;
import org.opentrafficsim.core.network.route.ProbabilisticRouteGenerator;
import org.opentrafficsim.core.network.route.Route;
import org.opentrafficsim.core.network.route.RouteGenerator;
import org.opentrafficsim.road.animation.AnimationToggles;
import org.opentrafficsim.road.gtu.generator.CharacteristicsGenerator;
import org.opentrafficsim.road.gtu.generator.GTUTypeGenerator;
import org.opentrafficsim.road.gtu.generator.LaneBasedGTUGenerator;
import org.opentrafficsim.road.gtu.generator.LaneBasedGTUGenerator.RoomChecker;
import org.opentrafficsim.road.gtu.generator.SpeedGenerator;
import org.opentrafficsim.road.gtu.generator.TTCRoomChecker;
import org.opentrafficsim.road.gtu.lane.tactical.LaneBasedTacticalPlannerFactory;
import org.opentrafficsim.road.gtu.lane.tactical.following.AbstractIDM;
import org.opentrafficsim.road.gtu.lane.tactical.following.CarFollowingModelFactory;
import org.opentrafficsim.road.gtu.lane.tactical.following.IDMPlus;
import org.opentrafficsim.road.gtu.lane.tactical.following.IDMPlusFactory;
import org.opentrafficsim.road.gtu.lane.tactical.lmrs.DefaultLMRSPerceptionFactory;
import org.opentrafficsim.road.gtu.lane.tactical.lmrs.LMRS;
import org.opentrafficsim.road.gtu.lane.tactical.lmrs.LMRSFactory;
import org.opentrafficsim.road.gtu.lane.tactical.util.lmrs.LmrsParameters;
import org.opentrafficsim.road.gtu.strategical.route.LaneBasedStrategicalRoutePlannerFactory;
import org.opentrafficsim.road.network.factory.xml.XmlNetworkLaneParser;
import org.opentrafficsim.road.network.lane.CrossSectionLink;
import org.opentrafficsim.road.network.lane.DirectedLanePosition;
import org.opentrafficsim.road.network.lane.Lane;
import org.opentrafficsim.simulationengine.AbstractWrappableAnimation;
import org.opentrafficsim.simulationengine.OTSSimulationException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;
import nl.tudelft.simulation.language.io.URLResource;

/**
 * <p>
 * Copyright (c) 2013-2016 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version 7 apr. 2017 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class ShortMerge extends AbstractWrappableAnimation
{

    /** Simulation time. */
    public static final Time SIMTIME = Time.createSI(3600);

    /** */
    private static final long serialVersionUID = 20170407L;

    /** The simulator. */
    private OTSDEVSSimulatorInterface sim;

    /** {@inheritDoc} */
    @Override
    public String shortName()
    {
        return "ShortMerge";
    }

    /** {@inheritDoc} */
    @Override
    public String description()
    {
        return "Short merge to test lane change models.";
    }

    /** {@inheritDoc} */
    @Override
    protected final void addAnimationToggles()
    {
        AnimationToggles.setTextAnimationTogglesFull(this);
        this.toggleAnimationClass(OTSLink.class);
        this.toggleAnimationClass(OTSNode.class);
    }

    /** {@inheritDoc} */
    @Override
    protected OTSModelInterface makeModel(final GTUColorer colorer) throws OTSSimulationException
    {
        return new ShortMergeModel(colorer);
    }

    /**
     * @return simulator.
     */
    public OTSDEVSSimulatorInterface getSimulator()
    {
        return this.sim;
    }

    /**
     * @param simulator set simulator.
     */
    public void setSimulator(final OTSDEVSSimulatorInterface simulator)
    {
        this.sim = simulator;
    }

    /**
     * Main method.
     * @param args args for main program
     */
    public static void main(final String[] args)
    {

        ShortMerge shortMerge = new ShortMerge();
        try
        {
            shortMerge.buildAnimator(Time.ZERO, Duration.ZERO, Duration.createSI(SIMTIME.si), new ArrayList<Property<?>>(),
                    null, true);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

    }

    /**
     * <p>
     * Copyright (c) 2013-2016 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved.
     * <br>
     * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
     * <p>
     * @version $Revision$, $LastChangedDate$, by $Author$, initial version 7 apr. 2017 <br>
     * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
     * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
     */
    class ShortMergeModel implements OTSModelInterface
    {

        /**
         * @param network set network.
         */
        public void setNetwork(final OTSNetwork network)
        {
            this.network = network;
        }

        /** */
        private static final long serialVersionUID = 20170407L;

        /** The network. */
        private OTSNetwork network;

        /** Colorer. */
        private final GTUColorer colorer;

        /**
         * @param colorer colorer
         */
        ShortMergeModel(final GTUColorer colorer)
        {
            this.colorer = colorer;
        }

        /** {@inheritDoc} */
        @Override
        public void constructModel(final SimulatorInterface<Time, Duration, OTSSimTimeDouble> simulator)
                throws SimRuntimeException, RemoteException
        {
            ShortMerge.this.setSimulator((OTSDEVSSimulatorInterface) simulator);

            try
            {
                InputStream stream = URLResource.getResourceAsStream("/lmrs/shortMerge.xml");
                XmlNetworkLaneParser nlp = new XmlNetworkLaneParser((OTSDEVSSimulatorInterface) simulator);
                this.network = new OTSNetwork("ShortMerge");
                nlp.build(stream, this.network);

                addGenerator();

            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }

        /** {@inheritDoc} */
        @Override
        public SimulatorInterface<Time, Duration, OTSSimTimeDouble> getSimulator() throws RemoteException
        {
            return ShortMerge.this.getSimulator();
        }

        /** {@inheritDoc} */
        @Override
        public OTSNetwork getNetwork()
        {
            return this.network;
        }

        /**
         * Create generators.
         * @throws ParameterException on parameter exception
         * @throws GTUException on GTU exception
         * @throws NetworkException if not does not exist
         * @throws ProbabilityException negative probability
         * @throws SimRuntimeException in case of sim run time exception
         * @throws RemoteException if no simulator
         */
        private void addGenerator() throws ParameterException, GTUException, NetworkException, ProbabilityException,
                SimRuntimeException, RemoteException
        {

            Random seedGenerator = new Random(1L);
            Map<String, StreamInterface> streams = new HashMap<>();
            StreamInterface stream = new MersenneTwister(Math.abs(seedGenerator.nextLong()) + 1);
            streams.put("headwayGeneration", stream);
            streams.put("gtuClass", new MersenneTwister(Math.abs(seedGenerator.nextLong()) + 1));
            this.getSimulator().getReplication().setStreams(streams);

            TTCRoomChecker roomChecker = new TTCRoomChecker(new Duration(10.0, TimeUnit.SI));
            IdGenerator idGenerator = new IdGenerator("");

            CarFollowingModelFactory<IDMPlus> idmPlusFactory = new IDMPlusFactory();
            BehavioralCharacteristics bc = new BehavioralCharacteristics();
            bc.setDefaultParameter(AbstractIDM.DELTA);

            LaneBasedTacticalPlannerFactory<LMRS> tacticalFactory =
                    new LMRSFactory(idmPlusFactory, bc, new DefaultLMRSPerceptionFactory());

            GTUType car = new GTUType("car", CAR);
            GTUType truck = new GTUType("truck", TRUCK);
            Route routeAE = this.network.getShortestRouteBetween(car, this.network.getNode("A"), this.network.getNode("E"));
            Route routeAG = this.network.getShortestRouteBetween(car, this.network.getNode("A"), this.network.getNode("G"));
            Route routeFE = this.network.getShortestRouteBetween(car, this.network.getNode("F"), this.network.getNode("E"));
            Route routeFG = this.network.getShortestRouteBetween(car, this.network.getNode("F"), this.network.getNode("G"));

            List<FrequencyAndObject<Route>> routesA = new ArrayList<>();
            routesA.add(new FrequencyAndObject<>(0.2, routeAE));
            routesA.add(new FrequencyAndObject<>(0.8, routeAG));
            List<FrequencyAndObject<Route>> routesF = new ArrayList<>();
            routesF.add(new FrequencyAndObject<>(0.2, routeFE));
            routesF.add(new FrequencyAndObject<>(0.8, routeFG));
            RouteGenerator routeGeneratorA = new ProbabilisticRouteGenerator(routesA, stream);
            RouteGenerator routeGeneratorF = new ProbabilisticRouteGenerator(routesF, stream);

            Speed speedA = new Speed(120.0, SpeedUnit.KM_PER_HOUR);
            Speed speedF = new Speed(20.0, SpeedUnit.KM_PER_HOUR);

            CrossSectionLink linkA = (CrossSectionLink) this.network.getLink("AB");
            CrossSectionLink linkF = (CrossSectionLink) this.network.getLink("FB");

            BehavioralCharacteristicsFactoryByType bcFactory = new BehavioralCharacteristicsFactoryByType();
            bcFactory.addGaussianParameter(car, ParameterTypes.FSPEED, 123.7 / 120, 12.0 / 120, stream);
            bcFactory.addGaussianParameter(car, LmrsParameters.HIERARCHY, 0.5, 0.1, stream);
            bcFactory.addParameter(truck, ParameterTypes.A, new Acceleration(0.8, AccelerationUnit.SI));
            bcFactory.addGaussianParameter(truck, LmrsParameters.HIERARCHY, 0.5, 0.1, stream);

            Generator<Duration> headwaysA1 = new HeadwayGenerator(new Frequency(400, FrequencyUnit.PER_HOUR));
            Generator<Duration> headwaysA2 = new HeadwayGenerator(new Frequency(400, FrequencyUnit.PER_HOUR));
            Generator<Duration> headwaysA3 = new HeadwayGenerator(new Frequency(400, FrequencyUnit.PER_HOUR));
            Generator<Duration> headwaysF = new HeadwayGenerator(new Frequency(600, FrequencyUnit.PER_HOUR));

            SpeedGenerator speedCar = new SpeedGenerator(new Speed(160.0, SpeedUnit.KM_PER_HOUR),
                    new Speed(200.0, SpeedUnit.KM_PER_HOUR), stream);
            SpeedGenerator speedTruck =
                    new SpeedGenerator(new Speed(80.0, SpeedUnit.KM_PER_HOUR), new Speed(95.0, SpeedUnit.KM_PER_HOUR), stream);
            GTUTypeGenerator gtuTypeAllCar = new GTUTypeGenerator(ShortMerge.this.getSimulator());
            GTUTypeGenerator gtuType1Lane = new GTUTypeGenerator(ShortMerge.this.getSimulator());
            GTUTypeGenerator gtuType3rdLane = new GTUTypeGenerator(ShortMerge.this.getSimulator());
            gtuTypeAllCar.addType(new Length(4.0, LengthUnit.SI), new Length(2.0, LengthUnit.SI), new GTUType("car", CAR),
                    speedCar, 1.0);
            gtuType1Lane.addType(new Length(4.0, LengthUnit.SI), new Length(2.0, LengthUnit.SI), new GTUType("car", CAR),
                    speedCar, 0.9);
            gtuType1Lane.addType(new Length(15.0, LengthUnit.SI), new Length(2.5, LengthUnit.SI), new GTUType("truck", TRUCK),
                    speedTruck, 0.1);
            gtuType3rdLane.addType(new Length(4.0, LengthUnit.SI), new Length(2.0, LengthUnit.SI), new GTUType("car", CAR),
                    speedCar, 0.7);
            gtuType3rdLane.addType(new Length(15.0, LengthUnit.SI), new Length(2.5, LengthUnit.SI), new GTUType("truck", TRUCK),
                    speedTruck, 0.3);

            makeGenerator(getLane(linkA, "FORWARD1"), speedA, "gen1", routeGeneratorA, idGenerator, gtuTypeAllCar, headwaysA1,
                    this.colorer, roomChecker, bcFactory, tacticalFactory, SIMTIME);
            makeGenerator(getLane(linkA, "FORWARD2"), speedA, "gen2", routeGeneratorA, idGenerator, gtuTypeAllCar, headwaysA2,
                    this.colorer, roomChecker, bcFactory, tacticalFactory, SIMTIME);
            makeGenerator(getLane(linkA, "FORWARD3"), speedA, "gen3", routeGeneratorA, idGenerator, gtuType3rdLane, headwaysA3,
                    this.colorer, roomChecker, bcFactory, tacticalFactory, SIMTIME);
            makeGenerator(getLane(linkF, "FORWARD1"), speedF, "gen4", routeGeneratorF, idGenerator, gtuType1Lane, headwaysF,
                    this.colorer, roomChecker, bcFactory, tacticalFactory, SIMTIME);

        }

        /**
         * Get lane from link by id.
         * @param link link
         * @param id id
         * @return lane
         */
        private Lane getLane(final CrossSectionLink link, final String id)
        {
            for (Lane lane : link.getLanes())
            {
                if (lane.getId().equals(id))
                {
                    return lane;
                }
            }
            throw new RuntimeException("Could not find lane " + id + " on link " + link.getId());
        }

        /**
         * @param lane the reference lane for this generator
         * @param generationSpeed the speed of the GTU
         * @param id the id of the generator itself
         * @param routeGenerator the generator for the route
         * @param idGenerator the generator for the ID
         * @param gtuTypeGenerator the type generator for the GTU
         * @param headwayGenerator the headway generator for the GTU
         * @param gtuColorer the GTU colorer for animation
         * @param roomChecker the checker to see if there is room for the GTU
         * @param bcFactory the factory to generate behavioral characteristics for the GTU
         * @param tacticalFactory the generator for the tactical planner
         * @param simulationTime simulation time
         * @throws SimRuntimeException in case of scheduling problems
         * @throws ProbabilityException in case of an illegal probability distribution
         * @throws GTUException in case the GTU is inconsistent
         * @throws ParameterException in case a parameter for the perception is missing
         */
        private void makeGenerator(final Lane lane, final Speed generationSpeed, final String id,
                final RouteGenerator routeGenerator, final IdGenerator idGenerator, final GTUTypeGenerator gtuTypeGenerator,
                final Generator<Duration> headwayGenerator, final GTUColorer gtuColorer, final RoomChecker roomChecker,
                final BehavioralCharacteristicsFactory bcFactory, final LaneBasedTacticalPlannerFactory<?> tacticalFactory,
                final Time simulationTime) throws SimRuntimeException, ProbabilityException, GTUException, ParameterException
        {
            Set<DirectedLanePosition> initialLongitudinalPositions = new HashSet<>();
            // TODO DIR_MINUS
            initialLongitudinalPositions
                    .add(new DirectedLanePosition(lane, new Length(10.0, LengthUnit.SI), GTUDirectionality.DIR_PLUS));

            LaneBasedStrategicalRoutePlannerFactory strategicalFactory =
                    new LaneBasedStrategicalRoutePlannerFactory(tacticalFactory, bcFactory);

            CharacteristicsGenerator characteristicsGenerator = new CharacteristicsGenerator(strategicalFactory, routeGenerator,
                    idGenerator, ShortMerge.this.getSimulator(), this.network, gtuTypeGenerator, generationSpeed,
                    initialLongitudinalPositions);

            new LaneBasedGTUGenerator(id, headwayGenerator, Long.MAX_VALUE, Time.ZERO, simulationTime, gtuColorer,
                    characteristicsGenerator, initialLongitudinalPositions, this.network, roomChecker);
        }

    }

    /**
     * <p>
     * Copyright (c) 2013-2016 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved.
     * <br>
     * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
     * <p>
     * @version $Revision$, $LastChangedDate$, by $Author$, initial version 29 jan. 2017 <br>
     * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
     * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
     */
    private class HeadwayGenerator implements Generator<Duration>
    {

        /** Demand level. */
        private final Frequency demand;

        /**
         * @param demand demand
         */
        HeadwayGenerator(final Frequency demand)
        {
            this.demand = demand;
        }

        /** {@inheritDoc} */
        @Override
        public Duration draw() throws ProbabilityException, ParameterException
        {
            return new Duration(
                    -Math.log(ShortMerge.this.getSimulator().getReplication().getStream("headwayGeneration").nextDouble())
                            / this.demand.si,
                    TimeUnit.SI);
        }

    }

}
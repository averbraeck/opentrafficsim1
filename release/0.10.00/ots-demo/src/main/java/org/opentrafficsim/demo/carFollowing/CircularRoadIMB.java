package org.opentrafficsim.demo.carFollowing;

import java.awt.Container;
import java.awt.Frame;
import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.naming.NamingException;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.djunits.unit.TimeUnit;
import org.djunits.unit.UNITS;
import org.djunits.value.vdouble.scalar.Acceleration;
import org.djunits.value.vdouble.scalar.DoubleScalar;
import org.djunits.value.vdouble.scalar.DoubleScalar.Abs;
import org.djunits.value.vdouble.scalar.DoubleScalar.Rel;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Speed;
import org.djunits.value.vdouble.scalar.Time;
import org.opentrafficsim.core.dsol.OTSDEVSSimulatorInterface;
import org.opentrafficsim.core.dsol.OTSModelInterface;
import org.opentrafficsim.core.dsol.OTSSimTimeDouble;
import org.opentrafficsim.core.geometry.OTSGeometryException;
import org.opentrafficsim.core.geometry.OTSPoint3D;
import org.opentrafficsim.core.gtu.GTU;
import org.opentrafficsim.core.gtu.GTUDirectionality;
import org.opentrafficsim.core.gtu.GTUException;
import org.opentrafficsim.core.gtu.GTUType;
import org.opentrafficsim.core.gtu.animation.GTUColorer;
import org.opentrafficsim.core.gtu.behavioralcharacteristics.BehavioralCharacteristics;
import org.opentrafficsim.core.network.LongitudinalDirectionality;
import org.opentrafficsim.core.network.NetworkException;
import org.opentrafficsim.core.network.OTSNetwork;
import org.opentrafficsim.core.network.OTSNode;
import org.opentrafficsim.graphs.AccelerationContourPlot;
import org.opentrafficsim.graphs.ContourPlot;
import org.opentrafficsim.graphs.DensityContourPlot;
import org.opentrafficsim.graphs.FlowContourPlot;
import org.opentrafficsim.graphs.LaneBasedGTUSampler;
import org.opentrafficsim.graphs.SpeedContourPlot;
import org.opentrafficsim.graphs.TrajectoryPlot;
import org.opentrafficsim.imb.observers.IMBTransmitter;
import org.opentrafficsim.road.gtu.animation.DefaultCarAnimation;
import org.opentrafficsim.road.gtu.lane.LaneBasedIndividualGTU;
import org.opentrafficsim.road.gtu.lane.tactical.LaneBasedCFLCTacticalPlannerFactory;
import org.opentrafficsim.road.gtu.lane.tactical.LaneBasedGTUFollowingChange0TacticalPlannerFactory;
import org.opentrafficsim.road.gtu.lane.tactical.LaneBasedGTUFollowingLaneChangeTacticalPlannerFactory;
import org.opentrafficsim.road.gtu.lane.tactical.following.AbstractIDM;
import org.opentrafficsim.road.gtu.lane.tactical.following.GTUFollowingModelOld;
import org.opentrafficsim.road.gtu.lane.tactical.following.IDMOld;
import org.opentrafficsim.road.gtu.lane.tactical.following.IDMPlus;
import org.opentrafficsim.road.gtu.lane.tactical.following.IDMPlusOld;
import org.opentrafficsim.road.gtu.lane.tactical.lanechangemobil.AbstractLaneChangeModel;
import org.opentrafficsim.road.gtu.lane.tactical.lanechangemobil.Altruistic;
import org.opentrafficsim.road.gtu.lane.tactical.lanechangemobil.Egoistic;
import org.opentrafficsim.road.gtu.lane.tactical.lmrs.LMRSFactory;
import org.opentrafficsim.road.gtu.lane.tactical.toledo.ToledoFactory;
import org.opentrafficsim.road.gtu.strategical.LaneBasedStrategicalPlanner;
import org.opentrafficsim.road.gtu.strategical.LaneBasedStrategicalPlannerFactory;
import org.opentrafficsim.road.gtu.strategical.route.LaneBasedStrategicalRoutePlannerFactory;
import org.opentrafficsim.road.network.factory.LaneFactory;
import org.opentrafficsim.road.network.lane.DirectedLanePosition;
import org.opentrafficsim.road.network.lane.Lane;
import org.opentrafficsim.road.network.lane.LaneType;
import org.opentrafficsim.simulationengine.AbstractWrappableAnimation;
import org.opentrafficsim.simulationengine.OTSSimulationException;
import org.opentrafficsim.simulationengine.properties.AbstractProperty;
import org.opentrafficsim.simulationengine.properties.BooleanProperty;
import org.opentrafficsim.simulationengine.properties.CompoundProperty;
import org.opentrafficsim.simulationengine.properties.ContinuousProperty;
import org.opentrafficsim.simulationengine.properties.IDMPropertySet;
import org.opentrafficsim.simulationengine.properties.IntegerProperty;
import org.opentrafficsim.simulationengine.properties.ProbabilityDistributionProperty;
import org.opentrafficsim.simulationengine.properties.PropertyException;
import org.opentrafficsim.simulationengine.properties.SelectionProperty;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.gui.swing.TablePanel;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventProducer;

/**
 * Circular road simulation demo.
 * <p>
 * Copyright (c) 2013-2016 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate: 2016-08-24 13:50:36 +0200 (Wed, 24 Aug 2016) $, @version $Revision: 2144 $, by $Author: pknoppers $,
 * initial version 21 nov. 2014 <br>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class CircularRoadIMB extends AbstractWrappableAnimation implements UNITS
{
    /** */
    private static final long serialVersionUID = 1L;

    /** The model. */
    private RoadSimulationModelIMB model;

    /**
     * Create a CircularRoad simulation.
     * @throws PropertyException
     */
    public CircularRoadIMB() throws PropertyException
    {
        this.properties.add(new SelectionProperty("LaneChanging", "Lane changing",
                "<html>The lane change strategies vary in politeness.<br>"
                        + "Two types are implemented:<ul><li>Egoistic (looks only at personal gain).</li>"
                        + "<li>Altruistic (assigns effect on new and current follower the same weight as "
                        + "the personal gain).</html>",
                new String[] { "Egoistic", "Altruistic" }, 0, false, 500));
        this.properties.add(new SelectionProperty("TacticalPlanner", "Tactical planner",
                "<html>The tactical planner determines if a lane change is desired and possible.</html>",
                new String[] { "MOBIL", "Verbraeck", "Verbraeck0", "LMRS", "Toledo" }, 0, false, 600));
        this.properties.add(new IntegerProperty("TrackLength", "Track length", "Circumference of the track", 2000, 500, 6000,
                "Track length %dm", false, 10));
        this.properties.add(new ContinuousProperty("MeanDensity", "Mean density", "Number of vehicles per km", 40.0, 5.0, 45.0,
                "Density %.1f veh/km", false, 11));
        this.properties.add(new ContinuousProperty("DensityVariability", "Density variability",
                "Variability of the number of vehicles per km", 0.0, 0.0, 1.0, "%.1f", false, 12));
        ArrayList<AbstractProperty<?>> outputProperties = new ArrayList<AbstractProperty<?>>();
        for (int lane = 1; lane <= 2; lane++)
        {
            String laneId = String.format("Lane %d ", lane);
            outputProperties.add(new BooleanProperty(laneId + "Density", laneId + " Density", laneId + "Density contour plot",
                    true, false, 0));
            outputProperties
                    .add(new BooleanProperty(laneId + "Flow", laneId + " Flow", laneId + "Flow contour plot", true, false, 1));
            outputProperties.add(
                    new BooleanProperty(laneId + "Speed", laneId + " Speed", laneId + "Speed contour plot", true, false, 2));
            outputProperties.add(new BooleanProperty(laneId + "Acceleration", laneId + " Acceleration",
                    laneId + "Acceleration contour plot", true, false, 3));
            outputProperties.add(new BooleanProperty(laneId + "Trajectories", laneId + " Trajectories",
                    laneId + "Trajectory (time/distance) diagram", true, false, 4));
        }
        this.properties.add(new CompoundProperty("OutputGraphs", "Output graphs", "Select the graphical output",
                outputProperties, true, 1000));
    }

    /** {@inheritDoc} */
    @Override
    public final void stopTimersThreads()
    {
        super.stopTimersThreads();
        this.model = null;
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
                    CircularRoadIMB circularRoad = new CircularRoadIMB();
                    ArrayList<AbstractProperty<?>> propertyList = circularRoad.getProperties();
                    try
                    {
                        propertyList.add(new ProbabilityDistributionProperty("TrafficComposition", "Traffic composition",
                                "<html>Mix of passenger cars and trucks</html>", new String[] { "passenger car", "truck" },
                                new Double[] { 0.8, 0.2 }, false, 10));
                    }
                    catch (PropertyException exception)
                    {
                        exception.printStackTrace();
                    }
                    propertyList.add(new SelectionProperty("CarFollowingModel", "Car following model",
                            "<html>The car following model determines "
                                    + "the acceleration that a vehicle will make taking into account "
                                    + "nearby vehicles, infrastructural restrictions (e.g. speed limit, "
                                    + "curvature of the road) capabilities of the vehicle and personality "
                                    + "of the driver.</html>",
                            new String[] { "IDM", "IDM+" }, 1, false, 1));
                    propertyList.add(IDMPropertySet.makeIDMPropertySet("IDMCar", "Car",
                            new Acceleration(1.0, METER_PER_SECOND_2), new Acceleration(1.5, METER_PER_SECOND_2),
                            new Length(2.0, METER), new Duration(1.0, SECOND), 2));
                    propertyList.add(IDMPropertySet.makeIDMPropertySet("IDMTruck", "Truck",
                            new Acceleration(0.5, METER_PER_SECOND_2), new Acceleration(1.25, METER_PER_SECOND_2),
                            new Length(2.0, METER), new Duration(1.0, SECOND), 3));

                    circularRoad.buildAnimator(new Time(0.0, SECOND), new Duration(0.0, SECOND), new Duration(3600.0, SECOND),
                            propertyList, null, true);
                }
                catch (SimRuntimeException | NamingException | OTSSimulationException | PropertyException exception)
                {
                    exception.printStackTrace();
                }
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    protected final OTSModelInterface makeModel(final GTUColorer colorer)
    {
        this.model = new RoadSimulationModelIMB(getSavedUserModifiedProperties(), colorer);
        return this.model;
    }

    /**
     * @return the saved user properties for a next run
     */
    private ArrayList<AbstractProperty<?>> getSavedUserModifiedProperties()
    {
        return this.savedUserModifiedProperties;
    }

    /** {@inheritDoc} */
    @Override
    protected final Rectangle2D.Double makeAnimationRectangle()
    {
        return new Rectangle2D.Double(-350, -350, 700, 700);
    }

    /** {@inheritDoc} */
    @Override
    protected final JPanel makeCharts() throws OTSSimulationException, PropertyException
    {
        // Make the tab with the plots
        AbstractProperty<?> output = new CompoundProperty("", "", "", this.properties, false, 0).findByKey("OutputGraphs");
        if (null == output)
        {
            throw new Error("Cannot find output properties");
        }
        ArrayList<BooleanProperty> graphs = new ArrayList<BooleanProperty>();
        if (output instanceof CompoundProperty)
        {
            CompoundProperty outputProperties = (CompoundProperty) output;
            for (AbstractProperty<?> ap : outputProperties.getValue())
            {
                if (ap instanceof BooleanProperty)
                {
                    BooleanProperty bp = (BooleanProperty) ap;
                    if (bp.getValue())
                    {
                        graphs.add(bp);
                    }
                }
            }
        }
        else
        {
            throw new Error("output properties should be compound");
        }

        int graphCount = graphs.size();
        int columns = (int) Math.ceil(Math.sqrt(graphCount));
        int rows = 0 == columns ? 0 : (int) Math.ceil(graphCount * 1.0 / columns);
        TablePanel charts = new TablePanel(columns, rows);

        for (int i = 0; i < graphCount; i++)
        {
            String graphName = graphs.get(i).getKey();
            Container container = null;
            LaneBasedGTUSampler graph;
            int pos = graphName.indexOf(' ') + 1;
            String laneNumberText = graphName.substring(pos, pos + 1);
            int lane = Integer.parseInt(laneNumberText) - 1;

            if (graphName.contains("Trajectories"))
            {
                TrajectoryPlot tp = new TrajectoryPlot(graphName, new Duration(0.5, SECOND), this.model.getPath(lane));
                tp.setTitle("Trajectory Graph");
                tp.setExtendedState(Frame.MAXIMIZED_BOTH);
                graph = tp;
                container = tp.getContentPane();
            }
            else
            {
                ContourPlot cp;
                if (graphName.contains("Density"))
                {
                    cp = new DensityContourPlot(graphName, this.model.getPath(lane));
                    cp.setTitle("Density Contour Graph");
                }
                else if (graphName.contains("Speed"))
                {
                    cp = new SpeedContourPlot(graphName, this.model.getPath(lane));
                    cp.setTitle("Speed Contour Graph");
                }
                else if (graphName.contains("Flow"))
                {
                    cp = new FlowContourPlot(graphName, this.model.getPath(lane));
                    cp.setTitle("Flow Contour Graph");
                }
                else if (graphName.contains("Acceleration"))
                {
                    cp = new AccelerationContourPlot(graphName, this.model.getPath(lane));
                    cp.setTitle("Acceleration Contour Graph");
                }
                else
                {
                    throw new Error("Unhandled type of contourplot: " + graphName);
                }
                graph = cp;
                container = cp.getContentPane();
            }
            // Add the container to the matrix
            charts.setCell(container, i % columns, i / columns);
            this.model.getPlots().add(graph);
        }

        return charts;
    }

    /** {@inheritDoc} */
    @Override
    public final String shortName()
    {
        return "Circular Road simulation";
    }

    /** {@inheritDoc} */
    @Override
    public final String description()
    {
        return "<html><h1>Circular Road simulation</h1>" + "Vehicles are unequally distributed over a two lane ring road.<br>"
                + "When simulation starts, all vehicles begin driving, some lane changes will occurr and some "
                + "shockwaves should develop.<br>"
                + "Trajectories and contourplots are generated during the simulation for both lanes.</html>";
    }

}

/**
 * Simulate traffic on a circular, two-lane road.
 * <p>
 * Copyright (c) 2013-2016 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate: 2016-08-24 13:50:36 +0200 (Wed, 24 Aug 2016) $, @version $Revision: 2144 $, by $Author: pknoppers $,
 * initial version 1 nov. 2014 <br>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
class RoadSimulationModelIMB implements OTSModelInterface, UNITS
{
    /** */
    private static final long serialVersionUID = 20141121L;

    /** The simulator. */
    private OTSDEVSSimulatorInterface simulator;

    /** The network. */
    private OTSNetwork network = new OTSNetwork("network");

    /** Number of cars created. */
    private int carsCreated = 0;

    /** The car following model, e.g. IDM Plus for cars. */
    private GTUFollowingModelOld carFollowingModelCars;

    /** The car following model, e.g. IDM Plus for trucks. */
    private GTUFollowingModelOld carFollowingModelTrucks;

    /** The probability that the next generated GTU is a passenger car. */
    private double carProbability;

    /** The lane change model. */
    private AbstractLaneChangeModel laneChangeModel;

    /** Minimum distance. */
    private Length minimumDistance = new Length(0, METER);

    /** The speed limit. */
    private Speed speedLimit = new Speed(100, KM_PER_HOUR);

    /** The plots. */
    private ArrayList<LaneBasedGTUSampler> plots = new ArrayList<LaneBasedGTUSampler>();

    /** User settable properties. */
    private ArrayList<AbstractProperty<?>> properties = null;

    /** The sequence of Lanes that all vehicles will follow. */
    private ArrayList<List<Lane>> paths = new ArrayList<List<Lane>>();

    /** The random number generator used to decide what kind of GTU to generate. */
    private Random randomGenerator = new Random(12345);

    /** The GTUColorer for the generated vehicles. */
    private final GTUColorer gtuColorer;

    /** Strategical planner generator for cars. */
    private LaneBasedStrategicalPlannerFactory<LaneBasedStrategicalPlanner> strategicalPlannerGeneratorCars = null;

    /** Strategical planner generator for cars. */
    private LaneBasedStrategicalPlannerFactory<LaneBasedStrategicalPlanner> strategicalPlannerGeneratorTrucks = null;

    /** */
    private final CarListener carListener = new CarListener();

    /** */
    private final IMBTransmitter imbTransmitter = new IMBTransmitter();

    /**
     * @param properties ArrayList&lt;AbstractProperty&lt;?&gt;&gt;; the properties
     * @param gtuColorer the default and initial GTUColorer, e.g. a DefaultSwitchableTUColorer.
     */
    public RoadSimulationModelIMB(final ArrayList<AbstractProperty<?>> properties, final GTUColorer gtuColorer)
    {
        this.properties = properties;
        this.gtuColorer = gtuColorer;
    }

    /**
     * @param index int; the rank number of the path
     * @return List&lt;Lane&gt;; the set of lanes for the specified index
     */
    public List<Lane> getPath(final int index)
    {
        return this.paths.get(index);
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface<Abs<TimeUnit>, Rel<TimeUnit>, OTSSimTimeDouble> theSimulator)
            throws SimRuntimeException, RemoteException
    {
        final int laneCount = 2;
        for (int laneIndex = 0; laneIndex < laneCount; laneIndex++)
        {
            this.paths.add(new ArrayList<Lane>());
        }
        this.simulator = (OTSDEVSSimulatorInterface) theSimulator;
        double radius = 6000 / 2 / Math.PI;
        double headway = 40;
        double headwayVariability = 0;
        try
        {

            // Get car-following model name
            String carFollowingModelName = null;
            CompoundProperty propertyContainer = new CompoundProperty("", "", "", this.properties, false, 0);
            AbstractProperty<?> cfmp = propertyContainer.findByKey("CarFollowingModel");
            if (null == cfmp)
            {
                throw new Error("Cannot find \"Car following model\" property");
            }
            if (cfmp instanceof SelectionProperty)
            {
                carFollowingModelName = ((SelectionProperty) cfmp).getValue();
            }
            else
            {
                throw new Error("\"Car following model\" property has wrong type");
            }

            // Get car-following model parameter
            Iterator<AbstractProperty<List<AbstractProperty<?>>>> iterator =
                    new CompoundProperty("", "", "", this.properties, false, 0).iterator();
            while (iterator.hasNext())
            {
                AbstractProperty<?> ap = iterator.next();
                if (ap instanceof CompoundProperty)
                {
                    CompoundProperty cp = (CompoundProperty) ap;
                    System.out.println("Checking compound property " + cp);
                    if (ap.getKey().contains("IDM"))
                    {
                        System.out.println("Car following model name appears to be " + ap.getKey());
                        Acceleration a = IDMPropertySet.getA(cp);
                        Acceleration b = IDMPropertySet.getB(cp);
                        Length s0 = IDMPropertySet.getS0(cp);
                        Duration tSafe = IDMPropertySet.getTSafe(cp);
                        GTUFollowingModelOld gtuFollowingModel = null;
                        if (carFollowingModelName.equals("IDM"))
                        {
                            gtuFollowingModel = new IDMOld(a, b, s0, tSafe, 1.0);
                        }
                        else if (carFollowingModelName.equals("IDM+"))
                        {
                            gtuFollowingModel = new IDMPlusOld(a, b, s0, tSafe, 1.0);
                        }
                        else
                        {
                            throw new Error("Unknown gtu following model: " + carFollowingModelName);
                        }
                        if (ap.getKey().contains("Car"))
                        {
                            this.carFollowingModelCars = gtuFollowingModel;
                        }
                        else if (ap.getKey().contains("Truck"))
                        {
                            this.carFollowingModelTrucks = gtuFollowingModel;
                        }
                        else
                        {
                            throw new Error("Cannot determine gtu type for " + ap.getKey());
                        }
                    }
                }
            }

            // Get lane change model
            cfmp = propertyContainer.findByKey("LaneChanging");
            if (null == cfmp)
            {
                throw new Error("Cannot find \"Lane changing\" property");
            }
            if (cfmp instanceof SelectionProperty)
            {
                String laneChangeModelName = ((SelectionProperty) cfmp).getValue();
                if ("Egoistic".equals(laneChangeModelName))
                {
                    this.laneChangeModel = new Egoistic();
                }
                else if ("Altruistic".equals(laneChangeModelName))
                {
                    this.laneChangeModel = new Altruistic();
                }
                else
                {
                    throw new Error("Lane changing " + laneChangeModelName + " not implemented");
                }
            }
            else
            {
                throw new Error("\"Lane changing\" property has wrong type");
            }

            // Get remaining properties
            iterator = new CompoundProperty("", "", "", this.properties, false, 0).iterator();
            while (iterator.hasNext())
            {
                AbstractProperty<?> ap = iterator.next();
                if (ap instanceof SelectionProperty)
                {
                    SelectionProperty sp = (SelectionProperty) ap;
                    if ("TacticalPlanner".equals(sp.getKey()))
                    {
                        String tacticalPlannerName = sp.getValue();
                        if ("MOBIL".equals(tacticalPlannerName))
                        {
                            this.strategicalPlannerGeneratorCars = new LaneBasedStrategicalRoutePlannerFactory(
                                    new LaneBasedCFLCTacticalPlannerFactory(this.carFollowingModelCars, this.laneChangeModel));
                            this.strategicalPlannerGeneratorTrucks =
                                    new LaneBasedStrategicalRoutePlannerFactory(new LaneBasedCFLCTacticalPlannerFactory(
                                            this.carFollowingModelTrucks, this.laneChangeModel));
                        }
                        else if ("Verbraeck".equals(tacticalPlannerName))
                        {
                            this.strategicalPlannerGeneratorCars = new LaneBasedStrategicalRoutePlannerFactory(
                                    new LaneBasedGTUFollowingLaneChangeTacticalPlannerFactory(this.carFollowingModelCars));
                            this.strategicalPlannerGeneratorTrucks = new LaneBasedStrategicalRoutePlannerFactory(
                                    new LaneBasedGTUFollowingLaneChangeTacticalPlannerFactory(this.carFollowingModelTrucks));
                        }
                        else if ("Verbraeck0".equals(tacticalPlannerName))
                        {
                            this.strategicalPlannerGeneratorCars = new LaneBasedStrategicalRoutePlannerFactory(
                                    new LaneBasedGTUFollowingChange0TacticalPlannerFactory(this.carFollowingModelCars));
                            this.strategicalPlannerGeneratorTrucks = new LaneBasedStrategicalRoutePlannerFactory(
                                    new LaneBasedGTUFollowingChange0TacticalPlannerFactory(this.carFollowingModelTrucks));
                        }
                        else if ("LMRS".equals(tacticalPlannerName))
                        {
                            // provide default parameters with the car-following model
                            BehavioralCharacteristics defaultBehavioralCFCharacteristics = new BehavioralCharacteristics();
                            defaultBehavioralCFCharacteristics.setDefaultParameters(AbstractIDM.class);
                            this.strategicalPlannerGeneratorCars = new LaneBasedStrategicalRoutePlannerFactory(
                                    new LMRSFactory(IDMPlus.class, defaultBehavioralCFCharacteristics));
                            this.strategicalPlannerGeneratorTrucks = new LaneBasedStrategicalRoutePlannerFactory(
                                    new LMRSFactory(IDMPlus.class, defaultBehavioralCFCharacteristics));
                        }
                        else if ("Toledo".equals(tacticalPlannerName))
                        {
                            this.strategicalPlannerGeneratorCars =
                                    new LaneBasedStrategicalRoutePlannerFactory(new ToledoFactory());
                            this.strategicalPlannerGeneratorTrucks =
                                    new LaneBasedStrategicalRoutePlannerFactory(new ToledoFactory());
                        }
                        else
                        {
                            throw new Error("Don't know how to create a " + tacticalPlannerName + " tactical planner");
                        }
                    }
                }
                else if (ap instanceof ProbabilityDistributionProperty)
                {
                    ProbabilityDistributionProperty pdp = (ProbabilityDistributionProperty) ap;
                    if (ap.getKey().equals("TrafficComposition"))
                    {
                        this.carProbability = pdp.getValue()[0];
                    }
                }
                else if (ap instanceof IntegerProperty)
                {
                    IntegerProperty ip = (IntegerProperty) ap;
                    if ("TrackLength".equals(ip.getKey()))
                    {
                        radius = ip.getValue() / 2 / Math.PI;
                    }
                }
                else if (ap instanceof ContinuousProperty)
                {
                    ContinuousProperty cp = (ContinuousProperty) ap;
                    if (cp.getKey().equals("MeanDensity"))
                    {
                        headway = 1000 / cp.getValue();
                    }
                    if (cp.getKey().equals("DensityVariability"))
                    {
                        headwayVariability = cp.getValue();
                    }
                }
                else if (ap instanceof CompoundProperty)
                {
                    if (ap.getKey().equals("OutputGraphs"))
                    {
                        continue; // Output settings are handled elsewhere
                    }
                }
            }
            GTUType gtuType = new GTUType("car");
            Set<GTUType> compatibility = new HashSet<GTUType>();
            compatibility.add(gtuType);
            LaneType laneType = new LaneType("CarLane", compatibility);
            OTSNode start = new OTSNode("Start", new OTSPoint3D(radius, 0, 0));
            OTSNode halfway = new OTSNode("Halfway", new OTSPoint3D(-radius, 0, 0));

            OTSPoint3D[] coordsHalf1 = new OTSPoint3D[127];
            for (int i = 0; i < coordsHalf1.length; i++)
            {
                double angle = Math.PI * (1 + i) / (1 + coordsHalf1.length);
                coordsHalf1[i] = new OTSPoint3D(radius * Math.cos(angle), radius * Math.sin(angle), 0);
            }
            Lane[] lanes1 = LaneFactory.makeMultiLane("FirstHalf", start, halfway, coordsHalf1, laneCount, laneType,
                    this.speedLimit, this.simulator, LongitudinalDirectionality.DIR_PLUS);
            OTSPoint3D[] coordsHalf2 = new OTSPoint3D[127];
            for (int i = 0; i < coordsHalf2.length; i++)
            {
                double angle = Math.PI + Math.PI * (1 + i) / (1 + coordsHalf2.length);
                coordsHalf2[i] = new OTSPoint3D(radius * Math.cos(angle), radius * Math.sin(angle), 0);
            }
            Lane[] lanes2 = LaneFactory.makeMultiLane("SecondHalf", halfway, start, coordsHalf2, laneCount, laneType,
                    this.speedLimit, this.simulator, LongitudinalDirectionality.DIR_PLUS);
            for (int laneIndex = 0; laneIndex < laneCount; laneIndex++)
            {
                this.paths.get(laneIndex).add(lanes1[laneIndex]);
                this.paths.get(laneIndex).add(lanes2[laneIndex]);
            }
            // Put the (not very evenly spaced) cars on the track
            double variability = (headway - 20) * headwayVariability;
            System.out.println("headway is " + headway + " variability limit is " + variability);
            Random random = new Random(12345);
            for (int laneIndex = 0; laneIndex < laneCount; laneIndex++)
            {
                double lane1Length = lanes1[laneIndex].getLength().getSI();
                double trackLength = lane1Length + lanes2[laneIndex].getLength().getSI();
                for (double pos = 0; pos <= trackLength - headway - variability;)
                {
                    Lane lane = pos >= lane1Length ? lanes2[laneIndex] : lanes1[laneIndex];
                    // Actual headway is uniformly distributed around headway
                    double laneRelativePos = pos > lane1Length ? pos - lane1Length : pos;
                    double actualHeadway = headway + (random.nextDouble() * 2 - 1) * variability;
                    // System.out.println(lane + ", len=" + lane.getLength() + ", pos=" + laneRelativePos);
                    generateCar(new Length(laneRelativePos, METER), lane, gtuType);
                    pos += actualHeadway;
                }
            }
            // Schedule regular updates of the graph
            this.simulator.scheduleEventAbs(new DoubleScalar.Abs<TimeUnit>(9.999, SECOND), this, this, "drawGraphs", null);
        }
        catch (SimRuntimeException | NamingException | NetworkException | GTUException | OTSGeometryException
                | PropertyException exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * Notify the contour plots that the underlying data has changed.
     */
    protected final void drawGraphs()
    {
        for (LaneBasedGTUSampler plot : this.plots)
        {
            plot.reGraph();
        }
        // Re schedule this method
        try
        {
            this.simulator.scheduleEventAbs(new Time(this.simulator.getSimulatorTime().get().getSI() + 10, SECOND), this, this,
                    "drawGraphs", null);
        }
        catch (SimRuntimeException exception)
        {
            exception.printStackTrace();
        }

    }

    /**
     * Generate cars at a fixed rate (implemented by re-scheduling this method).
     * @param initialPosition Length; the initial position of the new cars
     * @param lane Lane; the lane on which the new cars are placed
     * @param gtuType GTUType&lt;String&gt;; the type of the new cars
     * @throws NamingException on ???
     * @throws SimRuntimeException cannot happen
     * @throws NetworkException on network inconsistency
     * @throws GTUException when something goes wrong during construction of the car
     * @throws OTSGeometryException when the initial position is outside the center line of the lane
     */
    protected final void generateCar(final Length initialPosition, final Lane lane, final GTUType gtuType)
            throws NamingException, NetworkException, SimRuntimeException, GTUException, OTSGeometryException
    {

        // GTU itself
        boolean generateTruck = this.randomGenerator.nextDouble() > this.carProbability;
        Length vehicleLength = new Length(generateTruck ? 15 : 4, METER);
        LaneBasedIndividualGTU gtu =
                new LaneBasedIndividualGTU("" + (++this.carsCreated), gtuType, vehicleLength, new Length(1.8, METER),
                        new Speed(200, KM_PER_HOUR), this.simulator, DefaultCarAnimation.class, this.gtuColorer, this.network);

        // add the car to a listener to test.
        gtu.addListener(this.carListener, GTU.INIT_EVENT, true);
        gtu.addListener(this.carListener, GTU.MOVE_EVENT, true);
        gtu.addListener(this.carListener, GTU.DESTROY_EVENT, true);

        // add the car to the IMB Transmitter.
        gtu.addListener(this.imbTransmitter, GTU.INIT_EVENT, true);
        gtu.addListener(this.imbTransmitter, GTU.MOVE_EVENT, true);
        gtu.addListener(this.imbTransmitter, GTU.DESTROY_EVENT, true);

        // strategical planner
        LaneBasedStrategicalPlanner strategicalPlanner;
        if (!generateTruck)
        {
            strategicalPlanner = this.strategicalPlannerGeneratorCars.create(gtu);
        }
        else
        {
            strategicalPlanner = this.strategicalPlannerGeneratorTrucks.create(gtu);
        }

        // init
        Set<DirectedLanePosition> initialPositions = new LinkedHashSet<>(1);
        initialPositions.add(new DirectedLanePosition(lane, initialPosition, GTUDirectionality.DIR_PLUS));
        Speed initialSpeed = new Speed(0, KM_PER_HOUR);
        gtu.init(strategicalPlanner, initialPositions, initialSpeed);
    }

    /** {@inheritDoc} */
    @Override
    public SimulatorInterface<Abs<TimeUnit>, Rel<TimeUnit>, OTSSimTimeDouble> getSimulator() throws RemoteException
    {
        return this.simulator;
    }

    /**
     * @return plots
     */
    public final ArrayList<LaneBasedGTUSampler> getPlots()
    {
        return this.plots;
    }

    /**
     * @return minimumDistance
     */
    public final Length getMinimumDistance()
    {
        return this.minimumDistance;
    }

    /**
     * Stop simulation and throw an Error.
     * @param theSimulator OTSDEVSSimulatorInterface; the simulator
     * @param errorMessage String; the error message
     */
    public void stopSimulator(final OTSDEVSSimulatorInterface theSimulator, final String errorMessage)
    {
        System.out.println("Error: " + errorMessage);
        try
        {
            if (theSimulator.isRunning())
            {
                theSimulator.stop();
            }
        }
        catch (SimRuntimeException exception)
        {
            exception.printStackTrace();
        }
        throw new Error(errorMessage);
    }

    /** */
    protected static class CarListener extends EventProducer implements EventListenerInterface
    {
        /** */
        private static final long serialVersionUID = 1L;

        /** */
        /** {@inheritDoc} */
        @Override
        public void notify(final EventInterface event) throws RemoteException
        {
            if (event.getType().equals(GTU.INIT_EVENT))
            {
                Object[] initInfo = (Object[]) event.getContent();
                System.out.println("GTU " + initInfo[0] + " created");
            }
            else if (event.getType().equals(GTU.MOVE_EVENT))
            {
                Object[] moveInfo = (Object[]) event.getContent();
                System.out.println(
                        "GTU " + moveInfo[0] + " moved to " + moveInfo[1] + ", v=" + moveInfo[2] + ", a=" + moveInfo[3]);
            }
            else if (event.getType().equals(GTU.DESTROY_EVENT))
            {
                Object[] destroyInfo = (Object[]) event.getContent();
                System.out.println(
                        "GTU " + destroyInfo[0] + " destroyed at position " + destroyInfo[1] + ", odometer=" + destroyInfo[2]);
            }
            else
            {
                System.err.println("Unrecognized event of type " + event.getType());
            }
        }
    }

}
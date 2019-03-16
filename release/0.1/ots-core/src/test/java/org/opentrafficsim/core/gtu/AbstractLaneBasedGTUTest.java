package org.opentrafficsim.core.gtu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

import org.junit.Test;
import org.opentrafficsim.core.car.LaneBasedIndividualCar;
import org.opentrafficsim.core.dsol.OTSDEVSSimulatorInterface;
import org.opentrafficsim.core.dsol.OTSModelInterface;
import org.opentrafficsim.core.dsol.OTSSimTimeDouble;
import org.opentrafficsim.core.gtu.following.FixedAccelerationModel;
import org.opentrafficsim.core.gtu.following.GTUFollowingModel;
import org.opentrafficsim.core.gtu.lane.changing.AbstractLaneChangeModel;
import org.opentrafficsim.core.gtu.lane.changing.Egoistic;
import org.opentrafficsim.core.network.NetworkException;
import org.opentrafficsim.core.network.factory.LaneFactory;
import org.opentrafficsim.core.network.geotools.NodeGeotools;
import org.opentrafficsim.core.network.lane.Lane;
import org.opentrafficsim.core.network.lane.LaneType;
import org.opentrafficsim.core.unit.AccelerationUnit;
import org.opentrafficsim.core.unit.LengthUnit;
import org.opentrafficsim.core.unit.SpeedUnit;
import org.opentrafficsim.core.unit.TimeUnit;
import org.opentrafficsim.core.value.vdouble.scalar.DoubleScalar;
import org.opentrafficsim.core.value.vdouble.scalar.DoubleScalar.Abs;
import org.opentrafficsim.core.value.vdouble.scalar.DoubleScalar.Rel;
import org.opentrafficsim.simulationengine.SimpleSimulator;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Test the various methods of an AbstractLaneBasedGTU.<br/>
 * As abstract classes cannot be directly
 * <p>
 * Copyright (c) 2013-2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights
 * reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version 14 jan. 2015 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class AbstractLaneBasedGTUTest
{

    /**
     * Test that the constructor puts the supplied values in the correct fields.
     * @throws SimRuntimeException
     * @throws RemoteException
     * @throws NamingException
     * @throws NetworkException
     * @throws GTUException
     */
    @Test
    public void abstractLaneBasedGTUTest() throws RemoteException, SimRuntimeException, NamingException,
            NetworkException, GTUException
    {
        // This initialization code should probably be moved to a helper method that will be used in several tests.
        // First we need a set of Lanes
        // To create Lanes we need Nodes and a LaneType
        NodeGeotools.STR nodeAFrom = new NodeGeotools.STR("AFrom", new Coordinate(0, 0, 0));
        NodeGeotools.STR nodeATo = new NodeGeotools.STR("ATo", new Coordinate(1000, 0, 0));
        LaneType<String> laneType = new LaneType<String>("CarLane");
        // And a simulator, but for that we first need something that implements OTSModelInterface
        OTSModelInterface model = new DummyModelForTemplateGTUTest();
        final SimpleSimulator simulator =
                new SimpleSimulator(new DoubleScalar.Abs<TimeUnit>(0.0, TimeUnit.SECOND),
                        new DoubleScalar.Rel<TimeUnit>(0.0, TimeUnit.SECOND), new DoubleScalar.Rel<TimeUnit>(3600.0,
                                TimeUnit.SECOND), model, new Rectangle2D.Double(-1000, -1000, 2000, 2000));

        Lane[] lanesGroupA =
                LaneFactory.makeMultiLane("A", nodeAFrom, nodeATo, null, 3, laneType,
                        (OTSDEVSSimulatorInterface) simulator.getSimulator());
        // A GTU can exist on several lanes at once; create another lane group to test that
        NodeGeotools.STR nodeBFrom = new NodeGeotools.STR("BFrom", new Coordinate(10, 0, 0));
        NodeGeotools.STR nodeBTo = new NodeGeotools.STR("BTo", new Coordinate(1000, 100, 0));
        Lane[] lanesGroupB =
                LaneFactory.makeMultiLane("B", nodeBFrom, nodeBTo, null, 3, laneType,
                        (OTSDEVSSimulatorInterface) simulator.getSimulator());
        Map<Lane, DoubleScalar.Rel<LengthUnit>> initialLongitudinalPositions =
                new HashMap<Lane, DoubleScalar.Rel<LengthUnit>>();

        DoubleScalar.Rel<LengthUnit> positionA = new DoubleScalar.Rel<LengthUnit>(100, LengthUnit.METER);
        initialLongitudinalPositions.put(lanesGroupA[1], positionA);
        DoubleScalar.Rel<LengthUnit> positionB = new DoubleScalar.Rel<LengthUnit>(90, LengthUnit.METER);
        initialLongitudinalPositions.put(lanesGroupB[1], positionB);
        // A Car needs a CarFollowingModel
        DoubleScalar.Abs<AccelerationUnit> acceleration =
                new DoubleScalar.Abs<AccelerationUnit>(2, AccelerationUnit.METER_PER_SECOND_2);
        DoubleScalar.Rel<TimeUnit> validFor = new DoubleScalar.Rel<TimeUnit>(10, TimeUnit.SECOND);
        GTUFollowingModel gfm = new FixedAccelerationModel(acceleration, validFor);
        // A Car needs a lane change model
        AbstractLaneChangeModel laneChangeModel = new Egoistic();
        // A Car needs a type
        GTUType<String> gtuType = new GTUType<String>("Car");
        // A Car needs an initial speed
        DoubleScalar.Abs<SpeedUnit> initialSpeed = new DoubleScalar.Abs<SpeedUnit>(50, SpeedUnit.KM_PER_HOUR);
        // Length of the Car
        DoubleScalar.Rel<LengthUnit> carLength = new DoubleScalar.Rel<LengthUnit>(4, LengthUnit.METER);
        // Width of the Car
        DoubleScalar.Rel<LengthUnit> carWidth = new DoubleScalar.Rel<LengthUnit>(1.8, LengthUnit.METER);
        // Maximum velocity of the Car
        DoubleScalar.Abs<SpeedUnit> maximumVelocity = new DoubleScalar.Abs<SpeedUnit>(200, SpeedUnit.KM_PER_HOUR);
        // ID of the Car
        String carID = "theCar";
        // Now we can make a GTU
        LaneBasedIndividualCar<String> car =
                new LaneBasedIndividualCar<String>(carID, gtuType, gfm, laneChangeModel, initialLongitudinalPositions,
                        initialSpeed, carLength, carWidth, maximumVelocity,
                        (OTSDEVSSimulatorInterface) simulator.getSimulator());
        // Now we can verify the various fields in the newly created Car
        assertEquals("ID of the car should be identical to the provided one", carID, car.getId());
        assertEquals("GTU following model should be identical to the provided one", gfm, car.getGTUFollowingModel());
        assertEquals("GTU type should be identical to the provided one", gtuType, car.getGTUType());
        assertEquals("front in lanesGroupA[1] is positionA", positionA.getSI(),
                car.position(lanesGroupA[1], car.getReference()).getSI(), 0.0001);
        assertEquals("front in lanesGroupB[1] is positionB", positionB.getSI(),
                car.position(lanesGroupB[1], car.getReference()).getSI(), 0.0001);
        assertEquals("acceleration is 0", 0, car.getAcceleration().getSI(), 0.00001);
        assertEquals("longitudinal velocity is " + initialSpeed, initialSpeed.getSI(), car.getLongitudinalVelocity()
                .getSI(), 0.00001);
        assertEquals("lastEvaluation time is 0", 0, car.getLastEvaluationTime().getSI(), 0.00001);
        // Test the position(Lane, RelativePosition) method
        try
        {
            car.position(null, car.getFront());
            fail("position on null lane should have thrown a NetworkException");
        }
        catch (NetworkException ne)
        {
            // Ignore
        }
        for (Lane[] laneGroup : new Lane[][]{lanesGroupA, lanesGroupB})
        {
            for (int laneIndex = 0; laneIndex < laneGroup.length; laneIndex++)
            {
                Lane lane = laneGroup[laneIndex];
                boolean expectException = 1 != laneIndex;
                for (RelativePosition relativePosition : new RelativePosition[]{car.getFront(), car.getRear()})
                {
                    // System.out.println("lane:" + lane + ", expectedException: " + expectException
                    // + ", relativePostion: " + relativePosition);
                    try
                    {
                        DoubleScalar.Rel<LengthUnit> position = car.position(lane, relativePosition);
                        if (expectException)
                        {
                            // System.out.println("position: " + position);
                            fail("Calling position on lane that the car is NOT on should have thrown a NetworkException");
                        }
                        else
                        {
                            DoubleScalar.Rel<LengthUnit> expectedPosition =
                                    laneGroup == lanesGroupA ? positionA : positionB;
                            // FIXME There should be a better way to check equality of RelativePosition
                            if (relativePosition.getDx().getSI() != 0)
                            {
                                expectedPosition = DoubleScalar.plus(expectedPosition, carLength).immutable();
                            }
                            // System.out.println("reported position: " + position);
                            // System.out.println("expected position: " + expectedPosition);
                            assertEquals("Position should match initial position", expectedPosition.getSI(),
                                    position.getSI(), 0.0001);
                        }
                    }
                    catch (NetworkException ne)
                    {
                        if (!expectException)
                        {
                            System.out.println(ne);
                            fail("Calling position on lane that the car is on should NOT have thrown a NetworkException");
                        }
                    }
                }
            }
        }
        // Assign a movement to the car (10 seconds of acceleration of 2 m/s/s)
        // scheduled event that moves the car at t=0
        assertEquals("lastEvaluation time is 0", 0, car.getLastEvaluationTime().getSI(), 0.00001);
        assertEquals("nextEvaluation time is 0", 0, car.getNextEvaluationTime().getSI(), 0.00001);
        // Increase the simulator clock in small steps and verify the both positions on all lanes at each step
        double step = 0.01d;
        for (int i = 0;; i++)
        {
            DoubleScalar.Abs<TimeUnit> stepTime = new DoubleScalar.Abs<TimeUnit>(i * step, TimeUnit.SECOND);
            if (stepTime.getSI() > validFor.getSI())
            {
                break;
            }
            if (stepTime.getSI() > 0.5)
            {
                step = 0.1; // Reduce testing time by increasing the step size
            }
            // System.out.println("Simulating until " + stopTime.getSI());
            simulator.runUpTo(stepTime);
            if (stepTime.getSI() > 0)
            {
                assertEquals("nextEvaluation time is " + validFor, validFor.getSI(), car.getNextEvaluationTime()
                        .getSI(), 0.0001);
                assertEquals("acceleration is " + acceleration, acceleration.getSI(), car.getAcceleration().getSI(),
                        0.00001);
            }
            DoubleScalar.Abs<SpeedUnit> longitudinalVelocity = car.getLongitudinalVelocity();
            double expectedLongitudinalVelocity = initialSpeed.getSI() + stepTime.getSI() * acceleration.getSI();
            assertEquals("longitudinal velocity is " + expectedLongitudinalVelocity, expectedLongitudinalVelocity,
                    longitudinalVelocity.getSI(), 0.00001);
            assertEquals("lateral velocity is 0", 0, car.getLateralVelocity().getSI(), 0.00001);
            for (RelativePosition relativePosition : new RelativePosition[]{car.getFront(), car.getRear()})
            {
                Map<Lane, Double> positions = car.fractionalPositions(relativePosition);
                assertEquals("Car should be in two lanes", 2, positions.size());
                Double pos = positions.get(lanesGroupA[1]);
                assertTrue("Car should be in lane 1 of lane group A", null != pos);
                assertEquals("fractional position should be equal to result of fractionalPosition(lane, ...)", pos,
                        car.fractionalPosition(lanesGroupA[1], relativePosition), 0.0000001);
                pos = positions.get(lanesGroupB[1]);
                assertTrue("Car should be in lane 1 of lane group B", null != pos);
                assertEquals("fractional position should be equal to result of fractionalPosition(lane, ...)", pos,
                        car.fractionalPosition(lanesGroupB[1], relativePosition), 0.0000001);
            }
            for (Lane[] laneGroup : new Lane[][]{lanesGroupA, lanesGroupB})
            {
                for (int laneIndex = 0; laneIndex < laneGroup.length; laneIndex++)
                {
                    Lane lane = laneGroup[laneIndex];
                    boolean expectException = 1 != laneIndex;
                    for (RelativePosition relativePosition : new RelativePosition[]{car.getFront(), car.getRear()})
                    {
                        // System.out.println("lane:" + lane + ", expectedException: " + expectException
                        // + ", relativePostion: " + relativePosition);
                        try
                        {
                            DoubleScalar.Rel<LengthUnit> position = car.position(lane, relativePosition);
                            if (expectException)
                            {
                                // System.out.println("position: " + position);
                                fail("Calling position on lane that the car is NOT on should have thrown a "
                                        + "NetworkException");
                            }
                            else
                            {
                                DoubleScalar.Rel<LengthUnit> expectedPosition =
                                        laneGroup == lanesGroupA ? positionA : positionB;
                                expectedPosition =
                                        DoubleScalar.plus(
                                                expectedPosition,
                                                new DoubleScalar.Rel<LengthUnit>(stepTime.getSI()
                                                        * initialSpeed.getSI(), LengthUnit.SI)).immutable();
                                expectedPosition =
                                        DoubleScalar.plus(
                                                expectedPosition,
                                                new DoubleScalar.Rel<LengthUnit>(0.5 * acceleration.getSI()
                                                        * stepTime.getSI() * stepTime.getSI(), LengthUnit.SI))
                                                .immutable();
                                // FIXME There should be a (better) way to check equality of RelativePosition
                                if (relativePosition.getDx().getSI() != 0)
                                {
                                    expectedPosition = DoubleScalar.plus(expectedPosition, carLength).immutable();
                                }
                                // System.out.println("reported position: " + position);
                                // System.out.println("expected position: " + expectedPosition);
                                assertEquals("Position should match initial position", expectedPosition.getSI(),
                                        position.getSI(), 0.0001);
                            }
                        }
                        catch (NetworkException ne)
                        {
                            if (!expectException)
                            {
                                System.out.println(ne);
                                fail("Calling position on lane that the car is on should NOT have thrown a NetworkException");
                            }
                        }
                        try
                        {
                            double fractionalPosition = car.fractionalPosition(lane, relativePosition);
                            if (expectException)
                            {
                                // System.out.println("position: " + position);
                                fail("Calling position on lane that the car is NOT on should have thrown a NetworkException");
                            }
                            else
                            {
                                DoubleScalar.Rel<LengthUnit> expectedPosition =
                                        laneGroup == lanesGroupA ? positionA : positionB;
                                expectedPosition =
                                        DoubleScalar.plus(
                                                expectedPosition,
                                                new DoubleScalar.Rel<LengthUnit>(stepTime.getSI()
                                                        * initialSpeed.getSI(), LengthUnit.SI)).immutable();
                                expectedPosition =
                                        DoubleScalar.plus(
                                                expectedPosition,
                                                new DoubleScalar.Rel<LengthUnit>(0.5 * acceleration.getSI()
                                                        * stepTime.getSI() * stepTime.getSI(), LengthUnit.SI))
                                                .immutable();
                                // FIXME There should be a (better) way to check equality of RelativePosition
                                if (relativePosition.getDx().getSI() != 0)
                                {
                                    expectedPosition = DoubleScalar.plus(expectedPosition, carLength).immutable();
                                }
                                // System.out.println("reported position: " + position);
                                // System.out.println("expected position: " + expectedPosition);
                                double expectedFractionalPosition = expectedPosition.getSI() / lane.getLength().getSI();
                                assertEquals("Position should match initial position", expectedFractionalPosition,
                                        fractionalPosition, 0.000001);
                            }
                        }
                        catch (NetworkException ne)
                        {
                            if (!expectException)
                            {
                                System.out.println(ne);
                                fail("Calling fractionalPosition on lane that the car is on should NOT have thrown a "
                                        + "NetworkException");
                            }
                        }
                    }
                }
            }
        }
        // A GTU can exist on several lanes at once; create another lane group to test that
        NodeGeotools.STR nodeCFrom = new NodeGeotools.STR("CFrom", new Coordinate(10, 100, 0));
        NodeGeotools.STR nodeCTo = new NodeGeotools.STR("CTo", new Coordinate(1000, 0, 0));
        Lane[] lanesGroupC =
                LaneFactory.makeMultiLane("C", nodeCFrom, nodeCTo, null, 3, laneType,
                        (OTSDEVSSimulatorInterface) simulator.getSimulator());
        car.addLane(lanesGroupC[0], new DoubleScalar.Rel<LengthUnit>(0.0, LengthUnit.SI));
        for (RelativePosition relativePosition : new RelativePosition[]{car.getFront(), car.getRear()})
        {
            Map<Lane, Double> positions = car.fractionalPositions(relativePosition);
            assertEquals("Car should be in three lanes", 3, positions.size());
            Double pos = positions.get(lanesGroupA[1]);
            assertTrue("Car should be in lane 1 of lane group A", null != pos);
            assertEquals("fractional position should be equal to result of fractionalPosition(lane, ...)", pos,
                    car.fractionalPosition(lanesGroupA[1], relativePosition), 0.0000001);
            pos = positions.get(lanesGroupB[1]);
            assertTrue("Car should be in lane 1 of lane group B", null != pos);
            assertEquals("fractional position should be equal to result of fractionalPosition(lane, ...)", pos,
                    car.fractionalPosition(lanesGroupB[1], relativePosition), 0.0000001);
            pos = positions.get(lanesGroupC[0]);
            assertTrue("Car should be in lane 0 of lane group C", null != pos);
            // The next one fails - maybe I don't understand something - PK
            // assertEquals("fractional position should be 0", 0,
            // car.fractionalPosition(lanesGroupC[0], relativePosition), 0.0000001);
            assertEquals("fractional position should be equal to result of fractionalPosition(lane, ...)", pos,
                    car.fractionalPosition(lanesGroupC[0], relativePosition), 0.0000001);
        }
        car.removeLane(lanesGroupA[1]);
        for (RelativePosition relativePosition : new RelativePosition[]{car.getFront(), car.getRear()})
        {
            Map<Lane, Double> positions = car.fractionalPositions(relativePosition);
            assertEquals("Car should be in two lanes", 2, positions.size());
            Double pos = positions.get(lanesGroupB[1]);
            assertTrue("Car should be in lane 1 of lane group B", null != pos);
            assertEquals("fractional position should be equal to result of fractionalPosition(lane, ...)", pos,
                    car.fractionalPosition(lanesGroupB[1], relativePosition), 0.0000001);
            pos = positions.get(lanesGroupC[0]);
            assertTrue("Car should be in lane 0 of lane group C", null != pos);
            // The next one fails - maybe I don't understand something - PK
            // assertEquals("fractional position should be 0", 0,
            // car.fractionalPosition(lanesGroupC[0], relativePosition), 0.0000001);
            assertEquals("fractional position should be equal to result of fractionalPosition(lane, ...)", pos,
                    car.fractionalPosition(lanesGroupC[0], relativePosition), 0.0000001);
        }
        // TODO
        // removeLane should throw an Error when the car is not on that lane (currently this is silently ignored)
        // TODO
        // figure out why the added lane has a non-zero position
    }

}

/**
 * Dummy OTSModelInterface.
 * <p>
 * Copyright (c) 2013-2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights
 * reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version 14 jan. 2015 <br>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
class DummyModel implements OTSModelInterface
{
    /** */
    private static final long serialVersionUID = 20150114L;

    /** The simulator. */
    private SimulatorInterface<DoubleScalar.Abs<TimeUnit>, DoubleScalar.Rel<TimeUnit>, OTSSimTimeDouble> simulator;

    /**
     * Register the simulator.
     * @param simulator SimulatorInterface&lt;DoubleScalar.Abs&lt;TimeUnit&gt;, DoubleScalar.Rel&lt;TimeUnit&gt;,
     *            OTSSimTimeDouble&gt;; the simulator
     */
    public void setSimulator(
            SimulatorInterface<DoubleScalar.Abs<TimeUnit>, DoubleScalar.Rel<TimeUnit>, OTSSimTimeDouble> simulator)
    {
        this.simulator = simulator;
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(SimulatorInterface<Abs<TimeUnit>, Rel<TimeUnit>, OTSSimTimeDouble> arg0)
            throws SimRuntimeException, RemoteException
    {
        // Nothing happens here
    }

    /** {@inheritDoc} */
    @Override
    public SimulatorInterface<DoubleScalar.Abs<TimeUnit>, DoubleScalar.Rel<TimeUnit>, OTSSimTimeDouble> getSimulator()
            throws RemoteException
    {
        if (null == this.simulator)
        {
            throw new Error("getSimulator called, but simulator field is null");
        }
        return this.simulator;
    }

}
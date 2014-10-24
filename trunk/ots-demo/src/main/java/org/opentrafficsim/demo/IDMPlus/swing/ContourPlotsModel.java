package org.opentrafficsim.demo.IDMPlus.swing;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import javax.naming.NamingException;
import javax.vecmath.Point2d;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

import org.opentrafficsim.car.Car;
import org.opentrafficsim.core.dsol.OTSAnimatorInterface;
import org.opentrafficsim.core.dsol.OTSDEVSSimulatorInterface;
import org.opentrafficsim.core.dsol.OTSModelInterface;
import org.opentrafficsim.core.dsol.OTSSimTimeDouble;
import org.opentrafficsim.core.gtu.following.GTUFollowingModel;
import org.opentrafficsim.core.gtu.following.IDMPlus;
import org.opentrafficsim.core.gtu.following.GTUFollowingModel.GTUFollowingModelResult;
import org.opentrafficsim.core.unit.LengthUnit;
import org.opentrafficsim.core.unit.SpeedUnit;
import org.opentrafficsim.core.unit.TimeUnit;
import org.opentrafficsim.core.value.vdouble.scalar.DoubleScalar;
import org.opentrafficsim.demo.IDMPlus.swing.animation.AnimatedCar;
import org.opentrafficsim.demo.IDMPlus.swing.animation.Link;
import org.opentrafficsim.demo.IDMPlus.swing.animation.LinkAnimation;
import org.opentrafficsim.demo.IDMPlus.swing.animation.Node;
import org.opentrafficsim.graphs.ContourPlot;

/**
 * Simulate a single lane road of 5 km length. Vehicles are generated at a constant rate of 1500 veh/hour. At time 300s
 * a blockade is inserted at position 4 km; this blockade is removed at time 500s. The used car following algorithm is
 * IDM+ <a href="http://opentrafficsim.org/downloads/MOTUS%20reference.pdf"><i>Integrated Lane Change Model with
 * Relaxation and Synchronization</i>, by Wouter J. Schakel, Victor L. Knoop and Bart van Arem, 2012</a>. <br>
 * Output is a set of block charts:
 * <ul>
 * <li>Traffic density</li>
 * <li>Speed</li>
 * <li>Flow</li>
 * <li>Acceleration</li>
 * </ul>
 * All these graphs display simulation time along the horizontal axis and distance along the road along the vertical
 * axis.
 * <p>
 * Copyright (c) 2013-2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights
 * reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version Aug 1, 2014 <br>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class ContourPlotsModel implements OTSModelInterface
{
    /** */
    private static final long serialVersionUID = 20140815L;

    /** the simulator. */
    private OTSDEVSSimulatorInterface simulator;

    /** the headway (inter-vehicle time). */
    private DoubleScalar.Rel<TimeUnit> headway;

    /** number of cars created. */
    private int carsCreated = 0;

    /** the car following model, e.g. IDM Plus. */
    protected GTUFollowingModel<AnimatedCar> carFollowingModel;

    /** cars in the model. */
    private ArrayList<AnimatedCar> cars = new ArrayList<AnimatedCar>();

    /** minimum distance. */
    private DoubleScalar.Abs<LengthUnit> minimumDistance = new DoubleScalar.Abs<LengthUnit>(0, LengthUnit.METER);

    /** maximum distance. */
    private DoubleScalar.Abs<LengthUnit> maximumDistance = new DoubleScalar.Abs<LengthUnit>(5000, LengthUnit.METER);

    /** the speed limit. */
    private DoubleScalar.Abs<SpeedUnit> speedLimit = new DoubleScalar.Abs<SpeedUnit>(100, SpeedUnit.KM_PER_HOUR);

    /** the contour plots. */
    private ArrayList<ContourPlot> contourPlots = new ArrayList<ContourPlot>();

    /** {@inheritDoc} */
    @Override
    public final void constructModel(
            final SimulatorInterface<DoubleScalar.Abs<TimeUnit>, DoubleScalar.Rel<TimeUnit>, OTSSimTimeDouble> simulator)
            throws SimRuntimeException, RemoteException
    {
        this.simulator = (OTSDEVSSimulatorInterface) simulator;

        this.carFollowingModel = new IDMPlus<AnimatedCar>();

        // 1500 [veh / hour] == 2.4s headway
        this.headway = new DoubleScalar.Rel<TimeUnit>(3600.0 / 1500.0, TimeUnit.SECOND);

        try
        {
            this.simulator.scheduleEventAbs(new DoubleScalar.Abs<TimeUnit>(0.0, TimeUnit.SECOND), this, this,
                    "generateCar", null);
            this.simulator.scheduleEventAbs(new DoubleScalar.Abs<TimeUnit>(1799.99, TimeUnit.SECOND), this, this,
                    "drawGraphs", null);
        }
        catch (RemoteException | SimRuntimeException exception)
        {
            exception.printStackTrace();
        }

        // in case we run on an animator and not on a simulator, we create the animation
        if (simulator instanceof OTSAnimatorInterface)
        {
            createAnimation();
        }
    }

    /**
     * Make the animation for each of the components that we want to see on the screen.
     */
    private void createAnimation()
    {
        try
        {
            // let's make several layers with the different types of information
            Node nodeA = new Node("A", new Point2d(0.0d, 0.0d));
            Node nodeB = new Node("B", new Point2d(5000.0d, 0.0d));
            Link link = new Link("Road", nodeA, nodeB, new DoubleScalar.Rel<LengthUnit>(5000.0d, LengthUnit.METER));
            new LinkAnimation(link, this.simulator, 5.0f);
        }
        catch (NamingException | RemoteException exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * Add one movement step of one Car to all contour plots.
     * @param car Car
     */
    protected final void addToContourPlots(final Car car)
    {
        for (ContourPlot contourPlot : this.contourPlots)
        {
            contourPlot.addData(car);
        }
    }

    /**
     * Notify the contour plots that the underlying data has changed.
     */
    protected final void drawGraphs()
    {
        for (ContourPlot contourPlot : this.contourPlots)
        {
            contourPlot.reGraph();
        }
    }

    /**
     * Generate cars at a fixed rate (implemented by re-scheduling this method).
     * @throws NamingException
     */
    protected final void generateCar() throws NamingException
    {
        DoubleScalar.Abs<LengthUnit> initialPosition = new DoubleScalar.Abs<LengthUnit>(0, LengthUnit.METER);
        DoubleScalar.Rel<SpeedUnit> initialSpeed = new DoubleScalar.Rel<SpeedUnit>(100, SpeedUnit.KM_PER_HOUR);
        IDMCar car;
        try
        {
            car =
                    new IDMCar(++this.carsCreated, this.simulator, this.carFollowingModel, this.simulator
                            .getSimulatorTime().get(), initialPosition, initialSpeed);
            this.cars.add(0, car);
            this.simulator.scheduleEventRel(this.headway, this, this, "generateCar", null);
        }
        catch (RemoteException | SimRuntimeException exception)
        {
            exception.printStackTrace();
        }
    }

    /** {@inheritDoc} */
    @Override
    public final SimulatorInterface<DoubleScalar.Abs<TimeUnit>, DoubleScalar.Rel<TimeUnit>, OTSSimTimeDouble> getSimulator()
            throws RemoteException
    {
        return this.simulator;
    }

    /**
     * @return contourPlots
     */
    public final ArrayList<ContourPlot> getContourPlots()
    {
        return this.contourPlots;
    }

    /**
     * @return minimumDistance
     */
    public final DoubleScalar.Abs<LengthUnit> getMinimumDistance()
    {
        return this.minimumDistance;
    }

    /**
     * @return maximumDistance
     */
    public final DoubleScalar.Abs<LengthUnit> getMaximumDistance()
    {
        return this.maximumDistance;
    }

    /** Inner class IDMCar. */
    protected class IDMCar extends AnimatedCar
    {
        /**
         * Create a new IDMCar.
         * @param id integer; the id of the new IDMCar
         * @param simulator OTSDEVSSimulator; the simulator that runs the new IDMCar
         * @param carFollowingModel CarFollowingModel; the car following model of the new IDMCar
         * @param initialTime DoubleScalar.Abs&lt;TimeUnit&gt;; the time of first evaluation of the new IDMCar
         * @param initialPosition DoubleScalar.Abs&lt;LengthUnit&gt;; the initial position of the new IDMCar
         * @param initialSpeed DoubleScalar.Rel&lt;SpeedUnit&gt;; the initial speed of the new IDMCar
         * @throws NamingException
         * @throws RemoteException
         */
        public IDMCar(final int id, final OTSDEVSSimulatorInterface simulator,
                final GTUFollowingModel carFollowingModel, final DoubleScalar.Abs<TimeUnit> initialTime,
                final DoubleScalar.Abs<LengthUnit> initialPosition, final DoubleScalar.Rel<SpeedUnit> initialSpeed)
                throws RemoteException, NamingException
        {
            super(id, simulator, carFollowingModel, initialTime, initialPosition, initialSpeed);
            try
            {
                simulator.scheduleEventAbs(simulator.getSimulatorTime(), this, this, "move", null);
            }
            catch (RemoteException | SimRuntimeException exception)
            {
                exception.printStackTrace();
            }
        }

        /**
         * @throws RemoteException RemoteException
         * @throws NamingException
         */
        protected final void move() throws RemoteException, NamingException
        {
            System.out.println("move " + this.getID());
            DoubleScalar.Abs<TimeUnit> now = getSimulator().getSimulatorTime().get();
            if (getPosition(now).getSI() > ContourPlotsModel.this.maximumDistance.getSI())
            {
                ContourPlotsModel.this.cars.remove(this);
                return;
            }
            Collection<AnimatedCar> leaders = new ArrayList<AnimatedCar>();
            int carIndex = ContourPlotsModel.this.cars.indexOf(this);
            if (carIndex < ContourPlotsModel.this.cars.size() - 1)
            {
                leaders.add(ContourPlotsModel.this.cars.get(carIndex + 1));
            }
            // Add a stationary car at 4000m to simulate an opening bridge
            if (now.getSI() >= 300 && now.getSI() < 500)
            {
                AnimatedCar block =
                        new AnimatedCar(99999, null, ContourPlotsModel.this.carFollowingModel, now,
                                new DoubleScalar.Abs<LengthUnit>(4000, LengthUnit.METER),
                                new DoubleScalar.Rel<SpeedUnit>(0, SpeedUnit.KM_PER_HOUR));
                leaders.add(block);
            }
            GTUFollowingModelResult cfmr =
                    ContourPlotsModel.this.carFollowingModel.computeAcceleration(this, leaders,
                            ContourPlotsModel.this.speedLimit);
            setState(cfmr);

            // Add the movement of this Car to the contour plots
            addToContourPlots(this);

            try
            {
                getSimulator().scheduleEventRel(new DoubleScalar.Rel<TimeUnit>(0.5, TimeUnit.SECOND), this, this,
                        "move", null);
            }
            catch (RemoteException | SimRuntimeException exception)
            {
                exception.printStackTrace();
            }
        }
    }
}

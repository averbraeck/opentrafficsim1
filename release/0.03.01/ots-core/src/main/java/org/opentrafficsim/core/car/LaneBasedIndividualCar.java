package org.opentrafficsim.core.car;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.animation.D2.Renderable2D;
import nl.tudelft.simulation.language.reflection.ClassUtil;

import org.djunits.unit.LengthUnit;
import org.djunits.unit.SpeedUnit;
import org.djunits.value.vdouble.scalar.DoubleScalar;
import org.opentrafficsim.core.dsol.OTSAnimatorInterface;
import org.opentrafficsim.core.dsol.OTSDEVSSimulatorInterface;
import org.opentrafficsim.core.gtu.GTUException;
import org.opentrafficsim.core.gtu.GTUType;
import org.opentrafficsim.core.gtu.RelativePosition;
import org.opentrafficsim.core.gtu.RelativePosition.TYPE;
import org.opentrafficsim.core.gtu.animation.DefaultCarAnimation;
import org.opentrafficsim.core.gtu.animation.GTUColorer;
import org.opentrafficsim.core.gtu.following.GTUFollowingModel;
import org.opentrafficsim.core.gtu.lane.AbstractLaneBasedIndividualGTU;
import org.opentrafficsim.core.gtu.lane.changing.LaneChangeModel;
import org.opentrafficsim.core.network.NetworkException;
import org.opentrafficsim.core.network.lane.Lane;
import org.opentrafficsim.core.network.route.LaneBasedRouteGenerator;
import org.opentrafficsim.core.network.route.LaneBasedRouteNavigator;

/**
 * Augments the AbstractLaneBasedIndividualGTU with a LaneBasedIndividualCarBuilder and animation support
 * <p>
 * Copyright (c) 2013-2015 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$,
 *          initial version Oct 22, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class LaneBasedIndividualCar extends AbstractLaneBasedIndividualGTU
{
    /** */
    private static final long serialVersionUID = 20141025L;

    /** animation. */
    private Renderable2D animation;

    /** Sensing positions. */
    private final Map<RelativePosition.TYPE, RelativePosition> relativePositions = new LinkedHashMap<>();

    /**
     * @param id ID; the id of the GTU
     * @param gtuType GTUType; the type of GTU, e.g. TruckType, CarType, BusType
     * @param gtuFollowingModel GTUFollowingModel; the following model, including a reference to the simulator
     * @param laneChangeModel LaneChangeModel; the lane change model
     * @param initialLongitudinalPositions Map&lt;Lane, DoubleScalar.Rel&lt;LengthUnit&gt;&gt;; the initial positions of the car
     *            on one or more lanes
     * @param initialSpeed DoubleScalar.Abs&lt;SpeedUnit&gt;; the initial speed of the car on the lane
     * @param length DoubleScalar.Rel&lt;LengthUnit&gt;; the maximum length of the GTU (parallel with driving direction)
     * @param width DoubleScalar.Rel&lt;LengthUnit&gt;; the maximum width of the GTU (perpendicular to driving direction)
     * @param maximumVelocity DoubleScalar.Abs&lt;SpeedUnit&gt;;the maximum speed of the GTU (in the driving direction)
     * @param routeNavigator Route; the route that the GTU will follow
     * @param simulator OTSDEVSSimulatorInterface; the simulator
     * @throws NamingException if an error occurs when adding the animation handler
     * @throws RemoteException when the simulator cannot be reached
     * @throws NetworkException when the GTU cannot be placed on the given lane
     * @throws SimRuntimeException when the move method cannot be scheduled
     * @throws GTUException when a parameter is invalid
     */
    @SuppressWarnings("checkstyle:parameternumber")
    public LaneBasedIndividualCar(final String id, final GTUType gtuType, final GTUFollowingModel gtuFollowingModel,
        final LaneChangeModel laneChangeModel, final Map<Lane, DoubleScalar.Rel<LengthUnit>> initialLongitudinalPositions,
        final DoubleScalar.Abs<SpeedUnit> initialSpeed, final DoubleScalar.Rel<LengthUnit> length,
        final DoubleScalar.Rel<LengthUnit> width, final DoubleScalar.Abs<SpeedUnit> maximumVelocity,
        final LaneBasedRouteNavigator routeNavigator, final OTSDEVSSimulatorInterface simulator) throws NamingException,
        RemoteException, NetworkException, SimRuntimeException, GTUException
    {
        this(id, gtuType, gtuFollowingModel, laneChangeModel, initialLongitudinalPositions, initialSpeed, length, width,
            maximumVelocity, routeNavigator, simulator, DefaultCarAnimation.class, null);
    }

    /**
     * Construct a new LaneBasedIndividualCar.
     * @param id ID; the id of the GTU
     * @param gtuType GTUTYpe; the type of GTU, e.g. TruckType, CarType, BusType
     * @param gtuFollowingModel GTUFollowingModel; the following model, including a reference to the simulator
     * @param laneChangeModel LaneChangeModel; the lane change model
     * @param initialLongitudinalPositions Map&lt;Lane, DoubleScalar.Rel&lt;LengthUnit&gt;&gt;; the initial positions of the car
     *            on one or more lanes
     * @param initialSpeed DoubleScalar.Abs&lt;SpeedUnit&gt;; the initial speed of the car on the lane
     * @param length DoubleScalar.Rel&lt;LengthUnit&gt;; the maximum length of the GTU (parallel with driving direction)
     * @param width DoubleScalar.Rel&lt;LengthUnit&gt;; the maximum width of the GTU (perpendicular to driving direction)
     * @param maximumVelocity DoubleScalar.Abs&lt;SpeedUnit&gt;;the maximum speed of the GTU (in the driving direction)
     * @param routeNavigator Route the route that the GTU will follow
     * @param simulator OTSDEVSSimulatorInterface; the simulator
     * @param animationClass Class&lt;? extends Renderable2D&gt;; the class for animation or null if no animation
     * @param gtuColorer GTUColorer; the GTUColorer that will be linked from the animation to determine the color (may be null
     *            in which case a default will be used)
     * @throws NamingException if an error occurs when adding the animation handler
     * @throws RemoteException when the simulator cannot be reached
     * @throws NetworkException when the GTU cannot be placed on the given lane
     * @throws SimRuntimeException when the move method cannot be scheduled
     * @throws GTUException when a parameter is invalid
     */
    @SuppressWarnings("checkstyle:parameternumber")
    public LaneBasedIndividualCar(final String id, final GTUType gtuType, final GTUFollowingModel gtuFollowingModel,
        final LaneChangeModel laneChangeModel, final Map<Lane, DoubleScalar.Rel<LengthUnit>> initialLongitudinalPositions,
        final DoubleScalar.Abs<SpeedUnit> initialSpeed, final DoubleScalar.Rel<LengthUnit> length,
        final DoubleScalar.Rel<LengthUnit> width, final DoubleScalar.Abs<SpeedUnit> maximumVelocity,
        final LaneBasedRouteNavigator routeNavigator, final OTSDEVSSimulatorInterface simulator,
        final Class<? extends Renderable2D> animationClass, final GTUColorer gtuColorer) throws NamingException,
        RemoteException, NetworkException, SimRuntimeException, GTUException
    {
        super(id, gtuType, gtuFollowingModel, laneChangeModel, initialLongitudinalPositions, initialSpeed, length, width,
            maximumVelocity, routeNavigator, simulator);

        // sensor positions.
        // We take the rear position of the Car to be the reference point. So the front is the length
        // of the Car away from the reference point in the positive (driving) X-direction.
        DoubleScalar.Rel<LengthUnit> zero = new DoubleScalar.Rel<LengthUnit>(0.0d, LengthUnit.METER);
        DoubleScalar.Rel<LengthUnit> dx = new DoubleScalar.Rel<LengthUnit>(getLength().getSI(), LengthUnit.METER);
        this.relativePositions.put(RelativePosition.FRONT, new RelativePosition(dx, zero, zero, RelativePosition.FRONT));
        this.relativePositions.put(RelativePosition.REAR, new RelativePosition(zero, zero, zero, RelativePosition.REAR));
        this.relativePositions.put(RelativePosition.REFERENCE, RelativePosition.REFERENCE_POSITION);

        /*
         * REMOVE START if (id.equals("A1:1")) { try { this.simulator = simulator; //this.pwsimloc = new
         * PrintWriter("d:/pwsimloc.xls"); //simulator.scheduleEventRel(new DoubleScalar.Rel<TimeUnit>(0.1, TimeUnit.SECOND),
         * this, this, "simloc", null); this.pwthreadloc = new PrintWriter("d:/pwthreadloc.xls"); new ClockLocThread().start();
         * } catch (FileNotFoundException exception) { exception.printStackTrace(); } } /* REMOVE END
         */

        // animation
        if (simulator instanceof OTSAnimatorInterface && animationClass != null)
        {
            try
            {
                Constructor<?> constructor;

                if (null == gtuColorer)
                {
                    constructor = ClassUtil.resolveConstructor(animationClass, new Object[]{this, simulator});
                    this.animation = (Renderable2D) constructor.newInstance(this, simulator);
                }
                else
                {
                    constructor = ClassUtil.resolveConstructor(animationClass, new Object[]{this, simulator, gtuColorer});
                    this.animation = (Renderable2D) constructor.newInstance(this, simulator, gtuColorer);
                }
            }
            catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException
                | IllegalArgumentException | InvocationTargetException exception)
            {
                throw new NetworkException("Could not instantiate car animation of type " + animationClass.getName(),
                    exception);
            }
        }
    }

    /*
     * REMOVE START PrintWriter pwsimloc; PrintWriter pwthreadloc; OTSDEVSSimulatorInterface simulator; protected void simloc()
     * { try { Map<Lane, DoubleScalar.Rel<LengthUnit>> posmap = positions(getFront()); Lane lane =
     * posmap.keySet().iterator().next(); pwsimloc.write(getSimulator().getSimulatorTime().get().getSI() + "\t" +
     * lane.toString() + "\t" + posmap.get(lane).getSI() + "\n"); pwsimloc.flush(); simulator.scheduleEventRel(new
     * DoubleScalar.Rel<TimeUnit>(0.1, TimeUnit.SECOND), this, this, "simloc", null); } catch (RemoteException |
     * NetworkException | SimRuntimeException e) { e.printStackTrace(); } } protected class ClockLocThread extends Thread {
     * @Override public void run() { while (true) { try { Thread.sleep(100); Map<Lane, DoubleScalar.Rel<LengthUnit>> posmap =
     * positions(getFront()); Lane lane = posmap.keySet().iterator().next();
     * pwthreadloc.write(getSimulator().getSimulatorTime().get().getSI() + "\t" + lane.toString() + "\t" +
     * posmap.get(lane).getSI() + "\n"); pwthreadloc.flush(); } catch (RemoteException | NetworkException | InterruptedException
     * e) { e.printStackTrace(); } } } } /* REMOVE END
     */

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public RelativePosition getFront()
    {
        return this.relativePositions.get(RelativePosition.FRONT);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public RelativePosition getRear()
    {
        return this.relativePositions.get(RelativePosition.REAR);
    }

    /** {@inheritDoc} */
    @Override
    public final Map<TYPE, RelativePosition> getRelativePositions()
    {
        return this.relativePositions;
    }

    /** {@inheritDoc} */
    @Override
    public final void destroy()
    {
        if (this.animation != null)
        {
            try
            {
                this.animation.destroy();
                this.animation = null;
            }
            catch (Exception e)
            {
                System.err.println("Car: " + this.getId());
                e.printStackTrace();
            }
        }
        super.destroy();
    }

    /** {@inheritDoc} */
    public final String toString()
    {
        Map<Lane, DoubleScalar.Rel<LengthUnit>> frontPositions;
        try
        {
            frontPositions = positions(getFront());
        }
        catch (RemoteException | NetworkException exception)
        {
            return String.format("Car %s [FRONTPOSITIONS EXCEPTION]", getId());
        }
        if (frontPositions.size() == 0)
        {
            return String.format("Car %s [NOT ON A LANE]", getId());
        }
        Lane frontLane = frontPositions.keySet().iterator().next();
        return String.format("Car %s front: %s[%s]", getId(), frontLane, frontPositions.get(frontLane));
    }

    /**
     * Build an individual car and use easy setter methods to instantiate the car. Typical use looks like:
     * 
     * <pre>
     * LaneBasedIndividualCar&lt;String&gt; car = new LaneBasedIndividualCarBuilder&lt;String&gt;().setId("Car:"+nr)
     *    .setLength(new DoubleScalar.Rel&lt;LengthUnit&gt;(4.0, LengthUnit.METER))....build(); 
     *    
     * or
     * 
     * LaneBasedIndividualCarBuilder&lt;String&gt; carBuilder = new LaneBasedIndividualCarBuilder&lt;String&gt;();
     * carBuilder.setId("Car:"+nr);
     * carBuilder.setLength(new DoubleScalar.Rel&lt;LengthUnit&gt;(4.0, LengthUnit.METER));
     * carBuilder.setWidth(new DoubleScalar.Rel&lt;LengthUnit&gt;(1.8, LengthUnit.METER));
     * ...
     * LaneBasedIndividualCar&lt;String&gt; car = carBuilder.build();
     * </pre>
     * <p>
     * Copyright (c) 2013-2015 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. <br>
     * All rights reserved. <br>
     * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
     * <p>
     * @version $Revision$, $LastChangedDate$, by $Author$,
     *          initial Feb 3, 2015 <br>
     * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     */
    @SuppressWarnings("checkstyle:hiddenfield")
    public static class LaneBasedIndividualCarBuilder
    {
        /** The id of the GTU. */
        private String id = null;

        /** The type of GTU, e.g. TruckType, CarType, BusType. */
        private GTUType gtuType = null;

        /** The initial positions of the car on one or more lanes. */
        private Map<Lane, DoubleScalar.Rel<LengthUnit>> initialLongitudinalPositions = null;;

        /** The initial speed of the car on the lane. */
        private DoubleScalar.Abs<SpeedUnit> initialSpeed = null;

        /** CarFollowingModel used by this Car. */
        private GTUFollowingModel gtuFollowingModel = null;

        /** The lane change model. */
        private LaneChangeModel laneChangeModel = null;

        /** The length of the GTU (parallel with driving direction). */
        private DoubleScalar.Rel<LengthUnit> length = null;

        /** The width of the GTU (perpendicular to driving direction). */
        private DoubleScalar.Rel<LengthUnit> width = null;

        /** The maximum speed of the GTU (in the driving direction). */
        private DoubleScalar.Abs<SpeedUnit> maximumVelocity = null;

        /** The simulator. */
        private OTSDEVSSimulatorInterface simulator = null;

        /** Animation. */
        private Class<? extends Renderable2D> animationClass = null;

        /** GTUColorer. */
        private GTUColorer gtuColorer = null;

        /** Cached Route. */
        private LaneBasedRouteGenerator routeGenerator = null;

        /**
         * @param id set id
         * @return the class itself for chaining the setters
         */
        public final LaneBasedIndividualCarBuilder setId(final String id)
        {
            this.id = id;
            return this;
        }

        /**
         * @param gtuType set gtuType
         * @return the class itself for chaining the setters
         */
        public final LaneBasedIndividualCarBuilder setGtuType(final GTUType gtuType)
        {
            this.gtuType = gtuType;
            return this;
        }

        /**
         * @param gtuFollowingModel GTUFollowingModel; the GTU following model used by the built cars
         * @return the class itself for chaining the setters
         */
        public final LaneBasedIndividualCarBuilder setGTUFollowingModel(final GTUFollowingModel gtuFollowingModel)
        {
            this.gtuFollowingModel = gtuFollowingModel;
            return this;
        }

        /**
         * @param laneChangeModel AbstractLaneChangeModel; the lane change model
         * @return the class itself for chaining the setters
         */
        public final LaneBasedIndividualCarBuilder setLaneChangeModel(final LaneChangeModel laneChangeModel)
        {
            this.laneChangeModel = laneChangeModel;
            return this;
        }

        /**
         * @param initialLongitudinalPositions set initialLongitudinalPositions
         * @return the class itself for chaining the setters
         */
        public final LaneBasedIndividualCarBuilder setInitialLongitudinalPositions(
            final Map<Lane, DoubleScalar.Rel<LengthUnit>> initialLongitudinalPositions)
        {
            this.initialLongitudinalPositions = initialLongitudinalPositions;
            return this;
        }

        /**
         * @param initialSpeed set initialSpeed
         * @return the class itself for chaining the setters
         */
        public final LaneBasedIndividualCarBuilder setInitialSpeed(final DoubleScalar.Abs<SpeedUnit> initialSpeed)
        {
            this.initialSpeed = initialSpeed;
            return this;
        }

        /**
         * @param length set length
         * @return the class itself for chaining the setters
         */
        public final LaneBasedIndividualCarBuilder setLength(final DoubleScalar.Rel<LengthUnit> length)
        {
            this.length = length;
            return this;
        }

        /**
         * @param width set width
         * @return the class itself for chaining the setters
         */
        public final LaneBasedIndividualCarBuilder setWidth(final DoubleScalar.Rel<LengthUnit> width)
        {
            this.width = width;
            return this;
        }

        /**
         * @param maximumVelocity set maximumVelocity
         * @return the class itself for chaining the setters
         */
        public final LaneBasedIndividualCarBuilder setMaximumVelocity(final DoubleScalar.Abs<SpeedUnit> maximumVelocity)
        {
            this.maximumVelocity = maximumVelocity;
            return this;
        }

        /**
         * @param simulator set simulator
         * @return the class itself for chaining the setters
         */
        public final LaneBasedIndividualCarBuilder setSimulator(final OTSDEVSSimulatorInterface simulator)
        {
            this.simulator = simulator;
            return this;
        }

        /**
         * @param animationClass set animation class
         * @return the class itself for chaining the setters
         */
        public final LaneBasedIndividualCarBuilder setAnimationClass(final Class<? extends Renderable2D> animationClass)
        {
            this.animationClass = animationClass;
            return this;
        }

        /**
         * @param routeGenerator set route generator.
         * @return the class itself for chaining the setters
         */
        public final LaneBasedIndividualCarBuilder setRouteGenerator(final LaneBasedRouteGenerator routeGenerator)
        {
            this.routeGenerator = routeGenerator;
            return this;
        }

        /**
         * @return id.
         */
        public final String getId()
        {
            return this.id;
        }

        /**
         * @return route generator.
         */
        public final LaneBasedRouteGenerator getRouteGenerator()
        {
            return this.routeGenerator;
        }

        /**
         * @return gtuType.
         */
        public final GTUType getGtuType()
        {
            return this.gtuType;
        }

        /**
         * @return initialLongitudinalPositions.
         */
        public final Map<Lane, DoubleScalar.Rel<LengthUnit>> getInitialLongitudinalPositions()
        {
            return this.initialLongitudinalPositions;
        }

        /**
         * @return initialSpeed.
         */
        public final DoubleScalar.Abs<SpeedUnit> getInitialSpeed()
        {
            return this.initialSpeed;
        }

        /**
         * @return gtuFollowingModel.
         */
        public final GTUFollowingModel getGtuFollowingModel()
        {
            return this.gtuFollowingModel;
        }

        /**
         * @return laneChangeModel.
         */
        public final LaneChangeModel getLaneChangeModel()
        {
            return this.laneChangeModel;
        }

        /**
         * @return length.
         */
        public final DoubleScalar.Rel<LengthUnit> getLength()
        {
            return this.length;
        }

        /**
         * @return width.
         */
        public final DoubleScalar.Rel<LengthUnit> getWidth()
        {
            return this.width;
        }

        /**
         * @return maximumVelocity.
         */
        public final DoubleScalar.Abs<SpeedUnit> getMaximumVelocity()
        {
            return this.maximumVelocity;
        }

        /**
         * @return simulator.
         */
        public final OTSDEVSSimulatorInterface getSimulator()
        {
            return this.simulator;
        }

        /**
         * @return animationClass.
         */
        public final Class<? extends Renderable2D> getAnimationClass()
        {
            return this.animationClass;
        }

        /**
         * @return gtuColorer.
         */
        public final GTUColorer getGtuColorer()
        {
            return this.gtuColorer;
        }

        /**
         * @param gtuColorer set gtuColorer.
         */
        public final void setGtuColorer(final GTUColorer gtuColorer)
        {
            this.gtuColorer = gtuColorer;
        }

        /**
         * Build one LaneBasedIndividualCar.
         * @return the built Car with the set properties
         * @throws Exception when not all required values have been set
         */
        public final LaneBasedIndividualCar build() throws Exception
        {
            if (null == this.id || null == this.gtuType || null == this.gtuFollowingModel || null == this.laneChangeModel
                || null == this.initialLongitudinalPositions || null == this.initialSpeed || null == this.length
                || null == this.width || null == this.maximumVelocity || null == this.routeGenerator
                || null == this.simulator)
            {
                // TODO Should throw a more specific Exception type
                throw new GTUException("factory settings incomplete");
            }
            LaneBasedIndividualCar gtu =
                new LaneBasedIndividualCar(this.id, this.gtuType, this.gtuFollowingModel, this.laneChangeModel,
                    this.initialLongitudinalPositions, this.initialSpeed, this.length, this.width, this.maximumVelocity,
                    this.routeGenerator.generateRouteNavigator(), this.simulator, this.animationClass, this.gtuColorer);
            // System.out.println("Generated GTU " + gtu + " at t=" + this.simulator.getSimulatorTime());
            return gtu;

        }

    }

}
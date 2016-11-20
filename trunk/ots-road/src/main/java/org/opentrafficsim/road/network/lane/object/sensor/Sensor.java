package org.opentrafficsim.road.network.lane.object.sensor;

import java.io.Serializable;

import org.djunits.unit.LengthUnit;
import org.djunits.value.vdouble.scalar.Length;
import org.opentrafficsim.base.immutablecollections.ImmutableSet;
import org.opentrafficsim.core.dsol.OTSDEVSSimulatorInterface;
import org.opentrafficsim.core.gtu.GTUType;
import org.opentrafficsim.core.gtu.RelativePosition;
import org.opentrafficsim.road.gtu.lane.LaneBasedGTU;
import org.opentrafficsim.road.network.lane.object.LaneBasedObject;

import nl.tudelft.simulation.event.EventType;

/**
 * A sensor is a lane-based object that can be triggered by a relative position of the GTU (e.g., front, back) when that
 * relative position passes over the sensor location on the lane.
 * <p>
 * Copyright (c) 2013-2016 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate: 2015-09-14 01:33:02 +0200 (Mon, 14 Sep 2015) $, @version $Revision: 1401 $, by $Author: averbraeck $,
 * initial version Dec 31, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public interface Sensor extends Serializable, Comparable<Sensor>, LaneBasedObject
{
    /** @return the relative position type of the vehicle (e.g., FRONT, BACK) that triggers the sensor. */
    RelativePosition.TYPE getPositionType();

    /**
     * Trigger an action on the GTU. Normally this is the GTU that triggered the sensor. The typical call therefore is
     * <code>sensor.trigger(this);</code>.
     * @param gtu the GTU for which to carry out the trigger action.
     */
    void trigger(LaneBasedGTU gtu);

    /** @return The simulator. */
    OTSDEVSSimulatorInterface getSimulator();
    
    /**
     * Return which GTU types will trigger this particular sensor.
     * @return Set&lt;GTUType&gt; the GTU types will trigger this particular sensor.
     */
    ImmutableSet<GTUType> getTriggeringGTUTypes();  

    /**
     * The <b>timed</b> event type for pub/sub indicating the triggering of a Sensor on a lane. <br>
     * Payload: Object[] {String sensorId, Sensor sensor, LaneBasedGTU gtu, RelativePosition.TYPE relativePosition}
     */
    EventType SENSOR_TRIGGER_EVENT = new EventType("SENSOR.TRIGGER");

    /** Default elevation of a sensor; if the lane is not at elevation 0; this value is probably far off. */
    Length DEFAULT_SENSOR_ELEVATION = new Length(0.1, LengthUnit.METER);

}

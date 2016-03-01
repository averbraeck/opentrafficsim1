package org.opentrafficsim.road.gtu.lane.perception;

import java.io.Serializable;

import org.djunits.value.vdouble.scalar.Time;

/**
 * An object with a time stamp, where the object is of a specific class.
 * <p>
 * Copyright (c) 2013-2015 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * $LastChangedDate: 2015-07-24 02:58:59 +0200 (Fri, 24 Jul 2015) $, @version $Revision: 1147 $, by $Author: averbraeck $,
 * initial version Jan 29, 2016 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <C> the timestamped object class. 
 */
public class TimeStampedObject<C> implements Serializable
{
    /** */
    private static final long serialVersionUID = 20160129L;

    /** the object. */
    final C object;
    
    /** the time stamp. */
    final Time.Abs timestamp;
    
    /**
     * @param object the object.
     * @param timestamp the time stamp.
     */
    public TimeStampedObject(final C object, final Time.Abs timestamp)
    {
        this.object = object;
        this.timestamp = timestamp;
    }

    /**
     * @return object
     */
    public final C getObject()
    {
        return this.object;
    }

    /**
     * @return time stamp
     */
    public final Time.Abs getTimestamp()
    {
        return this.timestamp;
    }

}

package org.opentrafficsim.core.math;

import java.io.Serializable;
import java.util.Locale;

import org.djunits.unit.DirectionUnit;
import org.djunits.value.StorageType;
import org.djunits.value.ValueException;
import org.djunits.value.vdouble.scalar.Direction;
import org.djunits.value.vdouble.vector.DirectionVector;

/**
 * 3D-rotation, RPY coded (longitudinal roll along the x-axis, lateral pitch along the y-axis and vertical yaw along the
 * z-axis), also called Tait–Bryan angles or Cardan angles. Angles are absolute and relate to the absolute XYZ-frame of the
 * world.
 * <p>
 * Copyright (c) 2013-2017 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * $LastChangedDate: 2015-07-24 02:58:59 +0200 (Fri, 24 Jul 2015) $, @version $Revision: 1147 $, by $Author: averbraeck $,
 * initial version Dec 10, 2015 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Direction3D implements Serializable
{
    /** */
    private static final long serialVersionUID = 20160000L;

    /** The angles of the rotation in 3D (RPY coded). */
    private final DirectionVector rotation;

    /**
     * @param rotation the angles in 3D (RPY coded)
     * @throws ValueException in case the vector does not have exactly three elements
     */
    public Direction3D(final DirectionVector rotation) throws ValueException
    {
        if (rotation.size() != 3)
        {
            throw new ValueException("Size of an RPY-rotation vector should be exactly 3. Got: " + rotation);
        }
        this.rotation = rotation;
    }

    /**
     * @param roll (phi) the rotation around the x-axis
     * @param pitch (theta) the rotation around the y-axis
     * @param yaw (psi) the rotation around the z-axis
     * @throws ValueException in case the units are incorrect
     */
    public Direction3D(final Direction roll, final Direction pitch, final Direction yaw) throws ValueException
    {
        this.rotation = new DirectionVector(new Direction[] { roll, pitch, yaw }, StorageType.DENSE);
    }

    /**
     * @param roll (phi) the rotation around the x-axis
     * @param pitch (theta) the rotation around the y-axis
     * @param yaw (psi) the rotation around the z-axis
     * @param unit the unit of the RPY parameters
     * @throws ValueException in case the units are incorrect
     */
    public Direction3D(final double roll, final double pitch, final double yaw, final DirectionUnit unit) throws ValueException
    {
        this.rotation = new DirectionVector(new double[] { roll, pitch, yaw }, unit, StorageType.DENSE);
    }

    /**
     * @return the roll.
     */
    public final Direction getRoll()
    {
        try
        {
            return this.rotation.get(0);
        }
        catch (ValueException exception)
        {
            // should be impossible as we constructed the vector always with three elements
            throw new RuntimeException(
                    "getRoll() gave an exception; apparently vector " + this.rotation + " was not constructed right",
                    exception);
        }
    }

    /**
     * @return the pitch.
     */
    public final Direction getPitch()
    {
        try
        {
            return this.rotation.get(1);
        }
        catch (ValueException exception)
        {
            // should be impossible as we constructed the vector always with three elements
            throw new RuntimeException(
                    "getPitch() gave an exception; apparently vector " + this.rotation + " was not constructed right",
                    exception);
        }
    }

    /**
     * @return the yaw.
     */
    public final Direction getYaw()
    {
        try
        {
            return this.rotation.get(2);
        }
        catch (ValueException exception)
        {
            // should be impossible as we constructed the vector always with three elements
            throw new RuntimeException(
                    "getYaw() gave an exception; apparently vector " + this.rotation + " was not constructed right", exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        return String.format(Locale.US, "Rotation3D.Abs roll %s, pitch %s, yaw %s", getRoll(), getPitch(), getYaw());
    }
}

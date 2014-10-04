package org.opentrafficsim.core.value;

import java.io.Serializable;

import org.opentrafficsim.core.unit.Unit;

/**
 * AbstractValue is a class to help construct Matrix, Complex, and Vector but it does not extend java.lang.Number. The Scalar
 * class <i>does</i> extend Number, and implements the same interfaces from Value.
 * <p>
 * Copyright (c) 2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version Jun 13, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <U> the Unit of the value(s) in this AbstractValue. Used for setting, getting and displaying the value(s)
 */
public abstract class AbstractValue<U extends Unit<U>> implements Value<U>, Serializable
{
    /** */
    private static final long serialVersionUID = 20140615L;

    /** The unit of the AbstractValue. */
    private final U unit;

    /**
     * Construct a new AbstractValue.
     * @param unit U; the unit of the new AbstractValue
     */
    protected AbstractValue(final U unit)
    {
        this.unit = unit;
    }

    /** {@inheritDoc} */
    @Override
    public final U getUnit()
    {
        return this.unit;
    }

    /** {@inheritDoc} */
    @Override
    public final double expressAsSIUnit(final double value)
    {
        return ValueUtil.expressAsSIUnit(value, this.unit);
    }

    /**
     * Convert a value in SI standard unit into the unit of this AbstractValue.
     * @param value double; the value in standard SI unit
     * @return double; the value in the unit as specified for this AbstractValue
     */
    protected final double expressAsSpecifiedUnit(final double value)
    {
        return ValueUtil.expressAsUnit(value, this.unit);
    }

    /** {@inheritDoc} */
    @Override
    public final boolean isAbsolute()
    {
        return this instanceof Absolute;
    }

    /** {@inheritDoc} */
    @Override
    public final boolean isRelative()
    {
        return this instanceof Relative;
    }

}

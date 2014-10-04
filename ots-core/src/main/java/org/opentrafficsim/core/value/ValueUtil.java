package org.opentrafficsim.core.value;

import org.opentrafficsim.core.unit.OffsetUnit;
import org.opentrafficsim.core.unit.Unit;

/**
 * ValueUtil implements a couple of unit-related static methods.
 * <p>
 * Copyright (c) 2013-2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights
 * reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version Aug 18, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public final class ValueUtil
{
    /**
     * This class shall never be instantiated.
     */
    private ValueUtil()
    {
        // Prevent instantiation of this class
    }

    /**
     * Convert a value in a given unit into the equivalent in the standard SI unit.
     * @param value double; the value to convert into the standard SI unit
     * @param unit Unit&lt;?&gt;; the unit of the given value
     * @return double; the value in the standard SI unit
     */
    public static double expressAsSIUnit(final double value, final Unit<?> unit)
    {
        if (unit instanceof OffsetUnit<?>)
        {
            return (value - ((OffsetUnit<?>) unit).getOffsetToStandardUnit())
                    * unit.getConversionFactorToStandardUnit();
        }
        return value * unit.getConversionFactorToStandardUnit();
    }

    /**
     * Convert a value from the standard SI unit into a compatible unit.
     * @param siValue double; the given value in the standard SI unit
     * @param targetUnit Unit&lt;?&gt;; the unit to convert the value into
     * @return double; the value in the targetUnit
     */
    public static double expressAsUnit(final double siValue, final Unit<?> targetUnit)
    {
        if (targetUnit instanceof OffsetUnit<?>)
        {
            return siValue / targetUnit.getConversionFactorToStandardUnit()
                    + ((OffsetUnit<?>) targetUnit).getOffsetToStandardUnit();
        }
        return siValue / targetUnit.getConversionFactorToStandardUnit();
    }

}

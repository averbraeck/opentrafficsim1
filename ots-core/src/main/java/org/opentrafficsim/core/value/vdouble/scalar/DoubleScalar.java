package org.opentrafficsim.core.value.vdouble.scalar;

import org.opentrafficsim.core.unit.Unit;
import org.opentrafficsim.core.value.Absolute;
import org.opentrafficsim.core.value.Relative;
import org.opentrafficsim.core.value.Scalar;
import org.opentrafficsim.core.value.ValueUtil;

/**
 * All calculations are according to IEEE 754. This means that division by zero results in Double.INFINITY, and some
 * calculations could result in NaN. No changes have been made to avoid this, as it is the standard behavior of Java for
 * floating point numbers.
 * <p>
 * Copyright (c) 2014 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * <p>
 * See for project information <a href="http://www.opentrafficsim.org/"> www.opentrafficsim.org</a>.
 * <p>
 * The OpenTrafficSim project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is" and any express or implied warranties,
 * including, but not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the copyright holder or contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or
 * services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability,
 * whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use
 * of this software, even if advised of the possibility of such damage.
 * @version Jun 13, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <U> the unit of the values in the constructor and for display
 */
public abstract class DoubleScalar<U extends Unit<U>> extends Scalar<U>
{
    /** */
    private static final long serialVersionUID = 20140618L;

    /**
     * @param unit
     */
    public DoubleScalar(U unit)
    {
        super(unit);
    }

    /** the value, stored in SI units. */
    protected double valueSI;

    /**
     * @param <U> Unit
     */
    public static class Abs<U extends Unit<U>> extends DoubleScalar<U> implements Absolute, Comparable<Abs<U>>
    {
        /** */
        private static final long serialVersionUID = 20140905L;

        /**
         * Create a new Absolute DoubleScalar.
         * @param value double; the value of the new Absolute DoubleScalar
         * @param unit Unit; the unit of the new Absolute DoubleScalar
         */
        public Abs(final double value, final U unit)
        {
            super(unit);
            // System.out.println("Created Abs");
            initialize(value);
        }

        /**
         * Create a new Absolute DoubleScalar from an existing one.
         * @param value Absolute DoubleScalar; the reference
         */
        public Abs(final DoubleScalar.Abs<U> value)
        {
            super(value.getUnit());
            // System.out.println("Created Abs");
            initialize(value);
        }

        /**
         * Create a new Absolute DoubleScalar from an existing Absolute MutableDoubleScalar.
         * @param value Absolute MutableDoubleScalar; the reference
         */
        public Abs(final MutableDoubleScalar.Abs<U> value)
        {
            super(value.getUnit());
            // System.out.println("Created Abs");
            initialize(value);
        }

        /**
         * Create an mutable version of this DoubleScalar
         * @return Absolute MutableDoubleScalar
         */
        @Override
        public MutableDoubleScalar.Abs<U> mutable()
        {
            return new MutableDoubleScalar.Abs<U>(this);
        }

        /**
         * @see org.opentrafficsim.core.value.vdouble.scalar.DoubleScalar#copy()
         */
        @Override
        public DoubleScalar.Abs<U> copy()
        {
            return this; // that was easy!
        }

        /**
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(Abs<U> o)
        {
            return new Double(this.valueSI).compareTo(o.valueSI);
        }

    }

    /**
     * @param <U> Unit
     */
    public static class Rel<U extends Unit<U>> extends DoubleScalar<U> implements Relative, Comparable<Rel<U>>
    {
        /** */
        private static final long serialVersionUID = 20140905L;

        /**
         * Create a new Relative DoubleScalar.
         * @param value double; the value of the new Relative DoubleScalar
         * @param unit Unit; the unit of the new Relative DoubleScalar
         */
        public Rel(final double value, final U unit)
        {
            super(unit);
            // System.out.println("Created Rel");
            initialize(value);
        }

        /**
         * Create a new Relative DoubleScalar from an existing one.
         * @param value Relative DoubleScalar; the reference
         */
        public Rel(final DoubleScalar.Rel<U> value)
        {
            super(value.getUnit());
            // System.out.println("Created Rel");
            initialize(value);
        }

        /**
         * Create a new Relative DoubleScalar from an existing Relative MutableDoubleScalar.
         * @param value Relative MutableDoubleScalar; the reference
         */
        public Rel(final MutableDoubleScalar.Rel<U> value)
        {
            super(value.getUnit());
            // System.out.println("Created Abs");
            initialize(value);
        }

        /**
         * Create a mutable version.
         * @return Relative MutableDoubleScalar
         */
        @Override
        public MutableDoubleScalar.Rel<U> mutable()
        {
            return new MutableDoubleScalar.Rel<U>(this);
        }

        /**
         * @see org.opentrafficsim.core.value.vdouble.scalar.DoubleScalar#copy()
         */
        @Override
        public DoubleScalar.Rel<U> copy()
        {
            return this; // that was easy!
        }

        /**
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(Rel<U> o)
        {
            return new Double(this.valueSI).compareTo(o.valueSI);
        }

    }

    /**
     * Initialize the valueSI field (performing conversion to the SI standard unit if needed).
     * @param value double; the value in the unit of this DoubleScalar
     */
    protected void initialize(double value)
    {
        if (this.unit.equals(this.unit.getStandardUnit()))
        {
            this.valueSI = value;
        }
        else
        {
            this.valueSI = expressAsSIUnit(value);
        }
    }

    /**
     * Initialize the valueSI field. As the provided value is already in the SI standard unit, conversion is never
     * necessary.
     * @param value DoubleScalar; the value to use for initialization
     */
    protected void initialize(DoubleScalar<U> value)
    {
        this.valueSI = value.valueSI;
    }

    /**
     * @return value in SI units
     */
    public double getValueSI()
    {
        return this.valueSI;
    }

    /**
     * @return value in original units
     */
    public double getValueInUnit()
    {
        return expressAsSpecifiedUnit(this.valueSI);
    }

    /**
     * @param targetUnit the unit to convert the value to
     * @return value in specific target unit
     */
    public double getValueInUnit(final U targetUnit)
    {
        return ValueUtil.expressAsUnit(this.valueSI, targetUnit);
    }

    /**********************************************************************************/
    /******************************** NUMBER METHODS **********************************/
    /**********************************************************************************/

    /**
     * @see java.lang.Number#intValue()
     */
    @Override
    public int intValue()
    {
        return (int) Math.round(this.valueSI);
    }

    /**
     * @see java.lang.Number#longValue()
     */
    @Override
    public long longValue()
    {
        return Math.round(this.valueSI);
    }

    /**
     * @see java.lang.Number#floatValue()
     */
    @Override
    public float floatValue()
    {
        return (float) this.valueSI;
    }

    /**
     * @see java.lang.Number#doubleValue()
     */
    @Override
    public double doubleValue()
    {
        return this.valueSI;
    }

    /**
     * Create a mutable version of this DoubleScalar. <br />
     * The mutable version is created as a deep copy of this. Delayed copying is not worthwhile for a Scalar.
     * @return MutableDoubleScalar; mutable version of this DoubleScalar
     */
    public abstract MutableDoubleScalar<U> mutable();

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj)
    {
        // unequal if object is of a different type.
        if (!(obj instanceof DoubleScalar<?>))
            return false;
        DoubleScalar<?> fs = (DoubleScalar<?>) obj;

        // unequal if the SI unit type differs (km/h and m/s could have the same content, so that is allowed)
        if (!this.getUnit().getStandardUnit().equals(fs.getUnit().getStandardUnit()))
            return false;

        // unequal if one is absolute and the other is relative
        if (this.isAbsolute() != fs.isAbsolute() || this.isRelative() != fs.isRelative())
            return false;

        return this.valueSI == fs.valueSI;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return this.getValueInUnit() + " " + this.unit.getAbbreviationKey();
    }

    /**
     * @see org.opentrafficsim.core.value.Value#copy()
     */
    public abstract DoubleScalar<U> copy();

}
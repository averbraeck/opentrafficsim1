package org.opentrafficsim.core.value.vfloat.scalar;

import org.opentrafficsim.core.unit.SICoefficients;
import org.opentrafficsim.core.unit.SIUnit;
import org.opentrafficsim.core.unit.Unit;
import org.opentrafficsim.core.value.Absolute;
import org.opentrafficsim.core.value.Format;
import org.opentrafficsim.core.value.Relative;
import org.opentrafficsim.core.value.Scalar;
import org.opentrafficsim.core.value.ValueUtil;

/**
 * Immutable FloatScalar.
 * <p>
 * This file was generated by the OpenTrafficSim value classes generator, 26 jun, 2015
 * <p>
 * Copyright (c) 2015 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate$, @version $Revision$, by $Author: pknoppers
 * $, initial version 26 jun, 2015 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <U> Unit; the unit of this FloatScalar
 */
public abstract class FloatScalar<U extends Unit<U>> extends Scalar<U>
{
    /**  */
    private static final long serialVersionUID = 20150626L;

    /** The value, stored in the standard SI unit. */
    private float valueSI;

    /**
     * Construct a new Immutable FloatScalar.
     * @param unit U; the unit of the new FloatScalar
     */
    protected FloatScalar(final U unit)
    {
        super(unit);
        // System.out.println("Created FloatScalar");
    }

    /**
     * @param <U> Unit
     */
    public static class Abs<U extends Unit<U>> extends FloatScalar<U> implements Absolute, Comparable<Abs<U>>
    {
        /**  */
        private static final long serialVersionUID = 20150626L;

        /**
         * Construct a new Absolute Immutable FloatScalar.
         * @param value float; the value of the new Absolute Immutable FloatScalar
         * @param unit U; the unit of the new Absolute Immutable FloatScalar
         */
        public Abs(final float value, final U unit)
        {
            super(unit);
            // System.out.println("Created Abs");
            initialize(value);
        }

        /**
         * Construct a new Absolute Immutable FloatScalar from an existing Absolute Immutable FloatScalar.
         * @param value FloatScalar.Abs&lt;U&gt;; the reference
         */
        public Abs(final FloatScalar.Abs<U> value)
        {
            super(value.getUnit());
            // System.out.println("Created Abs");
            initialize(value);
        }

        /**
         * Construct a new Absolute Immutable FloatScalar from an existing Absolute MutableFloatScalar.
         * @param value MutableFloatScalar.Abs&lt;U&gt;; the reference
         */
        public Abs(final MutableFloatScalar.Abs<U> value)
        {
            super(value.getUnit());
            // System.out.println("Created Abs");
            initialize(value);
        }

        /** {@inheritDoc} */
        @Override
        public final MutableFloatScalar.Abs<U> mutable()
        {
            return new MutableFloatScalar.Abs<U>(this);
        }

        /** {@inheritDoc} */
        @Override
        public final int compareTo(final Abs<U> o)
        {
            return new Float(getSI()).compareTo(o.getSI());
        }

        /** {@inheritDoc} */
        @Override
        public final FloatScalar.Abs<U> copy()
        {
            return this;
        }

        /**
         * Test if this FloatScalar.Abs&lt;U&gt; is less than a FloatScalar.Abs&lt;U&gt;.
         * @param o FloatScalar.Abs&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean lt(final FloatScalar.Abs<U> o)
        {
            return this.getSI() < o.getSI();
        }

        /**
         * Test if this FloatScalar.Abs&lt;U&gt; is less than or equal to a FloatScalar.Abs&lt;U&gt;.
         * @param o FloatScalar.Abs&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean le(final FloatScalar.Abs<U> o)
        {
            return this.getSI() <= o.getSI();
        }

        /**
         * Test if this FloatScalar.Abs&lt;U&gt; is greater than or equal to a FloatScalar.Abs&lt;U&gt;.
         * @param o FloatScalar.Abs&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean gt(final FloatScalar.Abs<U> o)
        {
            return this.getSI() > o.getSI();
        }

        /**
         * Test if this FloatScalar.Abs&lt;U&gt; is greater than a FloatScalar.Abs&lt;U&gt;.
         * @param o FloatScalar.Abs&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean ge(final FloatScalar.Abs<U> o)
        {
            return this.getSI() >= o.getSI();
        }

        /**
         * Test if this FloatScalar.Abs&lt;U&gt; is equal to a FloatScalar.Abs&lt;U&gt;.
         * @param o FloatScalar.Abs&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean eq(final FloatScalar.Abs<U> o)
        {
            return this.getSI() == o.getSI();
        }

        /**
         * Test if this FloatScalar.Abs&lt;U&gt; is not equal to a FloatScalar.Abs&lt;U&gt;.
         * @param o FloatScalar.Abs&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean ne(final FloatScalar.Abs<U> o)
        {
            return this.getSI() != o.getSI();
        }

        /**
         * Test if this FloatScalar.Abs&lt;U&gt; is less than a MutableFloatScalar.Abs&lt;U&gt;.
         * @param o MutableFloatScalar.Abs&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean lt(final MutableFloatScalar.Abs<U> o)
        {
            return this.getSI() < o.getSI();
        }

        /**
         * Test if this FloatScalar.Abs&lt;U&gt; is less than or equal to a MutableFloatScalar.Abs&lt;U&gt;.
         * @param o MutableFloatScalar.Abs&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean le(final MutableFloatScalar.Abs<U> o)
        {
            return this.getSI() <= o.getSI();
        }

        /**
         * Test if this FloatScalar.Abs&lt;U&gt; is greater than or equal to a MutableFloatScalar.Abs&lt;U&gt;.
         * @param o MutableFloatScalar.Abs&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean gt(final MutableFloatScalar.Abs<U> o)
        {
            return this.getSI() > o.getSI();
        }

        /**
         * Test if this FloatScalar.Abs&lt;U&gt; is greater than a MutableFloatScalar.Abs&lt;U&gt;.
         * @param o MutableFloatScalar.Abs&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean ge(final MutableFloatScalar.Abs<U> o)
        {
            return this.getSI() >= o.getSI();
        }

        /**
         * Test if this FloatScalar.Abs&lt;U&gt; is equal to a MutableFloatScalar.Abs&lt;U&gt;.
         * @param o MutableFloatScalar.Abs&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean eq(final MutableFloatScalar.Abs<U> o)
        {
            return this.getSI() == o.getSI();
        }

        /**
         * Test if this FloatScalar.Abs&lt;U&gt; is not equal to a MutableFloatScalar.Abs&lt;U&gt;.
         * @param o MutableFloatScalar.Abs&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean ne(final MutableFloatScalar.Abs<U> o)
        {
            return this.getSI() != o.getSI();
        }

    }

    /**
     * @param <U> Unit
     */
    public static class Rel<U extends Unit<U>> extends FloatScalar<U> implements Relative, Comparable<Rel<U>>
    {
        /**  */
        private static final long serialVersionUID = 20150626L;

        /**
         * Construct a new Relative Immutable FloatScalar.
         * @param value float; the value of the new Relative Immutable FloatScalar
         * @param unit U; the unit of the new Relative Immutable FloatScalar
         */
        public Rel(final float value, final U unit)
        {
            super(unit);
            // System.out.println("Created Rel");
            initialize(value);
        }

        /**
         * Construct a new Relative Immutable FloatScalar from an existing Relative Immutable FloatScalar.
         * @param value FloatScalar.Rel&lt;U&gt;; the reference
         */
        public Rel(final FloatScalar.Rel<U> value)
        {
            super(value.getUnit());
            // System.out.println("Created Rel");
            initialize(value);
        }

        /**
         * Construct a new Relative Immutable FloatScalar from an existing Relative MutableFloatScalar.
         * @param value MutableFloatScalar.Rel&lt;U&gt;; the reference
         */
        public Rel(final MutableFloatScalar.Rel<U> value)
        {
            super(value.getUnit());
            // System.out.println("Created Rel");
            initialize(value);
        }

        /** {@inheritDoc} */
        @Override
        public final MutableFloatScalar.Rel<U> mutable()
        {
            return new MutableFloatScalar.Rel<U>(this);
        }

        /** {@inheritDoc} */
        @Override
        public final int compareTo(final Rel<U> o)
        {
            return new Float(getSI()).compareTo(o.getSI());
        }

        /** {@inheritDoc} */
        @Override
        public final FloatScalar.Rel<U> copy()
        {
            return this;
        }

        /**
         * Test if this FloatScalar.Rel&lt;U&gt; is less than a FloatScalar.Rel&lt;U&gt;.
         * @param o FloatScalar.Rel&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean lt(final FloatScalar.Rel<U> o)
        {
            return this.getSI() < o.getSI();
        }

        /**
         * Test if this FloatScalar.Rel&lt;U&gt; is less than or equal to a FloatScalar.Rel&lt;U&gt;.
         * @param o FloatScalar.Rel&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean le(final FloatScalar.Rel<U> o)
        {
            return this.getSI() <= o.getSI();
        }

        /**
         * Test if this FloatScalar.Rel&lt;U&gt; is greater than or equal to a FloatScalar.Rel&lt;U&gt;.
         * @param o FloatScalar.Rel&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean gt(final FloatScalar.Rel<U> o)
        {
            return this.getSI() > o.getSI();
        }

        /**
         * Test if this FloatScalar.Rel&lt;U&gt; is greater than a FloatScalar.Rel&lt;U&gt;.
         * @param o FloatScalar.Rel&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean ge(final FloatScalar.Rel<U> o)
        {
            return this.getSI() >= o.getSI();
        }

        /**
         * Test if this FloatScalar.Rel&lt;U&gt; is equal to a FloatScalar.Rel&lt;U&gt;.
         * @param o FloatScalar.Rel&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean eq(final FloatScalar.Rel<U> o)
        {
            return this.getSI() == o.getSI();
        }

        /**
         * Test if this FloatScalar.Rel&lt;U&gt; is not equal to a FloatScalar.Rel&lt;U&gt;.
         * @param o FloatScalar.Rel&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean ne(final FloatScalar.Rel<U> o)
        {
            return this.getSI() != o.getSI();
        }

        /**
         * Test if this FloatScalar.Rel&lt;U&gt; is less than a MutableFloatScalar.Rel&lt;U&gt;.
         * @param o MutableFloatScalar.Rel&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean lt(final MutableFloatScalar.Rel<U> o)
        {
            return this.getSI() < o.getSI();
        }

        /**
         * Test if this FloatScalar.Rel&lt;U&gt; is less than or equal to a MutableFloatScalar.Rel&lt;U&gt;.
         * @param o MutableFloatScalar.Rel&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean le(final MutableFloatScalar.Rel<U> o)
        {
            return this.getSI() <= o.getSI();
        }

        /**
         * Test if this FloatScalar.Rel&lt;U&gt; is greater than or equal to a MutableFloatScalar.Rel&lt;U&gt;.
         * @param o MutableFloatScalar.Rel&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean gt(final MutableFloatScalar.Rel<U> o)
        {
            return this.getSI() > o.getSI();
        }

        /**
         * Test if this FloatScalar.Rel&lt;U&gt; is greater than a MutableFloatScalar.Rel&lt;U&gt;.
         * @param o MutableFloatScalar.Rel&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean ge(final MutableFloatScalar.Rel<U> o)
        {
            return this.getSI() >= o.getSI();
        }

        /**
         * Test if this FloatScalar.Rel&lt;U&gt; is equal to a MutableFloatScalar.Rel&lt;U&gt;.
         * @param o MutableFloatScalar.Rel&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean eq(final MutableFloatScalar.Rel<U> o)
        {
            return this.getSI() == o.getSI();
        }

        /**
         * Test if this FloatScalar.Rel&lt;U&gt; is not equal to a MutableFloatScalar.Rel&lt;U&gt;.
         * @param o MutableFloatScalar.Rel&lt;U&gt;; the right hand side operand of the comparison
         * @return boolean
         */
        public final boolean ne(final MutableFloatScalar.Rel<U> o)
        {
            return this.getSI() != o.getSI();
        }

    }

    /**
     * Create a mutable version of this FloatScalar. <br>
     * The mutable version is created as a deep copy of this. Delayed copying is not worthwhile for a Scalar.
     * @return MutableFloatScalar&lt;U&gt;
     */
    public abstract MutableFloatScalar<U> mutable();

    /**
     * Initialize the valueSI field (performing conversion to the SI standard unit if needed).
     * @param value float; the value in the unit of this FloatScalar
     */
    protected final void initialize(final float value)
    {
        if (this.getUnit().equals(this.getUnit().getStandardUnit()))
        {
            setValueSI(value);
        }
        else
        {
            setValueSI((float) expressAsSIUnit(value));
        }
    }

    /**
     * Initialize the valueSI field. As the provided value is already in the SI standard unit, conversion is never
     * necessary.
     * @param value FloatScalar&lt;U&gt;; the value to use for initialization
     */
    protected final void initialize(final FloatScalar<U> value)
    {
        setValueSI(value.getSI());
    }

    /**
     * Retrieve the value in the underlying SI unit.
     * @return float
     */
    public final float getSI()
    {
        return this.valueSI;
    }

    /**
     * Set the value in the underlying SI unit.
     * @param value float; the new value in the underlying SI unit
     */
    protected final void setValueSI(final float value)
    {
        this.valueSI = value;
    }

    /**
     * Retrieve the value in the original unit.
     * @return float
     */
    public final float getInUnit()
    {
        return (float) expressAsSpecifiedUnit(getSI());
    }

    /**
     * Retrieve the value converted into some specified unit.
     * @param targetUnit U; the unit to convert the value into
     * @return float
     */
    public final float getInUnit(final U targetUnit)
    {
        return (float) ValueUtil.expressAsUnit(getSI(), targetUnit);
    }

    /**********************************************************************************/
    /********************************* NUMBER METHODS *********************************/
    /**********************************************************************************/

    /** {@inheritDoc} */
    @Override
    public final int intValue()
    {
        return Math.round(getSI());
    }

    /** {@inheritDoc} */
    @Override
    public final long longValue()
    {
        return Math.round(getSI());
    }

    /** {@inheritDoc} */
    @Override
    public final float floatValue()
    {
        return getSI();
    }

    /** {@inheritDoc} */
    @Override
    public final double doubleValue()
    {
        return getSI();
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        return toString(getUnit(), false, true);
    }

    /**
     * Print this FloatScalar with the value expressed in the specified unit.
     * @param displayUnit U; the unit into which the value is converted for display
     * @return String; printable string with the scalar contents expressed in the specified unit
     */
    public final String toString(final U displayUnit)
    {
        return toString(displayUnit, false, true);
    }

    /**
     * Print this FloatScalar with optional type and unit information.
     * @param verbose boolean; if true; include type info; if false; exclude type info
     * @param withUnit boolean; if true; include the unit; of false; exclude the unit
     * @return String; printable string with the scalar contents
     */
    public final String toString(final boolean verbose, final boolean withUnit)
    {
        return toString(getUnit(), verbose, withUnit);
    }

    /**
     * Print this FloatScalar with the value expressed in the specified unit.
     * @param displayUnit U; the unit into which the value is converted for display
     * @param verbose boolean; if true; include type info; if false; exclude type info
     * @param withUnit boolean; if true; include the unit; of false; exclude the unit
     * @return String; printable string with the scalar contents
     */
    public final String toString(final U displayUnit, final boolean verbose, final boolean withUnit)
    {
        StringBuffer buf = new StringBuffer();
        if (verbose)
        {
            if (this instanceof MutableFloatScalar)
            {
                buf.append("Mutable   ");
                if (this instanceof MutableFloatScalar.Abs)
                {
                    buf.append("Abs ");
                }
                else if (this instanceof MutableFloatScalar.Rel)
                {
                    buf.append("Rel ");
                }
                else
                {
                    buf.append("??? ");
                }
            }
            else
            {
                buf.append("Immutable ");
                if (this instanceof FloatScalar.Abs)
                {
                    buf.append("Abs ");
                }
                else if (this instanceof FloatScalar.Rel)
                {
                    buf.append("Rel ");
                }
                else
                {
                    buf.append("??? ");
                }
            }
        }
        float f = (float) ValueUtil.expressAsUnit(getSI(), displayUnit);
        buf.append(Format.format(f));
        if (withUnit)
        {
            buf.append(displayUnit.getAbbreviation());
        }
        return buf.toString();
    }

    /** {@inheritDoc} */
    @Override
    public final int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(this.valueSI);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public final boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof FloatScalar))
        {
            return false;
        }
        FloatScalar<?> other = (FloatScalar<?>) obj;
        // unequal if not both Absolute or both Relative
        if (this.isAbsolute() != other.isAbsolute() || this.isRelative() != other.isRelative())
        {
            return false;
        }
        // unequal if the standard SI units differ
        if (!this.getUnit().getStandardUnit().equals(other.getUnit().getStandardUnit()))
        {
            return false;
        }
        if (Float.floatToIntBits(this.valueSI) != Float.floatToIntBits(other.valueSI))
        {
            return false;
        }
        return true;
    }

    /**********************************************************************************/
    /********************************* STATIC METHODS *********************************/
    /**********************************************************************************/

    /**
     * Add a Relative value to an Absolute value. Return a new instance of the value. The unit of the return value will
     * be the unit of the left argument.
     * @param left FloatScalar.Abs&lt;U&gt;; the left argument
     * @param right FloatScalar.Rel&lt;U&gt;; the right argument
     * @param <U> Unit; the unit of the parameters and the result
     * @return MutableFloatScalar.Abs&lt;U&gt;; the sum of the values as an Absolute value
     */
    public static <U extends Unit<U>> MutableFloatScalar.Abs<U> plus(final FloatScalar.Abs<U> left,
            final FloatScalar.Rel<U> right)
    {
        MutableFloatScalar.Abs<U> result = new MutableFloatScalar.Abs<U>(left);
        result.incrementByImpl(right);
        return result;
    }

    /**
     * Add a Relative value to a Relative value. Return a new instance of the value. The unit of the return value will
     * be the unit of the left argument.
     * @param left FloatScalar.Rel&lt;U&gt;; the left argument
     * @param right FloatScalar.Rel&lt;U&gt;; the right argument
     * @param <U> Unit; the unit of the parameters and the result
     * @return MutableFloatScalar.Rel&lt;U&gt;; the sum of the values as a Relative value
     */
    public static <U extends Unit<U>> MutableFloatScalar.Rel<U> plus(final FloatScalar.Rel<U> left,
            final FloatScalar.Rel<U> right)
    {
        MutableFloatScalar.Rel<U> result = new MutableFloatScalar.Rel<U>(left);
        result.incrementByImpl(right);
        return result;
    }

    /**
     * Subtract a Relative value from an absolute value. Return a new instance of the value. The unit of the return
     * value will be the unit of the left argument.
     * @param left FloatScalar.Abs&lt;U&gt;; the left value
     * @param right FloatScalar.Rel&lt;U&gt;; the right value
     * @param <U> Unit; the unit of the parameters and the result
     * @return MutableFloatScalar.Abs&lt;U&gt;; the resulting value as an absolute value
     */
    public static <U extends Unit<U>> MutableFloatScalar.Abs<U> minus(final FloatScalar.Abs<U> left,
            final FloatScalar.Rel<U> right)
    {
        MutableFloatScalar.Abs<U> result = new MutableFloatScalar.Abs<U>(left);
        result.decrementByImpl(right);
        return result;
    }

    /**
     * Subtract a relative value from a relative value. Return a new instance of the value. The unit of the value will
     * be the unit of the first argument.
     * @param left FloatScalar.Rel&lt;U&gt;; the left value
     * @param right FloatScalar.Rel&lt;U&gt;; the right value
     * @param <U> Unit; the unit of the parameters and the result
     * @return MutableFloatScalar.Rel&lt;U&gt;; the resulting value as a relative value
     */
    public static <U extends Unit<U>> MutableFloatScalar.Rel<U> minus(final FloatScalar.Rel<U> left,
            final FloatScalar.Rel<U> right)
    {
        MutableFloatScalar.Rel<U> result = new MutableFloatScalar.Rel<U>(left);
        result.decrementByImpl(right);
        return result;
    }

    /**
     * Subtract two absolute values. Return a new instance of a relative value of the difference. The unit of the value
     * will be the unit of the first argument.
     * @param valueAbs1 FloatScalar.Abs&lt;U&gt;; value 1
     * @param valueAbs2 FloatScalar.Abs&lt;U&gt;; value 2
     * @param <U> Unit; the unit of the parameters and the result
     * @return MutableFloatScalar.Rel&lt;U&gt;; the difference of the two absolute values as a relative value
     */
    public static <U extends Unit<U>> MutableFloatScalar.Rel<U> minus(final FloatScalar.Abs<U> valueAbs1,
            final FloatScalar.Abs<U> valueAbs2)
    {
        MutableFloatScalar.Rel<U> result = new MutableFloatScalar.Rel<U>(valueAbs1.getInUnit(), valueAbs1.getUnit());
        result.decrementBy(valueAbs2);
        return result;
    }

    /**
     * Multiply two values; the result is a new instance with a different (existing or generated) SI unit.
     * @param left FloatScalar.Abs&lt;?&gt;; the left operand
     * @param right FloatScalar.Abs&lt;?&gt;; the right operand
     * @return MutableFloatScalar.Abs&lt;SIUnit&gt;; the product of the two values
     */
    public static MutableFloatScalar.Abs<SIUnit> multiply(final FloatScalar.Abs<?> left, final FloatScalar.Abs<?> right)
    {
        SIUnit targetUnit =
                Unit.lookupOrCreateSIUnitWithSICoefficients(SICoefficients.multiply(left.getUnit().getSICoefficients(),
                        right.getUnit().getSICoefficients()).toString());
        return new MutableFloatScalar.Abs<SIUnit>(left.getSI() * right.getSI(), targetUnit);
    }

    /**
     * Multiply two values; the result is a new instance with a different (existing or generated) SI unit.
     * @param left FloatScalar.Rel&lt;?&gt;; the left operand
     * @param right FloatScalar.Rel&lt;?&gt;; the right operand
     * @return MutableFloatScalar.Rel&lt;SIUnit&gt;; the product of the two values
     */
    public static MutableFloatScalar.Rel<SIUnit> multiply(final FloatScalar.Rel<?> left, final FloatScalar.Rel<?> right)
    {
        SIUnit targetUnit =
                Unit.lookupOrCreateSIUnitWithSICoefficients(SICoefficients.multiply(left.getUnit().getSICoefficients(),
                        right.getUnit().getSICoefficients()).toString());
        return new MutableFloatScalar.Rel<SIUnit>(left.getSI() * right.getSI(), targetUnit);
    }

    /**
     * Divide two values; the result is a new instance with a different (existing or generated) SI unit.
     * @param left FloatScalar.Abs&lt;?&gt;; the left operand
     * @param right FloatScalar.Abs&lt;?&gt;; the right operand
     * @return MutableFloatScalar.Abs&lt;SIUnit&gt;; the ratio of the two values
     */
    public static MutableFloatScalar.Abs<SIUnit> divide(final FloatScalar.Abs<?> left, final FloatScalar.Abs<?> right)
    {
        SIUnit targetUnit =
                Unit.lookupOrCreateSIUnitWithSICoefficients(SICoefficients.divide(left.getUnit().getSICoefficients(),
                        right.getUnit().getSICoefficients()).toString());
        return new MutableFloatScalar.Abs<SIUnit>(left.getSI() / right.getSI(), targetUnit);
    }

    /**
     * Divide two values; the result is a new instance with a different (existing or generated) SI unit.
     * @param left FloatScalar.Rel&lt;?&gt;; the left operand
     * @param right FloatScalar.Rel&lt;?&gt;; the right operand
     * @return MutableFloatScalar.Rel&lt;SIUnit&gt;; the ratio of the two values
     */
    public static MutableFloatScalar.Rel<SIUnit> divide(final FloatScalar.Rel<?> left, final FloatScalar.Rel<?> right)
    {
        SIUnit targetUnit =
                Unit.lookupOrCreateSIUnitWithSICoefficients(SICoefficients.divide(left.getUnit().getSICoefficients(),
                        right.getUnit().getSICoefficients()).toString());
        return new MutableFloatScalar.Rel<SIUnit>(left.getSI() / right.getSI(), targetUnit);
    }

    /**
     * Interpolate between or extrapolate over two values.
     * @param zero FloatScalar.Abs&lt;U&gt;; zero reference (returned when ratio == 0)
     * @param one FloatScalar.Abs&lt;U&gt;; one reference (returned when ratio == 1)
     * @param ratio float; the ratio that determines where between (or outside) zero and one the result lies
     * @param <U> Unit; the unit of the parameters and the result
     * @return MutableFloatScalar.Abs&lt;U&gt;
     */
    public static <U extends Unit<U>> MutableFloatScalar.Abs<U> interpolate(final FloatScalar.Abs<U> zero,
            final FloatScalar.Abs<U> one, final float ratio)
    {
        MutableFloatScalar.Abs<U> result = zero.mutable();
        result.setSI(result.getSI() * (1 - ratio) + one.getSI() * ratio);
        return result;
    }

    /**
     * Interpolate between or extrapolate over two values.
     * @param zero FloatScalar.Rel&lt;U&gt;; zero reference (returned when ratio == 0)
     * @param one FloatScalar.Rel&lt;U&gt;; one reference (returned when ratio == 1)
     * @param ratio float; the ratio that determines where between (or outside) zero and one the result lies
     * @param <U> Unit; the unit of the parameters and the result
     * @return MutableFloatScalar.Rel&lt;U&gt;
     */
    public static <U extends Unit<U>> MutableFloatScalar.Rel<U> interpolate(final FloatScalar.Rel<U> zero,
            final FloatScalar.Rel<U> one, final float ratio)
    {
        MutableFloatScalar.Rel<U> result = zero.mutable();
        result.setSI(result.getSI() * (1 - ratio) + one.getSI() * ratio);
        return result;
    }

}
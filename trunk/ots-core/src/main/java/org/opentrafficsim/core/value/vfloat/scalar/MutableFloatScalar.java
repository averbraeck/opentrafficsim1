package org.opentrafficsim.core.value.vfloat.scalar;

import org.opentrafficsim.core.unit.Unit;
import org.opentrafficsim.core.value.Absolute;
import org.opentrafficsim.core.value.Relative;
import org.opentrafficsim.core.value.ValueUtil;
import org.opentrafficsim.core.value.vfloat.FloatMathFunctions;

/**
 * MutableFloatScalar.
 * <p>
 * This file was generated by the OpenTrafficSim value classes generator, 30 dec, 2014
 * <p>
 * Copyright (c) 2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version 30 dec, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <U> Unit; the unit of this MutableFloatScalar
 */
public abstract class MutableFloatScalar<U extends Unit<U>> extends FloatScalar<U> implements FloatMathFunctions
{
    /**  */
    private static final long serialVersionUID = 20141230L;

    /**
     * Construct a new MutableFloatScalar.
     * @param unit U; the unit of the new MutableFloatScalar
     */
    protected MutableFloatScalar(final U unit)
    {
        super(unit);
        // System.out.println("Created MutableFloatScalar");
    }

    /**
     * @param <U> Unit
     */
    public static class Abs<U extends Unit<U>> extends MutableFloatScalar<U> implements Absolute, Comparable<Abs<U>>
    {
        /**  */
        private static final long serialVersionUID = 20141230L;

        /**
         * Construct a new Absolute MutableFloatScalar.
         * @param value float; the value of the new Absolute MutableFloatScalar
         * @param unit U; the unit of the new Absolute MutableFloatScalar
         */
        public Abs(final float value, final U unit)
        {
            super(unit);
            // System.out.println("Created Abs");
            initialize(value);
        }

        /**
         * Construct a new Absolute MutableFloatScalar from an existing Absolute Immutable FloatScalar.
         * @param value FloatScalar.Abs&lt;U&gt;; the reference
         */
        public Abs(final FloatScalar.Abs<U> value)
        {
            super(value.getUnit());
            // System.out.println("Created Abs");
            initialize(value);
        }

        /**
         * Construct a new Absolute MutableFloatScalar from an existing Absolute MutableFloatScalar.
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
        public final FloatScalar.Abs<U> immutable()
        {
            return new FloatScalar.Abs<U>(this);
        }

        /** {@inheritDoc} */
        @Override
        public final int compareTo(final Abs<U> o)
        {
            return new Float(getSI()).compareTo(o.getSI());
        }

        /** {@inheritDoc} */
        @Override
        public final MutableFloatScalar.Abs<U> copy()
        {
            return new MutableFloatScalar.Abs<U>(this);
        }

    }

    /**
     * @param <U> Unit
     */
    public static class Rel<U extends Unit<U>> extends MutableFloatScalar<U> implements Relative, Comparable<Rel<U>>
    {
        /**  */
        private static final long serialVersionUID = 20141230L;

        /**
         * Construct a new Relative MutableFloatScalar.
         * @param value float; the value of the new Relative MutableFloatScalar
         * @param unit U; the unit of the new Relative MutableFloatScalar
         */
        public Rel(final float value, final U unit)
        {
            super(unit);
            // System.out.println("Created Rel");
            initialize(value);
        }

        /**
         * Construct a new Relative MutableFloatScalar from an existing Relative Immutable FloatScalar.
         * @param value FloatScalar.Rel&lt;U&gt;; the reference
         */
        public Rel(final FloatScalar.Rel<U> value)
        {
            super(value.getUnit());
            // System.out.println("Created Rel");
            initialize(value);
        }

        /**
         * Construct a new Relative MutableFloatScalar from an existing Relative MutableFloatScalar.
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
        public final FloatScalar.Rel<U> immutable()
        {
            return new FloatScalar.Rel<U>(this);
        }

        /** {@inheritDoc} */
        @Override
        public final int compareTo(final Rel<U> o)
        {
            return new Float(getSI()).compareTo(o.getSI());
        }

        /** {@inheritDoc} */
        @Override
        public final MutableFloatScalar.Rel<U> copy()
        {
            return new MutableFloatScalar.Rel<U>(this);
        }

    }

    /**
     * Make (immutable) FloatScalar equivalent for any type of MutableFloatScalar. <br>
     * The immutable version is created as a deep copy of this. Delayed copying is not worthwhile for a Scalar.
     * @return FloatScalar&lt;U&gt;; immutable version of this FloatScalar
     */
    public abstract FloatScalar<U> immutable();

    /**
     * Replace the stored value by the supplied value which is expressed in the standard SI unit.
     * @param valueSI float; the value to store (value must already be in the standard SI unit)
     */
    final void setSI(final float valueSI)
    {
        setValueSI(valueSI);
    }

    /**
     * Replace the stored value by the supplied value.
     * @param value FloatScalar&lt;U&gt;; the strongly typed value to store
     */
    final void set(final FloatScalar<U> value)
    {
        setValueSI(value.getSI());
    }

    /**
     * Replace the stored value by the supplied value which can be expressed in any compatible unit.
     * @param value float; the value to store
     * @param valueUnit U; the unit of the supplied value
     */
    final void setInUnit(final float value, final U valueUnit)
    {
        setValueSI((float) ValueUtil.expressAsSIUnit(value, valueUnit));
    }

    /**********************************************************************************/
    /********************************** MATH METHODS **********************************/
    /**********************************************************************************/

    /** {@inheritDoc} */
    @Override
    public final void abs()
    {
        setValueSI(Math.abs(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void acos()
    {
        // TODO dimensionless result (SIUnit.ONE).
        setValueSI((float) Math.acos(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void asin()
    {
        // TODO dimensionless result (SIUnit.ONE).
        setValueSI((float) Math.asin(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void atan()
    {
        // TODO dimensionless result (SIUnit.ONE).
        setValueSI((float) Math.atan(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void cbrt()
    {
        // TODO dimension for all SI coefficients / 3.
        setValueSI((float) Math.cbrt(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void ceil()
    {
        setValueSI((float) Math.ceil(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void cos()
    {
        // TODO dimensionless result (SIUnit.ONE).
        setValueSI((float) Math.cos(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void cosh()
    {
        // TODO dimensionless result (SIUnit.ONE).
        setValueSI((float) Math.cosh(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void exp()
    {
        // TODO dimensionless result (SIUnit.ONE).
        setValueSI((float) Math.exp(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void expm1()
    {
        // TODO dimensionless result (SIUnit.ONE).
        setValueSI((float) Math.expm1(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void floor()
    {
        setValueSI((float) Math.floor(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void log()
    {
        // TODO dimensionless result (SIUnit.ONE).
        setValueSI((float) Math.log(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void log10()
    {
        // TODO dimensionless result (SIUnit.ONE).
        setValueSI((float) Math.log10(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void log1p()
    {
        // TODO dimensionless result (SIUnit.ONE).
        setValueSI((float) Math.log1p(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void pow(final double x)
    {
        // TODO SI unit with coefficients * x.
        setValueSI((float) Math.pow(getSI(), x));
    }

    /** {@inheritDoc} */
    @Override
    public final void rint()
    {
        setValueSI((float) Math.rint(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void round()
    {
        setValueSI(Math.round(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void signum()
    {
        // TODO dimensionless result (SIUnit.ONE).
        setValueSI(Math.signum(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void sin()
    {
        // TODO dimensionless result (SIUnit.ONE).
        setValueSI((float) Math.sin(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void sinh()
    {
        // TODO dimensionless result (SIUnit.ONE).
        setValueSI((float) Math.sinh(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void sqrt()
    {
        // TODO dimension for all SI coefficients / 2.
        setValueSI((float) Math.sqrt(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void tan()
    {
        // TODO dimensionless result (SIUnit.ONE).
        setValueSI((float) Math.tan(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void tanh()
    {
        // TODO dimensionless result (SIUnit.ONE).
        setValueSI((float) Math.tanh(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void toDegrees()
    {
        setValueSI((float) Math.toDegrees(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void toRadians()
    {
        setValueSI((float) Math.toRadians(getSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void inv()
    {
        // TODO negate all coefficients in the Unit.
        setValueSI(1.0f / getSI());
    }

    /** {@inheritDoc} */
    @Override
    public final void multiply(final float constant)
    {
        setValueSI(getSI() * constant);
    }

    /** {@inheritDoc} */
    @Override
    public final void divide(final float constant)
    {
        setValueSI(getSI() / constant);
    }

    /**********************************************************************************/
    /******************************* NON-STATIC METHODS *******************************/
    /**********************************************************************************/

    /**
     * Increment the value in this MutableFloatScalar by the value in a FloatScalar.
     * @param increment FloatScalar&lt;U&gt;; the amount by which to increment the value in this MutableFloatScalar
     * @return MutableFloatScalar&lt;U&gt;; this modified MutableFloatScalar
     */
    protected final MutableFloatScalar<U> incrementBy(final FloatScalar<U> increment)
    {
        setValueSI(getSI() + increment.getSI());
        return this;
    }

    /**
     * Decrement the value in this MutableFloatScalar by the value in a FloatScalar.
     * @param decrement FloatScalar&lt;U&gt;; the amount by which to decrement the value in this MutableFloatScalar
     * @return MutableFloatScalar&lt;U&gt;; this modified MutableFloatScalar
     */
    protected final MutableFloatScalar<U> decrementBy(final FloatScalar<U> decrement)
    {
        setValueSI(getSI() - decrement.getSI());
        return this;
    }

    /**
     * Increment the value in this MutableFloatScalar by the value in a Relative FloatScalar. <br>
     * Only Relative values are allowed; adding an Absolute value to an Absolute value is not allowed. Adding an Absolute value
     * to an existing Relative value would require the result to become Absolute, which is a type change that is impossible. For
     * that operation use a static method.
     * @param rel FloatScalar.Rel&lt;U&gt;; the Relative FloatScalar
     * @return MutableFloatScalar&lt;U&gt;; this modified MutableFloatScalar
     */
    public final MutableFloatScalar<U> incrementBy(final FloatScalar.Rel<U> rel)
    {
        setValueSI(getSI() + rel.getSI());
        return this;
    }

    /**
     * Decrement the value of a Relative FloatScalar from the value of this MutableFloatScalar. <br>
     * Only Relative values are allowed; subtracting an Absolute value from a Relative value is not allowed. Subtracting an
     * Absolute value from an existing Absolute value would require the result to become Relative, which is a type change that
     * is impossible. For that operation use a static method.
     * @param rel FloatScalar.Rel&lt;U&gt;; the Relative FloatScalar
     * @return MutableFloatScalar&lt;U&gt;; this modified MutableFloatScalar
     */
    public final MutableFloatScalar<U> decrementBy(final FloatScalar.Rel<U> rel)
    {
        setValueSI(getSI() - rel.getSI());
        return this;
    }

}

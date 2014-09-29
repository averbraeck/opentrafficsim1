package org.opentrafficsim.core.value.vfloat.vector;

import java.io.Serializable;

import org.opentrafficsim.core.unit.Unit;
import org.opentrafficsim.core.value.Absolute;
import org.opentrafficsim.core.value.AbstractValue;
import org.opentrafficsim.core.value.DenseData;
import org.opentrafficsim.core.value.Format;
import org.opentrafficsim.core.value.Relative;
import org.opentrafficsim.core.value.SparseData;
import org.opentrafficsim.core.value.ValueException;
import org.opentrafficsim.core.value.ValueUtil;
import org.opentrafficsim.core.value.vfloat.scalar.FloatScalar;

import cern.colt.matrix.tfloat.FloatMatrix1D;
import cern.colt.matrix.tfloat.impl.DenseFloatMatrix1D;
import cern.colt.matrix.tfloat.impl.SparseFloatMatrix1D;

/**
 * Immutable float vector.
 * <p>
 * Copyright (c) 2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version Jun 13, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <U> the unit for this FloatVector
 */
public abstract class FloatVector<U extends Unit<U>> extends AbstractValue<U> implements Serializable,
        ReadOnlyFloatVectorFunctions<U>
{
    /** */
    private static final long serialVersionUID = 20140618L;

    /** The internal storage for the vector; the values are stored in standard SI unit; storage can be dense or sparse. */
    private FloatMatrix1D vectorSI;

    /**
     * Construct a new Immutable FloatVector.
     * @param unit U; the unit of the new FloatVector
     */
    protected FloatVector(final U unit)
    {
        super(unit);
        // System.out.println("Created FloatVector");
    }

    /**
     * @param <U> Unit
     */
    public abstract static class Abs<U extends Unit<U>> extends FloatVector<U> implements Absolute
    {
        /** */
        private static final long serialVersionUID = 20140905L;

        /**
         * Construct a new Absolute Immutable FloatVector.
         * @param unit U; the unit of the new Absolute Immutable FloatVector
         */
        Abs(final U unit)
        {
            super(unit);
            // System.out.println("Created Abs");
        }

        /**
         * @param <U> Unit
         */
        public static class Dense<U extends Unit<U>> extends Abs<U> implements DenseData
        {
            /** */
            private static final long serialVersionUID = 20140905L;

            /**
             * Construct a new Absolute Dense Immutable FloatVector.
             * @param values float[]; the values of the entries in the new Absolute Dense Immutable FloatVector
             * @param unit U; the unit of the new Absolute Dense Immutable FloatVector
             */
            public Dense(final float[] values, final U unit)
            {
                super(unit);
                // System.out.println("Created Dense");
                initialize(values);
            }

            /**
             * Construct a new Absolute Dense Immutable FloatVector.
             * @param values FloatScalar.Abs<U>[]; the values of the entries in the new Absolute Dense Immutable FloatVector
             * @throws ValueException when values has zero entries
             */
            public Dense(final FloatScalar.Abs<U>[] values) throws ValueException
            {
                super(checkNonEmpty(values)[0].getUnit());
                // System.out.println("Created Dense");
                initialize(values);
            }

            /**
             * For package internal use only.
             * @param values FloatMatrix1D; the values of the entries in the new Absolute Dense Immutable FloatVector
             * @param unit U; the unit of the new Absolute Dense Immutable FloatVector
             */
            protected Dense(final FloatMatrix1D values, final U unit)
            {
                super(unit);
                // System.out.println("Created Dense");
                initialize(values); // shallow copy
            }

            /** {@inheritDoc} */
            @Override
            public final MutableFloatVector.Abs.Dense<U> mutable()
            {
                return new MutableFloatVector.Abs.Dense<U>(getVectorSI(), getUnit());
            }

            /** {@inheritDoc} */
            @Override
            protected final FloatMatrix1D createMatrix1D(final int size)
            {
                return new DenseFloatMatrix1D(size);
            }

            /** {@inheritDoc} */
            @Override
            public final FloatVector.Abs.Dense<U> copy()
            {
                return this; // That was easy...
            }

        }

        /**
         * @param <U> Unit
         */
        public static class Sparse<U extends Unit<U>> extends Abs<U> implements SparseData
        {
            /** */
            private static final long serialVersionUID = 20140905L;

            /**
             * Construct a new Absolute Sparse Immutable FloatVector.
             * @param values float[]; the values of the entries in the new Absolute Sparse Immutable FloatVector
             * @param unit U; the unit of the new Absolute Sparse Immutable FloatVector
             */
            public Sparse(final float[] values, final U unit)
            {
                super(unit);
                // System.out.println("Created Sparse");
                initialize(values);
            }

            /**
             * Construct a new Absolute Sparse Immutable FloatVector.
             * @param values FloatScalar.Abs<U>[]; the values of the entries in the new Absolute Sparse Immutable FloatVector
             * @throws ValueException when values has zero entries
             */
            public Sparse(final FloatScalar.Abs<U>[] values) throws ValueException
            {
                super(checkNonEmpty(values)[0].getUnit());
                // System.out.println("Created Sparse");
                initialize(values);
            }

            /**
             * For package internal use only.
             * @param values FloatMatrix1D; the values of the entries in the new Absolute Sparse Immutable FloatVector
             * @param unit U; the unit of the new Absolute Sparse Immutable FloatVector
             */
            protected Sparse(final FloatMatrix1D values, final U unit)
            {
                super(unit);
                // System.out.println("Created Sparse");
                initialize(values); // shallow copy
            }

            /** {@inheritDoc} */
            @Override
            public final MutableFloatVector.Abs.Sparse<U> mutable()
            {
                return new MutableFloatVector.Abs.Sparse<U>(getVectorSI(), getUnit());
            }

            /** {@inheritDoc} */
            @Override
            protected final FloatMatrix1D createMatrix1D(final int size)
            {
                return new SparseFloatMatrix1D(size);
            }

            /** {@inheritDoc} */
            @Override
            public final FloatVector.Abs.Sparse<U> copy()
            {
                return this; // That was easy...
            }

        }

        /** {@inheritDoc} */
        @Override
        public final FloatScalar.Abs<U> get(final int index) throws ValueException
        {
            return new FloatScalar.Abs<U>(getInUnit(index, getUnit()), getUnit());
        }

    }

    /**
     * @param <U> Unit
     */
    public abstract static class Rel<U extends Unit<U>> extends FloatVector<U> implements Relative
    {
        /** */
        private static final long serialVersionUID = 20140905L;

        /**
         * Construct a new Relative Immutable FloatVector.
         * @param unit U; the unit of the new Relative Immutable FloatVector
         */
        Rel(final U unit)
        {
            super(unit);
           // System.out.println("Created Rel");
        }

        /**
         * @param <U> Unit
         */
        public static class Dense<U extends Unit<U>> extends Rel<U> implements DenseData
        {
            /** */
            private static final long serialVersionUID = 20140905L;

            /**
             * Construct a new Relative Dense Immutable FloatVector.
             * @param values float[]; the values of the entries in the new Relative Dense Immutable FloatVector
             * @param unit U; the unit of the new Relative Dense Immutable FloatVector
             */
            public Dense(final float[] values, final U unit)
            {
                super(unit);
                // System.out.println("Created Dense");
                initialize(values);
            }

            /**
             * Construct a new Relative Dense Immutable FloatVector.
             * @param values FloatScalar.Rel[]; the values of the entries in the new Relative Dense Immutable FloatVector
             * @throws ValueException when values has zero entries
             */
            public Dense(final FloatScalar.Rel<U>[] values) throws ValueException
            {
                super(checkNonEmpty(values)[0].getUnit());
                // System.out.println("Created Dense");
                initialize(values);
            }

            /**
             * For package internal use only.
             * @param values FloatMatrix1D; the values of the entries in the new Relative Dense Immutable FloatVector
             * @param unit U; the unit of the new Relative Dense Immutable FloatVector
             */
            protected Dense(final FloatMatrix1D values, final U unit)
            {
                super(unit);
                // System.out.println("Created Dense");
                initialize(values); // shallow copy
            }

            /** {@inheritDoc} */
            @Override
            public final MutableFloatVector.Rel.Dense<U> mutable()
            {
                return new MutableFloatVector.Rel.Dense<U>(getVectorSI(), getUnit());
            }

            /** {@inheritDoc} */
            @Override
            protected final FloatMatrix1D createMatrix1D(final int size)
            {
                return new DenseFloatMatrix1D(size);
            }

            /** {@inheritDoc} */
            @Override
            public final FloatVector.Rel.Dense<U> copy()
            {
                return this; // That was easy...
            }

        }

        /**
         * @param <U> Unit
         */
        public static class Sparse<U extends Unit<U>> extends Rel<U> implements SparseData
        {
            /** */
            private static final long serialVersionUID = 20140905L;

            /**
             * Construct a new Relative Sparse Immutable FloatVector.
             * @param values float[]; the values of the entries in the new Relative Sparse Immutable FloatVector
             * @param unit U; the unit of the new Relative Sparse Immutable FloatVector
             */
            public Sparse(final float[] values, final U unit)
            {
                super(unit);
                // System.out.println("Created Sparse");
                initialize(values);
            }

            /**
             * Construct a new Relative Sparse Immutable FloatVector.
             * @param values FloatScalar.Rel; the values of the entries in the new Relative Sparse Immutable FloatVector
             * @throws ValueException when values has zero entries
             */
            public Sparse(final FloatScalar.Rel<U>[] values) throws ValueException
            {
                super(checkNonEmpty(values)[0].getUnit());
                // System.out.println("Created Sparse");
                initialize(values);
            }

            /**
             * For package internal use only.
             * @param values FloatMatrix1D; the values of the entries in the new Relative Sparse Immutable FloatVector
             * @param unit U; the unit of the new Relative Sparse Immutable FloatVector
             */
            protected Sparse(final FloatMatrix1D values, final U unit)
            {
                super(unit);
                // System.out.println("Created Sparse");
                initialize(values); // shallow copy
            }

            /** {@inheritDoc} */
            @Override
            public final MutableFloatVector.Rel.Sparse<U> mutable()
            {
                return new MutableFloatVector.Rel.Sparse<U>(getVectorSI(), getUnit());
            }

            /** {@inheritDoc} */
            @Override
            protected final FloatMatrix1D createMatrix1D(final int size)
            {
                return new SparseFloatMatrix1D(size);
            }

            /** {@inheritDoc} */
            @Override
            public final FloatVector.Rel.Sparse<U> copy()
            {
                return this; // That was easy...
            }

        }

        /** {@inheritDoc} */
        @Override
        public final FloatScalar.Rel<U> get(final int index) throws ValueException
        {
            return new FloatScalar.Rel<U>(getInUnit(index, getUnit()), getUnit());
        }

    }

    /**
     * Retrieve the internal data.
     * @return FloatMatrix1D; the data in the internal format
     */
    protected final FloatMatrix1D getVectorSI()
    {
        return this.vectorSI;
    }

    /**
     * Make a deep copy of the data (used ONLY in the MutableFloatVector sub class).
     */
    protected final void deepCopyData()
    {
        this.vectorSI = getVectorSI().copy(); // makes a deep copy, using multithreading
    }

    /**
     * Create a mutable version of this FloatVector. <br>
     * The mutable version is created with a shallow copy of the data and the internal copyOnWrite flag set. The first operation
     * in the mutable version that modifies the data shall trigger a deep copy of the data.
     * @return MutableFloatVector<U>; mutable version of this FloatVector
     */
    public abstract MutableFloatVector<U> mutable();

    /**
     * Import the values and convert them into the SI standard unit.
     * @param values float[]; an array of values
     */
    protected final void initialize(final float[] values)
    {
        this.vectorSI = createMatrix1D(values.length);
        if (getUnit().equals(getUnit().getStandardUnit()))
        {
            this.vectorSI.assign(values);
        }
        else
        {
            for (int index = 0; index < values.length; index++)
            {
                safeSet(index, (float) expressAsSIUnit(values[index]));
            }
        }
    }

    /**
     * Import the values from an existing FloatMatrix1D. This makes a shallow copy.
     * @param values FloatMatrix1D<U>; the values
     */
    protected final void initialize(final FloatMatrix1D values)
    {
        this.vectorSI = values;
    }

    /**
     * Construct the vector and store the values in the standard SI unit.
     * @param values FloatScalar<U>[]; an array of values
     * @throws ValueException when values is empty
     */
    protected final void initialize(final FloatScalar<U>[] values) throws ValueException
    {
        this.vectorSI = createMatrix1D(values.length);
        for (int index = 0; index < values.length; index++)
        {
            safeSet(index, values[index].getValueSI());
        }
    }

    /**
     * Create storage for the data. <br/>
     * This method must be implemented by each leaf class.
     * @param size int; the number of cells in the vector
     * @return FloatMatrix1D; an instance of the right type of matrix (absolute / relative, dense / sparse, etc.)
     */
    protected abstract FloatMatrix1D createMatrix1D(final int size);

    /**
     * Create a float[] array filled with the values in the standard SI unit.
     * @return float[]; array of values in the standard SI unit
     */
    public final float[] getValuesSI()
    {
        return this.vectorSI.toArray(); // this makes a deep copy
    }

    /**
     * Create a float[] array filled with the values in the original unit.
     * @return float[]; the values in the original unit
     */
    public final float[] getValuesInUnit()
    {
        return getValuesInUnit(getUnit());
    }

    /**
     * Create a float[] array filled with the values converted into a specified unit.
     * @param targetUnit U; the unit to convert the values into
     * @return float[]; the values converted into the specified unit
     */
    public final float[] getValuesInUnit(final U targetUnit)
    {
        float[] values = this.vectorSI.toArray();
        for (int i = 0; i < values.length; i++)
        {
            values[i] = (float) ValueUtil.expressAsUnit(values[i], targetUnit);
        }
        return values;
    }

    /** {@inheritDoc} */
    @Override
    public final int size()
    {
        return (int) this.vectorSI.size();
    }

    /** {@inheritDoc} */
    @Override
    public final float getSI(final int index) throws ValueException
    {
        checkIndex(index);
        return safeGet(index);
    }

    /** {@inheritDoc} */
    @Override
    public final float getInUnit(final int index) throws ValueException
    {
        return (float) expressAsSpecifiedUnit(getSI(index));
    }

    /** {@inheritDoc} */
    @Override
    public final float getInUnit(final int index, final U targetUnit) throws ValueException
    {
        return (float) ValueUtil.expressAsUnit(getSI(index), targetUnit);
    }

    /** {@inheritDoc} */
    @Override
    public final float zSum()
    {
        return this.vectorSI.zSum();
    }

    /** {@inheritDoc} */
    @Override
    public final int cardinality()
    {
        return this.vectorSI.cardinality();
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        return toString(getUnit());
    }

    /**
     * Print this FloatVector with the values expressed in the specified unit.
     * @param displayUnit the unit to display the vector in.
     * @return a printable String with the vector contents
     */
    public final String toString(final U displayUnit)
    {
        StringBuffer buf = new StringBuffer();
        if (this instanceof MutableFloatVector)
        {
            buf.append("Mutable   ");
            if (this instanceof MutableFloatVector.Abs.Dense)
            {
                buf.append("Abs Dense  ");
            }
            else if (this instanceof MutableFloatVector.Rel.Dense)
            {
                buf.append("Rel Dense  ");
            }
            else if (this instanceof MutableFloatVector.Abs.Sparse)
            {
                buf.append("Abs Sparse ");
            }
            else if (this instanceof MutableFloatVector.Rel.Sparse)
            {
                buf.append("Rel Sparse ");
            }
            else
            {
                buf.append("??? ");
            }
        }
        else
        {
            buf.append("Immutable ");
            if (this instanceof FloatVector.Abs.Dense)
            {
                buf.append("Abs Dense  ");
            }
            else if (this instanceof FloatVector.Rel.Dense)
            {
                buf.append("Rel Dense  ");
            }
            else if (this instanceof FloatVector.Abs.Sparse)
            {
                buf.append("Abs Sparse ");
            }
            else if (this instanceof FloatVector.Rel.Sparse)
            {
                buf.append("Rel Sparse ");
            }
            else
            {
                buf.append("??? ");
            }
        }
        buf.append("[" + displayUnit.getAbbreviation() + "]");
        for (int i = 0; i < this.vectorSI.size(); i++)
        {
            float f = (float) ValueUtil.expressAsUnit(safeGet(i), displayUnit);
            buf.append(" " + Format.format(f));
        }
        return buf.toString();
    }

    /**
     * Centralized size equality check.
     * @param other FloatVector&lt;?&gt;; other FloatVector
     * @throws ValueException when vectors have unequal size
     */
    protected final void checkSize(final FloatVector<?> other) throws ValueException
    {
        if (size() != other.size())
        {
            throw new ValueException("The vectors have different sizes: " + size() + " != " + other.size());
        }
    }

    /**
     * Centralized size equality check.
     * @param other float[]; array of float
     * @throws ValueException when vectors have unequal size
     */
    protected final void checkSize(final float[] other) throws ValueException
    {
        if (size() != other.length)
        {
            throw new ValueException("The vector and the array have different sizes: " + size() + " != " + other.length);
        }
    }

    /**
     * Check that a provided index is valid.
     * @param index integer; the value to check
     * @throws ValueException when index is invalid
     */
    protected final void checkIndex(final int index) throws ValueException
    {
        if (index < 0 || index >= this.vectorSI.size())
        {
            throw new ValueException("index out of range (valid range is 0.." + (this.vectorSI.size() - 1) + ", got " + index
                    + ")");
        }
    }

    /**
     * Retrieve a value in vectorSI without checking validity of the index.
     * @param index integer; the index
     * @return float; the value stored at that index
     */
    protected final float safeGet(final int index)
    {
        return this.vectorSI.getQuick(index);
    }

    /**
     * Modify a value in vectorSI without checking validity of the index.
     * @param index integer; the index
     * @param valueSI float; the new value for the entry in vectorSI
     */
    protected final void safeSet(final int index, final float valueSI)
    {
        this.vectorSI.setQuick(index, valueSI);
    }

    /**
     * Create a deep copy of the data.
     * @return FloatMatrix1D; deep copy of the data
     */
    protected final FloatMatrix1D deepCopyOfData()
    {
        return this.vectorSI.copy();
    }

    /**
     * Check that a provided array can be used to create some descendant of an FloatVector.
     * @param fsArray FloatScalar[]; the provided array
     * @return FloatScalar[]; the provided array
     * @throws ValueException when the proved array has length equal to 0
     * @param <U> Unit; the unit of the FloatScalar array
     */
    protected static <U extends Unit<U>> FloatScalar<U>[] checkNonEmpty(final FloatScalar<U>[] fsArray) throws ValueException
    {
        if (0 == fsArray.length)
        {
            throw new ValueException("Cannot create a FloatValue or MutableFloatValue from an empty array of FloatScalar");
        }
        return fsArray;
    }

    /** {@inheritDoc} */
    @Override
    public final int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.vectorSI.hashCode();
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
        if (!(obj instanceof FloatVector))
        {
            return false;
        }
        FloatVector<?> other = (FloatVector<?>) obj;
        // unequal if one is absolute and the other is relative
        if (this.isAbsolute() != other.isAbsolute() || this.isRelative() != other.isRelative())
        {
            return false;
        }
        // unequal if the SI unit type differs (km/h and m/s could have the same content, so that is allowed)
        if (!this.getUnit().getStandardUnit().equals(other.getUnit().getStandardUnit()))
        {
            return false;
        }
        // Colt's equals also tests the size of the vector
        if (!this.vectorSI.equals(other.vectorSI))
        {
            return false;
        }
        return true;
    }

}

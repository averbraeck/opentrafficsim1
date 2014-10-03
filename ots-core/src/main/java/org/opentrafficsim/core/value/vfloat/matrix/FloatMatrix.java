package org.opentrafficsim.core.value.vfloat.matrix;

import java.io.Serializable;

import org.opentrafficsim.core.unit.SICoefficients;
import org.opentrafficsim.core.unit.SIUnit;
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
import org.opentrafficsim.core.value.vfloat.vector.FloatVector;

import cern.colt.matrix.tfloat.FloatMatrix1D;
import cern.colt.matrix.tfloat.FloatMatrix2D;
import cern.colt.matrix.tfloat.algo.DenseFloatAlgebra;
import cern.colt.matrix.tfloat.algo.SparseFloatAlgebra;
import cern.colt.matrix.tfloat.impl.DenseFloatMatrix1D;
import cern.colt.matrix.tfloat.impl.DenseFloatMatrix2D;
import cern.colt.matrix.tfloat.impl.SparseFloatMatrix1D;
import cern.colt.matrix.tfloat.impl.SparseFloatMatrix2D;

/**
 * Immutable FloatMatrix.
 * <p>
 * Copyright (c) 2013-2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights
 * reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version Sep 9, 2014 <br>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <U> Unit; the unit for this FloatMatrix
 */
public abstract class FloatMatrix<U extends Unit<U>> extends AbstractValue<U> implements Serializable,
        ReadOnlyFloatMatrixFunctions<U>
{
    /** */
    private static final long serialVersionUID = 20140909L;

    /**
     * The internal storage for the matrix; internally the values are stored in standard SI unit; storage can be dense
     * or sparse.
     */
    private FloatMatrix2D matrixSI;

    /**
     * Construct a new Immutable FloatMatrix.
     * @param unit U; the unit of the new FloatMatrix
     */
    protected FloatMatrix(final U unit)
    {
        super(unit);
        // System.out.println("Created FloatMatrix");
    }

    /**
     * @param <U> Unit
     */
    public abstract static class Abs<U extends Unit<U>> extends FloatMatrix<U> implements Absolute
    {
        /** */
        private static final long serialVersionUID = 20140905L;

        /**
         * Construct a new Absolute Immutable FloatMatrix.
         * @param unit U; the unit of the new Absolute Immutable FloatMatrix
         */
        protected Abs(final U unit)
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
             * Construct a new Absolute Dense Immutable FloatMatrix.
             * @param values float[][]; the values of the entries in the new Absolute Dense Immutable FloatMatrix
             * @param unit U; the unit of the new Absolute Dense Immutable FloatMatrix
             * @throws ValueException when values is not rectangular
             */
            public Dense(final float[][] values, final U unit) throws ValueException
            {
                super(unit);
                // System.out.println("Created Dense");
                initialize(values);
            }

            /**
             * Construct a new Absolute Dense Immutable FloatMatrix.
             * @param values FloatScalar.Abs&lt;U&gt;[][]; the values of the entries in the new Absolute Dense Immutable
             *            FloatMatrix
             * @throws ValueException when values has zero entries, or is not rectangular
             */
            public Dense(final FloatScalar.Abs<U>[][] values) throws ValueException
            {
                super(checkNonEmpty(values)[0][0].getUnit());
                // System.out.println("Created Dense");
                initialize(values);
            }

            /**
             * For package internal use only.
             * @param values FloatMatrix2D; the values of the entries in the new Absolute Dense Immutable FloatMatrix
             * @param unit U; the unit of the new Absolute Dense Immutable FloatMatrix
             */
            protected Dense(final FloatMatrix2D values, final U unit)
            {
                super(unit);
                // System.out.println("Created Dense");
                initialize(values); // shallow copy
            }

            /** {@inheritDoc} */
            @Override
            public final MutableFloatMatrix.Abs.Dense<U> mutable()
            {
                return new MutableFloatMatrix.Abs.Dense<U>(getMatrixSI(), getUnit());
            }

            /** {@inheritDoc} */
            @Override
            protected final FloatMatrix2D createMatrix2D(final int rows, final int columns)
            {
                return new DenseFloatMatrix2D(rows, columns);
            }

            /** {@inheritDoc} */
            @Override
            public final FloatMatrix.Abs.Dense<U> copy()
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
             * Construct a new Absolute Sparse Immutable FloatMatrix.
             * @param values float[][]; the values of the entries in the new Absolute Sparse Immutable FloatMatrix
             * @param unit U; the unit of the new Absolute Sparse Immutable FloatMatrix
             * @throws ValueException when values is not rectangular
             */
            public Sparse(final float[][] values, final U unit) throws ValueException
            {
                super(unit);
                // System.out.println("Created Sparse");
                initialize(values);
            }

            /**
             * Construct a new Absolute Sparse Immutable FloatMatrix.
             * @param values FloatScalar.Abs&lt;U&gt;[][]; the values of the entries in the new Absolute Sparse
             *            Immutable FloatMatrix
             * @throws ValueException when values has zero entries, or is not rectangular
             */
            public Sparse(final FloatScalar.Abs<U>[][] values) throws ValueException
            {
                super(checkNonEmpty(values)[0][0].getUnit());
                // System.out.println("Created Sparse");
                initialize(values);
            }

            /**
             * For package internal use only.
             * @param values FloatMatrix2D; the values of the entries in the new Absolute Sparse Immutable FloatMatrix
             * @param unit U; the unit of the new Absolute Sparse Immutable FloatMatrix
             */
            protected Sparse(final FloatMatrix2D values, final U unit)
            {
                super(unit);
                // System.out.println("Created Sparse");
                initialize(values); // shallow copy
            }

            /** {@inheritDoc} */
            @Override
            public final MutableFloatMatrix.Abs.Sparse<U> mutable()
            {
                return new MutableFloatMatrix.Abs.Sparse<U>(getMatrixSI(), getUnit());
            }

            /** {@inheritDoc} */
            @Override
            protected final FloatMatrix2D createMatrix2D(final int rows, final int columns)
            {
                return new SparseFloatMatrix2D(rows, columns);
            }

            /** {@inheritDoc} */
            @Override
            public final FloatMatrix.Abs.Sparse<U> copy()
            {
                return this; // That was easy...
            }

        }

        /** {@inheritDoc} */
        @Override
        public final FloatScalar.Abs<U> get(final int row, final int column) throws ValueException
        {
            return new FloatScalar.Abs<U>(getInUnit(row, column, getUnit()), getUnit());
        }

    }

    /**
     * @param <U> Unit
     */
    public abstract static class Rel<U extends Unit<U>> extends FloatMatrix<U> implements Relative
    {
        /** */
        private static final long serialVersionUID = 20140905L;

        /**
         * Construct a new Relative Immutable FloatMatrix.
         * @param unit U; the unit of the new Relative Immutable FloatMatrix
         */
        protected Rel(final U unit)
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
             * Construct a new Relative Dense Immutable FloatMatrix.
             * @param values float[][]; the values of the entries in the new Relative Dense Immutable FloatMatrix
             * @param unit U; the unit of the new Relative Dense Immutable FloatMatrix
             * @throws ValueException when values is not rectangular
             */
            public Dense(final float[][] values, final U unit) throws ValueException
            {
                super(unit);
                // System.out.println("Created Dense");
                initialize(values);
            }

            /**
             * Construct a new Relative Dense Immutable FloatMatrix.
             * @param values FloatScalar.Rel&lt;U&gt;[][]; the values of the entries in the new Relative Dense Immutable
             *            FloatMatrix
             * @throws ValueException when values has zero entries, or is not rectangular
             */
            public Dense(final FloatScalar.Rel<U>[][] values) throws ValueException
            {
                super(checkNonEmpty(values)[0][0].getUnit());
                // System.out.println("Created Dense");
                initialize(values);
            }

            /**
             * For package internal use only.
             * @param values FloatMatrix2D; the values of the entries in the new Relative Dense Immutable FloatMatrix
             * @param unit U; the unit of the new Relative Dense Immutable FloatMatrix
             */
            protected Dense(final FloatMatrix2D values, final U unit)
            {
                super(unit);
                // System.out.println("Created Dense");
                initialize(values); // shallow copy
            }

            /** {@inheritDoc} */
            @Override
            public final MutableFloatMatrix.Rel.Dense<U> mutable()
            {
                return new MutableFloatMatrix.Rel.Dense<U>(getMatrixSI(), getUnit());
            }

            /** {@inheritDoc} */
            @Override
            protected final FloatMatrix2D createMatrix2D(final int rows, final int columns)
            {
                return new DenseFloatMatrix2D(rows, columns);
            }

            /** {@inheritDoc} */
            @Override
            public final FloatMatrix.Rel.Dense<U> copy()
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
             * Construct a new Relative Sparse Immutable FloatMatrix.
             * @param values float[][]; the values of the entries in the new Relative Sparse Immutable FloatMatrix
             * @param unit U; the unit of the new Relative Sparse Immutable FloatMatrix
             * @throws ValueException when values is not rectangular
             */
            public Sparse(final float[][] values, final U unit) throws ValueException
            {
                super(unit);
                // System.out.println("Created Sparse");
                initialize(values);
            }

            /**
             * Construct a new Relative Sparse Immutable FloatMatrix.
             * @param values FloatScalar.Rel&lt;U&gt;[][]; the values of the entries in the new Relative Sparse
             *            Immutable FloatMatrix
             * @throws ValueException when values has zero entries, or is not rectangular
             */
            public Sparse(final FloatScalar.Rel<U>[][] values) throws ValueException
            {
                super(checkNonEmpty(values)[0][0].getUnit());
                // System.out.println("Created Sparse");
                initialize(values);
            }

            /**
             * For package internal use only.
             * @param values FloatMatrix2D; the values of the entries in the new Relative Sparse Immutable FloatMatrix
             * @param unit U; the unit of the new Relative Sparse Immutable FloatMatrix
             */
            protected Sparse(final FloatMatrix2D values, final U unit)
            {
                super(unit);
                // System.out.println("Created Sparse");
                initialize(values); // shallow copy
            }

            /** {@inheritDoc} */
            @Override
            public final MutableFloatMatrix.Rel.Sparse<U> mutable()
            {
                return new MutableFloatMatrix.Rel.Sparse<U>(getMatrixSI(), getUnit());
            }

            /** {@inheritDoc} */
            @Override
            protected final FloatMatrix2D createMatrix2D(final int rows, final int columns)
            {
                return new SparseFloatMatrix2D(rows, columns);
            }

            /** {@inheritDoc} */
            @Override
            public final FloatMatrix.Rel.Sparse<U> copy()
            {
                return this; // That was easy...
            }

        }

        /** {@inheritDoc} */
        @Override
        public final FloatScalar.Rel<U> get(final int row, final int column) throws ValueException
        {
            return new FloatScalar.Rel<U>(getInUnit(row, column, getUnit()), getUnit());
        }

    }

    /**
     * Retrieve the internal data.
     * @return FloatMatrix2D; the data in the internal format
     */
    protected final FloatMatrix2D getMatrixSI()
    {
        return this.matrixSI;
    }

    /**
     * Make a deep copy of the data (used ONLY in the MutableFloatMatrix sub class).
     */
    protected final void deepCopyData()
    {
        this.matrixSI = getMatrixSI().copy(); // makes a deep copy, using multithreading
    }

    /**
     * Create a mutable version of this FloatMatrix. <br>
     * The mutable version is created with a shallow copy of the data and the internal copyOnWrite flag set. The first
     * operation in the mutable version that modifies the data shall trigger a deep copy of the data.
     * @return MutableFloatMatrix&lt;U&gt;; mutable version of this FloatMatrix
     */
    public abstract MutableFloatMatrix<U> mutable();

    /**
     * Import the values and convert them into the SI standard unit.
     * @param values float[][]; an array of values
     * @throws ValueException when values is not rectangular
     */
    protected final void initialize(final float[][] values) throws ValueException
    {
        ensureRectangular(values);
        this.matrixSI = createMatrix2D(values.length, 0 == values.length ? 0 : values[0].length);
        if (getUnit().equals(getUnit().getStandardUnit()))
        {
            this.matrixSI.assign(values);
        }
        else
        {
            for (int row = values.length; --row >= 0;)
            {
                for (int column = values[row].length; --column >= 0;)
                {
                    safeSet(row, column, (float) expressAsSIUnit(values[row][column]));
                }
            }
        }
    }

    /**
     * Import the values from an existing FloatMatrix2D. This makes a shallow copy.
     * @param values FloatMatrix2D; the values
     */
    protected final void initialize(final FloatMatrix2D values)
    {
        this.matrixSI = values;
    }

    /**
     * Construct the matrix and store the values in the standard SI unit.
     * @param values FloatScalar&lt;U&gt;[][]; a 2D array of values
     * @throws ValueException when values is empty
     */
    protected final void initialize(final FloatScalar<U>[][] values) throws ValueException
    {
        ensureRectangularAndNonEmpty(values);
        this.matrixSI = createMatrix2D(values.length, values[0].length);
        for (int row = values.length; --row >= 0;)
        {
            for (int column = values[row].length; --column >= 0;)
            {
                safeSet(row, column, values[row][column].getValueSI());
            }
        }
    }

    /**
     * Create storage for the data. <br/>
     * This method must be implemented by each leaf class.
     * @param rows int; the number of rows in the matrix
     * @param columns int; the number of columns in the matrix
     * @return FloatMatrix2D; an instance of the right type of FloatMatrix2D (absolute/relative, dense/sparse, etc.)
     */
    protected abstract FloatMatrix2D createMatrix2D(final int rows, final int columns);

    /**
     * Create a float[][] array filled with the values in the standard SI unit.
     * @return float[][]; array of values in the standard SI unit
     */
    public final float[][] getValuesSI()
    {
        return this.matrixSI.toArray(); // this makes a deep copy
    }

    /**
     * Create a float[][] array filled with the values in the original unit.
     * @return float[][]; the values in the original unit
     */
    public final float[][] getValuesInUnit()
    {
        return getValuesInUnit(getUnit());
    }

    /**
     * Create a float[][] array filled with the values converted into a specified unit.
     * @param targetUnit U; the unit into which the values are converted for use
     * @return float[][]; the values converted into the specified unit
     */
    public final float[][] getValuesInUnit(final U targetUnit)
    {
        float[][] values = this.matrixSI.toArray();
        for (int row = rows(); --row >= 0;)
        {
            for (int column = columns(); --column >= 0;)
            {
                values[row][column] = (float) ValueUtil.expressAsUnit(values[row][column], targetUnit);
            }
        }
        return values;
    }

    /** {@inheritDoc} */
    @Override
    public final int rows()
    {
        return this.matrixSI.rows();
    }

    /** {@inheritDoc} */
    @Override
    public final int columns()
    {
        return this.matrixSI.columns();
    }

    /** {@inheritDoc} */
    @Override
    public final float getSI(final int row, final int column) throws ValueException
    {
        checkIndex(row, column);
        return safeGet(row, column);
    }

    /** {@inheritDoc} */
    @Override
    public final float getInUnit(final int row, final int column) throws ValueException
    {
        return (float) expressAsSpecifiedUnit(getSI(row, column));
    }

    /** {@inheritDoc} */
    @Override
    public final float getInUnit(final int row, final int column, final U targetUnit) throws ValueException
    {
        return (float) ValueUtil.expressAsUnit(getSI(row, column), targetUnit);
    }

    /** {@inheritDoc} */
    @Override
    public final float zSum()
    {
        return this.matrixSI.zSum();
    }

    /** {@inheritDoc} */
    @Override
    public final int cardinality()
    {
        return this.matrixSI.cardinality();
    }

    /** {@inheritDoc} */
    @Override
    public final float det() throws ValueException
    {
        try
        {
            if (this instanceof SparseData)
            {
                return new SparseFloatAlgebra().det(this.matrixSI);
            }
            if (this instanceof DenseData)
            {
                return new DenseFloatAlgebra().det(this.matrixSI);
            }
            throw new ValueException("FloatMatrix.det -- matrix implements neither Sparse nor Dense");
        }
        catch (IllegalArgumentException exception)
        {
            if (!exception.getMessage().startsWith("Matrix must be square"))
            {
                exception.printStackTrace();
            }
            throw new ValueException(exception.getMessage()); // probably Matrix must be square
        }
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        return toString(getUnit());
    }

    /**
     * Print this FloatMatrix with the values expressed in the specified unit.
     * @param displayUnit U; the unit into which the values are converted for display
     * @return String; printable string with the matrix contents
     */
    public final String toString(final U displayUnit)
    {
        StringBuffer buf = new StringBuffer();
        if (this instanceof MutableFloatMatrix)
        {
            buf.append("Mutable   ");
            if (this instanceof MutableFloatMatrix.Abs.Dense)
            {
                buf.append("Abs Dense  ");
            }
            else if (this instanceof MutableFloatMatrix.Rel.Dense)
            {
                buf.append("Rel Dense  ");
            }
            else if (this instanceof MutableFloatMatrix.Abs.Sparse)
            {
                buf.append("Abs Sparse ");
            }
            else if (this instanceof MutableFloatMatrix.Rel.Sparse)
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
            if (this instanceof FloatMatrix.Abs.Dense)
            {
                buf.append("Abs Dense  ");
            }
            else if (this instanceof FloatMatrix.Rel.Dense)
            {
                buf.append("Rel Dense  ");
            }
            else if (this instanceof FloatMatrix.Abs.Sparse)
            {
                buf.append("Abs Sparse ");
            }
            else if (this instanceof FloatMatrix.Rel.Sparse)
            {
                buf.append("Rel Sparse ");
            }
            else
            {
                buf.append("??? ");
            }
        }
        buf.append("[" + displayUnit.getAbbreviation() + "]");
        for (int row = 0; row < rows(); row++)
        {
            buf.append("\r\n\t");
            for (int column = 0; column < columns(); column++)
            {
                float f = (float) ValueUtil.expressAsUnit(safeGet(row, column), displayUnit);
                buf.append(" " + Format.format(f));
            }
        }
        return buf.toString();
    }

    /**
     * Centralized size equality check.
     * @param other FloatMatrix&lt;?&gt;; other FloatMatrix
     * @throws ValueException when matrices have unequal size
     */
    protected final void checkSize(final FloatMatrix<?> other) throws ValueException
    {
        if (rows() != other.rows() || columns() != other.columns())
        {
            throw new ValueException("The matrices have different sizes: " + rows() + "x" + columns() + " != "
                    + other.rows() + "x" + other.columns());
        }
    }

    /**
     * Centralized size equality check.
     * @param other float[][]; array of float
     * @throws ValueException when matrices have unequal size
     */
    protected final void checkSize(final float[][] other) throws ValueException
    {
        final int otherColumns = 0 == other.length ? 0 : other[0].length;
        if (rows() != other.length || columns() != otherColumns)
        {
            throw new ValueException("The matrix and the array have different sizes: " + rows() + "x" + columns()
                    + " != " + other.length + "x" + otherColumns);
        }
        ensureRectangular(other);
    }

    /**
     * Check that a 2D array of float is rectangular; i.e. all rows have the same length.
     * @param values float[][]; the 2D array to check
     * @throws ValueException when not all rows have the same length
     */
    private static void ensureRectangular(final float[][] values) throws ValueException
    {
        for (int row = values.length; --row >= 1;)
        {
            if (values[0].length != values[row].length)
            {
                throw new ValueException("Lengths of rows are not all the same");
            }
        }
    }

    /**
     * Check that a 2D array of FloatScalar&lt;?&gt; is rectangular; i.e. all rows have the same length and is non
     * empty.
     * @param values FloatScalar&lt;?&gt;[][]; the 2D array to check
     * @throws ValueException when not all rows have the same length, or contains no data
     */
    private static void ensureRectangularAndNonEmpty(final FloatScalar<?>[][] values) throws ValueException
    {
        if (0 == values.length || 0 == values[0].length)
        {
            throw new ValueException("Cannot determine unit for FloatMatrix from an empty array of FloatScalar");
        }
        for (int row = values.length; --row >= 1;)
        {
            if (values[0].length != values[row].length)
            {
                throw new ValueException("Lengths of rows are not all the same");
            }
        }
    }

    /**
     * Check that provided row and column indices are valid.
     * @param row int; the row value to check
     * @param column int; the column value to check
     * @throws ValueException when row or column is invalid
     */
    protected final void checkIndex(final int row, final int column) throws ValueException
    {
        if (row < 0 || row >= rows() || column < 0 || column >= columns())
        {
            throw new ValueException("index out of range (valid range is 0.." + (rows() - 1) + ", 0.."
                    + (columns() - 1) + ", got " + row + ", " + column + ")");
        }
    }

    /**
     * Retrieve a value in matrixSI without checking validity of the indices.
     * @param row int; the row where the value must be retrieved
     * @param column int; the column where the value must be retrieved
     * @return float; the value stored at the indicated row and column
     */
    protected final float safeGet(final int row, final int column)
    {
        return this.matrixSI.getQuick(row, column);
    }

    /**
     * Modify a value in matrixSI without checking validity of the indices.
     * @param row int; the row where the value must be stored
     * @param column int; the column where the value must be stored
     * @param valueSI float; the new value for the entry in matrixSI
     */
    protected final void safeSet(final int row, final int column, final float valueSI)
    {
        this.matrixSI.setQuick(row, column, valueSI);
    }

    /**
     * Create a deep copy of the data.
     * @return FloatMatrix2D; deep copy of the data
     */
    protected final FloatMatrix2D deepCopyOfData()
    {
        return this.matrixSI.copy();
    }

    /**
     * Check that a provided array can be used to create some descendant of a FloatMatrix.
     * @param fsArray FloatScalar&lt;U&gt;[][]; the provided array
     * @param <U> Unit; the unit of the FloatScalar array
     * @return FloatScalar&lt;U&gt;[][]; the provided array
     * @throws ValueException when the array has zero entries
     */
    protected static <U extends Unit<U>> FloatScalar<U>[][] checkNonEmpty(final FloatScalar<U>[][] fsArray)
            throws ValueException
    {
        if (0 == fsArray.length || 0 == fsArray[0].length)
        {
            throw new ValueException(
                    "Cannot create a FloatMatrix or MutableFloatMatrix from an empty array of FloatScalar");
        }
        return fsArray;
    }

    /**
     * Solve x for A*x = b. According to Colt: x; a new independent matrix; solution if A is square, least squares
     * solution if A.rows() &gt; A.columns(), underdetermined system solution if A.rows() &lt; A.columns().
     * @param A FloatMatrix&lt;?&gt;; matrix A in A*x = b
     * @param b FloatVector&lt;?&gt;; vector b in A*x = b
     * @return FloatVector&lt;SIUnit&gt;; vector x in A*x = b
     * @throws ValueException when matrix A is neither Sparse nor Dense
     */
    public static FloatVector<SIUnit> solve(final FloatMatrix<?> A, final FloatVector<?> b) throws ValueException
    {
        // TODO: is this correct? Should lookup matrix algebra to find out unit for x when solving A*x = b ?
        SIUnit targetUnit =
                Unit.lookupOrCreateSIUnitWithSICoefficients(SICoefficients.divide(b.getUnit().getSICoefficients(),
                        A.getUnit().getSICoefficients()).toString());

        // TODO: should the algorithm throw an exception when rows/columns do not match when solving A*x = b ?
        FloatMatrix2D A2D = A.getMatrixSI();
        if (A instanceof SparseData)
        {
            SparseFloatMatrix1D b1D = new SparseFloatMatrix1D(b.getValuesSI());
            FloatMatrix1D x1D = new SparseFloatAlgebra().solve(A2D, b1D);
            FloatVector.Abs.Sparse<SIUnit> x = new FloatVector.Abs.Sparse<SIUnit>(x1D.toArray(), targetUnit);
            return x;
        }
        if (A instanceof DenseData)
        {
            DenseFloatMatrix1D b1D = new DenseFloatMatrix1D(b.getValuesSI());
            FloatMatrix1D x1D = new DenseFloatAlgebra().solve(A2D, b1D);
            FloatVector.Abs.Dense<SIUnit> x = new FloatVector.Abs.Dense<SIUnit>(x1D.toArray(), targetUnit);
            return x;
        }
        throw new ValueException("FloatMatrix.det -- matrix implements neither Sparse nor Dense");
    }

    /** {@inheritDoc} */
    @Override
    public final int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.matrixSI.hashCode();
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
        if (!(obj instanceof FloatMatrix))
        {
            return false;
        }
        FloatMatrix<?> other = (FloatMatrix<?>) obj;
        // unequal if not both absolute or both relative
        if (this.isAbsolute() != other.isAbsolute() || this.isRelative() != other.isRelative())
        {
            return false;
        }
        // unequal if the standard SI units differ
        if (!this.getUnit().getStandardUnit().equals(other.getUnit().getStandardUnit()))
        {
            return false;
        }
        // Colt's equals also tests the size of the matrix
        if (!getMatrixSI().equals(other.getMatrixSI()))
        {
            return false;
        }
        return true;
    }

}

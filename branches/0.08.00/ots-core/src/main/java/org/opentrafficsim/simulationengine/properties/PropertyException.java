package org.opentrafficsim.simulationengine.properties;


/**
 * Exception thrown when an operation is attempted that is not compatible with the indicated property.
 * <p>
 * Copyright (c) 2013-2015 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate$, @version $Revision$, by $Author$,
 * initial version 18 dec. 2014 <br>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class PropertyException extends Exception
{
    /**  */
    private static final long serialVersionUID = 20141023L;

    /**
     * Construct a new IncompatiblePropertyException.
     */
    public PropertyException()
    {
        super();
    }

    /**
     * Construct a new IncompatiblePropertyException.
     * @param message String; description of the problem
     */
    public PropertyException(final String message)
    {
        super(message);
    }

    /**
     * Construct a new IncompatiblePropertyException.
     * @param cause Throwable; the cause of this ValueException
     */
    public PropertyException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * Construct a new IncompatiblePropertyException.
     * @param message String; description of the problem
     * @param cause Throwable; the cause of this ValueException
     */
    public PropertyException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Construct a new IncompatiblePropertyException.
     * @param message String; description of the problem
     * @param cause Throwable; the cause of this ValueException
     * @param enableSuppression boolean; whether or not suppression is enabled or disabled
     * @param writableStackTrace boolean; whether or not the stack trace should be writable
     */
    public PropertyException(final String message, final Throwable cause, final boolean enableSuppression,
        final boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Throw an Exception if a condition is met, e.g. for pre- and postcondition checking.
     * @param condition the condition to check; an exception will be thrown if this is <b>true</b>
     * @param message the message to use in the exception
     * @throws PropertyException the exception to throw on true condition
     */
    public static void failIf(final boolean condition, final String message) throws PropertyException
    {
        if (condition)
        {
            StackTraceElement[] ste = new Exception().getStackTrace();
            String where = ste[1].getClassName() + "." + ste[1].getMethodName() + " (" + ste[1].getLineNumber() + "): ";
            throw new PropertyException(where + message);
        }
    }
}

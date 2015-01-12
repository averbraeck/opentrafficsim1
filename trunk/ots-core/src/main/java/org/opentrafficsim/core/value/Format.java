package org.opentrafficsim.core.value;

/**
 * Format a floating point number in a reasonable way. <br>
 * I've experienced problems with the %g conversions that caused array bounds violations. Those versions of the JRE that do
 * <b>not</b> throw such Exceptions use one digit less than specified in the %g conversions. <br >
 * TODO check how to always format numbers corresponding to the Locale used.
 * <p>
 * This file was generated by the OpenTrafficSim value classes generator, 12 jan, 2015
 * <p>
 * Copyright (c) 2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version 12 jan, 2015 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public final class Format
{
    /**
     * This class shall never be instantiated.
     */
    private Format()
    {
        // Prevent instantiation of this class
    }

    /** Default total width of formatted value. */
    public static final int DEFAULTSIZE = 9;

    /** Default number of fraction digits. */
    public static final int DEFAULTPRECISION = 3;

    /**
     * Build a format string.
     * @param width int; the number of characters in the result
     * @param precision int; the number of fractional digits in the result
     * @param converter String; the format conversion specifier
     * @return String; suitable for formatting a float or double
     */
    private static String formatString(final int width, final int precision, final String converter)
    {
        return String.format("%%%d.%d%s", width, precision, converter);
    }

    /**
     * Format a floating point value.
     * @param value float; the value to format
     * @param width int; the number of characters in the result
     * @param precision int; the number of fractional digits in the result
     * @return String; the formatted floating point value
     */
    public static String format(final float value, final int width, final int precision)
    {
        if (0 == value || Math.abs(value) > 0.01 && Math.abs(value) < 9999.0)
        {
            return String.format(formatString(width, precision, "f"), value);
        }
        return String.format(formatString(width, precision, "e"), value);
    }

    /**
     * Format a floating point value.
     * @param value float; the value to format
     * @param size int; the number of characters in the result
     * @return String; the formatted floating point value
     */
    public static String format(final float value, final int size)
    {
        return Format.format(value, size, Format.DEFAULTPRECISION);
    }

    /**
     * Format a floating point value.
     * @param value float; the value to format
     * @return String; the formatted floating point value
     */
    public static String format(final float value)
    {
        return format(value, Format.DEFAULTSIZE, Format.DEFAULTPRECISION);
    }

    /**
     * Format a floating point value.
     * @param value double; the value to format
     * @param width int; the number of characters in the result
     * @param precision int; the number of fractional digits in the result
     * @return String; the formatted floating point value
     */
    public static String format(final double value, final int width, final int precision)
    {
        if (0 == value || Math.abs(value) > 0.01 && Math.abs(value) < 9999.0)
        {
            return String.format(formatString(width, precision, "f"), value);
        }
        return String.format(formatString(width, precision, "e"), value);
    }

    /**
     * Format a floating point value.
     * @param value double; the value to format
     * @param size int; the number of characters in the result
     * @return String; the formatted floating point value
     */
    public static String format(final double value, final int size)
    {
        return Format.format(value, size, Format.DEFAULTPRECISION);
    }

    /**
     * Format a floating point value.
     * @param value double; the value to format
     * @return String; the formatted floating point value
     */
    public static String format(final double value)
    {
        return format(value, Format.DEFAULTSIZE, Format.DEFAULTPRECISION);
    }

}

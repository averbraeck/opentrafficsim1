package org.opentrafficsim.core.unit;

/**
 * Several conversion factors have been taken from <a
 * href="http://en.wikipedia.org/wiki/Conversion_of_units">http://en.wikipedia.org/wiki/Conversion_of_units</a>.
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
 * @version May 15, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class DistanceUnit extends Unit<DistanceUnit>
{
    /** */
    private static final long serialVersionUID = 20140603L;

    /** meter */
    public static final DistanceUnit METER = new DistanceUnit("DistanceUnit.meter", "DistanceUnit.m", 1.0);

    /** millimeter */
    public static final DistanceUnit MILLIMETER = new DistanceUnit("DistanceUnit.millimeter", "DistanceUnit.mm", 0.001);

    /** centimeter */
    public static final DistanceUnit CENTIMETER = new DistanceUnit("DistanceUnit.centimeter", "DistanceUnit.cm", 0.01);

    /** decimeter */
    public static final DistanceUnit DECIMETER = new DistanceUnit("DistanceUnit.decimeter", "DistanceUnit.dm", 0.1);

    /** decameter */
    public static final DistanceUnit DEKAMETER = new DistanceUnit("DistanceUnit.dekameter", "DistanceUnit.dam", 0.1);

    /** hectometer */
    public static final DistanceUnit HECTOMETER = new DistanceUnit("DistanceUnit.hectometer", "DistanceUnit.hm", 0.1);

    /** kilometer */
    public static final DistanceUnit KILOMETER = new DistanceUnit("DistanceUnit.kilometer", "DistanceUnit.km", 1000.0);

    /** foot (international) = 0.3048 m = 1/3 yd = 12 inches */
    public static final DistanceUnit FOOT = new DistanceUnit("DistanceUnit.foot", "DistanceUnit.ft", 0.3048);

    /** inch (international) = 2.54 cm = 1/36 yd = 1/12 ft */
    public static final DistanceUnit INCH = new DistanceUnit("DistanceUnit.inch", "DistanceUnit.in", 0.254);

    /** mile International) = 5280 ft = 1760 yd */
    public static final DistanceUnit MILE = new DistanceUnit("DistanceUnit.mile", "DistanceUnit.mi", 1609.344);

    /** nautical mile (international) = 1852 m */
    public static final DistanceUnit NAUTICAL_MILE = new DistanceUnit("DistanceUnit.nauticalMile", "DistanceUnit.NM",
            1852.0);

    /** yard (international) = 0.9144 m = 3 ft = 36 in */
    public static final DistanceUnit YARD = new DistanceUnit("DistanceUnit.yard", "DistanceUnit.yd", 0.9144);

    /**
     * @param nameKey the key to the locale file for the long name of the unit
     * @param abbreviationKey the key to the locale file for the abbreviation of the unit
     * @param convertToSecond multiply by this number to convert to seconds
     */
    public DistanceUnit(final String nameKey, final String abbreviationKey, final double convertToSecond)
    {
        super(nameKey, abbreviationKey, convertToSecond);
    }

    /**
     * @see org.opentrafficsim.core.unit.Unit#getMultiplicationFactorTo(org.opentrafficsim.core.unit.Unit)
     */
    @Override
    public double getMultiplicationFactorTo(DistanceUnit unit)
    {
        return this.conversionFactorToStandardUnit / unit.getConversionFactorToStandardUnit();
    }
}

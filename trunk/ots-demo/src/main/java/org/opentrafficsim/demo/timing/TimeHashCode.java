package org.opentrafficsim.demo.timing;

import org.djunits.unit.LengthUnit;
import org.djunits.value.vdouble.scalar.Length;

/**
 * <p>
 * Copyright (c) 2013-2018 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/current/license.html">OpenTrafficSim License</a>.
 * </p>
 * $LastChangedDate: 2015-07-24 02:58:59 +0200 (Fri, 24 Jul 2015) $, @version $Revision: 1147 $, by $Author: averbraeck $,
 * initial version Apr 14, 2017 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public final class TimeHashCode
{

    /** */
    private TimeHashCode()
    {
        //
    }

    /**
     * @param args String[]; args should be empty
     */
    public static void main(final String[] args)
    {
        long t = System.currentTimeMillis();
        double d = 1.23;
        long l = 0;
        for (int i = 0; i < 1000000000; i++)
        {
            d += 1.234567;
            new Length(d, LengthUnit.METER);
            l++;
        }
        long t0 = System.currentTimeMillis() - t;
        t = System.currentTimeMillis();
        l = 0;
        d = 1.23;
        for (int i = 0; i < 1000000000; i++)
        {
            d += 1.234567;
            Length length = new Length(d, LengthUnit.METER);
            l += length.hashCode();
        }
        System.out.println("1 mld x Length.hashCode() = " + (System.currentTimeMillis() - t - t0) + " ms");
        System.out.println(l);
    }

}

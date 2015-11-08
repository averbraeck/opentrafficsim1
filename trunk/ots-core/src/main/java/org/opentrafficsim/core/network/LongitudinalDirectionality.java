package org.opentrafficsim.core.network;

/**
 * Permitted longitudinal driving directions.
 * <p>
 * Copyright (c) 2013-2015 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate$, @version $Revision$, by $Author$,
 * initial version Oct 15, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.citg.tudelft.nl">Guus Tamminga</a>
 */
public enum LongitudinalDirectionality
{
    /** Direction the same as the direction of the graph. */
    FORWARD,
    /** Direction opposite to the direction of the graph. */
    BACKWARD,
    /** Bidirectional. */
    BOTH,
    /** No traffic possible. */
    NONE;

    /**
     * This method looks if this directionality "contains" the provided other directionality. The logic table looks as follows:
     * <table border="1" summary="">
     * <tr>
     * <td><b>THIS &darr; &nbsp; OTHER &rarr;</b></td>
     * <td><b>BOTH&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></td>
     * <td><b>FORWARD</b></td>
     * <td><b>BACKWARD</b></td>
     * <td><b>NONE&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></td>
     * </tr>
     * <tr>
     * <td><b>BOTH</b></td>
     * <td>true</td>
     * <td>true</td>
     * <td>true</td>
     * <td>true</td>
     * </tr>
     * <tr>
     * <td><b>FORWARD</b></td>
     * <td>false</td>
     * <td>true</td>
     * <td>false</td>
     * <td>true</td>
     * </tr>
     * <tr>
     * <td><b>BACKWARD</b></td>
     * <td>false</td>
     * <td>false</td>
     * <td>true</td>
     * <td>true</td>
     * </tr>
     * <tr>
     * <td><b>NONE</b></td>
     * <td>false</td>
     * <td>false</td>
     * <td>false</td>
     * <td>true</td>
     * </tr>
     * </table>
     * @param directionality the directionality to compare with
     * @return whether this directionality "contains" the provided other directionality
     */
    public final boolean contains(final LongitudinalDirectionality directionality)
    {
        return (this.equals(directionality) || this.equals(BOTH) || directionality.equals(NONE)) ? true : false;
    }
    
    /**
     * Easy access method to test if the directionality is FORWARD or BOTH.
     * @return whether the directionality is FORWARD or BOTH
     */
    public final boolean isForwardOrBoth()
    {
        return this.equals(FORWARD) || this.equals(BOTH);
    }

    /**
     * Easy access method to test if the directionality is BACKWARD or BOTH.
     * @return whether the directionality is BACKWARD or BOTH
     */
    public final boolean isBackwardOrBoth()
    {
        return this.equals(BACKWARD) || this.equals(BOTH);
    }
}

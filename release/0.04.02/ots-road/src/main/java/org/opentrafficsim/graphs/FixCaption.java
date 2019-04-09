package org.opentrafficsim.graphs;

import org.jfree.chart.JFreeChart;

/**
 * Fix the font size of the caption of a JFreeChart.
 * <p>
 * Copyright (c) 2013-2015 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate: 2015-07-26 01:01:13 +0200 (Sun, 26 Jul 2015) $, @version $Revision: 1155 $, by $Author: averbraeck $,
 * initial version 19 dec. 2014 <br>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public final class FixCaption
{
    /**
     * This class shall never be instantiated.
     */
    private FixCaption()
    {
        // Prevent instantiation of this class
    }

    /**
     * Make the title text a little bit smaller.
     * @param chart JFreeChart; the chart of which to adjust the title size
     */
    static void fixCaption(final JFreeChart chart)
    {
        chart.setTitle(new org.jfree.chart.title.TextTitle(chart.getTitle().getText(), new java.awt.Font("SansSerif",
            java.awt.Font.BOLD, 16)));
    }
}
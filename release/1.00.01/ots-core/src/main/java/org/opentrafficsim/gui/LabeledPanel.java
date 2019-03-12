package org.opentrafficsim.gui;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * JPanel with an outline and a name.
 * <p>
 * Copyright (c) 2013-2018 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate$, @version $Revision$, by $Author$,
 * initial version 22 dec. 2014 <br>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class LabeledPanel extends JPanel
{
    /** */
    private static final long serialVersionUID = 20141222L;

    /**
     * Create a JPanel with border and caption.
     * @param caption String; the caption of the LabeledPanel
     */
    public LabeledPanel(final String caption)
    {
        super();
        setBorder(new TitledBorder(null, caption, TitledBorder.LEADING, TitledBorder.TOP, null, null));
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        return "LabeledPanel [caption=" + ((TitledBorder) getBorder()).getTitle() + "]";
    }
}
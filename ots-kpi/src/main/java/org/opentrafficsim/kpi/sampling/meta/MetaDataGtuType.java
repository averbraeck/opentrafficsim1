package org.opentrafficsim.kpi.sampling.meta;

import org.opentrafficsim.kpi.interfaces.GtuDataInterface;
import org.opentrafficsim.kpi.interfaces.GtuTypeDataInterface;

/**
 * Accepts trajectories with a GTUType included in a set in a query.
 * <p>
 * Copyright (c) 2013-2016 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Sep 22, 2016 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class MetaDataGtuType extends MetaDataType<GtuTypeDataInterface>
{

    /**
     * @param id id
     */
    public MetaDataGtuType(final String id)
    {
        super(id);
    }

    /** {@inheritDoc} */
    @Override
    public final GtuTypeDataInterface getValue(final GtuDataInterface gtu)
    {
        return gtu.getGtuTypeData();
    }
    
    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return "MetaDataGTUType: " + super.toString();
    }

}

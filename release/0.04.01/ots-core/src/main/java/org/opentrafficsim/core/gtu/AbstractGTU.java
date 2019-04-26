package org.opentrafficsim.core.gtu;

import org.opentrafficsim.core.network.route.RouteNavigator;

/**
 * Implements id, GtuType, Route and odometer.
 * <p>
 * Copyright (c) 2013-2015 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$,
 *          initial version Oct 22, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public abstract class AbstractGTU implements GTU
{
    /** */
    private static final long serialVersionUID = 20140822L;

    /** the id of the GTU. */
    private final String id;

    /** the type of GTU, e.g. TruckType, CarType, BusType. */
    private final GTUType gtuType;

    /** the route navigator to determine the route. */
    private RouteNavigator routeNavigator;

    /**
     * @param id the id of the GTU
     * @param gtuType the type of GTU, e.g. TruckType, CarType, BusType
     * @param routeNavigator RouteNavigator; the navigator that determines the route that the GTU will take
     * @throws GTUException when route is null
     */
    public AbstractGTU(final String id, final GTUType gtuType, final RouteNavigator routeNavigator) throws GTUException
    {
        super();
        this.id = id;
        this.gtuType = gtuType;
        this.routeNavigator = routeNavigator;
    }

    /** {@inheritDoc} */
    @Override
    public final String getId()
    {
        return this.id;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("checkstyle:designforextension")
    @Override
    public GTUType getGTUType()
    {
        return this.gtuType;
    }

    /** {@inheritDoc} */
    @Override
    public final RelativePosition getReference()
    {
        return RelativePosition.REFERENCE_POSITION;
    }

    /**
     * @return routeNavigator
     */
    public RouteNavigator getRouteNavigator()
    {
        return this.routeNavigator;
    }

    /**
     * @param routeNavigator set routeNavigator
     */
    public final void setRouteNavigator(final RouteNavigator routeNavigator)
    {
        this.routeNavigator = routeNavigator;
    }

}
package org.opentrafficsim.kpi.sampling.indicator;

import org.djunits.value.vdouble.scalar.DoubleScalarInterface;
import org.djunits.value.vdouble.scalar.Time;
import org.opentrafficsim.kpi.sampling.Query;

import nl.tudelft.simulation.language.Throw;

/**
 * Abstract indicator which stores the last calculated value and returns it in {@code getValue()} for an equal query, start time
 * and end time.
 * <p>
 * Copyright (c) 2013-2016 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version 1 okt. 2016 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <T> class of the value
 */
public abstract class AbstractIndicator<T extends DoubleScalarInterface>
{

    /** Last query. */
    private Query lastQuery;

    /** Last start time. */
    private Time lastStartTime;

    /** Last end time. */
    private Time lastEndTime;

    /** Last value. */
    private T lastValue;

    /**
     * Get value for given query until given time, returning earlier calculated value if possible.
     * @param query query
     * @param endTime start time of interval to calculate indicator over
     * @return value for given query
     */
    @SuppressWarnings("checkstyle:designforextension")
    public T getValue(final Query query, final Time endTime)
    {
        return getValue(query, Time.ZERO, endTime);
    }

    /**
     * Get value for given query over time interval, returning earlier calculated value if possible.
     * @param query query
     * @param startTime start time of interval to calculate indicator over
     * @param endTime start time of interval to calculate indicator over
     * @return value for given query
     */
    public final T getValue(final Query query, final Time startTime, final Time endTime)
    {
        Throw.whenNull(query, "Query may not be null.");
        Throw.whenNull(startTime, "Start time may not be null.");
        Throw.whenNull(endTime, "End time may not be null.");
        if (this.lastQuery == null || !this.lastQuery.equals(query) || !this.lastStartTime.equals(startTime)
                || !this.lastEndTime.equals(endTime))
        {
            this.lastQuery = query;
            this.lastStartTime = startTime;
            this.lastEndTime = endTime;
            this.lastValue = calculate(query, startTime, endTime);
        }
        return this.lastValue;
    }

    /**
     * Calculate value for given query until given time.
     * @param query query
     * @param endTime start time of interval to calculate indicator over
     * @return value for given query
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected T calculate(final Query query, final Time endTime)
    {
        return calculate(query, Time.ZERO, endTime);
    }

    /**
     * Calculate value for given query over time interval.
     * @param query query
     * @param startTime start time of interval to calculate indicator over
     * @param endTime start time of interval to calculate indicator over
     * @return value for given query
     */
    protected abstract T calculate(Query query, Time startTime, Time endTime);

}
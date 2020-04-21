package org.sim0mq.publisher;

import org.djunits.Throw;
import org.djutils.event.EventType;
import org.djutils.metadata.MetaData;
import org.djutils.metadata.ObjectDescriptor;
import org.djutils.serialization.EndianUtil;
import org.djutils.serialization.SerializationException;
import org.djutils.serialization.serializers.Pointer;
import org.djutils.serialization.serializers.Serializer;

/**
 * Convert one Event into an equivalent Sim0MQ Message, or back.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/current/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <Event>
 */
public class EventSerializer<Event extends org.djutils.event.Event> implements Serializer<Event>
{
    /** The type of event that this EventSerializer handles. */
    final EventType eventType;

    /**
     * Construct a new EventSerializer for the specified <code>EventType</code>.
     * @param eventType EventType; the event type
     */
    EventSerializer(final EventType eventType)
    {
        this.eventType = eventType;
    }

    /** {@inheritDoc} */
    @Override
    public int size(final Event event) throws SerializationException
    {
        EventType et = event.getType();
        Throw.when(et.equals(this.eventType), ClassCastException.class,
                "EventSerializer for " + this.eventType.getName() + " cannot handle event " + event);
        int result = et.getName().length();
        MetaData metaData = this.eventType.getMetaData();
        for (int index = 0; index < metaData.size(); index++)
        {
            
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public int sizeWithPrefix(final Event event) throws SerializationException
    {
        return 1 + size(event);
    }

    /** {@inheritDoc} */
    @Override
    public byte fieldType()
    {
        return 33;
    }

    /** {@inheritDoc} */
    @Override
    public void serialize(final Event object, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
            throws SerializationException
    {
        // TODO Auto-generated method stub

    }

    /** {@inheritDoc} */
    @Override
    public void serializeWithPrefix(final Event object, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
            throws SerializationException
    {
        // TODO Auto-generated method stub

    }

    /** {@inheritDoc} */
    @Override
    public Event deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
            throws SerializationException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String dataClassName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public final int getNumberOfDimensions()
    {
        return 0;
    }

}

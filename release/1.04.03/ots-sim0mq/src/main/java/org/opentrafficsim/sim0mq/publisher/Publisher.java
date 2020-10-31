package org.opentrafficsim.sim0mq.publisher;

import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.djunits.Throw;
import org.djutils.event.EventProducerInterface;
import org.djutils.metadata.MetaData;
import org.djutils.metadata.ObjectDescriptor;
import org.djutils.serialization.SerializationException;
import org.opentrafficsim.core.gtu.GTU;
import org.opentrafficsim.core.network.Link;
import org.opentrafficsim.core.network.Network;
import org.opentrafficsim.core.network.OTSNetwork;
import org.opentrafficsim.road.network.lane.CrossSectionLink;
import org.sim0mq.Sim0MQException;

/**
 * Publish all available transceivers for an OTS network to a Sim0MQ master and handle its requests. <br>
 * Example sequence of events: <br>
 * <ol>
 * <li>OTSNetwork is somehow constructed and then a Publisher for that network is constructed.</li>
 * <li>Sim0MQ master requests names of all available subscription handlers</li>
 * <li>Sim0MQ master decides that it wants all GTU MOVE events of all GTUs. To do that it needs to know about all GTUs when they
 * are created and about all GTUs that have already been created. The Sim0MQ master issues to the publisher a request to
 * subscribe to all NETWORK.GTU_ADD_EVENTs of the GTUs_in_network SubscriptionHandler</li>
 * <li>This Publisher requests the GTUs_in_network SubscriptionHandler to subscribe to the add events. From now on, the
 * GTUs_in_network SubscriptionHandler will receive these events generated by the OTSNetwork and transcribe those into a Sim0MQ
 * events which are transmitted to the Sim0MQ master.</li>
 * <li>Sim0MQ master requests publisher to list all the elements of the GTUs_in_network SubscriptionHandler</li>
 * <li>This Publisher calls the list method of the GTUs_in_network SubscriptionHandler which results in a list of all active
 * GTUs being sent to the Sim0MQ master</li>
 * <li>The Sim0MQ master requests this Publisher to create a subscription for the update events of the GTU_move
 * SubscriptionHandler, providing the GTU id as address. It does that once for every GTU id.</li>
 * <li>This Publishers creates the subscriptions. From now on any GTU.MOVE_EVENT event is transcribed by the GTU_move
 * SubscriptionHandler in to a corresponding Sim0MQ event and sent to the Sim0MQ master.</li>
 * </ol>
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate: 2020-02-13 11:08:16 +0100 (Thu, 13 Feb 2020) $, @version $Revision: 6383 $, by $Author: pknoppers $,
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Publisher extends AbstractTransceiver
{
    /** Map Publisher names to the corresponding Publisher object. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    final Map<String, SubscriptionHandler> subscriptionHandlerMap = new LinkedHashMap<>();

    /** The OTS network. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    final OTSNetwork network;

    /**
     * Construct a Publisher for an OTS network.
     * @param network OTSNetwork; the OTS network
     * @throws RemoteException ...
     */
    public Publisher(final OTSNetwork network) throws RemoteException
    {
        super("Publisher for " + Throw.whenNull(network, "Network may not be null").getId(),
                new MetaData("Publisher for " + network.getId(), "Publisher",
                        new ObjectDescriptor[] {new ObjectDescriptor("Name of subscription handler", "String", String.class)}),
                new MetaData("Subscription handlers", "Subscription handlers",
                        new ObjectDescriptor[] {new ObjectDescriptor("Name of subscription handler", "String", String.class)}));
        this.network = network;

        GTUIdTransceiver gtuIdTransceiver = new GTUIdTransceiver(network);
        GTUTransceiver gtuTransceiver = new GTUTransceiver(network, gtuIdTransceiver);
        SubscriptionHandler gtuSubscriptionHandler =
                new SubscriptionHandler("GTU move", gtuTransceiver, new LookupEventProducerInterface()
                {
                    @Override
                    public EventProducerInterface lookup(final Object[] address, final ReturnWrapper returnWrapper)
                            throws Sim0MQException, SerializationException
                    {
                        String bad = AbstractTransceiver.verifyMetaData(getAddressMetaData(), address);
                        if (bad != null)
                        {
                            returnWrapper.nack(bad);
                            return null;
                        }
                        EventProducerInterface result = network.getGTU((String) address[0]);
                        if (null == result)
                        {
                            returnWrapper.nack("No GTU with id \"" + address[0] + "\" found");
                        }
                        return result;
                    }

                    private final MetaData metaData = new MetaData("GTU Id", "GTU Id",
                            new ObjectDescriptor[] {new ObjectDescriptor("GTU ID", "GTU Id", String.class)});

                    @Override
                    public MetaData getAddressMetaData()
                    {
                        return this.metaData;
                    }
                }, null, null, GTU.MOVE_EVENT, null);
        addSubscriptionHandler(gtuSubscriptionHandler);
        addSubscriptionHandler(new SubscriptionHandler("GTUs in network", gtuIdTransceiver, new LookupEventProducerInterface()
        {
            @Override
            public EventProducerInterface lookup(final Object[] address, final ReturnWrapper returnWrapper)
                    throws Sim0MQException, SerializationException
            {
                String bad = AbstractTransceiver.verifyMetaData(getAddressMetaData(), address);
                if (bad != null)
                {
                    returnWrapper.nack(bad);
                    return null;
                }
                return network;
            }

            @Override
            public String toString()
            {
                return "Subscription handler for GTUs in network";
            }

            @Override
            public MetaData getAddressMetaData()
            {
                return MetaData.EMPTY;
            }
        }, Network.GTU_ADD_EVENT, Network.GTU_REMOVE_EVENT, null, gtuSubscriptionHandler));
        LinkIdTransceiver linkIdTransceiver = new LinkIdTransceiver(network);
        LinkTransceiver linkTransceiver = new LinkTransceiver(network, linkIdTransceiver);
        SubscriptionHandler linkSubscriptionHandler = new SubscriptionHandler("Link change", linkTransceiver, this.lookupLink,
                Link.GTU_ADD_EVENT, Link.GTU_REMOVE_EVENT, null, null);
        addSubscriptionHandler(linkSubscriptionHandler);
        addSubscriptionHandler(new SubscriptionHandler("Links in network", linkIdTransceiver, new LookupEventProducerInterface()
        {
            @Override
            public EventProducerInterface lookup(final Object[] address, final ReturnWrapper returnWrapper)
                    throws Sim0MQException, SerializationException
            {
                String bad = AbstractTransceiver.verifyMetaData(getAddressMetaData(), address);
                if (bad != null)
                {
                    returnWrapper.nack(bad);
                    return null;
                }
                return network;
            }

            @Override
            public String toString()
            {
                return "Subscription handler for Links in network";
            }

            @Override
            public MetaData getAddressMetaData()
            {
                return MetaData.EMPTY;
            }
        }, Network.LINK_ADD_EVENT, Network.LINK_REMOVE_EVENT, null, linkSubscriptionHandler));
        NodeIdTransceiver nodeIdTransceiver = new NodeIdTransceiver(network);
        NodeTransceiver nodeTransceiver = new NodeTransceiver(network, nodeIdTransceiver);
        // addTransceiver(nodeIdTransceiver);
        // addTransceiver(new NodeTransceiver(network, nodeIdTransceiver));
        SubscriptionHandler nodeSubscriptionHandler =
                new SubscriptionHandler("Node change", nodeTransceiver, new LookupEventProducerInterface()
                {
                    @Override
                    public EventProducerInterface lookup(final Object[] address, final ReturnWrapper returnWrapper)
                    {
                        return null; // Nodes do not emit events
                    }

                    @Override
                    public String toString()
                    {
                        return "Subscription handler for Node change";
                    }

                    private final MetaData metaData = new MetaData("Node Id", "Node Id",
                            new ObjectDescriptor[] {new ObjectDescriptor("Node ID", "Node Id", String.class)});

                    @Override
                    public MetaData getAddressMetaData()
                    {
                        return this.metaData;
                    }
                }, null, null, null, null);
        addSubscriptionHandler(nodeSubscriptionHandler);
        addSubscriptionHandler(new SubscriptionHandler("Nodes in network", nodeIdTransceiver, new LookupEventProducerInterface()
        {
            @Override
            public EventProducerInterface lookup(final Object[] address, final ReturnWrapper returnWrapper)
                    throws Sim0MQException, SerializationException
            {
                String bad = AbstractTransceiver.verifyMetaData(getAddressMetaData(), address);
                if (bad != null)
                {
                    returnWrapper.nack(bad);
                    return null;
                }
                return network;
            }

            @Override
            public String toString()
            {
                return "Subscription handler for Nodes in network";
            }

            @Override
            public MetaData getAddressMetaData()
            {
                return MetaData.EMPTY;
            }
        }, Network.NODE_ADD_EVENT, Network.NODE_REMOVE_EVENT, null, nodeSubscriptionHandler));
        SubscriptionHandler linkGTUIdSubscriptionHandler = new SubscriptionHandler("GTUs on Link",
                new LinkGTUIdTransceiver(network), this.lookupLink, Link.GTU_ADD_EVENT, Link.GTU_REMOVE_EVENT, null, null);
        addSubscriptionHandler(linkGTUIdSubscriptionHandler);
        addSubscriptionHandler(new SubscriptionHandler("Cross section elements on Link",
                new CrossSectionElementTransceiver(network), this.lookupLink, CrossSectionLink.LANE_ADD_EVENT,
                CrossSectionLink.LANE_REMOVE_EVENT, null, linkGTUIdSubscriptionHandler));
        // addTransceiver(new LaneGTUIdTransceiver(network));
        SimulatorStateTransceiver stt = new SimulatorStateTransceiver(network.getSimulator());
        SubscriptionHandler simulatorStateSubscriptionHandler = new SubscriptionHandler("Simulator running", stt,
                stt.getLookupEventProducerInterface(), null, null, SimulatorStateTransceiver.SIMULATOR_STATE_CHANGED, null);
        addSubscriptionHandler(simulatorStateSubscriptionHandler);

        addSubscriptionHandler(new SubscriptionHandler("", this, null, null, null, null, null)); // The meta transceiver
    }

    /** Lookup a CrossSectionLink in the network. */
    private LookupEventProducerInterface lookupLink = new LookupEventProducerInterface()
    {
        @Override
        public EventProducerInterface lookup(final Object[] address, final ReturnWrapper returnWrapper)
                throws IndexOutOfBoundsException, Sim0MQException, SerializationException
        {
            Throw.whenNull(address, "LookupLink requires the name of a link");
            Throw.when(address.length != 1 || !(address[1] instanceof String), IllegalArgumentException.class, "Bad address");
            Link link = Publisher.this.network.getLink((String) address[0]);
            if (null == link)
            {
                returnWrapper.nack("Network does not contain a Link with id " + address[0]);
                return null;
            }
            if (!(link instanceof EventProducerInterface))
            {
                returnWrapper.nack("Link \"" + address[0] + "\" is not able to handle subscriptions");
                return null;
            }
            return (CrossSectionLink) link;
        }

        @Override
        public String toString()
        {
            return "LookupProducerInterface that looks up a Link in the network";
        }

        @Override
        public MetaData getAddressMetaData()
        {
            return new MetaData("Link id", "Name of a link in the network",
                    new ObjectDescriptor[] {new ObjectDescriptor("Link id", "Name of a link in the network", String.class)});
        }
    };

    /**
     * Add a SubscriptionHandler to the map.
     * @param subscriptionHandler SubscriptionHandler; the subscription handler to add to the map
     */
    private void addSubscriptionHandler(final SubscriptionHandler subscriptionHandler)
    {
        this.subscriptionHandlerMap.put(subscriptionHandler.getId(), subscriptionHandler);
    }

    /** {@inheritDoc} */
    @Override
    public Object[] get(final Object[] address, final ReturnWrapper returnWrapper)
            throws Sim0MQException, SerializationException
    {
        Throw.whenNull(returnWrapper, "returnWrapper may not be null");
        String bad = verifyMetaData(getAddressFields(), address);
        if (bad != null)
        {
            returnWrapper.nack("Bad address (should be the name of a transceiver): " + bad);
            return null;
        }
        SubscriptionHandler subscriptionHandler = this.subscriptionHandlerMap.get(address[0]);
        if (null == subscriptionHandler)
        {
            returnWrapper.nack("No transceiver with name \"" + address[0] + "\"");
            return null;
        }
        return new Object[] {subscriptionHandler};
    }

    /** Returned by the getIdSource method. */
    private final TransceiverInterface idSource = new TransceiverInterface()
    {
        @Override
        public String getId()
        {
            return "Transceiver for names of available transceivers in Publisher";
        }

        @Override
        public MetaData getAddressFields()
        {
            return MetaData.EMPTY;
        }

        /** Result of getResultFields. */
        private MetaData resultMetaData =
                new MetaData("Transceiver names available in Publisher", "String array", new ObjectDescriptor[] {
                        new ObjectDescriptor("Transceiver names available in Publisher", "String array", String[].class)});

        @Override
        public MetaData getResultFields()
        {
            return this.resultMetaData;
        }

        @Override
        public Object[] get(final Object[] address, final ReturnWrapper returnWrapper)
                throws RemoteException, Sim0MQException, SerializationException
        {
            Object[] result = new Object[Publisher.this.subscriptionHandlerMap.size()];
            int index = 0;
            for (String key : Publisher.this.subscriptionHandlerMap.keySet())
            {
                result[index++] = key;
            }
            return result;
        }
    };

    /** {@inheritDoc} */
    @Override
    public TransceiverInterface getIdSource(final int addressLevel, final ReturnWrapper returnWrapper)
            throws Sim0MQException, SerializationException
    {
        if (0 != addressLevel)
        {
            returnWrapper.encodeReplyAndTransmit("Address should be 0");
            return null;
        }
        return this.idSource;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasIdSource()
    {
        return true;
    }

    /**
     * Execute one command.
     * @param subscriptionHandlerName String; name of the SubscriptionHandler for which the command is destined
     * @param command SubscriptionHandler.Command; the operation to perform
     * @param address Object[]; the address on which to perform the operation
     * @param returnWrapper ReturnWrapper; to transmit the result
     * @throws RemoteException on RMI network failure
     * @throws SerializationException on illegal type in serialization
     * @throws Sim0MQException on communication error
     */
    public void executeCommand(final String subscriptionHandlerName, final SubscriptionHandler.Command command,
            final Object[] address, final ReturnWrapper returnWrapper)
            throws RemoteException, Sim0MQException, SerializationException
    {
        SubscriptionHandler subscriptionHandler = this.subscriptionHandlerMap.get(subscriptionHandlerName);
        if (null == subscriptionHandler)
        {
            returnWrapper.nack("No subscription handler for \"" + subscriptionHandlerName + "\"");
            return;
        }
        subscriptionHandler.executeCommand(command, address, returnWrapper);
    }

    /**
     * Execute one command.
     * @param subscriptionHandlerName String; name of the SubscriptionHandler for which the command is destined
     * @param commandString String; the operation to perform
     * @param address Object[]; the address on which to perform the operation
     * @param returnWrapper ReturnWrapper; to transmit the result
     * @throws RemoteException on RMI network failure
     * @throws SerializationException on illegal type in serialization
     * @throws Sim0MQException on communication error
     */
    public void executeCommand(final String subscriptionHandlerName, final String commandString, final Object[] address,
            final ReturnWrapperImpl returnWrapper) throws RemoteException, Sim0MQException, SerializationException
    {
        executeCommand(subscriptionHandlerName,
                Throw.whenNull(SubscriptionHandler.lookupCommand(commandString), "Invalid command (%s)", commandString),
                address, returnWrapper);
    }

}

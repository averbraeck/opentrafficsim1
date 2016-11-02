package org.opentrafficsim.core.network;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.media.j3d.Bounds;

import org.djunits.value.vdouble.scalar.Length;
import org.opentrafficsim.core.dsol.OTSSimulatorInterface;
import org.opentrafficsim.core.geometry.OTSLine3D;
import org.opentrafficsim.core.gtu.GTU;
import org.opentrafficsim.core.gtu.GTUType;

import nl.tudelft.simulation.dsol.animation.Locatable;
import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.language.Throw;
import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * A standard implementation of a link between two OTSNodes.
 * <p>
 * Copyright (c) 2013-2016 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate$, @version $Revision$, by $Author$,
 * initial version Aug 19, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class OTSLink extends EventProducer implements Link, Serializable, Locatable
{
    /** */
    private static final long serialVersionUID = 20150101L;

    /** the Network. */
    private final Network network;

    /** Link id. */
    private final String id;

    /** Start node (directional). */
    private final Node startNode;

    /** End node (directional). */
    private final Node endNode;

    /** Link type to indicate compatibility with GTU types. */
    private final LinkType linkType;

    /** Design line of the link. */
    private final OTSLine3D designLine;

    /** The GTUs on this Link. */
    private final Set<GTU> gtus = new HashSet<>();

    /**
     * The direction in which vehicles can drive, i.e., in direction of geometry, reverse, or both. It might be that the link is
     * FORWARD (from start node to end node) for the GTU type CAR, but BOTH for the GTU type BICYCLE (i.e., bicycles can also go
     * from end node to start node). If the directionality for a GTUType is set to NONE, this means that the given GTUTYpe
     * cannot use the Link. If a Directionality is set for GTUType.ALL, the getDirectionality will default to these settings
     * when there is no specific entry for a given directionality. This means that the settings can be used additive, or
     * restrictive. <br>
     * In <b>additive use</b>, set the directionality for GTUType.ALL to NONE, or do not set the directionality for GTUType.ALL.
     * Now, one by one, the allowed directionalities can be added. An example is a highway, which we only open for CAR, TRUCK
     * and BUS. <br>
     * In <b>restrictive use</b>, set the directionality for GTUType.ALL to BOTH, FORWARD, or BACKWARD. Override the
     * directionality for certain GTUTypes to a more restrictive access, e.g. to NONE. An example is a road that is open for all
     * road users, except PEDESTRIAN.
     */
    private final Map<GTUType, LongitudinalDirectionality> directionalityMap;

    /**
     * Construct a new link.
     * @param id the link id
     * @param network the network to which the link belongs
     * @param startNode start node (directional)
     * @param endNode end node (directional)
     * @param linkType Link type to indicate compatibility with GTU types
     * @param designLine the OTSLine3D design line of the Link
     * @param directionalityMap the directions (FORWARD, BACKWARD, BOTH, NONE) that GTUtypes can traverse this link
     * @throws NetworkException if link already exists in the network, if name of the link is not unique, or if the start node
     *             or the end node of the link are not registered in the network.
     */
    public OTSLink(final Network network, final String id, final Node startNode, final Node endNode, final LinkType linkType,
            final OTSLine3D designLine, final Map<GTUType, LongitudinalDirectionality> directionalityMap)
            throws NetworkException
    {
        Throw.whenNull(network, "network cannot be null");
        Throw.whenNull(id, "id cannot be null");
        Throw.whenNull(startNode, "startNode cannot be null");
        Throw.whenNull(endNode, "endNode cannot be null");
        Throw.whenNull(linkType, "linkType cannot be null");
        Throw.whenNull(designLine, "designLine cannot be null");
        Throw.whenNull(directionalityMap, "directionalityMap cannot be null");

        this.network = network;
        this.id = id;
        this.startNode = startNode;
        this.endNode = endNode;
        this.linkType = linkType;
        this.startNode.addLink(this);
        this.endNode.addLink(this);
        this.designLine = designLine;
        this.directionalityMap = directionalityMap;

        this.network.addLink(this);
    }

    /**
     * Construct a new link, with a directionality for all GTUs as provided.
     * @param id the link id
     * @param network the network to which the link belongs
     * @param startNode start node (directional)
     * @param endNode end node (directional)
     * @param linkType Link type to indicate compatibility with GTU types
     * @param designLine the OTSLine3D design line of the Link
     * @param directionality the directionality for all GTUs
     * @throws NetworkException if link already exists in the network, if name of the link is not unique, or if the start node
     *             or the end node of the link are not registered in the network.
     */
    public OTSLink(final Network network, final String id, final Node startNode, final Node endNode, final LinkType linkType,
            final OTSLine3D designLine, final LongitudinalDirectionality directionality) throws NetworkException
    {
        this(network, id, startNode, endNode, linkType, designLine, new HashMap<GTUType, LongitudinalDirectionality>());
        addDirectionality(GTUType.ALL, directionality);
    }

    /**
     * Clone a link for a new network.
     * @param newNetwork the new network to which the clone belongs
     * @param newSimulator the new simulator for this network
     * @param animation whether to (re)create animation or not
     * @param link the link to clone from
     * @throws NetworkException if link already exists in the network, if name of the link is not unique, or if the start node
     *             or the end node of the link are not registered in the network.
     */
    protected OTSLink(final Network newNetwork, final OTSSimulatorInterface newSimulator, final boolean animation,
            final OTSLink link) throws NetworkException
    {
        this(newNetwork, link.id, newNetwork.getNode(link.startNode.getId()), newNetwork.getNode(link.endNode.getId()),
                link.linkType, link.designLine, new HashMap<>(link.directionalityMap));
    }

    /** {@inheritDoc} */
    @Override
    public final LongitudinalDirectionality getDirectionality(final GTUType gtuType)
    {
        if (this.directionalityMap.containsKey(gtuType))
        {
            return this.directionalityMap.get(gtuType);
        }
        if (this.directionalityMap.containsKey(GTUType.ALL))
        {
            return this.directionalityMap.get(GTUType.ALL);
        }
        return LongitudinalDirectionality.DIR_NONE;
    }

    /** {@inheritDoc} */
    @Override
    public final void addDirectionality(final GTUType gtuType, final LongitudinalDirectionality directionality)
    {
        this.directionalityMap.put(gtuType, directionality);
    }

    /** {@inheritDoc} */
    @Override
    public final void removeDirectionality(final GTUType gtuType)
    {
        this.directionalityMap.remove(gtuType);
    }

    /** {@inheritDoc} */
    @Override
    public final void addGTU(final GTU gtu)
    {
        if (!this.gtus.contains(gtu))
        {
            this.gtus.add(gtu);
            fireTimedEvent(Link.GTU_ADD_EVENT, new Object[] { gtu.getId(), gtu, this.gtus.size() },
                    gtu.getSimulator().getSimulatorTime());
        }
    }

    /** {@inheritDoc} */
    @Override
    public final void removeGTU(final GTU gtu)
    {
        if (this.gtus.contains(gtu))
        {
            this.gtus.remove(gtu);
            fireTimedEvent(Link.GTU_REMOVE_EVENT, new Object[] { gtu.getId(), gtu, this.gtus.size() },
                    gtu.getSimulator().getSimulatorTime());
        }
    }

    /** {@inheritDoc} */
    @Override
    public final Set<GTU> getGTUs()
    {
        return new HashSet<GTU>(this.gtus);
    }

    /** {@inheritDoc} */
    @Override
    public final int getGTUCount()
    {
        return this.gtus.size();
    }

    /** {@inheritDoc} */
    @Override
    public final Network getNetwork()
    {
        return this.network;
    }

    /** {@inheritDoc} */
    @Override
    public final String getId()
    {
        return this.id;
    }

    /** {@inheritDoc} */
    @Override
    public final Node getStartNode()
    {
        return this.startNode;
    }

    /** {@inheritDoc} */
    @Override
    public final Node getEndNode()
    {
        return this.endNode;
    }

    /** {@inheritDoc} */
    @Override
    public final LinkType getLinkType()
    {
        return this.linkType;
    }

    /** {@inheritDoc} */
    @Override
    public final OTSLine3D getDesignLine()
    {
        return this.designLine;
    }

    /**
     * @return directionalityMap the directionality map. Only for internal use in (sub)classes.
     */
    protected final Map<GTUType, LongitudinalDirectionality> getDirectionalityMap()
    {
        return this.directionalityMap;
    }

    /** {@inheritDoc} */
    @Override
    public final Length getLength()
    {
        return this.designLine.getLength();
    }

    /** {@inheritDoc} */
    @Override
    public final DirectedPoint getLocation()
    {
        return this.designLine.getLocation();
    }

    /** {@inheritDoc} */
    @Override
    public final Bounds getBounds()
    {
        return this.designLine.getBounds();
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return this.id.toString();
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.endNode == null) ? 0 : this.endNode.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.linkType == null) ? 0 : this.linkType.hashCode());
        result = prime * result + ((this.startNode == null) ? 0 : this.startNode.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings({ "checkstyle:designforextension", "checkstyle:needbraces" })
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OTSLink other = (OTSLink) obj;
        if (this.endNode == null)
        {
            if (other.endNode != null)
                return false;
        }
        else if (!this.endNode.equals(other.endNode))
            return false;
        if (this.id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.linkType == null)
        {
            if (other.linkType != null)
                return false;
        }
        else if (!this.linkType.equals(other.linkType))
            return false;
        if (this.startNode == null)
        {
            if (other.startNode != null)
                return false;
        }
        else if (!this.startNode.equals(other.startNode))
            return false;
        return true;
    }

    /**
     * Clone the OTSLink for e.g., copying a network.
     * @param newNetwork the new network to which the clone belongs
     * @param newSimulator the new simulator for this network
     * @param animation whether to (re)create animation or not
     * @return a clone of this object
     * @throws NetworkException in case the cloning fails
     */
    @SuppressWarnings("checkstyle:designforextension")
    public OTSLink clone(final Network newNetwork, final OTSSimulatorInterface newSimulator, final boolean animation)
            throws NetworkException
    {
        return new OTSLink(newNetwork, newSimulator, animation, this);
    }
}

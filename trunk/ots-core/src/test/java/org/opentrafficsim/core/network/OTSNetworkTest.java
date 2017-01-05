package org.opentrafficsim.core.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventType;

import org.junit.Test;
import org.opentrafficsim.core.geometry.OTSGeometryException;
import org.opentrafficsim.core.geometry.OTSLine3D;
import org.opentrafficsim.core.geometry.OTSPoint3D;
import org.opentrafficsim.core.gtu.GTUType;
import org.opentrafficsim.core.network.route.CompleteRoute;
import org.opentrafficsim.core.network.route.Route;

/**
 * <p>
 * Copyright (c) 2013-2016 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Jan 3, 2017 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class OTSNetworkTest implements EventListenerInterface
{

    /** Count NODE_ADD events. */
    private int nodeAddedCount = 0;

    /** Count NODE_REMOVE events. */
    private int nodeRemovedCount = 0;

    /** Count LINK_ADD events. */
    private int linkAddedCount = 0;

    /** Count LINK_REMOVE events. */
    private int linkRemovedCount = 0;

    /** Count other events. */
    private int otherEventCount = 0;

    /**
     * Test OTSNetwork class.
     * @throws NetworkException if that happens; this test has failed
     * @throws OTSGeometryException if that happens; this test has failed
     */
    @Test
    public final void testOTSNetwork() throws NetworkException, OTSGeometryException
    {
        String networkId = "testOTSNetwork";
        OTSNetwork network = new OTSNetwork(networkId);
        assertTrue("Id must match", networkId.equals(network.getId()));
        network.addListener(this, Network.LINK_ADD_EVENT);
        network.addListener(this, Network.LINK_REMOVE_EVENT);
        network.addListener(this, Network.NODE_ADD_EVENT);
        network.addListener(this, Network.NODE_REMOVE_EVENT);
        assertEquals("link add event count is 0", 0, this.linkAddedCount);
        assertEquals("link removed event count is 0", 0, this.linkRemovedCount);
        assertEquals("node add event count is 0", 0, this.nodeAddedCount);
        assertEquals("node removed event count is 0", 0, this.nodeRemovedCount);
        assertEquals("other event count is 0", 0, this.otherEventCount);
        assertEquals("Node map is empty", 0, network.getNodeMap().size());
        Node node1 = new OTSNode(network, "node1", new OTSPoint3D(10, 20, 30));
        assertEquals("link add event count is 0", 0, this.linkAddedCount);
        assertEquals("link removed event count is 0", 0, this.linkRemovedCount);
        assertEquals("node add event count is 1", 1, this.nodeAddedCount);
        assertEquals("node removed event count is 0", 0, this.nodeRemovedCount);
        assertEquals("other event count is 0", 0, this.otherEventCount);
        assertEquals("Node map now contains one node", 1, network.getNodeMap().size());
        assertEquals("Node is node1", node1, network.getNodeMap().values().iterator().next());
        assertEquals("Node can be retrieved by id", node1, network.getNode(node1.getId()));
        assertTrue("network contains a node with id node1", network.containsNode("node1"));
        // Create a node that is NOT in this network; to do that we must create another network
        OTSNetwork otherNetwork = new OTSNetwork("other network");
        Node node2 = new OTSNode(otherNetwork, "node2", new OTSPoint3D(11, 12, 13));
        assertFalse("node2 is NOT in network", network.containsNode(node2));
        assertEquals("link add event count is 0", 0, this.linkAddedCount);
        assertEquals("link removed event count is 0", 0, this.linkRemovedCount);
        assertEquals("node add event count is 1", 1, this.nodeAddedCount);
        assertEquals("node removed event count is 0", 0, this.nodeRemovedCount);
        assertEquals("other event count is 0", 0, this.otherEventCount);
        try
        {
            new OTSNode(network, "node1", new OTSPoint3D(110, 20, 30));
            fail("duplicate node id should have thrown a NetworkException");
        }
        catch (NetworkException ne)
        {
            // Ignore expected exception
        }
        try
        {
            network.addNode(node1);
            fail("duplicate node should have thrown a NetworkException");
        }
        catch (NetworkException ne)
        {
            // Ignore expected exception
        }
        network.removeNode(node1);
        assertEquals("link add event count is 0", 0, this.linkAddedCount);
        assertEquals("link removed event count is 0", 0, this.linkRemovedCount);
        assertEquals("node add event count is 1", 1, this.nodeAddedCount);
        assertEquals("node removed event count is 1", 1, this.nodeRemovedCount);
        assertEquals("other event count is 0", 0, this.otherEventCount);
        assertEquals("Node map is empty", 0, network.getNodeMap().size());
        assertEquals("network now had 0 nodes", 0, network.getNodeMap().size());
        network.addNode(node1);
        assertEquals("Node map now contains one node", 1, network.getNodeMap().size());
        assertEquals("Node is node1", node1, network.getNodeMap().values().iterator().next());
        assertEquals("Node can be retrieved by id", node1, network.getNode(node1.getId()));
        assertEquals("LinkMap is empty", 0, network.getLinkMap().size());
        assertEquals("link add event count is 0", 0, this.linkAddedCount);
        assertEquals("link removed event count is 0", 0, this.linkRemovedCount);
        assertEquals("node add event count is 2", 2, this.nodeAddedCount);
        assertEquals("node removed event count is 1", 1, this.nodeRemovedCount);
        assertEquals("other event count is 0", 0, this.otherEventCount);
        try
        {
            new OTSLink(network, "link1", node1, node2, LinkType.ALL, new OTSLine3D(node1.getPoint(), node2.getPoint()),
                    LongitudinalDirectionality.DIR_BOTH);
            fail("new OTSLink should have thrown an exception because node2 is not in network");
        }
        catch (NetworkException ne)
        {
            // Ignore expected exception
        }
        try
        {
            new OTSLink(network, "link1", node2, node1, LinkType.ALL, new OTSLine3D(node2.getPoint(), node1.getPoint()),
                    LongitudinalDirectionality.DIR_BOTH);
            fail("new OTSLink should have thrown an exception because node2 is not in network");
        }
        catch (NetworkException ne)
        {
            // Ignore expected exception
        }
        Node node3 = new OTSNode(network, "node3", new OTSPoint3D(11, 12, 13));
        assertEquals("link add event count is 0", 0, this.linkAddedCount);
        assertEquals("link removed event count is 0", 0, this.linkRemovedCount);
        assertEquals("node add event count is 3", 3, this.nodeAddedCount);
        assertEquals("node removed event count is 1", 1, this.nodeRemovedCount);
        assertEquals("other event count is 0", 0, this.otherEventCount);
        Link link1 =
                new OTSLink(network, "link1", node1, node3, LinkType.ALL, new OTSLine3D(node1.getPoint(), node3.getPoint()),
                        LongitudinalDirectionality.DIR_BOTH);
        assertEquals("LinkMap now contains 1 link", 1, network.getLinkMap().size());
        assertTrue("LinkMap contains link1", network.containsLink(link1));
        assertTrue("LinkMap.contain link with name link1", network.containsLink("link1"));
        assertEquals("link add event count is 1", 1, this.linkAddedCount);
        assertEquals("link removed event count is 0", 0, this.linkRemovedCount);
        assertEquals("node add event count is 3", 3, this.nodeAddedCount);
        assertEquals("node removed event count is 1", 1, this.nodeRemovedCount);
        assertEquals("other event count is 0", 0, this.otherEventCount);
        try
        {
            network.addLink(link1);
            fail("Adding link1 again should have thrown a NetworkException");
        }
        catch (NetworkException ne)
        {
            // Ignore expected exception
        }
        assertEquals("link1 is the link connecting node1 to node3", link1, network.getLink(node1, node3));
        assertEquals("link1 is the link connecting node named node1 to node named node3", link1,
                network.getLink("node1", "node3"));
        Node node4 = new OTSNode(otherNetwork, "node4", new OTSPoint3D(-2, -3, -4));
        Link otherLink =
                new OTSLink(otherNetwork, "otherLink", node2, node4, LinkType.ALL, new OTSLine3D(node2.getPoint(),
                        node4.getPoint()), LongitudinalDirectionality.DIR_BOTH);
        try
        {
            network.removeLink(otherLink);
            fail("Removing a link that is in another network should have thrown a NetworkException");
        }
        catch (NetworkException ne)
        {
            // Ignore expected exception
        }
        try
        {
            network.addLink(otherLink);
            fail("Adding a link that connects nodes not in the network should have thrown a NetworkException");
        }
        catch (NetworkException ne)
        {
            // Ignore expected exception
        }
        assertEquals("link add event count is 1", 1, this.linkAddedCount);
        assertEquals("link removed event count is 0", 0, this.linkRemovedCount);
        assertEquals("node add event count is 3", 3, this.nodeAddedCount);
        assertEquals("node removed event count is 1", 1, this.nodeRemovedCount);
        assertEquals("other event count is 0", 0, this.otherEventCount);
        Link secondLink =
                new OTSLink(network, "reverseLink", node3, node1, LinkType.ALL, new OTSLine3D(node3.getPoint(),
                        node1.getPoint()), LongitudinalDirectionality.DIR_PLUS);
        assertEquals("link add event count is 2", 2, this.linkAddedCount);
        assertEquals("link removed event count is 0", 0, this.linkRemovedCount);
        assertEquals("node add event count is 3", 3, this.nodeAddedCount);
        assertEquals("node removed event count is 1", 1, this.nodeRemovedCount);
        assertEquals("other event count is 0", 0, this.otherEventCount);
        assertTrue("Network contains secondLink", network.containsLink(secondLink));
        assertTrue("Network contains link named reverseLink", network.containsLink("reverseLink"));
        assertFalse("Network does not contain link named junk", network.containsLink("junk"));
        try
        {
            network.getLink("junk", "node3");
            fail("looking up a link starting at nonexistent node should have thrown a NetworkException");
        }
        catch (NetworkException ne)
        {
            // Ignore expected exception
        }
        try
        {
            network.getLink("node1", "junk");
            fail("looking up a link ending at nonexistent node should have thrown a NetworkException");
        }
        catch (NetworkException ne)
        {
            // Ignore expected exception
        }
        assertEquals("lookup link from node node1 to node node3", link1, network.getLink("node1", "node3"));
        assertEquals("lookup link from node1 to node3", link1, network.getLink(node1, node3));
        assertEquals("lookup link from node node3 to node node1", secondLink, network.getLink("node3", "node1"));
        assertEquals("lookup link from node3 to node1", secondLink, network.getLink(node3, node1));
        assertNull("lookup link that does not exist but both nodes do exist", network.getLink(node1, node1));
        assertNull("lookup link that does not exist but both nodes do exist", network.getLink("node1", "node1"));
        assertEquals("lookup link by name", link1, network.getLink("link1"));
        assertEquals("lookup link by name", secondLink, network.getLink("reverseLink"));
        network.removeLink(link1);
        assertFalse("Network no longer contains link1", network.containsLink(link1));
        assertFalse("Network no longer contains link with name link1", network.containsLink("link1"));
        assertEquals("link add event count is 2", 2, this.linkAddedCount);
        assertEquals("link removed event count is 1", 1, this.linkRemovedCount);
        assertEquals("node add event count is 3", 3, this.nodeAddedCount);
        assertEquals("node removed event count is 1", 1, this.nodeRemovedCount);
        assertEquals("other event count is 0", 0, this.otherEventCount);
        assertEquals("network now contains one link", 1, network.getLinkMap().size());
    }

    /** {@inheritDoc} */
    @Override
    public final void notify(final EventInterface event) throws RemoteException
    {
        EventType type = event.getType();
        if (type.equals(Network.NODE_ADD_EVENT))
        {
            this.nodeAddedCount++;
        }
        else if (type.equals(Network.NODE_REMOVE_EVENT))
        {
            this.nodeRemovedCount++;
        }
        else if (type.equals(Network.LINK_ADD_EVENT))
        {
            this.linkAddedCount++;
        }
        else if (type.equals(Network.LINK_REMOVE_EVENT))
        {
            this.linkRemovedCount++;
        }
        else
        {
            this.otherEventCount++;
        }
    }

    /**
     * Test the route map stuff.
     * @throws NetworkException if that happens uncaught; this test has failed
     */
    @Test
    public final void testRouteMap() throws NetworkException
    {
        Network network = new OTSNetwork("Route map test network");
        Node node1 = new OTSNode(network, "node1", new OTSPoint3D(10, 20, 30));
        Node node2 = new OTSNode(network, "node2", new OTSPoint3D(110, 20, 30));
        List<Node> nodeList = new ArrayList<>();
        nodeList.add(node1);
        nodeList.add(node2);
        Route route1 = new Route("route1", nodeList);
        Route route2 = new Route("route2");
        Route route3 = new Route("route3");
        GTUType carType = new GTUType("car", GTUType.VEHICLE);
        GTUType bicycleType = new GTUType("bicycle", GTUType.BIKE);
        // The next test makes little sense until the getters are changed to search up to the GTUType root.
        assertEquals("initially the network has 0 routes", 0, network.getDefinedRouteMap(GTUType.ALL).size());
        network.addRoute(carType, route1);
        assertEquals("list for carType contains one entry", 1, network.getDefinedRouteMap(carType).size());
        assertEquals("route for carType route1 is route1", route1, network.getRoute(carType, "route1"));
        assertNull("route for bycicleType route1 is null", network.getRoute(bicycleType, "route1"));
        assertEquals("list for bicycleType contains 0 routes", 0, network.getDefinedRouteMap(bicycleType).size());
        network.addRoute(carType, route2);
        network.addRoute(bicycleType, route3);
        assertEquals("list for carType contains two entries", 2, network.getDefinedRouteMap(carType).size());
        assertEquals("list for bicycleType contains one entry", 1, network.getDefinedRouteMap(bicycleType).size());
        assertEquals("route for carType route1 is route1", route1, network.getRoute(carType, "route1"));
        assertEquals("route for carType route2 is route2", route2, network.getRoute(carType, "route2"));
        assertEquals("route for bicycle route3 is route3", route3, network.getRoute(bicycleType, "route3"));
        assertNull("route for bicycle route1 is null", network.getRoute(bicycleType, "route1"));
        try
        {
            network.addRoute(carType, route2);
            fail("adding route again should have thrown a NetworkException");
        }
        catch (NetworkException ne)
        {
            // Ignore expected exception
        }
        Network otherNetwork = new OTSNetwork("other Route map test network");
        Node badNode = new OTSNode(otherNetwork, "nodeInOtherNetwork", new OTSPoint3D(100, 200, 0));
        List<Node> badNodeList = new ArrayList<>();
        badNodeList.add(node1);
        badNodeList.add(node2);
        badNodeList.add(badNode);
        Route badRoute = new Route("badRoute", badNodeList);
        try
        {
            network.addRoute(carType, badRoute);
            fail("adding a route with a node that is not in the network should have thrown a NetworkException");
        }
        catch (NetworkException ne)
        {
            // Ignore expected exception
        }
        try
        {
            network.removeRoute(bicycleType, route1);
            fail("attempt to remove a route that is not defined for this GTUType should have thrown a NetworkException");
        }
        catch (NetworkException ne)
        {
            // Ignore expected exception
        }
        assertEquals("there is one route from node1 to node2 for carType", 1, network.getRoutesBetween(carType, node1, node2)
                .size());
        assertEquals("the one route from node1 to node2 is route1", route1, network.getRoutesBetween(carType, node1, node2)
                .iterator().next());
        assertEquals("there are no routes from node1 to node2 for bicycleType", 0,
                network.getRoutesBetween(bicycleType, node1, node2).size());
        assertEquals("there are no routes from node2 to node1 for carTypecleType", 0,
                network.getRoutesBetween(carType, node2, node1).size());
        assertEquals("there are no routes from node1 to node1 for carTypecleType", 0,
                network.getRoutesBetween(carType, node1, node1).size());
        GTUType junkType = new GTUType("junk", GTUType.VEHICLE);
        assertEquals("there are no routes from node1 to node2 for badType", 0, network.getRoutesBetween(junkType, node1, node2)
                .size());
        network.removeRoute(carType, route1);
        assertEquals("list for carType now contains one entry", 1, network.getDefinedRouteMap(carType).size());
        assertEquals("list for bicycleType contains one entry", 1, network.getDefinedRouteMap(bicycleType).size());
        assertNull("route for carType route1 is null", network.getRoute(carType, "route1"));
        assertEquals("route for carType route2 is route2", route2, network.getRoute(carType, "route2"));
        assertEquals("route for bicycle route3 is route3", route3, network.getRoute(bicycleType, "route3"));
        assertTrue("network contains route2 for carType", network.containsRoute(carType, route2));
        assertFalse("network does not contain route1 for carType", network.containsRoute(carType, route1));
        assertTrue("network contains route with name route2 for carType", network.containsRoute(carType, "route2"));
        assertFalse("network does not contain route with name route1 for carType", network.containsRoute(carType, "route1"));
        assertFalse("network does not contain route with name route1 for junkType", network.containsRoute(junkType, "route1"));
    }

    /**
     * Test the shortest path functionality.
     * @throws NetworkException if that happens uncaught; this test has failed
     * @throws OTSGeometryException if that happens uncaught; this test has failed
     */
    @Test
    public final void testShortestPath() throws NetworkException, OTSGeometryException
    {
        Network network = new OTSNetwork("shortest path test network");
        // Create a bunch of nodes spread out over a circle
        List<Node> nodes = new ArrayList<>();
        double radius = 500;
        double centerX = 0;
        double centerY = 0;
        int maxNode = 4;
        for (int i = 0; i < maxNode; i++)
        {
            double angle = i * Math.PI * 2 / maxNode;
            nodes.add(new OTSNode(network, "node" + i, new OTSPoint3D(centerX + radius * Math.cos(angle), centerY + radius
                    * Math.sin(angle), 20)));
        }
        // Create bi-directional links between all adjacent nodes
        Node prevNode = nodes.get(maxNode - 1);
        for (Node node : nodes)
        {
            new OTSLink(network, "from " + prevNode.getId() + " to " + node.getId(), prevNode, node, LinkType.ALL,
                    new OTSLine3D(prevNode.getPoint(), node.getPoint()), LongitudinalDirectionality.DIR_BOTH);
            prevNode = node;
        }
        for (int skip = 1; skip < maxNode / 2; skip++)
        {
            for (int fromNodeIndex = 0; fromNodeIndex < maxNode; fromNodeIndex++)
            {
                Node fromNode = nodes.get(fromNodeIndex);
                Node toNode = nodes.get((fromNodeIndex + skip) % maxNode);
                CompleteRoute route = network.getShortestRouteBetween(GTUType.ALL, fromNode, toNode);
                assertEquals("route size is skip + 1", skip + 1, route.size());
                for (int i = 0; i < route.size(); i++)
                {
                    assertEquals("node in route at position i should match", nodes.get((fromNodeIndex + i) % maxNode),
                            route.getNode(i));
                }
                // reverse direction
                route = network.getShortestRouteBetween(GTUType.ALL, toNode, fromNode);
                System.out.println("Shortest route from " + toNode + " to " + fromNode + " is " + route);
                assertEquals("route size is skip + 1", skip + 1, route.size());
                for (int i = 0; i < route.size(); i++)
                {
                    assertEquals("node in route at position i should match",
                            nodes.get((fromNodeIndex + skip - i + maxNode) % maxNode), route.getNode(i));
                }
            }
        }
    }

}

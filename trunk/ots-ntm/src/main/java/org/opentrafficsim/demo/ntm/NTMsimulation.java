package org.opentrafficsim.demo.ntm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.NavigableMap;
import org.opentrafficsim.core.unit.FrequencyUnit;
import org.opentrafficsim.core.unit.TimeUnit;
import org.opentrafficsim.core.value.vdouble.scalar.DoubleScalar;
import org.opentrafficsim.core.value.vdouble.scalar.DoubleScalar.Abs;
import org.opentrafficsim.demo.ntm.Node.TrafficBehaviourType;
import org.opentrafficsim.demo.ntm.trafficdemand.FractionOfTripDemandByTimeSegment;
import org.opentrafficsim.demo.ntm.trafficdemand.TripInfoTimeDynamic;

/**
 * <p>
 * Copyright (c) 2013-2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights
 * reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version 29 Oct 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://Hansvanlint.weblog.tudelft.nl">Hans van Lint</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.citg.tudelft.nl">Guus Tamminga</a>
 * @author <a href="http://www.citg.tudelft.nl">Yufei Yuan</a>
 */
public class NTMsimulation
{
    /**
     * @param model that is being simulated
     */
    public static void simulate(final NTMModel model)
    {

        /**
         * 
         */
        @SuppressWarnings("unchecked")
        double accumulatedCarsInCell = 0;
        DoubleScalar.Abs<TimeUnit> currentTime = null;
        boolean DEBUG = true;
        BufferedWriter out = null;

        
        try
        {
            currentTime =
                    new DoubleScalar.Abs<TimeUnit>(model.getSettingsNTM().getStartTimeSinceMidnight().getSI()
                            + model.getSimulator().getSimulatorTime().get().getSI(), TimeUnit.SECOND);
        }
        catch (RemoteException exception2)
        {
            exception2.printStackTrace();
        }
        // long timeStep = 0;

        // Initiate trips from OD to first Area (Origin)
        Map<String, Map<String, TripInfoTimeDynamic>> trips = model.getTripDemand().getTripInfo();
        // retrieve information from the Area Graph containing the NTM areas and the selected highways

        // The AreaGraph contains EDGES with differing characteristics:
        // - "NTM"-links between NTM areas
        // - "Cordon" links at the border of the study area that act as feeders and sinks of traffic to the "real" Areas
        // - "Flow" links representing higher order roads with behaviour that deviates from the NTM area links
        // (only for specific "main" roads)

        // The VERTICES (type: BoundedNode) represent the NTM areas, or the entrance / exit of a Flow link.
        // NTM nodes and FlowLink entrances/exits are connected by a "Transfer" link
        //
        // The "NTM" links between a pair of nodes, visualise the connection of that node to its "neighbours" with NTM
        // characteristics
        // The "Flow" links represent a connection where traffic behaves differently than in the NTM areas:
        // once traffic is on a homogeneous link, the capacity remains stable
        //
        // The simulation of Traffic becomes a nested process:
        // - the NTM process is the parent of the simulation
        // - the Flow process acts as a "child" process

        // ********************************************************************************************************
        // STEP 1 of the simulation describes the initialisation of Demand from the Traffic Demand file:
        // - this is generated by the OD matrix from the model

        // first loop through the network nodes and select the NTM and Cordon "Area nodes"
        // These nodes generate traffic from the trip demand file (feeders)

        // There are two ESSENTIAL variables / class types:
        // - the Node.ClassBehaviour represents the aggregated traffic flow dynamics between nodes/cells
        // - the TripInfoTimeDynamic of an Area, showing for every OD-pair:
        // . - number of trips between origin (or intermediate areas on a path from O to D) - destination pairs (from
        // . . demand file)
        // . - neighbour area on path form Origin to Destination
        // . - time profile curve of trip departures from this origin (only used for the real origins!)
        // . - accumulated cars in this area/node/cell, heading on the way to a certain destination
        // . - flow in this time-step to neighbour from this area to destination

        // for testing we open a file and write some results:
        // TODO testing
        if (DEBUG)
        {
            File file = new File("D:/gtamminga/workspace/ots-ntm/testing/outputTest.txt");

            // if file doesnt exists, then create it
            if (!file.exists())
            {
                try
                {
                    file.createNewFile();
                }
                catch (IOException exception)
                {
                    exception.printStackTrace();
                }
            }

            try
            {
                out = new BufferedWriter(new FileWriter(file));
            }
            catch (IOException exception1)
            {
                exception1.printStackTrace();
            }
        }
        // Loop through all areas to detect the trips to the destination area
        for (BoundedNode origin : model.getAreaGraph().vertexSet())
        {
            try
            {
                // only the feeding areas: of the type NTM and Cordon
                if (origin.getBehaviourType() == TrafficBehaviourType.NTM
                        || origin.getBehaviourType() == TrafficBehaviourType.CORDON)
                {
                    // the variable CellBehaviour(NTM) defines the traffic process within an area (the area is
                    // represented by the "BoundedNode"). This can be NTM behaviour, Cell transmission or other.
                    CellBehaviour cellBehaviour = origin.getCellBehaviour();
                    // during the simulation traffic enters and leaves the NTM areas. The number of "accumulated cars"
                    // represents the net balance of cars within the Nodes/areas. The new demand will be added!
                    accumulatedCarsInCell = cellBehaviour.getAccumulatedCars();
                    // The variable TripsFrom contains information on trips from an origin/node to ALL other
                    // destinations.
                    // This origin can be the real origin or an intermediate area on the path to destination
                    // (neighbours).
                    // The structure (or Class in Java) named TripInfoDynamic is stored in a HashMap (lookup array) that
                    // contains this information for all destinations separately.
                    Map<String, TripInfoTimeDynamic> tripsFrom = trips.get(origin.getId());
                    // loop through all destinations
                    for (BoundedNode nodeTo : model.getAreaGraph().vertexSet())
                    {
                        // only select the final destinations: where Trips are heading to
                        if (tripsFrom.containsKey(nodeTo.getId()))
                        {
                            // retrieve the TRIPS from the demand file (2 hour period)
                            // first the total within the defined period
                            double startingTrips = tripsFrom.get(nodeTo.getId()).getNumberOfTrips();
                            // get the share of Trips of this time slice (NTM simulation step of 10 seconds)
                            if (startingTrips > 0.0)
                            {
                                // retrieve the departure time curve showing the fractions of demand by time-slice
                                NavigableMap<Abs<TimeUnit>, FractionOfTripDemandByTimeSegment> curve =
                                        tripsFrom.get(nodeTo.getId()).getDepartureTimeProfile().getDepartureTimeCurve();
                                Object ceilingKey = curve.ceilingKey(currentTime);
                                // The variable segment of the type (FractionOfTripDemandByTimeSegment) contains the
                                // duration (in time units) of this segment and the fraction
                                FractionOfTripDemandByTimeSegment segment = curve.get(ceilingKey);
                                // the share is adjusted by the TimeStepDuration of the simulation
                                double share =
                                        segment.getShareOfDemand()
                                                * model.getSettingsNTM().getTimeStepDurationNTM().getSI()
                                                / segment.getDuration().getSI();
                                startingTrips = share * startingTrips;
                                // these new Trips are added to the TRIPS that are already on their way (passing an NTM
                                // area): the AccumulatedCars specified by their specific destination (nodeTo.getId())
                                tripsFrom.get(nodeTo.getId()).addAccumulatedCarsToDestination(startingTrips);
                                // and this also increases the total number of accumulated cars in the area, that is
                                // used for NTM computations
                                accumulatedCarsInCell += startingTrips;
                            }
                        }
                    }
                    // The variable CellDynamics stores the number of accumulated cars, the demandToEnter, and the
                    // put these trips in the stock of cars within the Area (added the new Trips)
                    cellBehaviour.setAccumulatedCars(accumulatedCarsInCell);
                    // the demand and supply of traffic is based on the type of Cell that is considered
                    if (origin.getBehaviourType() == TrafficBehaviourType.NTM)
                    {
                        // CellBehaviourNTM extends CellBehaviour (additional or different behaviour)
                        CellBehaviourNTM cellBehaviourNTM = (CellBehaviourNTM) origin.getCellBehaviour();
                        // compute the total Demand (production) from an Area to all other Destinations (the level is
                        // based on the accumulation, the capacity of an area and the NFD algorithm).
                        // The new demand of this area is derived via the method RetrieveDemand that is based on the
                        // network fundamental diagram (see there for further details)
                        // TODO implement roadLength and length simulation step !!!!!!!!!!!!!!!!!!!!!!!!
                        cellBehaviourNTM.setDemand(cellBehaviourNTM.retrieveDemand(accumulatedCarsInCell,
                                cellBehaviourNTM.getMaxCapacity(), cellBehaviourNTM.getParametersNTM()));
                        // compute the total supply (maximum) from neighbours to this Area (again based on the
                        // accumulation and NFD/area characteristics)
                        // TODO implement roadLength and length simulation step !!!!!!!!!!!!!!!!!!!!!!!!
                        cellBehaviourNTM.setSupply(cellBehaviourNTM.retrieveSupply(accumulatedCarsInCell,
                                cellBehaviourNTM.getMaxCapacity(), cellBehaviourNTM.getParametersNTM()));
                    }
                    // the border, or CORDON areas, as sink/source for traffic
                    // The flow nodes act as entrances, intermediate or exits of the cell transmission model
                    // For this last category, we assume that the links are putting a restriction on capacity
                    else if (origin.getBehaviourType() == TrafficBehaviourType.CORDON
                            || origin.getBehaviourType() == TrafficBehaviourType.FLOW)
                    {
                        // demand is the sum of new demand and accumulated traffic from previous time steps (possibly if
                        // the neighbour area does not accept all traffic)
                        cellBehaviour.setDemand(accumulatedCarsInCell);
                        // the total supply is infinite for Cordon and Flow nodes (sinks with no limit on in-flow)
                        cellBehaviour.setSupply(java.lang.Double.POSITIVE_INFINITY);
                    }
                    String text;
                    text = "Area " + origin.getId() + ", ";
                    text +=
                            "Accumulated cars: " + String.valueOf(origin.getCellBehaviour().getAccumulatedCars())
                                    + ", \n";
                    if (DEBUG)
                    {
    
                        try
                        {
                            out.write(text);
                        }
                        catch (IOException exception)
                        {
                            exception.printStackTrace();
                        }
                    }
                }
            }
            // in the next steps, the dynamics of demand and supply create a certain flow between areas
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        // ********************************************************************************************************
        // STEP 2:
        // Compute the flows (based on DEMAND!) between all areas, first assuming that there are no restrictions on the
        // supply side.
        // These will be corrected in Step 3, in case supply poses restrictions to the in-flow!
        // These STEPS regard both the dynamics from the NTM areas and from the Cell transmission behaviour at Flow
        // Nodes/Edges
        for (BoundedNode startNode : model.getAreaGraph().vertexSet())
        {
            try
            {
                // the behaviour of Cell Transmission still has to be implemented here
                // the network is already prepared!
                if (startNode.getBehaviourType() == TrafficBehaviourType.NTM
                        || startNode.getBehaviourType() == TrafficBehaviourType.CORDON
                        || startNode.getBehaviourType() == TrafficBehaviourType.FLOW)
                {
                    // first loop through the NTM and Cordon Area "nodes"
                    CellBehaviour cellBehaviour = startNode.getCellBehaviour();
                    accumulatedCarsInCell = cellBehaviour.getAccumulatedCars();
                    // tripsFrom represents the "row" of trips leaving from a zone to all of its destinations
                    // (columns): see step 1 for a detailed description of this key variable!
                    Map<String, TripInfoTimeDynamic> tripsFrom = trips.get(startNode.getArea().getCentroidNr());
                    // only production, if there are accumulated cars!!
                    if (cellBehaviour.getDemand() > 0.0)
                    {
                        for (BoundedNode toNode : model.getAreaGraph().vertexSet())
                        {
                            // only select relations with OD flows
                            if (tripsFrom.containsKey(toNode.getId()) && startNode.getId() != toNode.getId())
                            {
                                // The tripsFrom includes information about the trips to all other zones
                                // In this step we are interested in the first zone we encounter ("neighbour") of the
                                // cars on their path to a certain destination Area.
                                // Therefore we retrieve this neighbour.
                                BoundedNode neighbour = (BoundedNode) tripsFrom.get(toNode.getId()).getNeighbour();
                                // Compute the share of the accumulated trips to a certain destination as part of the
                                // total accumulation
                                double share =
                                        tripsFrom.get(toNode.getId()).getAccumulatedCarsToDestination()
                                                / cellBehaviour.getAccumulatedCars();
                                // for the NTM areas only:
                                // . the out-flow to a certain destination may be restricted by the bounds of the total
                                // . demand based on total accumulation and the characteristics of the NFD diagram
                                double flowFromDemand = share * cellBehaviour.getDemand();

                                // this potential out-flow is heading to the neighbour that is on its path to
                                // destination
                                if (neighbour != null)
                                {
                                    if (startNode.getBehaviourType() == TrafficBehaviourType.FLOW
                                            && neighbour.getBehaviourType() == TrafficBehaviourType.FLOW)
                                    {
                                        // In case of Cell Transmission Links, there is an intermediate process of
                                        // traffic
                                        // moving over a link. The demand to this link is computed.
                                        // In Step 3 see if this demand is below the capacity of the link (supply)
                                        try
                                        {
                                            LinkCellTransmission ctmLink =
                                                    (LinkCellTransmission) model.getAreaGraph()
                                                            .getEdge(startNode, neighbour).getLink();
                                            FlowCell startCell = ctmLink.getCells().get(0);
                                            CellBehaviourFlow flowBehaviour =
                                                    (CellBehaviourFlow) startCell.getCellBehaviour();
                                            // retrieve the cars that want to enter (from the previous step)
                                            double cars = flowBehaviour.getNumberOfTripsTo().get(toNode.getId());
                                            // add the additional demand that wants to traverse
                                            flowBehaviour.getNumberOfTripsTo().put(toNode.getId(),
                                                    cars + flowFromDemand);
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                    // first add these trips to the number of trips that want to transfer to their
                                    // neighbour
                                    tripsFrom.get(toNode.getId()).setFlowToNeighbour(flowFromDemand);
                                    // this flow is also added to the total sum of traffic that wants to enter this
                                    // neighbour Area.
                                    neighbour.getCellBehaviour().addDemandToEnter(flowFromDemand);
                                }
                                else
                                {
                                    System.out.println("no route between Area " + startNode.getId() + " and Area "
                                            + toNode.getId());
                                }
                                // In the next step, see whether this demand from nodes is able to enter completely or
                                // just
                                // partly (when supply is restricted)
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        // ********************************************************************************************************
        // STEP 3:
        // Simulate the CellTranmission model
        // This phase identifies how trips proceed through the link and results in the traffic state of the flow cells
        // within the CTM link and the amount of traffic that wants to enter the end Node of the CTM link
        // If this Node provides the entrance to a new Cell Transmission Link this is implemented in the simulation
        // if that Node provides a transfer to an NTM of Cordon area it captures the trips that want to enter that node

        // Simulate this 5 times!!
        for (BoundedNode startNode : model.getAreaGraph().vertexSet())
        {
            try
            {
                if (startNode.getBehaviourType() == TrafficBehaviourType.FLOW)
                {
                    // first loop through the NTM and Cordon Area "nodes"
                    // tripsFrom represents the "row" of trips leaving from a zone to all of its destinations
                    // (columns)
                    Map<String, TripInfoTimeDynamic> tripsFrom = trips.get(startNode.getId());
                    for (BoundedNode nodeTo : model.getAreaGraph().vertexSet())
                    {
                        if (nodeTo.getId() == null || nodeTo == null)
                        {
                            System.out.println("null");
                        }

                        if (tripsFrom.containsKey(nodeTo.getId()))
                        {
                            // retrieve the neighbour area on the path to a certain destination
                            BoundedNode neighbour = (BoundedNode) tripsFrom.get(nodeTo.getId()).getNeighbour();
                            if (neighbour != null)
                            {
                                if (neighbour.getBehaviourType() == TrafficBehaviourType.FLOW)
                                {
                                    // Do CTM
                                    // In case of Cell Transmission Links, there is an intermediate process of traffic
                                    // moving over a link. The demand to this link is computed.
                                    // In Step 3 see if this demand is below the capacity of the link (supply)
                                    try
                                    {
                                        // all cells have identical characteristics
                                        LinkCellTransmission ctmLink =
                                                (LinkCellTransmission) model.getAreaGraph()
                                                        .getEdge(startNode, neighbour).getLink();
                                        FlowCell startCell = ctmLink.getCells().get(0);
                                        double demandToEnter = startCell.getCellBehaviour().getDemandToEnter();
                                        DoubleScalar.Abs<FrequencyUnit> capacity = ctmLink.getCapacity();
                                        double shareToEnter =
                                                demandToEnter
                                                        * model.getSettingsNTM()
                                                                .getTimeStepDurationCellTransmissionModel().getSI()
                                                        / capacity.getSI();
                                        // Loop through the cells and do transmission
                                        for (FlowCell cell : ctmLink.getCells())
                                        {
                                            CellBehaviourFlow flowBehaviour =
                                                    (CellBehaviourFlow) cell.getCellBehaviour();
                                            double enterCell =
                                                    shareToEnter
                                                            * flowBehaviour.getNumberOfTripsTo().get(nodeTo.getId());
                                        }

                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }

                                    // retrieve the type of CellBehaviour (showing the demand and supply characteristics
                                    // of
                                    // this neighbour)
                                    CellBehaviour cellBehaviourNeighbour = neighbour.getCellBehaviour();
                                    double tripsToNeighbour = tripsFrom.get(nodeTo.getId()).getFlowToNeighbour();
                                    // compute the share of traffic that wants to enter this Neighbour area from a
                                    // certain
                                    // origin - destination pair as part of the total demand that wants to enter the
                                    // neighbour cell.
                                    double share = tripsToNeighbour / cellBehaviourNeighbour.getDemandToEnter();
                                    // the total supply to the neighbour may be restricted (by calling getSupply that
                                    // provides the maximum Supply). Compute the final flow based on the share of Trips
                                    // to a
                                    // certain destination and this maximum supply of the Cell.
                                    double flowFromDemand = share * cellBehaviourNeighbour.getSupply();
                                    // set the final flow to the neighbour
                                    tripsFrom.get(nodeTo.getId()).setFlowToNeighbour(flowFromDemand);
                                }
                            }
                            else
                            {
                                System.out.println("no route...");
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        // ********************************************************************************************************
        // STEP 4:
        // Monitor whether the demand of traffic from outside areas is able to enter a certain Area
        // Perhaps SUPPLY poses an upper bound on the Demand!
        for (BoundedNode startNode : model.getAreaGraph().vertexSet())
        {
            try
            {
                if (startNode.getBehaviourType() == TrafficBehaviourType.NTM
                        || startNode.getBehaviourType() == TrafficBehaviourType.CORDON)
                {
                    // tripsFrom represents the "row" of trips leaving from a zone to all of its destinations
                    // (columns)
                    Map<String, TripInfoTimeDynamic> tripsFrom = trips.get(startNode.getId());
                    if (startNode.getCellBehaviour().getAccumulatedCars() > 0.0)
                    {
                        for (BoundedNode nodeTo : model.getAreaGraph().vertexSet())
                        {
                            if (tripsFrom.containsKey(nodeTo.getId()))
                            {
                                // retrieve the neighbour area on the path to a certain destination
                                BoundedNode neighbour = (BoundedNode) tripsFrom.get(nodeTo.getId()).getNeighbour();
                                if (neighbour != null && tripsFrom.get(nodeTo.getId()).getFlowToNeighbour() > 0.0)
                                {
                                    // retrieve the type of CellBehaviour (showing the demand and supply characteristics
                                    // of
                                    // this neighbour)
                                    CellBehaviour cellBehaviourNeighbour = neighbour.getCellBehaviour();
                                    double tripsToNeighbour = tripsFrom.get(nodeTo.getId()).getFlowToNeighbour();
                                    // compute the share of traffic that wants to enter this Neighbour area from a
                                    // certain
                                    // origin - destination pair as part of the total demand that wants to enter the
                                    // neighbour cell.
                                    double share = tripsToNeighbour / cellBehaviourNeighbour.getDemandToEnter();
                                    // the total supply to the neighbour may be restricted (by calling getSupply that
                                    // provides the maximum Supply). Compute the final flow based on the share of Trips
                                    // to a
                                    // certain destination and this maximum supply of the Cell.
                                    double flowFromDemand = share * cellBehaviourNeighbour.getSupply();
                                    // set the final flow to the neighbour
                                    tripsFrom.get(nodeTo.getId()).setFlowToNeighbour(flowFromDemand);
                                }
                                else
                                {
                                    System.out.println("no route ... or no Trips");
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        // ********************************************************************************************************
        // STEP 5:
        // This step finishes the bookkeeping and transfers traffic to the neighbouring cells based on the flows from
        // the previous step
        for (BoundedNode startNode : model.getAreaGraph().vertexSet())
        {
            try
            {
                if (startNode.getBehaviourType() == TrafficBehaviourType.NTM
                        || startNode.getBehaviourType() == TrafficBehaviourType.CORDON)
                {
                    CellBehaviour cellBehaviour = startNode.getCellBehaviour();
                    // tripsFrom represents the "row" of trips leaving from a zone to all of its destinations
                    // (columns)
                    Map<String, TripInfoTimeDynamic> tripsFrom = trips.get(startNode.getId());
                    for (BoundedNode nodeTo : model.getAreaGraph().vertexSet())
                    {
                        if (tripsFrom.containsKey(nodeTo.getId()))
                        {
                            // retrieve the neighbour area on the path to a certain destination
                            BoundedNode neighbour = (BoundedNode) tripsFrom.get(nodeTo.getId()).getNeighbour();
                            if (neighbour != null)
                            {
                                CellBehaviour cellBehaviourNeighbour = neighbour.getCellBehaviour();
                                // this is the final flow from startNode to the neighbour
                                double flow = tripsFrom.get(nodeTo.getId()).getFlowToNeighbour();
                                // adjust the number of cars in the Cell (leaving)
                                cellBehaviour.setAccumulatedCars(cellBehaviour.getAccumulatedCars() - flow);
                                // and adjust the number of cars in the neighbouring Cell (entering)
                                cellBehaviourNeighbour.setAccumulatedCars(cellBehaviourNeighbour.getAccumulatedCars()
                                        + flow);
                                startNode.getArea().setAccumulatedCars(cellBehaviour.getAccumulatedCars());
                            }
                            else
                            {
                                System.out.println("no route possible ...");
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        // TODO testing
        if (DEBUG)
        {    
            try
            {
                out.close();
            }
            catch (IOException exception)
            {
                exception.printStackTrace();
            }
        }

    }

    private void cellTransmissionTimeStep()
    {

    }

}

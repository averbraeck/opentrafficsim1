package org.opentrafficsim.imb.kpi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.djunits.unit.SpeedUnit;
import org.djunits.value.vdouble.scalar.Dimensionless;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Speed;
import org.djunits.value.vdouble.scalar.Time;
import org.opentrafficsim.imb.IMBException;
import org.opentrafficsim.imb.connector.Connector;
import org.opentrafficsim.imb.connector.Connector.IMBEventType;
import org.opentrafficsim.kpi.interfaces.GtuDataInterface;
import org.opentrafficsim.kpi.sampling.Query;
import org.opentrafficsim.kpi.sampling.TrajectoryGroup;
import org.opentrafficsim.kpi.sampling.indicator.MeanSpeed;
import org.opentrafficsim.kpi.sampling.indicator.MeanTravelTimePerDistance;
import org.opentrafficsim.kpi.sampling.indicator.MeanTripLength;
import org.opentrafficsim.kpi.sampling.indicator.TotalDelay;
import org.opentrafficsim.kpi.sampling.indicator.TotalNumberOfStops;
import org.opentrafficsim.kpi.sampling.indicator.TotalTravelDistance;
import org.opentrafficsim.kpi.sampling.indicator.TotalTravelTime;

/**
 * OTS can publish messages about statistics or Key Performance Indicators (KPIs) of the relation between GTUs and a part of the
 * network. Examples are average speeds, travel times, trip durations, and number of stops. The statistics can be transmitted
 * via IMB at certain intervals. The statistics are based on the classes in the ots.road.sampling package. Most statistics
 * consider a time interval (can be unbound) and a region in space (a collection of lanes; can be the entire network), filtered
 * on metadata such as GTU type, origin, destination, or route.<br>
 * When a statistic is published for the first time, a NEW message is sent to IMB to identify the type of statistic, the time
 * interval and the lane(s) for which the statistic is calculated, and the metadata used to filter the GTUs. The CHANGE message
 * is posted whenever an updated statistic is available, or when the sample frequency time is reached. When a statistic is no
 * longer published, a DELETE event is posted. The Graph NEW messages are posted after the Network NEW, Node NEW, Link NEW, and
 * Lane NEW messages are posted, as it has to be able to identify Lanes.
 * <p>
 * <style>table,th,td {border:1px solid grey; border-style:solid; text-align:left; border-collapse: collapse;}</style>
 * <h2>NEW</h2>
 * <table caption="" style="width:800px;">
 * <thead>
 * <tr>
 * <th style="width:25%;">Variable</th>
 * <th style="width:15%;">Type</th>
 * <th style="width:60%;">Comments</th>
 * </tr>
 * </thead><tbody>
 * <tr>
 * <td>timestamp</td>
 * <td>double</td>
 * <td>time of the event, in simulation time seconds</td>
 * </tr>
 * <tr>
 * <td>statisticId</td>
 * <td>String</td>
 * <td>a unique id for the statistic, e.g. a UUID string</td>
 * </tr>
 * <tr>
 * <td>description</td>
 * <td>String</td>
 * <td>textual description of the statistic</td>
 * </tr>
 * <tr>
 * <td>networkId</td>
 * <td>String</td>
 * <td>id of the Network for which the statistic is made</td>
 * </tr>
 * <tr>
 * <td>numberMetadataEntries</td>
 * <td>int</td>
 * <td>number of metadata entries</td>
 * </tr>
 * <tr>
 * <td>metadataId_1</td>
 * <td>String</td>
 * <td>id of the first metadata entry</td>
 * </tr>
 * <tr>
 * <td>metadataType_1</td>
 * <td>String</td>
 * <td>type of metadata, one of GTUTYPE, ORIGIN, DESTINATION, ROUTE</td>
 * </tr>
 * <tr>
 * <td>metadataValue_1</td>
 * <td>String</td>
 * <td>value of the first metadata entry</td>
 * </tr>
 * <tr>
 * <td>...</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>metadataId_n</td>
 * <td>String</td>
 * <td>id of the last metadata entry</td>
 * </tr>
 * <tr>
 * <td>metadataType_n</td>
 * <td>String</td>
 * <td>type of metadata, one of GTUTYPE, ORIGIN, DESTINATION, ROUTE</td>
 * </tr>
 * <tr>
 * <td>metadataValue_n</td>
 * <td>String</td>
 * <td>value of the last metadata entry</td>
 * </tr>
 * <td>numberSpaceTimeRegions</td>
 * <td>int</td>
 * <td>number of space-time regions for this statistic</td>
 * </tr>
 * <tr>
 * <td>startTime_1</td>
 * <td>double</td>
 * <td>start time for the first space time region, in seconds</td>
 * </tr>
 * <tr>
 * <td>endTime_1</td>
 * <td>double</td>
 * <td>end time for the first space time region, in seconds</td>
 * </tr>
 * <tr>
 * <td>linkId_1</td>
 * <td>String</td>
 * <td>id of the first Link for the space-time region</td>
 * </tr>
 * <tr>
 * <td>laneId_1</td>
 * <td>String</td>
 * <td>id of the first Lane in the link for the space-time region</td>
 * </tr>
 * <tr>
 * <td>...</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>startTime_n</td>
 * <td>double</td>
 * <td>start time for the last space time region, in seconds</td>
 * </tr>
 * <tr>
 * <td>endTime_n</td>
 * <td>double</td>
 * <td>end time for the last space time region, in seconds</td>
 * </tr>
 * <tr>
 * <td>linkId_n</td>
 * <td>String</td>
 * <td>id of the last Link for the space-time region</td>
 * </tr>
 * <tr>
 * <td>laneId_n</td>
 * <td>String</td>
 * <td>id of the last Lane in the link for the space-time region</td>
 * </tr>
 * <tr>
 * <td>connected</td>
 * <td>boolean</td>
 * <td>whether the lanes in the space-time regions are longitudinally connected or not</td>
 * </tr>
 * <tr>
 * <td>transmissionInterval</td>
 * <td>double</td>
 * <td>transmission interval of the statistic in seconds</td>
 * </tr>
 * </tbody>
 * </table>
 * </p>
 * <p>
 * <h2>CHANGE</h2>
 * <table caption="" style="width:800px;">
 * <thead>
 * <tr>
 * <th style="width:25%;">Variable</th>
 * <th style="width:15%;">Type</th>
 * <th style="width:60%;">Comments</th>
 * </tr>
 * </thead><tbody>
 * <tr>
 * <td>timestamp</td>
 * <td>double</td>
 * <td>time of the event, in simulation time seconds</td>
 * </tr>
 * <tr>
 * <td>statisticId</td>
 * <td>String</td>
 * <td>the unique id for the statistic, e.g. a UUID string</td>
 * </tr>
 * <tr>
 * <td>totalGtuDistance</td>
 * <td>double</td>
 * <td>total distance traveled by filtered GTUs in the given time and space, in meters</td>
 * </tr>
 * <tr>
 * <td>totalGtuTravelTime</td>
 * <td>double</td>
 * <td>total travel time by filtered GTUs in the given time and space, in seconds</td>
 * </tr>
 * <tr>
 * <td>averageGtuSpeed</td>
 * <td>double</td>
 * <td>average filtered GTU speed in the given time and space, in meter/second</td>
 * </tr>
 * <tr>
 * <td>averageGtuTravelTimePerDistance</td>
 * <td>double</td>
 * <td>average filtered GTU travel time in the given time and space, in seconds per km</td>
 * </tr>
 * <tr>
 * <td>totalGtuTimeDelay</td>
 * <td>double</td>
 * <td>total time delay incurred by the filtered GTUs in the given time and space, in seconds</td>
 * </tr>
 * <tr>
 * <td>averageTripLength</td>
 * <td>double</td>
 * <td>average length of the trip of the filtered GTUs in the given time and space, in seconds</td>
 * </tr>
 * <tr>
 * <td>totalNumberStops</td>
 * <td>double</td>
 * <td>total number of stops that GTUs made in the given time and space, dimensionless</td>
 * </tr>
 * </tbody>
 * </table>
 * </p>
 * <p>
 * <h2>DELETE</h2>
 * <table caption="" style="width:800px;">
 * <thead>
 * <tr>
 * <th style="width:25%;">Variable</th>
 * <th style="width:15%;">Type</th>
 * <th style="width:60%;">Comments</th>
 * </tr>
 * </thead><tbody>
 * <tr>
 * <td>timestamp</td>
 * <td>double</td>
 * <td>time of the event, in simulation time seconds</td>
 * </tr>
 * <tr>
 * <td>statisticId</td>
 * <td>String</td>
 * <td>the unique id for the statistic that is removed</td>
 * </tr>
 * </tbody>
 * </table>
 * </p>
 * <p>
 * Copyright (c) 2013-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/current/license.html">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Sep 16, 2016 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <G> gtu daat type
 */
public class ImbKpiTransceiver<G extends GtuDataInterface> implements Serializable
{

    /** */
    private static final long serialVersionUID = 20160923L;

    /** IMB connector. */
    private final Connector connector;

    /** The query for the statistic. */
    private final Query<G> query;

    /** The Network id for which the graph is made. */
    private final String networkId;

    /** The interval between generation of graphs. */
    private final Duration transmissionInterval;

    /** Total travel distance. */
    private TotalTravelDistance totalTravelDistance = new TotalTravelDistance();

    /** Total travel time. */
    private TotalTravelTime totalTravelTime = new TotalTravelTime();

    /** Mean speed. */
    private MeanSpeed meanSpeed = new MeanSpeed(this.totalTravelDistance, this.totalTravelTime);

    /** Mean travel time per km. */
    private MeanTravelTimePerDistance meanTravelTimePerDistance = new MeanTravelTimePerDistance(this.meanSpeed);

    /** Mean trip length. */
    private MeanTripLength meanTripLength = new MeanTripLength();

    /** Total delay. */
    private TotalDelay totalDelay = new TotalDelay(new Speed(50.0, SpeedUnit.KM_PER_HOUR));

    /** Total number of stops. */
    private TotalNumberOfStops totalNumberOfStops = new TotalNumberOfStops();

    /** Update time. */
    private Time updateTime = Time.ZERO;

    // TODO implement DELETE message

    /**
     * Construct a new ImbKpiTransceiver.
     * @param connector Connector; the IMB connector
     * @param time Time; time of creation
     * @param networkId String; the network id
     * @param query Query&lt;G&gt;; the statistics query
     * @param transmissionInterval Duration; the interval between generation of graphs
     * @throws IMBException when the post of the IMB message fails
     */
    public ImbKpiTransceiver(final Connector connector, Time time, String networkId, final Query<G> query,
            final Duration transmissionInterval) throws IMBException
    {
        this.connector = connector;
        this.query = query;
        this.networkId = networkId;
        this.transmissionInterval = transmissionInterval;

        List<Object> newMessage = new ArrayList<>();
        newMessage.add(time.si);
        newMessage.add(query.getId());
        newMessage.add(query.toString());
        newMessage.add(this.networkId);
        newMessage.add(0); // TODO numberMetadataEntries
        newMessage.add(0); // TODO numberSpaceTimeRegions
        newMessage.add(false); // TODO "connected" not part of query anymore
        newMessage.add(true); // TODO totalTrajectory
        newMessage.add(transmissionInterval.si);

        this.connector.postIMBMessage("StatisticsGTULane", IMBEventType.NEW, newMessage.toArray());
        sendStatisticsUpdate();
    }

    /**
     * Notifies about time, such that statistics over some period (very recently ended) can be gathered and published.
     * @param time Time; the time to be used in the notification (usually the current time)
     */
    public void notifyTime(Time time)
    {
        if (time.gt(this.updateTime))
        {
            try
            {
                sendStatisticsUpdate();
            }
            catch (IMBException exception)
            {
                throw new RuntimeException("Cannot send statistics update.", exception);
            }
        }
    }

    /**
     * @throws IMBException when the transmission of the IMB message fails
     */
    public void sendStatisticsUpdate() throws IMBException
    {
        List<TrajectoryGroup<G>> groups = this.query.getTrajectoryGroups(this.updateTime);
        Length tdist = this.totalTravelDistance.getValue(this.query, this.updateTime, groups);
        Duration ttt = this.totalTravelTime.getValue(this.query, this.updateTime, groups);
        Speed ms = this.meanSpeed.getValue(this.query, this.updateTime, groups);
        Duration mttpdist = this.meanTravelTimePerDistance.getValue(this.query, this.updateTime, groups);
        Length mtl = this.meanTripLength.getValue(this.query, this.updateTime, groups);
        Duration tdel = this.totalDelay.getValue(this.query, this.updateTime, groups);
        Dimensionless nos = this.totalNumberOfStops.getValue(this.query, this.updateTime, groups);
        double time = this.updateTime.si;
        System.out.println("===== @time " + time + " s on " + this.query.getId() + " =====");
        System.out.println("Total distance " + tdist);
        System.out.println("Total travel time " + ttt);
        System.out.println("Mean speed " + ms);
        System.out.println("Mean travel time " + mttpdist + " (per m)");
        System.out.println("Mean trip length " + mtl);
        System.out.println("Total delay " + tdel);
        System.out.println("Number of stops " + nos);
        this.connector.postIMBMessage("StatisticsGTULane", IMBEventType.CHANGE,
                new Object[] {time, this.query.getId(), tdist.si, ttt.si, ms.si, mttpdist.si, tdel.si, mtl.si, nos.si});
        this.updateTime = this.updateTime.plus(this.transmissionInterval);
    }

}

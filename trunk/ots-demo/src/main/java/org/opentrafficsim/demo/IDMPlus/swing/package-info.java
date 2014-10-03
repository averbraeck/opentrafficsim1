/**
 * <H2>Integrated Lane Change Model with Relaxation and Synchronization (LRMS)</H2>
 * <p>
 * Wouter J. Schakel, Victor L. Knoop and Bart van Arem
 * </p>
 * <p>
 * A proposed lane change model can be integrated with a car-following model to form a complete microscopic driver
 * model. The model resembles traffic better at a macroscopic level, especially regarding the amount of traffic
 * volume per lane, the traffic speeds in different lanes, and the onset of congestion. In a new approach, lane
 * change incentives are combined for determining a lane change desire. Included incentives are to follow a route,
 * to gain speed, and to keep right. Classification of lane changes is based on behavior that depends on the level
 * of lane change desire. Integration with a car-following model is achieved by influencing car-following behavior
 * for relaxation and synchronization, that is, following vehicles in adjacent lanes. Other improvements of the
 * model are trade-offs between lane change incentives and the use of anticipation speed for the speed gain
 * incentive. Although all these effects are captured, the lane change model has only seven parameters. Loop
 * detector data were used to validate and calibrate the model, and an accurate representation of lane distribution
 * and the onset of congestion was shown.
 * </p>
 * <p>
 * Full article available at <A href="http://trb.metapress.com/content/b458k77j4m77233n/fulltext.pdf">TRB</a>. <br>
 * Journal: Transportation Research Record: Journal of the Transportation Research Board<br>
 * Publisher: Transportation Research Board of the National Academies<br>
 * ISSN: 0361-1981 (Print)<br>
 * Subject: Transportation<br>
 * Issue: Volume 2316 / 2012 Traffic Flow Theory and Characteristics 2012: Driver Behavior; Pedestrian and
 * Simulation Modeling, Vol. 2<br>
 * Pages: 47-57
 */

package org.opentrafficsim.demo.IDMPlus.swing;


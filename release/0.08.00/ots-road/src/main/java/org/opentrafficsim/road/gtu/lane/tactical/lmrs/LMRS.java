package org.opentrafficsim.road.gtu.lane.tactical.lmrs;

import java.util.ArrayList;

import nl.tudelft.simulation.language.d3.DirectedPoint;

import org.djunits.unit.AccelerationUnit;
import org.djunits.unit.SpeedUnit;
import org.djunits.value.vdouble.scalar.Acceleration;
import org.djunits.value.vdouble.scalar.Speed;
import org.djunits.value.vdouble.scalar.Time.Abs;
import org.opentrafficsim.core.gtu.GTU;
import org.opentrafficsim.core.gtu.GTUException;
import org.opentrafficsim.core.gtu.drivercharacteristics.ParameterException;
import org.opentrafficsim.core.gtu.drivercharacteristics.ParameterTypeDouble;
import org.opentrafficsim.core.gtu.drivercharacteristics.ParameterTypes;
import org.opentrafficsim.core.gtu.plan.operational.OperationalPlan;
import org.opentrafficsim.core.gtu.plan.operational.OperationalPlanException;
import org.opentrafficsim.core.network.NetworkException;
import org.opentrafficsim.road.gtu.lane.LaneBasedGTU;
import org.opentrafficsim.road.gtu.lane.perception.LanePerception;
import org.opentrafficsim.road.gtu.lane.tactical.AbstractLaneBasedTacticalPlanner;
import org.opentrafficsim.road.gtu.lane.tactical.following.CarFollowingModel;
import org.opentrafficsim.road.gtu.lane.tactical.following.HeadwayGTU;

/**
 * Implementation of the LMRS (Lane change Model with Relaxation and Synchronization).
 * See Schakel, W.J., Knoop, V.L., and Van Arem, B. (2012), 
 * <a href="http://victorknoop.eu/research/papers/TRB2012_LMRS_reviewed.pdf">LMRS: Integrated Lane Change Model with 
 * Relaxation and Synchronization</a>, Transportation Research Records: Journal of the Transportation Research Board, 
 * No. 2316, pp. 47-57. Note in the official versions of TRB and TRR some errors appeared due to the typesetting of the 
 * papers (not in the preprint provided here). A list of errata for the official versions is found 
 * <a href="http://victorknoop.eu/research/papers/Erratum_LMRS.pdf">here</a>.
 * @author Wouter Schakel
 */
public class LMRS extends AbstractLaneBasedTacticalPlanner {

	private static final long serialVersionUID = 20160803L;
	
	/** Free lane change desire threshold. */
	public static final ParameterTypeDouble DFREE = new ParameterTypeDouble("dFree", 
			"Free lane change desire threshold.", 0.365) {
		public void check(double value) throws ParameterException {
			ParameterException.failIf(value<=0, "Parameter of type dFree may not have a negative or zero value.");
			ParameterException.failIf(value>1, "Parameter of type dFree may not have a value greater than 1.");
		}		
	};
	
	/** Synchronized lane change desire threshold. */
	public static final ParameterTypeDouble DSYNC = new ParameterTypeDouble("dSync", 
			"Synchronized lane change desire threshold.", 0.577) {
		public void check(double value) throws ParameterException {
			ParameterException.failIf(value<=0, "Parameter of type dSync may not have a negative or zero value.");
			ParameterException.failIf(value>1, "Parameter of type dSync may not have a value greater than 1.");
		}		
	};
	
	/** Cooperative lane change desire threshold. */
	public static final ParameterTypeDouble DCOOP = new ParameterTypeDouble("dCoop", 
			"Cooperative lane change desire threshold.", 0.788) {
		public void check(double value) throws ParameterException {
			ParameterException.failIf(value<=0, "Parameter of type dCoop may not have a negative or zero value.");
			ParameterException.failIf(value>1, "Parameter of type dCoop may not have a value greater than 1.");
		}		
	};
	
	/** Minimum acceleration for current plan. */
	protected Acceleration minimumAcceleration;
	
	/** List of mandatory lane change incentives. */
	protected ArrayList<MandatoryIncentive> mandatoryIncentives;
	
	/** List of voluntary lane change incentives. */
	protected ArrayList<VoluntaryIncentive> voluntaryIncentives;
	
	/**
	 * Adds a mandatory incentive.
	 * @param incentive Incentive to add.
	 */
	public void addMandatoryIncentive(MandatoryIncentive incentive) {
		this.mandatoryIncentives.add(incentive);
	}
	
	/**
	 * Adds a voluntary incentive.
	 * @param incentive Incentive to add.
	 */
	public void addVoluntaryIncentive(VoluntaryIncentive incentive) {
		this.voluntaryIncentives.add(incentive);
	}
	
	/**
	 * Sets the default lane change incentives. These are the mandatory route incentive, and the voluntary speed and 
	 * keep incentives. Any existing incentives are removed.
	 */
	public void setDefaultIncentives() {
		this.mandatoryIncentives.clear();
		this.voluntaryIncentives.clear();
		this.mandatoryIncentives.add(new IncentiveRoute());
		this.voluntaryIncentives.add(new IncentiveSpeedWithCourtesy());
		this.voluntaryIncentives.add(new IncentiveKeep());
	}
	
	/**
	 * Disables lane changes by clearing all incentives and setting a dummy incentive as mandatory incentive.
	 */
	public void disableLaneChanges() {
		this.mandatoryIncentives.clear();
		this.voluntaryIncentives.clear();
		this.mandatoryIncentives.add(new IncentiveDummy());
	}
	
	@Override
	public OperationalPlan generateOperationalPlan(GTU gtu, Abs startTime, DirectedPoint locationAtStartTime) 
			throws OperationalPlanException, GTUException, NetworkException {
		
		// TODO: Remove this when other todo's are done, it is used as a placeholder where some acceleration needs to be
		// determined.
		Acceleration dummy = new Acceleration(0, AccelerationUnit.SI);
		
		// Check existence of mandatory incentive 
		if (mandatoryIncentives.isEmpty()) {
			throw new OperationalPlanException("At the least the LMRS requires 1 mandatory lane change incentive.");
		}

		// Obtain objects to get info
		LaneBasedGTU gtuLane = (LaneBasedGTU) gtu;
		LanePerception perception = (LanePerception) gtu.getPerception();
		CarFollowingModel dfm = (CarFollowingModel) gtuLane.getStrategicalPlanner().getDrivingCharacteristics().getGTUFollowingModel();
				
		
		// TODO: Throw ParameterException
		Acceleration b;
		double dFree;
		double dSync;
		double dCoop;
		try {
			b = gtuLane.getBehavioralCharacteristics().getAccelerationParameter(ParameterTypes.B);
			dFree = gtuLane.getBehavioralCharacteristics().getParameter(DFREE);
			dSync = gtuLane.getBehavioralCharacteristics().getParameter(DSYNC);
			dCoop = gtuLane.getBehavioralCharacteristics().getParameter(DCOOP);
		} catch (ParameterException pe) {
			throw new RuntimeException(pe);
		}
		
		// Reset stuff that is used in creating a plan
		minimumAcceleration = new Acceleration(Double.POSITIVE_INFINITY, AccelerationUnit.SI);
		
		// Determine tactical plan
		if (Math.random()>.5) { // TODO: if changing lane (rand to disable annoying warnings)
			
			/*
			 * During a lane change, both leaders are followed.
			 */
			lowerAcceleration(dummy);
			lowerAcceleration(dummy);
			
			/*
    		 * Operational plan.
    		 */
			// TODO: Build the operational plan using minimum acceleration
			//LaneOperationalPlanBuilder
			return null;
			
		}
		
		/*
		 * Relaxation
		 */
		// TODO: relaxation
			
		/*
		 * Determine desire
		 * Mandatory is deduced as the maximum of a set of mandatory incentives, while voluntary desires are added.
		 * Depending on the level of mandatory lane change desire, voluntary desire may be included partially. If 
		 * both are positive or negative, voluntary desire is fully included. Otherwise, voluntary desire is less
		 * considered within the range dSync < |mandatory| < dCoop. The absolute value is used as large negative
		 * mandatory desire may also dominate voluntary desire.
		 */
		// Mandatory desire
		double dLeftMandatory = Double.NEGATIVE_INFINITY;
		double dRightMandatory = Double.NEGATIVE_INFINITY;
		for (MandatoryIncentive incentive : this.mandatoryIncentives) {
			Desire d = incentive.determineDesire(gtuLane, perception);
			dLeftMandatory = d.getLeft()>dLeftMandatory ? d.getLeft() : dLeftMandatory;
			dRightMandatory = d.getRight()>dRightMandatory ? d.getRight() : dRightMandatory;
		}
		Desire mandatoryDesire = new Desire(dLeftMandatory, dRightMandatory);
		// Voluntary desire
		double dLeftVoluntary = 0;
		double dRightVoluntary = 0;
		for (VoluntaryIncentive incentive : this.voluntaryIncentives) {
			Desire d = incentive.determineDesire(gtuLane, perception, mandatoryDesire);
			dLeftVoluntary += d.getLeft();
			dRightVoluntary += d.getRight();
		}
		// Total desire
		double thetaLeft = 0;
		if (dLeftMandatory<=dSync || dLeftMandatory*dLeftVoluntary>=0) {
			// low mandatory desire, or same sign
			thetaLeft = 1;
		} else if (dSync<dLeftMandatory && dLeftMandatory<dCoop && dLeftMandatory*dLeftVoluntary<0) {
			// linear from 1 at dSync to 0 at dCoop
			thetaLeft = (dCoop-Math.abs(dLeftMandatory)) / (dCoop-dSync);
		}
		double thetaRight = 0;
		if (dRightMandatory<=dSync || dRightMandatory*dRightVoluntary>=0) {
			// low mandatory desire, or same sign
			thetaRight = 1;
		} else if (dSync<dRightMandatory && dRightMandatory<dCoop && dRightMandatory*dRightVoluntary<0) {
			// linear from 1 at dSync to 0 at dCoop
			thetaRight = (dCoop-Math.abs(dRightMandatory)) / (dCoop-dSync);
		}
		Desire totalDesire = new Desire(dLeftMandatory + thetaLeft*dLeftVoluntary, 
				dRightMandatory + thetaRight*dRightVoluntary);
		
		
		/*
		 * Gap acceptance
		 * The adjacent gap is accepted if acceleration is safe for the potential follower and for this driver.
		 */
		// TODO: get neighboring vehicles, use their (perceived) car-following model with altered headway
		Acceleration aFollow = dummy;
		Acceleration aSelf = dummy;
		boolean leftAllowed = true; // TODO: get from perception / infrastructure, with relaxation
		boolean acceptLeft = aSelf.getSI()>=-b.si*totalDesire.getLeft() && 
                aFollow.getSI()>=-b.si*totalDesire.getLeft() && leftAllowed;
        aFollow = dummy;
		aSelf = dummy;
		boolean rightAllowed = true; // TODO: get from perception / infrastructure, with relaxation
		boolean acceptRight = aSelf.getSI()>=-b.si*totalDesire.getRight() && 
				aFollow.getSI()>=-b.si*totalDesire.getRight() && rightAllowed;
		
        /*
         * Lane change decision
         * A lane change is initiated for the largest desire if this is above the threshold and the gap is accepted.
         * Otherwise, the indicator may be switched on.
         */
		// TODO: switch on indicators (and off)?
        boolean changeLeft = false;
        boolean changeRight = false;
        if (totalDesire.leftIsLargerOrEqual() && totalDesire.getLeft()>=dFree && acceptLeft) {
        	// change left
        	changeLeft = true;
        } else if (!totalDesire.leftIsLargerOrEqual() && totalDesire.getRight()>=dFree && acceptRight) {
        	// change right
        	changeRight = true;
        } else if (totalDesire.leftIsLargerOrEqual() && totalDesire.getLeft()>=dCoop) {
        	// switch on left indicator
        	
        } else if (!totalDesire.leftIsLargerOrEqual() && totalDesire.getRight()>=dCoop) {
        	// switch on right indicator
        	
        }
        
        /*
         * Acceleration
         * Acceleration is determined by the leader, and possibly adjacent vehicles for synchronization and 
         * cooperation.
         */
        // follow leader
        lowerAcceleration(dummy);
        // synchronize
        // TODO: get neighboring vehicles, use car-following model with altered headway
        if (totalDesire.leftIsLargerOrEqual() && totalDesire.getLeft()>=dSync) {
        	// sync left
        	lowerAcceleration(safe(dummy, b));
        } else if (!totalDesire.leftIsLargerOrEqual() && totalDesire.getRight()>=dSync) {
        	// sync right
        	lowerAcceleration(safe(dummy, b));
        }
        // cooperate
        // TODO: get neighboring vehicles, their indicators, use car-following model with altered headway
        if (Math.random()>0) {
        	// cooperate left
        	lowerAcceleration(safe(dummy, b));
        }
        if (Math.random()>0) {
        	// cooperate right
        	lowerAcceleration(safe(dummy, b));
        }
        
        /*
		 * Operational plan.
		 */
		// TODO: Build the operational plan using minimum acceleration and including a possible lane change using 
        // changeLeft/changeRight
		//LaneOperationalPlanBuilder
        return null;
		
	}
	
	protected Acceleration calculateAcceleration(LaneBasedGTU follower, HeadwayGTU leader, double d) {
		// TODO: adjust desired headway based on desire
		// set T
		Acceleration a = calculateAcceleration(follower, leader);
		// reset T
		return a;
	}
	
	protected Acceleration calculateAcceleration(LaneBasedGTU follower, HeadwayGTU leader) {
		// TODO: speed limit
		// TODO: follower != self
		Speed speedLimit = new Speed(0, SpeedUnit.SI);
		return follower.getStrategicalPlanner().getDrivingCharacteristics().getGTUFollowingModel().computeAcceleration(follower.getVelocity(), follower.getMaximumVelocity(), leader.getGtuSpeed(), leader.getDistance(), speedLimit);
	}

	/**
	 * Remembers the lowest acceleration per tactical plan.
	 * @param a Acceleration to remember if lower than any previous acceleration.
	 */
	protected void lowerAcceleration(Acceleration a) {
		if (a.getSI()<this.minimumAcceleration.si) {
			this.minimumAcceleration = a;
		}
	}
	
	/**
	 * Limits the supplied acceleration to safe values, i.e. above or equal to -b.
	 * @param a Acceleration to limit.
	 * @param b Deceleration to limit to.
	 * @return Limited acceleration.
	 */
	protected Acceleration safe(Acceleration a, Acceleration b) {
		if (a.si>=-b.si) {
			return a;
		}
		return new Acceleration(-b.si, AccelerationUnit.SI);
	}

}
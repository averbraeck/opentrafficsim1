package org.opentrafficsim.demo.ntm;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.opentrafficsim.core.unit.SpeedUnit;
import org.opentrafficsim.core.value.vdouble.scalar.DoubleScalar;
import org.opentrafficsim.demo.ntm.fundamentaldiagrams.NetworkFundamentalDiagram;

/**
 * <p>
 * Copyright (c) 2013-2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights
 * reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version 26 Sep 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://Hansvanlint.weblog.tudelft.nl">Hans van Lint</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.citg.tudelft.nl">Guus Tamminga</a>
 * @author <a href="http://www.citg.tudelft.nl">Yufei Yuan</a>
 */
public class CellBehaviourNTM extends CellBehaviour
{
    /** */
    private static final long serialVersionUID = 20140903L;

    /** currentSpeed: average current speed of Cars in this CELL. */
    private DoubleScalar.Abs<SpeedUnit> currentSpeed;

    /** */
    private double maxCapacity;

    /** */
    private double speedSupply;

    /** */
    private double speedDemand;

    /**
     * parametersNTM are: - id ID - accCritical1 low param - accCritical2 high param - accJam jam param - freeSpeed -
     * uncongested speed - roadLength length of all roads.
     */
    private ParametersNTM parametersNTM;

    /** */
    private Area area;

    /**
     * @param parametersNTM contains a set of params
     * @param area that contains this behaviour
     */
    public CellBehaviourNTM(final Area area, final ParametersNTM parametersNTM)
    {
        this.parametersNTM = parametersNTM;
        this.maxCapacity =
                parametersNTM.getAccCritical1() * parametersNTM.getFreeSpeed().getInUnit(SpeedUnit.KM_PER_HOUR);
    }

    /**
     * {@inheritDoc}
     * @param accumulatedCars
     * @param maximumCapacity
     * @param param
     * @return
     */
    // @Override
    public double retrieveSupply(final Double accumulatedCars, final Double maximumCapacity, final ParametersNTM param)
    {
        double carProduction = maximumCapacity;
        if (accumulatedCars > param.getAccCritical2())
        {
            carProduction = retrieveCarProduction(accumulatedCars, maximumCapacity, param);
        }
        double productionSupply = Math.min(maximumCapacity, carProduction); // supply
        return productionSupply;
    }

    /**
     * {@inheritDoc}
     * @param accumulatedCars
     * @param maxCapacity
     * @param param
     * @return
     */
    // @Override
    public double retrieveDemand(final Double accumulatedCars, final Double maximumCapacity, final ParametersNTM param)
    {
        double productionDemand = retrieveCarProduction(accumulatedCars, maximumCapacity, param);
        return productionDemand;
    }

    /** {@inheritDoc} */
    // @Override
    public double computeAccumulation()
    {
        double accumulation = 0.0;
        return accumulation;
    }

    /**
     * Retrieves car production from network fundamental diagram.
     * @param accumulatedCars number of cars in Cell
     * @param maxCapacity 
     * @param param 
     * @return carProduction 
     */
    public final double retrieveCarProduction(final double accumulatedCars, final double maximumCapacity,
            final ParametersNTM param)
    {
        ArrayList<Point2D> xyPairs = new ArrayList<Point2D>();
        Point2D p = new Point2D.Double();
        p.setLocation(0, 0);
        xyPairs.add(p);
        p = new Point2D.Double();
        p.setLocation(param.getAccCritical1(), maximumCapacity);
        xyPairs.add(p);
        p = new Point2D.Double();
        p.setLocation(param.getAccCritical2(), maximumCapacity);
        xyPairs.add(p);
        p = new Point2D.Double();
        p.setLocation(param.getAccJam(), 0);
        xyPairs.add(p);
        double carProduction = NetworkFundamentalDiagram.PieceWiseLinear(xyPairs, accumulatedCars);
        return carProduction;
    }

    /**
     * @return parametersNTM.
     */
    public final ParametersNTM getParametersNTM()
    {
        return this.parametersNTM;
    }

    /**
     * @param parametersNTM set parametersNTM.
     */
    public void setParametersNTM(ParametersNTM parametersNTM)
    {
        this.parametersNTM = parametersNTM;
    }

    /**
     * @return area.
     */
    public final Area getArea()
    {
        return this.area;
    }

    /**
     * @param area set area.
     */
    public void setArea(final Area area)
    {
        this.area = area;
    }

    /**
     * @return averageSpeed
     */
    public final DoubleScalar.Abs<SpeedUnit> getCurrentSpeed()
    {
        return this.currentSpeed;
    }

    /**
     * @return maxCapacity
     */
    public final double getMaxCapacity()
    {
        return this.maxCapacity;
    }

    /**
     * @param maxCapacity set maxCapacity.
     */
    public final void setMaxCapacity(final double maxCapacity)
    {
        this.maxCapacity = maxCapacity;
    }

    /**
     * @param currentSpeed set currentSpeed.
     */
    public final void setCurrentSpeed(final DoubleScalar.Abs<SpeedUnit> currentSpeed)
    {
        this.currentSpeed = currentSpeed;
    }

    /**
     * @param speedSupply set speedSupply.
     */
    public final void setSpeedSupply(final double speedSupply)
    {
        this.speedSupply = speedSupply;
    }

    // this.speedSupply = this.productionSupply / accumulatedCars;
    /**
     * @return speedSupply
     */
    public final double getSpeedSupply()
    {
        return this.speedSupply;
    }

    // this.speedDemand = this.productionDemand / accumulatedCars;
    /**
     * @return speedDemand
     */
    public final double getSpeedDemand()
    {
        return this.speedDemand;
    }

    /**
     * @param speedDemand set speedDemand
     */
    public final void setSpeedDemand(final double speedDemand)
    {
        this.speedDemand = speedDemand;
    }

    /*    *//**
     * not used
     * @param accumulatedCars number of cars in Cell
     */
    /*
     * public final void computeProductionElse(final double accumulatedCars) { double carProduction =
     * retrieveCarProduction(accumulatedCars); double production = Math.min(this.maxCapacity, carProduction); // supply
     * double lowerBoundProduction = Math.max(0.05 * this.maxCapacity, production); double maxDemand =
     * this.parametersNTM.getFreeSpeed().getValueSI() * accumulatedCars; // ask Victor double demand =
     * Math.min(maxDemand, this.maxCapacity); // / demand this.productionElse = Math.min(lowerBoundProduction, demand);
     * // / else this.speedElse = this.productionElse / accumulatedCars; // if (accumulationCars > 0) { //
     * this.currentSpeed = new DoubleScalarAbs<SpeedUnit>(carProduction / accumulatedCars, SpeedUnit.KM_PER_HOUR); // }
     * }
     */
}

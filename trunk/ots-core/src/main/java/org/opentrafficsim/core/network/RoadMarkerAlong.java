package org.opentrafficsim.core.network;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.opentrafficsim.core.gtu.GTUType;
import org.opentrafficsim.core.unit.LengthUnit;
import org.opentrafficsim.core.value.vdouble.scalar.DoubleScalar;

/**
 * <p>
 * Copyright (c) 2013-2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version Oct 25, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public abstract class RoadMarkerAlong extends CrossSectionElement
{
    /** lateral permeability per GTU type and direction. */
    private final Map<GTUType<?>, Set<LateralDirectionality>> permeabilityMap = new HashMap<>();

    /**
     * <b>Note:</b> LEFT is seen as a positive lateral direction, RIGHT as a negative lateral direction, with the direction from
     * the StartNode towards the EndNode as the longitudinal direction.
     * @param parentLink Cross Section Link to which the element belongs.
     * @param lateralCenterPosition the lateral start position compared to the linear geometry of the Cross Section Link.
     * @param beginWidth start width, positioned <i>symmetrically around</i> the lateral start position.
     * @param endWidth end width, positioned <i>symmetrically around</i> the lateral end position.
     */
    public RoadMarkerAlong(final CrossSectionLink<?, ?> parentLink, final DoubleScalar.Abs<LengthUnit> lateralCenterPosition,
            final DoubleScalar.Abs<LengthUnit> beginWidth, final DoubleScalar.Abs<LengthUnit> endWidth)
    {
        super(parentLink, lateralCenterPosition, beginWidth, endWidth);
    }

    /**
     * Add lateral permeability for a GTU type in the direction of the design line of the overarching CrossSectionLink.
     * Therefore, the lateral directionality of one-sided permeability has to be switched for left lanes. This is done because
     * the CrossSectionLink has no idea in which direction vehicles will be moving. On a 1+1 lane road with overtaking
     * possibilities, the longitudinal directionality of both lanes will be BOTH. Example:
     * 
     * <pre>
     * Suppose the design line runs from left to right.
     * 
     * =========================
     * 
     * LANE 1L (BACKWARD)         GTUs are allowed to move to lane 2L 
     *                            Permeability RIGHT is true, although vehicles will go to the LEFT...
     * -------------------------  
     * =========================
     * 
     * LANE 2L (BACKWARD)         GTUs are NOT allowed to move to lane 1L nor to lane 2R
     *                            No permeability defined (empty set)
     * =========================
     * =========================
     * 
     * LANE 2R (FORWARD)          GTUs are NOT allowed to move to lane 1R nor to lane 2L
     *                            No permeability defined (empty set)
     * =========================
     * -------------------------
     * 
     * LANE 1R (FORWARD)          GTUs are allowed to move to lane 2R
     *                            Permeability LEFT is true
     * =========================
     * </pre>
     * 
     * <b>Note:</b> GTUType.ALL can be used to set permeability for all types of GTU at once.
     * <p>
     * @param gtuType GTU type to add permeability for.
     * @param lateralDirection direction to add (LEFT or RIGHT) compared to the direction of the design line.
     */
    public final void addPermeability(final GTUType<?> gtuType, final LateralDirectionality lateralDirection)
    {
        if (!this.permeabilityMap.containsKey(gtuType))
        {
            this.permeabilityMap.put(gtuType, new HashSet<LateralDirectionality>(2));
        }
        this.permeabilityMap.get(gtuType).add(lateralDirection);
    }

    /**
     * @param gtuType GTU type to look for.
     * @param lateralDirection direction to look for (LEFT or RIGHT) compared to the direction of the design line.
     * @return whether the road marker is permeable for the GTU type.
     */
    public final boolean isPermeable(final GTUType<?> gtuType, final LateralDirectionality lateralDirection)
    {
        if (this.permeabilityMap.containsKey(GTUType.ALL))
        {
            return this.permeabilityMap.get(GTUType.ALL).contains(lateralDirection);
        }
        if (!this.permeabilityMap.containsKey(gtuType))
        {
            return false;
        }
        return this.permeabilityMap.get(gtuType).contains(lateralDirection);
    }

}

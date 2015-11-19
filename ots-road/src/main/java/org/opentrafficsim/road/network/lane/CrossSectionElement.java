package org.opentrafficsim.road.network.lane;

import java.io.Serializable;

import javax.media.j3d.Bounds;

import nl.tudelft.simulation.dsol.animation.LocatableInterface;
import nl.tudelft.simulation.language.d3.DirectedPoint;

import org.djunits.value.vdouble.scalar.Length;
import org.opentrafficsim.core.geometry.OTSGeometryException;
import org.opentrafficsim.core.geometry.OTSLine3D;
import org.opentrafficsim.core.geometry.OTSPoint3D;
import org.opentrafficsim.core.network.LateralDirectionality;
import org.opentrafficsim.core.network.NetworkException;

/**
 * <p>
 * Copyright (c) 2013-2015 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate: 2015-09-14 01:33:02 +0200 (Mon, 14 Sep 2015) $, @version $Revision: 1401 $, by $Author: averbraeck $,
 * initial version Aug 19, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.citg.tudelft.nl">Guus Tamminga</a>
 */
public abstract class CrossSectionElement implements LocatableInterface, Serializable
{
    /** */
    private static final long serialVersionUID = 20150826L;

    /** the id. Should be unique within the parentLink. */
    private final String id;

    /** Cross Section Link to which the element belongs. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected final CrossSectionLink parentLink;

    /** The lateral offset from the design line of the parentLink at the start of the parentLink. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected final Length.Rel designLineOffsetAtBegin;

    /** The lateral offset from the design line of the parentLink at the end of the parentLink. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected final Length.Rel designLineOffsetAtEnd;

    /** Start width, positioned <i>symmetrically around</i> the lateral start position. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected final Length.Rel beginWidth;

    /** End width, positioned <i>symmetrically around</i> the lateral end position. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected final Length.Rel endWidth;

    /** The length of the line. Calculated once at the creation. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected final Length.Rel length;

    /** The center line of the element. Calculated once at the creation. */
    private final OTSLine3D centerLine;

    /** The contour of the element. Calculated once at the creation. */
    private final OTSLine3D contour;

    /**
     * <b>Note:</b> LEFT is seen as a positive lateral direction, RIGHT as a negative lateral direction, with the direction from
     * the StartNode towards the EndNode as the longitudinal direction.
     * @param id String; The id of the CrosssSectionElement. Should be unique within the parentLink.
     * @param parentLink CrossSectionLink; Link to which the element belongs.
     * @param lateralOffsetAtBegin DoubleScalar.Rel&lt;LengthUnit&gt;; the lateral offset of the design line of the new
     *            CrossSectionLink with respect to the design line of the parent Link at the start of the parent Link
     * @param lateralOffsetAtEnd DoubleScalar.Rel&lt;LengthUnit&gt;; the lateral offset of the design line of the new
     *            CrossSectionLink with respect to the design line of the parent Link at the end of the parent Link
     * @param beginWidth DoubleScalar.Rel&lt;LengthUnit&gt;; width at start, positioned <i>symmetrically around</i> the design
     *            line
     * @param endWidth DoubleScalar.Rel&lt;LengthUnit&gt;; width at end, positioned <i>symmetrically around</i> the design line
     * @throws OTSGeometryException when creation of the geometry fails
     * @throws NetworkException when id equal to null or not unique
     */
    public CrossSectionElement(final CrossSectionLink parentLink, final String id, final Length.Rel lateralOffsetAtBegin,
            final Length.Rel lateralOffsetAtEnd, final Length.Rel beginWidth, final Length.Rel endWidth)
            throws OTSGeometryException, NetworkException
    {
        super();
        if (id == null)
        {
            throw new NetworkException("Constructor of CrossSectionElement -- id cannot be null");
        }
        for (CrossSectionElement cse : parentLink.getCrossSectionElementList())
        {
            if (cse.getId().equals(id))
            {
                throw new NetworkException("Constructor of CrossSectionElement -- id " + id + " not unique within the Link");
            }
        }
        this.id = id;
        this.parentLink = parentLink;
        this.designLineOffsetAtBegin = lateralOffsetAtBegin;
        this.designLineOffsetAtEnd = lateralOffsetAtEnd;
        this.beginWidth = beginWidth;
        this.endWidth = endWidth;

        this.centerLine =
                this.getParentLink().getDesignLine()
                        .offsetLine(this.designLineOffsetAtBegin.getSI(), this.designLineOffsetAtEnd.getSI());
        this.length = this.centerLine.getLength();
        this.contour = constructContour(this);

        this.parentLink.addCrossSectionElement(this);
    }

    /**
     * @return parentLink.
     */
    public final CrossSectionLink getParentLink()
    {
        return this.parentLink;
    }

    /**
     * Retrieve the lateral offset from the Link design line at the specified longitudinal position.
     * @param fractionalPosition double; fractional longitudinal position on this Lane
     * @return DoubleScalar.Rel&lt;LengthUnit&gt; the lateralCenterPosition at the specified longitudinal position
     */
    public final Length.Rel getLateralCenterPosition(final double fractionalPosition)
    {
        return Length.Rel.interpolate(this.designLineOffsetAtBegin, this.designLineOffsetAtEnd, fractionalPosition);
    }

    /**
     * Retrieve the lateral offset from the Link design line at the specified longitudinal position.
     * @param longitudinalPosition DoubleScalar.Rel&lt;LengthUnit&gt;; the longitudinal position on this Lane
     * @return DoubleScalar.Rel&lt;LengthUnit&gt; the lateralCenterPosition at the specified longitudinal position
     */
    public final Length.Rel getLateralCenterPosition(final Length.Rel longitudinalPosition)
    {
        return getLateralCenterPosition(longitudinalPosition.getSI() / getLength().getSI());
    }

    /**
     * Return the width of this CrossSectionElement at a specified longitudinal position.
     * @param longitudinalPosition DoubleScalar&lt;LengthUnit&gt;; the longitudinal position
     * @return DoubleScalar.Rel&lt;LengthUnit&gt;; the width of this CrossSectionElement at the specified longitudinal position.
     */
    public final Length.Rel getWidth(final Length.Rel longitudinalPosition)
    {
        return getWidth(longitudinalPosition.getSI() / getLength().getSI());
    }

    /**
     * Return the width of this CrossSectionElement at a specified fractional longitudinal position.
     * @param fractionalPosition double; the fractional longitudinal position
     * @return DoubleScalar.Rel&lt;LengthUnit&gt;; the width of this CrossSectionElement at the specified fractional
     *         longitudinal position.
     */
    public final Length.Rel getWidth(final double fractionalPosition)
    {
        return Length.Rel.interpolate(this.beginWidth, this.endWidth, fractionalPosition);
    }

    /**
     * Return the length of this CrossSectionElement as measured along the design line (which equals the center line).
     * @return DoubleScalar.Rel&lt;LengthUnit&gt;; the length of this CrossSectionElement
     */
    public final Length.Rel getLength()
    {
        return this.length;
    }

    /**
     * @return designLineOffsetAtBegin.
     */
    public final Length.Rel getDesignLineOffsetAtBegin()
    {
        return this.designLineOffsetAtBegin;
    }

    /**
     * @return designLineOffsetAtEnd.
     */
    public final Length.Rel getDesignLineOffsetAtEnd()
    {
        return this.designLineOffsetAtEnd;
    }

    /**
     * @return beginWidth.
     */
    public final Length.Rel getBeginWidth()
    {
        return this.beginWidth;
    }

    /**
     * @return endWidth.
     */
    public final Length.Rel getEndWidth()
    {
        return this.endWidth;
    }

    /**
     * @return the z-offset for drawing (what's on top, what's underneath).
     */
    protected abstract double getZ();

    /**
     * @return centerLine.
     */
    public final OTSLine3D getCenterLine()
    {
        return this.centerLine;
    }

    /**
     * @return contour.
     */
    public final OTSLine3D getContour()
    {
        return this.contour;
    }

    /**
     * @return id
     */
    public final String getId()
    {
        return this.id;
    }

    /**
     * Return the lateral offset from the design line of the parent Link of the Left or Right boundary of this
     * CrossSectionElement at the specified fractional longitudinal position.
     * @param lateralDirection LateralDirectionality; LEFT, or RIGHT
     * @param fractionalLongitudinalPosition double; ranges from 0.0 (begin of parentLink) to 1.0 (end of parentLink)
     * @return DoubleScalar.Rel&lt;LengthUnit&gt;
     */
    public final Length.Rel getLateralBoundaryPosition(final LateralDirectionality lateralDirection,
            final double fractionalLongitudinalPosition)
    {
        Length.Rel designLineOffset =
                Length.Rel
                        .interpolate(this.designLineOffsetAtBegin, this.designLineOffsetAtEnd, fractionalLongitudinalPosition);
        Length.Rel halfWidth =
                Length.Rel.interpolate(this.beginWidth, this.endWidth, fractionalLongitudinalPosition).multiplyBy(0.5);
        switch (lateralDirection)
        {
            case LEFT:
                return designLineOffset.minus(halfWidth);
            case RIGHT:
                return designLineOffset.plus(halfWidth);
            default:
                throw new Error("Bad switch on LateralDirectionality " + lateralDirection);
        }
    }

    /**
     * Return the lateral offset from the design line of the parent Link of the Left or Right boundary of this
     * CrossSectionElement at the specified longitudinal position.
     * @param lateralDirection LateralDirectionality; LEFT, or RIGHT
     * @param longitudinalPosition DoubleScalar.Rel&lt;LengthUnit&gt;; the position along the length of this CrossSectionElement
     * @return DoubleScalar.Rel&lt;LengthUnit&gt;
     */
    public final Length.Rel getLateralBoundaryPosition(final LateralDirectionality lateralDirection,
            final Length.Rel longitudinalPosition)
    {
        return getLateralBoundaryPosition(lateralDirection, longitudinalPosition.getSI() / getLength().getSI());
    }

    /**
     * Construct a buffer geometry by offsetting the linear geometry line with a distance and constructing a so-called "buffer"
     * around it.
     * @param cse the CrossSectionElement to construct the contour for
     * @return the geometry belonging to this CrossSectionElement.
     * @throws OTSGeometryException when construction of the geometry fails
     * @throws NetworkException when the resulting contour is degenerate (cannot happen; we hope)
     */
    public static OTSLine3D constructContour(final CrossSectionElement cse) throws OTSGeometryException, NetworkException
    {
        OTSLine3D crossSectionDesignLine =
                cse.getParentLink().getDesignLine()
                        .offsetLine(cse.getDesignLineOffsetAtBegin().getSI(), cse.getDesignLineOffsetAtEnd().getSI());
        OTSLine3D rightBoundary =
                crossSectionDesignLine.offsetLine(-cse.getBeginWidth().getSI() / 2, -cse.getEndWidth().getSI() / 2);
        OTSLine3D leftBoundary =
                crossSectionDesignLine.offsetLine(cse.getBeginWidth().getSI() / 2, cse.getEndWidth().getSI() / 2);
        OTSPoint3D[] result = new OTSPoint3D[rightBoundary.size() + leftBoundary.size() + 1];
        int resultIndex = 0;
        for (int index = 0; index < rightBoundary.size(); index++)
        {
            result[resultIndex++] = rightBoundary.get(index);
        }
        for (int index = leftBoundary.size(); --index >= 0;)
        {
            result[resultIndex++] = leftBoundary.get(index);
        }
        result[resultIndex] = rightBoundary.get(0); // close the contour
        return OTSLine3D.createAndCleanOTSLine3D(result);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public DirectedPoint getLocation()
    {
        DirectedPoint centroid = this.contour.getLocation();
        return new DirectedPoint(centroid.x, centroid.y, getZ());
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public Bounds getBounds()
    {
        return this.contour.getBounds();
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return String.format("CSE offset %.2fm..%.2fm, width %.2fm..%.2fm", this.designLineOffsetAtBegin.getSI(),
                this.designLineOffsetAtEnd.getSI(), this.beginWidth.getSI(), this.endWidth.getSI());
    }

    /** {@inheritDoc} */
    @SuppressWarnings("checkstyle:designforextension")
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.parentLink == null) ? 0 : this.parentLink.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "checkstyle:designforextension", "checkstyle:needbraces" })
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CrossSectionElement other = (CrossSectionElement) obj;
        if (this.id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.parentLink == null)
        {
            if (other.parentLink != null)
                return false;
        }
        else if (!this.parentLink.equals(other.parentLink))
            return false;
        return true;
    }

}

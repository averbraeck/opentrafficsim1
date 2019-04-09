package org.opentrafficsim.road.network.lane;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.media.j3d.Bounds;

import nl.tudelft.simulation.dsol.animation.LocatableInterface;
import nl.tudelft.simulation.language.d3.DirectedPoint;

import org.djunits.value.vdouble.scalar.Length;
import org.opentrafficsim.core.geometry.OTSGeometry;
import org.opentrafficsim.core.geometry.OTSGeometryException;
import org.opentrafficsim.core.geometry.OTSLine3D;
import org.opentrafficsim.core.geometry.OTSPoint3D;
import org.opentrafficsim.core.geometry.OTSPolygon3D;
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

    /** The offsets and widths at positions along the line, relative to the design line of the parent link. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected final List<CrossSectionSlice> crossSectionSlices;

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
     * @param crossSectionSlices The offsets and widths at positions along the line, relative to the design line of the parent
     *            link. If there is just one with and offset, there should just be one element in the list with Length.Rel = 0.
     *            If there are more slices, the last one should be at the length of the design line. If not, a NetworkException
     *            is thrown.
     * @throws OTSGeometryException when creation of the geometry fails
     * @throws NetworkException when id equal to null or not unique, or there are multiple slices and the last slice does not
     *             end at the length of the design line.
     */
    public CrossSectionElement(final CrossSectionLink parentLink, final String id,
            final List<CrossSectionSlice> crossSectionSlices) throws OTSGeometryException, NetworkException
    {
        if (parentLink == null)
        {
            throw new NetworkException("Constructor of CrossSectionElement for id " + id + ", parentLink cannot be null");
        }
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

        this.crossSectionSlices = new ArrayList<>(crossSectionSlices); // copy of list with immutable slices
        if (this.crossSectionSlices.size() == 0)
        {
            throw new NetworkException("CrossSectionElement " + id + " is created with zero slices for " + parentLink);
        }
        if (this.crossSectionSlices.get(0).getRelativeLength().si != 0.0)
        {
            throw new NetworkException("CrossSectionElement " + id + " for " + parentLink
                    + " has a first slice with relativeLength is not equal to 0.0");
        }
        if (this.crossSectionSlices.size() > 1
                && this.crossSectionSlices.get(this.crossSectionSlices.size() - 1).getRelativeLength()
                        .ne(this.parentLink.getLength()))
        {
            throw new NetworkException("CrossSectionElement " + id + " for " + parentLink
                    + " has a last slice with relativeLength is not equal to the length of the parent link");
        }

        if (this.crossSectionSlices.size() <= 2)
        {
            this.centerLine =
                    this.getParentLink().getDesignLine()
                            .offsetLine(getDesignLineOffsetAtBegin().getSI(), getDesignLineOffsetAtEnd().getSI());
        }
        else
        {
            double[] relativeFractions = new double[this.crossSectionSlices.size()];
            double[] offsets = new double[this.crossSectionSlices.size()];
            for (int i = 0; i < this.crossSectionSlices.size(); i++)
            {
                relativeFractions[i] = this.crossSectionSlices.get(i).getRelativeLength().si / this.parentLink.getLength().si;
                offsets[i] = this.crossSectionSlices.get(i).getDesignLineOffset().si;
            }
            this.centerLine = this.getParentLink().getDesignLine().offsetLine(relativeFractions, offsets);
        }

        this.length = this.centerLine.getLength();
        this.contour = constructContour(this);

        this.parentLink.addCrossSectionElement(this);
        // This commented-out code was used to find the cause of an unexpected lane narrowing in the testod.xodr network.
        // if (new OTSPolygon3D(this.contour.getPoints()).contains(new OTSPoint3D(-180, -150)))
        // {
        // System.out.println("Contour contains p: " + this);
        // OTSLine3D crossSectionDesignLine =
        // getParentLink().getDesignLine()
        // .offsetLine(getDesignLineOffsetAtBegin().getSI(), getDesignLineOffsetAtEnd().getSI());
        // System.out.println(OTSGeometry.printCoordinates("#reference:\nc0,0,0\n#", crossSectionDesignLine, "\n    "));
        // OTSLine3D rightBoundary =
        // crossSectionDesignLine.offsetLine(-getBeginWidth().getSI() / 2, -getEndWidth().getSI() / 2);
        // System.out.println(OTSGeometry.printCoordinates("#right boundary:\nc0,1,0\n#", rightBoundary, "\n    "));
        // constructContour(this);
        // }
    }

    /**
     * <b>Note:</b> LEFT is seen as a positive lateral direction, RIGHT as a negative lateral direction, with the direction from
     * the StartNode towards the EndNode as the longitudinal direction.
     * @param id String; The id of the CrosssSectionElement. Should be unique within the parentLink.
     * @param parentLink CrossSectionLink; Link to which the element belongs.
     * @param lateralOffsetAtBegin Length.Rel; the lateral offset of the design line of the new CrossSectionLink with respect to
     *            the design line of the parent Link at the start of the parent Link
     * @param lateralOffsetAtEnd Length.Rel; the lateral offset of the design line of the new CrossSectionLink with respect to
     *            the design line of the parent Link at the end of the parent Link
     * @param beginWidth Length.Rel; width at start, positioned <i>symmetrically around</i> the design line
     * @param endWidth Length.Rel; width at end, positioned <i>symmetrically around</i> the design line
     * @throws OTSGeometryException when creation of the geometry fails
     * @throws NetworkException when id equal to null or not unique
     */
    public CrossSectionElement(final CrossSectionLink parentLink, final String id, final Length.Rel lateralOffsetAtBegin,
            final Length.Rel lateralOffsetAtEnd, final Length.Rel beginWidth, final Length.Rel endWidth)
            throws OTSGeometryException, NetworkException
    {
        this(parentLink, id, Arrays.asList(new CrossSectionSlice[] {
                new CrossSectionSlice(Length.Rel.ZERO, lateralOffsetAtBegin, beginWidth),
                new CrossSectionSlice(parentLink.getLength(), lateralOffsetAtEnd, endWidth) }));
    }

    /**
     * <b>Note:</b> LEFT is seen as a positive lateral direction, RIGHT as a negative lateral direction, with the direction from
     * the StartNode towards the EndNode as the longitudinal direction.
     * @param id String; The id of the CrosssSectionElement. Should be unique within the parentLink.
     * @param parentLink CrossSectionLink; Link to which the element belongs.
     * @param lateralOffset Length.Rel; the lateral offset of the design line of the new CrossSectionLink with respect to the
     *            design line of the parent Link
     * @param width Length.Rel; width, positioned <i>symmetrically around</i> the design line
     * @throws OTSGeometryException when creation of the geometry fails
     * @throws NetworkException when id equal to null or not unique
     */
    public CrossSectionElement(final CrossSectionLink parentLink, final String id, final Length.Rel lateralOffset,
            final Length.Rel width) throws OTSGeometryException, NetworkException
    {
        this(parentLink, id, Arrays.asList(new CrossSectionSlice[] { new CrossSectionSlice(Length.Rel.ZERO, lateralOffset,
                width) }));
    }

    /**
     * @return parentLink.
     */
    public final CrossSectionLink getParentLink()
    {
        return this.parentLink;
    }

    /**
     * Calculate the slice the fractional position is in.
     * @param fractionalPosition the fractional position between 0 and 1 compared to the design line
     * @return the lower slice number between 0 and number of slices - 1.
     */
    private int calculateSliceNumber(final double fractionalPosition)
    {
        double linkLength = this.parentLink.getLength().si;
        for (int i = 0; i < this.crossSectionSlices.size() - 1; i++)
        {
            if (fractionalPosition >= this.crossSectionSlices.get(i).getRelativeLength().si / linkLength
                    && fractionalPosition <= this.crossSectionSlices.get(i + 1).getRelativeLength().si / linkLength)
            {
                return i;
            }
        }
        return this.crossSectionSlices.size() - 2;
    }

    /**
     * Retrieve the lateral offset from the Link design line at the specified longitudinal position.
     * @param fractionalPosition double; fractional longitudinal position on this Lane
     * @return Length.Rel the lateralCenterPosition at the specified longitudinal position
     */
    public final Length.Rel getLateralCenterPosition(final double fractionalPosition)
    {
        if (this.crossSectionSlices.size() == 1)
        {
            return this.getDesignLineOffsetAtBegin();
        }
        if (this.crossSectionSlices.size() == 2)
        {
            return Length.Rel.interpolate(this.getDesignLineOffsetAtBegin(), this.getDesignLineOffsetAtEnd(),
                    fractionalPosition);
        }
        int sliceNr = calculateSliceNumber(fractionalPosition);
        return Length.Rel.interpolate(this.crossSectionSlices.get(sliceNr).getDesignLineOffset(),
                this.crossSectionSlices.get(sliceNr + 1).getDesignLineOffset(), fractionalPosition
                        - this.crossSectionSlices.get(sliceNr).getRelativeLength().si / this.parentLink.getLength().si);
    }

    /**
     * Retrieve the lateral offset from the Link design line at the specified longitudinal position.
     * @param longitudinalPosition Length.Rel; the longitudinal position on this Lane
     * @return Length.Rel the lateralCenterPosition at the specified longitudinal position
     */
    public final Length.Rel getLateralCenterPosition(final Length.Rel longitudinalPosition)
    {
        return getLateralCenterPosition(longitudinalPosition.getSI() / getLength().getSI());
    }

    /**
     * Return the width of this CrossSectionElement at a specified longitudinal position.
     * @param longitudinalPosition DoubleScalar&lt;LengthUnit&gt;; the longitudinal position
     * @return Length.Rel; the width of this CrossSectionElement at the specified longitudinal position.
     */
    public final Length.Rel getWidth(final Length.Rel longitudinalPosition)
    {
        return getWidth(longitudinalPosition.getSI() / getLength().getSI());
    }

    /**
     * Return the width of this CrossSectionElement at a specified fractional longitudinal position.
     * @param fractionalPosition double; the fractional longitudinal position
     * @return Length.Rel; the width of this CrossSectionElement at the specified fractional longitudinal position.
     */
    public final Length.Rel getWidth(final double fractionalPosition)
    {
        if (this.crossSectionSlices.size() == 1)
        {
            return this.getBeginWidth();
        }
        if (this.crossSectionSlices.size() == 2)
        {
            return Length.Rel.interpolate(this.getBeginWidth(), this.getEndWidth(), fractionalPosition);
        }
        int sliceNr = calculateSliceNumber(fractionalPosition);
        return Length.Rel.interpolate(this.crossSectionSlices.get(sliceNr).getWidth(), this.crossSectionSlices.get(sliceNr + 1)
                .getWidth(),
                fractionalPosition - this.crossSectionSlices.get(sliceNr).getRelativeLength().si
                        / this.parentLink.getLength().si);
    }

    /**
     * Return the length of this CrossSectionElement as measured along the design line (which equals the center line).
     * @return Length.Rel; the length of this CrossSectionElement
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
        return this.crossSectionSlices.get(0).getDesignLineOffset();
    }

    /**
     * @return designLineOffsetAtEnd.
     */
    public final Length.Rel getDesignLineOffsetAtEnd()
    {
        return this.crossSectionSlices.get(this.crossSectionSlices.size() - 1).getDesignLineOffset();
    }

    /**
     * @return beginWidth.
     */
    public final Length.Rel getBeginWidth()
    {
        return this.crossSectionSlices.get(0).getWidth();
    }

    /**
     * @return endWidth.
     */
    public final Length.Rel getEndWidth()
    {
        return this.crossSectionSlices.get(this.crossSectionSlices.size() - 1).getWidth();
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
     * @return Length.Rel
     */
    public final Length.Rel getLateralBoundaryPosition(final LateralDirectionality lateralDirection,
            final double fractionalLongitudinalPosition)
    {
        Length.Rel designLineOffset;
        Length.Rel halfWidth;
        if (this.crossSectionSlices.size() <= 2)
        {
            designLineOffset =
                    Length.Rel.interpolate(getDesignLineOffsetAtBegin(), getDesignLineOffsetAtEnd(),
                            fractionalLongitudinalPosition);
            halfWidth = Length.Rel.interpolate(getBeginWidth(), getEndWidth(), fractionalLongitudinalPosition).multiplyBy(0.5);
        }
        else
        {
            int sliceNr = calculateSliceNumber(fractionalLongitudinalPosition);
            double startFractionalPosition =
                    this.crossSectionSlices.get(sliceNr).getRelativeLength().si / this.parentLink.getLength().si;
            designLineOffset =
                    Length.Rel.interpolate(this.crossSectionSlices.get(sliceNr).getDesignLineOffset(), this.crossSectionSlices
                            .get(sliceNr + 1).getDesignLineOffset(), fractionalLongitudinalPosition - startFractionalPosition);
            halfWidth =
                    Length.Rel.interpolate(this.crossSectionSlices.get(sliceNr).getWidth(),
                            this.crossSectionSlices.get(sliceNr + 1).getWidth(),
                            fractionalLongitudinalPosition - startFractionalPosition).multiplyBy(0.5);
        }

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
     * @param longitudinalPosition Length.Rel; the position along the length of this CrossSectionElement
     * @return Length.Rel
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
        OTSPoint3D[] result = null;

        if (cse.crossSectionSlices.size() <= 2)
        {
            OTSLine3D crossSectionDesignLine =
                    cse.getParentLink().getDesignLine()
                            .offsetLine(cse.getDesignLineOffsetAtBegin().getSI(), cse.getDesignLineOffsetAtEnd().getSI());
            OTSLine3D rightBoundary =
                    crossSectionDesignLine.offsetLine(-cse.getBeginWidth().getSI() / 2, -cse.getEndWidth().getSI() / 2);
            OTSLine3D leftBoundary =
                    crossSectionDesignLine.offsetLine(cse.getBeginWidth().getSI() / 2, cse.getEndWidth().getSI() / 2);
            result = new OTSPoint3D[rightBoundary.size() + leftBoundary.size() + 1];
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
        }

        else

        {
            List<OTSPoint3D> resultList = new ArrayList<>();
            for (int i = 0; i < cse.crossSectionSlices.size() - 1; i++)
            {
                double plLength = cse.getParentLink().getLength().si;
                double so = cse.crossSectionSlices.get(i).getDesignLineOffset().si;
                double eo = cse.crossSectionSlices.get(i + 1).getDesignLineOffset().si;
                double sw2 = cse.crossSectionSlices.get(i).getWidth().si / 2.0;
                double ew2 = cse.crossSectionSlices.get(i + 1).getWidth().si / 2.0;
                double sf = cse.crossSectionSlices.get(i).getRelativeLength().si / plLength;
                double ef = cse.crossSectionSlices.get(i + 1).getRelativeLength().si / plLength;
                OTSLine3D crossSectionDesignLine =
                        cse.getParentLink().getDesignLine().extractFractional(sf, ef).offsetLine(so, eo);
                OTSLine3D rightBoundary = crossSectionDesignLine.offsetLine(-sw2, -ew2);
                OTSLine3D leftBoundary = crossSectionDesignLine.offsetLine(sw2, ew2);
                for (int index = 0; index < rightBoundary.size(); index++)
                {
                    resultList.add(rightBoundary.get(index));
                }
                for (int index = leftBoundary.size(); --index >= 0;)
                {
                    resultList.add(leftBoundary.get(index));
                }
            }
            resultList.add(resultList.get(0)); // close the contour
            result = resultList.toArray(new OTSPoint3D[] {});
        }

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
        return String.format("CSE offset %.2fm..%.2fm, width %.2fm..%.2fm", getDesignLineOffsetAtBegin().getSI(),
                getDesignLineOffsetAtEnd().getSI(), getBeginWidth().getSI(), getEndWidth().getSI());
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
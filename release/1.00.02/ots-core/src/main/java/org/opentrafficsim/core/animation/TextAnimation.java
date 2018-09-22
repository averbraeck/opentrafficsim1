package org.opentrafficsim.core.animation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.io.Serializable;
import java.rmi.RemoteException;

import javax.media.j3d.Bounds;
import javax.naming.NamingException;

import org.opentrafficsim.core.logger.SimLogger;

import nl.tudelft.simulation.dsol.animation.Locatable;
import nl.tudelft.simulation.dsol.animation.D2.Renderable2D;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.language.d3.BoundingBox;
import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * Display a text for another Locatable object.
 * <p>
 * Copyright (c) 2013-2018 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/current/license.html">OpenTrafficSim License</a>.
 * </p>
 * $LastChangedDate: 2015-07-24 02:58:59 +0200 (Fri, 24 Jul 2015) $, @version $Revision: 1147 $, by $Author: averbraeck $,
 * initial version Dec 11, 2016 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public abstract class TextAnimation implements Locatable, Serializable
{
    /** */
    private static final long serialVersionUID = 20161211L;

    /** The object for which the text is displayed. */
    private final Locatable source;

    /** The text to display. */
    private String text;

    /** The horizontal movement of the text, in meters. */
    private final float dx;

    /** The vertical movement of the text, in meters. */
    private final float dy;

    /** Whether to center or not. */
    private final TextAlignment textAlignment;

    /** The color of the text. */
    private Color color;

    /** FontSize the size of the font; default = 2.0 (meters). */
    private final float fontSize;

    /** The animation implementation. */
    private final AnimationImpl animationImpl;

    /** The font. */
    private Font font;

    /** The font rectangle. */
    private Rectangle2D fontRectangle = null;

    /**
     * @param source Locatable; the object for which the text is displayed
     * @param text String; the text to display
     * @param dx float; the horizontal movement of the text, in meters
     * @param dy float; the vertical movement of the text, in meters
     * @param textAlignment TextAlignment; where to place the text
     * @param color Color; the color of the text
     * @param fontSize float; the size of the font; default = 2.0 (meters)
     * @param simulator SimulatorInterface.TimeDoubleUnit; the simulator
     * @throws NamingException when animation context cannot be created or retrieved
     * @throws RemoteException when remote context cannot be found
     */
    @SuppressWarnings("checkstyle:parameternumber")
    public TextAnimation(final Locatable source, final String text, final float dx, final float dy,
            final TextAlignment textAlignment, final Color color, final float fontSize,
            final SimulatorInterface.TimeDoubleUnit simulator) throws RemoteException, NamingException
    {
        this.source = source;
        this.text = text;
        this.dx = dx;
        this.dy = dy;
        this.textAlignment = textAlignment;
        this.color = color;
        this.fontSize = fontSize;

        this.font = new Font("SansSerif", Font.PLAIN, 2);
        if (this.fontSize != 2.0f)
        {
            this.font = this.font.deriveFont(this.fontSize);
        }

        this.animationImpl = new AnimationImpl(this, simulator);
    }

    /**
     * @param source Locatable; the object for which the text is displayed
     * @param text String; the text to display
     * @param dx float; the horizontal movement of the text, in meters
     * @param dy float; the vertical movement of the text, in meters
     * @param textAlignment TextAlignment; where to place the text
     * @param color Color; the color of the text
     * @param simulator SimulatorInterface.TimeDoubleUnit; the simulator
     * @throws NamingException when animation context cannot be created or retrieved
     * @throws RemoteException when remote context cannot be found
     */
    public TextAnimation(final Locatable source, final String text, final float dx, final float dy,
            final TextAlignment textAlignment, final Color color, final SimulatorInterface.TimeDoubleUnit simulator)
            throws RemoteException, NamingException
    {
        this(source, text, dx, dy, textAlignment, color, 2.0f, simulator);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public DirectedPoint getLocation() throws RemoteException
    {
        // draw always on top.
        DirectedPoint p = this.source.getLocation();
        return new DirectedPoint(p.x, p.y, Double.MAX_VALUE, 0.0, 0.0, p.getRotZ());
    }

    /** {@inheritDoc} */
    @Override
    public final Bounds getBounds() throws RemoteException
    {
        return new BoundingBox(0.0, 0.0, 0.0);
    }

    /**
     * paint() method so it can be overridden or extended.
     * @param graphics Graphics2D; the graphics object
     * @param observer ImageObserver; the observer
     * @throws RemoteException on network exception
     */
    @SuppressWarnings("checkstyle:designforextension")
    public void paint(final Graphics2D graphics, final ImageObserver observer) throws RemoteException
    {
        graphics.setFont(this.font);
        synchronized (this.font)
        {
            if (this.fontRectangle == null)
            {
                FontMetrics fm = graphics.getFontMetrics();
                this.fontRectangle = fm.getStringBounds(this.text, graphics);
            }
            graphics.setColor(this.color);
            float dxText =
                    this.textAlignment.equals(TextAlignment.LEFT) ? 0.0f : this.textAlignment.equals(TextAlignment.CENTER)
                            ? (float) -this.fontRectangle.getWidth() / 2.0f : (float) -this.fontRectangle.getWidth();
            graphics.drawString(this.text, dxText + this.dx, this.fontSize / 2.0f - this.dy);
        }
    }

    /**
     * Destroy the text animation.
     */
    public final void destroy()
    {
        try
        {
            this.animationImpl.destroy();
        }
        catch (NamingException exception)
        {
            SimLogger.always().warn(exception, "Tried to destroy Text for GTU animation of GTU {}", this.source.toString());
        }
    }

    /**
     * Clone the TextAnimation and return a copy for the new source on the new simulator.
     * @param newSource Locatable; the new source to link to the text animation
     * @param newSimulator SimulatorInterface.TimeDoubleUnit; the new simulator to register the animation on
     * @return TextAnimation; a copy of this TextAnimation
     * @throws RemoteException when remote animation cannot be reached
     * @throws NamingException when animation name cannot be found or bound in the Context
     */
    public abstract TextAnimation clone(Locatable newSource, SimulatorInterface.TimeDoubleUnit newSimulator)
            throws RemoteException, NamingException;

    /**
     * Retrieve the source.
     * @return Locatable; the source
     */
    protected final Locatable getSource()
    {
        return this.source;
    }

    /**
     * Retrieve dx.
     * @return float; the value of dx
     */
    protected final float getDx()
    {
        return this.dx;
    }

    /**
     * Retrieve dy.
     * @return float; the value of dy
     */
    protected final float getDy()
    {
        return this.dy;
    }

    /**
     * Retrieve the text alignment.
     * @return TextAlignment; the text alignment
     */
    protected final TextAlignment getTextAlignment()
    {
        return this.textAlignment;
    }

    /**
     * Retrieve the font size.
     * @return float; the font size
     */
    protected final float getFontSize()
    {
        return this.fontSize;
    }

    /**
     * Retrieve the font.
     * @return Font; the font
     */
    protected final Font getFont()
    {
        return this.font;
    }

    /**
     * Retrieve the current text.
     * @return String; the current text
     */
    protected final String getText()
    {
        return this.text;
    }

    /**
     * Update the text.
     * @param text String; the new text
     */
    protected final void setText(final String text)
    {
        this.text = text;
        synchronized (this.font)
        {
            this.fontRectangle = null;
        }
    }

    /**
     * Retrieve the current color.
     * @return Color; the current color
     */
    protected final Color getColor()
    {
        return this.color;
    }

    /**
     * Update the color.
     * @param color Color; the new color
     */
    protected final void setColor(final Color color)
    {
        this.color = color;
    }

    /**
     * Retrieve the current flip status.
     * @return boolean; the current flip status
     */
    public final boolean isFlip()
    {
        return this.animationImpl.isFlip();
    }

    /**
     * Update the flip status.
     * @param flip boolean; the new flip status
     */
    public final void setFlip(final boolean flip)
    {
        this.animationImpl.setFlip(flip);
    }

    /**
     * Retrieve the current rotation status.
     * @return boolean; the current rotation status
     */
    public final boolean isRotate()
    {
        return this.animationImpl.isRotate();
    }

    /**
     * Update the rotation status.
     * @param rotate boolean; the new rotation status
     */
    public final void setRotate(final boolean rotate)
    {
        this.animationImpl.setRotate(rotate);

    }

    /**
     * Retrieve the current scale status.
     * @return boolean; the current scale status
     */
    public final boolean isScale()
    {
        return this.animationImpl.isScale();
    }

    /**
     * Update the scale status.
     * @param scale boolean; the new scale status
     */
    public final void setScale(final boolean scale)
    {
        this.animationImpl.setScale(scale);
    }

    /**
     * Retrieve the current translate status.
     * @return boolean; the current translate status
     */
    public final boolean isTranslate()
    {
        return this.animationImpl.isTranslate();
    }

    /**
     * Update the translate status.
     * @param translate boolean; the new translate status
     */
    public final void setTranslate(final boolean translate)
    {
        this.animationImpl.setTranslate(translate);
    }

    /**
     * The implementation of the text animation. Cloning will be taken care of by the overarching TextAnimation-derived class.
     * <p>
     * Copyright (c) 2013-2018 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved.
     * <br>
     * BSD-style license. See <a href="http://opentrafficsim.org/docs/current/license.html">OpenTrafficSim License</a>.
     * </p>
     * $LastChangedDate: 2015-07-24 02:58:59 +0200 (Fri, 24 Jul 2015) $, @version $Revision: 1147 $, by $Author: averbraeck $,
     * initial version Dec 11, 2016 <br>
     * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
     * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
     */
    private static class AnimationImpl extends Renderable2D<Locatable> implements Serializable
    {
        /** */
        private static final long serialVersionUID = 20170400L;

        /**
         * Construct a new AnimationImpl.
         * @param source Locatable; the source
         * @param simulator SimulatorInterface.TimeDoubleUnit; the simulator
         * @throws NamingException when animation context cannot be created or retrieved
         * @throws RemoteException when remote context cannot be found
         */
        AnimationImpl(final Locatable source, final SimulatorInterface.TimeDoubleUnit simulator)
                throws NamingException, RemoteException
        {
            super(source, simulator);
        }

        /** {@inheritDoc} */
        @Override
        public final void paint(final Graphics2D graphics, final ImageObserver observer) throws RemoteException
        {
            TextAnimation ta = ((TextAnimation) getSource());
            ta.paint(graphics, observer);
        }

        /** {@inheritDoc} */
        @Override
        public boolean contains(final Point2D pointWorldCoordinates, final Rectangle2D extent, final Dimension screen)
        {
            return false;
        }

        /** {@inheritDoc} */
        @Override
        public final String toString()
        {
            return "TextAnimation.AnimationImpl []";
        }

    }
}

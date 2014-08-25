package org.opentrafficsim.graphs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import org.jfree.chart.ChartPanel;
import org.junit.Test;
import org.opentrafficsim.car.Car;
import org.opentrafficsim.car.following.CarFollowingModel;
import org.opentrafficsim.core.dsol.OTSDEVSSimulator;
import org.opentrafficsim.core.unit.LengthUnit;
import org.opentrafficsim.core.unit.SpeedUnit;
import org.opentrafficsim.core.unit.TimeUnit;
import org.opentrafficsim.core.value.vdouble.scalar.DoubleScalarAbs;
import org.opentrafficsim.core.value.vdouble.scalar.DoubleScalarRel;

/**
 * <p>
 * Copyright (c) 2002-2014 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
 * <p>
 * The OpenTrafficSim project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is" and any express or implied warranties,
 * including, but not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the copyright holder or contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or
 * services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability,
 * whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use
 * of this software, even if advised of the possibility of such damage.
 * @version Aug 25, 2014 <br>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class FundamentalDiagramPlotTest
{
    /**
     * Test the FundamentalDiagram
     */
    @SuppressWarnings("static-method")
    @Test
    public void FundamentalDiagramTest()
    {
        DoubleScalarRel<TimeUnit> aggregationTime = new DoubleScalarRel<TimeUnit>(30, TimeUnit.SECOND);
        DoubleScalarAbs<LengthUnit> position = new DoubleScalarAbs<LengthUnit>(123, LengthUnit.METER);
        FundamentalDiagram fd;
        for (int numberOfLanes = -2; numberOfLanes <= 0; numberOfLanes++)
        {
            try
            {
                fd = new FundamentalDiagram("Fundamental Diagram", -1, aggregationTime, position);
                fail("Bad number of lanes should have thrown an Error");
            }
            catch (Error e)
            {
                // Ignore
            }
        }
        try
        {
            fd =
                    new FundamentalDiagram("Fundamental Diagram", 1, new DoubleScalarRel<TimeUnit>(0, TimeUnit.SECOND),
                            position);
            fail("Bad number of lanes should have thrown an Error");
        }
        catch (Error e)
        {
            // Ignore
        }
        for (int numberOfLanes = 1; numberOfLanes <= 3; numberOfLanes++)
        {
            fd = new FundamentalDiagram("Fundamental Diagram", numberOfLanes, aggregationTime, position);
            assertEquals("SeriesCount should match numberOfLanes", numberOfLanes, fd.getSeriesCount());
            assertEquals("Position should match the supplied position", position.getValueSI(), fd.getPosition()
                    .getValueSI(), 0.0001);
            try
            {
                fd.getXValue(-1, 0);
                fail("Bad series should have thrown an Error");
            }
            catch (Error e)
            {
                // Ignore
            }
            try
            {
                fd.getXValue(numberOfLanes, 0);
                fail("Bad series should have thrown an Error");
            }
            catch (Error e)
            {
                // Ignore
            }
            double value = fd.getXValue(0, 0);
            assertTrue("No data should result in NaN", Double.isNaN(value));
            value = fd.getX(0, 0).doubleValue();
            assertTrue("No data should result in NaN", Double.isNaN(value));
            value = fd.getYValue(0, 0);
            assertTrue("No data should result in NaN", Double.isNaN(value));
            value = fd.getY(0, 0).doubleValue();
            assertTrue("No data should result in NaN", Double.isNaN(value));
            ActionEvent setXToSpeed = new ActionEvent(fd, 0, "Speed/Speed");
            ActionEvent resetAxis = new ActionEvent(fd, 0, "Flow/Density");
            DoubleScalarRel<SpeedUnit> speed = new DoubleScalarRel<SpeedUnit>(100, SpeedUnit.KM_PER_HOUR);
            DoubleScalarAbs<TimeUnit> time = new DoubleScalarAbs<TimeUnit>(123, TimeUnit.SECOND);
            int bucket = (int) Math.floor(time.getValueSI() / aggregationTime.getValueSI());
            for (int lane = 0; lane < numberOfLanes; lane++)
            {
                Car car = new Car(1 + lane, null, null, time, new DoubleScalarAbs<LengthUnit>(23, LengthUnit.METER), speed);
                fd.addData(lane, car, time);
                for (int readBackLane = 0; readBackLane < numberOfLanes; readBackLane++)
                {
                    for (int sample = 0; sample < 10; sample++)
                    {
                        boolean shouldHaveData = readBackLane <= lane && sample == bucket;
                        value = fd.getXValue(readBackLane, sample);
                        if (shouldHaveData)
                        {
                            double expectedDensity = 3600 / aggregationTime.getValueSI() / speed.getValueSI();
                            assertEquals("Density should be " + expectedDensity, expectedDensity, value, 0.00001);
                        }
                        else
                            assertTrue("Data should be NaN", Double.isNaN(value));
                        value = fd.getX(readBackLane, sample).doubleValue();
                        if (shouldHaveData)
                        {
                            double expectedDensity = 3600 / aggregationTime.getValueSI() / speed.getValueSI();
                            assertEquals("Density should be " + expectedDensity, expectedDensity, value, 0.00001);
                        }
                        else
                            assertTrue("Data should be NaN", Double.isNaN(value));
                        shouldHaveData = readBackLane <= lane && sample <= bucket;
                        value = fd.getYValue(readBackLane, sample);
                        if (shouldHaveData)
                        {
                            double expectedFlow = readBackLane <= lane && sample == bucket ? 3600 / aggregationTime.getValueSI() : 0;
                            assertEquals("Flow should be " + expectedFlow, expectedFlow, value, 0.00001);
                        }
                        else
                            assertTrue("Data should be NaN", Double.isNaN(value));
                        value = fd.getY(readBackLane, sample).doubleValue();
                        if (shouldHaveData)
                        {
                            double expectedFlow = readBackLane <= lane && sample == bucket ? 3600 / aggregationTime.getValueSI() : 0;
                            assertEquals("Flow should be " + expectedFlow, expectedFlow, value, 0.00001);
                        }
                        else
                            assertTrue("Data should be NaN", Double.isNaN(value));
                        fd.actionPerformed(setXToSpeed);
                        value = fd.getYValue(readBackLane, sample);
                        if (shouldHaveData)
                        {
                            double expectedSpeed = readBackLane <= lane && sample == bucket ? speed.getValueInUnit() : 0;
                            assertEquals("Speed should be " + expectedSpeed, expectedSpeed, value, 0.00001);
                        }
                        else
                            assertTrue("Data should be NaN", Double.isNaN(value));
                        value = fd.getY(readBackLane, sample).doubleValue();
                        if (shouldHaveData)
                        {
                            double expectedSpeed = readBackLane <= lane && sample == bucket ? speed.getValueInUnit() : 0;
                            assertEquals("Speed should be " + expectedSpeed, expectedSpeed, value, 0.00001);
                        }
                        else
                            assertTrue("Data should be NaN", Double.isNaN(value));
                        fd.actionPerformed(resetAxis);
                    }
                }
            }
            // Check that harmonic mean speed is computed
            speed = new DoubleScalarRel<SpeedUnit>(10, SpeedUnit.KM_PER_HOUR);
            Car car = new Car(1234, null, null, time, new DoubleScalarAbs<LengthUnit>(23, LengthUnit.METER), speed);
            fd.addData(0, car, time);
            fd.actionPerformed(setXToSpeed);
            value = fd.getYValue(0, bucket);
            double expected = 2d / (1d / 100 + 1d / 10);
            //System.out.println("harmonic speed is " + value + ", expected is " + expected);
            assertEquals("Harmonic mean of 10 and 100 is " + expected, expected, value, 0.0001);
            // Test the actionPerformed method with various malformed ActionEvents.
            try
            {
                fd.actionPerformed(new ActionEvent(fd, 0, "bla"));
                fail("Bad ActionEvent should have thrown an Error");
            }
            catch (Error e)
            {
                // Ignore
            }
            try
            {
                fd.actionPerformed(new ActionEvent(fd, 0, "Speed/bla"));
                fail("Bad ActionEvent should have thrown an Error");
            }
            catch (Error e)
            {
                // Ignore
            }
            try
            {
                fd.actionPerformed(new ActionEvent(fd, 0, "Flow/bla"));
                fail("Bad ActionEvent should have thrown an Error");
            }
            catch (Error e)
            {
                // Ignore
            }
            try
            {
                fd.actionPerformed(new ActionEvent(fd, 0, "Density/bla"));
                fail("Bad ActionEvent should have thrown an Error");
            }
            catch (Error e)
            {
                // Ignore
            }
            try
            {
                fd.actionPerformed(new ActionEvent(fd, 0, "bla/Speed"));
                fail("Bad ActionEvent should have thrown an Error");
            }
            catch (Error e)
            {
                // Ignore
            }
            try
            {
                fd.actionPerformed(new ActionEvent(fd, 0, "bla/Flow"));
                fail("Bad ActionEvent should have thrown an Error");
            }
            catch (Error e)
            {
                // Ignore
            }
            try
            {
                fd.actionPerformed(new ActionEvent(fd, 0, "bla/Density"));
                fail("Bad ActionEvent should have thrown an Error");
            }
            catch (Error e)
            {
                // Ignore
            }
        }
    }
    
    /**
     * Test the updateHint method in the PointerHandler
     */
    @SuppressWarnings("static-method")
    @Test
    public void testHints()
    {
        DoubleScalarRel<TimeUnit> aggregationTime = new DoubleScalarRel<TimeUnit>(30, TimeUnit.SECOND);
        DoubleScalarAbs<LengthUnit> position = new DoubleScalarAbs<LengthUnit>(123, LengthUnit.METER);
        FundamentalDiagram fd = new FundamentalDiagram("Fundamental Diagram", 1, aggregationTime, position);
        // First get the panel that stores the result of updateHint (this is ugly)
        JLabel hintPanel = null;
        ChartPanel chartPanel = null;
        for (Component c0 : fd.getComponents())
            for (Component c1 : ((Container) c0).getComponents())
                if (c1 instanceof Container)
                    for (Component c2 : ((Container) c1).getComponents())
                    {
                        //System.out.println("c2 is " + c2);
                        if (c2 instanceof Container)
                            for (Component c3 : ((Container) c2).getComponents())
                            {
                                //System.out.println("c3 is " + c3);
                                if (c3 instanceof JLabel)
                                    if (null == hintPanel)
                                        hintPanel = (JLabel) c3;
                                    else
                                        fail("There should be only one JPanel in a FundamentalDiagram");
                                if (c3 instanceof ChartPanel)
                                    if (null == chartPanel)
                                        chartPanel = (ChartPanel) c3;
                                    else
                                        fail("There should be only one ChartPanel in a FundamentalDiagram");
                            }
                    }
        if (null == hintPanel)
            fail("Could not find a JLabel in FundamentalDiagram");
        if (null == chartPanel)
            fail("Could not find a ChartPanel in FundamentalDiagram");
        assertEquals("Initially the text should be a single space", " ", hintPanel.getText());
        PointerHandler ph = null;
        for (MouseListener ml : chartPanel.getMouseListeners())
            if (ml instanceof PointerHandler)
                if (null == ph)
                    ph = (PointerHandler) ml;
                else
                    fail("There should be only one PointerHandler on the chartPanel");
        if (null == ph)
            fail("Could not find the PointerHandler for the chartPanel");
        ph.updateHint(1, 2);
        //System.out.println("Hint text is now " + hintPanel.getText());
        assertFalse("Hint should not be a single space", " ".equals(hintPanel.getText()));
        ph.updateHint(Double.NaN, Double.NaN);
        assertEquals("The text should again be a single space", " ", hintPanel.getText());
    }
}

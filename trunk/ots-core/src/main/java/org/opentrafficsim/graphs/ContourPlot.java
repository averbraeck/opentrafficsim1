package org.opentrafficsim.graphs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYZDataset;
import org.opentrafficsim.car.Car;
import org.opentrafficsim.core.unit.TimeUnit;
import org.opentrafficsim.core.value.vdouble.scalar.DoubleScalar;

/**
 * Common code for a contour plot. <br />
 * The data collection code for acceleration assumes constant acceleration during the evaluation period of the GTU.
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
 * @version Jul 16, 2014 <br>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public abstract class ContourPlot extends JFrame implements ActionListener, XYZDataset
{
    /** */
    private static final long serialVersionUID = 20140716L;

    /** The ChartPanel for this ContourPlot. */
    protected final ChartPanel chartPanel;

    /** Area to show status information. */
    protected final JLabel statusLabel;

    /** Definition of the X-axis. */
    protected final Axis xAxis;

    /** Definition of the Y-axis. */
    protected final Axis yAxis;

    /** Time granularity values. */
    protected static final double[] STANDARDTIMEGRANULARITIES = {1, 2, 5, 10, 20, 30, 60, 120, 300, 600};

    /** Index of the initial time granularity in standardTimeGranularites. */
    protected static final int STANDARDINITIALTIMEGRANULARITYINDEX = 3;

    /** Distance granularity values. */
    protected static final double[] STANDARDDISTANCEGRANULARITIES = {10, 20, 50, 100, 200, 500, 1000};

    /** Index of the initial distance granularity in standardTimeGranularites. */
    protected static final int STANDARDINITIALDISTANCEGRANULARITYINDEX = 3;

    /** Initial lower bound for the time scale. */
    protected static final DoubleScalar.Abs<TimeUnit> INITIALLOWERTIMEBOUND = new DoubleScalar.Abs<TimeUnit>(0,
            TimeUnit.SECOND);

    /** Initial upper bound for the time scale. */
    protected static final DoubleScalar.Abs<TimeUnit> INITIALUPPERTIMEBOUND = new DoubleScalar.Abs<TimeUnit>(300,
            TimeUnit.SECOND);

    /**
     * Create a new ContourPlot.
     * @param caption String; text to show above the plotting area
     * @param xAxis Axis; the X (time) axis
     * @param yAxis Axis; the Y axis
     * @param redValue Double; contour value that will be rendered in Red
     * @param yellowValue Double; contour value that will be rendered in Yellow
     * @param greenValue Double; contour value that will be rendered in Green
     * @param valueFormat String; format string for the contour values
     * @param legendFormat String; format string for the captions in the color legend
     * @param legendStep Double; increment between color legend entries
     */
    public ContourPlot(final String caption, final Axis xAxis, final Axis yAxis, final double redValue,
            final double yellowValue, final double greenValue, final String valueFormat, final String legendFormat,
            final double legendStep)
    {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        extendXRange(xAxis.getMaximumValue());
        double[] boundaries = {redValue, yellowValue, greenValue};
        this.chartPanel = new ChartPanel(createChart(caption, valueFormat, this, boundaries, legendFormat, legendStep));
        final PointerHandler ph = new PointerHandler()
        {
            /**
             * @see org.opentrafficsim.graphs.PointerHandler#updateHint(double, double)
             */
            @Override
            void updateHint(final double domainValue, final double rangeValue)
            {
                if (Double.isNaN(domainValue))
                {
                    ContourPlot.this.statusLabel.setText(" ");
                    return;
                }
                XYPlot plot = (XYPlot) ContourPlot.this.chartPanel.getChart().getPlot();
                XYZDataset dataset = (XYZDataset) plot.getDataset();
                String value = "";
                double roundedTime = domainValue;
                double roundedDistance = rangeValue;
                for (int item = dataset.getItemCount(0); --item >= 0;)
                {
                    double x = dataset.getXValue(0, item);
                    if (x + xAxis.getCurrentGranularity() / 2 < domainValue
                            || x - xAxis.getCurrentGranularity() / 2 >= domainValue)
                    {
                        continue;
                    }
                    double y = dataset.getYValue(0, item);
                    if (y + yAxis.getCurrentGranularity() / 2 < rangeValue
                            || y - yAxis.getCurrentGranularity() / 2 >= rangeValue)
                    {
                        continue;
                    }
                    roundedTime = x;
                    roundedDistance = y;
                    double valueUnderMouse = dataset.getZValue(0, item);
                    // System.out.println("Value under mouse is " + valueUnderMouse);
                    if (Double.isNaN(valueUnderMouse))
                    {
                        break;
                    }
                    String format =
                            ((ContinuousColorPaintScale) (((XYBlockRenderer) (plot.getRenderer(0))).getPaintScale())).format;
                    value = String.format(format, valueUnderMouse);
                }
                ContourPlot.this.statusLabel.setText(String.format("time %.0fs, distance %.0fm, %s", roundedTime,
                        roundedDistance, value));
            }

        };
        this.chartPanel.addMouseMotionListener(ph);
        this.chartPanel.addMouseListener(ph);
        this.chartPanel.setMouseWheelEnabled(true);
        add(this.chartPanel, BorderLayout.CENTER);
        this.statusLabel = new JLabel(" ", SwingConstants.CENTER);
        add(this.statusLabel, BorderLayout.SOUTH);
        final JPopupMenu popupMenu = this.chartPanel.getPopupMenu();
        popupMenu.insert(
                buildMenu("Distance granularity", "%.0f m", "setDistanceGranularity", yAxis.granularities,
                        yAxis.getCurrentGranularity()), 0);
        popupMenu.insert(
                buildMenu("Time granularity", "%.0f s", "setTimeGranularity", xAxis.granularities,
                        xAxis.getCurrentGranularity()), 1);
        reGraph();
    }

    /**
     * Create a JMenu to let the user set the granularity of the XYBlockChart.
     * @param caption String; caption for the new JMenu
     * @param format String; format string for the values in the items under the new JMenu
     * @param commandPrefix String; prefix for the actionCommand of the items under the new JMenu
     * @param values double[]; array of values to be formatted using the format strings to yield the items under the new
     *            JMenu
     * @param currentValue double; the currently selected value (used to put the bullet on the correct item)
     * @return JMenu with JRadioMenuItems for the values and a bullet on the currentValue item
     */
    private JMenu buildMenu(final String caption, final String format, final String commandPrefix,
            final double[] values, final double currentValue)
    {
        final JMenu result = new JMenu(caption);
        // Enlighten me: Do the menu items store a reference to the ButtonGroup so it won't get garbage collected?
        final ButtonGroup group = new ButtonGroup();
        for (double value : values)
        {
            final JRadioButtonMenuItem item = new JRadioButtonMenuItem(String.format(format, value));
            item.setSelected(value == currentValue);
            item.setActionCommand(commandPrefix + String.format(Locale.US, " %f", value));
            item.addActionListener(this);
            result.add(item);
            group.add(item);
        }
        return result;
    }

    /**
     * Create a XYBlockChart.
     * @param caption String; text to show above the chart
     * @param valueFormat String; format string used to render the value under the mouse in the status bar
     * @param dataset XYZDataset with the values to render
     * @param boundaries double[]; array of three boundary values corresponding to Red, Yellow and Green
     * @param legendFormat String; the format string for captions in the legend
     * @param legendStep value difference for successive colors in the legend. The first legend value displayed is equal
     *            to the lowest value in boundaries.
     * @return JFreeChart; the new XYBlockChart
     */
    private static JFreeChart createChart(final String caption, final String valueFormat, final XYZDataset dataset,
            final double[] boundaries, final String legendFormat, final double legendStep)
    {
        final NumberAxis xAxis = new NumberAxis("\u2192 " + "time [s]");
        xAxis.setLowerMargin(0.0);
        xAxis.setUpperMargin(0.0);
        final NumberAxis yAxis = new NumberAxis("\u2192 " + "Distance [m]");
        yAxis.setAutoRangeIncludesZero(false);
        yAxis.setLowerMargin(0.0);
        yAxis.setUpperMargin(0.0);
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        XYBlockRenderer renderer = new XYBlockRenderer();
        final Color[] colorValues = {Color.RED, Color.YELLOW, Color.GREEN};
        final ContinuousColorPaintScale paintScale =
                new ContinuousColorPaintScale(valueFormat, boundaries, colorValues);
        renderer.setPaintScale(paintScale);
        final XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        final LegendItemCollection legend = new LegendItemCollection();
        for (int i = 0;; i++)
        {
            double value = paintScale.getLowerBound() + i * legendStep;
            if (value > paintScale.getUpperBound())
            {
                break;
            }
            legend.add(new LegendItem(String.format(legendFormat, value), paintScale.getPaint(value)));
        }
        legend.add(new LegendItem("No data", Color.BLACK));
        plot.setFixedLegendItems(legend);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        final JFreeChart chart = new JFreeChart(caption, plot);
        chart.setBackgroundPaint(Color.white);
        return chart;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent actionEvent)
    {
        final String command = actionEvent.getActionCommand();
        // System.out.println("command is \"" + command + "\"");
        String[] fields = command.split("[ ]");
        if (fields.length == 2)
        {
            final NumberFormat nf = NumberFormat.getInstance(Locale.US);
            double value;
            try
            {
                value = nf.parse(fields[1]).doubleValue();
            }
            catch (ParseException e)
            {
                throw new Error("Bad value: " + fields[1]);
            }
            if (fields[0].equalsIgnoreCase("setDistanceGranularity"))
            {
                this.yAxis.setCurrentGranularity(value);
            }
            else if (fields[0].equalsIgnoreCase("setTimeGranularity"))
            {
                this.xAxis.setCurrentGranularity(value);
            }
            else
            {
                throw new Error("Unknown ActionEvent");
            }
            reGraph();
        }
        else
        {
            throw new Error("Unknown ActionEvent: " + command);
        }
    }

    /**
     * Redraw this ContourGraph (after the underlying data, or a granularity setting has been changed).
     */
    public void reGraph()
    {
        notifyListeners(new DatasetChangeEvent(this, null)); // This guess work actually works!
        final XYPlot plot = this.chartPanel.getChart().getXYPlot();
        plot.notifyListeners(new PlotChangeEvent(plot));
        final XYBlockRenderer blockRenderer = (XYBlockRenderer) plot.getRenderer();
        blockRenderer.setBlockHeight(this.yAxis.getCurrentGranularity());
        blockRenderer.setBlockWidth(this.xAxis.getCurrentGranularity());
    }

    /**
     * Notify interested parties of an event affecting this ContourPlot.
     * @param event
     */
    private void notifyListeners(final DatasetChangeEvent event)
    {
        for (DatasetChangeListener dcl : this.listenerList.getListeners(DatasetChangeListener.class))
        {
            dcl.datasetChanged(event);
        }
    }

    /** List of parties interested in changes of this ContourPlot. */
    transient EventListenerList listenerList = new EventListenerList();

    /**
     * @see org.jfree.data.general.SeriesDataset#getSeriesCount()
     */
    @Override
    public int getSeriesCount()
    {
        return 1;
    }

    /**
     * Retrieve the number of cells to use along the distance axis.
     * @return Integer; the number of cells to use along the distance axis
     */
    protected int yAxisBins()
    {
        return this.yAxis.getAggregatedBinCount();
    }

    /**
     * Return the y-axis bin number (the row number) of an item. <br />
     * Do not rely on the (current) fact that the data is stored column by column!
     * @param item Integer; the item
     * @return Integer; the bin number along the y axis of the item
     */
    protected int yAxisBin(final int item)
    {
        if (item < 0 || item >= getItemCount(0))
        {
            throw new Error("yAxisBin: item out of range (value is " + item + "), valid range is 0.." + getItemCount(0));
        }
        return item % yAxisBins();
    }

    /**
     * Return the x-axis bin number (the column number) of an item. <br />
     * Do not rely on the (current) fact that the data is stored column by column!
     * @param item Integer; the item
     * @return Integer; the bin number along the x axis of the item
     */
    protected int xAxisBin(final int item)
    {
        if (item < 0 || item >= getItemCount(0))
        {
            throw new Error("xAxisBin: item out of range (value is " + item + "), valid range is 0.." + getItemCount(0));
        }
        return item / yAxisBins();
    }

    /**
     * Retrieve the number of cells to use along the time axis.
     * @return Integer; the number of cells to use along the time axis
     */
    protected int xAxisBins()
    {
        return this.xAxis.getAggregatedBinCount();
    }

    /**
     * @see org.jfree.data.xy.XYDataset#getItemCount(int)
     */
    @Override
    public int getItemCount(final int series)
    {
        return yAxisBins() * xAxisBins();
    }

    /**
     * @see org.jfree.data.xy.XYDataset#getX(int, int)
     */
    @Override
    public Number getX(final int series, final int item)
    {
        return getXValue(series, item);
    }

    /**
     * @see org.jfree.data.xy.XYDataset#getXValue(int, int)
     */
    @Override
    public double getXValue(final int series, final int item)
    {
        double result = this.xAxis.getValue(xAxisBin(item));
        // System.out.println(String.format("XValue(%d, %d) -> %.3f, binCount=%d", series, item, result,
        // this.yAxisDefinition.getAggregatedBinCount()));
        return result;
    }

    /**
     * @see org.jfree.data.xy.XYDataset#getY(int, int)
     */
    @Override
    public Number getY(final int series, final int item)
    {
        return getYValue(series, item);
    }

    /**
     * @see org.jfree.data.xy.XYDataset#getYValue(int, int)
     */
    @Override
    public double getYValue(final int series, final int item)
    {
        return this.yAxis.getValue(yAxisBin(item));
    }

    /**
     * @see org.jfree.data.xy.XYZDataset#getZ(int, int)
     */
    @Override
    public Number getZ(final int series, final int item)
    {
        return getZValue(series, item);
    }

    /**
     * @see org.jfree.data.general.Dataset#addChangeListener(org.jfree.data.general.DatasetChangeListener)
     */
    @Override
    public void addChangeListener(final DatasetChangeListener listener)
    {
        this.listenerList.add(DatasetChangeListener.class, listener);
    }

    /**
     * @see org.jfree.data.general.Dataset#removeChangeListener(org.jfree.data.general.DatasetChangeListener)
     */
    @Override
    public void removeChangeListener(final DatasetChangeListener listener)
    {
        this.listenerList.remove(DatasetChangeListener.class, listener);
    }

    /**
     * @see org.jfree.data.general.Dataset#getGroup()
     */
    @Override
    public DatasetGroup getGroup()
    {
        return null;
    }

    /**
     * @see org.jfree.data.general.Dataset#setGroup(org.jfree.data.general.DatasetGroup)
     */
    @Override
    public void setGroup(final DatasetGroup group)
    {
        // ignore
    }

    /**
     * @see org.jfree.data.general.SeriesDataset#indexOf(java.lang.Comparable)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public int indexOf(final Comparable seriesKey)
    {
        return 0;
    }

    /**
     * @see org.jfree.data.xy.XYDataset#getDomainOrder()
     */
    @Override
    public DomainOrder getDomainOrder()
    {
        return DomainOrder.ASCENDING;
    }

    /**
     * Add a fragment of a trajectory to this ContourPlot.
     * @param car Car; the GTU that is being sampled TODO: replace Car by GTU
     */
    public void addData(final Car car)
    {
        final DoubleScalar.Abs<TimeUnit> fromTime = car.getLastEvaluationTime();
        final DoubleScalar.Abs<TimeUnit> toTime = car.getNextEvaluationTime();
        if (toTime.getValueSI() > this.xAxis.getMaximumValue().getValueSI())
        {
            extendXRange(toTime);
            this.xAxis.adjustMaximumValue(toTime);
        }
        if (toTime.getValueSI() <= fromTime.getValueSI()) // degenerate sample???
        {
            return;
        }
        /*-
        System.out.println(String.format("addData: fromTime=%.1f, toTime=%.1f, fromDist=%.2f, toDist=%.2f", fromTime
                .getValueSI(), toTime.getValueSI(), car.position(fromTime).getValueSI(), car.position(toTime)
                .getValueSI()));
         */
        // The "relative" values are "counting" distance or time in the minimum bin size unit
        final double relativeFromDistance =
                (car.getPosition(fromTime).getValueSI() - this.yAxis.getMinimumValue().getValueSI())
                        / this.yAxis.granularities[0];
        final double relativeToDistance =
                (car.getPosition(toTime).getValueSI() - this.yAxis.getMinimumValue().getValueSI())
                        / this.yAxis.granularities[0];
        double relativeFromTime =
                (fromTime.getValueSI() - this.xAxis.getMinimumValue().getValueSI()) / this.xAxis.granularities[0];
        final double relativeToTime =
                (toTime.getValueSI() - this.xAxis.getMinimumValue().getValueSI()) / this.xAxis.granularities[0];
        final int fromTimeBin = (int) Math.floor(relativeFromTime);
        final int toTimeBin = (int) Math.floor(relativeToTime) + 1;
        double relativeMeanSpeed = (relativeToDistance - relativeFromDistance) / (relativeToTime - relativeFromTime);
        // FIXME: The code for acceleration assumes that acceleration is constant (which is correct for IDM+, but may be
        // wrong for other car following algorithms).
        double acceleration = car.getAcceleration(car.getLastEvaluationTime()).getValueSI();
        for (int timeBin = fromTimeBin; timeBin < toTimeBin; timeBin++)
        {
            if (timeBin < 0)
            {
                continue;
            }
            double binEndTime = timeBin + 1;
            if (binEndTime > relativeToTime)
            {
                binEndTime = relativeToTime;
            }
            if (binEndTime <= relativeFromTime)
            {
                continue; // no time spent in this timeBin
            }
            double binDistanceStart =
                    (car.getPosition(
                            new DoubleScalar.Abs<TimeUnit>(relativeFromTime * this.xAxis.granularities[0],
                                    TimeUnit.SECOND)).getValueSI() - this.yAxis.getMinimumValue().getValueSI())
                            / this.yAxis.granularities[0];
            double binDistanceEnd =
                    (car.getPosition(
                            new DoubleScalar.Abs<TimeUnit>(binEndTime * this.xAxis.granularities[0], TimeUnit.SECOND))
                            .getValueSI() - this.yAxis.getMinimumValue().getValueSI())
                            / this.yAxis.granularities[0];

            // Compute the time in each distanceBin
            for (int distanceBin = (int) Math.floor(binDistanceStart); distanceBin <= binDistanceEnd; distanceBin++)
            {
                double relativeDuration = 1;
                if (relativeFromTime > timeBin)
                {
                    relativeDuration -= relativeFromTime - timeBin;
                }
                if (distanceBin == (int) Math.floor(binDistanceEnd))
                {
                    // This GTU does not move out of this distanceBin before the binEndTime
                    if (binEndTime < timeBin + 1)
                    {
                        relativeDuration -= timeBin + 1 - binEndTime;
                    }
                }
                else
                {
                    // This GTU moves out of this distanceBin before the binEndTime
                    // Interpolate the time when this GTU crosses into the next distanceBin
                    // Using f.i. Newton-Rhaphson interpolation would yield a slightly more precise result...
                    double timeToBinBoundary = (distanceBin + 1 - binDistanceStart) / relativeMeanSpeed;
                    double endTime = relativeFromTime + timeToBinBoundary;
                    relativeDuration -= timeBin + 1 - endTime;
                }
                final double duration = relativeDuration * this.xAxis.granularities[0];
                final double distance = duration * relativeMeanSpeed * this.yAxis.granularities[0];
                /*-
                System.out.println(String.format("tb=%d, db=%d, t=%.2f, d=%.2f", timeBin, distanceBin, duration,
                        distance));
                 */
                incrementBinData(timeBin, distanceBin, duration, distance, acceleration);
                relativeFromTime += relativeDuration;
                binDistanceStart = distanceBin + 1;
            }
            relativeFromTime = timeBin + 1;
        }

    }

    /**
     * Increase storage for sample data. <br />
     * This is only implemented for the time axis.
     * @param newUpperLimit DoubleScalar<?> new upper limit for the X range
     */
    public abstract void extendXRange(DoubleScalar<?> newUpperLimit);

    /**
     * Increment the data of one bin.
     * @param timeBin Integer; the rank of the bin on the time-scale
     * @param distanceBin Integer; the rank of the bin on the distance-scale
     * @param duration Double; the time spent in this bin
     * @param distanceCovered Double; the distance covered in this bin
     * @param acceleration Double; the average acceleration in this bin
     */
    public abstract void incrementBinData(int timeBin, int distanceBin, double duration, double distanceCovered,
            double acceleration);

    /**
     * @see org.jfree.data.xy.XYZDataset#getZValue(int, int)
     */
    @Override
    public double getZValue(final int series, final int item)
    {
        final int timeBinGroup = xAxisBin(item);
        final int distanceBinGroup = yAxisBin(item);
        // System.out.println(String.format("getZValue(s=%d, i=%d) -> tbg=%d, dbg=%d", series, item, timeBinGroup,
        // distanceBinGroup));
        final int timeGroupSize = (int) (this.xAxis.getCurrentGranularity() / this.xAxis.granularities[0]);
        final int firstTimeBin = timeBinGroup * timeGroupSize;
        final int distanceGroupSize = (int) (this.yAxis.getCurrentGranularity() / this.yAxis.granularities[0]);
        final int firstDistanceBin = distanceBinGroup * distanceGroupSize;
        final int endTimeBin = Math.min(firstTimeBin + timeGroupSize, this.xAxis.getBinCount());
        final int endDistanceBin = Math.min(firstDistanceBin + distanceGroupSize, this.yAxis.getBinCount());
        return computeZValue(firstTimeBin, endTimeBin, firstDistanceBin, endDistanceBin);
    }

    /**
     * Combine values in a range of time bins and distance bins to obtain a combined density value of the ranges.
     * @param firstTimeBin Integer; the first time bin to use
     * @param endTimeBin Integer; one higher than the last time bin to use
     * @param firstDistanceBin Integer; the first distance bin to use
     * @param endDistanceBin Integer; one higher than the last distance bin to use
     * @return Double; the density value (or Double.NaN if no value can be computed)
     */
    public abstract double computeZValue(int firstTimeBin, int endTimeBin, int firstDistanceBin, int endDistanceBin);

}

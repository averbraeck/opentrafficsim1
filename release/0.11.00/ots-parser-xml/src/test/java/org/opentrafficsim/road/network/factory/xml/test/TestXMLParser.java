package org.opentrafficsim.road.network.factory.xml.test;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

import javax.naming.NamingException;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.djunits.unit.SpeedUnit;
import org.djunits.unit.TimeUnit;
import org.djunits.value.vdouble.scalar.Dimensionless;
import org.djunits.value.vdouble.scalar.DoubleScalar;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Speed;
import org.djunits.value.vdouble.scalar.Time;
import org.opentrafficsim.core.dsol.OTSDEVSSimulatorInterface;
import org.opentrafficsim.core.dsol.OTSModelInterface;
import org.opentrafficsim.core.dsol.OTSSimTimeDouble;
import org.opentrafficsim.core.geometry.OTSGeometryException;
import org.opentrafficsim.core.gtu.GTUException;
import org.opentrafficsim.core.gtu.animation.GTUColorer;
import org.opentrafficsim.core.network.NetworkException;
import org.opentrafficsim.core.network.OTSNetwork;
import org.opentrafficsim.road.network.factory.xml.XmlNetworkLaneParser;
import org.opentrafficsim.road.network.factory.xml.test.TestXMLParser.WGS84ToRDNewTransform.Coords;
import org.opentrafficsim.road.network.sampling.Query;
import org.opentrafficsim.road.network.sampling.Sampling;
import org.opentrafficsim.road.network.sampling.indicator.MeanSpeed;
import org.opentrafficsim.road.network.sampling.indicator.MeanTravelTime;
import org.opentrafficsim.road.network.sampling.indicator.MeanTripLength;
import org.opentrafficsim.road.network.sampling.indicator.TotalDelay;
import org.opentrafficsim.road.network.sampling.indicator.TotalNumberOfStops;
import org.opentrafficsim.road.network.sampling.indicator.TotalTravelDistance;
import org.opentrafficsim.road.network.sampling.indicator.TotalTravelTime;
import org.opentrafficsim.simulationengine.AbstractWrappableAnimation;
import org.opentrafficsim.simulationengine.OTSSimulationException;
import org.opentrafficsim.simulationengine.SimpleSimulatorInterface;
import org.opentrafficsim.simulationengine.properties.AbstractProperty;
import org.opentrafficsim.simulationengine.properties.PropertyException;
import org.xml.sax.SAXException;

import nl.javel.gisbeans.io.esri.CoordinateTransform;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.animation.D2.GisRenderable2D;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.language.io.URLResource;

/**
 * <p>
 * Copyright (c) 2013-2016 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate: 2015-09-14 01:33:02 +0200 (Mon, 14 Sep 2015) $, @version $Revision: 1401 $, by $Author: averbraeck $,
 * initial version Oct 17, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class TestXMLParser extends AbstractWrappableAnimation
{
    /**
     * Main program.
     * @param args String[]; the command line arguments (not used)
     * @throws SimRuntimeException should never happen
     */
    public static void main(final String[] args) throws SimRuntimeException
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    TestXMLParser xmlModel = new TestXMLParser();
                    // 1 hour simulation run for testing
                    xmlModel.buildAnimator(new Time(0.0, TimeUnit.SECOND), new Duration(0.0, TimeUnit.SECOND),
                            new Duration(60.0, TimeUnit.MINUTE), new ArrayList<AbstractProperty<?>>(), null, true);
                }
                catch (SimRuntimeException | NamingException | OTSSimulationException | PropertyException exception)
                {
                    exception.printStackTrace();
                }
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public final String shortName()
    {
        return "TestXMLModel";
    }

    /** {@inheritDoc} */
    @Override
    public final String description()
    {
        return "TestXMLModel";
    }

    /** {@inheritDoc} */
    @Override
    public final void stopTimersThreads()
    {
        super.stopTimersThreads();
    }

    /** {@inheritDoc} */
    @Override
    protected final JPanel makeCharts(final SimpleSimulatorInterface simulator)
    {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected final OTSModelInterface makeModel(final GTUColorer colorer)
    {
        return new TestXMLModel();
    }

    /** {@inheritDoc} */
    @Override
    protected final Double makeAnimationRectangle()
    {
        // return new Rectangle2D.Double(-1000, -1000, 2000, 2000);
        return new Rectangle2D.Double(104000, 482000, 5000, 5000);
        // return new Rectangle2D.Double(0, 0, 5000, 5000);
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        return "TestXMLParser []";
    }

    /**
     * Model to test the XML parser.
     * <p>
     * Copyright (c) 2013-2016 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. <br>
     * All rights reserved. BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim
     * License</a>.
     * <p>
     * $LastChangedDate: 2015-09-14 01:33:02 +0200 (Mon, 14 Sep 2015) $, @version $Revision: 1401 $, by $Author: averbraeck $,
     * initial version un 27, 2015 <br>
     * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
     */
    class TestXMLModel implements OTSModelInterface
    {
        /** */
        private static final long serialVersionUID = 20141121L;

        /** The simulator. */
        private OTSDEVSSimulatorInterface simulator;

        /** {@inheritDoc} */
        @Override
        public final void constructModel(
                final SimulatorInterface<DoubleScalar.Abs<TimeUnit>, DoubleScalar.Rel<TimeUnit>, OTSSimTimeDouble> pSimulator)
                throws SimRuntimeException
        {
            this.simulator = (OTSDEVSSimulatorInterface) pSimulator;
            // URL url = URLResource.getResource("/PNH1.xml");
            // URL url = URLResource.getResource("/offset-example.xml");
            // URL url = URLResource.getResource("/circular-road-new-gtu-example.xml");
            // URL url = URLResource.getResource("/straight-road-new-gtu-example_2.xml");
            // URL url = URLResource.getResource("/Circuit.xml");
            URL url = URLResource.getResource("/N201v8.xml");
            XmlNetworkLaneParser nlp = new XmlNetworkLaneParser(this.simulator);
            OTSNetwork network;
            try
            {
                network = nlp.build(url);
                // ODMatrixTrips matrix = N201ODfactory.get(network);
                // N201ODfactory.makeGeneratorsFromOD(network, matrix, this.simulator);
                Query query = N201ODfactory.getQuery(network, new Sampling(this.simulator));
                scheduleKpiEvent(30.0, this.simulator, query);
            }
            catch (NetworkException | ParserConfigurationException | SAXException | IOException | NamingException | GTUException
                    | OTSGeometryException exception)
            {
                exception.printStackTrace();
            }
            
            URL gisURL = URLResource.getResource("/N201/map.xml");
            System.err.println("GIS-map file: " + gisURL.toString());
            CoordinateTransform rdto0 = new CoordinateTransformRD(0, 0);
            new GisRenderable2D(this.simulator, gisURL, rdto0);
        }

        /** {@inheritDoc} */
        @Override
        public SimulatorInterface<DoubleScalar.Abs<TimeUnit>, DoubleScalar.Rel<TimeUnit>, OTSSimTimeDouble> getSimulator()
        {
            return this.simulator;
        }

        /** {@inheritDoc} */
        @Override
        public final String toString()
        {
            return "TestXMLModel [simulator=" + this.simulator + "]";
        }

    }
    
    TotalTravelDistance totalTravelDistance  = new TotalTravelDistance();
    TotalTravelTime totalTravelTime  = new TotalTravelTime();
    MeanSpeed meanSpeed = new MeanSpeed(this.totalTravelDistance, this.totalTravelTime);
    MeanTravelTime meanTravelTime = new MeanTravelTime(this.meanSpeed);
    MeanTripLength meanTripLength = new MeanTripLength();
    TotalDelay totalDelay = new TotalDelay(new Speed(80.0, SpeedUnit.KM_PER_HOUR));
    TotalNumberOfStops totalNumberOfStops = new TotalNumberOfStops();
    
    public void publishKpis(double time, final OTSDEVSSimulatorInterface simulator, final Query query)
    {
        Length tdist = this.totalTravelDistance.getValue(query, new Duration(time, TimeUnit.SI));
        Duration ttt = this.totalTravelTime.getValue(query, new Duration(time, TimeUnit.SI));
        Speed ms = this.meanSpeed.getValue(query, new Duration(time, TimeUnit.SI));
        Duration mtt = this.meanTravelTime.getValue(query, new Duration(time, TimeUnit.SI));
        Length mtl = this.meanTripLength.getValue(query, new Duration(time, TimeUnit.SI));
        Duration tdel = this.totalDelay.getValue(query, new Duration(time, TimeUnit.SI));
        Dimensionless nos = this.totalNumberOfStops.getValue(query, new Duration(time, TimeUnit.SI));
        System.out.println("===== @time " + time + " s =====");
        System.out.println("Total distance " + tdist);
        System.out.println("Total travel time " + ttt);
        System.out.println("Mean speed " + ms);
        System.out.println("Mean travel time " + mtt);
        System.out.println("Mean trip length " + mtl);
        System.out.println("Total delay " + tdel);
        System.out.println("Number of stops " + nos);
        scheduleKpiEvent(time + 30, simulator, query);
    }
    
    public void scheduleKpiEvent(double time, final OTSDEVSSimulatorInterface simulator, final Query query) 
    {
        try
        {
            simulator.scheduleEventAbs(new Time(time, TimeUnit.SI), this, this, "publishKpis", new Object[]{time, simulator, query});
        }
        catch (SimRuntimeException exception)
        {
            throw new RuntimeException("Cannot schedule KPI event.", exception);
        }
    }
    
    /**
     * Convert coordinates to/from the Dutch RD system.
     */
    class CoordinateTransformRD implements CoordinateTransform, Serializable
    {
        /** */
        private static final long serialVersionUID = 20141017L;

        /** */
        final double dx;

        /** */
        final double dy;

        /**
         * @param dx x transform
         * @param dy y transform
         */
        public CoordinateTransformRD(final double dx, final double dy)
        {
            this.dx = dx;
            this.dy = dy;
        }

        /** {@inheritDoc} */
        @Override
        public float[] floatTransform(double x, double y)
        {
            double[] d = doubleTransform(x, y);
            return new float[] { (float) d[0], (float) d[1] };
        }

        /** {@inheritDoc} */
        @Override
        public double[] doubleTransform(double x, double y)
        {
            try
            {
                Coords c = WGS84ToRDNewTransform.ellipswgs842rd(x, y);
                return new double[] { c.x - this.dx, c.y - this.dy };
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
                return new double[] { 0, 0 };
            }
        }

        /** {@inheritDoc} */
        @Override
        public final String toString()
        {
            return "CoordinateTransformRD [dx=" + this.dx + ", dy=" + this.dy + "]";
        }
    }
    
    /**
     * <p>
     * Copyright (c) 2011 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. The
     * source code and binary code of this software is proprietary information of Delft University of Technology. Specific
     * MathTransform for WGS84 WGS84 (EPSG:4326) to RD_new (EPSG:28992) conversions. Code based on C code by Peter Knoppers as
     * applied <a href="http://www.regiolab-delft.nl/?q=node/36">here</a>, which is based on <a
     * href="http://www.dekoepel.nl/pdf/Transformatieformules.pdf">this</a> paper.
     * @author Gert-Jan Stolk
     **/
    public static class WGS84ToRDNewTransform
    {

        /** */
        private static final long serialVersionUID = 20141017L;

        //@formatter:off
        private static final double r[][] = { /* p down, q right */
            {  155000.00, 190094.945,   -0.008, -32.391, 0.0   , },
            {     -0.705, -11832.228,    0.0  ,   0.608, 0.0   , },
            {      0.0  ,   -114.221,    0.0  ,   0.148, 0.0   , },
            {      0.0  ,      2.340,    0.0  ,   0.0  , 0.0   , },
            {      0.0  ,      0.0  ,    0.0  ,   0.0  , 0.0   , }};
        private static final double s[][] = { /* p down, q right */
            { 463000.00 ,      0.433, 3638.893,   0.0  ,  0.092, },
            { 309056.544,     -0.032, -157.984,   0.0  , -0.054, },
            {     73.077,      0.0  ,   -6.439,   0.0  ,  0.0  , },
            {     59.788,      0.0  ,    0.0  ,   0.0  ,  0.0  , },
            {      0.0  ,      0.0  ,    0.0  ,   0.0  ,  0.0  , }};
        //@formatter:on

        public static void transform(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) throws Exception
        {
            int offsetDelta = dstOff - srcOff;
            for (int i = srcOff; i < srcOff + numPts && i + 1 < srcPts.length && i + offsetDelta + 1 < dstPts.length; i += 2)
            {
                Coords transformedCoords = ellipswgs842rd(srcPts[i], srcPts[i + 1]);
                dstPts[i + offsetDelta] = transformedCoords.x;
                dstPts[i + offsetDelta + 1] = transformedCoords.y;
            }
        }

        private static Coords ellipswgs842rd(double EW, double NS)
        {
            Coords result = new Coords(0, 0);
            int p;
            double pown = 1;
            double dn = 0.36 * (NS - 52.15517440);
            double de = 0.36 * (EW - 5.38720621);
            if (NS <= 50 || NS >= 54 || EW <= 3 || (EW >= 8))
            {
                System.err.println("Error: ellipswgs842rd input out of range (" + EW + ", " + NS + ")");
            }

            for (p = 0; p < 5; p++)
            {
                double powe = 1;
                int q;

                for (q = 0; q < 5; q++)
                {
                    result.x += r[p][q] * powe * pown;
                    result.y += s[p][q] * powe * pown;
                    powe *= de;
                }
                pown *= dn;
            }
            return result;
        }

        /**
         * Coordinate pair.
         */
        public static class Coords implements Serializable
        {
            /** */
            private static final long serialVersionUID = 20141017L;

            public double x, y;

            public Coords(double x, double y)
            {
                this.x = x;
                this.y = y;
            }

            /** {@inheritDoc} */
            @Override
            public final String toString()
            {
                return "Coords [x=" + this.x + ", y=" + this.y + "]";
            }

        }

        /** {@inheritDoc} */
        @Override
        public final String toString()
        {
            return "WGS84ToRDNewTransform []";
        }

    }

}
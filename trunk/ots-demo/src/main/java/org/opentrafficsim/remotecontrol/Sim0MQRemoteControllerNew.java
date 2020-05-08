package org.opentrafficsim.remotecontrol;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.djunits.unit.DurationUnit;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djutils.cli.Checkable;
import org.djutils.cli.CliUtil;
import org.djutils.decoderdumper.HexDumper;
import org.djutils.io.URLResource;
import org.djutils.logger.CategoryLogger;
import org.djutils.logger.LogCategory;
import org.djutils.serialization.SerializationException;
import org.pmw.tinylog.Level;
import org.sim0mq.Sim0MQException;
import org.sim0mq.message.Sim0MQMessage;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Remotely control OTS using Sim0MQ messages.
 * <p>
 * Copyright (c) 2013-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Mar 4, 2020 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class Sim0MQRemoteControllerNew extends JFrame implements WindowListener, ActionListener
{
    /** ... */
    private static final long serialVersionUID = 20200304L;

    /**
     * The command line options.
     */
    @Command(description = "Test program for Remote Control OTS", name = "Remote Control OTS", mixinStandardHelpOptions = true,
            version = "1.0")
    public static class Options implements Checkable
    {
        /** The IP port. */
        @Option(names = {"-p", "--port"}, description = "Internet port to use", defaultValue = "8888")
        private int port;

        /**
         * Retrieve the port.
         * @return int; the port
         */
        public final int getPort()
        {
            return this.port;
        }

        /** The host name. */
        @Option(names = {"-H", "--host"}, description = "Internet host to use", defaultValue = "localhost")
        private String host;

        /**
         * Retrieve the host name.
         * @return String; the host name
         */
        public final String getHost()
        {
            return this.host;
        }

        @Override
        public final void check() throws Exception
        {
            if (this.port <= 0 || this.port > 65535)
            {
                throw new Exception("Port should be between 1 and 65535");
            }
        }
    }

    /** The instance of the RemoteControl. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    static Sim0MQRemoteControllerNew gui = null;

    /** Socket for sending messages that should be relayed to OTS. */
    private ZMQ.Socket toOTS;

    /**
     * Start the OTS remote control program.
     * @param args String[]; the command line arguments
     */
    public static void main(final String[] args)
    {
        CategoryLogger.setAllLogLevel(Level.WARNING);
        CategoryLogger.setLogCategories(LogCategory.ALL);
        // Instantiate the RemoteControl GUI
        try
        {
            EventQueue.invokeAndWait(new Runnable()
            {
                /** {@inheritDoc} */
                @Override
                public void run()
                {
                    try
                    {
                        gui = new Sim0MQRemoteControllerNew();
                        gui.setVisible(true);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        System.exit(ERROR);
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(ERROR);
        }
        // We don't get here until the GUI is fully running.
        Options options = new Options();
        CliUtil.execute(options, args); // register Unit converters, parse the command line, etc..
        gui.processArguments(options.getHost(), options.getPort());
    }

    /** ... */
    private ZContext zContext = new ZContext(1);

    /** Message relayer. */
    private Thread pollerThread;

    /**
     * Poller thread for relaying messages between the remote OTS and local AWT.
     */
    class PollerThread extends Thread
    {
        /** The ZContext. */
        private final ZContext context;

        /** The host that runs the OTS simulation. */
        private final String slaveHost;

        /** The port on which to connect to the OTS simulation. */
        private final int slavePort;

        /**
         * Construct a new PollerThread for relaying messages.
         * @param context ZContext; the ZMQ context
         * @param slaveHost String; host name of the OTS server machine
         * @param slavePort int; port number on which to connect to the OTS server machine
         */
        PollerThread(final ZContext context, final String slaveHost, final int slavePort)
        {
            this.context = context;
            this.slaveHost = slaveHost;
            this.slavePort = slavePort;
        }

        @Override
        public final void run()
        {
            int nextExpectedPacket = 0;
            ZMQ.Socket slaveSocket = this.context.createSocket(SocketType.PAIR); // changed to PAIR
            slaveSocket.setHWM(100000);
            ZMQ.Socket awtSocketIn = this.context.createSocket(SocketType.PULL);
            awtSocketIn.setHWM(100000);
            ZMQ.Socket awtSocketOut = this.context.createSocket(SocketType.PUSH);
            awtSocketOut.setHWM(100000);
            slaveSocket.connect("tcp://" + this.slaveHost + ":" + this.slavePort);
            awtSocketIn.bind("inproc://fromAWT");
            awtSocketOut.bind("inproc://toAWT");
            // XXX took the POLLER out
            // ZMQ.Poller items = this.context.createPoller(2);
            // items.register(slaveSocket, ZMQ.Poller.POLLIN);
            // items.register(awtSocketIn, ZMQ.Poller.POLLIN);
            while (!Thread.currentThread().isInterrupted())
            {
                byte[] message = slaveSocket.recv(ZMQ.DONTWAIT); // non-blocking
                // items.poll();
                // if (items.pollin(0))
                if (message != null)
                {
                    String expectedSenderField = String.format("slave_%05d", ++nextExpectedPacket);
                    try
                    {
                        Object[] messageFields = Sim0MQMessage.decode(message).createObjectArray();
                        String senderTag = (String) messageFields[3];
                        if (!senderTag.equals(expectedSenderField))
                        {
                            System.err.println("Got message " + senderTag + " , expected " + expectedSenderField
                                    + ", message is " + messageFields[5]);
                        }
                    }
                    catch (Sim0MQException | SerializationException e)
                    {
                        e.printStackTrace();
                    }

                    // System.out.println("poller has received a message on the fromOTS DEALER socket; transmitting to AWT");
                    awtSocketOut.send(message);
                }
                else // look at awt
                {
                    message = awtSocketIn.recv(ZMQ.DONTWAIT); // non-blocking
                    // System.out.println("poller has received a message on the fromAWT PULL socket; transmitting to OTS");
                    if (message != null)
                    {
                        slaveSocket.send(message);
                    }
                    else
                    {
                        try
                        {
                            Thread.sleep(1); // 1000 Hz
                        }
                        catch (Exception exception)
                        {
                            // ignore
                        }
                    }
                }
            }

        }

    }

    /**
     * Open connections as specified on the command line, then start the message transfer threads.
     * @param host String; host to connect to (listening OTS server should already be running)
     * @param port int; port to connect to (listening OTS server should be listening on that port)
     */
    public void processArguments(final String host, final int port)
    {
        this.output.println("host is " + host + ", port is " + port);

        this.pollerThread = new PollerThread(this.zContext, host, port);

        this.pollerThread.start();

        this.toOTS = this.zContext.createSocket(SocketType.PUSH);
        this.toOTS.setHWM(100000);

        new OTS2AWT(this.zContext).start();

        this.toOTS.connect("inproc://fromAWT");
    }

    /**
     * Write something to the remote OTS.
     * @param command String; the command to write
     * @throws IOException when communication fails
     */
    public void write(final String command) throws IOException
    {
        this.toOTS.send(command);
        // output.println("Wrote " + command.getBytes().length + " bytes");
        this.output.println("Sent string \"" + command + "\"");
    }

    /**
     * Write something to the remote OTS.
     * @param bytes byte[]; the bytes to write
     * @throws IOException when communication fails
     */
    public void write(final byte[] bytes) throws IOException
    {
        this.toOTS.send(bytes);
        // output.println("Wrote " + command.getBytes().length + " bytes");
        // output.println(HexDumper.hexDumper(bytes));
    }

    /** Step to button. */
    private JButton stepTo;

    /**
     * Construct the GUI.
     */
    Sim0MQRemoteControllerNew()
    {
        // Construct the GUI
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 800);
        JPanel panelAll = new JPanel();
        panelAll.setBorder(new EmptyBorder(5, 5, 5, 5));
        panelAll.setLayout(new BorderLayout(0, 0));
        setContentPane(panelAll);
        JPanel panelControls = new JPanel();
        panelAll.add(panelControls, BorderLayout.PAGE_START);
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        panelAll.add(scrollPane, BorderLayout.PAGE_END);
        this.output = new PrintStream(new TextAreaOutputStream(textArea), true);
        JPanel controls = new JPanel();
        controls.setLayout(new FlowLayout());
        JButton sendNetwork = new JButton("Send network");
        sendNetwork.setActionCommand("SendNetwork");
        sendNetwork.addActionListener(this);
        controls.add(sendNetwork);
        this.stepTo = new JButton("Step to 10 s");
        this.stepTo.setActionCommand("StepTo");
        this.stepTo.addActionListener(this);
        controls.add(this.stepTo);
        JButton step100TimesTo = new JButton("Step 100 times 10 s");
        step100TimesTo.setActionCommand("Step100To");
        step100TimesTo.addActionListener(this);
        controls.add(step100TimesTo);
        JButton getGTUPositions = new JButton("Get all GTU positions");
        getGTUPositions.setActionCommand("GetAllGTUPositions");
        getGTUPositions.addActionListener(this);
        controls.add(getGTUPositions);
        panelAll.add(controls, BorderLayout.CENTER);
    }

    /** Debugging and other output goes here. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    PrintStream output = null;

    /**
     * Shut down this application.
     */
    public void shutDown()
    {
        // Do we have to kill anything for a clean exit?
    }

    /** {@inheritDoc} */
    @Override
    public void windowOpened(final WindowEvent e)
    {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public final void windowClosing(final WindowEvent e)
    {
        shutDown();
    }

    /** {@inheritDoc} */
    @Override
    public void windowClosed(final WindowEvent e)
    {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void windowIconified(final WindowEvent e)
    {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void windowDeiconified(final WindowEvent e)
    {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void windowActivated(final WindowEvent e)
    {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void windowDeactivated(final WindowEvent e)
    {
        // Do nothing
    }

    /**
     * Thread that reads results from OTS and (for now) writes those to the textArea.
     */
    class OTS2AWT extends Thread
    {
        /** Socket where the message from OTS will appear. */
        private final ZMQ.Socket fromOTS;

        /**
         * Construct a new OTS2AWT thread.
         * @param zContext ZContext; the ZContext that is needed to construct the PULL socket to read the messages
         */
        OTS2AWT(final ZContext zContext)
        {
            this.fromOTS = zContext.createSocket(SocketType.PULL);
            this.fromOTS.setHWM(100000);
            this.fromOTS.connect("inproc://toAWT");
        }

        /** {@inheritDoc} */
        @Override
        public void run()
        {
            do
            {
                try
                {
                    // Read from remotely controlled OTS
                    byte[] bytes = this.fromOTS.recv(0); /// XXX: this one is okay to block
                    // System.out.println("remote controller has received a message on the fromOTS PULL socket");
                    Object[] message = Sim0MQMessage.decode(bytes).createObjectArray();
                    if (message.length > 8 && message[5] instanceof String)
                    {
                        String command = (String) message[5];
                        switch (command)
                        {
                            case "GTUPOSITION":
                                Sim0MQRemoteControllerNew.this.output
                                        .println(String.format("%10.10s: %s x=%8.3f y=%8.3f z=%8.3f heading=%6.1f, v=%s, a=%s",
                                                message[8], message[9], message[10], message[11], message[12],
                                                Math.toDegrees((Double) message[13]), message[14], message[15]));
                                break;

                            case "TIME_CHANGED_EVENT":
                                Sim0MQRemoteControllerNew.this.output.println(message[8]);
                                break;

                            case "TRAFFICCONTROL.CONTROLLER_WARNING":
                                Sim0MQRemoteControllerNew.this.output
                                        .println(String.format("%s: warning %s", message[8], message[9]));
                                break;

                            case "TRAFFICCONTROL.CONTROLLER_EVALUATING":
                                Sim0MQRemoteControllerNew.this.output
                                        .println(String.format("%s: evaluating %s", message[8], message[9]));
                                break;

                            case "TRAFFICCONTROL.CONFLICT_GROUP_CHANGED":
                                Sim0MQRemoteControllerNew.this.output.println(String.format(
                                        "%s: conflict group changed from %s to %s", message[8], message[9], message[10]));
                                break;

                            case "NETWORK.GTU.ADD":
                                Sim0MQRemoteControllerNew.this.output.println(String.format("GTU added %s", message[8]));
                                break;

                            case "NETWORK.GTU.REMOVE":
                                Sim0MQRemoteControllerNew.this.output.println(String.format("GTU removed %s", message[8]));
                                break;

                            case "READY":
                                Sim0MQRemoteControllerNew.this.output.println("Slave is ready for the next command");
                                break;

                            default:
                                Sim0MQRemoteControllerNew.this.output.println("Unhandled reply: " + command);
                                Sim0MQRemoteControllerNew.this.output.println(HexDumper.hexDumper(bytes));
                                Sim0MQRemoteControllerNew.this.output.println("Received:");
                                Sim0MQRemoteControllerNew.this.output.println(Sim0MQMessage.print(message));
                                break;

                        }
                    }
                    else
                    {
                        Sim0MQRemoteControllerNew.this.output.println(HexDumper.hexDumper(bytes));
                    }
                }
                catch (ZMQException | Sim0MQException | SerializationException e)
                {
                    e.printStackTrace();
                    return;
                }
            }
            while (true);

        }
    }

    /**
     * Open an URL, read it and store the contents in a string. Adapted from
     * https://stackoverflow.com/questions/4328711/read-url-to-string-in-few-lines-of-java-code
     * @param url URL; the URL
     * @return String
     * @throws IOException when reading the file fails
     */
    public static String readStringFromURL(final URL url) throws IOException
    {
        try (Scanner scanner = new Scanner(url.openStream(), StandardCharsets.UTF_8.toString()))
        {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "SendNetwork":
            {
                String networkFile = "/TrafCODDemo2/TrafCODDemo2.xml";
                Duration warmupDuration = Duration.ZERO;
                Duration runDuration = new Duration(3600, DurationUnit.SECOND);
                Long seed = 123456L;
                URL url = URLResource.getResource(networkFile);
                // System.out.println("url is " + url);
                try
                {
                    String xml = readStringFromURL(url);
                    System.out.println("xml length = " + xml.length());
                    try
                    {
                        write(Sim0MQMessage.encodeUTF8(true, 0, "RemoteControl", "OTS", "LOADNETWORK", 0, xml, warmupDuration,
                                runDuration, seed));
                    }
                    catch (IOException e1)
                    {
                        this.output.println("Write failed; Caught IOException");
                        ((JComponent) e.getSource()).setEnabled(false);
                    }
                    catch (Sim0MQException e1)
                    {
                        e1.printStackTrace();
                    }
                    catch (SerializationException e1)
                    {
                        e1.printStackTrace();
                    }
                }
                catch (IOException e2)
                {
                    System.err.println("Could not load file " + networkFile);
                    e2.printStackTrace();
                }
                break;
            }

            case "StepTo":
            {
                JButton button = (JButton) e.getSource();
                String caption = button.getText();
                int position;
                for (position = 0; position < caption.length(); position++)
                {
                    if (Character.isDigit(caption.charAt(position)))
                    {
                        break;
                    }
                }
                Time toTime = Time.valueOf(caption.substring(position));
                try
                {
                    write(Sim0MQMessage.encodeUTF8(true, 0, "RemoteControl", "OTS", "SIMULATEUNTIL", 0, toTime));
                    toTime = toTime.plus(new Duration(10, DurationUnit.SECOND));
                    button.setText(caption.substring(0, position)
                            + String.format("%.0f %s", toTime.getInUnit(), toTime.getDisplayUnit()));
                }
                catch (IOException | Sim0MQException | SerializationException e1)
                {
                    e1.printStackTrace();
                }
                break;
            }

            case "Step100To":
            {
                for (int i = 0; i < 100; i++)
                {
                    actionPerformed(new ActionEvent(this.stepTo, 0, "StepTo"));
                }
                break;
            }

            case "GetAllGTUPositions":
            {
                try
                {
                    write(Sim0MQMessage.encodeUTF8(true, 0, "RemoteControl", "OTS", "SENDALLGTUPOSITIONS", 0));
                }
                catch (IOException | Sim0MQException | SerializationException e1)
                {
                    e1.printStackTrace();
                }
                break;
            }

            default:
                this.output.println("Oops: unhandled action command");
        }

    }
}

package org.opentrafficsim.simulationengine;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.MaskFormatter;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.gui.swing.DSOLPanel;
import nl.tudelft.simulation.dsol.gui.swing.SimulatorControlPanel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

import org.opentrafficsim.core.dsol.OTSSimTimeDouble;
import org.opentrafficsim.core.unit.TimeUnit;
import org.opentrafficsim.core.value.vdouble.scalar.DoubleScalar;

/**
 * Peter's improved simulation control panel.
 * <p>
 * Copyright (c) 2013-2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights
 * reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version 11 dec. 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class ControlPanel implements ActionListener, PropertyChangeListener
{
    /** The simulator. */
    final DEVSSimulator<DoubleScalar.Abs<TimeUnit>, DoubleScalar.Rel<TimeUnit>, OTSSimTimeDouble> simulator;

    /** The SimulatorInterface that is controlled by the buttons. */
    final SimulatorInterface<?, ?, ?> target;

    /** Logger. */
    final Logger logger;

    /** The clock. */
    ClockPanel clockPanel;

    /** The control buttons. */
    ArrayList<JButton> buttons = new ArrayList<JButton>();
    
    /** Font used to display the clock and the stop time. */
    Font timeFont = new Font("SansSerif", Font.BOLD, 18);
    
    /** The TimeEdit that lets the user set a time when the simulation will be stopped */
    final TimeEdit timeEdit;

    /** The currently registered stop at event */
    SimEvent<OTSSimTimeDouble> stopAtEvent = null;
    /**
     * Decorate a SimpleSimulator with a different set of control buttons.
     * @param simulator SimpleSimulator; the simulator.
     */
    public ControlPanel(SimpleSimulator simulator)
    {
        this.simulator = simulator.getSimulator();
        this.target = simulator.getSimulator();
        this.logger = Logger.getLogger("nl.tudelft.opentrafficsim");

        DSOLPanel<DoubleScalar.Abs<TimeUnit>, DoubleScalar.Rel<TimeUnit>, OTSSimTimeDouble> panel =
                simulator.getPanel();
        SimulatorControlPanel controlPanel =
                (SimulatorControlPanel) ((BorderLayout) panel.getLayout()).getLayoutComponent(BorderLayout.NORTH);
        JPanel buttonPanel = (JPanel) controlPanel.getComponent(0);
        buttonPanel.removeAll();
        buttonPanel.add(makeButton("stepButton", "/Last_recor.png", "Step", true));
        buttonPanel.add(makeButton("nextTimeButton", "/NextTrack.png", "NextTime", true));
        buttonPanel.add(makeButton("runButton", "/Play.png", "Run", true));
        buttonPanel.add(makeButton("pauseButton", "/Pause.png", "Pause", false));
        buttonPanel.add(makeButton("resetButton", "/Undo.png", "Reset", false));
        this.clockPanel = new ClockPanel();
        buttonPanel.add(this.clockPanel);
        this.timeEdit = new TimeEdit(new DoubleScalar.Abs<TimeUnit>(0, TimeUnit.SECOND));
        this.timeEdit.addPropertyChangeListener("value", this);
        buttonPanel.add(this.timeEdit);
    }

    /**
     * Create a button.
     * @param name String; name of the button
     * @param iconPath String; path to the resource
     * @param actionCommand String; the action command
     * @param enabled boolean; true if the new button must initially be enable; false if it must initially be disabled
     * @return JButton
     */
    private JButton makeButton(String name, String iconPath, String actionCommand, boolean enabled)
    {
        JButton result = new JButton(new ImageIcon(this.getClass().getResource(iconPath)));
        result.setName(name);
        result.setEnabled(enabled);
        result.setActionCommand(actionCommand);
        result.addActionListener(this);
        this.buttons.add(result);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String actionCommand = e.getActionCommand();
        System.out.println("actionCommand: " + actionCommand);
        try
        {
            if (actionCommand.equals("Step"))
            {
                if (this.simulator.isRunning())
                {
                    this.simulator.stop();
                }
                this.target.step();
            }
            if (actionCommand.equals("Run"))
            {
                this.target.start();
            }
            if (actionCommand.equals("NextTime"))
            {
                if (this.simulator.isRunning())
                {
                    this.simulator.stop();
                }
                double now = this.simulator.getSimulatorTime().get().getSI();
                System.out.println("now is " + now);
                this.simulator.scheduleEventAbs(new DoubleScalar.Abs<TimeUnit>(now + Math.ulp(now), TimeUnit.SECOND),
                        this, this, "autoPauseSimulator", null);
                this.target.start();
            }
            if (actionCommand.equals("Pause"))
            {
                this.target.stop();
            }
            if (actionCommand.equals("Reset"))
            {
                if (this.simulator.isRunning())
                {
                    this.simulator.stop();
                }
                // Should this create a new replication?
            }
            fixButtons();
        }
        catch (Exception exception)
        {
            this.logger.logp(Level.SEVERE, "ControlPanel", "actionPerformed", "", exception);
        }
    }

    /**
     * Update the enabled state of all the buttons.
     */
    void fixButtons()
    {
        final boolean moreWorkToDo = this.simulator.getEventList().size() > 0;
        for (JButton button : this.buttons)
        {
            final String actionCommand = button.getActionCommand();
            if (actionCommand.equals("Step"))
            {
                button.setEnabled(moreWorkToDo);
            }
            else if (actionCommand.equals("Run"))
            {
                button.setEnabled(moreWorkToDo && !this.simulator.isRunning());
            }
            else if (actionCommand.equals("NextTime"))
            {
                button.setEnabled(moreWorkToDo);
            }
            else if (actionCommand.equals("Pause"))
            {
                button.setEnabled(this.simulator.isRunning());
            }
            else if (actionCommand.equals("Reset"))
            {
                button.setEnabled(true);// FIXME: should be disabled when the simulator was just reset or initialized
            }
            else
            {
                this.logger.logp(Level.SEVERE, "ControlPanel", "fixButtons", "", new Exception("Unknown button?"));
            }
        }
    }

    /**
     * Pause the simulator.
     */
    public void autoPauseSimulator()
    {
        if (this.simulator.isRunning())
        {
            this.simulator.stop();
            double currentTick = this.simulator.getSimulatorTime().get().getSI();
            double nextTick = this.simulator.getEventList().first().getAbsoluteExecutionTime().get().getSI();
            System.out.println("currentTick is " + currentTick);
            System.out.println("nextTick is " + nextTick);
            if (nextTick - Math.ulp(nextTick) > currentTick)
            {
                // The clock is now just beyond where it was when the user requested the NextTime operation
                // Insert another autoPauseSimulator event just before what is now the time of the next event
                // and let the simulator time increment to that time
                try
                {
                    System.out.println("Re-Scheduling at " + (nextTick - Math.ulp(nextTick)));
                    this.simulator.scheduleEventAbs(new DoubleScalar.Abs<TimeUnit>(nextTick - Math.ulp(nextTick),
                            TimeUnit.SECOND), this, this, "autoPauseSimulator", null);
                    this.simulator.start();
                }
                catch (RemoteException | SimRuntimeException exception)
                {
                    this.logger.logp(Level.SEVERE, "ControlPanel", "autoPauseSimulator",
                            "Caught an exception while trying to step up to the next real event");
                }
            }
            else
            {
                System.out.println("Not re-scheduling");
                try
                {
                    SwingUtilities.invokeAndWait(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            fixButtons();
                        }
                    });
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        System.out.println("PropertyChanged: " + evt);
        if (null != this.stopAtEvent)
        {
            this.simulator.cancelEvent(this.stopAtEvent);   // silently ignore false result
            this.stopAtEvent = null;
        }
        String newValue = (String) evt.getNewValue();
        String[] fields = newValue.split("[:\\.]");
        int hours = Integer.parseInt(fields[0]);
        int minutes = Integer.parseInt(fields[1]);
        int seconds = Integer.parseInt(fields[2]);
        int fraction = Integer.parseInt(fields[3]);
        double stopTime = hours * 3600 + minutes * 60 + seconds + fraction / 1000d;
        stopTime -= Math.ulp(stopTime);
        if (stopTime < this.simulator.getSimulatorTime().get().getSI())
        {
            return;
        }
        else
        {
            this.stopAtEvent = new SimEvent<OTSSimTimeDouble>(new OTSSimTimeDouble(new DoubleScalar.Abs<TimeUnit>(stopTime, TimeUnit.SECOND)) , SimEventInterface.NORMAL_PRIORITY, this, this, "autoPauseSimulator", null);
            try
            {
                this.simulator.scheduleEvent(this.stopAtEvent);
            }
            catch (SimRuntimeException exception)
            {
                exception.printStackTrace();
            }
        }
        
    }
    
    /** JLabel that displays the simulation time. */
    class ClockPanel extends JLabel
    {
        /** */
        private static final long serialVersionUID = 20141211L;

        /** The JLabel that displays the time. */
        final JLabel clockLabel;

        /** timer update in msec. */
        protected final long PERIOD = 1000;

        /** Construct a clock panel. */
        ClockPanel()
        {
            super("00:00:00.000");
            this.clockLabel = this;
            this.setFont(ControlPanel.this.timeFont);
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimeUpdateTask(), 0, this.PERIOD);

        }

        /** Updater for the clock panel. */
        private class TimeUpdateTask extends TimerTask
        {
            /**
             * Create a TimeUpdateTask.
             */
            public TimeUpdateTask()
            {
            }

            /** {@inheritDoc} */
            @Override
            public void run()
            {
                double now = Math.round(ControlPanel.this.simulator.getSimulatorTime().get().getSI() * 1000) / 1000d;
                int seconds = (int) Math.floor(now);
                int fractionalSeconds = (int) Math.floor(1000 * (now - seconds));
                ClockPanel.this.clockLabel.setText(String.format("  %02d:%02d:%02d.%03d  ", seconds / 3600,
                        seconds / 60 % 60, seconds % 60, fractionalSeconds));
                ClockPanel.this.clockLabel.repaint();
            }
        }

    }

    /** Entry field for time */
    class TimeEdit extends JFormattedTextField
    {
        /** */
        private static final long serialVersionUID = 20141212L;

        /** Formatter for the text field. */
        private final MaskFormatter timeMask;

        /**
         * Construct a new TimeEdit.
         * @param initialValue DoubleScalar.Abs&lt;TimeUnit&gt;; the initial value for the TimeEdit
         */
        TimeEdit(DoubleScalar.Abs<TimeUnit> initialValue)
        {
            super(new RegexFormatter("\\d\\d\\d\\d:[0-5]\\d:[0-5]\\d\\.\\d\\d\\d"));
            MaskFormatter mf = null;
            try
            {
                mf = new MaskFormatter("####:##:##.###");
            }
            catch (ParseException exception)
            {
                exception.printStackTrace();
            }
            this.timeMask = mf;
            this.timeMask.setPlaceholderCharacter('0');
            this.timeMask.setAllowsInvalid(false);
            this.timeMask.setCommitsOnValidEdit(true);
            this.timeMask.setOverwriteMode(true);
            this.timeMask.install(this);
            setTime(initialValue);
            setFont(ControlPanel.this.timeFont);
        }

        /**
         * Set or update the time shown in this TimeEdit.
         * @param newValue DoubleScalar.Abs&lt;TimeUnit&gt;; the (new) value to set/show in this TimeEdit
         */
        public void setTime(DoubleScalar.Abs<TimeUnit> newValue)
        {
            double v = newValue.getSI();
            int integerPart = (int) Math.floor(v);
            int fraction = (int) Math.floor((v - integerPart) * 1000);
            String text =
                    String.format("%04d:%02d:%02d.%03d", integerPart / 3600, integerPart / 60 % 60, integerPart % 60,
                            fraction);
            this.setText(text);
        }
    }

    /**
     * Extension of a DefaultFormatter that uses a regular expression. <br />
     * Derived from <a
     * href="http://www.java2s.com/Tutorial/Java/0240__Swing/RegexFormatterwithaJFormattedTextField.htm">
     * http://www.java2s.com/Tutorial/Java/0240__Swing/RegexFormatterwithaJFormattedTextField.htm</a> <p>
     * Copyright (c) 2013-2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights
     * reserved. <br>
     * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
     * <p>
     * @version 12 dec. 2014 <br>
     * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
     */
    class RegexFormatter extends DefaultFormatter
    {
        /** */
        private static final long serialVersionUID = 20141212L;
        
        /** The regular expression pattern */
        private Pattern pattern;
        
        /**
         * Create a new RegexFormatter.
         * @param pattern String; regular expression pattern that defines what
         * this RexexFormatter will accept
         * @throws PatternSyntaxException
         */
        public RegexFormatter(String pattern) throws PatternSyntaxException {
            this.pattern = Pattern.compile(pattern);
        }
        
        @Override
        public Object stringToValue(String text) throws ParseException {
            Matcher matcher = this.pattern.matcher(text);
            if (matcher.matches())
            {
                //System.out.println("String \"" + text + "\" matches");
                return super.stringToValue(text);
            }
            //System.out.println("String \"" + text + "\" does not match");
            throw new ParseException("Pattern did not match", 0);
        }
    }

}

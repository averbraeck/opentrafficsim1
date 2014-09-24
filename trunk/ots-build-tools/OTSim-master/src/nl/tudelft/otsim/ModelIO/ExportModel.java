package nl.tudelft.otsim.ModelIO;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import nl.tudelft.otsim.FileIO.DataWriter;
import nl.tudelft.otsim.FileIO.StaXWriter;
import nl.tudelft.otsim.GUI.FileDialog;
import nl.tudelft.otsim.GUI.Main;
import nl.tudelft.otsim.GUI.Model;
import nl.tudelft.otsim.TrafficDemand.TrafficDemand;

import com.vividsolutions.jts.geom.Coordinate;

public class ExportModel implements ActionListener {

    private static JPanel cards = new JPanel(new CardLayout());
	private int cardCounter ;
	private JButton cancelButton;
	private JButton finishButton;
    private JButton nextButton;
    private JButton prevButton;

	private JCheckBox optionModel;
	private JCheckBox optionDemand;
	JFrame frame;
    private static FileChooser fChooser;
    private Model exportModel;
    private static String fileExportModel;
    private static String fileExportDemand;
	private static int index; 
	private static int fileCount;

	public ExportModel() {
		Class<?> klass;
		klass = Coordinate.class;
		URL location = klass.getResource('/' + klass.getName().replace(".", "/") + ".class");
		System.out.print(location);
        frame = new JFrame() ;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(700, 400));
        cards.setBorder(BorderFactory.createLineBorder(Color.black));
        cardCounter = 0;  // start at the initial/first card       
    	JPanel choices = new JPanel() ;
        choices.setLayout(new GridBagLayout());	        
        GridBagConstraints gbConstraints = new GridBagConstraints();

        gbConstraints.anchor = GridBagConstraints.NORTHWEST;
        gbConstraints.insets = new Insets(15,30,0,0);
        gbConstraints.gridx = 0;
        gbConstraints.gridy = 0;
        optionModel= new JCheckBox("Export Model (network and geo-objects");
        optionModel.addActionListener(this);
        optionModel.setEnabled(true);
        choices.add(optionModel, gbConstraints);	
        gbConstraints.weightx = 0.4;
        gbConstraints.weighty = 0.4;
        gbConstraints.gridx = 0;
        gbConstraints.gridy = 1;
        optionDemand = new JCheckBox("Export Traffic Demand (trip patterns)");
        optionDemand.addActionListener(this);
        optionDemand.setEnabled(true);
        choices.add(optionDemand, gbConstraints);
        cards.add(choices, "Options");

        fChooser = null;
    	int files = 2;
    	String[] fileNames = new String[files];
    	fileNames[0] = "Model File";
    	fileNames[1] = "Traffic Demand File";
    	String[] labels = new String[files];
    	labels[0] = "Network File";
    	labels[1] = "Traffic Demand File";
    	fileCount = fileNames.length;
    	String[] commandNames = new String[files];
    	commandNames[0] = "Model";
    	commandNames[1] = "Demand";
    	fChooser = new FileChooser(files, labels, fileNames, commandNames);
    	fChooser.setVisible(true); 
    	fChooser.setModuleName("ExportModel");
    	// The module name is used in the class FileChooser to select the action
    	// of the file chooser!!!!
    	cards.add(fChooser, fChooser.toString());

    	JPanel control = new JPanel() ;
        prevButton = new JButton("\u22b2Prev");
        prevButton.addActionListener(this);
        prevButton.setEnabled(false);
        control.add(prevButton);	        
        nextButton = new JButton("Next\u22b3");
        nextButton.addActionListener(this);
        nextButton.setEnabled(false);
        control.add(nextButton);
        finishButton = new JButton("Finish");
    	finishButton.addActionListener(this);
    	finishButton.setEnabled(false);  	
    	control.add(finishButton);
    	cancelButton = new JButton("Cancel");
    	cancelButton.addActionListener(this);
    	cancelButton.setEnabled(true);  	
    	control.add(cancelButton);

    	frame.add(cards, BorderLayout.CENTER);
        frame.add(control, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		System.out.println("actionperformed: command is " + command);
		if (command.startsWith("Export Model") )   {
			//this.getNextButton().setEnabled(true);
	        Main.mainFrame.getExportModel().getNextButton().setEnabled(true);
	        if (optionModel.isSelected())  {
	        	fChooser.getFileButton()[0].setEnabled(true);
	        }
        	else  {
	        	fChooser.getFileButton()[0].setEnabled(false);        		
        	}        		
	    }

		if (command.startsWith("Export Traffic Demand") )   {
	        Main.mainFrame.getLoadModel().getNextButton().setEnabled(true);
	        if (optionModel.isSelected())  {
	        	fChooser.getFileButton()[1].setEnabled(true);	        		
	        }
        	else  {
	        	fChooser.getFileButton()[1].setEnabled(false);
        	} 
		}
		if (command.startsWith("Finish")) {
			System.out.println("Finish" + command);
				if (fChooser.getFileButton()[0].isEnabled() == true) {
					String fileName = fChooser.getTextField()[0].getText();
					try {
						DataWriter.WriteData(fileName, Main.mainFrame.model);
/*							if (! Main.mainFrame.model.network.writeXML(staXWriter))
								throw new Error("Could not write network");
							if (! staXWriter.close())
								throw new Error("Could not close XML file");*/
						Main.mainFrame.model.network.clearModified();
						Main.mainFrame.model.network.setStorageName(fileName);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				if (fChooser.getFileButton()[1].isEnabled() == true) {
					String fileName = fChooser.getTextField()[1].getText();
					try {
						StaXWriter staXWriter = new StaXWriter(fileName, true);
						if (! staXWriter.writeNodeStart(TrafficDemand.XMLTAG))
							throw new Error("Could not write " + TrafficDemand.XMLTAG + " start node");
						if (! Main.mainFrame.model.trafficDemand.writeXML(staXWriter))
							throw new Error("Could not write trafficDemand");
						if (! staXWriter.writeNodeEnd(TrafficDemand.XMLTAG))
							throw new Error("Could not write " + TrafficDemand.XMLTAG + " end node");
						if (! staXWriter.close())
							throw new Error("Could not close XML file");
						Main.mainFrame.model.network.clearModified();
						Main.mainFrame.model.network.setStorageName(fileName);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				//importedModel.trafficDemand.createTripPatternList(tripPatternList);

	        Main.mainFrame.setTitle("Exported");
	        Main.mainFrame.setActiveGraph();
	        Main.mainFrame.menuItemExportModel.setEnabled(true); 
	        this.frame.dispose();
			}
		if (command.startsWith("Cancel")) {
			System.out.println("Cancel" + command);
			exportModel = null;
			this.frame.dispose();
			//TODO add close statement
		}
		if (command.startsWith("\u22b2Prev")) {
            CardLayout cl = (CardLayout) cards.getLayout();
            if (cardCounter == cards.getComponentCount()-1)  {
            	this.finishButton.setEnabled(false);
            }
            if (cardCounter > 0)  {
            	cl.previous(cards);
            	this.nextButton.setEnabled(true);
            	cardCounter--;
            	System.out.print( "cardnumber" + cardCounter +"prev" );
            }
            if (cardCounter == 0)
            	this.prevButton.setEnabled(false);
		}
		if (command.startsWith("Next\u22b3")) {
            CardLayout cl = (CardLayout) cards.getLayout();
            if (cardCounter != cards.getComponentCount()-1)  {
            	cl.next(cards);
            	this.prevButton.setEnabled(true);
            	cardCounter++;
            	System.out.print( "cardnumber" + cardCounter +"next" );
            }
            if (cardCounter == cards.getComponentCount()-1)  { 
            	this.finishButton.setEnabled(true);
            	this.nextButton.setEnabled(false);
            }
        }
	}
	
	public static void exportModel() throws Exception {		
		if (fChooser.getCommand().startsWith("Model")) {
    		index = 0;            	
        	String fileName =  FileDialog.showFileDialog(true, ".*", "all files", Main.mainFrame.initialDirectory);
        	if (null == fileName)
        		return;
            fileExportModel = new File(fileName).getPath();
            fileExportModel = new File(fileName).getParent();
            
			fChooser.getTextField()[index].setText(fileExportModel);
            System.out.printf("User selected network file \"%s\"", fileExportModel);

		}
		else if (fChooser.getCommand().startsWith("Demand")) {
			index = 1;
        	String fileName =  FileDialog.showFileDialog(true, "xml", "xml files", Main.mainFrame.initialDirectory);
        	if (null == fileName)
        		return;
            fileExportModel = new File(fileName).getPath();
			fChooser.getTextField()[index].setText(fileExportModel);
            System.out.printf("User selected network file \"%s\"", fileExportModel);
		}
    
	}

    private static boolean openDialogExportFile() {
    	String fileName = FileDialog.showFileDialog(false, "xml", "xml files", Main.mainFrame.initialDirectory);
    	if (null == fileName)
    		return false;
    	fileExportDemand = new File(fileName).getPath();
		fChooser.getTextField()[index].setText(fileExportDemand);
        System.out.printf("User selected network file \"%s\"", fileExportDemand);
        return true;
    }
    
    private static String[] names = null;
	public static String[] getNames() {
		return names;
	}
    
    public static String getFileSavedModel() {
		return fileExportModel;
	}

	public Model getExportModel() {
		return exportModel;
	}

	public void setExportModel(Model loadedModel) {
		this.exportModel = loadedModel;
	}

    public static JPanel getCards() {
		return cards;
	}

	public static void setCards(JPanel cards) {
		ExportModel.cards = cards;
	}

	public JButton getFinishButton() {
		return finishButton;
	}

	public void setFinishButton(JButton finishButton) {
		this.finishButton = finishButton;
	}

	public JButton getNextButton() {
		return nextButton;
	}

	public void setNextButton(JButton nextButton) {
		this.nextButton = nextButton;
	}

	public JButton getPrevButton() {
		return prevButton;
	}

	public void setPrevButton(JButton prevButton) {
		this.prevButton = prevButton;
	}

}
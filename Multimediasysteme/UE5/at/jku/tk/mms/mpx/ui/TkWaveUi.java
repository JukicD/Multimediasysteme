package at.jku.tk.mms.mpx.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import at.jku.tk.mms.mpx.App;

/**
 * Provides the user interface for the TkWave encoder / decoder
 * 
 * @author matthias
 *
 */
public class TkWaveUi extends JFrame {

	private static final long serialVersionUID = 5058168996116569186L;
	
	private static final int SPACE = 15;
	
	private JComboBox<ComboSelectModeItem> modeSelectCombo;

	private JComboBox<ComboSelectDefaultWaveItem> fileSelectCombo;
	private JLabel inputFileSelectLabel;
	private JTextField inputFileSelectText;
	private JButton inputFileSelectOpen;
	
	private JTextField outputFileSelectText;
	private JButton outputFileSelectOpen;
	
	private JButton convert;
	
	private MixerPanel mixerPanel;
	
	private boolean encode;
	
	/**
	 * Creates the UI 
	 * 
	 */
	public TkWaveUi() {
		super("TkWave Converter");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initUi();
		String defOutputFile = System.getProperty("user.home") + (TkWaveUi.isOSX() ? "/Desktop" : "") + "/out." + (this.encode ? "tkw" : "wav");
		this.outputFileSelectText.setText(defOutputFile);
		this.pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
	}

	/**
	 * Creates the user interface
	 */
	private void initUi() {
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		
		this.convert = new JButton();
		this.mixerPanel = new MixerPanel(App.BLOCK_SIZE);
		
		this.modeSelectCombo = new JComboBox<ComboSelectModeItem>(ComboSelectModeItem.getOptions());
		this.modeSelectCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateFileSelectCombo();
			}
		});
		this.getContentPane().add(this.modeSelectCombo);
		this.getContentPane().add(Box.createVerticalStrut(SPACE));
		
		this.inputFileSelectLabel = new JLabel("Select input file:");
		Box inputFileSelectLabelBox = Box.createHorizontalBox();
		inputFileSelectLabelBox.add(this.inputFileSelectLabel);
		this.getContentPane().add(inputFileSelectLabelBox);
		
		ComboSelectDefaultWaveItem[] selectItems = new ComboSelectDefaultWaveItem[] {
				new ComboSelectDefaultWaveItem("Arnold Schwarzenegger 'Questions'", "/questions.wav"),
				new ComboSelectDefaultWaveItem("Maryling Monroe 'Born Yesterday'", "/marilyn.wav"),
				new ComboSelectDefaultWaveItem("Select custom file ...", null),
		};
		this.fileSelectCombo = new JComboBox<ComboSelectDefaultWaveItem>(selectItems);
		this.fileSelectCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateFileSelectCombo();
			}
		});
		
		Box inputFileSelectBox = Box.createHorizontalBox();
		this.getContentPane().add(fileSelectCombo);
		this.getContentPane().add(Box.createVerticalStrut(SPACE));
		
		this.inputFileSelectText = new JTextField();
		inputFileSelectBox.add(this.inputFileSelectText);
		inputFileSelectBox.add(Box.createHorizontalStrut(SPACE));
		this.inputFileSelectOpen = new JButton("open");
		this.inputFileSelectOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseInputFile();
			}
		});
		inputFileSelectBox.add(this.inputFileSelectOpen);
		this.getContentPane().add(inputFileSelectBox);
		this.getContentPane().add(Box.createVerticalStrut(SPACE));
		updateFileSelectCombo();
		
		this.getContentPane().add(this.mixerPanel);
		
		Box outputFileSelectLabelBox = Box.createHorizontalBox();
		outputFileSelectLabelBox.add(new JLabel("Select output file:"));
		this.getContentPane().add(outputFileSelectLabelBox);
		
		Box outputFileSelectBox = Box.createHorizontalBox();
		this.outputFileSelectText = new JTextField();
		outputFileSelectBox.add(this.outputFileSelectText);
		outputFileSelectBox.add(Box.createHorizontalStrut(SPACE));
		this.outputFileSelectOpen = new JButton("open");
		this.outputFileSelectOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseOutputFile();
			}
		});
		outputFileSelectBox.add(this.outputFileSelectOpen);
		this.getContentPane().add(outputFileSelectBox);
		this.getContentPane().add(Box.createVerticalStrut(SPACE));
		
		Box convertButtonBox = Box.createHorizontalBox();
		convertButtonBox.add(this.convert);
		this.convert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				convert();
			}
		});
		this.getContentPane().add(convertButtonBox);
	}
	
	/**
	 * Can be used to correctly enable and disable parts of the UI
	 */
	protected void updateFileSelectCombo() {
		boolean lastUiState = this.encode;
		
		ComboSelectModeItem modeSelectItem = (ComboSelectModeItem) modeSelectCombo.getSelectedItem();
		ComboSelectDefaultWaveItem fileSelectItem = (ComboSelectDefaultWaveItem) fileSelectCombo.getSelectedItem();
		boolean fileSelectEnable = modeSelectItem != null && fileSelectItem != null && ((modeSelectItem.getMode() == 0 && fileSelectItem.getReference() == null) || modeSelectItem.getMode() == 1);
		this.inputFileSelectLabel.setEnabled(fileSelectEnable);
		this.inputFileSelectText.setEnabled(fileSelectEnable);
		this.inputFileSelectOpen.setEnabled(fileSelectEnable);
		
		boolean fileDefaultEnable = modeSelectItem != null && modeSelectItem.getMode() == 0;
		this.fileSelectCombo.setEnabled(fileDefaultEnable);
		this.mixerPanel.setEnabled(fileDefaultEnable);
		if(fileDefaultEnable) {
			this.convert.setText("Encode");
		}else{
			this.convert.setText("Decode");
			if(lastUiState) {
				this.inputFileSelectText.setText(this.outputFileSelectText.getText());
				this.outputFileSelectText.setText("");
			}
		}
		this.encode = fileDefaultEnable;
	}
	
	/**
	 * {@link JFileChooser} for input file
	 */
	protected void chooseInputFile() {
		JFileChooser chooser;
		if(this.inputFileSelectText.getText().trim().length() > 0) {
			File f = new File(this.inputFileSelectText.getText().trim());
			chooser = new JFileChooser(f.getParentFile());
		}else{
			chooser = new JFileChooser();
		}
		FileFilter ff = new SoundFileFilters(this.encode ? 0 : 1);
		chooser.addChoosableFileFilter(ff);
		chooser.setFileFilter(ff);
		int res = chooser.showOpenDialog(this);
		if (res == JFileChooser.APPROVE_OPTION) {
			this.inputFileSelectText.setText(chooser.getSelectedFile().getAbsolutePath());
		}
	}
	
	/**
	 * {@link JFileChooser} for output file
	 */
	protected void chooseOutputFile() {
		JFileChooser chooser;
		if(this.outputFileSelectText.getText().trim().length() > 0) {
			File f = new File(this.outputFileSelectText.getText().trim());
			chooser = new JFileChooser(f.getParentFile());
		}else{
			chooser = new JFileChooser();
		}
		FileFilter ff = new SoundFileFilters(this.encode ? 1 : 0);
		chooser.addChoosableFileFilter(ff);
		chooser.setFileFilter(ff);
		int res = chooser.showSaveDialog(this);
		if (res == JFileChooser.APPROVE_OPTION) {
			String path = chooser.getSelectedFile().getAbsolutePath();
			String ext = this.encode ? ".tkw" : ".wav";
			if(!path.toLowerCase().endsWith(ext)) {
				path += ext;
			}
			this.outputFileSelectText.setText(path);
		}
	}
	
	/**
	 * Starts the conversion process
	 */
	protected void convert() {
		File inputFile = new File(this.inputFileSelectText.getText());
		File outputFile = new File(this.outputFileSelectText.getText());
		String info = null;
		if(outputFile.exists()) {
			int res = JOptionPane.showConfirmDialog(this, "The file '" + outputFile.getAbsolutePath() + "' already exists! Do you want to overwrite it?", "Overwrite file?", JOptionPane.YES_NO_OPTION);
			if(res != 0) {
				return;
			}
		}
		try {
			if(this.encode) {
				ComboSelectDefaultWaveItem item = (ComboSelectDefaultWaveItem) this.fileSelectCombo.getSelectedItem();
				if(item == null || item.getReference() == null) {
					info = App.encode(inputFile, outputFile, this.mixerPanel.getFactors());
				}else{
					InputStream in = this.getClass().getResourceAsStream(item.getReference());
					info = App.encode(in, outputFile, this.mixerPanel.getFactors());
				}
			}else{
				App.decode(inputFile, outputFile);
			}
		}catch(Throwable t) {
			t.printStackTrace();
			JOptionPane.showMessageDialog(this, t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE, null);
			return;
		}
		if(info == null) {
			info = "Successfully converted!";
		}else{
			info = "Successfully converted!: " + info; 
		}
		JOptionPane.showMessageDialog(this, info, "Info", JOptionPane.INFORMATION_MESSAGE, null);
	}
	
	/**
	 * True if we run on MacOS X
	 * 
	 * @return
	 */
	public static boolean isOSX() {
	    String osName = System.getProperty("os.name");
	    return osName.contains("OS X");
	}

}

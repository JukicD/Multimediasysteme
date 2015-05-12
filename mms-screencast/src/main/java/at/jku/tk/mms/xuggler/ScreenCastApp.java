package at.jku.tk.mms.xuggler;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import at.jku.tk.mms.xuggler.impl.ScreenCastImpl;

/**
 * Application capable of recording screencasts
 */
public class ScreenCastApp extends JFrame {

	private static final long serialVersionUID = 4149675007191047575L;
	
	private JTextField fileName;

	private JTextField recordingTime;

	/**
	  * Creates new Screncast APP with GUI
	  */
	 public ScreenCastApp() {
		 this(null);
	 }
	 
	 /**
	  * Creates new Screencast APP with GUI and specifies output filename
	  * 
	  * @param filename
	  */
	 public ScreenCastApp(String filename) {
		 super("Screencast Recorder");
		 initUi();
	 }
	 
	 /**
	  * Initializes the UI
	  */
	 private void initUi() {
		 final JFrame tmpFrame = this;
		 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));
		 
		 Box inputBox = Box.createVerticalBox();
		 
		 Box fileNameBox = Box.createHorizontalBox();
		 fileName = new JTextField();
		 fileName.setColumns(50);
		 JButton fileSelectButton = new JButton("...");
		 fileNameBox.add(Box.createHorizontalStrut(5));
		 fileNameBox.add(new JLabel("Filename"));
		 fileNameBox.add(Box.createHorizontalStrut(5));
		 fileNameBox.add(fileName);
		 fileNameBox.add(Box.createHorizontalStrut(5));
		 fileNameBox.add(fileSelectButton);
		 fileNameBox.add(Box.createHorizontalStrut(5));
		 fileSelectButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = new JFileChooser();
					fc.setFileFilter(new FileFilter() {
						@Override
						public String getDescription() {
							return "H.264 MP4 Files";
						}
						@Override
						public boolean accept(File f) {
							String n = f.getName();
							return n.endsWith(".mp4");
						}
					});
					if(fc.showSaveDialog(tmpFrame) == JFileChooser.APPROVE_OPTION) {
						File f = fc.getSelectedFile();
						fileName.setText(f.getAbsolutePath());
					}
				}
			});
		 
		 
		 Box recordingTimeBox = Box.createHorizontalBox();
		 recordingTime = new JTextField();
		 recordingTimeBox.add(Box.createHorizontalStrut(5));
		 recordingTimeBox.add(new JLabel("Recording T. sec."));
		 recordingTimeBox.add(Box.createHorizontalStrut(5));
		 recordingTimeBox.add(recordingTime);
		 recordingTimeBox.add(Box.createHorizontalStrut(5));
		 
		 inputBox.add(fileNameBox);
		 inputBox.add(Box.createVerticalStrut(5));
		 inputBox.add(recordingTimeBox);
		 
		 JButton record = new JButton(new ImageIcon(getClass().getResource("/Record-Normal-icon.png")));
		 record.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				startRecording();
			}
		});
		 getContentPane().add(inputBox);
		 getContentPane().add(record);
		 
		 pack();
	 }
	 
	 /**
	  * Check input fields and start a recorder
	  */
	 private void startRecording() {
		 long seconds = Long.parseLong(recordingTime.getText());
		 ScreenCastImpl cast;
		 try {
			 cast = new ScreenCastImpl(fileName.getText(), seconds);
			 setState(Frame.ICONIFIED);
			 cast.startRecording();
			 setState(Frame.NORMAL);
		 } catch (AWTException e) {
			 e.printStackTrace();
		 } catch (InterruptedException e) {
			e.printStackTrace();
		}
	 }
	 
	 /**
	  * Runs the screencast application
	  * Opens a Window with a record button, a file entry box and a time entry box
	  * 
	  * @param args
	  */
	 public static void main(String[] args) {
		 ScreenCastApp app;
		 if(args.length > 0) {
			 app = new ScreenCastApp(args[0]);
		 }else{
			 app = new ScreenCastApp();
		 }
		 app.setVisible(true);
	 }

}

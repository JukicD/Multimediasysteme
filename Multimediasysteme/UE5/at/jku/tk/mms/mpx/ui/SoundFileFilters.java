package at.jku.tk.mms.mpx.ui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Allows selection of TkWave or WAVE files
 * 
 * @author matthias
 */
public class SoundFileFilters extends FileFilter {
	
	private int mode;
	
	/**
	 * Creates a new filter
	 */
	public SoundFileFilters(int mode) {
		this.mode = mode;
	}

	@Override
	public boolean accept(File f) {
		String fName = f.getName().toLowerCase();
		return mode == 0 && fName.endsWith(".wav") || mode == 1 && fName.endsWith(".tkw");
	}

	@Override
	public String getDescription() {
		return this.mode == 0 ? "WAVE Audio File" : "TkWave Audio File";
	}

}

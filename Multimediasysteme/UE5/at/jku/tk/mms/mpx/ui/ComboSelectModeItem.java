package at.jku.tk.mms.mpx.ui;

import java.io.Serializable;

/**
 * Can be used to select modes
 * 
 * @author matthias
 */
public class ComboSelectModeItem implements Serializable {
	
	private static final long serialVersionUID = -4283605534561659902L;
	
	private int mode;

	private ComboSelectModeItem(int mode) {
		this.mode = mode;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}
	
	@Override
	public String toString() {
		return this.mode == 0 ? "Encode" : "Decode";
	}
	
	public static ComboSelectModeItem[] getOptions () {
		return new ComboSelectModeItem[] {
			new ComboSelectModeItem(0),
			new ComboSelectModeItem(1)
		};
	}
	
}

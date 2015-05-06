package at.jku.tk.mms.mpx.ui;

import java.io.Serializable;

/**
 * Object used to display the options in the display combo
 * 
 * @author matthias
 */
public class ComboSelectDefaultWaveItem implements Serializable {

	private static final long serialVersionUID = 5016490722905192135L;

	private String display;
	
	private String reference;
	
	public ComboSelectDefaultWaveItem(String display, String reference) {
		this.display = display;
		this.reference = reference;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	@Override
	public String toString() {
		return getDisplay();
	}
	
}

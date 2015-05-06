package at.jku.tk.mms.mpx.ui;

import java.io.Serializable;

import at.jku.tk.mms.mpx.App;

/**
 * Used as display item in the {@link MixerPanel}
 * 
 * @author matthias
 */
public class ComboSelectFactorsItem implements Serializable {

	private static final long serialVersionUID = -3575808914365682450L;
	
	private String display;
	
	private double[] factors;
	
	public ComboSelectFactorsItem(String display, double[] factors) {
		this.display = display;
		this.factors = factors;
	}
	
	public double[] getFactors() {
		return this.factors;
	}

	@Override
	public String toString() {
		return this.display;
	}
	
	public static ComboSelectFactorsItem[] getPresets() {
		return new ComboSelectFactorsItem[] {
			new ComboSelectFactorsItem(".. select a preset ..", null),
			new ComboSelectFactorsItem("Identity (4.0)", App.FACTORS_IDENTITY),
			new ComboSelectFactorsItem("Low linear 10.0", App.FACTORS_10),
			new ComboSelectFactorsItem("Strong linear 25.0", App.FACTORS_25),
			new ComboSelectFactorsItem("Low-pass + high-pass", App.FACTORS_LOW_HIGH),
			new ComboSelectFactorsItem("Optimised", App.FACTORS_OPTIMISED),
		};
	}
	
}

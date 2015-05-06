package at.jku.tk.mms.mpx.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;

/**
 * Mixer for qantisation factors
 * 
 * @author matthias
 */
public class MixerPanel extends JPanel {

	private static final int SPACE = 15;

	private static final long serialVersionUID = 389698168456342573L;
	
	private final int size;
	
	private JSlider[] sliders;

	private JComboBox<ComboSelectFactorsItem> factorPresets;
	
	/**
	 * Creates the mixer Panel
	 */
	public MixerPanel(int size) {
		this.size = size;
		initUi();
	}
	
	/** Initialises the UI */
	private void initUi() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		factorPresets = new JComboBox<ComboSelectFactorsItem>(ComboSelectFactorsItem.getPresets());
		factorPresets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ComboSelectFactorsItem si = (ComboSelectFactorsItem) factorPresets.getSelectedItem();
				if(si != null && si.getFactors() != null) {
					for(int i=0;i<sliders.length;i++) {
						sliders[i].setValue((int) si.getFactors()[i]);
					}
				}
			}
		});
		add(factorPresets);
		add(Box.createVerticalStrut(SPACE));
		
		sliders = new JSlider[this.size];
		Box sliderBox = Box.createHorizontalBox();
		for(int i=0;i<this.size;i++) {
			if(i > 0) {
				sliderBox.add(Box.createHorizontalStrut(SPACE));
			}
			this.sliders[i] = new JSlider(JSlider.VERTICAL);
			this.sliders[i].setPaintLabels(true);
			this.sliders[i].setPaintTicks(true);
			this.sliders[i].setMinimum(4);
			this.sliders[i].setMaximum(100);
			this.sliders[i].setMajorTickSpacing(25);
			sliderBox.add(this.sliders[i]);
		}
		add(sliderBox);
	}
	
	/** Get the currently set factors */
	public double[] getFactors() {
		double[] factors = new double[this.sliders.length];
		for(int i=0;i<factors.length;i++) {
			factors[i] = this.sliders[i].getValue();
		}
		return factors;
	}

	@Override
	public void setEnabled(boolean enabled) {
		for(int i=0;i<this.sliders.length;i++) {
			this.sliders[i].setEnabled(enabled);
		}
		this.factorPresets.setEnabled(enabled);
		super.setEnabled(enabled);
	}

}

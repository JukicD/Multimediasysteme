package at.jku.tk.mms.img;

import java.awt.image.BufferedImage;
import java.util.Properties;

/** Just to show correct handles in ComboBox */
public class DummyFilterImplementation implements FilterInterface{

	@Override
	public BufferedImage runFilter(BufferedImage img, Properties settings) {
		throw new IllegalStateException("Dummy Filter not implemented");
	}

	@Override
	public String[] mandatoryProperties() {
		throw new IllegalStateException("Dummy Filter not implemented");
	}
	
	@Override
	public String toString() {
		return "<select a filter>";
	}

}

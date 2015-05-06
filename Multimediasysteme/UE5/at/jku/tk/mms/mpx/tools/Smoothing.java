package at.jku.tk.mms.mpx.tools;

/**
 * Helper class that 
 * 
 * @author matthias
 *
 */
public class Smoothing {

	/**
	 * apply a smoothing filter to data
	 * 
	 * @param input
	 * @return
	 */
	public static void smooth(byte[] input, byte smoothing) {
		if(input.length == 0) {
			return;
		}
		byte value = input[0];
		for(int i=1;i<input.length;i++) {
			byte currentValue = input[i];
			value += (currentValue - value) / smoothing;
			input[i] = value;
		}
	}
	
}

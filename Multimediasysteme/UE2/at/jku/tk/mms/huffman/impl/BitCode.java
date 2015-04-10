package at.jku.tk.mms.huffman.impl;

/**
 * Represents a single bitcode of the lookup table 
 * @author matthias
 */
public class BitCode {

	private byte code;
	private int length;
	
	public BitCode(byte code, int length) {
		this.code = code;
		this.length = length;
	}

	@Override
	public String toString() {
		String printalbe = Integer.toBinaryString(code);
		return "BitCode [" + printalbe + ", " + length + "]";
	}
	
	public byte getCode() {
		return code;
	}
	
	public int getLength() {
		return length;
	}
}

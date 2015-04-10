package at.jku.tk.mms.huffman.impl;

import java.util.ArrayList;

/**
 * Manages an array of {@link BitCode} which may then get converted to a byte array 
 * 
 * @author matthias
 */
public class BitCodeArray {
	
	private ArrayList<BitCode> bitCodeArray;
	
	/** Initialize the backing data structure */
	public BitCodeArray() {
		bitCodeArray = new ArrayList<BitCode>();
	}
	
	/** Append bitcode to the end */
	public void append(BitCode code) {
		bitCodeArray.add(code);
	}

	public byte[] toByteArray() {
		BitStream bst = new BitStream();
		for(BitCode code : bitCodeArray) {
			bst.writeBitCode(code);
		}
		bst.flush();
		return bst.toByteArray();
	}
	
}

package at.jku.tk.mms.huffman;

import at.jku.tk.mms.huffman.impl.FreqencyTable;
import at.jku.tk.mms.huffman.impl.HuffmanTree;

/**
 * Implementation of the Huffman Encoding
 * 
 * @author matthias
 */
public class HuffmanCompression {
	
	private FreqencyTable freqTable;
	
	private HuffmanTree huffmanTree;

	/**
	 * Creates a new Huffman Encoder initialized with plain byte arr for frequency table creation
	 * 
	 * @param ipt
	 */
	public HuffmanCompression(byte[] ipt) {
		this.freqTable = new FreqencyTable(ipt);
		this.huffmanTree = new HuffmanTree(this.freqTable);
	}
	
	/** Getter for Frequency Table */
	public FreqencyTable getFreqencyTable() {
		return this.freqTable;
	}
	
	/** Getter for Huffman Tree */
	public HuffmanTree getHuffmanTree() {
		return this.huffmanTree;
	}
	
	/** Encode with huffman lookup table */
	public byte[] compress(byte[] data) {
		
	}
	
	/** Decode with huffman lookup table */
	public byte[] decompress(byte[] compressedData) {
/* 		@TODO Place your implementation here		 */
	}
}

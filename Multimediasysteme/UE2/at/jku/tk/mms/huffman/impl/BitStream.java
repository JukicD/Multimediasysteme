package at.jku.tk.mms.huffman.impl;

import java.util.ArrayList;
import java.util.List;

/** Read and write bits in an array only sequential! */
public class BitStream {

	private ArrayList<Byte> backingArray;
	
	private byte curByte;
	private int curByteLen;
	
	private boolean modeReading;
	
	private boolean debug = false;
	
	private String writeLog = "";
	private String readLog = "";
	
	/** Create empty bitstream for writing */
	public BitStream() {
		backingArray = new ArrayList<Byte>();
		curByte = 0;
		curByteLen = 0;
		modeReading = false;
	}
	
	/** create bitstream for reading */
	public BitStream(byte[] data) {
		backingArray = new ArrayList<Byte>();
		for(byte cur : data) {
			backingArray.add(cur);
		}
		modeReading = true;
		curByteLen = 0;
		curByte = 0;
	}
	
	/** write a single bit to the byte array */
	public void writeBit(byte in) {
		if(modeReading) throw new IllegalStateException("You were writing to a bit stream which is in reading mode");
		in = (byte) (in & 1);
		if(debug) writeLog += in;
		curByte = (byte) (curByte | in);
		curByteLen ++;
		if(curByteLen > 7) {
			if(debug) {
				System.out.println("write log " + writeLog + " vs binary representation " + Integer.toBinaryString(curByte) + " resulting in " + curByte);
				writeLog = "";
			}
			backingArray.add(curByte);
			curByte = 0;
			curByteLen = 0;
		}else{
			curByte = (byte) ( (curByte << 1) & 0xFF);
		}
	}
	
	/** pad rest with 0 */
	public void flush() {
		while(curByteLen != 0) {
			writeBit((byte)0);
		}
	}

	/** write a bit code object to the array */
	public void writeBitCode(BitCode code) {
		int l = code.getLength();
		byte c = code.getCode();
		
		for(int i=0; i<l; i++) {
			int s = l - i - 1;
			byte im = (byte) (c >> s);
			writeBit(im);
		}
	}

	/** convert backing structure to byte array */
	public byte[] toByteArray() {
		return convertToByteArray(backingArray);
	}
	
	/** convert a list to a byte array */
	public static byte[] convertToByteArray(List<Byte> list) {
		byte[] arr = new byte[list.size()];
		int i = 0;
		for(byte b : list) {
			arr[i++] = b;
		}
		return arr;
	}
	
	/** any bits left for reading? */
	public boolean canRead() {
		return modeReading && (backingArray.size() > 0 || curByteLen > 0);
	}
	
	/** read bit from stream */
	public byte readBit() {
		if(!canRead()) throw new IllegalStateException("You were trying to read from an empty or non readable bit stream");
		if(curByteLen == 0) {
			curByte = backingArray.remove(0);
			curByteLen = 8; // zai0oo
			if(debug) System.out.println("Getting new Byte " + curByte);
		}
		curByteLen --;
		byte result = (byte) ((curByte >> curByteLen) & 1);
		if(debug) {
			if(readLog.length() == 8) {
				System.out.println("Read Log " + readLog);
				readLog = "";
			}
			readLog += result;
		}
		return result;
	}
	
	public void setDebug(boolean value) {
		debug = value;
	}
	
}

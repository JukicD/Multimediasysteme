package at.jku.tk.mms.huffman.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import at.jku.tk.mms.huffman.impl.BitStream;

public class BitStreamTest {

	public static final String TEST_INPUT = "Two goldfish are in a tank. One says to the other, \"Do you know how to drive this thing?\"";

	@Test
	public void testBitStreamWriting() {
		BitStream s = new BitStream();
		s.setDebug(true);
		s.writeBit((byte)0);// byte 1 / val 85
		s.writeBit((byte)1);
		s.writeBit((byte)0);
		s.writeBit((byte)1);
		s.writeBit((byte)0);
		s.writeBit((byte)1);
		s.writeBit((byte)0);
		s.writeBit((byte)1);
		s.writeBit((byte)0);// byte 2 / val 85
		s.writeBit((byte)1);
		s.writeBit((byte)0);
		s.writeBit((byte)1);
		s.writeBit((byte)0);
		s.writeBit((byte)1);
		s.writeBit((byte)0);
		s.writeBit((byte)1);
		s.writeBit((byte)0);// byte 3 / val 64
		s.writeBit((byte)1);// fill with 0
		s.flush();
		
		byte[] arr = s.toByteArray();
		assertEquals(3, arr.length);
		assertEquals(85, arr[0]);
		assertEquals(85, arr[1]);
		assertEquals(64, arr[2]);
	}

	@Test
	public void testBitStreamReading() {
		byte[]arr = new byte [] { 85, 85, 64 };
		BitStream s = new BitStream(arr);
		s.setDebug(true);
		String cur = "";
		int i=0;
		while(s.canRead()) {
			if(s.readBit() > 0) {
				cur += "1";
			}else{
				cur += "0";
			}
			i++;
		}
		assertEquals(24, i);
		assertEquals("010101010101010101000000", cur);
	}

	@Test
	public void testBitStreamInOut() {
		BitStream in = new BitStream(TEST_INPUT.getBytes());
		in.setDebug(true);
		BitStream out = new BitStream();
		out.setDebug(true);
		int i=0;
		while(in.canRead()) {
			byte bit = in.readBit();
			out.writeBit(bit);
			i++;
		}
		out.flush();
		assertEquals(TEST_INPUT.length() * 8, i);
		String str = new String(out.toByteArray());
		assertEquals(TEST_INPUT, str);
	}

}

package at.jku.tk.mms.huffman.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import at.jku.tk.mms.huffman.HuffmanCompression;
import at.jku.tk.mms.huffman.impl.TreeNode;

/**
 * Test the {@link HuffmanCompression} classes
 *  
 * @author matthias
 */
public class HuffmanCompressionTest {
	
	public static final String TEST_INPUT = "abracadabra"; //"Two goldfish are in a tank. One says to the other, \"Do you know how to drive this thing?\"";
	public static final byte[] COMPRESSED_INPUT = new byte[] { -7, 116, 116, -65, 115, -10, 55, 70, -84, -28, 73, -93, 53, 61, -80, 123, -105, 55, -81, 54, 98, 50, -100, 89, 79, 111, -25, 30, -111, -30, 100, -18, 85, -54, 87, 49, 28, -38, 26, 115, 42, 54, 101, 68, -12, -33, 0 };
	public static final String TEST_INPUT_SHORT = "MMS";
	public static final byte[] COMPRESSED_SHORT = new byte[] { -64 };
	public static final String TEST_ABRACADABRA = "abracadabra";
	public static final byte[] COMPRESSED_ABRACADABRA = new byte[] { 110, -118, -36 };

	@Test
	public void testFrequencyTable() {
		HuffmanCompression compressor = new HuffmanCompression(TEST_INPUT.getBytes());
		assertEquals(9,  compressor.getFreqencyTable().getFrequency((byte) 'o'));
		assertEquals(18, compressor.getFreqencyTable().getFrequency((byte) ' '));
		assertEquals(2,  compressor.getFreqencyTable().getFrequency((byte) '"'));
		assertEquals(1,  compressor.getFreqencyTable().getFrequency((byte) '?'));
		assertEquals(1,  compressor.getFreqencyTable().getFrequency((byte) 'T'));
	}
	
	@Test
	public void testFrequencyTableShort() {
		HuffmanCompression compressor = new HuffmanCompression(TEST_INPUT_SHORT.getBytes());
		assertEquals(2, compressor.getFreqencyTable().getFrequency((byte) 'M'));
		assertEquals(1, compressor.getFreqencyTable().getFrequency((byte) 'S'));
	}
	
	@Test
	public void testHuffmanTree() {
		HuffmanCompression compressor = new HuffmanCompression(TEST_INPUT_SHORT.getBytes());
		TreeNode root = compressor.getHuffmanTree().getRootNode();
		assertEquals("Root node has wrong frequency", root.getFreq(), 3);
		TreeNode left = root.getLeft();
		TreeNode right = root.getRight();
		if(left.getFreq() == 1) {
			assertEquals("Left node has wrong frequency", 1, left.getFreq());
			assertEquals("Right node has wrong frequency", 2, right.getFreq());
			assertEquals("Left node has wrong value", (byte) 83, (byte) left.getValue());
			assertEquals("Right node has wrong value", (byte) 77, (byte) right.getValue());
		}else{
			assertEquals("Left node has wrong frequency", 2, left.getFreq());
			assertEquals("Right node has wrong frequency", 1, right.getFreq());
			assertEquals("Left node has wrong value", (byte) 77, (byte) left.getValue());
			assertEquals("Right node has wrong value", (byte) 83, (byte) right.getValue());
			
		}
	}
	
	@Test
	public void testCompressionInOutShort() {
		HuffmanCompression compressor = new HuffmanCompression(TEST_INPUT_SHORT.getBytes());
		byte[] compressed = compressor.compress(TEST_INPUT_SHORT.getBytes());
		byte[] uncompressed = compressor.decompress(compressed);
		String result = new String(uncompressed);
		assertEquals(TEST_INPUT_SHORT, result.substring(0, TEST_INPUT_SHORT.length()));
		System.out.println("Compression result: " + TEST_INPUT_SHORT.getBytes().length + " vs " + compressed.length);
	}
	
	@Test
	public void testCompressionInOutLong() {
		HuffmanCompression compressor = new HuffmanCompression(TEST_INPUT.getBytes());
		byte[] compressed = compressor.compress(TEST_INPUT.getBytes());
		byte[] uncompressed = compressor.decompress(compressed);
		String result = new String(uncompressed);
		assertEquals(TEST_INPUT, result.substring(0, TEST_INPUT.getBytes().length));
		System.out.println("Compression result: " + TEST_INPUT.getBytes().length + " vs " + compressed.length);
	}
	
	@Test
	public void testCompressionInOutAbracadabra() {
		HuffmanCompression compressor = new HuffmanCompression(TEST_ABRACADABRA.getBytes());
		byte[] compressed = compressor.compress(TEST_ABRACADABRA.getBytes());
		byte[] uncompressed = compressor.decompress(compressed);
		String result = new String(uncompressed);
		assertEquals(TEST_ABRACADABRA, result.substring(0, TEST_ABRACADABRA.getBytes().length));
		System.out.println("Compression result: " + TEST_ABRACADABRA.getBytes().length + " vs " + compressed.length);
	}
	
	@Test
	public void testCompressionShort() {
		testCompress(TEST_INPUT_SHORT, COMPRESSED_SHORT);
	}
	
	@Test
	public void testCompressionAbracadabra() {
		testCompress(TEST_ABRACADABRA, COMPRESSED_ABRACADABRA);
	}
	
	@Test
	public void testCompressionLong() {
		testCompress(TEST_INPUT, COMPRESSED_INPUT);
	}
	
	public void testCompress(String input, byte[] expected) {
		HuffmanCompression compressor = new HuffmanCompression(input.getBytes());
		byte[] compress = compressor.compress(input.getBytes());
		assertArrayEquals("Compression not successful", expected, compress);
	}
	
}
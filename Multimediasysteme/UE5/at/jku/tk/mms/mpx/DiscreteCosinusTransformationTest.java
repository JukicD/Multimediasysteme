package at.jku.tk.mms.mpx;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

import org.junit.Test;

public class DiscreteCosinusTransformationTest {
	
	public static double DELTA = 0.5;
	public static double ROUNDING_FACTOR = 100.0;
	public static boolean DEBUG_LOG = true;
	
	public static double[] TEST_BLOCK_1 = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };
	public static double[] TEST_BLOCK_1_DCT = new double[] { 2.83, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
	public static byte[] TEST_BLOCK_1_QUANT = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 };
	public static double[] TEST_BLOCK_1_IQUANT = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
	public static double[] TEST_BLOCK_1_IDCT = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
	
	public static double[] TEST_BLOCK_127 = new double[] { 127.0, 127.0, 127.0, 127.0, 127.0, 127.0, 127.0, 127.0 };
	public static double[] TEST_BLOCK_127_DCT = new double[] { 359.21, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
	public static byte[] TEST_BLOCK_127_QUANT = new byte[] { 33, 0, 0, 0, 0, 0, 0, 0 };
	public static double[] TEST_BLOCK_127_IQUANT = new double[] { 363.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
	public static double[] TEST_BLOCK_127_IDCT = new double[] { 127.0, 127.0, 127.0, 127.0, 127.0, 127.0, 127.0, 127.0 };
	
	public static double[] TEST_NOISE = new double[] { 127.0, 127.0, 127.0, -128.0, 127.0, 127.0, -128.0, -128.0 };
	public static double[] TEST_NOISE_DCT = new double[] { 88.74, 206.19, -48.79, 151.97, -90.16, -160.23, 117.79, 79.09 };
	public static byte[] TEST_NOISE_QUANT = new byte[] { 8, 12, -2, 5, -2, -3, 2, 1 };
	public static double[] TEST_NOISE_IQUANT = new double[] { 88.0, 204.0, -44.0, 160.0, -82.0, -153.0, 128.0, 89.0 };
	public static double[] TEST_NOISE_IDCT = new double[] { 127.0, 112.07, 127.0, -128.0, 127.0, 127.0, -126.95, -126.45 };
	
	public static double[] TEST_SAMPLE1 = new double[] { -83.0, -91.0, -107.0, -110.0, -107.0, -109.0, 122.0, 118.0 };
	public static double[] TEST_SAMPLE1_DCT = new double[] { -129.75, -186.86, 163.67, -62.93, 1.06, 47.57, -65.88, 41.86 };
	public static byte[] TEST_SAMPLE1_QUANT = new byte[] { -12, -11, 7, -2, 0, 1, -1, 0 };
	public static double[] TEST_SAMPLE1_IQUANT = new double[] { -132.0, -187.0, 154.0, -64.0, 0.0, 51.0, -64.0, 0.0 };
	public static double[] TEST_SAMPLE1_IDCT = new double[] { -91.92, -84.15, -121.29, -84.82, -126.3, -90.11, 108.87, 116.37 };
	
	@Test
	public void testDctBlock1() { testDct(TEST_BLOCK_1, TEST_BLOCK_1_DCT); }
	
	@Test
	public void testDctBlock127() { testDct(TEST_BLOCK_127, TEST_BLOCK_127_DCT); }
	
	@Test
	public void testDctNoise() { testDct(TEST_NOISE, TEST_NOISE_DCT); }
	
	@Test
	public void testDctSample1() { testDct(TEST_BLOCK_1, TEST_BLOCK_1_DCT); }
	
	private void testDct(double[] input, double[] expected) {
		DiscreteCosinusTransformation dct = new DiscreteCosinusTransformation(input.length);
		double[] result = dct.DCT(input);
		roundArray(result, ROUNDING_FACTOR);
		assertArrayEquals(expected, result, DELTA);
	}
	
	@Test
	public void testQuantisationBlock1() { testQuantisation(TEST_BLOCK_1_DCT, TEST_BLOCK_1_QUANT, App.FACTORS_OPTIMISED); }
	
	@Test
	public void testQuantisationBlock172() { testQuantisation(TEST_BLOCK_127_DCT, TEST_BLOCK_127_QUANT, App.FACTORS_OPTIMISED); }
	
	@Test
	public void testQuantisationNoise() { testQuantisation(TEST_NOISE_DCT, TEST_NOISE_QUANT, App.FACTORS_OPTIMISED); }
	
	@Test
	public void testQuantisationSample1() { testQuantisation(TEST_SAMPLE1_DCT, TEST_SAMPLE1_QUANT, App.FACTORS_OPTIMISED); }
	
	private void testQuantisation(double[] input, byte[] expected, double[] factors) {
		DiscreteCosinusTransformation dct = new DiscreteCosinusTransformation(input.length);
		int[] result = dct.quant(input, factors);
		byte[] bresult = convertToByte(result);
		assertArrayEquals(expected, bresult);
	}
	
	@Test
	public void testDeQuantisationBlock1() { testDeQuantisation(TEST_BLOCK_1_QUANT, TEST_BLOCK_1_IQUANT, App.FACTORS_OPTIMISED); }
	
	@Test
	public void testDeQuantisationBlock127() { testDeQuantisation(TEST_BLOCK_127_QUANT, TEST_BLOCK_127_IQUANT, App.FACTORS_OPTIMISED); }
	
	@Test
	public void testDeQuantisationNoise() { testDeQuantisation(TEST_NOISE_QUANT, TEST_NOISE_IQUANT, App.FACTORS_OPTIMISED); }
	
	@Test
	public void testDeQuantisationSample1() { testDeQuantisation(TEST_SAMPLE1_QUANT, TEST_SAMPLE1_IQUANT, App.FACTORS_OPTIMISED); }
	
	private void testDeQuantisation(byte[] input, double[] expected, double[] factors) {
		DiscreteCosinusTransformation dct = new DiscreteCosinusTransformation(input.length);
		double[] iQuant = dct.iQuant(input, factors);
		roundArray(iQuant, ROUNDING_FACTOR);
		assertArrayEquals(expected, iQuant, DELTA);
	}
	
	@Test
	public void testInverseDctBlock1() { testInverseDct(TEST_BLOCK_1_IQUANT, TEST_BLOCK_1_IDCT); }
	
	@Test
	public void testInverseDctBlock127() { testInverseDct(TEST_BLOCK_127_IQUANT, TEST_BLOCK_127_IDCT); }
	
	@Test
	public void testInverseDctNoise1() { testInverseDct(TEST_NOISE_IQUANT, TEST_NOISE_IDCT); }
	
	@Test
	public void testInverseDctSample1() { testInverseDct(TEST_SAMPLE1_IQUANT, TEST_SAMPLE1_IDCT); }
	
	private void testInverseDct(double[] input, double[] expected) {
		DiscreteCosinusTransformation dct = new DiscreteCosinusTransformation(input.length);
		double[] iDCT = dct.iDCT(input);
		roundArray(iDCT, ROUNDING_FACTOR);
		assertArrayEquals(expected, iDCT, DELTA);
	}
	
	@Test
	public void testFullConversion() {
		testFullConversion(TEST_BLOCK_1, TEST_BLOCK_1_DCT, TEST_BLOCK_1_QUANT, TEST_BLOCK_1_IQUANT, TEST_BLOCK_1_IDCT, App.FACTORS_OPTIMISED);
		testFullConversion(TEST_BLOCK_127, TEST_BLOCK_127_DCT, TEST_BLOCK_127_QUANT, TEST_BLOCK_127_IQUANT, TEST_BLOCK_127_IDCT, App.FACTORS_OPTIMISED);
		testFullConversion(TEST_NOISE, TEST_NOISE_DCT, TEST_NOISE_QUANT, TEST_NOISE_IQUANT, TEST_NOISE_IDCT, App.FACTORS_OPTIMISED);
		testFullConversion(TEST_SAMPLE1, TEST_SAMPLE1_DCT, TEST_SAMPLE1_QUANT, TEST_SAMPLE1_IQUANT, TEST_SAMPLE1_IDCT, App.FACTORS_OPTIMISED);
	}

	private void testFullConversion(double[] input, double[] fdct, byte[] quant, double[] iquant, double[] idct, double[] factors) {
		DiscreteCosinusTransformation dct = new DiscreteCosinusTransformation(input.length);
		// Forward DCT process
		double[] result = dct.DCT(input);
		roundArray(result, ROUNDING_FACTOR); // during tests we round for better readability
		debugLog("DCT: " + Arrays.toString(result));
		assertArrayEquals(fdct, result, DELTA);
		
		// Quantization step
		byte[] rquant = convertToByte(dct.quant(result, factors));
		debugLog("quant: " + Arrays.toString(rquant));
		assertArrayEquals(quant, rquant);
		
		// Inverse quantization step
		double[] iQuant = dct.iQuant(rquant,factors);
		roundArray(iQuant, ROUNDING_FACTOR); // during tests we round for better readability
		debugLog("iquant: " + Arrays.toString(iQuant));
		assertArrayEquals(iquant, iQuant, DELTA);
		
		// Inverse DCT
		double[] original = dct.iDCT(iQuant);
		roundArray(original, ROUNDING_FACTOR);
		debugLog("iDCT: " + Arrays.toString(original));
		assertArrayEquals(idct, original, DELTA);
	}
	
	private void roundArray(double[] data, double factor) {
		for(int i=0;i<data.length;i++) {
			data[i] = Math.round(data[i] * factor) / factor;
		}
	}
	
	private byte[] convertToByte(int[] input) {
		byte[] result = new byte[input.length];
		for(int i=0;i<input.length;i++) {
			result[i] = (byte) input[i];
		}
		return result;
	}
	
	private void debugLog(String message) {
		if(DEBUG_LOG) System.out.println(message);
	}
	
}

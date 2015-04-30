package at.jku.tk.mms.jpeg;

import java.io.IOException;
import java.text.DecimalFormat;

import junit.framework.TestCase;

import org.junit.Test;

import at.jku.tk.mms.jpeg.helper.PrintableBlock;
import at.jku.tk.mms.jpeg.helper.PrintableByteArray;
import at.jku.tk.mms.jpeg.impl.Constants;
import at.jku.tk.mms.jpeg.impl.DiscreteCosinusTransformation;
import at.jku.tk.mms.jpeg.impl.JFIFContainer;
import at.jku.tk.mms.jpeg.impl.ZigZag;

/**
 * Is able to test abritary JPEG Encoder functionality
 * 
 * @author matthias
 */
public class EncoderTest extends TestCase {

	@Test
	public void testDiscreteCosinusTransformArbitraryBlock() {
		float[][] data = new float[][] {
				{ -33f,  -87f,  -74f,  -36f,   24f,   13f,  -44f,  -74f},
				{ -75f, -106f, -113f,  -94f,  -90f,    8f,   13f,  -90f},
				{ -85f, -111f, -127f, -123f, -128f, -128f,  -94f,  -93f},
				{-100f, -109f, -124f, -128f, -128f, -122f, -102f,  -90f},
				{ -87f,  -91f, -107f, -120f, -118f, -103f,  -88f,  -82f},
				{ -90f,  -90f,  -96f, -102f, -104f,  -86f,  -77f,  -69f},
				{ -74f,  -79f,  -82f,  -89f,  -84f,  -72f,  -52f,  -61f},
				{ -64f,  -63f,  -69f,  -66f,  -68f,  -78f,  -56f,  -16f}
		};
		double[][] expected	= new double[][] {
				{-13425,-516.38,421.19,385.72,-41,166.71,-15.35,-1.08},
				{121.48,-166.88,-338.69,671.17,-76.46,198.35,43.8,-9.01},
				{1436.93,-257.88,-430.15,339.93,70.18,33.77,62.68,20.75},
				{512.83,-25.24,-354.8,273.01,-27.97,-61.04,103.91,16.16},
				{253,149.14,-149.58,-77.04,277,-222.29,54.38,52.12},
				{-105.12,209.02,-139.4,-66.29,241.11,-135.63,14.78,99.45},
				{-113.53,223.61,-27.32,-213.46,318.38,-170.02,18.15,48.37},
				{-46.88,185.74,20.49,-51.04,121.4,-54,-17.86,69.5}
		};
		assertDCT(data, expected, "Arbritary Image Luminance Block");
	}
	
	@Test
	public void testDiscreteCosinusTransformGreyBlock() {
		float[][] data = new float[][] {
				{ -30f, -30f, -30f, -30f, -30f, -30f, -30f, -30f},
				{ -30f, -30f, -30f, -30f, -30f, -30f, -30f, -30f},
				{ -30f, -30f, -30f, -30f, -30f, -30f, -30f, -30f},
				{ -30f, -30f, -30f, -30f, -30f, -30f, -30f, -30f},
				{ -30f, -30f, -30f, -30f, -30f, -30f, -30f, -30f},
				{ -30f, -30f, -30f, -30f, -30f, -30f, -30f, -30f},
				{ -30f, -30f, -30f, -30f, -30f, -30f, -30f, -30f},
				{ -30f, -30f, -30f, -30f, -30f, -30f, -30f, -30f}
		};
		double[][] expected = new double[][] {
				{-10112,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0}
		};
		assertDCT(data, expected, "Grey Luminance Block");
	}
	
	private void assertDCT(float[][] input, double[][] expected, String label) {
		DiscreteCosinusTransformation dct = new DiscreteCosinusTransformation();
		double[][] result = dct.forwardDCT(input);
		if(!compareBlocks(result, expected)) {
			System.out.println("This is the input block");
			System.out.println(new PrintableBlock(input));
			System.out.println("This is the output block your code calculated");
			System.out.println(new PrintableBlock(result));
			System.out.println("This is the output block we expected");
			System.out.println(new PrintableBlock(expected));
			System.out.println("Your result is differing from the expected result by the following factors");
			double avgFactor = printFactors(result, expected);
			DecimalFormat df = new DecimalFormat("#.##");
			System.out.println("On average your results are differing from the expected results by an factor of " + df.format(avgFactor));
			double addFactor = 1 / avgFactor;
			System.out.println("If the factor is the same for all cells consider adding the factor " + df.format(addFactor) + " to your DCT formula");
			fail("Blocks did not calculate to correct DCT for " + label + " - find details in the Java console");
		} 
	}
	
	@Test
	public void testQuantizationZeroOut() {
		double[][] data	= new double[][] {
				{-13425,-516.38,421.19,385.72,-41,166.71,-15.35,-1.08},
				{121.48,-166.88,-338.69,671.17,-76.46,198.35,43.8,-9.01},
				{1436.93,-257.88,-430.15,339.93,70.18,33.77,62.68,20.75},
				{512.83,-25.24,-354.8,273.01,-27.97,-61.04,103.91,16.16},
				{253,149.14,-149.58,-77.04,277,-222.29,54.38,52.12},
				{-105.12,209.02,-139.4,-66.29,241.11,-135.63,14.78,99.45},
				{-113.53,223.61,-27.32,-213.46,318.38,-170.02,18.15,48.37},
				{-46.88,185.74,20.49,-51.04,121.4,-54,-17.86,69.5}
		};
		double[] quantTable = new double [] {
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 
				0, 0, 0, 0, 0, 0, 0, 0, 
				0, 0, 0, 0, 0, 0, 0, 0, 
				0, 0, 0, 0, 0, 0, 0, 0, 
				0, 0, 0, 0, 0, 0, 0, 0, 
				0, 0, 0, 0, 0, 0, 0, 0 
		};
		int[] expected = new int [] {
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 
				0, 0, 0, 0, 0, 0, 0, 0, 
				0, 0, 0, 0, 0, 0, 0, 0, 
				0, 0, 0, 0, 0, 0, 0, 0, 
				0, 0, 0, 0, 0, 0, 0, 0, 
				0, 0, 0, 0, 0, 0, 0, 0 
		};
		DiscreteCosinusTransformation dct = new DiscreteCosinusTransformation();
		int[] result = dct.quantize(data, quantTable);
		assertTrue("Your quantization step was not successful when trying to zero out the result", compareArrays(result, expected));
	}
	
	@Test
	public void testQuantizationArbritaryLuminanceBlock() {
		float[][] input = new float[][] {
				{ -33f,  -87f,  -74f,  -36f,   24f,   13f,  -44f,  -74f},
				{ -75f, -106f, -113f,  -94f,  -90f,    8f,   13f,  -90f},
				{ -85f, -111f, -127f, -123f, -128f, -128f,  -94f,  -93f},
				{-100f, -109f, -124f, -128f, -128f, -122f, -102f,  -90f},
				{ -87f,  -91f, -107f, -120f, -118f, -103f,  -88f,  -82f},
				{ -90f,  -90f,  -96f, -102f, -104f,  -86f,  -77f,  -69f},
				{ -74f,  -79f,  -82f,  -89f,  -84f,  -72f,  -52f,  -61f},
				{ -64f,  -63f,  -69f,  -66f,  -68f,  -78f,  -56f,  -16f}
		};
		int[] expected = new int[] {-839, -47, 40, 21, -3, 7, -1, 0, 11, -11, -23, 26, -2, 4, 1, 0, 137, -18, -16, 14, 2, 1, 2, 1, 55, -1, -14, 8, -1, -1, 3, 1, 16, 7, -4, -1, 5, -3, 1, 3, -8, 6, -3, -1, 5, -3, 0, 6, -5, 6, -1, -5, 7, -4, 1, 4, -3, 7, 1, -2, 5, -3, -1, 11};
		DiscreteCosinusTransformation dct = new DiscreteCosinusTransformation();
		int[] result = dct.dctQuantLuminance(input);
		assertTrue("Your quantization step was not successful when running on a real world luminance block", compareArrays(result, expected));
	}
	
	@Test
	public void testZigZagScan() {
		int[] input = new int[] {-839, -47, 40, 21, -3, 7, -1, 0, 11, -11, -23, 26, -2, 4, 1, 0, 137, -18, -16, 14, 2, 1, 2, 1, 55, -1, -14, 8, -1, -1, 3, 1, 16, 7, -4, -1, 5, -3, 1, 3, -8, 6, -3, -1, 5, -3, 0, 6, -5, 6, -1, -5, 7, -4, 1, 4, -3, 7, 1, -2, 5, -3, -1, 11};
		int[] expected = new int [] {-839, -47, 11, 137, -11, 40, 21, -23, -18, 55, 16, -1, -16, 26, -3, 7, -2, 14, -14, 7, -8, -5, 6, -4, 8, 2, 4, -1, 0, 1, 1, -1, -1, -3, 6, -3, 7, -1, -1, 5, -1, 2, 0, 1, 3, -3, 5, -5, 1, -2, 7, -3, 1, 1, 3, 0, -4, 5, -3, 1, 6, 4, -1, 11};
		int[] output = ZigZag.zigzag(input);
		assertTrue("Your ZigZag Scan did not produce a correct result", compareArrays(output, expected));
	}
	
	public void testJFIFComment() {
		DiscreteCosinusTransformation dct = new DiscreteCosinusTransformation();
		try {
			JFIFContainer container = new JFIFContainer(dct, "Matthias Stein1bauer Testcase", 100, 100);
			byte[] data = container.getBytes();
			byte[] comment = new byte[33];
			byte[] expected = new byte[] {
					(byte) 0xff, (byte) 0xfe, 0x00, 0x1f, 0x4d, 0x61, 0x74, 0x74,
					0x68, 0x69, 0x61, 0x73, 0x20, 0x53, 0x74, 0x65,
					0x69, 0x6e, 0x31, 0x62, 0x61, 0x75, 0x65, 0x72,
					0x20, 0x54, 0x65, 0x73, 0x74, 0x63, 0x61, 0x73,
					0x65 };
			for(int i=0;i<comment.length;i++) {
				comment[i] = data[i+20];
			}
			assertEquals("Your code did not create a valid JFIF Comment", new PrintableByteArray(comment).toString(), new PrintableByteArray(expected).toString());
		} catch (IOException e) {
			fail("There was an ICException " + e.getMessage());
		}		
	}
	
	private boolean compareBlocks(double[][] result, double[][] reference) {
		for(int i=0;i<Constants.JPEG_BLOCK_SIZE;i++) {
			for(int j=0;j<Constants.JPEG_BLOCK_SIZE;j++) {
				int rs, rf;
				rs = (int) Math.round(result[i][j]);
				rf = (int) Math.round(reference[i][j]);
				if(!equalsWithError(rf, rs, 2)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean compareArrays(int [] result, int[] reference) {
		if(result.length != reference.length) return false;
		for(int i=0;i<result.length && i<reference.length; i++) {
			if(!equalsWithError(result[i], reference[i], 1)) {
				return false;
			}
		}
		return true;
	}
	
	private double printFactors(double[][] result, double[][] expected) {
		double[][] diff = new double[Constants.JPEG_BLOCK_SIZE][Constants.JPEG_BLOCK_SIZE];
		
		double avg = 0;
		
		for(int i=0;i<Constants.JPEG_BLOCK_SIZE;i++) {
			for(int j=0;j<Constants.JPEG_BLOCK_SIZE;j++) {
				if(expected[i][j] == 0) {
					diff[i][j] = 1;
				}else{
					diff[i][j] = result[i][j] / expected[i][j];
				}
				avg += diff[i][j];
			}
		}
		
		System.out.println(new PrintableBlock(diff));
		avg = avg / 64;
		
		return avg;
	}
	
	private boolean equalsWithError(int a, int b, int error) {
		return a < (b+error) && a > (b-error);
	}
}

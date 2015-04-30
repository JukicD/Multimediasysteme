package at.jku.tk.mms.jpeg.impl;

public class ZigZag {

	public static int[] zigzag(int[] input) {
		int[] result = new int[input.length];
/* 		@TODO Place your implementation here		 */
		
		for (int i = 0; i < Constants.JPEG_BLOCK_SIZE * Constants.JPEG_BLOCK_SIZE; i++) {
			result[i] = input[Constants.JPEG_NATURAL_ORDER[i]];
		}
		return result;
	}
}
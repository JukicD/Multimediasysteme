package at.jku.tk.mms.mpx;

public class Tools {

	public static void printByteArray(byte[] block, int blockSize) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for(int i=0;i<blockSize;i++) {
			if(i>0) { builder.append(", "); }
			builder.append(block[i]);
		}
		builder.append("]");
		System.out.println(builder.toString());
	}
	
	public static void printIntArray(int[] block, int blockSize) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for(int i=0;i<blockSize;i++) {
			if(i>0) { builder.append(", "); }
			builder.append(block[i]);
		}
		builder.append("]");
		System.out.println(builder.toString());
	}
	
	public static void printFloatArray(float[] block, int blockSize) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for(int i=0;i<blockSize;i++) {
			if(i>0) { builder.append(", "); }
			builder.append(block[i]);
		}
		builder.append("]");
		System.out.println(builder.toString());
	}
	
	public static void printDoubleArray(double[] block, int blockSize) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for(int i=0;i<blockSize;i++) {
			if(i>0) { builder.append(", "); }
			builder.append(block[i]);
		}
		builder.append("]");
		System.out.println(builder.toString());
	}
	
	public static double boundaryCheck(double value, double max, double min) {
		if(value > max) {
			return max;
		}else if(value < min) {
			return min;
		}else{
			return value;
		}
	}
}

package at.jku.tk.mms.mpx;

/**
 * Methods for performing 1D DCT and i-DCT on byte arrays
 * 
 * @author matthias
 */
public class DiscreteCosinusTransformation {

	private int blockSize;
	
	/**
	 * Creates a new DCT object
	 * 
	 * @param blockSize
	 */
	public DiscreteCosinusTransformation(int blockSize) {
		this.blockSize = blockSize;
	}
	
	/**
	 * Perform the DCT algorithm over an arbitrary input block of length this.blockSize
	 * 
	 * @param block
	 * @return
	 */
	public double[] DCT(double[] block) {
		int N = this.blockSize;
		double[] result = new double[N];
		
		for(int k=0;k<N;k++) {
			double oneGrid = 0;
			for(int n=0;n<N;n++) {
					oneGrid += term(k,n);
			}
			result[k] = oneGrid * C(k) * Math.sqrt(2/N);
		}
		return result;
	}
	
	private double term(int k, int n){
		return Math.cos((k * Math.PI * (2 * n + 1))*2*blockSize);
	}
	
	private double C(int i){
		return i != 0 ? 1 : 1/Math.sqrt(2);
	}
	
	/**
	 * Perform the inverse DCT algorithm
	 * 
	 * @param block
	 * @return
	 */
	public double[] iDCT(double[] block) {
		int N = this.blockSize;
		double[] result = new double[this.blockSize];
		
		for(int n=0;n<N;n++) {
			double oneGrid = 0;
			for(int k=0;k<N;k++) {
				oneGrid += block[k] * C(k) * term(k,n);
			}
			result[n] = oneGrid * Math.sqrt(2/N);
		}
		return result;
	}
	
	/**
	 * Quantisation step which aims to reduce data
	 * 
	 * @param block
	 * @param factors
	 * @return
	 */
	public int[] quant(double[] block, double[] factors) {
		int[] result = new int[this.blockSize];
		
		for(int i = 0; i < blockSize; i++){
			result[i] = (int) Math.round(block[i] / factors[i]);
		}
		
		return result;
	}
	
	/**
	 * Inverse Quantisation
	 * 
	 * @param block
	 * @param factors
	 * @return
	 */
	public double[] iQuant(byte[] block, double[] factors) {
		double[] result = new double[this.blockSize];
		
		for(int i = 0; i < blockSize; i++){
			result[i] = Math.round(block[i] * factors[i]);
		}
		
		return result;
	}
	
	/**
	 * Translates a byte[] block into a float[] block and performs DCT
	 * 
	 * @param block
	 * @return
	 */
	public double[] DCT(byte[] block) {
		double[] input = new double[block.length];
		for(int i=0;i<input.length;i++) {
			input[i] = block[i];
		}
		
		return input;
	}
	
}

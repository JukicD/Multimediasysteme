package at.jku.tk.mms.jpeg.impl;

public class DiscreteCosinusTransformation {
	
	private int[] quantumLuminance;
	private double[] divLuminance;
	
	private int[] quantumChrominance;
	private double[] divChrominance;
	
	private int blockCount = 0;

	public DiscreteCosinusTransformation() {
		this(Constants.JPEG_DEFAULT_QUALITY);
	}

	public DiscreteCosinusTransformation(int quality) {
	    if(quality <= 0) {
	    	quality = 1;
	    }
	    if(quality > 100) {
	    	quality = 100;
	    }
	    if(quality < 50) {
	    	quality = 5000 / quality;
	    }else{
	      quality = 200 - quality * 2;
	    }
		
		quantumLuminance = qualityAdaption(Constants.JPEG_QUANTUM_LUMINANCE, quality);
		quantumChrominance = qualityAdaption(Constants.JPEG_QUANTUM_CHROMINANCE, quality);
		divLuminance = setupDivisors(quantumLuminance);
		divChrominance = setupDivisors(quantumChrominance);
	}
	
	private int[] qualityAdaption(int[] input, int quality) {
		int[] result = new int[input.length];
		int tmp;
		for(int i=0;i<input.length;i++) {
			tmp = (input[i] * quality + 50) / 100;
			if(tmp <= 0) tmp = 1;
			if(tmp >= 255) tmp = 255;
			result[i] = tmp;
		}
		return result;
	}
	
	private double[] setupDivisors(int[] input) {
		double[] result = new double[Constants.JPEG_BLOCK_SIZE * Constants.JPEG_BLOCK_SIZE];
	    int index = 0;
	    for(int i=0;i<Constants.JPEG_BLOCK_SIZE;i++) {
	    	for(int j=0;j<Constants.JPEG_BLOCK_SIZE;j++) {
	    		result[index] = (1.0/(input[index] * Constants.JPEG_AAN_SCALE_FACTOR[i] * Constants.JPEG_AAN_SCALE_FACTOR[j] * 8.0));
	    		index++;
	    	}
	    }
	    return result;
	}
	
	public double[][] forwardDCT(float[][] inputBlock) {
		double[][] block = new double[Constants.JPEG_BLOCK_SIZE][Constants.JPEG_BLOCK_SIZE];
		double[][] dct = new double[Constants.JPEG_BLOCK_SIZE][Constants.JPEG_BLOCK_SIZE];
		
		for(int i=0;i<Constants.JPEG_BLOCK_SIZE;i++) {
			for(int j=0;j<Constants.JPEG_BLOCK_SIZE;j++) {
				// shift values from [0, 255] to [-128, 127]
				block[i][j] = inputBlock[i][j] - 128.0;
			}
		}
		
/* 		@TODO Place your implementation here		 */
		
		for(int u = 0; u < 8; u++){
			for(int v = 0; v < 8; v++){
				
				double oneGrid = 0;
				for(int x = 0;x < 8; x++){
					for(int y = 0; y < 8; y++){
						oneGrid += (double)2/Constants.JPEG_BLOCK_SIZE * this.C(u) * this.C(v) * block[x][y] * DiscreteCosinusTransformation.term(x, y, u, v);
					}
				}
				dct[u][v] = Math.round(oneGrid * 8);
			}
		}
		return dct;
	}
	
	static double term(int x, int y, int u, int v){
		return (Math.cos(((2 * x + 1) * u * Math.PI) / (2 * Constants.JPEG_BLOCK_SIZE)) * Math.cos(((2 * y + 1) * v * Math.PI) / (2 * Constants.JPEG_BLOCK_SIZE)));
	}
	

	public int[] quantize(double[][] block, double[] divisors) {
		int[] result = new int[Constants.JPEG_BLOCK_SIZE * Constants.JPEG_BLOCK_SIZE];
/* 		@TODO Place your implementation here		 */
		
		int counter = 0;
		for(int i = 0; i < Constants.JPEG_BLOCK_SIZE; i++){
			for(int j = 0; j < Constants.JPEG_BLOCK_SIZE; j++){
				result[counter] = (int) Math.round(block[i][j] * divisors[counter]);
				counter++;
			}
		}
	    return result;
	}
	
	public int[] quantizeLuminance(double[][] block) {
		return quantize(block, divLuminance);
	}
	
	public int[] quantizeChrominance(double[][] block) {
		return quantize(block, divChrominance);
	}
	
	public int[] dctQuantLuminance(float[][] block) {
		return quantizeLuminance(forwardDCT(block));
	}
	
	public int[] dctQuantChrominance(float[][] block) {
		return quantizeChrominance(forwardDCT(block));
	}
	
	public int[] getQuantumLuminance() {
		return quantumLuminance;
	}
	
	public int[] getQuantumChrominance() {
		return quantumChrominance;
	}
	
	private double C(int x){
		return x == 0 ? 1/Math.sqrt(2) : 1;
	}
}

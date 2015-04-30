package at.jku.tk.mms.jpeg.impl;

/**
 * This class is capable of down sampling an YCC image
 * 
 * In this class we use a static down sampling
 * 
 * @author matthias
 *
 */
public class SubsampledYCCImage {
	
	private int width;
	
	private int height;
	
	private int blocksWide;
	
	private int blocksHigh;
	
	private float Y[][];
	
	private float Cb[][];
	
	private float Cr[][];

	public SubsampledYCCImage(YCCImage ycc) {
		this.width = ycc.getWidth();
		this.height = ycc.getHeight();
		this.blocksWide = width / Constants.JPEG_BLOCK_SIZE;
		this.blocksHigh = height / Constants.JPEG_BLOCK_SIZE;
		this.Y = ycc.getY();
		Cb = new float[this.width][this.height];
		Cr = new float[this.width][this.height];
		subsample(ycc);
	}

	private void subsample(YCCImage ycc) {
		for(int x=0;x<width;x++) {
			for( int y=0;y<height;y++) {
				Cb[x][y] = ycc.getCb()[x-(x%2)][y-(y%2)];
				Cr[x][y] = ycc.getCr()[x-(x%2)][y-(y%2)];
			}
		}
	}
	
	private float[][] copyBlock(float[][] input, int x, int y) {
		float[][] result = new float[Constants.JPEG_BLOCK_SIZE][Constants.JPEG_BLOCK_SIZE];
		int xStart, xEnd, xC = 0;
		int yStart, yEnd, yC = 0;
		xStart = x * Constants.JPEG_BLOCK_SIZE;
		xEnd = xStart + Constants.JPEG_BLOCK_SIZE;
		yStart = y * Constants.JPEG_BLOCK_SIZE;
		yEnd = yStart + Constants.JPEG_BLOCK_SIZE;
		for(int i=xStart;i<xEnd;i++) {
			for(int j=yStart;j<yEnd;j++) {
				result[xC][yC] = input[i][j];
				xC++;
			}
			xC = 0;
			yC++;
		}
		return result;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public int getBlocksHigh() {
		return blocksHigh;
	}
	
	public int getBlocksWide() {
		return blocksWide;
	}

	public float[][] getY() {
		return Y;
	}

	public float[][] getCb() {
		return Cb;
	}

	public float[][] getCr() {
		return Cr;
	}

	public float[][] getYBlock(int x, int y) {
		return copyBlock(Y, x, y);
	}
	
	public float[][] getCbBlock(int x, int y) {
		return copyBlock(Cb, x, y);
	}
	
	public float[][] getCrBlock(int x, int y) {
		return copyBlock(Cr, x, y);
	}
	
}

package at.jku.tk.mms.jpeg.impl;

/**
 * This class is able to convert an {@link RGBImage} to an {@link YCCImage}
 * 
 * It is basically a very simple color space conversion
 * 
 * @author matthias
 *
 */
public class YCCImage {

	private int width;
	
	private int height;
	
	private float[][] Y;
	
	private float[][] Cb;
	
	private float[][] Cr;
	
	public YCCImage(RGBImage rgb) {
		this.width = rgb.getWidth();
		this.height = rgb.getHeight();
		this.Y = new float[width][height];
		this.Cb = new float[width][height];
		this.Cr = new float[width][height];
		colorSpaceConversion(rgb);
	}
	
	private void colorSpaceConversion(RGBImage rgb) {
		int[][] red = rgb.getRed();
		int[][] green = rgb.getGreen();
		int[][] blue = rgb.getBlue();
		int r, g, b;
		
		for(int x=0;x<width;x++) {
			for(int y=0;y<height;y++) {
				r = red[x][y];
				g = green[x][y];
				b = blue[x][y];
		        Y[x][y] = (float)((0.299 * r + 0.587 * g + 0.114 * b));
		        Cb[x][y] = 128 + (float)((-0.16874 * r - 0.33126 * g + 0.5 * b));
		        Cr[x][y] = 128 + (float)((0.5 * r - 0.41869 * g - 0.08131 * b));
			}
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
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
}

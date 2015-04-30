package at.jku.tk.mms.jpeg.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * This class is just a wrapper around Image
 * 
 * The constructor is loading the specified file with AWT default image loading methods
 * 
 * Some things in JPEG just work a little bit easier if we use mod 16 image sizes. This is why we just crop the image here
 * 
 * @author matthias
 */
public class RGBImage {
	
	private int width, height;
	
	private int[][] red;
	
	private int[][] green;
	
	private int[][] blue;
	
	public RGBImage(String filename) throws IOException {
		File f = new File(filename);
		BufferedImage img = ImageIO.read(f);
		this.width = img.getWidth() - (img.getWidth() % 8);
		this.height = img.getHeight() - (img.getWidth() % 8);
		if(this.width < 16 || this.height < 16) {
			throw new IllegalStateException("This tool only operates on images larger than 16x16");
		}
		this.red = new int[width][height];
		this.green = new int[width][height];
		this.blue = new int[width][height];
		loadRGBArrays(img);
	}
	
	private void loadRGBArrays(BufferedImage img) {
		int curpixel, r, g, b;
		for(int x=0;x<width;x++) {
			for(int y=0;y<height;y++) {
				curpixel = img.getRGB(x, y);
		        r = (curpixel >> 16) & 0xff;
		        g = (curpixel >> 8) & 0xff;
		        b = (curpixel) & 0xff;
		        red[x][y] = r;
		        green[x][y] = g;
		        blue[x][y] = b;
			}
		}
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int[][] getRed() {
		return red;
	}
	
	public int[][] getGreen() {
		return green;
	}
	
	public int[][] getBlue() {
		return blue;
	}

}

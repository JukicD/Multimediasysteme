package at.jku.tk.mms.img.filters;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.util.Properties;

import at.jku.tk.mms.img.FilterInterface;
import at.jku.tk.mms.img.pixels.Pixel;

/** Filter that implements image thresholding */
public class Threshold implements FilterInterface {

	@Override
	public Image runFilter(BufferedImage image, Properties settings) {
		int threshold = Integer.parseInt(settings.getProperty("threshold"));
/* 		@TODO Place your implementation here		 */
		
		int whitePixel = Pixel.generateRGBAPixel(255, 255, 255, 255);
		int blackPixel = Pixel.generateRGBAPixel(0, 0, 0, 255);
		
		for(int x = 0; x < image.getWidth(); x++){
			for(int y = 0; y < image.getHeight(); y++){
				
				int red = Pixel.getRed(image.getRGB(x, y));
				int green = Pixel.getGreen(image.getRGB(x,y));
				int blue = Pixel.getBlue(image.getRGB(x, y));
				
				image.setRGB(x, y, (red + green + blue)/3 > threshold ? whitePixel : blackPixel);
			}
		}
		return image;
	}

	@Override
	public String[] mandatoryProperties() {
		return new String[] { "threshold:n:0-255:128" };
	}

	@Override
	public String toString() {
		return "threshold";
	}
}
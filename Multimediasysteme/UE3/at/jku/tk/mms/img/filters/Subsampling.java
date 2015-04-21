package at.jku.tk.mms.img.filters;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;

import at.jku.tk.mms.img.FilterInterface;
import at.jku.tk.mms.img.pixels.Pixel;

/** Perform sub sampling on the image */
public class Subsampling implements FilterInterface {

	@Override
	public Image runFilter(BufferedImage image, Properties settings) {
		int rate = Integer.parseInt(settings.getProperty("rate"));
		
		BufferedImage result = new BufferedImage(image.getWidth()/2, image.getHeight()/2,BufferedImage.TYPE_INT_ARGB);getClass();
		
/* 		@TODO Place your implementation here		 */
		
		if(rate > 1){ //i == 1 -> return the image without processing
			for(int x = 0; x < image.getWidth(); x++){
				for(int y = 0; y < image.getHeight(); y++){
					if(x%rate == 0 && y%rate == 0){
					
						int red = Pixel.getRed(image.getRGB(x, y));
						int green = Pixel.getGreen(image.getRGB(x, y));
						int blue = Pixel.getBlue(image.getRGB(x, y));
				
						result.setRGB(x/rate, y/rate, Pixel.generateRGBAPixel(red, green, blue, 255));
					}
				}
			}	
		}
		return rate == 1 ? image : result;
	}

	@Override
	public String[] mandatoryProperties() {
		return new String [] { "rate:n:1-8:2" };
	}
	
	@Override
	public String toString() {
		return "subsampling";
	}
}
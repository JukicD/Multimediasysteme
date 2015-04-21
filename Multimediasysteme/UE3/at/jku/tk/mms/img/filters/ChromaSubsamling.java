package at.jku.tk.mms.img.filters;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;

import at.jku.tk.mms.img.FilterInterface;
import at.jku.tk.mms.img.pixels.Pixel;

/** Apply sub sampling of color values only */
public class ChromaSubsamling implements FilterInterface {

	@Override
	public Image runFilter(BufferedImage image, Properties settings) {
		int horizontal = Integer.parseInt(settings.getProperty("horizontal"));
		int vertical = Integer.parseInt(settings.getProperty("vertical"));
		
		BufferedImage subsampled = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
/* 		@TODO Place your implementation here		 */
		
		// According to the lecture-sheet it is implied that every 'horizontal' and every -
		// 'vertical pixel the color is set to black and white.
	
		for(int x = 0; x < image.getWidth(); x++){
			for(int y = 0; y < image.getHeight(); y++){
				
				Pixel p = new Pixel(image.getRGB(x, y));
				
				if(x%horizontal == 0){
					for(int i = 1; i<horizontal; i++){
						Pixel q = new Pixel(image.getRGB(x+i, y));
						p.setCb(q.getCb());
						p.setCr(q.getCr());
					}
				}
				
				if(y%vertical == 0){
					for(int j = 1; j<horizontal; j++){
						Pixel q = new Pixel(image.getRGB(x, y+j));
						p.setCb(q.getCb());
						p.setCr(q.getCr());
					}
				}
				subsampled.setRGB(x, y, p.getRawRGBA());
			}
		}
		return subsampled;
	}

	@Override
	public String[] mandatoryProperties() {
		return new String [] { "horizontal:s:1-8:2", "vertical:n:1-8:2" };
	}
	
	@Override
	public String toString() {
		return "subsampling - chroma";
	}

}

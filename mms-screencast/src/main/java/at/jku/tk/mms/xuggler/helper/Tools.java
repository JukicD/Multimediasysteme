package at.jku.tk.mms.xuggler.helper;

import java.awt.image.BufferedImage;

public class Tools {
	
	public static  BufferedImage convertToType(BufferedImage sourceImage, int targetType) {
		BufferedImage image;
		if (sourceImage.getType() == targetType) {
			image = sourceImage;
		} else {
			image = new BufferedImage(sourceImage.getWidth(),
					sourceImage.getHeight(), targetType);
			image.getGraphics().drawImage(sourceImage, 0, 0, null);
		}
		return image;
	}
	
	public static BufferedImage prepareForEncoding(BufferedImage sourceImage) {
		return Tools.convertToType(sourceImage, BufferedImage.TYPE_3BYTE_BGR);
	}
}

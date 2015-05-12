package at.jku.tk.mms.xuggler.impl;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

/**
 * Is capable of getting a screenshot of the display
 * 
 * @author matthiassteinbauer
 */
public class ScreenshotUtility {

	private Robot robot;
	private Toolkit toolkit;
	private Rectangle screenBounds;
	
	/**
	 * Creates a screenshot utility and initializes with screen size
	 * @throws AWTException 
	 */
	public ScreenshotUtility() throws AWTException {
		robot = new Robot();
		toolkit = Toolkit.getDefaultToolkit();
		screenBounds = new Rectangle(toolkit.getScreenSize());
	}
	
	/**
	 * Get Size of screen and also of resulting screenshots
	 * @return
	 */
	public Rectangle getSize() {
		return screenBounds;
	}
	
	/**
	 * Captures a screenshot
	 * @return
	 */
	public BufferedImage getScreenShot() {
		return robot.createScreenCapture(screenBounds);
	}
	
}

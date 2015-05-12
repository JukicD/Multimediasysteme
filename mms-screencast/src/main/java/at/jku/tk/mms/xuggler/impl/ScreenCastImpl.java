package at.jku.tk.mms.xuggler.impl;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import at.jku.tk.mms.xuggler.helper.Tools;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.ICodec.ID;

/**
 * Is capable of recording a Screencast for a defined amount of time
 * 
 * @author matthiassteinbauer
 */
public class ScreenCastImpl {

	private ScreenshotUtility screenshot;
	private final String outputFile;
	private final long seconds;

	/**
	 * Creates a screen cast impl which is capable of recording the screen for a
	 * defined amount of time.
	 * 
	 * File format should be H.264 in an .mp4 file.
	 * 
	 * @param outputFile
	 * @param seconds
	 * @throws AWTException
	 */
	public ScreenCastImpl(String outputFile, long seconds) throws AWTException {
		this.outputFile = outputFile;
		this.seconds = seconds;
		if (!outputFile.toLowerCase().endsWith(".mp4")) {
			throw new IllegalArgumentException(
					"Only .mp4 files can get created by this utility");
		}
		screenshot = new ScreenshotUtility();
	}

	/**
	 * Starts the recording and automatically stops after the recording is done
	 * 
	 * @throws InterruptedException
	 */
	public void startRecording() throws InterruptedException {
		
		long end = System.nanoTime() + seconds * (1000 * 1000 * 1000);
		long begin = System.nanoTime();
		
		IMediaWriter mw = ToolFactory.makeWriter(outputFile);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Rectangle rect = new Rectangle(tk.getScreenSize());
	
		mw.addVideoStream(0, 0, ID.CODEC_ID_H264, rect.width,rect.height);

		while (System.nanoTime() < end) {
			BufferedImage bi = screenshot.getScreenShot();
			bi = Tools.convertToType(bi, BufferedImage.TYPE_3BYTE_BGR);
			mw.encodeVideo(0, bi, System.nanoTime() - begin,TimeUnit.NANOSECONDS);
			Thread.sleep(40);
		}
		mw.close();
	}
}
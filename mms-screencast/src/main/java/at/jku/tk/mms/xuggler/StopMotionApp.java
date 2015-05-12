package at.jku.tk.mms.xuggler;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import at.jku.tk.mms.xuggler.helper.Tools;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.ICodec.ID;

/**
 * Class capable of creating stop motion animations
 * 
 */
public class StopMotionApp {
	
	private final File directory, target;
	
	/**
	 * Initialize class
	 * At this point the input params are checked
	 * 
	 * @param directory
	 * @param target
	 */
	public StopMotionApp(File directory, File target) {
		this.directory = directory;
		this.target = target;
	}
	

	private void createAnimation(int delayBetweenImages) throws IOException {
/* 		@TODO Place your implementation here		 */
		
		IMediaWriter mw = ToolFactory.makeWriter(target.toString());
		Toolkit tk = Toolkit.getDefaultToolkit();
		Rectangle rect = new Rectangle(tk.getScreenSize());
		long start = System.nanoTime();

		mw.addVideoStream(0, 0, ID.CODEC_ID_H264, rect.width,rect.height);

		File[] dir = directory.listFiles();
		Arrays.sort(dir);
		
		for (File file : dir) {
			BufferedImage s = ImageIO.read(file);
			s = Tools.convertToType(s, BufferedImage.TYPE_3BYTE_BGR);
			mw.encodeVideo(0, s, System.nanoTime() - start,TimeUnit.NANOSECONDS);
			try {
				Thread.sleep(delayBetweenImages);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
		mw.close();
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		if(args.length != 2) {
			throw new IllegalArgumentException("You need to pass a directory of images and a target movie file");
		}
		File directory = new File(args[0]);
		if(directory.exists() && directory.isDirectory()) {
			File target = new File(args[1]);
			StopMotionApp app = new StopMotionApp(directory, target);
			app.createAnimation(100);
		}else{
			throw new IllegalArgumentException("First argument '" + args[0] + "' did not denote an existing directory");
		}
	}

}

package at.jku.tk.mms.xuggler;

import java.io.File;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec.ID;

public class VideoTranscoderApp {
	
	private File source;
	
	private File target;
	
	public VideoTranscoderApp(File source, File target) {
		this.source = source;
		this.target = target;
	}
	
	public void transcode() {
		
		IMediaReader mr = null;
		IMediaWriter mw = null;
		
		do{
			mr = ToolFactory.makeReader(source.toString());
			mw = ToolFactory.makeWriter(target.toString(), mr);
			mw.getContainer().setForcedVideoCodec(ID.CODEC_ID_H264);
			mr.addListener(mw);
		
		}while (mr.readPacket() != null);
	}

	public static void main(String[] args) {
		File source = new File(args[0]);
		if(source.canRead()) {
			File target = new File(args[1]);
			VideoTranscoderApp transcoder = new VideoTranscoderApp(source, target);
			transcoder.transcode();
		}
	}

}

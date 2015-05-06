package at.jku.tk.mms.mpx.wave;

import java.io.IOException;
import java.io.OutputStream;


/**
 * Can write a specified byte[] to a file
 * 
 * @author matthias
 */
public class CustomWaveFileWriter {

	/**
	 * Writes out an 8-bit mono WAV stream
	 * 
	 * @param data
	 * @param out
	 * @throws IOException
	 */
	public static void write(byte[] data, OutputStream out) throws IOException {
		IOTools.writeString(out, "RIFF");
		IOTools.writeInt(out, 36 + data.length);
		IOTools.writeString(out, "WAVE");
		
		short format = 1;
		short numChannels = 1;
		int sampleRate = 11025;
		short bitsPerSample = 8;

		/* fmt chunk */
		IOTools.writeString(out, "fmt ");
		IOTools.writeInt(out, 16);
		IOTools.writeShort(out, format); // pcm
		IOTools.writeShort(out, numChannels);
		IOTools.writeInt(out, sampleRate);
		IOTools.writeInt(out, numChannels * sampleRate * bitsPerSample / 8);
		IOTools.writeShort(out, (short) (numChannels * bitsPerSample / 8));
		IOTools.writeShort(out, bitsPerSample);

		/* data chunk */
		IOTools.writeString(out, "data");
		IOTools.writeInt(out, data.length);
		
		out.write(data);
		out.flush();
	}

}

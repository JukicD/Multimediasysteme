package at.jku.tk.mms.mpx.wave;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Helper component that generally allows to read any audio data supported by Java but
 * restricts loading to an 8 bit mono wave PCM_UNSIGNED file
 * to be loaded to a byte[]
 * 
 * @author matthias
 */
public class AudioFileReader {
	
	private BufferedInputStream in;
	
	private byte[] samples;
	
	private int totalFramesRead;
	
	private int blockSize;

	/**
	 * Creates a new reader
	 * 
	 * @param in
	 */
	public AudioFileReader(InputStream in, int blockSize) {
		this.in = new BufferedInputStream(in);
		this.samples = null;
		this.totalFramesRead = 0;
		this.blockSize = blockSize;
	}
	
	/**
	 * Reads the samples from audio stream
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 * @throws AudioFormatException 
	 */
	private void readSamples() throws UnsupportedAudioFileException, IOException, AudioFormatException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.in);
		AudioFormat audioFormat = audioInputStream.getFormat();
		if(audioFormat.getChannels() != 1) {
			throw new AudioFormatException("TkWave only supports WAVE files with 1 audio channel as input");
		}
		if(audioFormat.getEncoding() != Encoding.PCM_UNSIGNED) {
			throw new AudioFormatException("TkWave only works on PCM UNSIGNED data");
		}
		if(audioFormat.getSampleSizeInBits() != 8) {
			throw new AudioFormatException("TkWave only works with 8 Bit per sample as input");
		}
		int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
	    if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
	    	bytesPerFrame = 1;
	    } 
		// Set an arbitrary buffer size of 1024 frames.
		int numBytes = 1024 * bytesPerFrame;
		byte[] audioBytes = new byte[numBytes];
	    int numBytesRead = 0, numFramesRead = 0;
	    while ((numBytesRead = audioInputStream.read(audioBytes)) != -1) {
	    	numFramesRead = numBytesRead / bytesPerFrame;
	    	totalFramesRead += numFramesRead;
	    	bout.write(audioBytes, 0, numBytesRead);
	    }
	    
	    int numPaddingBytes = numBytesRead % this.blockSize;
	    for(int i=0;i<numPaddingBytes;i++) {
	    	bout.write((byte) 0);
	    }
	    
	    this.samples = bout.toByteArray();
	}
	
	/**
	 * Return the read samples
	 * 
	 * @return
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 * @throws AudioFormatException 
	 */
	public byte[] getSamples() throws UnsupportedAudioFileException, IOException, AudioFormatException {
		if(this.samples == null) {
			readSamples();
		}
		return samples;
	}

	/**
	 * Allows us to retrieve the total frames read
	 * 
	 * @return
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 * @throws AudioFormatException 
	 */
	public int getTotalFramesRead() throws UnsupportedAudioFileException, IOException, AudioFormatException {
		getSamples();
		return totalFramesRead;
	}
	
	/**
	 * Retrieves the total byte size of the sample
	 * 
	 * @return
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws AudioFormatException 
	 */
	public int getSizeBytes() throws UnsupportedAudioFileException, IOException, AudioFormatException {
		return getSamples().length;
	}
	
}

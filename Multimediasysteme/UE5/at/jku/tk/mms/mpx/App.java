package at.jku.tk.mms.mpx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;

import javax.sound.sampled.UnsupportedAudioFileException;

import at.jku.tk.mms.mpx.tkwave.TkWaveFile;
import at.jku.tk.mms.mpx.tools.Smoothing;
import at.jku.tk.mms.mpx.ui.TkWaveUi;
import at.jku.tk.mms.mpx.wave.AudioFileReader;
import at.jku.tk.mms.mpx.wave.AudioFormatException;
import at.jku.tk.mms.mpx.wave.CustomWaveFileWriter;

/**
 * MPX Encoder utility 
 * 
 * @author matthias
 */
public class App {

	public static final String SOURCE_FILE = "/questions.wav";
	
	public static final int BLOCK_SIZE = 8;
	
	/** Min Factor is 4.0 anything smaller could cut out data */
	public static final double[] FACTORS_IDENTITY = new double [] {
		4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0
	};
	
	public static final double[] FACTORS_10 = new double [] {
		10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0
	};
	
	public static final double[] FACTORS_25 = new double [] {
		25.0, 25.0, 25.0, 25.0, 25.0, 25.0, 25.0, 25.0
	};
	
	public static final double[] FACTORS_OPTIMISED = new double [] {
		11.0, 17.0, 22.0, 32.0, 41.0, 51.0, 64.0, 89.0
	};
	
	public static final double[] FACTORS_LOW_HIGH = new double [] {
		100.0, 75.0, 50.0, 10.0, 10.0, 50.0, 75.0, 100.0
	};

	public static void main(String args[]) throws UnsupportedAudioFileException, IOException, ClassNotFoundException, AudioFormatException {
		if(args.length == 0) {
			// Run application with UI
			TkWaveUi mainFrame = new TkWaveUi();
			mainFrame.pack();
			mainFrame.setVisible(true);
		}else{
			// Run as Encoder / Decoder
			if(args.length == 3) {
				// flag
				String flag = args[0].trim().toLowerCase();
				if("-encode".equals(flag)) {
					// encode a wave file to TkWave
					File inputFile = new File(args[1]);
					if(inputFile.exists()) {
						File outputFile = new File(args[2]);
						String info = encode(inputFile, outputFile, App.FACTORS_LOW_HIGH);
						System.out.println("Encoded successfully");
						System.out.println(info);
					}else{
						System.err.println("Input file " + inputFile.getAbsolutePath() + " does not exist.");
					}
				}else if("-decode".equals(flag)){
					// encode a TkWave file to wave
					File inputFile = new File(args[1]);
					if(inputFile.exists()) {
						File outputFile = new File(args[2]);
						decode(inputFile, outputFile);
					}
				}else{
					System.err.println("Invlaid flag parameters; use one of -encode|-decode");
				}
			}else{
				System.err.println("Invalid parameters; usage: at.jku.tk.mms.mpx.App <-encode|-decode> <input-file> <output-file>");
				System.err.println("Alternatively you can run this application without parameters to get a UI");
			}
		}
	}
	
	/**
	 * Encodes a WAVE file to TkWave
	 * 
	 * @param inputFile
	 * @param outputFile
	 * @throws FileNotFoundException
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws AudioFormatException
	 */
	public static String encode(File inputFile, File outputFile, double[] factors) throws FileNotFoundException, UnsupportedAudioFileException, IOException, AudioFormatException {
		return encode(new FileInputStream(inputFile), outputFile, factors);
	}

	/**
	 * Encodes a WAVE file to TkWave
	 * 
	 * @param inputFile
	 * @param outputFile
	 * @throws FileNotFoundException
	 * @throws UnsupportedAudioFileException
	 * @throws IOExceptio
	 * @throws AudioFormatException
	 */
	public static String encode(InputStream in, File outputFile, double[] factors) throws FileNotFoundException, UnsupportedAudioFileException, IOException, AudioFormatException {
		AudioFileReader audioFileReader = new AudioFileReader(in, App.BLOCK_SIZE);
		byte[] waveSamples = audioFileReader.getSamples();
		int originalLength = waveSamples.length;
		Encoder encoder = new Encoder(App.BLOCK_SIZE, factors);
		byte[] encodedSamples = encoder.encode(waveSamples);
		TkWaveFile tkWaveFile = new TkWaveFile(encoder.getCompressor().getHuffmanTree(), encodedSamples, factors);
		OutputStream out = new FileOutputStream(outputFile);
		int encodedLength = tkWaveFile.write(out);
		out.close();
		
		double ratio = Math.round((double) encodedLength / (double) originalLength * 100.0) / 100.0;
		
		StringBuilder sb = new StringBuilder();
		sb.append("original: " + originalLength + " bytes, compressed: " + encodedLength + " bytes, ratio: " + NumberFormat.getPercentInstance().format(ratio));
		return sb.toString();
	}
	
	/**
	 * Decodes a TkWave file to WAVE
	 * 
	 * @param inputFile
	 * @param outputFile
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void decode(File inputFile, File outputFile) throws ClassNotFoundException, IOException {
		InputStream in = new FileInputStream(inputFile);
		TkWaveFile tkWaveFile = TkWaveFile.read(in);
		Encoder encoder = new Encoder(App.BLOCK_SIZE, tkWaveFile.getFactors());
		byte[] decodedSamples = encoder.decode(tkWaveFile.getEncodedData(), tkWaveFile.getHuffman());
		Smoothing.smooth(decodedSamples, (byte) 1);
		CustomWaveFileWriter.write(decodedSamples, new FileOutputStream(outputFile));
	}
	
}

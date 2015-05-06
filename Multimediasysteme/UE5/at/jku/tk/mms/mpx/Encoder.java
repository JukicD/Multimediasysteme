package at.jku.tk.mms.mpx;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import at.jku.tk.mms.huffman.HuffmanCompression;

/**
 * Audio Encoder that uses {@link DiscreteCosinusTransformation} and Huffman Encoding
 * 
 * @author matthias
 */
public class Encoder {

	private int blockSize;
	
	private double[] factors;
	
	private DiscreteCosinusTransformation dct;
	
	private HuffmanCompression compressor;
	
	/**
	 * Initializes an Encoder with a certain blockSize
	 * 
	 * @param blockSize
	 */
	public Encoder(int blockSize, double[] factors) {
		this.blockSize = blockSize;
		this.factors = factors;
		this.dct = new DiscreteCosinusTransformation(this.blockSize);
	}
	
	/**
	 * Encodes an audio file in samples of size blockSize
	 * 
	 * @param sample
	 * @return
	 * @throws IOException 
	 */
	public byte[] encode(byte[] data) throws IOException {
		ByteArrayOutputStream preHuffman = new ByteArrayOutputStream();
		byte[] sample = new byte[this.blockSize];
		byte[] resByte = new byte[this.blockSize];
		int numSamples = data.length / this.blockSize;
		
		double min = Double.MAX_VALUE, max = Double.MIN_NORMAL;
		for(int i=0;i<numSamples;i++) {
			copySample(data, i, sample);
			double[] resDct = dct.DCT(sample);
			for(int q=0;q<resDct.length;q++) {
				double cur = resDct[q];
				if(cur > max) {
					max = cur;
				}
				if(cur < min) {
					min = cur;
				}
			}
			int[] resQuant = dct.quant(resDct, this.factors);
			for(int j=0;j<this.blockSize;j++) {
				resByte[j] = (byte) resQuant[j];
			}
			preHuffman.write(resByte);
		}
		byte[] huffOrig = preHuffman.toByteArray();
		this.compressor = new HuffmanCompression(huffOrig);
		byte[] resHuffman = this.compressor.compress(huffOrig);
		return resHuffman;
	}
	
	public byte[] decode(byte[] data, HuffmanCompression compressor) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		if(compressor == null) {
			compressor = this.compressor;
		}
		byte[] decompressed = compressor.decompress(data);
		byte[] sample = new byte[this.blockSize];
		int numSamples = decompressed.length / this.blockSize;
		
		for(int i=0;i<numSamples;i++) {
			copySample(decompressed, i, sample);
			double[] reverseQuant = this.dct.iQuant(sample, this.factors);
			double[] inverseDct = this.dct.iDCT(reverseQuant);
			
			byte[] bb = new byte[this.blockSize];
			for(int x=0;x<this.blockSize;x++) {
				bb[x] = (byte) inverseDct[x];
			}
			bout.write(bb);
		}
		return bout.toByteArray();
	}
	
	public HuffmanCompression getCompressor() {
		return this.compressor;
	}
	
	private void copySample(byte[] data, int sampleNumber, byte[] sample) {
		int offset = sampleNumber * this.blockSize;
		for(int i=0;i<this.blockSize;i++) {
			sample[i] = data[offset + i];
		}
	}
	
}

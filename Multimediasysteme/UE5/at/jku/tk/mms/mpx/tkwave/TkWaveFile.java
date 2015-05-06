package at.jku.tk.mms.mpx.tkwave;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.jku.tk.mms.mpx.App;

/**
 * Class capable of reading and writing TK-Wave files 
 * 
 * @author matthias
 */
public class TkWaveFile {
	
	private HuffmanTree tree;
	
	private byte[] encodedData;
	
	private double[] factors;

	public TkWaveFile(HuffmanTree tree, byte[] encodedData, double[] factors) {
		this.tree = tree;
		this.encodedData = encodedData;
		this.factors = factors;
	}
	
	/**
	 * Write the file to an {@link OutputStream}
	 * 
	 * @param out
	 * @throws IOException 
	 */
	public int write(OutputStream out) throws IOException {
		int length = 0;
		byte[] bf = new byte[App.BLOCK_SIZE];
		for(int i=0;i<App.BLOCK_SIZE;i++) {
			bf[i] = (byte) this.factors[i];
		}
		length += bf.length;
		out.write(bf);
		length += this.tree.write(out);
		IOTools.writeInt(out, this.encodedData.length);
		length += 4;
		out.write(this.encodedData);
		length += this.encodedData.length;
		out.flush();
		return length;
	}
	
	/**
	 * Constructs a {@link TkWaveFile} from any {@link InputStream}
	 * 
	 * @param in
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static TkWaveFile read(InputStream in) throws IOException, ClassNotFoundException {
		double[] factors = new double[App.BLOCK_SIZE];
		byte[] bf = new byte[App.BLOCK_SIZE];
		in.read(bf);
		for(int i=0;i<App.BLOCK_SIZE;i++) {
			factors[i] = bf[i];
		}
		HuffmanTree tree = HuffmanTree.read(in);
		int dataLength = IOTools.readInt(in);
		byte[] encodedData = new byte[dataLength];
		in.read(encodedData, 0, dataLength);
		return new TkWaveFile(tree, encodedData, factors);
	} 

	/**
	 * Retrieves the {@link HuffmanTree}
	 * 
	 * @return
	 */
	public HuffmanTree getTree() {
		return this.tree;
	}
	
	/**
	 * Retrieves a {@link HuffmanCompression}
	 * 
	 * @return
	 */
	public HuffmanCompression getHuffman() {
		return new HuffmanCompression(getTree());
	}

	/**
	 * Access to the encoded data
	 * 
	 * @return
	 */
	public byte[] getEncodedData() {
		return this.encodedData;
	}
	
	/**
	 * Get the factors used for encoding
	 * 
	 * @return
	 */
	public double[] getFactors() {
		return this.factors;
	}
	
}

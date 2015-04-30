package at.jku.tk.mms.jpeg.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class JFIFContainer {

	private ByteArrayOutputStream out;
	
	private boolean closed;
	
	public JFIFContainer(DiscreteCosinusTransformation dct, String comment, int width, int height) throws IOException {
		out = new ByteArrayOutputStream();
		closed = false;
		
		writeStartOfImageMarker();
		writeJfifHeader();
		writeCommentHeader(comment);
		writeQuantTable(dct);
		writeStartOfFrameHeader(width, height);
		writeDefineHuffmanTreeHeader();
		writeScanHeader();
	}

	private void writeStartOfImageMarker() throws IOException {
		checkWriteState();
		write(Constants.JFIF_START_OF_IMAGE_MARKER);
	}
	
	private void writeJfifHeader() throws IOException {
		checkWriteState();
		write(Constants.JFIF_HEADER);
	}
	
	private void writeCommentHeader(String comment) throws IOException {
		checkWriteState();
/* 		@TODO Place your implementation here		 */
	}
	
	private void writeQuantTable(DiscreteCosinusTransformation dct) throws IOException {
		checkWriteState();
		write(Constants.JFIF_START_OF_QUANT);
		byte[] 		data = new byte[dct.getQuantumLuminance().length];
		// Luminance
		write((byte) (0 << 4) + 0);
		for(int i=0;i<dct.getQuantumLuminance().length;i++) {
			data[i] = (byte) dct.getQuantumLuminance()[Constants.JPEG_NATURAL_ORDER[i]];
		}
		write(data);
		// Chrominance
		write((byte) (0 << 4) + 1);
		for(int i=0;i<dct.getQuantumChrominance().length;i++) {
			data[i] = (byte) dct.getQuantumChrominance()[Constants.JPEG_NATURAL_ORDER[i]];
		}
		write(data);
	}
	
	private void writeStartOfFrameHeader(int width, int height) throws IOException {
		checkWriteState();
		write(Constants.JFIF_START_OF_FRAME);
		byte[] sof = new byte[6];
		sof[0] = Constants.JPEG_PRECISION;
		sof[1] = (byte) ((height >> 8) & 0xFF);
		sof[2] = (byte) (height & 0xFF);
		sof[3] = (byte) ((width >> 8) & 0xFF);
		sof[4] = (byte) (width & 0xFF);
		sof[5] = Constants.JPEG_COMPONENTS;
		write(sof);
		
		byte[] comp = new byte[3];
		// Sampling factor is the same in all three channels (for our example)
		comp[1] = (Constants.JFIF_SAMPLING_FACTOR << 4) + Constants.JFIF_SAMPLING_FACTOR;
		// Y
		comp[0] = Constants.JFIF_COMPONENT_ID_Y;
 		comp[2] = Constants.JFIF_QTABLE_LUMINANCE;
 		write(comp);
 		// Cb
 		comp[0] = Constants.JFIF_COMPONENT_ID_Cb;
 		comp[2] = Constants.JFIF_QTABLE_CHROMINANCE;
 		write(comp);
 		// Cr
 		comp[0] = Constants.JFIF_COMPONENT_ID_Cr;
 		comp[2] = Constants.JFIF_QTABLE_CHROMINANCE;
 		write(comp);
	}
	
	private void writeDefineHuffmanTreeHeader() throws IOException {
		checkWriteState();
		write(Constants.JFIF_DEFINE_HUFFMAN_TREE);
		int len = Constants.JPEG_BITS_DC_LUMINANCE.length + Constants.JPEG_VAL_DC_LUMINANCE.length
				+ Constants.JPEG_BITS_AC_LUMINANCE.length  + Constants.JPEG_VAL_AC_LUMINANCE.length
				+ Constants.JPEG_BITS_DC_CHROMINANCE.length + Constants.JPEG_VAL_DC_CHROMINANCE.length
				+ Constants.JPEG_BITS_AC_CHROMINANCE.length + Constants.JPEG_VAL_AC_CHROMINANCE.length;
		len += 2; // length of the length field// zai0oo itself
		byte[] lenBuffer = new byte[2];
		lenBuffer[0] = (byte) ((len >> 8)& 0xFF);
		lenBuffer[1] = (byte) (len & 0xFF);
		write(lenBuffer);
		
		writeHuffmanTree(0, 0, Constants.JPEG_BITS_DC_LUMINANCE, Constants.JPEG_VAL_DC_LUMINANCE);
		writeHuffmanTree(1, 0, Constants.JPEG_BITS_AC_LUMINANCE, Constants.JPEG_VAL_AC_LUMINANCE);
		writeHuffmanTree(0, 1, Constants.JPEG_BITS_DC_CHROMINANCE, Constants.JPEG_VAL_DC_CHROMINANCE);
		writeHuffmanTree(1, 1, Constants.JPEG_BITS_AC_CHROMINANCE, Constants.JPEG_VAL_AC_CHROMINANCE);
	}
	
	private void writeHuffmanTree(int tc, int th, int[] bits, int[] values) throws IOException {
		int preample = 	((tc << 4) + th);
		int[] tmp = new int[bits.length-1];
		for(int i=1;i<bits.length;i++) {
			tmp[i-1] = bits[i];
		}
		write(preample);
		write(tmp);
		write(values);
	}
	
	private void writeScanHeader() throws IOException {
		write(Constants.JFIF_START_OF_SCAN_HEADER);
		write((byte) Constants.JPEG_COMPONENTS);
		writeScanHeaderEntry(Constants.JFIF_COMPONENT_ID_Y, 0, 0);
		writeScanHeaderEntry(Constants.JFIF_COMPONENT_ID_Cb, 1, 1);
		writeScanHeaderEntry(Constants.JFIF_COMPONENT_ID_Cr, 1, 1);
		write(Constants.JFIF_PREAMBLE_SCAN_HEADER);
	}
	
	private void writeScanHeaderEntry(int component, int dcTable, int acTable) throws IOException {
		byte[] entry = new byte[2];
		entry[0] = (byte) component;
		entry[1] = (byte) ((dcTable << 4) + acTable);
		write(entry);
	}
	
	public void writeBlock(byte[] data) {
		checkWriteState();
	}
	
	private void writeEndOfImageMarker() throws IOException {
		checkWriteState();
		write(Constants.JFIF_END_OF_IMAGE_MARKER);
	}
	
	public void write(byte value) throws IOException {
		out.write(value);
	}
	
	public void write(int value) throws IOException {
		write((byte) value);
	}
	
	public void write(byte[] data) throws IOException {
		out.write(data);
	}
	
	public void write(int[] data) throws IOException {
		byte[] result = new byte[data.length];
		for(int i=0;i<data.length;i++) {
			result[i] = (byte) data[i];
		}
		write(result);
	}
	
	private void checkWriteState() {
		if(closed) {
			throw new IllegalStateException("The JFIF Container was already closed, no further writes allowed");
		}
	}
	
	public void close() throws IOException {
		writeEndOfImageMarker();
		out.flush();
		closed = true;
	}
	
	public void writeToFile(String filename) throws IOException {
		if(!closed) {
			close();
		}
		FileOutputStream fout = new FileOutputStream(filename);
		fout.write(getBytes());
		fout.flush();
		fout.close();
	}
	
	public byte[] getBytes() {
		return out.toByteArray();
	}
	
}

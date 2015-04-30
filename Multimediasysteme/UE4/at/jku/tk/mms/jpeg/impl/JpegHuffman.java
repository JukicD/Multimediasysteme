package at.jku.tk.mms.jpeg.impl;

import java.io.IOException;

import at.jku.tk.mms.jpeg.impl.JpegHuffmanTables.HuffmanTable;

public class JpegHuffman {

	private final JFIFContainer container;
	
	private int[] lastDC;
	
	private int buffer;
	
	private int bufferedBits;

	private JpegHuffmanTables tables;
	
	public JpegHuffman(JFIFContainer container, JpegHuffmanTables tables) {
		this.container = container;
		this.tables = tables;
		this.buffer = 0;
		this.bufferedBits = 0;
		this.lastDC = new int[Constants.JPEG_COMPONENTS];
		for(int i=0;i<lastDC.length;i++) {
			lastDC[i] = 0;
		}
	}
	
	public void encodeLuminance(int[] block, int component) throws IOException {
		encode(block, component, tables.getDcLuminance(), tables.getAcLuminance());
	}
	
	public void encodeChrominance(int[] block, int component) throws IOException {
		encode(block, component, tables.getDcChrominance(), tables.getAcChrominance());
	}
	
	private void encode(int[] block, int component, HuffmanTable dc, HuffmanTable ac) throws IOException {
		// the component numbering starts with 1 in JFIF
		component = component - 1;
		// DC: Only the first element in zigzag
		int value = block[0] - lastDC[component];
		int nBits = countBits(value);
		if(value < 0) {
			value--;
		}
		write(dc.getCode(nBits), dc.getSize(nBits));
		if(nBits > 0) {
			write(value, nBits);
		}
		
		// store last DC value for next run
		lastDC[component] = block[0];
		
		// AC: The rest of the zigzag
		int r = 0, x = 0;
		for(int i=1;i<Constants.JPEG_BLOCK_SIZE*Constants.JPEG_BLOCK_SIZE;i++) {
			value = block[i];
			if(value == 0) {
				r++;
			}else{
				// This comment was added in the 2014 edition of the uebung
				while(r > 15) {
					write(ac.getCode(0xF0), ac.getSize(0xF0));
					r -= 16;
				}
				nBits = countBits(value);
				if(value < 0) {
					value --;
				}
				x = (r << 4) + nBits;
				write(ac.getCode(x), ac.getSize(x));
				write(value, nBits);
				r = 0;
				x = 0;
			}
		}
		if(r > 0) {
			write(ac.getCode(0), ac.getSize(0));
		}
		// if we debug we flush and write a marker
//		flush();
//		container.write(Constants.DEBUG_MARKER);
	}
	
	private int countBits(int value) {
		if(value < 0) {
			value = -value;
		}
		int count = 0;
	    while(value != 0) {
	    	count++;
	    	value >>= 1;
		}
	    return count;
	}
	
	private void write(int code, int len) throws IOException {
		code &= (1 << len) -1;
		bufferedBits += len;
		code <<= 24 - bufferedBits;
		buffer |= code;
		
		bufferToStream();
	}

	private void bufferToStream() throws IOException {
		while(bufferedBits >= 8) {
			int c = ((buffer >> 16) & 0xFF);
			container.write(c);
			if(c == 0xFF) {
				container.write(0);
			}
			buffer <<= 8;
			bufferedBits -= 8;
		}
	}

	public void flush() throws IOException {
		bufferToStream();
		
	    if (bufferedBits > 0) {
	    	int c = ((buffer >> 16) & 0xFF);
	    	container.write(c);
	    }
	}
	
}

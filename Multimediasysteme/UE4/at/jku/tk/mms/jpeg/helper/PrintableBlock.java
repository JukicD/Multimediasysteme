package at.jku.tk.mms.jpeg.helper;

import java.text.DecimalFormat;

import at.jku.tk.mms.jpeg.impl.Constants;


/**
 * Wrap a matrix into this class in order to pretty print it
 * 
 * @author matthias
 */
public class PrintableBlock {
	
	private final double[][] block;

	public PrintableBlock(double [][] block) {
		this.block = new double[Constants.JPEG_BLOCK_SIZE][Constants.JPEG_BLOCK_SIZE];
		for(int i=0;i<Constants.JPEG_BLOCK_SIZE;i++) {
			for(int j=0;j<Constants.JPEG_BLOCK_SIZE;j++) {
				this.block[i][j] = block[i][j];
			}
		}
	}
	
	public PrintableBlock(float[][] block) {
		this.block = new double[Constants.JPEG_BLOCK_SIZE][Constants.JPEG_BLOCK_SIZE];
		for(int i=0;i<Constants.JPEG_BLOCK_SIZE;i++) {
			for(int j=0;j<Constants.JPEG_BLOCK_SIZE;j++) {
				this.block[i][j] = block[i][j];
			}
		}
	}
	
	public PrintableBlock(int[] block) {
		this.block = new double[Constants.JPEG_BLOCK_SIZE][Constants.JPEG_BLOCK_SIZE];
		int count = 0;
		for(int i=0;i<Constants.JPEG_BLOCK_SIZE;i++) {
			for(int j=0;j<Constants.JPEG_BLOCK_SIZE;j++) {
				this.block[i][j] = block[count++];
			}
		}
	}

	@Override
	public String toString() {
		return convertToString("", "", "[", "]", ", ", "n");
	}
	
	public String toJavaMatrix() {
		return convertToString("{", "}", "{", "}", ",", ",");
	}
	
	private String convertToString(String start, String end, String startLine, String endLine, String elemDelimiter, String lineDelimiter) {
		DecimalFormat df = new DecimalFormat("#.##");
		StringBuffer buffer = new StringBuffer();
		buffer.append(start);
		for(int x=0;x<block.length;x++) {
			if(x > 0) {
				buffer.append(lineDelimiter);
			}
			buffer.append(startLine);
			for(int y=0;y<block[x].length;y++) {
				if(y > 0) {
					buffer.append(elemDelimiter);
				}
				String tmp = df.format(block[x][y]);
				if("-0".equals(tmp)) {
					tmp = "0";
				}
				buffer.append(tmp);
			}
			buffer.append(endLine);
		}
		buffer.append(end);
		return buffer.toString();
	}
	
}

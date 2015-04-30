package at.jku.tk.mms.jpeg.helper;

import java.util.Formatter;

public class PrintableByteArray {

	private final byte[] data;
	
	private String printable;
	
	public PrintableByteArray(byte[] data) {
		this.data = data;
		this.printable = null;
	}
	
	public PrintableByteArray(int[] data) {
		this.data = new byte[data.length];
		for(int i=0;i<data.length;i++) {
			this.data[i] = (byte) data[i];
		}
	}
	
	private void generatePrintable() {
		StringBuffer buffer = new StringBuffer();
		int counter = 0;
		
	    Formatter formatter = new Formatter(buffer);  
	    for (byte b:data) {
	        formatter.format("%02x", b);
	        counter ++;
	        if(counter % 8 == 0) {
	        	buffer.append(",");
	        }
	        if(counter % 2 == 0) {
	        	buffer.append(" ");
	        }
	        if(counter % 80 == 0) {
	        	buffer.append("n");
	        }
	    }  
	    formatter.close();
		
		printable = buffer.toString();
	}

	@Override
	public String toString() {
		if(printable == null) {
			generatePrintable();
		}
		return printable;
	}
	
}

package at.jku.tk.mms.jpeg.impl;

public class JpegHuffmanTables {
	
	public class HuffmanTable {
		
		private int[] codes;
		
		private int[] sizes;
		
		public HuffmanTable(int[] bits, int[] vals, int tblSize) {
			int[] huffSize = new int[257];
			int[] huffCode = new int[257];
			int count = 0;
			for(int i=1;i<=16;i++) {
				for(int j=1;j<=bits[i];j++) {
					huffSize[count++] = i;
				}
			}
			huffSize[count] = 0;
			int lastCount = count;
			int code = 0;
			int size = 0;
			count = 0;
			while(huffSize[count] != 0) {
				while(huffSize[count] == size) {
					huffCode[count++] = code;
					code++;
				}
				code <<= 1;
				size++;
			}
			codes = new int[tblSize];
			sizes = new int[tblSize];
			for(int i=0;i<lastCount;i++) {
				codes[vals[i]] = huffCode[i];
				sizes[vals[i]] = huffSize[i];
			}
		}
		
		public int getLength() {
			return codes.length;
		}
		
		public int getCode(int i) {
			return codes[i];
		}
		
		public int getSize(int i) {
			return sizes[i];
		}
		
	}

	private HuffmanTable dcLuminance;
	
	private HuffmanTable acLuminance;
	
	private HuffmanTable dcChrominance;
	
	private HuffmanTable acChrominance;
	
	public JpegHuffmanTables() {
		dcLuminance = new HuffmanTable(Constants.JPEG_BITS_DC_LUMINANCE, Constants.JPEG_VAL_DC_LUMINANCE, 12);
		acLuminance = new HuffmanTable(Constants.JPEG_BITS_AC_LUMINANCE, Constants.JPEG_VAL_AC_LUMINANCE, 255);
		dcChrominance = new HuffmanTable(Constants.JPEG_BITS_DC_CHROMINANCE, Constants.JPEG_VAL_DC_CHROMINANCE, 12);
		acChrominance = new HuffmanTable(Constants.JPEG_BITS_AC_CHROMINANCE, Constants.JPEG_VAL_AC_CHROMINANCE, 255);
		
	}

	public HuffmanTable getDcLuminance() {
		return dcLuminance;
	}

	public HuffmanTable getAcLuminance() {
		return acLuminance;
	}

	public HuffmanTable getDcChrominance() {
		return dcChrominance;
	}

	public HuffmanTable getAcChrominance() {
		return acChrominance;
	}
	
}

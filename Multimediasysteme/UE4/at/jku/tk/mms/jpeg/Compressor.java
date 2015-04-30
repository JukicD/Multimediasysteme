package at.jku.tk.mms.jpeg;

import java.io.IOException;

import at.jku.tk.mms.jpeg.impl.Constants;
import at.jku.tk.mms.jpeg.impl.DiscreteCosinusTransformation;
import at.jku.tk.mms.jpeg.impl.JFIFContainer;
import at.jku.tk.mms.jpeg.impl.JpegHuffman;
import at.jku.tk.mms.jpeg.impl.JpegHuffmanTables;
import at.jku.tk.mms.jpeg.impl.RGBImage;
import at.jku.tk.mms.jpeg.impl.SubsampledYCCImage;
import at.jku.tk.mms.jpeg.impl.YCCImage;
import at.jku.tk.mms.jpeg.impl.ZigZag;

public class Compressor {

	private final String infile;
	
	private final String outfile;
	
	private DiscreteCosinusTransformation dct;
	
	private JpegHuffmanTables tables;
	
	public Compressor(String infile, String outfile) {
		this.infile = infile;
		this.outfile = outfile;
		
		dct = new DiscreteCosinusTransformation();
		tables = new JpegHuffmanTables();
	}

	public void compress() throws IOException {
		float[][] inputBlock;
		int[] quantized;
		int[] zigzag;
		// Loading
		RGBImage rgb = new RGBImage(infile);
		// Color Space Conversion
		YCCImage ycc = new YCCImage(rgb);
		// Sub Sampling
		SubsampledYCCImage subsampled = new SubsampledYCCImage(ycc);
		// Prepare JFIF Container
		String comment = "JPEG Encoder Multimedia Systeme - Matthias Steinbauer";
		JFIFContainer jfif = new JFIFContainer(dct, comment, rgb.getWidth(), rgb.getHeight());
		// Prepare Huffman
		JpegHuffman huffman = new JpegHuffman(jfif, tables);
		// Iterate over Blocks
		for(int y=0;y<subsampled.getBlocksHigh();y++) {
			for(int x=0;x<subsampled.getBlocksWide();x++) {
				// Luminance Y
				inputBlock = subsampled.getYBlock(x, y);
				quantized = dct.dctQuantLuminance(inputBlock);
				zigzag = ZigZag.zigzag(quantized);
				huffman.encodeLuminance(zigzag, Constants.JFIF_COMPONENT_ID_Y);
				// Chrominance Cb
				inputBlock = subsampled.getCbBlock(x, y);
				quantized = dct.dctQuantChrominance(inputBlock);
				zigzag = ZigZag.zigzag(quantized);
				huffman.encodeChrominance(zigzag, Constants.JFIF_COMPONENT_ID_Cb);
				// Chrominance Cr
				inputBlock = subsampled.getCrBlock(x, y);
				quantized = dct.dctQuantChrominance(inputBlock);
				zigzag = ZigZag.zigzag(quantized);
				huffman.encodeChrominance(zigzag, Constants.JFIF_COMPONENT_ID_Cr);
			}
		}
		huffman.flush();
		// Write to file
		jfif.writeToFile(outfile);
	}

}

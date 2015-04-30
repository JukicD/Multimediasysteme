package at.jku.tk.mms.jpeg;

import java.io.IOException;

public class App {
	
	public static void usage() {
		System.err.println("at.jku.tk.mms.jpeg.App <input-file> <output-file>");
		System.exit(-1);
	}
	
	public static void main(String[] args) throws IOException {
		if(args.length < 2 || args.length > 2) {
			System.err.println("You did not specify valid command line parameters");
			usage();
		}else{
			String infile = args[0].trim();
			String outfile = args[1].trim();
			if(!outfile.toLowerCase().endsWith(".jpg")) {
				System.err.println("Outfile needs to end in .jpg");
				usage();
			}
			Compressor comp = new Compressor(infile, outfile);
			comp.compress();
		}
	}
	
}

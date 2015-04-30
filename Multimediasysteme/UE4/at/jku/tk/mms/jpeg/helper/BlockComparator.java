package at.jku.tk.mms.jpeg.helper;

public class BlockComparator {

	private final PrintableBlock before;
	private final PrintableBlock after;
	private final PrintableBlock reference;

	public BlockComparator(PrintableBlock before, PrintableBlock after, PrintableBlock reference) {
		this.before = before;
		this.after = after;
		this.reference = reference;
	}

	@Override
	public String toString() {
		String[] before = this.before.toString().split("n");
		String[] after = this.after.toString().split("n");
		String[] reference = this.reference.toString().split("n");
		StringBuffer buffer = new StringBuffer();
		
		for(int i=0;i<before.length;i++) {
			buffer.append(before[i]);
			buffer.append("t");
			buffer.append(after[i]);
			buffer.append("t");
			buffer.append(reference[i]);
			buffer.append("n");
		}
		
		return buffer.toString();
	}

	public boolean ok() {
		String refererence = this.reference.toString();
		String after = this.after.toString();
		return after.equals(refererence);
	}
	
}

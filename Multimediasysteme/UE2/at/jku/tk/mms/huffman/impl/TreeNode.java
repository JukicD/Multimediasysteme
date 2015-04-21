package at.jku.tk.mms.huffman.impl;

import java.util.Map;

/**
 * Represents a single treenode of the Huffman Tree.
 * May also get used to represent frequencies in the frequency table.
 * 
 * @author matthias
 */
public class TreeNode implements Comparable<TreeNode> {

	private byte value;
	
	private int frequency;
	
	private TreeNode left, right;
	
	public TreeNode(TreeNode left, TreeNode right)
	{
		this.left = left;
		this.right = right;
		this.frequency = left.getFreq() + right.getFreq();
	}
	
	/** Creates new TreeNode .. must get initialized with value */
	public TreeNode(byte value) {
		this(value, 0, null, null);
	}
	
	/** Creates new TreeNode with full init */
	public TreeNode(byte value, int frequency, TreeNode left, TreeNode right) {
		this.value = value;
		this.frequency = frequency;
		this.left = left;
		this.right = right;
	}
	
	/** increment frequency */
	public void incFrequency() {
		this.frequency++;
	}
	
	/** getter Frequency */
	public int getFreq() {
		return frequency;
	}
	
	/** getter nodes value */
	public Byte getValue() {
		return value;
	}
	
	/**
	 * @return left childnode
	 */
	public TreeNode getLeft() {
		return left;
	}
	
	/**
	 * @return right childnode
	 */
	public TreeNode getRight() {
		return right;
	}
	
	/** determine if this node is a leaf node */
	public boolean isLeaf() {
		return left == null && right == null;
	}
	
	/** Iterator method for lookup init */
	public void initLookup(byte myCode, int myDepth, Map<Byte, BitCode> lookupTable) {
		if(isLeaf()) {
			BitCode code = new BitCode(myCode, myDepth);
			lookupTable.put(value, code);
		}else{
			if(right != null) {
				right.initLookup((byte) ((myCode << 1) +1), myDepth +1, lookupTable);
			}
			if(left != null) {
				left.initLookup((byte) ((myCode << 1)), myDepth +1, lookupTable);
			}
		}
	}

	@Override
	public int compareTo(TreeNode o) {
		return frequency - o.frequency;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("{v: ");
		buffer.append(value);
		buffer.append(", f: ");
		buffer.append(frequency);
		buffer.append("}");
		
		return buffer.toString();
	}
	
	/** Output method for algorithm debugging */
	public void printSubTree(int tabs) {
		for(int i=0;i<tabs;i++) {
			System.out.print('t');
		}
		System.out.println(this);
		if(left != null) {
			left.printSubTree(tabs + 1);
		}
		if(right != null) {
			right.printSubTree(tabs + 1);
		}
	}

	public void setLeft(TreeNode left) {
		this.left = left;
	}

	public void setRight(TreeNode right) {
		this.right = right;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	
	
	
	
}

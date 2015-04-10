package at.jku.tk.mms.huffman.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains the huffman tree
 * 
 * @author matthias
 */
public class HuffmanTree {
	
	private TreeNode root;
	
	private Map<Byte, BitCode> lookup;

	/** Initialize HuffmanTree with FrequencyTable */
	public HuffmanTree(FreqencyTable ftable) {
		if(ftable == null) {
			throw new IllegalArgumentException("Cannot initialize Huffman Tree with null frequency table");
		}
		List<TreeNode> t = ftable.getTable();
		if(t.size() == 0) {
			throw new IllegalArgumentException("Cannot initialzie Huffman Tree with empty frequency table");
		}
		initializeTree(t);
		initializeLookup();
	}
	
	/** Initialize the huffman tree */
	private void initializeTree(List<TreeNode> tree) {
/* 		@TODO Place your implementation here		 */
		root = new TreeNode((byte)63);
		
		TreeNode first = tree.get(0);
		TreeNode second = tree.get(1);
		
		if(first.getValue() <= second.getValue())
		{
			root.setLeft(first);
			root.setRight(second);
		
		}else
		{
			root.setLeft(second);
			root.setRight(first);
		}
		root.setFrequency(root.getLeft().getFreq() + root.getRight().getFreq());
		
		for(TreeNode node : tree)
		{
			TreeNode dummy = new TreeNode(root.getValue());
			if(root.getFreq() <= node.getFreq())
			{
				dummy.setLeft(root);
				dummy.setRight(node);
				
			}else
			{
				dummy.setRight(root);
				dummy.setLeft(node);
			}
			
			if(!dummy.isLeaf())
			{
				dummy.setFrequency(dummy.getLeft().getFreq() + dummy.getRight().getFrequency());
			}
			
			dummy = root;
		}
	}

	/** Iterate over tree and init lookup */
	private void initializeLookup() {
		lookup = new HashMap<Byte, BitCode>();
		root.initLookup((byte) 0, 0, lookup);
	}
	
	public TreeNode getRootNode() {
		return root;
	}
	
	public Map<Byte, BitCode> getLookupTable() {
		return lookup;
	}
	
	/** Debug print tree */
	public void printTree() {
		root.printSubTree(0);
	}
	
	/** debug print lookup table */
	public void printLookupTable() {
		for(Byte cur : lookup.keySet()) {
			BitCode code = lookup.get(cur);
			char curChar = (char) (int) cur;
			System.out.println("value: " + cur + " (char) " + curChar + " encoded as " + code);
		}
	}
	
}

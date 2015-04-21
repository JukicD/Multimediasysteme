package at.jku.tk.mms.huffman.impl;

import java.util.Collections;
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
		
		while(tree.size() > 1)
		{
			TreeNode first = tree.get(0);
			TreeNode second = tree.get(1);
		
			if(first.getFreq() <= second.getFreq())
			{	
				root = new TreeNode(first, second);
			}else
			{	
				root = new TreeNode(second, first);
			}
			
			tree.remove(first);
			tree.remove(second);
			tree.add(root);
			
			Collections.sort(tree);
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
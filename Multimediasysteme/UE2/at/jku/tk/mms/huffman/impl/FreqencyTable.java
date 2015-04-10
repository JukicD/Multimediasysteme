package at.jku.tk.mms.huffman.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages a List of Treenodes during the process of Tree creation
 * 
 * @author matthias
 */
public class FreqencyTable {
	
	private Map<Byte, TreeNode> table;
	
	private ArrayList<TreeNode> sortedNodes;

	/** Initialize this frequency table from byte array */
	public FreqencyTable(byte[] ipt) {
		this.table = new HashMap<Byte, TreeNode>();
		for(byte value : ipt) {
			add(value);
		}
	}

	/** Add a value to this Table */
	public void add(byte v) {
		sortedNodes = null;
/* 		@TODO Place your implementation here*/
		
		if(table.containsKey(v))
		{
			table.get(v).incFrequency();
		}else
		{
			table.put(v, new TreeNode(v, 1, null, null));
		}
	}
	
	/** Sorts table and returns */
	public List<TreeNode> getTable() {
		if(sortedNodes == null) {
			sortedNodes = new ArrayList<TreeNode>(table.values());
			Collections.sort(sortedNodes);
		}
		return sortedNodes;
	}
	
	/** get frequency of single value */
	public int getFrequency(byte b) {
		return table.get(b).getFreq();
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		for(TreeNode node : getTable()) {
			if(buffer.length() > 0) {
				buffer.append(", ");
			}
			buffer.append(node);
		}
		
		buffer.insert(0, "[");
		buffer.append("]");
		
		return buffer.toString();
	}
	
}

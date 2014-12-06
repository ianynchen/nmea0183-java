package com.antu.nmea.sentence;

import java.util.HashMap;

public class MultiSentenceTable {
	
	static private MultiSentenceTable Instance = new MultiSentenceTable(); 
	
	private HashMap<String, Boolean> table = new HashMap<String, Boolean>();

	private MultiSentenceTable() {
		
		table.put("TUT", true);
	}

	static public MultiSentenceTable instance() {
		return MultiSentenceTable.Instance;
	}
	
	public boolean isMultiSentenceType(String sentenceType) {
		return this.table.containsKey(sentenceType.toUpperCase());
	}
	
	public void registerType(String sentenceType) {
		this.table.put(sentenceType.toUpperCase(), true);
	}
}

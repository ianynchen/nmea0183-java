package com.antu.nmea.codec;

import java.util.HashMap;

public class AcceptanceList {

	private HashMap<String, Boolean> publicAcceptanceList;
	private HashMap<String, Boolean> proprietaryAcceptanceList;
	private HashMap<String, HashMap<Integer, Boolean>> messageAcceptanceList;
	
	public AcceptanceList() {
		this.publicAcceptanceList = new HashMap<String, Boolean>();
		this.proprietaryAcceptanceList = new HashMap<String, Boolean>();
		this.messageAcceptanceList = new HashMap<String, HashMap<Integer, Boolean>>();
	}
	
	public void addPublicSentence(String sentenceType) {
		if (!this.publicAcceptanceList.containsKey(sentenceType)) {
			this.publicAcceptanceList.put(sentenceType.toUpperCase(), true);
		}
	}
	
	public void addProprietarySentence(String sentenceType) {
		if (!this.proprietaryAcceptanceList.containsKey(sentenceType)) {
			this.proprietaryAcceptanceList.put(sentenceType.toUpperCase(), true);
		}
	}
	
	public void addMessage(String sentenceType, int messageId) {
		
		if (!this.messageAcceptanceList.containsKey(sentenceType.toUpperCase())) {
			this.messageAcceptanceList.put(sentenceType.toUpperCase(), new HashMap<Integer, Boolean>());
		}
		
		if (!this.messageAcceptanceList.get(sentenceType.toUpperCase()).containsKey(messageId)) {
			this.messageAcceptanceList.get(sentenceType.toUpperCase()).put(messageId, true);
		}
		
		if (!this.publicAcceptanceList.containsKey(sentenceType.toUpperCase())) {
			this.publicAcceptanceList.put(sentenceType.toUpperCase(), true);
		}
	}
	
	public void addAllMessages(String sentenceType) {
		for (int i = 1; i <= 100; i++) {
			this.addMessage(sentenceType, i);
		}
	}
	
	public void removePublicSentence(String sentenceType) {
		if (this.publicAcceptanceList.containsKey(sentenceType)) {
			this.publicAcceptanceList.remove(sentenceType);
		}
		
		if (this.messageAcceptanceList.containsKey(sentenceType)) {
			this.messageAcceptanceList.remove(sentenceType);
		}
	}
	
	public void removeProprietarySentence(String sentenceType) {
		if (this.proprietaryAcceptanceList.containsKey(sentenceType)) {
			this.proprietaryAcceptanceList.remove(sentenceType);
		}
	}
	
	public void removeMessage(String sentenceType, int messageId) {
		if (this.messageAcceptanceList.containsKey(sentenceType)) {
			
			HashMap<Integer, Boolean> messages = this.messageAcceptanceList.get(sentenceType);
			
			if (messages.containsKey(messageId)) {
				messages.remove(messageId);
				
				if (messages.size() == 0 && this.publicAcceptanceList.containsKey(sentenceType)) {
					this.publicAcceptanceList.remove(sentenceType);
				}
			}
		}
	}
	
	public void removeAllMessages(String sentenceType) {
		if (this.publicAcceptanceList.containsKey(sentenceType)) {
			this.publicAcceptanceList.remove(sentenceType);
		}
		
		if (this.messageAcceptanceList.containsKey(sentenceType)) {
			this.messageAcceptanceList.remove(sentenceType);
		}
	}
	
	public boolean shouldAcceptPublic(String sentenceType) {
		return this.publicAcceptanceList.containsKey(sentenceType.toUpperCase());
	}
	
	public boolean shouldAcceptProprietary(String sentenceType) {
		return this.proprietaryAcceptanceList.containsKey(sentenceType.toUpperCase());
	}
	
	public boolean shouldAcceptMessage(String sentenceType, int messageId) {
		return this.messageAcceptanceList.containsKey(sentenceType.toUpperCase()) &&
				this.messageAcceptanceList.get(sentenceType.toUpperCase()).containsKey(messageId);
	}
}

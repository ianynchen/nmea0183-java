package com.antu.nmea.source;

import java.util.List;

public class MessageSetting {
	
	private String sentenceType;
	
	private List<Integer> messages;

	public MessageSetting() {
	}

	public String getSentenceType() {
		return sentenceType;
	}

	public void setSentenceType(String sentenceType) {
		this.sentenceType = sentenceType;
	}

	public List<Integer> getMessages() {
		return messages;
	}

	public void setMessages(List<Integer> messages) {
		this.messages = messages;
	}

}

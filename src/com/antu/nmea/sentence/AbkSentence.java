package com.antu.nmea.sentence;

import java.util.Date;

import com.antu.nmea.annotation.SentenceField;

public class AbkSentence extends ParametricSentence {

	public AbkSentence() {
		super();
	}

	public AbkSentence(Date date) {
		super(date);
	}

	public AbkSentence(long currentTimeSinceEpochInSeconds) {
		super(currentTimeSinceEpochInSeconds);
	}

	@Override
	public String sentenceType() {
		return "ABK";
	}

	@SentenceField(order = 1, fieldType = "integer")
	public int mmsi = 0;
	
	@SentenceField(order = 2, fieldType = "char")
	public char channel = 'A';
	
	@SentenceField(order = 3, fieldType = "integer")
	public int messageId;
	
	@SentenceField(order = 4, fieldType = "integer")
	public int messageSequenceNumber;
	
	@SentenceField(order = 5, fieldType = "integer")
	public int typeOfAcknowledgement;
}

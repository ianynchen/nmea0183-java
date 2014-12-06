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
		return "abk";
	}

	@SentenceField(order = 1, fieldType = "integer", isRequired = false, fieldWidth = 9)
	public Integer mmsi = null;
	
	@SentenceField(order = 2, fieldType = "char", isRequired = false)
	public Character channel = null;
	
	@SentenceField(order = 3, fieldType = "integer")
	public int messageId;
	
	@SentenceField(order = 4, fieldType = "integer")
	public int messageSequenceNumber;
	
	@SentenceField(order = 5, fieldType = "integer")
	public int typeOfAcknowledgement;
}

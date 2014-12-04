package com.antu.nmea.sentence;

import java.util.Date;

import com.antu.nmea.annotation.SentenceField;
import com.antu.nmea.util.UtcTime;

public class AcsSentence extends ParametricSentence {

	public AcsSentence() {
	}

	public AcsSentence(Date date) {
		super(date);
	}

	public AcsSentence(long currentTimeSinceEpochInSeconds) {
		super(currentTimeSinceEpochInSeconds);
	}

	@Override
	public String sentenceType() {
		return "ACS";
	}

	@SentenceField(order = 1, fieldType = "integer")
	public int sequenceNumber;
	
	@SentenceField(order = 2, fieldType = "integer")
	public int originatorMMSI;
	
	@SentenceField(order = 3, fieldType = "time")
	public UtcTime timeOfReceipt;
	
	@SentenceField(order = 4, fieldType = "integer", width = 2)
	public int day;
	
	@SentenceField(order = 5, fieldType = "integer", width = 2)
	public int month;
	
	@SentenceField(order = 6, fieldType = "integer")
	public int year;
}
